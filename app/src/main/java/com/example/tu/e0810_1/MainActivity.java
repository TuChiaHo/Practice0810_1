package com.example.tu.e0810_1;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Target;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;


public class MainActivity extends ActionBarActivity {



    String[] array_title = new String[10];
    String[] array_date = new String[10];
    String[] url = new String[10];

    TextView textview_result;

    ListView listView;

    String  title_old="", test;
    Handler mHandler;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textview_result = (TextView)findViewById(R.id.result);
        listView = (ListView)findViewById(R.id.listView);

        final MyAdapter adapter = new MyAdapter(this);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), "你選擇的是" + array_title[position], Toast.LENGTH_SHORT).show();

                GlobalVariable globalVariable = (GlobalVariable)getApplicationContext();
                globalVariable.position  = position;

                Intent intent = new Intent();
                intent.setClass(MainActivity.this, web.class);
                startActivity(intent);
                //TestExam001.this.finish();
            }
        });


        new Thread(new Runnable(){

            @Override
            public void run() {
                // TODO Auto-generated method stub
                while(true){
                    try{
                        Message msg = new Message();
                        msg.what = 1;
                        mHandler.sendMessage(msg);
                        Thread.sleep(2000);
                    }
                    catch(Exception e){
                        e.printStackTrace();
                    }
                }

            }

        }).start();

        mHandler = new Handler(){

            @Override
            public void handleMessage(Message msg) {
                switch(msg.what){
                    case 1:
                        update();
                        //textview_result.setText(array_title[0]+"yes");

                        for(int i=0; i<10; i++) {
                            if (array_title[i] != null) {
                                if (!title_old.equals(array_title[i])) {

                                    title_old = array_title[i];
                                    adapter.notifyDataSetChanged();

                                }
                            }

                        }
                        break;
                }
                super.handleMessage(msg);
            }
        };

    }

    public void update(){

        new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                try {
                    //API串接的uri路徑
                    String uri = "http://rest.sinyi.com.tw/News/getNewsData.json";

                    HttpPost mHttpPost = new HttpPost(uri);

                    List<NameValuePair> params = new ArrayList <NameValuePair> ( ) ; //宣告參數清單

                    for(int i=0; i<10; i++) {


                        params.add(new BasicNameValuePair("page", String.valueOf(i+1))); //加入參數定義
                        params.add(new BasicNameValuePair("pon", "1")); //加入參數定義

                        try //因Java的規定，所以從這開始要用try..catch

                        {

                            mHttpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8)); //設定參數和編碼

                            HttpResponse res = new DefaultHttpClient().execute(mHttpPost); //執行並接收Server回傳的Page值

                            if (res.getStatusLine().getStatusCode() == 200) { //判斷回傳的狀態是不是200

                                String mJsonText = EntityUtils.toString(res.getEntity());

                                //mJsonText = mJsonText.substring(43, mJsonText.length() - 41);

                                mJsonText = mJsonText.substring(42, mJsonText.length() - 40);

                                Gson gson = new Gson();
                                Type listType = new TypeToken<ArrayList<MyJsonObj>>() {
                                }.getType();
                                ArrayList<MyJsonObj> jsonArr = gson.fromJson(mJsonText, listType);

                                for (MyJsonObj obj : jsonArr) {
                                    array_title[i] = obj.getSna();
                                    array_date[i] = obj.getLat() + "-" + obj.getLng();
                                    url[i] = obj.getSbi();

                                    GlobalVariable globalVariable = (GlobalVariable)getApplicationContext();
                                    globalVariable.conHtml[i]  = obj.getBemp();
                                    globalVariable.title[i] = array_title[i];
                                    globalVariable.date[i] = array_date[i];

                                }
                                test="yes";
                                //textview_result.setText("7777");

                            } else {
                                textview_result.setText("No");

                            }
                        } catch (Exception e)

                        {
                        }

                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    // e.printStackTrace();
                }
            }

        }).start();

    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class MyAdapter extends BaseAdapter {

        private LayoutInflater adapterLayoutInflater;// 动态布局映射

        public MyAdapter(Context context) {
            //this.adapterLayoutInflater = LayoutInflater.from(context);
            adapterLayoutInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return 10;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            // TODO Auto-generated method stub

            TagView tag;
            if (view == null) {
                view = adapterLayoutInflater.inflate(R.layout.adapter, null);
                tag = new TagView(
                        //   (Button)view.findViewById(R.id.AdapterButton),
                        (ImageView) view.findViewById(R.id.AdapterImage),
                        (TextView) view.findViewById(R.id.AdapterText),
                        (TextView) view.findViewById(R.id.AdapterText_date));
                view.setTag(tag);
            } else {
                tag = (TagView) view.getTag();
            }

            Picasso.with(view.getContext()).load(url[position]).into(tag.image);


            //tag.image.setBackgroundResource(R.drawable.a);
            // tag.button.setText("button"+position);
            tag.text.setText(array_title[position]);
            tag.text_date.setText(array_date[position]);

            return view;
        }

        public class TagView{

            ImageView image;
            TextView text;
            TextView text_date;

            public TagView(ImageView image, TextView text, TextView text_date ){

                this.image = image;
                this.text = text;
                this.text_date = text_date;

            }
        }
    }

}
