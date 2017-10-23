package com.sxrecruit.aaaa;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;

import com.module.base.BaseActivity;
import com.module.utils.L;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import me.nereo.multi_image_selector.MultiImageSelector;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;

/**
 * Created by ShiShow_xk on 2017/9/6.
 * 从相册选择照片/拍照作为头像上传
 */

public abstract class SinglePhotoActivity extends BaseActivity {
    // 选择照片
    private int take_photo = 1002;
    // 裁剪照片
    private int cut_photo = 1003;

    // 裁剪后的照片文件
    public File picPath = new File(PhotoBitmapUtils.getPhoneRootPath(this) + "/sxzp/pic");


    // SD卡读写权限和拍照权限
    public String[] mPermission = {
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    // 裁剪的宽高
    int width, height;

    /**
     * 选择照片
     */
    public void choicePhoto() {
        callBack = new CallBack() {
            @Override
            public void hasPermission(boolean hasPermission) {
                if (hasPermission) {
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

                    MultiImageSelector.create()
                            .showCamera(true) // 是否显示相机. 默认为显示
                            .count(9) // 最大选择图片数量, 默认为9. 只有在选择模式为多选时有效
                            .single() // 单选模式
//                .multi() // 多选模式, 默认模式;
//                .origin(ArrayList < String >)// 默认已选择图片. 只有在选择模式为多选时有效
                            .start(SinglePhotoActivity.this, take_photo);
                }
            }
        };
        request_code = 1001;
        getPermission(mPermission, request_code);
    }

    /**
     * 选择照片
     * 带尺寸的裁剪
     */
    public void choicePhoto(int width, int height) {
        this.width = width;
        this.height = height;
        callBack = new CallBack() {
            @Override
            public void hasPermission(boolean hasPermission) {
                if (hasPermission) {
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

                    MultiImageSelector.create()
                            .showCamera(true) // 是否显示相机. 默认为显示
                            .count(9) // 最大选择图片数量, 默认为9. 只有在选择模式为多选时有效
                            .single() // 单选模式
//                .multi() // 多选模式, 默认模式;
//                .origin(ArrayList < String >)// 默认已选择图片. 只有在选择模式为多选时有效
                            .start(SinglePhotoActivity.this, take_photo);
                }
            }
        };
        request_code = 1001;
        getPermission(mPermission, request_code);
    }


    /**
     * 选择照片或者拍摄照片后，回到当前界面执行代码
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == take_photo) {
            if (resultCode == RESULT_OK) {
                // 获取返回的图片列表
                List<String> path = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                // 选择、拍照成功
                L.e("选择照片结束");
                File file = new File(path.get(0));
                Uri uri = null;
                // 判断版本
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    startPhotoZoom(null, file);
                } else {
                    uri = Uri.fromFile(file);
                    startPhotoZoom(uri, null);
                }
            }
        } else if (requestCode == cut_photo) {
            if (resultCode == RESULT_OK) {
                // 裁剪照片成功
                L.e("裁剪照片成功");
                selectPic(new File(PhotoBitmapUtils.getPhotoFileName(this)));
            }
        }
    }

    /**
     * 裁剪照片
     *
     * @param uri
     */
    public void startPhotoZoom(Uri uri, File file) {
        L.e("开始裁剪照片");
        Intent intent = new Intent("com.android.camera.action.CROP");
        // 判断版本
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setDataAndType(getImageContentUri(this, file), "image/*");
        } else {
            intent.setDataAndType(uri, "image/*");
        }

        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        if (width != 0) {
            if (width / height == 16 / 9) {
                // aspectX aspectY 是宽高的比例
                intent.putExtra("aspectX", 16);
                intent.putExtra("aspectY", 9);
                // outputX outputY 是裁剪图片宽高
                intent.putExtra("outputX", 320);
                intent.putExtra("outputY", 180);
            }
        } else {
            // aspectX aspectY 是宽高的比例
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            // outputX outputY 是裁剪图片宽高
            intent.putExtra("outputX", 250);
            intent.putExtra("outputY", 250);
        }

        intent.putExtra("return-data", true);
        //TODO 输入预裁剪图片的Uri，指定以后，可以通过这个Uri获得图片
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(PhotoBitmapUtils.getPhotoFileName(this))));
        startActivityForResult(intent, cut_photo);
    }

    /**
     * android 7.0裁剪照片
     *
     * @param context
     * @param imageFile
     * @return
     */
    public static Uri getImageContentUri(Context context, File imageFile) {
        L.e("7.0裁剪照片");
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
