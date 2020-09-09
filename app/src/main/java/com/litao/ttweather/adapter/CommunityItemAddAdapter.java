package com.litao.ttweather.adapter;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Toast;

import com.litao.ttweather.R;
import com.litao.ttweather.activity.LookPhotoGalleryActivity;
import com.litao.ttweather.activity.UserInformationActivity;
import com.litao.ttweather.alximageloader.SelectPhotoActivity;
import com.litao.ttweather.tool.Constants;
import com.litao.ttweather.tool.ImageTools;
import com.litao.ttweather.tool.ImageUtils;
import com.litao.ttweather.tool.UtilMethod;

import java.util.ArrayList;

public class CommunityItemAddAdapter extends BaseAdapter {
    private static final String TAG = "CommunityItemAddAdapter";
    private ArrayList<String> list_path;
    private Activity mActivity;
    private int width;
    private int imageWidth;

    public CommunityItemAddAdapter(Activity mActivity, ArrayList<String> list_path) {
        this.list_path = list_path;
        this.mActivity = mActivity;
        width = mActivity.getWindowManager().getDefaultDisplay().getWidth();

        imageWidth = (width - UtilMethod.dp2px(mActivity, 20) - 10) / 3;
    }

    @Override
    public int getCount() {
        return list_path.size() + 1;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View v1 = LayoutInflater.from(mActivity).inflate(R.layout.gv_photo, null);
        ImageView img = (ImageView) v1.findViewById(R.id.imageView1);

        img.setLayoutParams(new LayoutParams(imageWidth, imageWidth));
        if (position != list_path.size()) {
            new ImageTools().setImageWithNoHandle(list_path.get(position), img);
            //ImageUtils.getImageFromUrl(list_path.get(position), img);
            img.setOnLongClickListener(new View.OnLongClickListener() {

                @Override
                public boolean onLongClick(View v) {
                    list_path.remove(position);
                    notifyDataSetChanged();
                    return true;
                }

            });


        } else {
            img.setImageResource(R.drawable.add_image);
            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ContextCompat.checkSelfPermission(mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            mActivity.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
                        }
                        Toast.makeText(mActivity,
                                "请给存储权限", Toast.LENGTH_SHORT).show();
                    } else {
                        choosePicture();
                    }
                }
            });
        }
        return v1;
    }

    /**
     * 选取图片
     */
    private void choosePicture() {
        if (!ImageTools.checkSDCardAvailable()) {
            Toast.makeText(mActivity, "内存卡错误,请检查您的内存卡", Toast.LENGTH_SHORT).show();
            return;
        }
//        Intent intentphoto = new Intent(Intent.ACTION_PICK);
//        intentphoto.setDataAndType(
//                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
//                Constants.IMAGE_UNSPECIFIED);
//        mActivity.startActivityForResult(intentphoto, Constants.PHOTOZOOM);
        Intent intent = new Intent(mActivity, SelectPhotoActivity.class);
        mActivity.startActivityForResult(intent, 10);

    }
}
