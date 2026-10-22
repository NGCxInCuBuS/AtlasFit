package com.atlasfit.tracker;
import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.JavascriptInterface;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.media.AudioManager;
import android.view.KeyEvent;
import android.widget.Toast;

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
  public class Music {
    @JavascriptInterface public void control(String action) {
      int key;
      switch(action) {
        case "previous": key=KeyEvent.KEYCODE_MEDIA_PREVIOUS; break;
        case "next": key=KeyEvent.KEYCODE_MEDIA_NEXT; break;
        default: key=KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE;
      }
      final int code=key;
      runOnUiThread(()->{
        try {
          AudioManager manager=(AudioManager)getSystemService(Context.AUDIO_SERVICE);
          manager.dispatchMediaKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN,code));
          manager.dispatchMediaKeyEvent(new KeyEvent(KeyEvent.ACTION_UP,code));
        } catch(Exception e) {
          Toast.makeText(MainActivity.this,"Music player unavailable",Toast.LENGTH_SHORT).show();
        }
      });
    }
    @JavascriptInterface public void open(String service) {
      final String url;
      switch(service) {
        case "Spotify": url="https://open.spotify.com/"; break;
        case "YouTube Music": url="https://music.youtube.com/"; break;
        case "YouTube": url="https://www.youtube.com/"; break;
        case "SoundCloud": url="https://soundcloud.com/"; break;
        case "Pandora": url="https://www.pandora.com/"; break;
        default: return;
      }
      runOnUiThread(()->{
        try {startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse(url)));}
        catch(Exception e){Toast.makeText(MainActivity.this,"Unable to open music service",Toast.LENGTH_SHORT).show();}
      });
    }
  }
  @Override public void onCreate(Bundle b) {
    super.onCreate(b);
    web=new WebView(this);
    web.setWebViewClient(new WebViewClient());
    web.setWebChromeClient(new WebChromeClient());
    WebSettings settings=web.getSettings();
    settings.setJavaScriptEnabled(true);
    settings.setDomStorageEnabled(true);
    web.addJavascriptInterface(new Store(),"AtlasStore");
    web.addJavascriptInterface(new Music(),"AtlasMusic");
    setContentView(web);
    web.loadUrl("file:///android_asset/index.html");
  }
  @Override public void onBackPressed() {
    if(web.canGoBack()) web.goBack(); else super.onBackPressed();
  }
}
