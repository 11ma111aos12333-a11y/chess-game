package app.chess.game;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends Activity {

    private WebView web;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        web = new WebView(this);
        WebSettings s = web.getSettings();
        s.setJavaScriptEnabled(true);
        s.setDomStorageEnabled(true);
        s.setAllowFileAccess(true);
        s.setMediaPlaybackRequiresUserGesture(false);
        s.setBuiltInZoomControls(false);
        s.setDisplayZoomControls(false);
        s.setTextZoom(100);
        web.setBackgroundColor(0xFF161D26);
        web.setWebViewClient(new WebViewClient());
        web.addJavascriptInterface(new Bridge(), "ChessApp");
        web.loadUrl("file:///android_asset/chess.html");
        setContentView(web);
    }

    /* جسر الخروج: تستدعيه واجهة اللعبة بعد تأكيد المستخدم */
    private class Bridge {
        @JavascriptInterface
        public void exitApp() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() { finish(); }
            });
        }
    }

    /* زر الرجوع الفعلي تديره واجهة اللعبة بالكامل:
       إغلاق النوافذ ← العودة للقائمة ← خروج بالضغط مرتين */
    @Override
    public void onBackPressed() {
        if (web != null) {
            web.evaluateJavascript(
                "window.__NAV__?window.__NAV__.back():window.ChessApp.exitApp()", null);
        } else {
            super.onBackPressed();
        }
    }
}
