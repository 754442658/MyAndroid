package com.shishow.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lzy.okhttputils.OkHttpUtils;
import com.shishow.App;
import com.shishow.R;
import com.shishow.callback.DialogCallback;
import com.shishow.callback.JsonCallback;
import com.shishow.entity.MyEntity;
import com.shishow.entity.User;
import com.shishow.popWindow.PopPhoto;
import com.shishow.state.AppStore;
import com.shishow.state.Constant;
import com.shishow.utils.BitmapUtils;
import com.shishow.utils.L;
import com.shishow.utils.T;
import com.shishow.utils.UrlTools;
import com.zhy.autolayout.AutoRelativeLayout;

import java.io.File;
import java.io.FileNotFoundException;

import okhttp3.Request;
import okhttp3.Response;

public class PersonalActivity extends BaseActivity {

    private AutoRelativeLayout rl_back;
    // 头像、昵称、性别、所在地、收货地址 Rl
    private AutoRelativeLayout rl_photo, rl_name, rl_gender, rl_city, rl_address;
    // 保存 昵称、性别、所在地、收货地址
    private TextView tv_save, tv_name, tv_gender, tv_city, tv_address;

    private ImageView img_photo;

    // 修改的头像的base64
    private String photo;
    // 选择上传的图片方式
    private PopPhoto popW;


