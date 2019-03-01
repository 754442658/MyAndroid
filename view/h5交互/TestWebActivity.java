package com.xinanli.anhuanjia.webview;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import com.xinanli.anhuanjia.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TestWebActivity extends AppCompatActivity {

    @Bind(R.id.webview)
    WebView mWebView;
    @Bind(R.id.bt)
    Button bt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_web);
        ButterKnife.bind(this);

        //设置编码
        mWebView.getSettings().setDefaultTextEncodingName("utf-8");
        //支持js
        mWebView.getSettings().setJavaScriptEnabled(true);
        //设置背景颜色 透明
        mWebView.setBackgroundColor(Color.argb(0, 0, 0, 0));
        //设置本地调用对象及其接口
        mWebView.addJavascriptInterface(new JavaScriptObject(this), "myObj");

        mWebView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                mWebView.loadUrl("javascript:funFromjs()");

                super.onPageFinished(view, url);
            }
        });

        //载入js
        mWebView.loadUrl("file:///android_asset/test.html");

    }

    @OnClick(R.id.bt)
    public void onViewClicked() {
        mWebView.loadUrl("javascript:funFromjs()");
        Toast.makeText(this, "调用javascript:funFromjs()", Toast.LENGTH_LONG).show();
    }

    public class JavaScriptObject {
        Context mContext;

        JavaScriptObject(Context c) {
            mContext = c;
        }

        @JavascriptInterface
        public void fun1FromAndroid(String name) {
            Toast.makeText(mContext, name, Toast.LENGTH_LONG).show();
        }

        @JavascriptInterface
        public void fun2(String name) {
            Toast.makeText(mContext, "调用fun2:" + name, Toast.LENGTH_SHORT).show();
        }
    }
}
