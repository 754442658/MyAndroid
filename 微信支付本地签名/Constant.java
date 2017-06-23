package shixiutv.mitao.state;

import android.os.Environment;

import java.io.File;

import shixiutv.mitao.R;

/**
 * Created by ShiShow_xk on 2016/7/25.
 */
public class Constant {
  
    // 微信登录的标记
    public static final int WX_LOGIN = 0;
    // 微信分享的标记
    public static final int WX_SHARE = 1;


    // 微信AppID
    public static final String APP_ID = "wxdc10d63138e1ca58";
    // 微信Key
    public static final String KEY = "8dsfjlksjroweijdnldrkwe45342352d";
    // 微信AppSecret
    public static final String AppSecret = "6e6df4b16ea2fa4db2b883ce97ed6572";
    // 微信支付商户id
    public static final String MCH_ID = "1446081802";
    // 微信固定标识
    public static final String PACKAGE_VALUE = "Sign=WXPay";
    // 微信支付结果
    public static final int WX_PAY_SUCCESS = 1;
    public static final int WX_PAY_CANCLE = 0;
    public static final int WX_PAY_FAILED = -1;


    // 支付方式
    public static final int PAY_WX = 1;
    public static final int PAY_YE = 0;
    public static final int PAY_ZFB = 2;
}
