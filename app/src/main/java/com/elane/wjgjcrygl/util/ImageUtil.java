package com.elane.wjgjcrygl.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;

/**
 * Created by zgbf on 2017/2/15.
 */

public class ImageUtil {
    public static String byteArray2Str(byte[] byteArray){
        return new String(byteArray);
    }

    public static byte[] str2ByteArray(String str){
        return str.getBytes();
    }

    public static byte[] bitmap2ByteArray(Bitmap bitmap) throws Exception {
        byte[] datas = null;
        ByteArrayOutputStream baos = null;
        try{
            baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            datas = baos.toByteArray();
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            baos.close();
        }
        return datas;
    }

    public static Bitmap byteArray2Bitmap(byte[] bitmapData) throws Exception{
        return BitmapFactory.decodeByteArray(bitmapData,0,bitmapData.length);
    }
}