    // 拍照的文件
    private File img = new File(Constant.CACHE_FILE, "photo.jpg");
    private Uri imgUri;
    // 处理后的图片文件
    private File imgCrop = new File(Constant.CACHE_FILE, "photo_crop.jpg");
    private Uri imgCropUri = Uri.fromFile(imgCrop);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);
        try {
            addActivity();
            findView();
            initView();
            addListener();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void findView() {
        rl_back = (AutoRelativeLayout) findViewById(R.id.rl_back);
        rl_photo = (AutoRelativeLayout) findViewById(R.id.rl_photo);
        rl_name = (AutoRelativeLayout) findViewById(R.id.rl_name);
        rl_gender = (AutoRelativeLayout) findViewById(R.id.rl_gender);
        rl_city = (AutoRelativeLayout) findViewById(R.id.rl_city);
        rl_address = (AutoRelativeLayout) findViewById(R.id.rl_address);

        tv_save = (TextView) findViewById(R.id.tv_save);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_gender = (TextView) findViewById(R.id.tv_gender);
        tv_city = (TextView) findViewById(R.id.tv_city);
        tv_address = (TextView) findViewById(R.id.tv_address);
        img_photo = (ImageView) findViewById(R.id.img_photo);
    }

    private void initView() {
        Glide.with(this)
                .load(AppStore.user.getUserInfo().getUHeadPic() + "?" + System.currentTimeMillis()).asBitmap()
                .placeholder(Constant.PHOTO)
                .into(img_photo);

        tv_city.setText(AppStore.user.getUserInfo().getLocation());
        tv_name.setText(AppStore.user.getUserInfo().getUName());
        if (AppStore.user.getUserInfo().getUserSex() != null) {
            if (AppStore.user.getUserInfo().getUserSex().equals("0")) {
                // 男
                tv_gender.setText("男");
            } else if (AppStore.user.getUserInfo().getUserSex().equals("1")) {
                tv_gender.setText("女");
            } else if (AppStore.user.getUserInfo().getUserSex().equals("-1")) {
                tv_gender.setText("保密");
            }
        }
    }

    private void addListener() {
        rl_back.setOnClickListener(this);
        rl_photo.setOnClickListener(this);
        rl_name.setOnClickListener(this);
        rl_gender.setOnClickListener(this);
        rl_city.setOnClickListener(this);
        rl_address.setOnClickListener(this);
        tv_save.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        try {
            switch (view.getId()) {
                case R.id.rl_back:
                    finish();
                    break;
                case R.id.rl_photo:
                    popW = new PopPhoto(this, clickListener);
                    popW.showAtLocation(findViewById(R.id.rl_person),
                            Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); // 设置layout在PopupWindow中显示的位置
                    break;
                case R.id.rl_name:
                    startActivityForResult(new Intent(this, MyNingChengActivity.class), 20);
                    break;
                case R.id.rl_gender:
                    startActivityForResult(new Intent(this, GenderActivity.class), 30);
                    break;
                case R.id.rl_city:
                    break;
                case R.id.rl_address:
                    // 收货地址
                    startActivity(new Intent(this, AddressManagerAvtivity.class));
                    break;
                case R.id.tv_save:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 20:
                // 修改昵称
                if (resultCode == 200) {
                    // 获取用户修改的昵称,并赋值给tv
                    tv_name.setText(data.getStringExtra("name"));
                    AppStore.user.getUserInfo().setUName(data.getStringExtra("name"));
                }
                break;
            case 30:
                // 修改性别
                if (resultCode == 200) {
                    // 获取用户修改的昵称,并赋值给tv
                    String gender = data.getStringExtra("gender");
                    tv_gender.setText(gender);
                    if (gender.equals("男")) {
                        AppStore.user.getUserInfo().setUserSex("0");
                    } else if (gender.equals("女")) {
                        AppStore.user.getUserInfo().setUserSex("1");
                    } else if (gender.equals("保密")) {
                        AppStore.user.getUserInfo().setUserSex("-1");
                    }
                }
                break;
            /**
             * 选择照片或者拍摄照片后，回到当前界面执行代码
             */
            case 1:
                // 选择照片
                if (resultCode == Activity.RESULT_OK) {
                    startPhotoZoom(data.getData());
                }
                break;
            case 2:
                // 拍照
                if (resultCode == Activity.RESULT_OK) {

//                    File temp = new File(Environment.getExternalStorageDirectory() + "/touxiang_user.jpg");
//                    startPhotoZoom(Uri.fromFile(temp));

                    startPhotoZoom(imgUri);
                }
                break;
            case 3:
                // 裁剪完照片
                if (data != null) {
                    setPicToView(data);
                }
                break;
        }
    }


    /**
     * 为弹出的拍照窗口添加按钮点击监听事件
     */
    private View.OnClickListener clickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            popW.dismiss();
            switch (v.getId()) {
                case R.id.view_camero_rl_takephoto:
                    // 拍照
                    Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    i.putExtra("return-data", false);
                    imgUri = Uri.fromFile(img);
                    i.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);
                    i.putExtra("noFaceDetection", true);
                    startActivityForResult(i, 2);
                    break;
                case R.id.view_camero_rl_selectphoto:
                    // 选择照片
                    Intent i2 = new Intent(Intent.ACTION_PICK, null);
                    i2.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                    startActivityForResult(i2, 1);
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 裁剪选择/拍摄的照片
     *
     * @param uri
     */
    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("scale", true);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        //裁剪之后，保存在裁剪文件中，关键
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imgCropUri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        intent.putExtra("return-data", false);
        startActivityForResult(intent, 3);
    }

    /**
     * 拿到选择的图片
     *
     * @param picdata
     */
    private void setPicToView(Intent picdata) {
        Bundle extras = picdata.getExtras();
        Bitmap bitmap = null;
        if (extras != null) {
            try {
                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imgCropUri));
                L.e("size = " + bitmap.getByteCount() + "");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            //图片压缩
//            ByteArrayOutputStream stream = new ByteArrayOutputStream();
//            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            /**
             * 联网上传头像/背景图片
             */
            photo = BitmapUtils.bitmapToBase64(bitmap);
            L.e(photo);

            final Bitmap finalBitmap = bitmap;
            OkHttpUtils.post(UrlTools.changeUser)
                    .params("u", AppStore.uid)
                    .params("p", AppStore.pwd)
                    .params("f", photo)
                    .tag(this)
                    .execute(new DialogCallback<MyEntity>(this, MyEntity.class) {
                        @Override
                        public void onResponse(boolean isFromCache, MyEntity myEntity, okhttp3.Request
                                request, @Nullable Response response) {

                            int state = Integer.parseInt(myEntity.getID());
                            if (state > 0) {
                                L.e("修改成功");
                                img_photo.setImageBitmap(finalBitmap);
                                AppStore.isChangePhoto = true;
//                                // 修改成功 重新获取个人信息
//                                getUserInfo();
                            } else {
                                // 修改失败
                                T.showShort(App.instance, "修改失败");
                            }
                        }
                    });

        }
    }

    /**
     * 获取用户信息
     */
    public void getUserInfo() {
        OkHttpUtils.post(UrlTools.getUser)
                .tag(this)
                .params("UID", AppStore.uid)
                .execute(new JsonCallback<User>(User.class) {
                    @Override
                    public void onResponse(boolean isFromCache, User user, Request request,
                                           @Nullable Response response) {
                        L.e("用户信息获取成功");
                        AppStore.user = user;
                    }
                });
    }
}
