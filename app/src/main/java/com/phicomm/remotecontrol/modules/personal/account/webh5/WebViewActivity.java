package com.phicomm.remotecontrol.modules.personal.account.webh5;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.phicomm.remotecontrol.R;
import com.phicomm.remotecontrol.base.BaseActivity;
import com.phicomm.remotecontrol.util.NetworkManagerUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by yong04.zhou on 2017/9/18.
 */

public class WebViewActivity extends BaseActivity {
    public static final String WEB_KEY_STATUS = "mWebKey";
    public static final String WEB_VALUE_URL = "web_url";

    private String mWebKey = null;
    private String mWebUrl = null;

    @BindView(R.id.webView)
    WebView mWebView;

    @BindView(R.id.no_network_ll)
    View mNoNetWorkLayout;

    @BindView(R.id.tv_title)
    TextView mTitle;

    @BindView(R.id.iv_back)
    ImageView mBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_webview_layout);

        initDataAndTitleBar();

        initWebView();

        startToLoadWebUrl();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        try {
            if (mWebView != null) {
                ViewParent parent = mWebView.getParent();
                if (parent != null) {
                    ((ViewGroup) parent).removeView(mWebView);
                }
                mWebView.removeAllViews();
                mWebView.destroy();
                mWebView = null;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void initDataAndTitleBar() {
        Intent intent = getIntent();
        mWebKey = intent.getStringExtra(WEB_KEY_STATUS);
        mWebUrl = intent.getStringExtra(WEB_VALUE_URL);

        mTitle.setText(mWebKey);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    private void initWebView() {
        //启用支持javascript
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptEnabled(true);//支持js
        settings.setUseWideViewPort(true);//将图片调整到适合webview的大小
        settings.setSupportZoom(true);//支持缩放
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);//不显示webview缩放按钮
        settings.setDomStorageEnabled(true);//设置可以使用localStorage
        settings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        settings.setAppCacheEnabled(true);//应用可以有缓存开启缓存模式
        String appCaceDir = this.getApplicationContext().getDir("cache", Context.MODE_PRIVATE).getPath();
        settings.setAppCachePath(appCaceDir);
        if (NetworkManagerUtils.isNetworkAvailable(this)) {//联网情况下请求数据
            settings.setCacheMode(WebSettings.LOAD_DEFAULT);//缓存模式
        } else {//没联网情况走缓存
            settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);//缓存模式
        }

        //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return super.shouldOverrideUrlLoading(view, request);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                showWebViewOrError(false);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }
        });

        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                // TODO Auto-generated method stub
            }
        });
    }

    private void startToLoadWebUrl() {
        showWebViewOrError(true);

        //WebView加载web资源
        mWebView.loadUrl(mWebUrl);
    }

    private void showWebViewOrError(boolean flag) {
        if (flag) {
            mWebView.setVisibility(View.VISIBLE);
            mNoNetWorkLayout.setVisibility(View.GONE);
        } else {
            mWebView.setVisibility(View.GONE);
            mNoNetWorkLayout.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.tv_no_network)
    public void clickToReloadWebUrl() {
        startToLoadWebUrl();
    }
}

