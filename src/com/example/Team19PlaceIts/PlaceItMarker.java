package com.example.Team19PlaceIts;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

public class PlaceItMarker {

	/**
	 * @param args
	 */
	private final String title;
	private final String desc;
	private final String latitude;
	private final String longitude;
	LatLng lt;

	public PlaceItMarker(String t, String d, String x, String y) {
		title = t;
		desc = d;
		this.latitude = x;
		this.longitude = y;
		lt = new LatLng(Integer.parseInt(x), Integer.parseInt(y));
	}

	public PlaceItMarker(Marker m) {
		this.title = m.getTitle();
		this.desc = m.getSnippet();
		this.lt = m.getPosition();
		this.latitude = lt.latitude + "";
		this.longitude = lt.longitude + "";
	}

	public PlaceItMarker(String str) {
		String[] data = str.split("\n");
		assert (data.length == 5);
		title = data[0];
		desc = data[1];
		this.latitude = data[2];
		this.longitude = data[3];
		lt = new LatLng(Integer.parseInt(latitude), Integer.parseInt(longitude));

	}

	public LatLng getLatLng() {
		return lt;
	}

	public String getTitle() {
		return this.title;
	}

	public String getDesc() {
		return this.desc;
	}

	public boolean equals(Object obj) {
		PlaceItMarker that = (PlaceItMarker) obj;
		boolean a = this.lt.equals(that.lt);
		int b = this.title.compareTo(that.title);
		int c = this.desc.compareTo(that.desc);
		return a && b == 0 && c == 0;
	}

	public String toString() {
		return this.title + "\n" + this.desc + "\n" + this.latitude + "\n"
				+ this.longitude + "\n";
	}

}
