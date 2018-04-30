package edu.duke.compsci290.fpx;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Philipk on 29/04/18.
 */

public class SignInActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_screen);

        String auth="https://oauth.oit.duke.edu/oauth/authorize.php?client_id=fpx&" +
                "redirect_uri=http%3A%2F%2Flocalhost%3A1717&" +
                "response_type=token&state=1129&" +
                "scope=basic identity:netid:read";

        URL url=null;
        URLConnection conn=null;

        try {
            url = new URL(auth);
        } catch (MalformedURLException e) {

            e.printStackTrace();
        }

        try {
            conn = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {

            e.printStackTrace();
        }


        WebView myWebView = (WebView) findViewById(R.id.webview);
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        myWebView.loadUrl(auth);
        myWebView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                //Log.i("", "ACCESS_DENIED_HERE");
                String token="";

                if (url.contains("error")){

                    Intent mIntent = new Intent(SignInActivity.this, Starter_Activity.class);
                    startActivity(mIntent);

                }else if(url.contains("#access_token=")){

                    Intent mIntent = new Intent(SignInActivity.this, MapsActivity.class);
                    startActivity(mIntent);


                }



            }

        });



    }





}
