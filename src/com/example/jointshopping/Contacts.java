package com.example.firstandroidapp;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
 
import java.util.List;


@Table(name = "Contact")

public class Contacts extends Model{
 
	@Column(name = "contact_id")
    public String contact_id;
	
	@Column(name = "contact_uuid")
    public String contact_uuid;
	
	@Column(name = "name")
    public String name;
	
	@Column(name = "pass")
    public String pass;
	
	@Column(name = "last_activity")
    public String last_activity;

	@Column(name = "status_message")
    public String status_message;
	
	@Column(name = "deleted")
    public String deleted;
	
	public Contacts() {
        super();
	}
	
	public Contacts(String contact_id, String contact_uuid, String name, String pass, String last_activity, String status_message, String deleted) {
        super();
        this.contact_id = contact_id;
        this.contact_id = contact_uuid;
        this.name = name;
        this.name = pass;
        this.name = last_activity;
        this.name = status_message;
        this.name = deleted;
}
	
	
}