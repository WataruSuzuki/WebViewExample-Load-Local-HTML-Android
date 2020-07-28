package webview.localhtmlexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class WebViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        WebView webView = findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new JavaScriptObject(this), "Bridging");

        Intent intent = getIntent();
        String url = "file:///android_asset" + "/sample.html";
        
        if (intent.hasExtra("type") && "getFilesDir".equals(intent.getStringExtra("type"))) {
            copyAssetsHtmlFile(this, getFilesDir().getAbsolutePath());
            //Note : It's working on targetSdkVersion == 29,
            //       but not on targetSdkVersion == 30... (net::ERR_ACCESS_DENIED)
            url = "file://" + getFilesDir().getAbsolutePath() + "/sample.html";
        }
        webView.loadUrl(url);
    }

    private void copyAssetsHtmlFile(Context context, String directoryPath) {
        try {
            InputStream inputStream = context.getResources().getAssets().open("sample.html");
            FileOutputStream fileOutputStream = new FileOutputStream(
                    new File(directoryPath, "sample.html"), false);
            byte[] buffer = new byte[1024];
            int length = 0;
            while ((length = inputStream.read(buffer)) >= 0) {
                fileOutputStream.write(buffer, 0, length);
            }

            fileOutputStream.close();
            inputStream.close();
        } catch (IOException ignore) {
        }
    }
}