package shixiutv.mitao.wxapi;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import shixiutv.mitao.state.AppStore;
import shixiutv.mitao.state.Constant;
import shixiutv.mitao.utils.L;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";

    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.goods_type.pay_result);

        api = WXAPIFactory.createWXAPI(this, Constant.APP_ID);

        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {
        L.e(TAG, "onPayFinish, errCode = " + resp.errCode);
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            if (resp.errCode == 0) {
                if (AppStore.wxPayHandler != null)
                    AppStore.wxPayHandler.sendEmptyMessage(Constant.WX_PAY_SUCCESS);
            } else if (resp.errCode == -2) {
                if (AppStore.wxPayHandler != null)
                    AppStore.wxPayHandler.sendEmptyMessage(Constant.WX_PAY_CANCLE);
            } else if (resp.errCode == -1) {
                if (AppStore.wxPayHandler != null)
                    AppStore.wxPayHandler.sendEmptyMessage(Constant.WX_PAY_FAILED);
            }
            finish();
        }
    }
}