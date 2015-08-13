package com.example.tu.e0810_1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

/**
 * Created by tu on 2015/8/13.
 */
public class web  extends ActionBarActivity{

    int position;
    String conhtml;
    WebView myBrowser;
    String webData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_layout);

        final GlobalVariable globalVariable = (GlobalVariable)getApplicationContext();
        position  = globalVariable.position;

        conhtml = globalVariable.conHtml[position];
        String title = globalVariable.title[position];
        String date = globalVariable.date[position];

        webData ="<html><body>"+conhtml+"</body></html>";

        myBrowser=(WebView)findViewById(R.id.webView);

        WebSettings websettings = myBrowser.getSettings();
        websettings.setSupportZoom(true);
        //websettings.setBuiltInZoomControls(true);
        websettings.setJavaScriptEnabled(true);

        myBrowser.setWebViewClient(new WebViewClient());
        myBrowser.loadDataWithBaseURL(null, webData, "text/html", "utf-8", null);

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                conhtml = globalVariable.conHtml[position - 1];
                position=position-1;
                webData = "<html><body>" + conhtml + "</body></html>";
                myBrowser.loadDataWithBaseURL(null, webData, "text/html", "utf-8", null);
            }
        });

        Button butto_1 = (Button) findViewById(R.id.button_1);
        butto_1.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                conhtml = globalVariable.conHtml[position + 1];
                position=position+1;
                webData ="<html><body>"+conhtml+"</body></html>";
                myBrowser.loadDataWithBaseURL(null, webData, "text/html", "utf-8", null);
            }
        });


    }
}
