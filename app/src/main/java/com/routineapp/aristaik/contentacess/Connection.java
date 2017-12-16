package com.routineapp.aristaik.contentacess;

import android.app.DownloadManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by AR Istaik on 12/16/2017.
 */

public class Connection {

   private String server_url ;
    private String call_list ;
    private  String sms_list ;

    RequestQueue requestQueue ;

    public static void SendData(Context context, String url_string, String call_string , String sms_String) {
        Log.d("arif","on send data method") ;
           Connection con = new Connection();
           con.requestQueue = Volley.newRequestQueue(context);
           con.setServer_url(url_string);
           con.setCall_list(call_string);
           con.setSms_list(sms_String);
           con.SetData();
           con.requestQueue.stop();

    }

    public void setServer_url(String server_url) {
        this.server_url = server_url;
    }

    public void setCall_list(String call_list) {
        this.call_list = call_list;
    }

    public void setSms_list(String sms_list) {
        this.sms_list = sms_list;
    }

    private void SetData(){
        Log.d("arif","on set data method ");
       // server_url = sEditText.getText().toString() ;
        StringRequest stringResponse = new StringRequest(Request.Method.POST, server_url,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //mTextView.setText(response);
                        Log.d("arif","response recived ");
                        // requestQueue.stop();
                    };
                }  ,new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error) {
                //mTextView.setText(error.toString());
                error.printStackTrace();
                // requestQueue.stop();
            }

        }

        ) {

            @Override
            protected Map<String,String> getParams()
            {
                Map<String,String> params = new HashMap<String,String>();
               // String text = mEditText.getText().toString();
                params.put("call_list",call_list);
                params.put("sms_list",sms_list);
                return params;
            }
        };

        requestQueue.add(stringResponse);

    }
}
