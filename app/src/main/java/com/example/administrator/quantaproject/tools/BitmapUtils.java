package com.example.administrator.quantaproject.tools;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Administrator on 2017/5/20.
 */

//BitmapUtils工具类
public class BitmapUtils {


    /*** 该方法用于将图片进行圆形处理* */
    public static Bitmap circleBitmap(Bitmap source) {
        //默认只对宽进行处理
        int width = source.getWidth();
        Bitmap bitmap = Bitmap.createBitmap(width, width, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();    //设置抗锯齿
        paint.setAntiAlias(true);
        canvas.drawCircle(width / 2, width / 2, width / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(source, 0, 0, paint);
        return bitmap;
    }

    /**
     * 该方法用于图片压缩处理,注意width、height参数的类型必须是float   *
     */

    public static Bitmap zoom(Bitmap source, float width, float height) {
        Matrix matrix = new Matrix();    //图片进行压缩处理
        matrix.postScale(width / source.getWidth(), height / source.getHeight());
        Bitmap bitmap = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, false);
        return bitmap;
    }


    //保存图片
    public static void saveImage(Context context,Bitmap bitmap,String fileName) {
        File file;

        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){//判断sd卡是否挂载
            //路径1：storage/sdcard/Android/data/包名/files
            Log.i("SAVE_IMAGE","SAVING_SDCARD");
            file = context.getExternalFilesDir("");

        }else{//手机内部存储
            //路径：data/data/包名/files
            Log.i("SAVE_IMAGE","SAVING_PHONE");
            file = context.getFilesDir();

        }
        FileOutputStream fos = null;
        try {
            final File files = new File(file,fileName+".png");
            fos = new FileOutputStream(files);

            bitmap.compress(Bitmap.CompressFormat.PNG, 100,fos);
            Log.i("SAVE_IMAGE_SUCCESS",fileName+".png");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }finally{
            if(fos != null){
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        Log.i("FILE",file.getPath());

    }

    /**
     * 按照一定的宽高比例裁剪图片
     * @param bitmap 要裁剪的图片
     * @param num1 长边的比例
     * @param num2 短边的比例
     * @param isRecycled 是否回收原图片
     * @return 裁剪后的图片
     */
    @org.jetbrains.annotations.Contract("null, _, _, _ -> null")
    public static Bitmap  imageCrop(Bitmap bitmap, int num1, int num2, boolean isRecycled){
        if (bitmap == null){
            return null;
        }
        int w = bitmap.getWidth(); // 得到图片的宽，高
        int h = bitmap.getHeight();
        int retX, retY;
        int nw, nh;
        if (w > h){
            if (h > w * num2 / num1){
                nw = w;
                nh = w * num2 / num1;
                retX = 0;
                retY = (h - nh) / 2;
            } else{
                nw = h * num1 / num2;
                nh = h;
                retX = (w - nw) / 2;
                retY = 0;
            }
        } else{
            if (w > h * num2 / num1){
                nh = h;
                nw = h * num2 / num1;
                retY = 0;
                retX = (w - nw) / 2;
            } else{
                nh = w * num1 / num2;
                nw = w;
                retY = (h - nh) / 2;
                retX = 0;}
        }
        Bitmap bmp = Bitmap.createBitmap(bitmap, retX, retY, nw, nh, null,false);
        if (isRecycled && bitmap != null && !bitmap.equals(bmp)&& !bitmap.isRecycled()){
            bitmap.recycle();//回收原图片
            bitmap = null;
        }
        return bmp;
    }

    public static boolean readImage(ImageView view,Context context,String name) {
        File filesDir;
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){/**判断sd卡是否挂载*/
            /**路径1：storage/sdcard/Android/data/包名/files*/
            filesDir = context.getExternalFilesDir("");

        }else{/**手机内部存储   路径：data/data/包名/files*/
            filesDir = context.getFilesDir();

        }
        File file = new File(filesDir,name + ".png");
        if(file.exists()){
            /**存储--->内存*/
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            view.setImageBitmap(bitmap);
            return true;
        }
        return false;
    }
}