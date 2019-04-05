package com.irfan.draft1.News;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.CookieManager;
import android.webkit.DownloadListener;
import android.webkit.URLUtil;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.irfan.draft1.Notification.EventsNotification;
import com.irfan.draft1.R;

/**
 * Created by irfan on 26/10/2017.
 */

public class NewsLearnMore extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{
    private String urltoLoad;
    private ProgressBar progressBar;
    private WebView feedback;
    private SwipeRefreshLayout refresh;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_learnmore);
        Toolbar toolbar = findViewById(R.id.learnmore_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_clear_black_24dp);




        urltoLoad =  getIntent().getStringExtra("URL");
        //new EventsNotification().onMessageReceived();

        refresh = findViewById(R.id.refresh_news);
        refresh.setOnRefreshListener(this);

        feedback = findViewById(R.id.learnmore_webview);
        progressBar= findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);
        feedback.setWebViewClient(new WebViewClient());
        feedback.getSettings().setJavaScriptEnabled(true);
        feedback.getSettings().setBuiltInZoomControls(true);
        feedback.getSettings().setDisplayZoomControls(false);
        feedback.loadUrl(urltoLoad);

        feedback.setWebViewClient(new WebViewClient(){

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progressBar.setVisibility(View.VISIBLE);
                urltoLoad = url;
            }
            @Override
            public void onPageFinished(WebView view, String url) {
                progressBar.setVisibility(View.INVISIBLE);
            }
        });


//

        feedback.setDownloadListener(new DownloadListener() {
            public void onDownloadStart(String url, String userAgent,
                                        String contentDisposition, String mimetype,
                                        long contentLength) {
                DownloadManager.Request request = new
                        DownloadManager.Request(Uri.parse(url));

                request.setMimeType(mimetype);
                //------------------------COOKIE!!------------------------
                String cookies = CookieManager.getInstance().getCookie(url);
                request.addRequestHeader("cookie", cookies);
                //------------------------COOKIE!!------------------------
                request.addRequestHeader("User-Agent", userAgent);
                request.setDescription("Downloading file...");
                request.setTitle(URLUtil.guessFileName(url, contentDisposition, mimetype));
                request.allowScanningByMediaScanner();
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, URLUtil.guessFileName(url, contentDisposition, mimetype));
                DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                if (isStoragePermissionGranted()) {
                    dm.enqueue(request);
                    Toast.makeText(getApplicationContext(), "Downloading file", Toast.LENGTH_LONG).show();
                }

            }
        });

    }



    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(this,android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {

                return true;
            } else {

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            return true;
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.learnmore_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.shareNews:
                Intent sendIntent = new Intent(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, urltoLoad+"\n\nHey, check this out! Shared from MyCurtin");
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
                return true;
            case R.id.back_button:
                if (feedback.canGoBack()) {
                    feedback.goBack();
                }
                break;

            case R.id.forward_button:
                if (feedback.canGoForward()) {
                    feedback.goForward();
                }
                break;
            }


        return false;
    }

    @Override
    public void onRefresh() {
        refresh.setRefreshing(true);
        feedback.loadUrl(urltoLoad);
        refresh.setRefreshing(false);

    }
}
