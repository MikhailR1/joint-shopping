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


public class SecActivity extends Activity {
	
	final String LOG_TAG = "SecActivityLogs";
	Contacts contacts = new Contacts();
	private ListView listViewContacts;
	ShoppingList shoppingList = new ShoppingList();
	private ListView listViewShoppingList;
	private ArrayList<String> contactsItems;
	private ArrayList<String> shoppingListItems;
	//List<Contacts> contactsItems = new ArrayList();
	private ArrayAdapter contactsItemsAdapter;
	private ArrayAdapter shoppingListItemsAdapter;
	
	String server_name = "http://srv.embeddershop.ru";
	String iName = null;
	LOGtoSrv log_to_Srv;
	LOGtoSrv2 log_to_Srv2;
	String ansver = null;
	String ansFromCreate = null;
	HttpURLConnection conn;
	Integer res;
	int gosecact = 0;
	int gosecact2 = 0;
	
	 
	  @Override 
	  protected void onCreate(Bundle savedInstanceState) { 
	    super.onCreate(savedInstanceState);
	    ActiveAndroid.initialize(this);
	    setContentView(R.layout.activity_sec);
    	
	    log_to_Srv = new LOGtoSrv();
    	log_to_Srv.execute();
    	
    	while(gosecact!=1){/*Log.d(LOG_TAG, "gosecact = " + gosecact);*/}
    	
    	log_to_Srv2 = new LOGtoSrv2();
    	log_to_Srv2.execute();
    	
    	while(gosecact2!=1){/*Log.d(LOG_TAG, "gosecact2 = " + gosecact2);*/}
	    
	    TextView inView = (TextView) findViewById(R.id.textView1);
	    Intent intent = getIntent();
	    iName = intent.getStringExtra("iname");
	    ansFromCreate = intent.getStringExtra("ansToSecAct");
	    inView.setText("Дообро пожаловать " + iName);
	    
	    TextView textDebug = (TextView) findViewById(R.id.textDebug);
        textDebug.setText(ansFromCreate);
	    
	    listViewShoppingList = (ListView) findViewById(R.id.listViewShoppingList);
	    shoppingListItems = new ArrayList();    
	    showShoppingList();
	    
	    listViewShoppingList.setOnItemClickListener(new OnItemClickListener() {
	        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	          Log.d(LOG_TAG, "itemClick: position = " + position + ", id = " + id);
	          goToShopList(Long.toString(id));
	        }
	      });
	    
	    
	    
	  } //--------------END OF onCreate----------------------------
	  
//----------Buttons-------------------------------	  
	 
	  public void creating(View view) {
		  Contacts contactsgetOne = getOneContactByName(iName);
		  Intent intentCSL = new Intent(this, CreateShopList.class);
		  intentCSL.putExtra("iname", iName);
		  intentCSL.putExtra("cont_id", contactsgetOne.contact_id);
	      startActivity(intentCSL);
	    
	  }	  
	  
	  public void editing(View view) {
		  super.recreate();
		  //contactsItemsAdapter.notifyDataSetChanged();
		  //new Delete().from(Contacts.class).execute();
		  //ShoppingList shoppingListgetOne = getOne("1");
		  //TextView textDebug = (TextView) findViewById(R.id.textDebug);
          //textDebug.setText(shoppingListgetOne.shlist_title);
	  }
	  
//---------End-of-Buttons-------------------------------	  
	  
//----------------------------------------	
	  
