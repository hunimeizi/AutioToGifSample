package com.video.to.gif.sample;


import android.Manifest;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.joyrun.gifcreator.FFmpegExecutor;
import com.joyrun.gifcreator.FFmpegUtils;

import java.io.File;
import java.lang.ref.WeakReference;

import come.haolin_android.mvp.baselibrary.base.BaseActivity;
import come.haolin_android.mvp.baselibrary.base.ConstantObj;
import come.haolin_android.mvp.baselibrary.imagepicker.GlideImageLoader;
import come.haolin_android.mvp.baselibrary.imagepicker.ImagePicker;
import come.haolin_android.mvp.baselibrary.utils.FileUtil;
import come.haolin_android.mvp.baselibrary.view.ToolAlertDialog;
import ru.alexbykov.nopermission.PermissionHelper;

public class MainActivity extends BaseActivity implements View.OnClickListener{

    private PermissionHelper permissionHelper;
    private ToolAlertDialog toolAlertDialog;
    private static final int NOT_NOTICE = 2;//如果勾选了不再询问
    private ImageView imageView;
    private File outGifDir;//gif输出文件夹
    private TextView tv_dirGif;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initViews() {
        setTitleBarView();
        titleBar_title_tv.setText("video转gif");
        Button btn_chooseAudio = findViewById(R.id.btn_chooseAudio);
        btn_chooseAudio.setOnClickListener(this);
        imageView = findViewById(R.id.image_gif);
        tv_dirGif = findViewById(R.id.tv_dirGif);
        ImagePicker.getInstance().imageLoader(new GlideImageLoader());
        outGifDir = new File(Environment.getExternalStorageDirectory() + "/video_gif");
        if (!outGifDir.exists()) {
            if (outGifDir.mkdir()) {
                outGifDir = Environment.getExternalStorageDirectory();
            }
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_chooseAudio) {
            permissionHelper = new PermissionHelper(this);
            permissionHelper.check(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                    .onSuccess(this::onSuccess).onDenied(this::onDenied).onNeverAskAgain(this::onNeverAskAgain).run();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ImagePicker.getInstance().onActivityResult(requestCode, resultCode, data);
        if (requestCode == NOT_NOTICE) {
            permissionHelper.check(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                    .onSuccess(this::onSuccess).onDenied(this::onDenied).onNeverAskAgain(this::onNeverAskAgain).run();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        permissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void onSuccess() {
        ImagePicker.getInstance()
                .multiMode(false) //多选
                .showCamera(false)
                .crop(false)
                .selectedListener(items -> {
                    execute(items.get(0).getImageUrl());
                })
                .startImagePicker(this);
    }

    /**
     * 自己写异步asyncTask  直接调用executeFFmpegCommond
     *
     * @param imageUrl
     */
    private void execute(String imageUrl) {
        showLoadingDialog();
        GifCreateAsyncTask myAsyncTask = new GifCreateAsyncTask(this);
        myAsyncTask.execute(imageUrl,outGifDir.getAbsolutePath());
    }
    /**
     * 异步调用jni生成GIF方法
     */
    private class GifCreateAsyncTask extends AsyncTask<String, Integer, String> {

        private WeakReference<MainActivity> weakReference;

        GifCreateAsyncTask(MainActivity activity) {
            Glide.with(activity).clear(activity.imageView);
            this.weakReference = new WeakReference<>(activity);
        }


        @Override
        protected String doInBackground(String... strings) {
            String inputFilePath = strings[0];
            String outGifDir = strings[1];

            MediaMetadataRetriever mmr = new MediaMetadataRetriever();
            mmr.setDataSource(inputFilePath);

            int width = Integer.parseInt(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH));
            int height = Integer.parseInt(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));
            int videoHeight;
            int videoWidth;

            if (width > height) {
                // 横屏视屏
                videoWidth = 1080;
                videoHeight = (int) ((float) videoWidth / width * height);
            } else {
                // 竖屏视频
                videoHeight = 1080;
                videoWidth = (int) ((float) videoHeight / height * width);
            }


            String gifFile = outGifDir + File.separator + "VideoToGif" + System.currentTimeMillis() + ".gif";

            String[] command = FFmpegUtils.extractVideoFramesToGIF(
                    inputFilePath,
                    0,
                    20,
                    videoWidth,
                    videoHeight,
                    10,
                    gifFile);
            FFmpegExecutor fFmpegExecutor = new FFmpegExecutor();
            fFmpegExecutor.executeFFmpegCommond(command);
            return gifFile;
        }

        @Override
        protected void onPostExecute(String gifFile) {
            super.onPostExecute(gifFile);
            final MainActivity mainActivity = weakReference.get();
            if (mainActivity != null) {
                dismissLoadingDialog();
                tv_dirGif.setText("转换成功，gif图片路径为："+gifFile);
                Toast.makeText(mainActivity, "转换成功", Toast.LENGTH_SHORT).show();
                Glide.with(mainActivity).asGif().load(new File(gifFile)).into(mainActivity.imageView);
            }
        }
    }



    private void onDenied() {
        if (toolAlertDialog == null) {
            toolAlertDialog = new ToolAlertDialog(mContext);
        }
        toolAlertDialog.showAlertDialog("权限申请", getResources().getString(R.string.ensureNormalOperation)
                        + getResources().getString(R.string.app_name) + getResources().getString(R.string.pleaseAllowStoreAndCam), "去允许",
                view -> {
                    toolAlertDialog.dismissAlertDialog();
                    permissionHelper.check(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                            .onSuccess(this::onSuccess).onDenied(this::onDenied).onNeverAskAgain(this::onNeverAskAgain).run();
                }, "取消", view -> toolAlertDialog.dismissAlertDialog(), false);
    }

    private void onNeverAskAgain() {
        if (toolAlertDialog == null) {
            toolAlertDialog = new ToolAlertDialog(mContext);
        }
        toolAlertDialog.showAlertDialog(mContext.getString(R.string.permissionRequest), mContext.getString(R.string.ensureNormalOperation) + getString(R.string.app_name) + mContext.getString(R.string.pleaseAllow), mContext.getString(R.string.goToAllow), view -> {
            toolAlertDialog.dismissAlertDialog();
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", getPackageName(), null);//注意就是"package",不用改成自己的包名
            intent.setData(uri);
            startActivityForResult(intent, NOT_NOTICE);
        }, "取消", view -> toolAlertDialog.dismissAlertDialog(), false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FileUtil.deleteDirWihtFile(new File(Environment.getExternalStorageDirectory().getPath() + "/Android/data/" + mContext.getPackageName() + "/ImagePicker/cropTemp/"));
        FileUtil.deleteDirWihtFile(new File(ConstantObj.BASE_CACHE_PATH + mContext.getPackageName() + "/ImagePicker"));
        FileUtil.deleteDirWihtFile(new File(ConstantObj.BASE_CACHE_PATH + mContext.getPackageName() + "/cache/compress_cache"));
    }
}
