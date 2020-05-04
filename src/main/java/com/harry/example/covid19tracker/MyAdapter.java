package com.harry.example.covid19tracker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> implements Filterable {
    private Context context;
    private List<CountryModal>countryModalList;
    private List<CountryModal> copyCountryModalList;
    private static  int count;
    private DataFromRecyclerView dataFromRecyclerView;
    private HashMap<String,Bitmap> bitmapHashMap=new HashMap<>();
    private boolean isVisible;
    private boolean dataBinded;
//    private CountryModal countryModal;
//    public MyAdapter(Context context,CountryModal countryModal){
//        this.context=context;
//        this.countryModal=countryModal;
//    }
    public MyAdapter(Context context,List<CountryModal> countryModalList){
        this.context=context;
        this.countryModalList=countryModalList;
        this.copyCountryModalList=countryModalList;
        this.dataFromRecyclerView=(AffectedCountries)context;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(context);
        View view=layoutInflater.inflate(R.layout.countries_layout,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
            final CountryModal countryModal=countryModalList.get(position);
            final String country_name=countryModal.getCountryName();
            String image_url=countryModal.getImage_url();
            holder.countryName.setText(country_name);
            Bitmap bitmap=CacheImage.getImage(context,country_name);
            if(bitmap != null){
                holder.countryImage.setImageBitmap(bitmap);
                holder.progressBar.setVisibility(View.GONE);
                holder.countryImage.setVisibility(View.VISIBLE);
            }
            else if(bitmapHashMap.containsKey(country_name)){
                holder.countryImage.setImageBitmap(bitmapHashMap.get(country_name));
                holder.progressBar.setVisibility(View.GONE);
                holder.countryImage.setVisibility(View.VISIBLE);
            }else {
                ImageDownloader imageDownloader = new ImageDownloader(holder.countryImage, holder.progressBar, country_name);
                imageDownloader.execute(image_url);
            }
            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(isVisible){
                        RelativeLayout relativeLayout=holder.view.findViewById(R.id.relative_layout);
                        relativeLayout.setVisibility(View.GONE);
                        isVisible=false;
                        return;
                    }
                        dataFromRecyclerView.dataFromRecycler(country_name);
                }
            });
            holder.view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v){
                    RelativeLayout relativeLayout=holder.view.findViewById(R.id.relative_layout);
                    if(!isVisible) {
                            TextView cases = relativeLayout.findViewById(R.id.textview_cases);
                            TextView todayCases = relativeLayout.findViewById(R.id.textview_today_cases);
                            TextView deaths = relativeLayout.findViewById(R.id.textview_deaths);
                            TextView todayDeaths = relativeLayout.findViewById(R.id.textview_today_deaths);
                            TextView recovered = relativeLayout.findViewById(R.id.textview_recoverd);
                            TextView active = relativeLayout.findViewById(R.id.textview_active);
                            TextView critical = relativeLayout.findViewById(R.id.textview_critical);
                            cases.setText(countryModal.getCases());
                            todayCases.setText(countryModal.getTodayCases());
                            deaths.setText(countryModal.getDeaths());
                            todayDeaths.setText(countryModal.getTodayDeaths());
                            recovered.setText(countryModal.getRecovered());
                            active.setText(countryModal.getActive());
                            critical.setText(countryModal.getCritical());
                        relativeLayout.setVisibility(View.VISIBLE);
                        isVisible = true;
                    }
                    return true;
                }
            });
    }

    @Override
    public int getItemCount() {
        return countryModalList.size();
    }

    @Override
    public Filter getFilter() {
        Filter filter=new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults=new FilterResults();
                List<CountryModal> filteredName=new ArrayList<>();
                if(constraint == null || constraint.length() == 0){
                    filteredName.addAll(copyCountryModalList);
                }else{
                    String filterPattern = constraint.toString().toLowerCase();
                    for(CountryModal countryModal:copyCountryModalList){
                        if((countryModal.getCountryName().toLowerCase()).contains(filterPattern)){
                            filteredName.add(countryModal);
                        }
                    }
                }
                filterResults.values=filteredName;
                filterResults.count=filteredName.size();
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                countryModalList=(List<CountryModal>)results.values;
                notifyDataSetChanged();
            }
        };
        return filter;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public View view;
        public ImageView countryImage;
        public TextView countryName;
        public ProgressBar progressBar;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.view=itemView;
            countryImage=itemView.findViewById(R.id.countryImage);
            countryName=itemView.findViewById(R.id.countryName);
            progressBar=itemView.findViewById(R.id.progress_bar);

        }
    }
    private  class ImageDownloader extends AsyncTask<String,Bitmap,Bitmap>{
        private ImageView imageView;
        private ProgressBar progressBar;
        private String country_name;
        private ImageDownloader(ImageView imageView,ProgressBar progressBar,String country_name){
            this.imageView=imageView;
            this.progressBar=progressBar;
            this.country_name=country_name;
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            Log.i("TAG", "doInBackground: "+(count++));
            Bitmap bitmap=null;
            HttpURLConnection httpURLConnection=null;
            InputStream inputStream=null;
            try {
                URL url=new URL(strings[0]);
                httpURLConnection=(HttpURLConnection)url.openConnection();
                inputStream=httpURLConnection.getInputStream();
                bitmap= BitmapFactory.decodeStream(inputStream);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                if(httpURLConnection != null){
                    httpURLConnection.disconnect();
                }if(inputStream != null){
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            try {
                Thread.sleep(400);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return  bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            progressBar.setVisibility(View.GONE);
            imageView.setImageBitmap(bitmap);
            imageView.setVisibility(View.VISIBLE);
            bitmapHashMap.put(country_name,bitmap);
            CacheImage.putImage(context,country_name,bitmap);
            progressBar=null;
            imageView=null;
        }
    }
    public interface DataFromRecyclerView{
        void dataFromRecycler(String countryName);
    }
}