//---------------Functions-------------------
	  
	  private ShoppingList getOne(String id) {
	        return new Select()
	                .from(ShoppingList.class)
	                .where("id_inlist = ?", id)
	                .executeSingle();
	    }
	  
	  private List<ShoppingList> getAll() {
	        //Getting all items stored in Inventory table
	        return new Select()
	                .from(ShoppingList.class)
	                .orderBy("id ASC")
	                .execute();
	    }
	  
	  private Contacts getOneContactByID(String id) {
	        return new Select()
	                .from(Contacts.class)
	                .where("contact_id = ?", id)
	                .executeSingle();
	    }
	  
	  private Contacts getOneContactByName(String name) {
	        return new Select()
	                .from(Contacts.class)
	                .where("name = ?", name)
	                .executeSingle();
	    }
	  
	  /*private void prepareShoppingList() {
		    
	  }*/
	  
	  private void showShoppingList() {
		  Integer del_num_list = 0;
	        List<ShoppingList> inshlist = getAll();
	 
	        for (int i = 0; i < inshlist.size(); i++) {
	        	ShoppingList shoppingList = inshlist.get(i);
	        		String[] del_str = shoppingList.shlist_deleted.split("");
	        		try {del_num_list = Integer.parseInt(del_str[1]);} catch (NumberFormatException nfe) {};
	        		if (del_num_list != 1){
	        	shoppingListItems.add(shoppingList.shlist_title);
	        		}
	        }
	 
	        //Creating our adapter
	        shoppingListItemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, shoppingListItems);
	 
	        //Adding adapter to listview
	        listViewShoppingList.setAdapter(shoppingListItemsAdapter);
	 
	        //Updating the inventory list 
	        updateInventoryList();
	    }
	  private void updateInventoryList() {
		  shoppingListItemsAdapter.notifyDataSetChanged();
	    }
	  
	  private void goToShopList(String id_list) {
		  ShoppingList shoppingListgetOne = getOne(id_list);
		  Contacts contactsgetOne = getOneContactByID(shoppingListgetOne.shlist_id_contact);
		  Intent intentSL = new Intent(this, ShopList.class);
		  intentSL.putExtra("nameToList", shoppingListgetOne.shlist_title);
		  intentSL.putExtra("idReal", shoppingListgetOne.shlist_id);
		  intentSL.putExtra("DateToList", shoppingListgetOne.shlist_date);
		  intentSL.putExtra("ContactToList", contactsgetOne.name);
		  intentSL.putExtra("VersionToList", shoppingListgetOne.shlist_version);
		  intentSL.putExtra("iname", iName);
	      startActivity(intentSL);
	  }
	  
	  /*
	  private List<Contacts> getAll() {
	        return new Select()
	                .from(Contacts.class)
	                .orderBy("RANDOM()")
	                .execute();
	    }
	  
	  private void populate(List<Contacts> list) {
		  ArrayList<String> al = new ArrayList<String>();
		  for (int i = 0; i < list.size(); i++) {
	            al.add(list.get(i).contact_id + "---" + list.get(i).name);
	        }
		  final Dialog dialog = new Dialog(this);
	      dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
	      dialog.setContentView(R.layout.dialog);
	      dialog.setCancelable(true);
	      ListView lv = (ListView) dialog.findViewById(R.id.listView);
	      ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, al);
	      lv.setAdapter(adapter);
	      dialog.show();
			
		}
	  */
	  
	  /*
	  public static Item getRandom() {
			return new Select().from(Item.class).orderBy("RANDOM()").executeSingle();
	  }
	  */
	  
	  
