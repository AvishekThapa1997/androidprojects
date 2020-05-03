package com.harry.example.covid19tracker;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> implements Filterable {
    private Context context;
    private List<CountryModal>countryModalList;
    private List<CountryModal> copyCountryModalList;

    private DataFromRecyclerView dataFromRecyclerView;
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
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
            holder.countryImage.setImageBitmap(countryModalList.get(position).getCountryImage());
            holder.countryName.setText(countryModalList.get(position).getCountryName());
            holder.view.setTag(countryModalList.get(position).getCountryName());
            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        dataFromRecyclerView.dataFromRecycler(countryModalList.get(position).getCountryName());
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
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.view=itemView;
            countryImage=itemView.findViewById(R.id.countryImage);
            countryName=itemView.findViewById(R.id.countryName);

        }
    }
    public interface DataFromRecyclerView{
        void dataFromRecycler(String countryName);
    }
}
