package me.spencerwang.aiguille.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.util.HashMap;

/**
 * Created by SpencerWang on 2015/4/28.
 */
public class ImageLoadUtil {

    private static HashMap<String, SoftReference<Bitmap>> mImageCache = new HashMap<String, SoftReference<Bitmap>>();
    /**
    *  A interface that callback when image load finish
    */
    public interface OnLoadImageFinishListener{
        public void onLoadFinish(Bitmap bitmap);
    }
    /**
    *
    *  Note:load image
    *
    *  @param imageView     not null
    *  @param netUrl
    *  @param localUrl        local image buffer file
    *  @param  defaultImage   set imageview default darwable
    *  @param  compressSize     default image compress size
    *
    */

    public static void loadImageAsync(ImageView imageView,String netUrl,String localUrl,Drawable defaultImage,int compressSize){
        Bitmap bitmap = null;
        if(imageView != null && netUrl != null){
            SoftReference<Bitmap> cache = mImageCache.get(netUrl);
            if(cache != null){
                bitmap = cache.get();
            }

            if(bitmap != null){
                imageView.setImageBitmap(bitmap);
            }else{
                TaskUtil.executeAsyncTask(new ImageLoadTask(),imageView,netUrl,localUrl,defaultImage,compressSize);
            }

        }

    }


    /**
     *
     *  Note:load image
     *
     *  @param imageView     not null
     *  @param netUrl
     *  @param localUrl        local image buffer file
     *  @param  defaultImage   set imageview default darwable
     *  @param  compressSize     default image compress size
     *  @param listener
     */

    public static void loadImageAsync(ImageView imageView,String netUrl,String localUrl,Drawable defaultImage,int compressSize,OnLoadImageFinishListener listener){
        Bitmap bitmap = null;
        if(imageView != null && netUrl != null){
            SoftReference<Bitmap> cache = mImageCache.get(netUrl);
            if(cache != null){
                bitmap = cache.get();
            }

            if(bitmap != null){
                imageView.setImageBitmap(bitmap);
                if(listener != null){
                    listener.onLoadFinish(bitmap);
                }
            }else{
                TaskUtil.executeAsyncTask(new ImageLoadTask(),imageView,netUrl,localUrl,defaultImage,compressSize);
            }
        }

    }


    private static class ImageLoadTask extends AsyncTask<Object,Object,Bitmap> {
        private ImageView imageView;
        private String url;
        private String localPath;
        private Drawable defaultImage;
        private int compressSize = 50;
        private OnLoadImageFinishListener listener;
        private Bitmap mBitmap;

        @Override
        protected Bitmap doInBackground(Object... params) {
            imageView = (ImageView) params[0];

            if (params.length > 1) {
                url = (String) params[1];
            }
            if (params.length > 2) {
                localPath = (String) params[2];
            }
            if (params.length > 3) {
                defaultImage = (Drawable) params[3];
            }
            if (params.length > 4) {
                compressSize = (Integer) params[4];
            }
            if (params.length > 5) {
                listener = (OnLoadImageFinishListener) params[5];
            }

            if (localPath != null && url != null) {
                File file = new File(localPath + MD5(url));
                if (file.exists()) {
                    try {
                        FileInputStream stream = new FileInputStream(file);
                        mBitmap = BitmapFactory.decodeStream(stream);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }

            if (mBitmap == null && url != null) {
                if (localPath == null) {
                    try {
                        mBitmap = BitmapFactory.decodeStream(new URL(url).openStream());
                        if (mBitmap != null) {
                            mImageCache.put(url, new SoftReference<Bitmap>(mBitmap));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    mBitmap = donwloadImageFromUrl(url, localPath + MD5(url), compressSize);
                }
            }
            return mBitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null) {
                imageView.setImageBitmap(bitmap);
                if (listener != null) {
                    listener.onLoadFinish(bitmap);
                }
            } else if (defaultImage != null) {
                imageView.setImageDrawable(defaultImage);
            }
            bitmap = null;
        }


        private static Bitmap donwloadImageFromUrl(String urls, String path, int compress) {
            Bitmap bmp = null;
            FileOutputStream fsOut = null;
            InputStream is = null;

            URL url = null;
            try {
                url = new URL(urls);
            } catch (MalformedURLException e1) {
                e1.printStackTrace();
            }

            if(url != null)
            {
                File flLocal = new File(path);
                if(!flLocal.exists())
                {
                    File parent = flLocal.getParentFile();
                    if(parent != null)
                        parent.mkdirs();
                }
                try
                {
                    is = url.openStream();
                    if(is != null)
                    {
                        fsOut = new FileOutputStream(flLocal);

                        byte[] buf = new byte[10240];
                        int len = -1;
                        while((len  = is.read(buf)) != -1)
                        {
                            if(len > 0)
                                fsOut.write(buf, 0, len);
                        }
                    }
                }
                catch (FileNotFoundException e2) {
                    e2.printStackTrace();
                }
                catch(IOException e)
                {
                    e.printStackTrace();
                }
                if(is != null)
                {
                    try
                    {
                        is.close();
                    }
                    catch(IOException e)
                    {
                        e.printStackTrace();
                    }
                }

                if(fsOut != null)
                {
                    try
                    {
                        fsOut.close();
                    }
                    catch(IOException e)
                    {
                        e.printStackTrace();
                    }
                }

                BitmapFactory.Options opt = new BitmapFactory.Options();
                opt.inJustDecodeBounds = true;
                opt.outHeight = 0;
                opt.outWidth = 0;
                BitmapFactory.decodeFile(path, opt);
                if(opt.outWidth > 0 && opt.outHeight > 0)
                {
                    int sampleSize = 1;
                    if(opt.outWidth >= opt.outHeight && opt.outHeight > compress)
                    {
                        sampleSize = opt.outHeight / compress;
                    }
                    else if(opt.outWidth < opt.outHeight && opt.outWidth > compress)
                    {
                        sampleSize = opt.outWidth / compress;
                    }

                    opt = new BitmapFactory.Options();
                    opt.inSampleSize = 1;
                    opt.outHeight = opt.outHeight * 3;
                    bmp = BitmapFactory.decodeFile(path, opt);
                    if(bmp != null)
                    {
                        mImageCache.put(urls, new SoftReference<Bitmap>(bmp));
                        try
                        {
                            fsOut = new FileOutputStream(flLocal);
                            bmp.compress(Bitmap.CompressFormat.PNG, 90, fsOut);
                        }
                        catch(IOException e)
                        {
                            e.printStackTrace();
                        }
                        if(fsOut != null)
                        {
                            try
                            {
                                fsOut.close();
                            }
                            catch(IOException e)
                            {
                                e.printStackTrace();
                            }
                        }
                    }

                }
            }
            return bmp;

        }

        /**
         * MD5加密字符串
         *
         * @param str
         * @return
         */
        public static String MD5(String str) {
            MessageDigest md5 = null;
            try {
                md5 = MessageDigest.getInstance("MD5");
            } catch (Exception e) {
                e.printStackTrace();
                return "";
            }

            char[] charArray = str.toCharArray();
            byte[] byteArray = new byte[charArray.length];
            for (int i = 0; i < charArray.length; i++) {
                byteArray[i] = (byte) charArray[i];
            }

            byte[] md5Bytes = md5.digest(byteArray);
            StringBuffer hexValue = new StringBuffer();
            for (int i = 0; i < md5Bytes.length; i++) {
                int val = ((int) md5Bytes[i]) & 0xff;
                if (val < 16) {
                    hexValue.append("0");
                }
                hexValue.append(Integer.toHexString(val));
            }
            return hexValue.toString();
        }

    }
}
