package org.qiwoo.weekly75;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.qiwoo.weekly75.R;

public class ShowArticleActivity extends AppCompatActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_article);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        fab.setVisibility(View.GONE);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowArticleActivity.this.finish();
            }
        });

        webView = (WebView) findViewById(R.id.webview_show_article);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new BrowserWebChromeClient());
        webView.setWebViewClient(new UrlWebViewClient());
        Intent intent = getIntent();
        String urlStr = intent.getStringExtra(IssueListAdapter.ARTICLE_URL);
        String titleStr = intent.getStringExtra(IssueListAdapter.ARTICLE_TITLE);
        webView.loadUrl(urlStr);

        // 设置 title
        getSupportActionBar().setTitle(titleStr);
    }

    public class BrowserWebChromeClient extends WebChromeClient {

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
        }

        @Override
        public void onShowCustomView(View view, CustomViewCallback callback) {
            super.onShowCustomView(view, callback);
        }

        @Override
        public void onHideCustomView() {
            super.onHideCustomView();
        }
    }

    class UrlWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return dealWebView(view, url);
        }

        public void onReceivedError(WebView view, int errorCode, String description,
                                    String failingUrl) {
            view.stopLoading(); // may not be needed
            view.loadUrl("file:///android_asset/error.html");
        }

    }

    protected boolean dealWebView(WebView view, String url) {
        this.webView.loadUrl(url);
        return true;//返回true浏览器不再执行默认的操作
    }
}
