package com.harry.example.covid19tracker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class CacheImage {
    public static void putImage(Context context, String countryName,Bitmap bitmap){
        String fileName=context.getCacheDir()+"/"+countryName+".jpg";
        File file=new File(fileName);
        FileOutputStream fileOutputStream=null;
        try {
            fileOutputStream=new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG,50,fileOutputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }finally {
            if(fileOutputStream != null){
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
    public static Bitmap getImage(Context context,String countryName){
        String fileName=context.getCacheDir()+"/"+countryName+".jpg";
        File file=new File(fileName);
        FileInputStream fileInputStream=null;
        Bitmap bitmap=null;
        try {
            fileInputStream=new FileInputStream(file);
            bitmap= BitmapFactory.decodeStream(fileInputStream);
            return bitmap;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }finally {
            if(fileInputStream != null){
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
