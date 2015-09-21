package com.insep.navidemo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.content.res.Resources;

import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class MarkersList {

	private static ArrayList<MarkerInfo> l1_attractions = new ArrayList<MarkerInfo>();
	private static ArrayList<MarkerInfo> l1_shopping = new ArrayList<MarkerInfo>();
	private static ArrayList<MarkerInfo> l1_food = new ArrayList<MarkerInfo>();
	private static ArrayList<MarkerInfo> l2_attractions = new ArrayList<MarkerInfo>();
	private static ArrayList<MarkerInfo> l2_stores = new ArrayList<MarkerInfo>();
	private static ArrayList<MarkerInfo> l2_restaurants = new ArrayList<MarkerInfo>();
	private static ArrayList<MarkerInfo> l2_washrooms = new ArrayList<MarkerInfo>();
	private static Resources res;

	public MarkersList(Resources res) {
		MarkersList.res = res;
	}

	public ArrayList<MarkerInfo> getL1Attractions() {
		ArrayList<AttractionsAndFacilitie> temp = MarkersList
				.getJson("l1_attractions.json");
		int j = temp.size();
		for (int i = 0; i < j; i++) {
			AttractionsAndFacilitie t = temp.get(i);
			l1_attractions.add(new MarkersList.MarkerInfo(new MarkerOptions()
					.position(new LatLng(t.latitude, t.longitude))
					.title(t.title)
					.snippet(t.snippet)
					.icon(BitmapDescriptorFactory.fromResource(res
							.getIdentifier("landmark_" + t.description,
									"drawable", "com.insep.navidemo")))
					.draggable(false), t.description));
		}
		return l1_attractions;
	}

	public ArrayList<MarkerInfo> getL1Shopping() {
		ArrayList<AttractionsAndFacilitie> temp = MarkersList
				.getJson("l1_shopping.json");
		int j = temp.size();
		for (int i = 0; i < j; i++) {
			AttractionsAndFacilitie t = temp.get(i);
			l1_shopping.add(new MarkerInfo(new MarkerOptions()
					.position(new LatLng(t.latitude, t.longitude))
					.title(t.title)
					.snippet(t.snippet)
					.icon(BitmapDescriptorFactory.fromResource(res
							.getIdentifier("landmark_" + t.description,
									"drawable", "com.insep.navidemo")))
					.draggable(false), t.description));
		}
		return l1_shopping;
	}

	public ArrayList<MarkerInfo> getL1Food() {

		ArrayList<AttractionsAndFacilitie> temp = MarkersList
				.getJson("l1_food.json");
		int j = temp.size();
		for (int i = 0; i < j; i++) {
			AttractionsAndFacilitie t = temp.get(i);
			l1_food.add(new MarkerInfo(new MarkerOptions()
					.position(new LatLng(t.latitude, t.longitude))
					.title(t.title)
					.snippet(t.snippet)
					.icon(BitmapDescriptorFactory.fromResource(res
							.getIdentifier("landmark_" + t.description,
									"drawable", "com.insep.navidemo")))
					.draggable(false), t.description));
		}
		return l1_food;
	}
	
	public ArrayList<MarkerInfo> getL2Attractions() {

		ArrayList<AttractionsAndFacilitie> temp = MarkersList
				.getJson("l2_attractions.json");
		int j = temp.size();
		for (int i = 0; i < j; i++) {
			AttractionsAndFacilitie t = temp.get(i);
			l2_attractions.add(new MarkerInfo(new MarkerOptions()
					.position(new LatLng(t.latitude, t.longitude))
					.title(t.title)
					.snippet(t.snippet)
					.icon(BitmapDescriptorFactory.fromResource(res
							.getIdentifier("landmark_" + t.description.substring(1),
									"drawable", "com.insep.navidemo")))
					.draggable(false).visible(false), t.description));
		}
		return l2_attractions;
	}
	public ArrayList<MarkerInfo> getL2Stores() {

		ArrayList<AttractionsAndFacilitie> temp = MarkersList
				.getJson("l2_stores.json");
		int j = temp.size();
		for (int i = 0; i < j; i++) {
			AttractionsAndFacilitie t = temp.get(i);
			l2_stores.add(new MarkerInfo(new MarkerOptions()
					.position(new LatLng(t.latitude, t.longitude))
					.title(t.title)
					.snippet(t.snippet)
					.icon(BitmapDescriptorFactory.fromResource(res
							.getIdentifier("landmark_" + t.description.substring(1),
									"drawable", "com.insep.navidemo")))
					.draggable(false).visible(false), t.description));
		}
		return l2_stores;
	}
	public ArrayList<MarkerInfo> getL2Restaurants() {

		ArrayList<AttractionsAndFacilitie> temp = MarkersList
				.getJson("l2_restaurants.json");
		int j = temp.size();
		for (int i = 0; i < j; i++) {
			AttractionsAndFacilitie t = temp.get(i);
			l2_restaurants.add(new MarkerInfo(new MarkerOptions()
					.position(new LatLng(t.latitude, t.longitude))
					.title(t.title)
					.snippet(t.snippet)
					.icon(BitmapDescriptorFactory.fromResource(res
							.getIdentifier("landmark_" + t.description.substring(1),
									"drawable", "com.insep.navidemo")))
					.draggable(false).visible(false), t.description));
		}
		return l2_restaurants;
	}
	public ArrayList<MarkerInfo> getL2Washrooms() {

		ArrayList<AttractionsAndFacilitie> temp = MarkersList
				.getJson("l2_washrooms.json");
		int j = temp.size();
		for (int i = 0; i < j; i++) {
			AttractionsAndFacilitie t = temp.get(i);
			l2_washrooms.add(new MarkerInfo(new MarkerOptions()
					.position(new LatLng(t.latitude, t.longitude))
					.title(t.title)
					.snippet(t.snippet)
					.icon(BitmapDescriptorFactory.fromResource(res
							.getIdentifier("landmark_" + t.description.substring(1),
									"drawable", "com.insep.navidemo")))
					.draggable(false).visible(false), t.description));
		}
		return l2_washrooms;
	}
	// 解析JSON
	private static ArrayList<MarkersList.AttractionsAndFacilitie> getJson(
			String fileName) {
		StringBuilder stringBuilder = new StringBuilder();
		try {
			BufferedReader bf = new BufferedReader(new InputStreamReader(res
					.getAssets().open(fileName)));
			String line;
			while ((line = bf.readLine()) != null) {
				stringBuilder.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		ArrayList<MarkersList.AttractionsAndFacilitie> list = new ArrayList<MarkersList.AttractionsAndFacilitie>();
		Gson gson = new Gson();
		list = gson
				.fromJson(
						stringBuilder.toString(),
						new TypeToken<ArrayList<MarkersList.AttractionsAndFacilitie>>() {
						}.getType());
		return list;
	}

	// 封装JSON类
	class AttractionsAndFacilitie {

		public String description;
		public String title;
		public String snippet;
		public double longitude;
		public double latitude;

		public AttractionsAndFacilitie() {
		}
	}

	// 传递类
	class MarkerInfo {

		public MarkerOptions markerOption;
		public String description;

		public MarkerInfo(MarkerOptions mo, String d) {
			this.markerOption = mo;
			this.description = d;
		}
	}

}