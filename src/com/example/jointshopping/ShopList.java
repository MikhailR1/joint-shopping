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

public class ShopList extends Activity {
	
	Goods goods = new Goods();
	private ListView listViewGoods;
	private ArrayList<String> goodsItems;
	private ArrayAdapter goodsAdapter;
	
	
	final String LOG_TAG = "ShopListLogs";
	String server_name = "http://srv.embeddershop.ru";
	LOGtoSrv log_to_Srv;
	LOGtoSrvDel log_to_SrvDel;
	LOGtoSrvAdd log_to_SrvAdd;
	String ansver = null;
	String ansver2 = null;
	String ansver3 = null;
	HttpURLConnection conn;
	Integer res;
	Integer res2;
	Integer res3;
	int goshoplistact = 0;
	int goshoplistact2 = 0;
	int goshoplistact3 = 0;
	String ansToSecAct = " ";
	String iName = null;
	String idFromList = "";
	String nameFromList  = "";
	String addgoodname = null;
	String TextSL1 = null;
	String TextSL2 = null;
	String TextSL3 = null;
	String TextSL4 = null;
	String TextSL5 = null;
	
	protected void onCreate(Bundle savedInstanceState) { 
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.shop_list);
	    
	    TextView viewSL1 = (TextView) findViewById(R.id.textViewSL1);
	    TextView viewSL2 = (TextView) findViewById(R.id.textViewSL2);
	    TextView viewSL3 = (TextView) findViewById(R.id.textViewSL3);
	    TextView viewSL4 = (TextView) findViewById(R.id.textViewSL4);
	    TextView viewSL5 = (TextView) findViewById(R.id.textViewSL5);
	    Intent intent = getIntent();
	    TextSL1 = intent.getStringExtra("nameToList");
	    TextSL2 = intent.getStringExtra("idReal");
	    TextSL3 = intent.getStringExtra("DateToList");
	    TextSL4 = intent.getStringExtra("ContactToList");
	    TextSL5 = intent.getStringExtra("VersionToList");
	    iName = intent.getStringExtra("iname");
	    viewSL1.setText(TextSL1);
	    viewSL2.setText(TextSL2);
	    viewSL3.setText(TextSL3);
	    viewSL4.setText(TextSL4);
	    viewSL5.setText(TextSL5);
	    
	    nameFromList = TextSL1;
	    idFromList = TextSL2;
	    
	    log_to_Srv = new LOGtoSrv();
    	log_to_Srv.execute();
    	
    	while(goshoplistact!=1){/*Log.d(LOG_TAG, "goshoplistact = " + goshoplistact);*/}

    	
    	listViewGoods = (ListView) findViewById(R.id.listViewGoods);
    	goodsItems = new ArrayList();    
	    showGoodsList();
	    
	    listViewGoods.setOnItemClickListener(new OnItemClickListener() {
	        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	          Log.d(LOG_TAG, "itemClick: position = " + position + ", id = " + id);
	          goToGoodList(Long.toString(id));
	        }
	      });
	    
	} 
	//--------------END OF onCreate----------------------------

//----------Buttons-------------------------------	
	
	public void deleting(View view) {
		log_to_SrvDel = new LOGtoSrvDel();
    	log_to_SrvDel.execute();
    	
    	while(goshoplistact2!=1){/*Log.d(LOG_TAG, "goshoplistact2 = " + goshoplistact2);*/}
    	
    	int ans_num2=0;
    	if (ansver2 != null) {
    		try {ans_num2 = Integer.parseInt(ansver2.trim());} catch (NumberFormatException nfe) {};
    		if (ans_num2 == 1) {
    			String ansToSecAct = "Список успешно удален";
        		Intent intentSec = new Intent(this, SecActivity.class);
    			intentSec.putExtra("iname", iName);
    			intentSec.putExtra("ansToSecAct", ansToSecAct);
    	        startActivity(intentSec);
    		}
    	}
	  }
	
	public void adding(View view) {
		final EditText edit_addgood = (EditText)findViewById(R.id.good_add);
		addgoodname = edit_addgood.getText().toString();
		if (edit_addgood.getText().toString().equals("")){
			// ничего не делаем
		} else { // добавляем покупку
			log_to_SrvAdd = new LOGtoSrvAdd();
        	log_to_SrvAdd.execute();
        	
        	while(goshoplistact3!=1){/*Log.d(LOG_TAG, "goshoplistact3 = " + goshoplistact3);*/}
        	
        	super.recreate();
		}
		
	}
	
