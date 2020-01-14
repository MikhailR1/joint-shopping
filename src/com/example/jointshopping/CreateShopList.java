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

public class CreateShopList extends Activity {
	
	String iName = null;
	String cont_id = null;
	String edittitle = null;
	String editgood = null;
	
	final String LOG_TAG = "ShopListLogs";
	String server_name = "http://srv.embeddershop.ru";
	LOGtoSrv log_to_Srv;
	LOGtoSrv2 log_to_Srv2;
	String ansver = null;
	String ansver2 = null;
	String ansToSecAct = " ";
	String id_from_server = null;
	HttpURLConnection conn;
	Integer res;
	Integer res2;
	int goCSL = 0;
	int goCSL2 = 0;
	
	protected void onCreate(Bundle savedInstanceState) { 
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.create_shop_list);
	    
	    
	    TextView inView = (TextView) findViewById(R.id.textViewCSL1);
	    Intent intentCSL = getIntent();
	    iName = intentCSL.getStringExtra("iname");
	    cont_id = intentCSL.getStringExtra("cont_id");
	    inView.setText(iName);

	    
	} //--------------END OF onCreate----------------------------

	//----------Buttons-------------------------------	
	
	public void createlist(View view) {
    	
    	final EditText edit_title = (EditText)findViewById(R.id.edit_title);
    	edittitle = edit_title.getText().toString();
        final EditText edit_good = (EditText)findViewById(R.id.edit_good);
        editgood = edit_good.getText().toString();
        
        	if ((edit_title.getText().toString().equals(""))||(edit_good.getText().toString().equals(""))){
        		TextView warnView = (TextView) findViewById(R.id.textViewCSL5);
        		warnView.setText("Поля не должны быть пустыми");
        	} else { // если есть данные, отправка данных на сервер
        		// отправка данных на сервер
        		log_to_Srv = new LOGtoSrv();
            	log_to_Srv.execute();
            	
            	while(goCSL!=1){/*Log.d(LOG_TAG, "goCSL = " + goCSL);*/}
            	
            	int ans_num=0;
            	if (ansver != null) {
            		//String[] arr_str = ansver.split("");
            		try {ans_num = Integer.parseInt(ansver.trim());} catch (NumberFormatException nfe) {};
            		if (ans_num != 0) {
            			id_from_server = ansver;
            			
            			// отправка данных на сервер
                		log_to_Srv2 = new LOGtoSrv2();
                    	log_to_Srv2.execute();
                    	while(goCSL2!=1){/*Log.d(LOG_TAG, "goCSL2 = " + goCSL2);*/}
                    	
                    	int ans_num2=0;
                    	if (ansver2 != null) {
                    		try {ans_num2 = Integer.parseInt(ansver2.trim());} catch (NumberFormatException nfe) {};
                    		if (ans_num2 == 1) {
                    			String ansToSecAct = "Список успешно создан";
                    			TextView warnView = (TextView) findViewById(R.id.textViewCSL5);
                        		warnView.setText("Список создан, его id = " + id_from_server);
                        		Intent intent = new Intent(this, SecActivity.class);
                    			intent.putExtra("iname", iName);
                    			intent.putExtra("ansToSecAct", ansToSecAct);
                    	        startActivity(intent);
                    		}
                    	}
            			
            		}
            	} // eof if (ansver != null)
            	
        		
        	} // end of if-else
        

        }
        
	
	//---------End-of-Buttons-------------------------------
	
	//---------------Functions-------------------
	
	
	
	//---------End-of-Functions-------------------
	
	//-----------Classes---------------------
	
	private class LOGtoSrv extends AsyncTask<Void, Void, Integer> {

		@Override
		protected Integer doInBackground(Void... arg0) {
			
			try {
				URL url = new URL(server_name + "/srv.php?action=insertlist&title=" + edittitle + "&contact_id=" + cont_id);
				conn = (HttpURLConnection) url.openConnection();
				conn.setConnectTimeout(10000); // ждем 10сек
				conn.setRequestMethod("POST");
				conn.setRequestProperty("User-Agent", "Mozilla/5.0");
				conn.connect();
				res = conn.getResponseCode();
				//final TextView t_output = (TextView)findViewById(R.id.t_output);
				//t_output.setText(Integer.toString(res));
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
						"+ F --------------- ЗАКРОЕМ СОЕДИНЕНИЕ");
			}
			goCSL = 1;
			return res;
		}
    	
    } // End of private class LOGtoSrv extends AsyncTask<Void, Void, Integer>
	
	//-----------------------------------------------------------------------
	
	private class LOGtoSrv2 extends AsyncTask<Void, Void, Integer> {

		@Override
		protected Integer doInBackground(Void... arg0) {
			
			try {
				URL url = new URL(server_name + "/srv.php?action=insertgood&title_good=" + editgood + "&id_list_tosrv=" + id_from_server);
				conn = (HttpURLConnection) url.openConnection();
				conn.setConnectTimeout(10000); // ждем 10сек
				conn.setRequestMethod("POST");
				conn.setRequestProperty("User-Agent", "Mozilla/5.0");
				conn.connect();
				res2 = conn.getResponseCode();
				//final TextView t_output = (TextView)findViewById(R.id.t_output);
				//t_output.setText(Integer.toString(res));
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
				ansver2 = sb.toString();

				is.close();
				br.close();

			} catch (Exception e) {
				Log.i("srv", "+ ошибка: " + e.getMessage());
			} finally {
				conn.disconnect();
				Log.i("srv",
						"+ --------------- ЗАКРОЕМ СОЕДИНЕНИЕ");
			}
			goCSL2 = 1;
			return res2;
		}
    	
    } // End of private class LOGtoSrv extends AsyncTask<Void, Void, Integer>
	
	//-----End-of-Classes---------------------
	
}
