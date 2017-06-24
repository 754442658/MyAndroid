package shishow.testactionbar;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    // 1、2、3、4
    private TextView tv_1, tv_2, tv_3, tv_4;

    private ImageView cursor;
    private int bmpw = 0; // 游标宽度
    private int offset = 0;// // 动画图片偏移量
    private int currIndex = 1;// 当前页卡编号
    private int one = 0; // 游标移动一格的距离绝对值
    private int screenWidth; // 屏幕宽度

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findView();
        initView();
        // 初始化指示器位置
        initCursorPos();
        addListener();
    }

    private void findView() {
        try {
            tv_1 = (TextView) findViewById(R.id.activity_main_home);
            tv_2 = (TextView) findViewById(R.id.activity_main_train);
            tv_3 = (TextView) findViewById(R.id.activity_main_vote);
            tv_4 = (TextView) findViewById(R.id.activity_main_infrom);
            cursor = (ImageView) findViewById(R.id.cursor);

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    private void initView() {
        try {
            WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
            DisplayMetrics outMetrics = new DisplayMetrics();
            wm.getDefaultDisplay().getMetrics(outMetrics);
            screenWidth = outMetrics.widthPixels;
        } catch (Exception e) {
            e.printStackTrace();
            // TODO: handle exception
        }

    }

    private void addListener() {
        // TODO Auto-generated method stub
        try {
            tv_1.setOnClickListener(this);
            tv_2.setOnClickListener(this);
            tv_3.setOnClickListener(this);
            tv_4.setOnClickListener(this);

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        Animation animation = null;
        switch (v.getId()) {
            case R.id.activity_main_home:
                // 设置当前点击的按钮,
                if (currIndex == 2) {
                    animation = new TranslateAnimation(one, 0, 0, 0);
                } else if (currIndex == 3) {
                    animation = new TranslateAnimation(one * 2, 0, 0, 0);
                } else if (currIndex == 4) {
                    animation = new TranslateAnimation(one * 3, 0, 0, 0);
                }
                currIndex = 1;
                break;
            case R.id.activity_main_train:
                if (currIndex == 1) {
                    animation = new TranslateAnimation(0, one, 0, 0);
                } else if (currIndex == 3) {
                    animation = new TranslateAnimation(one * 2, one, 0, 0);
                } else if (currIndex == 4) {
                    animation = new TranslateAnimation(one * 3, one, 0, 0);
                }
                currIndex = 2;
                break;
            case R.id.activity_main_vote:
                if (currIndex == 1) {
                    animation = new TranslateAnimation(0, one * 2, 0, 0);
                } else if (currIndex == 2) {
                    animation = new TranslateAnimation(one, one * 2, 0, 0);
                } else if (currIndex == 4) {
                    animation = new TranslateAnimation(one * 3, one * 2, 0, 0);
                }
                currIndex = 3;
                break;
            case R.id.activity_main_infrom:
                if (currIndex == 1) {
                    animation = new TranslateAnimation(0, one * 3, 0, 0);
                } else if (currIndex == 2) {
                    animation = new TranslateAnimation(one, one * 3, 0, 0);
                } else if (currIndex == 3) {
                    animation = new TranslateAnimation(one * 2, one * 3, 0, 0);
                }
                currIndex = 4;
                break;
            default:
                break;
        }
        if (animation == null)
            return;
        animation.setFillAfter(true);// True:图片停在动画结束位置
        animation.setDuration(300);
        cursor.startAnimation(animation);
    }

    // 初始化指示器位置
    public void initCursorPos() {
        // 初始化动画
        cursor = (ImageView) findViewById(R.id.cursor);
		// 住这里如果导航条不同，只需要更换一下导航条的资源id即可
        bmpw = BitmapFactory.decodeResource(getResources(), R.mipmap.icon_bar)
                .getWidth();// 获取图片宽度-游尺宽度
        // 下面的4是当前有多少个菜单按钮，或者有多少个选项
        offset = (screenWidth / 4 - bmpw) / 2;// 计算偏移量

        one = bmpw + (offset * 2);

        Matrix matrix = new Matrix();
        matrix.postTranslate(offset, 0);
        cursor.setImageMatrix(matrix);// 设置动画初始位置
    }

}
