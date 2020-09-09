package com.litao.ttweather.tool;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.widget.ImageView;

/**
 * ͼƬ�������� Tools for handler picture
 *
 * @author Ryan.Tang
 */
public final class ImageTools {

    /**
     * Transfer drawable to bitmap
     *
     * @param drawable
     * @return
     */
    ImageView img_photo;
    String strpath;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            Bitmap bitmap = (Bitmap) msg.obj;
            if (bitmap == null) ;
            else {
                img_photo.setBackgroundColor(Color.TRANSPARENT);
                img_photo.setImageBitmap(UtilMethod.toRoundBitmap(bitmap));
            }
        }

        ;
    };
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            Bitmap bitmap = (Bitmap) msg.obj;
            if (bitmap == null) ;
            else {
                img_photo.setBackgroundColor(Color.TRANSPARENT);
                img_photo.setImageBitmap(bitmap);
            }
        }

        ;
    };


    public static Bitmap drawableToBitmap(Drawable drawable) {
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();

        Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Config.ARGB_8888
                : Config.RGB_565;
        Bitmap bitmap = Bitmap.createBitmap(w, h, config);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        drawable.draw(canvas);
        return bitmap;
    }

    /**
     * Bitmap to drawable
     *
     * @param bitmap
     * @return
     */
    public static Drawable bitmapToDrawable(Bitmap bitmap) {
        return new BitmapDrawable(bitmap);
    }

    /**
     * Input stream to bitmap
     *
     * @param inputStream
     * @return
     * @throws Exception
     */
    public static Bitmap inputStreamToBitmap(InputStream inputStream)
            throws Exception {
        return BitmapFactory.decodeStream(inputStream);
    }

    /**
     * Byte transfer to bitmap
     *
     * @param byteArray
     * @return
     */
    public static Bitmap byteToBitmap(byte[] byteArray) {
        if (byteArray.length != 0) {
            return BitmapFactory
                    .decodeByteArray(byteArray, 0, byteArray.length);
        } else {
            return null;
        }
    }

    /**
     * Byte transfer to drawable
     *
     * @param byteArray
     * @return
     */
    public static Drawable byteToDrawable(byte[] byteArray) {
        ByteArrayInputStream ins = null;
        if (byteArray != null) {
            ins = new ByteArrayInputStream(byteArray);
        }
        return Drawable.createFromStream(ins, null);
    }

    /**
     * Bitmap transfer to bytes
     *
     * @param byteArray
     * @return
     */
    public static byte[] bitmapToBytes(Bitmap bm) {
        byte[] bytes = null;
        if (bm != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
            bytes = baos.toByteArray();
        }
        return bytes;
    }

    /**
     * Drawable transfer to bytes
     *
     * @param drawable
     * @return
     */
    public static byte[] drawableToBytes(Drawable drawable) {
        BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
        Bitmap bitmap = bitmapDrawable.getBitmap();
        byte[] bytes = bitmapToBytes(bitmap);
        ;
        return bytes;
    }

    /**
     * Base64 to byte[] //
     */
    // public static byte[] base64ToBytes(String base64) throws IOException {
    // byte[] bytes = Base64.decode(base64);
    // return bytes;
    // }
    //
    // /**
    // * Byte[] to base64
    // */
    // public static String bytesTobase64(byte[] bytes) {
    // String base64 = Base64.encode(bytes);
    // return base64;
    // }

    /**
     * Create reflection images
     *
     * @param bitmap
     * @return
     */
    public static Bitmap createReflectionImageWithOrigin(Bitmap bitmap) {
        final int reflectionGap = 4;
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        Matrix matrix = new Matrix();
        matrix.preScale(1, -1);

        Bitmap reflectionImage = Bitmap.createBitmap(bitmap, 0, h / 2, w,
                h / 2, matrix, false);

        Bitmap bitmapWithReflection = Bitmap.createBitmap(w, (h + h / 2),
                Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmapWithReflection);
        canvas.drawBitmap(bitmap, 0, 0, null);
        Paint deafalutPaint = new Paint();
        canvas.drawRect(0, h, w, h + reflectionGap, deafalutPaint);

        canvas.drawBitmap(reflectionImage, 0, h + reflectionGap, null);

        Paint paint = new Paint();
        LinearGradient shader = new LinearGradient(0, bitmap.getHeight(), 0,
                bitmapWithReflection.getHeight() + reflectionGap, 0x70ffffff,
                0x00ffffff, TileMode.CLAMP);
        paint.setShader(shader);
        // Set the Transfer mode to be porter duff and destination in
        paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
        // Draw a rectangle using the paint with our linear gradient
        canvas.drawRect(0, h, w, bitmapWithReflection.getHeight()
                + reflectionGap, paint);

        return bitmapWithReflection;
    }

    /**
     * ��ȡԲ��ͼƬ Get rounded corner images
     *
     * @param bitmap
     * @param roundPx 5 10
     * @return
     */
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Bitmap output = Bitmap.createBitmap(w, h, Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, w, h);
        final RectF rectF = new RectF(rect);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    /**
     * ���û������»�ͼ Resize the bitmap
     *
     * @param bitmap
     * @param width��Ļ�Ŀ�
     * @param height��Ļ   �ĸ�
     * @return
     */
    public static Bitmap zoomBitmap(Bitmap bitmap, int width, int height) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Matrix matrix = new Matrix();
        float scaleWidth = ((float) width / w);
        float scaleHeight = ((float) height / h);
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
        return newbmp;
    }

    /**
     * �ı�ͼƬ�ĳߴ� Resize the drawable
     *
     * @param drawable
     * @param w
     * @param h
     * @return
     */
    public static Drawable zoomDrawable(Drawable drawable, int w, int h) {
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap oldbmp = drawableToBitmap(drawable);
        Matrix matrix = new Matrix();
        float sx = ((float) w / width);
        float sy = ((float) h / height);
        matrix.postScale(sx, sy);
        Bitmap newbmp = Bitmap.createBitmap(oldbmp, 0, 0, width, height,
                matrix, true);
        return new BitmapDrawable(newbmp);
    }

    /**
     * Get images from SD card by path and the name of image
     *
     * @param photoName
     * @return
     */
    public static Bitmap getPhotoFromSDCard(String path, String photoName) {
        Bitmap photoBitmap = BitmapFactory.decodeFile(path + "/" + photoName
                + ".jpg");
        if (photoBitmap == null) {
            return null;
        } else {
            return photoBitmap;
        }
    }

    /**
     * ���sd���Ƿ���� Check the SD card
     *
     * @return
     */
    public static boolean checkSDCardAvailable() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }

    /**
     * �ж��Ƿ���ͼƬ Get image from SD card by path and the name of image
     *
     * @param fileName
     * @return
     */
    public static boolean findPhotoFromSDCard(String path, String photoName) {
        boolean flag = false;

        if (checkSDCardAvailable()) {
            File dir = new File(path);
            if (dir.exists()) {
                File folders = new File(path);
                File photoFile[] = folders.listFiles();
                for (int i = 0; i < photoFile.length; i++) {
                    String fileName = photoFile[i].getName().split("\\.")[0];
                    if (fileName.equals(photoName)) {
                        flag = true;
                    }
                }
            } else {
                flag = false;
            }
            // File file = new File(path + "/" + photoName + ".jpg" );
            // if (file.exists()) {
            // flag = true;
            // }else {
            // flag = false;
            // }

        } else {
            flag = false;
        }
        return flag;
    }

    /**
     * Save image to the SD card
     *
     * @param photoBitmap
     * @param photoName
     * @param path
     */
    public static void savePhotoToSDCard(Bitmap photoBitmap, String path,
                                         String photoName) {
        if (checkSDCardAvailable()) {
            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            File photoFile = new File(path, photoName + ".png");
            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(photoFile);
                if (photoBitmap != null) {
                    // png����ѹ�� �ڶ�����ûӰ��
                    if (photoBitmap.compress(Bitmap.CompressFormat.PNG, 100,
                            fileOutputStream)) {

                        fileOutputStream.flush();
                    }
                }
            } catch (FileNotFoundException e) {
                photoFile.delete();
                e.printStackTrace();
            } catch (IOException e) {
                photoFile.delete();
                e.printStackTrace();
            } finally {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * ɾ��·���µ�����ͼƬ Delete the image from SD card
     *
     * @param context
     * @param path    file:///sdcard/temp.jpg
     */
    public static void deleteAllPhoto(String path) {
        if (checkSDCardAvailable()) {
            File folder = new File(path);
            File[] files = folder.listFiles();
            for (int i = 0; i < files.length; i++) {
                files[i].delete();
            }
        }
    }

    /**
     * ����·��������ɾ��ͼƬ
     *
     * @param path
     * @param fileName
     */
    public static void deletePhotoAtPathAndName(String path, String fileName) {
        if (checkSDCardAvailable()) {
            File folder = new File(path);
            File[] files = folder.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i].getName().split("\\.")[0].equals(fileName)) {
                    files[i].delete();
                }
            }
        }
    }

    // ��URIת��Ϊ��ʵ·��
    @SuppressWarnings("deprecation")
    public static String changeUriToPath(Uri uri, Activity activity) {
        String uriStr = uri.getScheme();
        System.out.println("uri scheme:" + uriStr);
        if (uriStr.indexOf("file") != -1) {
            System.out.println("uri scheme is file");
            return uri.getPath();
        }

        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor actualImageCursor = activity.managedQuery(uri, proj, null, null,
                null);

        int actual_image_column_index = actualImageCursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        actualImageCursor.moveToFirst();
        String currentImagePath = actualImageCursor
                .getString(actual_image_column_index);
        return currentImagePath;
    }

    /**
     * ����ͼƬ�Ŀ�� ���� ͼƬ  ���ص�һ����20Kb����
     *
     * @param path ͼƬ��·��
     * @return
     */
    public static Bitmap scacleToBitmap(String path, Activity activity
    ) {
        int scale = 1;
        BitmapFactory.Options op = new BitmapFactory.Options();
        op.inJustDecodeBounds = true;//����Ϊtrue ���������أ�����Ҫ�ڴ棬ֻ��Ϊ�˻�ȡͼƬ�Ŀ���
        BitmapFactory.decodeFile(path, op);//��ʱ���ز��������ڴ棬ֻ�ǻ�ȡop
        scale = computeSampleSize(op, 320, 320 * 480);
        op.inJustDecodeBounds = false;
        op.inSampleSize = scale;// �������Խ��,ͼƬ��СԽС. ���Ϊԭ����4��һ����С16��֮1
        return BitmapFactory.decodeFile(path, op);
    }


    /**
     * ��ȡѹ���ı���
     *
     * @param options
     * @param minSideLength
     * @param maxNumOfPixels
     * @return
     */
    public static int computeSampleSize(BitmapFactory.Options options,
                                        int minSideLength, int maxNumOfPixels) {
        int initialSize = computeInitialSampleSize(options, minSideLength,
                maxNumOfPixels);

        int roundedSize;
        if (initialSize <= 8) {
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }

        return roundedSize;
    }

    private static int computeInitialSampleSize(BitmapFactory.Options options,
                                                int minSideLength, int maxNumOfPixels) {
        double w = options.outWidth;
        double h = options.outHeight;

        int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
                .sqrt(w * h / maxNumOfPixels));
        int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(Math
                .floor(w / minSideLength), Math.floor(h / minSideLength));

        if (upperBound < lowerBound) {
            // return the larger one when there is no overlapping zone.
            return lowerBound;
        }

        if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
            return 1;
        } else if (minSideLength == -1) {
            return lowerBound;
        } else {
            return upperBound;
        }
    }

    public void get(final String url, ImageView img) {
        img_photo = img;
        new Thread() {
            public void run() {
                Message message = new Message();
                message.obj = getbitmap(url);
                if (message.obj == null) {
                    mHandler.sendMessage(message);
                } else {
                }
            }
        }.start();


    }

    public void setImage(final String url, ImageView img) {
        img_photo = img;
        new Thread() {
            public void run() {
                Message message = new Message();
                message.obj = getbitmap(url);
                handler.sendMessage(message);
            }
        }.start();

    }
    public void setImageWithNoHandle(final String url, ImageView img) {
        img_photo = img;
        new Thread() {
            public void run() {
                Message message = new Message();
                message.obj = getbitmapWithNoHandle(url);
                handler.sendMessage(message);
            }
        }.start();

    }

    public Bitmap getbitmap(String url) {
        Bitmap bm = null;
        if (url.contains("http://")) {
            try {
                InputStream in;
                URL u = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) u.openConnection();
                in = new BufferedInputStream(connection.getInputStream());
                bm = BitmapFactory.decodeStream(in);
                connection.disconnect();
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return null;
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return null;
            }
        } else if (url != null) {
            bm = BitmapFactory.decodeFile(url);
            System.out.println("����ͼ");
        } else {
            return null;
        }

        return UtilMethod.toRoundBitmap(bm);
    }
    public Bitmap getbitmapWithNoHandle(String url) {
        Bitmap bm = null;
        if(url==null) return bm;
        if (url.contains("http://")) {
            try {
                InputStream in;
                URL u = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) u.openConnection();
                in = new BufferedInputStream(connection.getInputStream());
                bm = BitmapFactory.decodeStream(in);
                connection.disconnect();
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return null;
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return null;
            }
        } else if (url != null) {
            bm = BitmapFactory.decodeFile(url);
            System.out.println("����ͼ");
        } else {
            return null;
        }

        return bm;
    }
    public void downLoad(final String urlstr) {
        new Thread() {
            public void run() {
                try {
                    URL url = new URL(urlstr);
                    //�򿪵�url������
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    //����Ϊjava IO���֣�������˵�����ȼ���ļ����Ƿ���ڣ��������򴴽�,Ȼ����ļ����ظ����⣬û�п���
                    InputStream istream = connection.getInputStream();
                    File file = new File(Environment.getExternalStorageDirectory() + "/sima/photo" + "/1.png");
                    if (file.exists()) {
                        file.delete();
                        file.createNewFile();
                    }
                    file.createNewFile();
                    OutputStream output = new FileOutputStream(file);
                    byte[] buffer = new byte[1024 * 4];
                    while (istream.read(buffer) != -1) {
                        output.write(buffer);
                    }
                    output.flush();
                    output.close();
                    istream.close();
                    //���toast���ļ�������Ϊ��������ǵ��̵߳ģ�����Ҫ�������ļ��Ժ�Ż�ִ����һ�䣬�м��ʱ���������������������̻߳�û��ѧ��
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("����������������������");
                }
            }

            ;
        }.start();
    }
}
