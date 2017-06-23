package shixiu.mitao.activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.zhy.autolayout.AutoLinearLayout;

import shixiu.mitao.R;
import shixiu.mitao.TApplication;
import shixiu.mitao.entity.DuoBaoOrder;
import shixiu.mitao.state.Constant;
import shixiu.mitao.utils.L;
import shixiu.mitao.utils.T;

public class ShareVideoActivity extends BaseActivity {
    // 返回
    private ImageView img_back;
    // 添加视频的布局
    private AutoLinearLayout ll_addVideo;
    // 分享bt
    private Button bt_share;
    // 选择的图片
    private ImageView img_video;
    // 选择的视频地址
    private String videoPath = "";
    // 要分享的夺宝订单
    private DuoBaoOrder duobaoOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_video);
        try {
            duobaoOrder = (DuoBaoOrder) getIntent().getSerializableExtra("duobaoOrder");
            findView();
            initView();
            addListener();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void findView() {
        img_back = (ImageView) findViewById(R.id.activity_share_video_img_back);
        img_video = (ImageView) findViewById(R.id.activity_share_video_img_video);
        ll_addVideo = (AutoLinearLayout) findViewById(R.id.activity_share_video_ll_addVideo);
        bt_share = (Button) findViewById(R.id.activity_share_video_bt_share);
    }

    private void initView() {

    }

    private void addListener() {
        img_back.setOnClickListener(this);
        ll_addVideo.setOnClickListener(this);
        bt_share.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        try {
            switch (view.getId()) {
                case R.id.activity_share_video_img_back:
                    // 点击了返回
                    finish();
                    break;
                case R.id.activity_share_video_ll_addVideo:
                    // 点击了添加视频
                    Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, Constant.REQUEST_CODE);
                    break;
                case R.id.activity_share_video_bt_share:
                    // 点击了分享
                    T.showShort(TApplication.instance, "分享");
                    Intent intent = new Intent(this, SubmitActivity.class);
                    intent.putExtra("duobaoOrder", duobaoOrder);
                    startActivity(intent);
                    finish();
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 选完视频后获取选中的视频地址
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constant.REQUEST_CODE && resultCode == RESULT_OK && null != data) {
            Uri selectedVideo = data.getData();
            String[] filePathColumn = {MediaStore.Video.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedVideo,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            videoPath = cursor.getString(columnIndex);
            cursor.close();

            L.e(videoPath);
            // 拿到选中的视频地址后，获取视频缩略图并且设置给imageview
            Bitmap bitmap = getVideoThumbnail(videoPath);
            img_video.setImageBitmap(bitmap);
        }

    }

    /**
     * 获取视频缩略图
     * <p/>
     * getFrameAtTime()有其他重载函数，该函数会随机选择一帧抓取，如果想要指定具体时间的缩略图，
     * 可以用函数getFrameAtTime(long timeUs), getFrameAtTime(long timeUs, int option)，
     * 具体如何使用可以查doc。
     *
     * @param filePath
     * @return
     */
    private Bitmap getVideoThumbnail(String filePath) {
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            retriever.setDataSource(filePath);
            bitmap = retriever.getFrameAtTime();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            try {
                retriever.release();
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }
}