//---------End-of-Buttons-------------------------------
	
//---------------Functions-------------------
	private Goods getOne(String id) {
        return new Select()
                .from(Goods.class)
                .where("goods_id_inourlist = ?", id)
                .executeSingle();
    }
	
	private List<Goods> getAll() {
        return new Select()
                .from(Goods.class)
                .orderBy("id ASC")
                .execute();
    }
	
	private void showGoodsList() {
		Integer del_num_list = 0;
        List<Goods> inGoodslist = getAll();
 
        for (int i = 0; i < inGoodslist.size(); i++) {
        	Goods goods = inGoodslist.get(i);
        		String[] del_str = goods.goods_deleted.split("");
        		try {del_num_list = Integer.parseInt(del_str[1]);} catch (NumberFormatException nfe) {};
        		if (del_num_list != 1){
        	goodsItems.add(goods.goods_title);
        		}
        }
 
        //Creating our adapter
        goodsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, goodsItems);
 
        //Adding adapter to listview
        listViewGoods.setAdapter(goodsAdapter);
 
        //Updating the inventory list 
        updateInventoryList();
    }
  private void updateInventoryList() {
	  goodsAdapter.notifyDataSetChanged();
    }
  
  private void goToGoodList(String id_good) {
	  Goods goodgetOne = getOne(id_good);
	  //Contacts contactsgetOne = getOneContactByID(shoppingListgetOne.shlist_id_contact);
	  Intent intentGI = new Intent(this, GoodInfo.class);
	  intentGI.putExtra("nameToGood", goodgetOne.goods_title);
	  intentGI.putExtra("idReal", goodgetOne.goods_id);
	  intentGI.putExtra("DateToGood", goodgetOne.goods_date);
	  intentGI.putExtra("StatusToGood", goodgetOne.goods_status);
	  intentGI.putExtra("ListToGood", nameFromList);
	  intentGI.putExtra("iname", iName);
	  intentGI.putExtra("DateToList", TextSL3);
	  intentGI.putExtra("ContactToList", TextSL4);
	  intentGI.putExtra("VersionToList", TextSL5);
	  intentGI.putExtra("idOfList", idFromList);
      startActivity(intentGI);
  }
	
	
