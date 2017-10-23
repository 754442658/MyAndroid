package com.sxrecruit.aaaa;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.widget.Toast;

import com.module.base.BaseActivity;
import com.module.dialog.PopSelectPhoto;
import com.module.utils.L;

import java.io.File;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by ShiShow_xk on 2017/9/6.
 * 从相册选择照片/拍照作为图片上传
 */

public abstract class PicaActivity extends BaseActivity {
    // 拍照
    public final int PHOTO_CAMERA = 1885;
    // 相册
    public final int PHOTO_PICTURE = 4678;
    // 图片的存储地址
    public File picPath = new File(PhotoBitmapUtils.getPhoneRootPath(this) + "/sxzp/pic");

    // 选择结束的照片
    public File pic;

    // 点击选择图片弹出的选择器
    public PopSelectPhoto pop;
    public View.OnClickListener itemClickLitener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            pop.dismiss();
            if (v.getId() == com.module.R.id.view_camero_rl_takephoto) {
                takePic(PHOTO_CAMERA);
            } else if (v.getId() == com.module.R.id.view_camero_rl_selectphoto) {
                takePic(PHOTO_PICTURE);
            }
        }
    };

    public void takePic(int type) {
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
        if (type == PHOTO_CAMERA) {
            // 拍照
            Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File img = new File(PhotoBitmapUtils.getPhotoFileName(PicaActivity.this));
            Uri uri = null;
            // 判断版本
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                // 因为 intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION) 只在5.0以上的版本有效，但是当前设置为7.0以上采用
                i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                //改变Uri  com.xykj.customview.fileprovider注意和xml中的一致
                uri = FileProvider.getUriForFile(PicaActivity.this, "sxzp.fileprovider", img);
            } else {
                uri = Uri.fromFile(img);
            }
            i.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            startActivityForResult(i, PHOTO_CAMERA);
        } else {
            // 手机相册选择
            Intent i2 = new Intent(Intent.ACTION_PICK, null);
            i2.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
            startActivityForResult(i2, PHOTO_PICTURE);
        }
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
                try {
                    Uri selectedImage = data.getData(); //获取系统返回的照片的Uri
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);//从系统表中查询指定Uri对应的照片
                    String picturePath = null;
                    if (cursor != null) {
                        cursor.moveToFirst();
                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        picturePath = cursor.getString(columnIndex);  //获取照片路径
                        cursor.close();
                    } else {
                        // 针对部分手机获取的cursor为null处理
                        picturePath = selectedImage.getPath();
                    }
                    L.e("相册选择原图地址 = " + picturePath);

                    // 取得图片旋转角度
                    int angle = PhotoBitmapUtils.readPictureDegree(picturePath);
                    // 获取选择的照片文件大小
                    double size = contBuffer(new File(picturePath));
                    L.e("照片大小" + size);
                    Bitmap bitmap = null;
                    if (size <= 1) {
                        // 1M以内不压缩
                        bitmap = PhotoBitmapUtils.getCompressPhoto(1, picturePath);
                    } else if (size <= 4) {
                        // 1-4M压缩50
                        bitmap = PhotoBitmapUtils.getCompressPhoto(2, picturePath);
                    } else if (size <= 10) {
                        // 4-10M 压缩到10%
                        bitmap = PhotoBitmapUtils.getCompressPhoto(10, picturePath);
                    } else {
                        // 10M以上提示图片过大，超过10M
                        Toast.makeText(this, "图片不可超过10M", Toast.LENGTH_SHORT).show();
                        return;
                    }
//                    // 修复图片被旋转的角度
                    Bitmap bitmap1 = PhotoBitmapUtils.rotaingImageView(angle, bitmap);
                    // 压缩后的图片存到指定位置
                    String picPath = PhotoBitmapUtils.savePhotoToSD(bitmap1, this);
                    L.e("相册选的图片地址 = " + picPath);
                    pic = new File(picPath);
                    selectPic(pic);
                } catch (Exception e) {
                    // TODO Auto-generatedcatch block
                    e.printStackTrace();
                }

                break;
            case PHOTO_CAMERA:
                // 拍照
                // 图片地址
                String path = PhotoBitmapUtils.getPhotoFileName(this);


                // 取得图片旋转角度
                int angle = PhotoBitmapUtils.readPictureDegree(path);
                // 获取选择的照片文件大小
                double size = contBuffer(new File(path));
                L.e("照片大小" + size);
                Bitmap bitmap = null;
                if (size <= 1) {
                    // 1M以内不压缩
                    bitmap = PhotoBitmapUtils.getCompressPhoto(1, path);
                } else if (size <= 4) {
                    // 1-4M压缩50
                    bitmap = PhotoBitmapUtils.getCompressPhoto(2, path);
                } else if (size <= 10) {
                    // 4-10M 压缩到10%
                    bitmap = PhotoBitmapUtils.getCompressPhoto(10, path);
                } else {
                    // 10M以上提示图片过大，超过10M
                    Toast.makeText(this, "图片不可超过10M", Toast.LENGTH_SHORT).show();
                    return;
                }

                // 修复图片被旋转的角度
                Bitmap bitmap1 = PhotoBitmapUtils.rotaingImageView(angle, bitmap);

                // 压缩后的图片存到指定位置
                String picPath = PhotoBitmapUtils.savePhotoToSD(bitmap1, this);

                L.e("修复后的拍照地址 = " + picPath);
                pic = new File(picPath);
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

    /**
     * @param file 传入文件夹路径的对象
     * @return 返回文件夹大小
     */
    public static double contBuffer(File file) {
        if (file != null) {
            if (file.exists()) {
                // 如果是目录则递归计算其内容的总大小
                if (file.isDirectory()) {
                    File[] children = file.listFiles();
                    double size = 0;
                    for (File f : children) {
                        size += contBuffer(f);
                    }
                    return (int) size;
                } else {
                    // 如果是文件则直接返回其大小,以“MB”为单位
                    double size = (double) file.length();

                    double kiloByte = size / 1024;
                    if (kiloByte < 1) {
                        return 1;
                    }

                    double megaByte = kiloByte / 1024;
                    if (megaByte < 1) {
                        return 1;
                    }
                    BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
                    return result2.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                }
            }
        }
        return 0;
    }

    public abstract void selectPic(File photo);
}
