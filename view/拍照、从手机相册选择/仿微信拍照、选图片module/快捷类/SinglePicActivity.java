package com.sxrecruit.aaaa;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.widget.Toast;

import com.module.base.BaseActivity;
import com.module.utils.L;

import java.io.File;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import me.nereo.multi_image_selector.MultiImageSelector;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;

/**
 * Created by ShiShow_xk on 2017/9/6.
 * 从相册选择照片/拍照作为图片上传
 */

public abstract class SinglePicActivity extends BaseActivity {
    // 选择照片
    private int take_photo = 1002;
    // 上传ppt
    public final int TYPE_SELECT_PPT = 0;
    // 发送图片消息
    public final int TYPE_SELECT_SEND_MSG = 1;
    // 选择的图片是ppt还是发送的图片 0:ppt 其他:发送图片
    public int selectPicType = -1;

    // 选择结束的照片
    public File pic;
    // 裁剪后的照片文件
    public File picPath = new File(PhotoBitmapUtils.getPhoneRootPath(this) + "/sxzp/pic");


    // SD卡读写权限和拍照权限
    public String[] mPermission = {
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    /**
     * 选择照片
     *
     * @param hasCamera 是否可拍照
     */
    public void choicePhoto(final boolean hasCamera) {
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
                    if (hasCamera) {
                        MultiImageSelector.create()
                                .showCamera(true) // 是否显示相机. 默认为显示
                                .count(9) // 最大选择图片数量, 默认为9. 只有在选择模式为多选时有效
                                .single() // 单选模式
//                .multi() // 多选模式, 默认模式;
//                .origin(ArrayList < String >)// 默认已选择图片. 只有在选择模式为多选时有效
                                .start(SinglePicActivity.this, take_photo);
                    } else {
                        MultiImageSelector.create()
                                .showCamera(false) // 是否显示相机. 默认为显示
                                .count(9) // 最大选择图片数量, 默认为9. 只有在选择模式为多选时有效
                                .single() // 单选模式
//                .multi() // 多选模式, 默认模式;
//                .origin(ArrayList < String >)// 默认已选择图片. 只有在选择模式为多选时有效
                                .start(SinglePicActivity.this, take_photo);
                    }

                }
            }
        };
        request_code = 1001;
        getPermission(mPermission, request_code);
    }

    /**
     * 选择ppt
     */
    public void choicePPT() {
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
                            .showCamera(false) // 是否显示相机. 默认为显示
                            .count(9) // 最大选择图片数量, 默认为9. 只有在选择模式为多选时有效
//                            .single() // 单选模式
                            .multi() // 多选模式, 默认模式;
//                .origin(ArrayList < String >)// 默认已选择图片. 只有在选择模式为多选时有效
                            .start(SinglePicActivity.this, take_photo);
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
                ArrayList<String> path = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                if (selectPicType == TYPE_SELECT_SEND_MSG || selectPicType == -1) {
                    // 发送照片消息
                    // 选择、拍照成功
                    File file = new File(path.get(0));

                    // 取得图片旋转角度
                    int angle = PhotoBitmapUtils.readPictureDegree(path.get(0));
                    // 获取选择的照片文件大小
                    double size = contBuffer(file);
                    L.e("照片大小" + size + "K");
                    Bitmap bitmap = null;
                    if (size >= 10 * 1024) {
                        // 10M以上提示图片过大，超过10M
                        Toast.makeText(this, "图片不可超过10M", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (size <= 600) {
                        // 不压缩
                        pic = new File(path.get(0));
                        selectPic(pic);
                        return;
                    } else if (size <= 1200) {
                        // 压缩到1/2
                        bitmap = PhotoBitmapUtils.getCompressPhoto(2, path.get(0));
                    } else {
                        L.e("压缩比例 = " + (int) size / 400);
                        bitmap = PhotoBitmapUtils.getCompressPhoto((int) (size / 400), path.get(0));
                    }
                    // 修复图片被旋转的角度
                    Bitmap bitmap1 = PhotoBitmapUtils.rotaingImageView(angle, bitmap);
                    // 压缩后的图片存到指定位置
                    String picPath = PhotoBitmapUtils.savePhotoToSD(bitmap1, this);
                    pic = new File(picPath);
                    L.e("压缩过后的图片大小 = " + contBuffer(pic));
                    selectPic(pic);
                } else if (selectPicType == TYPE_SELECT_PPT) {
                    // 选择ppt
                    selectPPT(path);
                }
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
                    // 如果是文件则直接返回其大小,以“KB”为单位
                    double size = (double) file.length();

                    double kiloByte = size / 1024;
                    if (kiloByte < 1) {
                        return 1;
                    }

//                    double megaByte = kiloByte / 1024;
//                    if (megaByte < 1) {
//                        return 1;
//                    }
                    BigDecimal result2 = new BigDecimal(Double.toString(kiloByte));
                    return result2.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                }
            }
        }
        return 0;
    }

    public abstract void selectPic(File photo);

    /**
     * 选择ppt
     *
     * @param list
     */
    public void selectPPT(ArrayList<String> list) {

    }

}
