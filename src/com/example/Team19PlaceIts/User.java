package com.example.Team19PlaceIts;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.Iterator;
import java.util.List;
import com.example.Team19PlaceIts.PlaceItMarker;
import com.example.Team19PlaceIts.PlaceItCategory;

/**
 * 
 * @author mustafa
 */

public class User {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String dataBaseCat = "###food place####";
		String dataBaseM = "#####";
		User u = new User("s@g.com", "mustafa", "sidiqi"); // phone 1
		User u1 = new User("s@g.com", "mustafa", "sidiqi"); // phone 2

		// read from database
		u.setCategory(dataBaseCat);
		u.setMarker(dataBaseM);
		// user adds things
		u.addCateogry(new PlaceItCategory("library"));
		PlaceItCategory p = new PlaceItCategory("pizza");
		u.addCateogry(p);
		//
		u1.addCateogry(new PlaceItCategory("lunge"));
		//
		u.addMarker(new PlaceItMarker("study", "study at library", "1.0", "2.0")); //
		// write to data base
		dataBaseCat = u.getCategory();
		dataBaseM = u.getMarker();
		// user two online, read from online
		u1.setCategory(dataBaseCat);
		u1.setMarker(dataBaseM);
		// user 2 adds things
		u1.addCateogry(new PlaceItCategory("bank"));
		u1.addCateogry(new PlaceItCategory("casino"));
		u1.removeCategory(p);
		u1.addMarker(new PlaceItMarker("buy", "buy at amazon", "145.0", "42.0"));
		u.addMarker(new PlaceItMarker("sell", "sell at ebay", "9814.0",
				"3302.0"));
		u1.removeMarker(new PlaceItMarker("study", "study at library", "1.0",
				"2.0"));
		// user 2 udate to server
		dataBaseCat = u1.getCategory();
		dataBaseM = u1.getMarker();
		// user 1, reads things from server
		u.setCategory(dataBaseCat);
		u.setMarker(dataBaseM);
		//
		for (PlaceItMarker s : u.traversMarker()) {
			System.out.println(s);
		}
		for (PlaceItCategory s : u.traversCatagory()) {
			System.out.println(s);
		}
		System.out.println(u.getCategory());
		System.out.println(u.getMarker());
	}

	public static void update(String to, String from) {
		to = from;
	}

	private final String emailAddress;
	private final String firstName;
	private final String lastName;
	private String id;
	private List<PlaceItMarker> marker;
	private List<PlaceItMarker> markerTracker;
	private List<PlaceItCategory> category;
	private List<PlaceItCategory> categoryTracker;

	public User(String em, String first, String last) {
		this.emailAddress = em;
		this.firstName = first;
		this.lastName = last;
		this.category = new java.util.ArrayList<PlaceItCategory>();
		this.marker = new java.util.ArrayList<PlaceItMarker>();
		this.categoryTracker = new java.util.ArrayList<PlaceItCategory>();
		this.markerTracker = new java.util.ArrayList<PlaceItMarker>();
	}

	public void addCateogry(PlaceItCategory c) {
		this.categoryTracker.add(c);
		this.category.add(c);
	}

	public void addMarker(PlaceItMarker m) {
		this.markerTracker.add(m);
		this.marker.add(m);
	}

	public boolean removeCategory(PlaceItCategory c) {
		this.categoryTracker.remove(c);
		return this.category.remove(c);
	}

	public boolean removeMarker(PlaceItMarker m) {
		this.markerTracker.remove(m);
		return this.marker.remove(m);
	}

	public Iterable<PlaceItCategory> traversCatagory() {
		return this.category;
	}

	public Iterable<PlaceItMarker> traversMarker() {
		return this.marker;
	}

	public String getCategory() {
		// get category, all info from category list is is returned
		this.categoryTracker.clear(); // reset tracker, all send to updated
		String cat = "";
		for (PlaceItCategory c : this.category) {
			cat += c + "#";
		}
		return cat;
	}

	public String getMarker() {
		this.markerTracker.clear();
		String mark = "";
		for (PlaceItMarker m : this.marker) {
			mark += m + "#";
		}
		return mark;
	}

	public void setMarker(String ms) {
		String[] markers = ms.split("#");
		// empty data base
		this.marker.clear();
		for (int i = 0; i < markers.length; i++) {
			if (markers[i].length() == 0)
				continue;
			this.marker.add(new PlaceItMarker(markers[i]));
		}
		if (this.markerTracker.size() != 0) {
			for (PlaceItMarker t : this.markerTracker) {
				this.marker.add(t);
			}
		}
	}

	public void setCategory(String cat) {
		this.category.clear();
		String[] c = cat.split("#");
		int j = 4;
		for (int i = 0; i < c.length; i++) {
			if (c[i].length() == 0)
				continue;
			this.category.add(new PlaceItCategory(c[i]));
		}
		if (this.categoryTracker.size() != 0) {
			for (PlaceItCategory t : this.categoryTracker) {
				this.category.add(t);
			}
		}
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
