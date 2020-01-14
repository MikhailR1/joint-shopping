// Приложение совместных покупок

package com.example.firstandroidapp;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Timer;


public class MainActivity extends Activity {
	public final static String EXTRA_MESSAGE = "com.example.firstandroidapp.MESSAGE";
	
	String server_name = "http://srv.embeddershop.ru";
	String str_logtext = "";
	String str_passtext = "";
			
	
	double xnew=1;
	int day1=0;
	int[] tmp_arr = new int[15];
	String readStr = "";
	String readStr_prev = "";
	
	LOGtoSrv log_to_Srv;
	String ansver = null;
	//int ans_num=0;
	int ans_fl=0;
	HttpURLConnection conn;
	Integer res;
	int thrgo=0;
	String ansToSecAct = " ";
	
	
//-------------onCreate-------------------------	
    @Override
    //protected void onCreate(Bundle savedInstanceState) {
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_main);
        /*
        Thread thr = new Thread(new Runnable() {
        	public void run()
            {
        		try {
				URL url = new URL(server_name + "/srv.php?action=select");
				conn = (HttpURLConnection) url.openConnection();
				conn.setConnectTimeout(10000); // ждем 10сек
				conn.setRequestMethod("POST");
				conn.setRequestProperty("User-Agent", "Mozilla/5.0");
				conn.connect();
				res = conn.getResponseCode();
				//final TextView t_output = (TextView)findViewById(R.id.t_output);
				//t_output.setText(Integer.toString(res));
			} catch (Exception e) {
				Log.i("srv","+ MainActivity - ответ сервера ОШИБКА: "+ e.getMessage());
			} //finally {
				//conn.disconnect();
			//}
			
			try {
				InputStream is = conn.getInputStream();
				BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
				StringBuilder sb = new StringBuilder();
				String bfr_st = null;
				while ((bfr_st = br.readLine()) != null) {
					sb.append(bfr_st);
				}

				Log.i("srv", "+ FoneService - полный ответ сервера:\n"
						+ sb.toString());
				ansver = sb.toString();
				final TextView t_output = (TextView)findViewById(R.id.t_output);
				t_output.setText(ansver);

				is.close();
				br.close();

			} catch (Exception e) {
				Log.i("srv", "+ FoneService ошибка: " + e.getMessage());
			} finally {
				conn.disconnect();
				Log.i("srv",
						"+ FoneService --------------- ЗАКРОЕМ СОЕДИНЕНИЕ");
			}
			
			thrgo=0;	
        		
            } // end of public void run()

        }); // end of Thread
        thr.start();
        */
    }
    //--------------END OF onCreate----------------------------
 
    
//--------------Buttons---------------------
    
    public void sendlog(View view) {
    	//thrgo=1;
    	//final TextView t_output = (TextView)findViewById(R.id.t_output);
    	//t_output.setText("Ошибка регистрации");
    	
    	final EditText logtext = (EditText)findViewById(R.id.textone);
    	str_logtext = logtext.getText().toString();
        final EditText passtext = (EditText)findViewById(R.id.texttwo);
        str_passtext = passtext.getText().toString();
    	
    	log_to_Srv = new LOGtoSrv();
    	log_to_Srv.execute();
    	
    	int ans_num=0;
    	if (ansver != null) {
    	String[] arr_str = ansver.split("");
		try {ans_num = Integer.parseInt(arr_str[1]);} catch (NumberFormatException nfe) {};
		if (ans_num == 1) {
			//super.recreate();
			Intent intent = new Intent(this, SecActivity.class);
			intent.putExtra("iname", str_logtext);
			intent.putExtra("ansToSecAct", ansToSecAct);
	        startActivity(intent);
			}
		if (ans_num == 0) {
			final TextView t_output = (TextView)findViewById(R.id.t_output);
			t_output.setText("Не верные данные");
			}
    	} // eof if (ansver != null)
    
    }
    
    public void rglog(View view) {
    	
    	Intent intent = new Intent(this, SecActivity.class); 
        startActivity(intent);
        
    	//final TextView t_output = (TextView)findViewById(R.id.t_output);
		//t_output.setText(Integer.toString(ans_fl));
		/*if (ansver.indexOf("1") == 1) {
    	super.recreate();
		}*/
    	/*int ans_num=0;
    	if (ansver != null) {
    	String[] arr_str = ansver.split("");
		try {ans_num = Integer.parseInt(arr_str[1]);} catch (NumberFormatException nfe) {};
		if (ans_num == 1) {
			//final TextView t_output = (TextView)findViewById(R.id.t_output);
			//t_output.setText(arr_str[1]);
			//ans_fl=1;
			super.recreate();
			}
		if (ans_num == 0) {
			final TextView t_output = (TextView)findViewById(R.id.t_output);
			t_output.setText(arr_str[1]);
			//ans_fl=0;
			}
    	} // eof if (ansver != null)	
    	*/   	
    }
  //---------End-of-Buttons-------------------------------	  
	  
  //----------------------------------------
    
  //---------------Functions-------------------
    
  //---------------Classes-------------------
    private class LOGtoSrv extends AsyncTask<Void, Void, Integer> {
    	//HttpURLConnection conn;
		//Integer res;
    	//int ans_num;

		@Override
		protected Integer doInBackground(Void... arg0) {
			
			try {
				URL url = new URL(server_name + "/srv.php?action=inslog&name="+str_logtext+"&pass="+str_passtext);
				conn = (HttpURLConnection) url.openConnection();
				conn.setConnectTimeout(10000); // ждем 10сек
				conn.setRequestMethod("POST");
				conn.setRequestProperty("User-Agent", "Mozilla/5.0");
				conn.connect();
				res = conn.getResponseCode();
				//final TextView t_output = (TextView)findViewById(R.id.t_output);
				//t_output.setText(Integer.toString(res));
			} catch (Exception e) {
				Log.i("srv","+ MainActivity - ответ сервера ОШИБКА: "+ e.getMessage());
			} //finally {
				//conn.disconnect();
			//}
			
			try {
				InputStream is = conn.getInputStream();
				BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
				StringBuilder sb = new StringBuilder();
				String bfr_st = null;
				while ((bfr_st = br.readLine()) != null) {
					sb.append(bfr_st);
				}

				Log.i("srv", "+ FoneService - полный ответ сервера:\n"
						+ sb.toString());
				ansver = sb.toString();
				//final TextView t_output = (TextView)findViewById(R.id.t_output);
		    	//t_output.setText(ansver);

				is.close();
				br.close();

			} catch (Exception e) {
				Log.i("srv", "+ FoneService ошибка: " + e.getMessage());
			} finally {
				conn.disconnect();
				Log.i("srv",
						"+ FoneService --------------- ЗАКРОЕМ СОЕДИНЕНИЕ");
			}
			
			return res;
		}
    	
    }

//-------------------------------------------------------
//-------------------------------------------------------


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}



//-------------------------------------------------------
//-------------------------------------------------------
//-------------------------------------------------------
//-------------------------------------------------------
//-------------------------------------------------------
//-------------------------------------------------------
