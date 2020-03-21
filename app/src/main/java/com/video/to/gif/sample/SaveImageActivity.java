package com.video.to.gif.sample;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.video.to.gif.sample.model.SharePicModel;
import com.video.to.gif.sample.utils.GeneratePictureManager;

import come.haolin_android.mvp.baselibrary.base.BaseActivity;
import come.haolin_android.mvp.baselibrary.utils.ToastUtils;

public class SaveImageActivity extends BaseActivity {
    private Button mStartBtn;
    private ImageView mResultIv;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_save_image;
    }

    @Override
    protected void initViews() {
        setTitleBarView();
        titleBar_title_tv.setText("布局转图片");
        mStartBtn = findViewById(R.id.start_btn);
        mResultIv = findViewById(R.id.result_iv);
        mStartBtn.setOnClickListener(v -> {
            showLoadingDialog();
            generate();
        });
    }

    private void generate() {
        SharePicModel sharePicModel = new SharePicModel((ViewGroup) getWindow().getDecorView());
        sharePicModel.setAvatarResId(R.mipmap.dkjg);
        // 需要保存 放开下面注释
//        sharePicModel.setSavePath(Environment.getExternalStorageDirectory() + "/video_gif");
        GeneratePictureManager.getInstance().generate(sharePicModel, (throwable, bitmap) -> {
            if (throwable != null || bitmap == null) {
                ToastUtils.showToast(mContext,"转换失败");
            } else {
                ToastUtils.showToast(mContext,"转换成功");
                mResultIv.setImageBitmap(bitmap);
//                Uri uri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, null,null));
//                Intent intent = new Intent();
//                intent.setAction(Intent.ACTION_SEND);//设置分享行为
//                intent.setType("image/*");//设置分享内容的类型
//                intent.putExtra(Intent.EXTRA_STREAM, uri);
//                intent = Intent.createChooser(intent, "分享");
//                startActivity(intent);
            }
            dismissLoadingDialog();
        });
    }
}
