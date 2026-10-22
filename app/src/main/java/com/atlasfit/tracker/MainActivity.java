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
import android.media.session.MediaSessionManager;
import android.media.session.MediaController;
import android.media.session.PlaybackState;
import android.media.MediaMetadata;
import android.content.ComponentName;
import android.provider.Settings;
import android.graphics.Bitmap;
import android.util.Base64;
import java.io.ByteArrayOutputStream;
import java.util.List;
import org.json.JSONObject;

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
  private MediaController activeController() {
    try {
      MediaSessionManager manager=(MediaSessionManager)getSystemService(Context.MEDIA_SESSION_SERVICE);
      List<MediaController> sessions=manager.getActiveSessions(new ComponentName(this,MediaAccessService.class));
      MediaController fallback=null;
      for(MediaController controller:sessions) {
        PlaybackState ps=controller.getPlaybackState();
        if(ps!=null && ps.getState()==PlaybackState.STATE_PLAYING) return controller;
        if(fallback==null && controller.getMetadata()!=null) fallback=controller;
      }
      return fallback;
    } catch(Exception e) { return null; }
  }
  private boolean hasMediaAccess() {
    String enabled=Settings.Secure.getString(getContentResolver(),"enabled_notification_listeners");
    return enabled!=null && enabled.contains(getPackageName()+"/"+MediaAccessService.class.getName());
  }
  public class Music {
    @JavascriptInterface public boolean permitted(){return hasMediaAccess();}
    @JavascriptInterface public void requestAccess(){
      runOnUiThread(()->startActivity(new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)));
    }
    @JavascriptInterface public String status() {
      JSONObject out=new JSONObject();
      try {
        out.put("permission",hasMediaAccess());
        MediaController controller=activeController();
        if(controller==null)return out.toString();
        out.put("app",controller.getPackageName());
        PlaybackState playback=controller.getPlaybackState();
        out.put("playing",playback!=null && playback.getState()==PlaybackState.STATE_PLAYING);
        MediaMetadata m=controller.getMetadata();
        if(m!=null){
          out.put("title",m.getString(MediaMetadata.METADATA_KEY_TITLE));
          out.put("artist",m.getString(MediaMetadata.METADATA_KEY_ARTIST));
          Bitmap art=m.getBitmap(MediaMetadata.METADATA_KEY_ALBUM_ART);
          if(art==null)art=m.getBitmap(MediaMetadata.METADATA_KEY_ART);
          if(art!=null){
            Bitmap scaled=Bitmap.createScaledBitmap(art,120,120,true);
            ByteArrayOutputStream bytes=new ByteArrayOutputStream();
            scaled.compress(Bitmap.CompressFormat.JPEG,65,bytes);
            out.put("art","data:image/jpeg;base64,"+Base64.encodeToString(bytes.toByteArray(),Base64.NO_WRAP));
            if(scaled!=art)scaled.recycle();
          }
        }
      } catch(Exception e) { }
      return out.toString();
    }
    @JavascriptInterface public void control(String action) {
      runOnUiThread(()->{
        try {
          MediaController controller=activeController();
          if(controller!=null) {
            MediaController.TransportControls transport=controller.getTransportControls();
            PlaybackState playback=controller.getPlaybackState();
            switch(action) {
              case "previous": transport.skipToPrevious(); return;
              case "next": transport.skipToNext(); return;
              default:
                if(playback!=null && playback.getState()==PlaybackState.STATE_PLAYING) transport.pause();
                else transport.play();
                return;
            }
          }
          AudioManager manager=(AudioManager)getSystemService(Context.AUDIO_SERVICE);
          int key=action.equals("previous")?KeyEvent.KEYCODE_MEDIA_PREVIOUS:action.equals("next")?KeyEvent.KEYCODE_MEDIA_NEXT:KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE;
          manager.dispatchMediaKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN,key));
          manager.dispatchMediaKeyEvent(new KeyEvent(KeyEvent.ACTION_UP,key));
        } catch(Exception e) {
          Toast.makeText(MainActivity.this,"Media controls unavailable",Toast.LENGTH_SHORT).show();
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
    web.setWebViewClient(new WebViewClient(){
      @Override public void onPageFinished(WebView view,String url){
        super.onPageFinished(view,url);
        view.evaluateJavascript("(function(){var s=document.createElement('script');s.src='file:///android_asset/media-dock.js';document.body.appendChild(s)})()",null);
      }
    });
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