//-----------Classes---------------------
	  
	  private class LOGtoSrv extends AsyncTask<Void, Void, Integer> {


			@Override
			protected Integer doInBackground(Void... arg0) {
				
				try {
					URL url = new URL(server_name + "/srv.php?action=selshlist");
					conn = (HttpURLConnection) url.openConnection();
					conn.setConnectTimeout(10000); // ждем 10сек
					conn.setRequestMethod("POST");
					conn.setRequestProperty("User-Agent", "Mozilla/5.0");
					conn.connect();
					res = conn.getResponseCode();
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
					ansver = ansver.substring(0, ansver.indexOf("]") + 1);

					is.close();
					br.close();

				} catch (Exception e) {
					Log.i("srv", "+ FoneService ошибка: " + e.getMessage());
				} finally {
					conn.disconnect();
					Log.i("srv",
							"+ FoneService --------------- ЗАКРОЕМ СОЕДИНЕНИЕ");
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
						
						new Delete().from(ShoppingList.class).execute();

						while (i < ja.length()) {

							// разберем JSON массив построчно
							jo = ja.getJSONObject(i);

							Log.i("srv",
									"=================>>> "
											+ jo.getString("id")
											+ " | "
											+ jo.getString("shop_list_uuid")
											+ " | " + jo.getString("title")
											+ " | " + jo.getString("create_date")
											+ " | " + jo.getString("sync")
											+ " | " + jo.getString("deleted")
											+ " | " + jo.getString("id_contact")
											+ " | " + jo.getString("version"));

							// введем запись
							String[] del_str = jo.getString("deleted").split("");
							try {del_num = Integer.parseInt(del_str[1]);} catch (NumberFormatException nfe) {};
							
							
							shoppingList = new ShoppingList();
							shoppingList.shlist_id = jo.getString("id");
							shoppingList.shlist_uuid = jo.getString("shop_list_uuid");
							shoppingList.shlist_title = jo.getString("title");
							shoppingList.shlist_date = jo.getString("create_date");
							shoppingList.shlist_sync = jo.getString("sync");
							shoppingList.shlist_deleted = jo.getString("deleted");
							shoppingList.shlist_id_contact = jo.getString("id_contact");
							shoppingList.shlist_version = jo.getString("version");
							if (del_num != 1){shoppingList.id_inlist = Integer.toString(i2);i2++;}
							shoppingList.save();
							
							

							i++;

						}
					} catch (Exception e) {
						//  если ответ сервера не содержит валидный JSON
						Log.i("chat",
								"+ FoneService ---------- ошибка ответа сервера:\n"
										+ e.getMessage());
					}
				}
				
				gosecact = 1;
				return res;
			} // End of protected Integer doInBackground(Void... arg0)
	    	
	    } // End of private class LOGtoSrv extends AsyncTask<Void, Void, Integer>
	  
//-----------------------------------------------------------------------------------	  
	  
	  private class LOGtoSrv2 extends AsyncTask<Void, Void, Integer> {


			@Override
			protected Integer doInBackground(Void... arg0) {
				
				try {
					URL url = new URL(server_name + "/srv.php?action=selcont");
					conn = (HttpURLConnection) url.openConnection();
					conn.setConnectTimeout(10000); // ждем 10сек
					conn.setRequestMethod("POST");
					conn.setRequestProperty("User-Agent", "Mozilla/5.0");
					conn.connect();
					res = conn.getResponseCode();
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
					ansver = ansver.substring(0, ansver.indexOf("]") + 1);

					is.close();
					br.close();

				} catch (Exception e) {
					Log.i("srv", "+ FoneService ошибка: " + e.getMessage());
				} finally {
					conn.disconnect();
					Log.i("srv",
							"+ FoneService --------------- ЗАКРОЕМ СОЕДИНЕНИЕ");
				}
				
				if (ansver != null && !ansver.trim().equals("")) {

					Log.i("srv", "ответ содержит JSON:");
					
					try {
						// ответ превратим в JSON массив
						JSONArray ja = new JSONArray(ansver);
						JSONObject jo;

						Integer i = 0;
						Integer del_num = 0;
						
						new Delete().from(Contacts.class).execute();

						while (i < ja.length()) {

							// разберем JSON массив построчно
							jo = ja.getJSONObject(i);

							Log.i("srv",
									"=================>>> "
											+ jo.getString("id")
											+ " | "
											+ jo.getString("contact_uuid")
											+ " | " + jo.getString("name")
											+ " | " + jo.getString("pass")
											+ " | " + jo.getString("last_activity")
											+ " | " + jo.getString("status_message")
											+ " | " + jo.getString("deleted"));

							// введем запись

							contacts = new Contacts();
							contacts.contact_id = jo.getString("id");
							contacts.contact_uuid = jo.getString("contact_uuid");
							contacts.name = jo.getString("name");
							contacts.pass = jo.getString("pass");
							contacts.last_activity = jo.getString("last_activity");
							contacts.status_message = jo.getString("status_message");
							contacts.deleted = jo.getString("deleted");
							contacts.save();


							i++;

						}
					} catch (Exception e) {
						//  если ответ сервера не содержит валидный JSON
						Log.i("chat",
								"+ FoneService ---------- ошибка ответа сервера:\n"
										+ e.getMessage());
					}
				}
				
				gosecact2 = 1;
				return res;
			} // End of protected Integer doInBackground(Void... arg0)
	    	
	    } // End of private class LOGtoSrv extends AsyncTask<Void, Void, Integer>

	  
	  
	}
