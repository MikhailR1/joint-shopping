package com.example.firstandroidapp;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
 
import java.util.List;


@Table(name = "Goods")

public class Goods extends Model{
 
	@Column(name = "goods_id")
    public String goods_id;
	
	@Column(name = "goods_uuid")
    public String goods_uuid;
	
	@Column(name = "goods_title")
    public String goods_title;
	
	@Column(name = "goods_date")
    public String goods_date;
	
	@Column(name = "goods_status")
    public String goods_status;
	
	@Column(name = "goods_sync")
    public String goods_sync;
	
	@Column(name = "goods_deleted")
    public String goods_deleted;
	
	@Column(name = "goods_id_list")
    public String goods_id_list;
	
	@Column(name = "goods_id_inourlist")
    public String goods_id_inourlist;
	
	
	public Goods() {
        super();
	}
	
	public Goods(String goods_id, String goods_uuid, String goods_title, String goods_date, String goods_status, String goods_sync, String goods_deleted, String goods_id_list, String goods_id_inourlist) {
        super();
        this.goods_id = goods_id;
        this.goods_uuid = goods_uuid;
        this.goods_title = goods_title;
        this.goods_date = goods_date;
        this.goods_status = goods_status;
        this.goods_sync = goods_sync;
        this.goods_deleted = goods_deleted;
        this.goods_id_list = goods_id_list;
        this.goods_id_inourlist = goods_id_inourlist;
}
	
	
}