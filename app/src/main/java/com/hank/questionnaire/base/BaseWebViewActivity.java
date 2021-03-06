package com.hank.questionnaire.base;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.DownloadListener;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import java.util.Map;

import com.hank.questionnaire.QuestionnairesApplication;
import com.hank.questionnaire.R;
import com.hank.questionnaire.data.Constants;


public class BaseWebViewActivity extends BaseActivity {

    private static final String TAG = BaseWebViewActivity.class.getSimpleName();

    private FrameLayout mContainer;

    protected WebView mWebView;
    protected String mUrl;
    protected String mTitle;

    private BaseWebViewClient mWebViewClient = new BaseWebViewClient();

    private View.OnClickListener mRefreshClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if (mWebView != null) {
                mWebView.reload();
            }
        }
    };

    private View.OnClickListener mCloseButtonClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        if (intent == null) {
            finish();
            return;
        }
        mUrl = getUrl(intent);
        mTitle = getWebViewTitle(intent);

        mContainer = new FrameLayout(this);
        mContainer.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mWebView = new WebView(getApplicationContext());
        mContainer.addView(mWebView, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        setContentView(mContainer);

        initView();

        initWebView();

        if (TextUtils.isEmpty(mUrl)) {
            if (!hasRedirect()) {
                finish();
            }
            return;
        }

        if (!TextUtils.isEmpty(mTitle)) {
            setTitle(mTitle);
        }

        loadPage();

    }

    protected void loadPage() {
        mWebView.loadUrl(mUrl, getAdditionalHttpHeaders());
    }

    protected String getUrl(Intent intent) {
        if (intent != null) {
            return intent.getStringExtra(Constants.EXTRA_URL);
        }
        return null;
    }

    protected String getWebViewTitle(Intent intent) {
        if (intent != null) {
            return intent.getStringExtra(Constants.EXTRA_TITLE_NAME);
        }
        return null;
    }

    protected void initView() {
        Toolbar toolbar = getToolbar();
        /*if (showRefreshButton()) {
            ImageButton refreshView = new ImageButton(this, null, android.support.v7.appcompat.R.attr.toolbarNavigationButtonStyle);
            Toolbar.LayoutParams layoutParams = new Toolbar.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            refreshView.setLayoutParams(layoutParams);
            layoutParams.gravity = Gravity.RIGHT | Gravity.CENTER_VERTICAL;
            layoutParams.rightMargin = (int) DensityUtils.dip2px(10);
            refreshView.setImageResource(R.drawable.ic_wallet_action_refresh);
            refreshView.setOnClickListener(getRefreshClickListener());
            toolbar.addView(refreshView, layoutParams);
        }*/
        updateHomeAsUpButton(toolbar);
    }

    protected void updateHomeAsUpButton(Toolbar toolbar) {
        if (toolbar == null) {
            return;
        }
        View.OnClickListener closeListener = getCloseButtonClickListener();
        if (closeListener != null) {
            toolbar.setNavigationOnClickListener(closeListener);
        }
        toolbar.setNavigationIcon(R.drawable.ic_back);
    }

    protected View.OnClickListener getCloseButtonClickListener() {
        return mCloseButtonClickListener;
    }

    protected boolean showRefreshButton() {
        return true;
    }

    protected View.OnClickListener getRefreshClickListener() {
        return mRefreshClickListener;
    }

    /**
     * 返回关闭页面时url地址
     *
     * @param url
     * @return true 关闭页面，false 不会关闭页面
     */
    protected boolean closePageUrl(String url) {
        return true;
    }

    /**
     * @param url
     * @return 是true的人为控制打开逻辑，为false时webview自己处理
     */
    protected boolean overrideUrlLoading(String url) {
        return true;
    }

    protected boolean needUpdateTitle() {
        return false;
    }

    protected Map<String, String> getAdditionalHttpHeaders() {
        return null;
    }

    protected void initWebView() {
        mWebView.setWebViewClient(mWebViewClient);
        mWebView.setDownloadListener(new DownloadListener() {

            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                if (!TextUtils.isEmpty(url) && url.startsWith("http")) {
                    try {
                        Uri uri = Uri.parse(url);
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                    } catch (Exception e) {
                    }
                }
            }
        });
        mWebView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    // TODO: 2017/6/30  网页加载结束处理
                }
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                if (needUpdateTitle() && !TextUtils.isEmpty(title)) {
                    mTitle = title;
                    setTitle(title);
                }
            }

            @Override
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                if (callback != null) {
                    callback.invoke(origin, true, false);
                }
                super.onGeolocationPermissionsShowPrompt(origin, callback);
            }
        });
        WebSettings settings = mWebView.getSettings();
        QuestionnairesApplication application = QuestionnairesApplication.getApplication();
        if (application != null) {
            String ua = new StringBuilder()
                    .append(settings.getUserAgentString())
                    .toString();
            settings.setUserAgentString(ua);
        }

        settings.setJavaScriptEnabled(true);

        settings.setAppCacheEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);

        String cacheDir = getApplicationContext().getDir("cache", Context.MODE_PRIVATE).getPath();
        String dbDir = getApplicationContext().getDir("databases", Context.MODE_PRIVATE).getPath();
        settings.setAppCachePath(cacheDir);
        settings.setAppCacheMaxSize(5 * 1024 * 1024);
        settings.setDatabasePath(dbDir);

        settings.setGeolocationDatabasePath(dbDir);

        settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);

        settings.setGeolocationEnabled(true);
        settings.setSupportZoom(true);
        settings.setUseWideViewPort(false);
        settings.setLoadWithOverviewMode(true);
        CookieManager.getInstance().setAcceptCookie(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            CookieManager.getInstance().setAcceptThirdPartyCookies(mWebView, true);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mWebView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mWebView.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mWebView.onResume();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        String url = getUrl(getIntent());
        if (TextUtils.isEmpty(url) || url.equalsIgnoreCase(mUrl)) {
            return;
        }
        mTitle = getWebViewTitle(intent);
        if (!TextUtils.isEmpty(mTitle)) {
            setTitle(mTitle);
        }
        loadPage();
    }

    @Override
    protected void onDestroy() {
        if (mWebView != null) {
            mWebView.stopLoading();
            mWebView.setWebChromeClient(null);
            mWebView.setDownloadListener(null);
            mWebView.setWebViewClient(null);
            mContainer.removeAllViews();
            mWebView.destroy();
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();//返回上一页面
        } else {
            super.onBackPressed();
        }
    }

    public class BaseWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            if (request != null) {
                Uri uri = request.getUrl();
                if (uri != null) {
                    return checkUrl(view, uri.toString());
                }
            }
            return super.shouldOverrideUrlLoading(view, request);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            //返回值是true的人为控制打开逻辑，为false时webview自己处理
            return checkUrl(view, url);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }
    }

    private boolean checkUrl(WebView view, String url) {
        if (!url.startsWith("http")) {
            Intent intent = null;
            if (url.startsWith("intent:")) {
                try {
                    intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
                } catch (Exception e) {
                    intent = null;
                }
                //如果希望在新的界面打开一个网页走下边的逻辑
                /*if (intent != null) {
                    Uri uri = intent.getData();
                    if (uri != null) {
                        String newPage = uri.getQueryParameter("newPage");
                        String realUrl = uri.getQueryParameter("url");
                        if (uri.getHost().equals("wallet") && !TextUtils.isEmpty(newPage) && !TextUtils.isEmpty(realUrl) && Boolean.valueOf(newPage)) {
                            Intent pageIntent = new Intent();
                            pageIntent.setClass(this, BaseWebViewActivity.class);
                            pageIntent.putExtra(Constants.EXTRA_URL, realUrl);
                            pageIntent.putExtra(Constants.EXTRA_TITLE_NAME, mTitle);
                            startActivity(pageIntent);
                            return true;
                        }
                    }
                }*/
            }
            if (overrideUrlLoading(url)) {
                try {
                    if (intent == null) {
                        intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (intent == null) {
                    return false;
                }
                try {
                    setFlagIfNeeded(intent);
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            }
        }
        return false;
    }

    public void setFlagIfNeeded(Intent intent) {

    }

    public boolean hasRedirect() {
        return false;
    }
}


