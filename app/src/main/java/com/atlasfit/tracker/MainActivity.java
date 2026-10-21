package com.atlasfit.tracker;
import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.JavascriptInterface;
import android.content.Context;
import android.content.SharedPreferences;
public class MainActivity extends Activity {
  private WebView web;
  public class Store {
    @JavascriptInterface public String load() {
      return getSharedPreferences("atlasfit",Context.MODE_PRIVATE).getString("data","");
    }
    @JavascriptInterface public void save(String data) {
      getSharedPreferences("atlasfit",Context.MODE_PRIVATE).edit().putString("data",data).apply();
    }
  }
  @Override public void onCreate(Bundle b) {
    super.onCreate(b);
    web=new WebView(this);
    web.setWebViewClient(new WebViewClient());
    web.setWebChromeClient(new WebChromeClient());
    WebSettings s=web.getSettings();
    s.setJavaScriptEnabled(true);
    s.setDomStorageEnabled(true);
    web.addJavascriptInterface(new Store(),"AtlasStore");
    setContentView(web);
    web.loadUrl("file:///android_asset/index.html");
  }
  @Override public void onBackPressed() {
    if(web.canGoBack()) web.goBack(); else super.onBackPressed();
  }
}