//----------END OF Functions-----------------
	
	
//-----------Classes---------------------
	  
	  private class LOGtoSrv extends AsyncTask<Void, Void, Integer> {


			@Override
			protected Integer doInBackground(Void... arg0) {
				
				try {
					URL url = new URL(server_name + "/srv.php?action=selgoodsbyid&id_list="+idFromList);
					conn = (HttpURLConnection) url.openConnection();
					conn.setConnectTimeout(10000); // ждем 10сек
					conn.setRequestMethod("POST");
					conn.setRequestProperty("User-Agent", "Mozilla/5.0");
					conn.connect();
					res = conn.getResponseCode();
				} catch (Exception e) {
					Log.i("srv","+ ShopList - ответ сервера ОШИБКА: "+ e.getMessage());
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

					Log.i("srv", "+ ShopList - полный ответ сервера:\n"
							+ sb.toString());
					ansver = sb.toString();
					ansver = ansver.substring(0, ansver.indexOf("]") + 1);

					is.close();
					br.close();

				} catch (Exception e) {
					Log.i("srv", "+ ShopList ошибка: " + e.getMessage());
				} finally {
					conn.disconnect();
					Log.i("srv",
							"+ ShopList --------------- ЗАКРОЕМ СОЕДИНЕНИЕ");
				}
				
				if (ansver != null && !ansver.trim().equals("")) {

					Log.i("srv", "ответ содержит JSON:");
					
					try {
						// ответ превратим в JSON массив
						JSONArray ja = new JSONArray(ansver);
						JSONObject jo;

						Integer i = 0;
						Integer i2 = 0;
						Integer del_num = 0;
						
						new Delete().from(Goods.class).execute();

						while (i < ja.length()) {

							// разберем JSON массив построчно
							jo = ja.getJSONObject(i);

							Log.i("srv",
									"=================>>> "
											+ jo.getString("id")
											+ " | "
											+ jo.getString("good_uuid")
											+ " | " + jo.getString("title")
											+ " | " + jo.getString("create_date")
											+ " | " + jo.getString("status")
											+ " | " + jo.getString("sync")
											+ " | " + jo.getString("deleted")
											+ " | " + jo.getString("id_list"));

							// введем запись
							String[] del_str = jo.getString("deleted").split("");
							try {del_num = Integer.parseInt(del_str[1]);} catch (NumberFormatException nfe) {};
							
							
							goods = new Goods();
							goods.goods_id = jo.getString("id");
							goods.goods_uuid = jo.getString("good_uuid");
							goods.goods_title = jo.getString("title");
							goods.goods_date = jo.getString("create_date");
							goods.goods_status = jo.getString("status");
							goods.goods_sync = jo.getString("sync");
							goods.goods_deleted = jo.getString("deleted");
							goods.goods_id_list = jo.getString("id_list");
							if (del_num != 1){goods.goods_id_inourlist = Integer.toString(i2);i2++;}
							goods.save();
							
							

							i++;

						}
					} catch (Exception e) {
						//  если ответ сервера не содержит валидный JSON
						Log.i("chat",
								"+ ShopList ---------- ошибка ответа сервера:\n"
										+ e.getMessage());
					}
				}
				goshoplistact = 1;
				return res;
			} // End of protected Integer doInBackground(Void... arg0)
	    	
	    } // End of private class LOGtoSrv extends AsyncTask<Void, Void, Integer>
	  
	  //---------------------------------------------
	  
	  private class LOGtoSrvDel extends AsyncTask<Void, Void, Integer> {

			@Override
			protected Integer doInBackground(Void... arg0) {
				
				try {
					URL url = new URL(server_name + "/srv.php?action=dellist&idtodel=" + idFromList);
					conn = (HttpURLConnection) url.openConnection();
					conn.setConnectTimeout(10000); // ждем 10сек
					conn.setRequestMethod("POST");
					conn.setRequestProperty("User-Agent", "Mozilla/5.0");
					conn.connect();
					res2 = conn.getResponseCode();
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
				goshoplistact2 = 1;
				return res2;
			}
	    	
	    } // End of private class LOGtoSrv extends AsyncTask<Void, Void, Integer>
	  
//---------------------------------------------
	  
	  private class LOGtoSrvAdd extends AsyncTask<Void, Void, Integer> {

			@Override
			protected Integer doInBackground(Void... arg0) {
				
				try {
					URL url = new URL(server_name + "/srv.php?action=insertgood&title_good=" + addgoodname + "&id_list_tosrv=" + idFromList);
					conn = (HttpURLConnection) url.openConnection();
					conn.setConnectTimeout(10000); // ждем 10сек
					conn.setRequestMethod("POST");
					conn.setRequestProperty("User-Agent", "Mozilla/5.0");
					conn.connect();
					res3 = conn.getResponseCode();
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
					ansver3 = sb.toString();

					is.close();
					br.close();

				} catch (Exception e) {
					Log.i("srv", "+ ошибка: " + e.getMessage());
				} finally {
					conn.disconnect();
					Log.i("srv",
							"+ --------------- ЗАКРОЕМ СОЕДИНЕНИЕ");
				}
				goshoplistact3 = 1;
				return res3;
			}
	    	
	    } // End of private class LOGtoSrv extends AsyncTask<Void, Void, Integer>
	
	
}
