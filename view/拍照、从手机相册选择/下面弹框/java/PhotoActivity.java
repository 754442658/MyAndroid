package com.sxrecruit.aaaa;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.view.View;

import com.module.base.BaseActivity;
import com.module.dialog.PopSelectPhoto;
import com.module.utils.L;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by ShiShow_xk on 2017/9/6.
 * 从相册选择照片/拍照作为头像上传
 */

public abstract class PhotoActivity extends BaseActivity {
    // 拍照
    public final int PHOTO_CAMERA = 1885;
    // 相册
    public final int PHOTO_PICTURE = 4678;
    // 选择结束的照片
    public File pic;

    // 图片的存储地址
    public File picPath = new File(PhotoBitmapUtils.getPhoneRootPath(this) + "/sxzp/pic");
    // 点击选择图片弹出的选择器
    public PopSelectPhoto pop;


    public View.OnClickListener itemClickLitener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            pop.dismiss();
            // 每次都清楚之前存储的照片
            if (!picPath.exists()) {
                picPath.mkdirs();
            } else {
                // 删除文件夹下的所有文件
                deleteDir(picPath);
            }
            // 设置图片存储路径
            PhotoBitmapUtils.setPicPath("/sxzp/pic");
            // 设置图片名
            PhotoBitmapUtils.setPicName("pic" + new SimpleDateFormat("yyyy-MM-ddHH:mm:ss:SSS", Locale.CHINA).format(new Date()));

            if (v.getId() == com.module.R.id.view_camero_rl_takephoto) {
                // 拍照
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                File img = new File(PhotoBitmapUtils.getPhotoFileName(PhotoActivity.this));
                Uri uri = null;
                // 判断版本
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    // 因为 intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION) 只在5.0以上的版本有效，但是当前设置为7.0以上采用
                    i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    //改变Uri  com.xykj.customview.fileprovider注意和xml中的一致
                    uri = FileProvider.getUriForFile(PhotoActivity.this, "sxzp.fileprovider", img);
                } else {
                    uri = Uri.fromFile(img);
                }
                i.putExtra(MediaStore.EXTRA_OUTPUT, uri);

                startActivityForResult(i, PHOTO_CAMERA);

            } else if (v.getId() == com.module.R.id.view_camero_rl_selectphoto) {
                // 手机相册选择
                Intent i2 = new Intent(Intent.ACTION_PICK, null);
                i2.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(i2, PHOTO_PICTURE);
            }
        }
    };


    /**
     * android 7.0裁剪照片
     *
     * @param context
     * @param imageFile
     * @return
     */
    public static Uri getImageContentUri(Context context, File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID},
                MediaStore.Images.Media.DATA + "=? ",
                new String[]{filePath}, null);

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return context.getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }


    /**
     * @param uri
     */
    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        // 判断版本
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setDataAndType(getImageContentUri(this, new File(PhotoBitmapUtils.getPhotoFileName(this))), "image/*");
        } else {
            intent.setDataAndType(uri, "image/*");
        }
        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 250);
        intent.putExtra("outputY", 250);
        intent.putExtra("return-data", true);
        //TODO 输入预裁剪图片的Uri，指定以后，可以通过这个Uri获得图片
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(PhotoBitmapUtils.getPhotoFileName(this))));
        startActivityForResult(intent, 9461);
    }


    /**
     * 选择照片或者拍摄照片后，回到当前界面执行代码
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PHOTO_PICTURE:
                // 手机相册选择
                if (resultCode == Activity.RESULT_OK) {
                    startPhotoZoom(data.getData());
                }
                break;
            case PHOTO_CAMERA:
                // 图片地址
                String path = PhotoBitmapUtils.getPhotoFileName(this);
                // 图片文件
                File img = new File(path);
                Uri uri = null;
                if (resultCode == Activity.RESULT_OK) {
                    // 判断版本
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        //改变Uri  com.xykj.customview.fileprovider注意和xml中的一致
                        uri = FileProvider.getUriForFile(PhotoActivity.this, "sxzp.fileprovider", img);
                    } else {
                        uri = Uri.fromFile(img);
                    }

                    startPhotoZoom(uri);
                }
                break;
            case 9461:
                pic = new File(PhotoBitmapUtils.getPhotoFileName(this));
                selectPic(pic);
                break;
            default:
                break;
        }
    }

    //删除文件夹和文件夹里面的文件
    public static void deleteDir(final File dir) {
        if (dir == null || !dir.exists() || !dir.isDirectory())
            return;
        for (File file : dir.listFiles()) {
            if (file.isFile())
                file.delete(); // 删除所有文件
            else if (file.isDirectory())
                deleteDir(file); // 递规的方式删除文件夹
        }
    }

    public abstract void selectPic(File photo);
}
