package com.example.firstandroidapp;

import android.app.Activity;
import android.support.v4.app.ListFragment;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.app.Dialog;
import android.view.Window;

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

//import com.example.firstandroidapp.SecActivity.LOGtoSrv;
//import com.orm.SugarRecord;
import java.util.List;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.activeandroid.query.Delete;

public class GoodInfo extends Activity {
	
	String iName = null;
	String idFromList = "";
	String nameFromList  = "";
	final String LOG_TAG = "GoodInfoLogs";
	String server_name = "http://srv.embeddershop.ru";
	private Button mButtonBuy;
	LOGtoSrvBuy log_to_SrvBuy;
	//LOGtoSrvDel log_to_SrvDel;
	String ansver = null;
	String ansver2 = null;
	String ansver3 = null;
	HttpURLConnection conn;
	Integer res;
	Integer res2;
	Integer res3;
	int gogoodinfo = 0;
	int gogoodinfo2 = 0;
	int gogoodinfo3 = 0;
	String TextGI1 = null;
    String TextGI2 = null;
    String TextGI3 = null;
    String TextGI4 = null;
    String TextGI4m = null;
    String TextGI5 = null;
    String TextSL3 = null;
    String TextSL4 = null;
    String TextSL5 = null;
    String idofList = null;
    String newversion = null;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.good_info);
	    
	    TextView viewGI1 = (TextView) findViewById(R.id.textViewGI1);
	    TextView viewGI2 = (TextView) findViewById(R.id.textViewGI2);
	    TextView viewGI3 = (TextView) findViewById(R.id.textViewGI3);
	    TextView viewGI4 = (TextView) findViewById(R.id.textViewGI4);
	    TextView viewGI5 = (TextView) findViewById(R.id.textViewGI5);
	    mButtonBuy = (Button) findViewById(R.id.buy_bt);
	    Intent intent = getIntent();
	    TextGI1 = intent.getStringExtra("nameToGood");
	    TextGI2 = intent.getStringExtra("idReal");
	    TextGI3 = intent.getStringExtra("DateToGood");
	    TextGI4 = intent.getStringExtra("StatusToGood");
	    TextGI5 = intent.getStringExtra("ListToGood");
	    TextSL3 = intent.getStringExtra("DateToList");
	    TextSL4 = intent.getStringExtra("ContactToList");
	    TextSL5 = intent.getStringExtra("VersionToList");
	    idofList = intent.getStringExtra("idOfList");
	    iName = intent.getStringExtra("iname");
	    
	    int stat_num=0;
	    try {stat_num = Integer.parseInt(TextGI4.trim());} catch (NumberFormatException nfe) {};
		if (stat_num == 0) {
			TextGI4m = "Не куплено";
		} else {TextGI4m = "Куплено"; mButtonBuy.setVisibility(View.INVISIBLE);}
		
		
	    viewGI1.setText(TextGI1);
	    viewGI2.setText(TextGI2);
	    viewGI3.setText(TextGI3);
	    viewGI4.setText(TextGI4m);
	    viewGI5.setText(TextGI5);
	    
	    nameFromList = TextGI1;
	    idFromList = TextGI2;
		
	}
	//--------------END OF onCreate----------------------------

	//----------Buttons-------------------------------
	
	public void buying(View view) {
		log_to_SrvBuy = new LOGtoSrvBuy();
    	log_to_SrvBuy.execute();
    	
    	while(gogoodinfo!=1){/*Log.d(LOG_TAG, "gogoodinfo = " + gogoodinfo);*/}
    	
    	int ans_num=0;
    	int version_num=0;
    	if (ansver != null) {
    		try {ans_num = Integer.parseInt(ansver.trim());} catch (NumberFormatException nfe) {};
    		if (ans_num == 1) {
    			try {version_num = Integer.parseInt(TextSL5.trim());} catch (NumberFormatException nfe) {};
    			version_num++;
    			newversion = Integer.toString(version_num);
    			Intent intentSL = new Intent(this, ShopList.class);
    			  intentSL.putExtra("nameToList", TextGI5);
    			  intentSL.putExtra("idReal", idofList);
    			  intentSL.putExtra("DateToList", TextSL3);
    			  intentSL.putExtra("ContactToList",TextSL4);
    			  intentSL.putExtra("VersionToList", newversion);
    			  intentSL.putExtra("iname", iName);
    		      startActivity(intentSL);
    		}
    	}
		
	}
	
	//---------End-of-Buttons-------------------------------
	
	//---------------Functions-------------------
	
	
	
	//----------END OF Functions-----------------
	
	//-----------Classes---------------------
	private class LOGtoSrvBuy extends AsyncTask<Void, Void, Integer> {

		@Override
		protected Integer doInBackground(Void... arg0) {
			
			try {
				URL url = new URL(server_name + "/srv.php?action=buygood&idgoodtobuy=" + idFromList);
				conn = (HttpURLConnection) url.openConnection();
				conn.setConnectTimeout(10000); // ждем 10сек
				conn.setRequestMethod("POST");
				conn.setRequestProperty("User-Agent", "Mozilla/5.0");
				conn.connect();
				res = conn.getResponseCode();
			} catch (Exception e) {
				Log.i("srv","+ ответ сервера ОШИБКА: "+ e.getMessage());
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

				Log.i("srv", "+ полный ответ сервера:\n"
						+ sb.toString());
				ansver = sb.toString();

				is.close();
				br.close();

			} catch (Exception e) {
				Log.i("srv", "+ ошибка: " + e.getMessage());
			} finally {
				conn.disconnect();
				Log.i("srv",
						"+ --------------- ЗАКРОЕМ СОЕДИНЕНИЕ");
			}
			gogoodinfo = 1;
			return res;
		}
    	
    } // End of private class LOGtoSrv extends AsyncTask<Void, Void, Integer>

}
