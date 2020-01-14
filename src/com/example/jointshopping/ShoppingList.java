package com.example.firstandroidapp;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
 
import java.util.List;


@Table(name = "ShoppingList")

public class ShoppingList extends Model{
 
	@Column(name = "shlist_id")
    public String shlist_id;
	
	@Column(name = "shlist_uuid")
    public String shlist_uuid;
	
	@Column(name = "shlist_title")
    public String shlist_title;
	
	@Column(name = "shlist_date")
    public String shlist_date;
	
	@Column(name = "shlist_sync")
    public String shlist_sync;
	
	@Column(name = "shlist_deleted")
    public String shlist_deleted;
	
	@Column(name = "shlist_id_contact")
    public String shlist_id_contact;
	
	@Column(name = "shlist_version")
    public String shlist_version;
	
	@Column(name = "id_inlist")
    public String id_inlist;
	
	public ShoppingList() {
        super();
	}
	
	public ShoppingList(String shlist_id, String shlist_uuid, String shlist_title, String shlist_date, String shlist_sync, String shlist_deleted, String shlist_id_contact, String shlist_version, String id_inlist) {
        super();
        this.shlist_id = shlist_id;
        this.shlist_uuid = shlist_uuid;
        this.shlist_title = shlist_title;
        this.shlist_date = shlist_date;
        this.shlist_sync = shlist_sync;
        this.shlist_deleted = shlist_deleted;
        this.shlist_id_contact = shlist_id_contact;
        this.shlist_version = shlist_version;
        this.id_inlist = id_inlist;
}
	
	
}