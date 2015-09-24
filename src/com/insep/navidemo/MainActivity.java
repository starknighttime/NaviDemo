package com.insep.navidemo;

import java.util.ArrayList;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.RouteSearch.OnRouteSearchListener;
import com.amap.api.services.route.RouteSearch.WalkRouteQuery;
import com.amap.api.services.route.WalkPath;
import com.amap.api.services.route.WalkRouteResult;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMap.InfoWindowAdapter;
import com.amap.api.maps2d.AMap.OnMapClickListener;
import com.amap.api.maps2d.AMap.OnMarkerClickListener;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource.OnLocationChangedListener;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.MapsInitializer;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.amap.api.maps2d.overlay.WalkRouteOverlay;
import com.amap.api.services.core.LatLonPoint;

import android.app.Activity;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements InfoWindowAdapter,
		LocationSource, AMapLocationListener {
	private AMap aMap;
	private MapView mapview;
	private Marker selectedMarker;
	private OnLocationChangedListener mListener;
	private LocationManagerProxy mAMapLocationManager;
	private LatLonPoint curPoint;

	private EditText latLng;
	private ImageView actionbarFlag;
	private ImageView actionbarNotification;
	private ImageView topFood;
	private ImageView topShopping;
	private ImageView topAttractions;
	private ImageView topStores;
	private ImageView topRestaurants;
	private ImageView topWashrooms;
	private ImageView naviMenu;
	private LinearLayout navibar;

	TempPopupWindow tPopWindow;

	private ArrayList<Marker> l1_attractions;
	private ArrayList<Marker> l1_shopping;
	private ArrayList<Marker> l1_food;
	private ArrayList<Marker> l2_attractions;
	private ArrayList<Marker> l2_stores;
	private ArrayList<Marker> l2_restaurants;
	private ArrayList<Marker> l2_washrooms;
	private ArrayList<Marker> l3_attractions;
	private ArrayList<Marker> l3_stores;
	private ArrayList<Marker> l3_restaurants;
	private ArrayList<Marker> l3_washrooms;

	private boolean topFoodShown = true;
	private boolean topShoppingShown = true;
	private boolean topAttractionsShown = true;
	private boolean topStoresShown = false;
	private boolean topRestaurantsShown = false;
	private boolean topWashroomsShown = false;
	private boolean fullnavibar = false;
	private boolean locating = false;
	private int mapLevel = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		this.getActionBar().hide();
		initComponent(savedInstanceState);
		initMarkers();
		refreshCamera();
	}

	private void initComponent(Bundle savedInstanceState) {
		mapview = (MapView) findViewById(R.id.mapView);
		latLng = (EditText) findViewById(R.id.et_search);
		actionbarFlag = (ImageView) findViewById(R.id.actionbar_flag);
		actionbarNotification = (ImageView) findViewById(R.id.actionbar_notification);
		topFood = (ImageView) findViewById(R.id.top_food);
		topShopping = (ImageView) findViewById(R.id.top_shopping);
		topAttractions = (ImageView) findViewById(R.id.top_attractions);
		topStores = (ImageView) findViewById(R.id.top_stores);
		topRestaurants = (ImageView) findViewById(R.id.top_restaurants);
		topWashrooms = (ImageView) findViewById(R.id.top_washrooms);
		naviMenu = (ImageView) findViewById(R.id.navi_menu);
		navibar = (LinearLayout) findViewById(R.id.ly_navi_second_line);

		mapview.onCreate(savedInstanceState);
		MapsInitializer.replaceURL(
				"http://a.tile.openstreetmap.org/%d/%d/%d.png", "OSM");
		aMap = mapview.getMap();
		aMap.getUiSettings().setZoomControlsEnabled(false);
		// Record Selected Marker
		aMap.setOnMarkerClickListener(new OnMarkerClickListener() {
			public boolean onMarkerClick(final Marker marker) {
				selectedMarker = marker;
				return false;
			}
		});
		aMap.setInfoWindowAdapter(this);
		aMap.setOnMapClickListener(new OnMapClickListener() {
			public void onMapClick(final LatLng arg0) {
				if (selectedMarker != null) {
					selectedMarker.hideInfoWindow();
					selectedMarker = null;
				}
				latLng.setText("lat:" + arg0.latitude + " Lng:"
						+ arg0.longitude);
			}
		});
		MyLocationStyle myLocationStyle = new MyLocationStyle();
		myLocationStyle.myLocationIcon(BitmapDescriptorFactory
				.fromResource(R.drawable.location_marker));
		myLocationStyle.strokeColor(Color.BLACK);
		myLocationStyle.radiusFillColor(Color.argb(100, 0, 0, 180));
		myLocationStyle.strokeWidth(1.0f);
		aMap.setMyLocationStyle(myLocationStyle);
		aMap.setLocationSource(this);
		aMap.getUiSettings().setMyLocationButtonEnabled(false);
		aMap.setMyLocationEnabled(true);
	}
	
	private void initMarkers() {
		final MarkersList ml = new MarkersList(MainActivity.this.getResources());
		l1_attractions = new ArrayList<Marker>();
		ArrayList<MarkersList.MarkerInfo> temp = ml.getL1Attractions();
		int j = temp.size();
		for (int i = 0; i < j; i++) {
			final MarkersList.MarkerInfo t = temp.get(i);
			l1_attractions.add(aMap.addMarker(t.markerOption));
			l1_attractions.get(i).setObject(t.description);
		}
		l1_shopping = new ArrayList<Marker>();
		temp = ml.getL1Shopping();
		j = temp.size();
		for (int i = 0; i < j; i++) {
			final MarkersList.MarkerInfo t = temp.get(i);
			l1_shopping.add(aMap.addMarker(t.markerOption));
			l1_shopping.get(i).setObject(t.description);
		}
		l1_food = new ArrayList<Marker>();
		temp = ml.getL1Food();
		j = temp.size();
		for (int i = 0; i < j; i++) {
			final MarkersList.MarkerInfo t = temp.get(i);
			l1_food.add(aMap.addMarker(t.markerOption));
			l1_food.get(i).setObject(t.description);
		}
		l2_attractions = new ArrayList<Marker>();
		temp = ml.getL2Attractions();
		j = temp.size();
		for (int i = 0; i < j; i++) {
			final MarkersList.MarkerInfo t = temp.get(i);
			l2_attractions.add(aMap.addMarker(t.markerOption));
			l2_attractions.get(i).setObject(t.description);
		}
		l2_stores = new ArrayList<Marker>();
		temp = ml.getL2Stores();
		j = temp.size();
		for (int i = 0; i < j; i++) {
			final MarkersList.MarkerInfo t = temp.get(i);
			l2_stores.add(aMap.addMarker(t.markerOption));
			l2_stores.get(i).setObject(t.description);
		}
		l2_restaurants = new ArrayList<Marker>();
		temp = ml.getL2Restaurants();
		j = temp.size();
		for (int i = 0; i < j; i++) {
			final MarkersList.MarkerInfo t = temp.get(i);
			l2_restaurants.add(aMap.addMarker(t.markerOption));
			l2_restaurants.get(i).setObject(t.description);
		}
		l2_washrooms = new ArrayList<Marker>();
		temp = ml.getL2Washrooms();
		j = temp.size();
		for (int i = 0; i < j; i++) {
			final MarkersList.MarkerInfo t = temp.get(i);
			l2_washrooms.add(aMap.addMarker(t.markerOption));
			l2_washrooms.get(i).setObject(t.description);
		}
		l3_attractions = new ArrayList<Marker>();
		temp = ml.getL3Attractions();
		j = temp.size();
		for (int i = 0; i < j; i++) {
			final MarkersList.MarkerInfo t = temp.get(i);
			l3_attractions.add(aMap.addMarker(t.markerOption));
			l3_attractions.get(i).setObject(t.description);
		}
		l3_stores = new ArrayList<Marker>();
		temp = ml.getL3Stores();
		j = temp.size();
		for (int i = 0; i < j; i++) {
			final MarkersList.MarkerInfo t = temp.get(i);
			l3_stores.add(aMap.addMarker(t.markerOption));
			l3_stores.get(i).setObject(t.description);
		}
		l3_restaurants = new ArrayList<Marker>();
		temp = ml.getL3Restaurants();
		j = temp.size();
		for (int i = 0; i < j; i++) {
			final MarkersList.MarkerInfo t = temp.get(i);
			l3_restaurants.add(aMap.addMarker(t.markerOption));
			l3_restaurants.get(i).setObject(t.description);
		}
		l3_washrooms = new ArrayList<Marker>();
		temp = ml.getL3Washrooms();
		j = temp.size();
		for (int i = 0; i < j; i++) {
			final MarkersList.MarkerInfo t = temp.get(i);
			l3_washrooms.add(aMap.addMarker(t.markerOption));
			l3_washrooms.get(i).setObject(t.description);
		}
		temp.clear();
		aMap.invalidate();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		return super.onOptionsItemSelected(item);
	}

	// TODO Action bar
	public void actionFlag(final View v) {
		if (mapLevel > 1) {
			refreshMap(false);
			if (mapLevel == 1) {
				((ImageView) v)
						.setImageResource(R.drawable.icon_actionbar_flag);
			}
		}
	}

	public void actionNotification(final View v) {
		tPopWindow = new TempPopupWindow(MainActivity.this, 1);
		tPopWindow
				.showPopupWindow(MainActivity.this.findViewById(R.id.mapView));
	}

	public void actionUserProfile(final View v) {
	}

	// TODO Filter bar
	public void showTourGuide(final View v) {
	}

	public void showFood(final View v) {
		topFoodShown = !topFoodShown;
		((ImageView) v)
				.setImageResource(topFoodShown ? R.drawable.icon_top_food_active
						: R.drawable.icon_top_food_inactive);
		int temp = l1_food.size();
		for (int i = 0; i < temp; i++) {
			l1_food.get(i).setVisible(topFoodShown);
		}
		aMap.invalidate();
	}

	public void showShopping(final View v) {
		topShoppingShown = !topShoppingShown;
		((ImageView) v)
				.setImageResource(topShoppingShown ? R.drawable.icon_top_shopping_active
						: R.drawable.icon_top_shopping_inactive);
		int temp = l1_shopping.size();
		for (int i = 0; i < temp; i++) {
			l1_shopping.get(i).setVisible(topShoppingShown);
		}
		aMap.invalidate();
	}

	public void showAttractions(final View v) {
		topAttractionsShown = !topAttractionsShown;
		((ImageView) v)
				.setImageResource(topAttractionsShown ? R.drawable.icon_top_attractions_active
						: R.drawable.icon_top_attractions_inactive);
		int temp;
		switch (mapLevel) {
		case 2:
			temp = l2_attractions.size();
			for (int i = 0; i < temp; i++) {
				l2_attractions.get(i).setVisible(topAttractionsShown);
			}
			break;
		case 3:
			temp = l3_attractions.size();
			for (int i = 0; i < temp; i++) {
				l3_attractions.get(i).setVisible(topAttractionsShown);
			}
			break;
		}
		aMap.invalidate();
	}

	public void showRestaurants(final View v) {
		topRestaurantsShown = !topRestaurantsShown;
		((ImageView) v)
				.setImageResource(topRestaurantsShown ? R.drawable.icon_top_restaurants_active
						: R.drawable.icon_top_restaurants_inactive);
		int temp;
		switch (mapLevel) {
		case 2:
			temp = l2_restaurants.size();
			for (int i = 0; i < temp; i++) {
				l2_restaurants.get(i).setVisible(topRestaurantsShown);
			}
			break;
		case 3:
			temp = l3_restaurants.size();
			for (int i = 0; i < temp; i++) {
				l3_restaurants.get(i).setVisible(topRestaurantsShown);
			}
			break;
		}
		aMap.invalidate();
	}

	public void showStores(final View v) {
		topStoresShown = !topStoresShown;
		((ImageView) v)
				.setImageResource(topStoresShown ? R.drawable.icon_top_stores_active
						: R.drawable.icon_top_stores_inactive);
		int temp;
		switch (mapLevel) {
		case 2:
			temp = l2_stores.size();
			for (int i = 0; i < temp; i++) {
				l2_stores.get(i).setVisible(topStoresShown);
			}
			break;
		case 3:
			temp = l3_stores.size();
			for (int i = 0; i < temp; i++) {
				l3_stores.get(i).setVisible(topStoresShown);
			}
			break;
		}
		aMap.invalidate();
	}

	public void showWashrooms(final View v) {
		topWashroomsShown = !topWashroomsShown;
		((ImageView) v)
				.setImageResource(topWashroomsShown ? R.drawable.icon_top_washrooms_active
						: R.drawable.icon_top_washrooms_inactive);
		int temp;
		switch (mapLevel) {
		case 2:
			temp = l2_washrooms.size();
			for (int i = 0; i < temp; i++) {
				l2_washrooms.get(i).setVisible(topWashroomsShown);
			}
			break;
		case 3:
			temp = l3_washrooms.size();
			for (int i = 0; i < temp; i++) {
				l3_washrooms.get(i).setVisible(topWashroomsShown);
			}
			break;
		}
		aMap.invalidate();
	}

	// TODO Navi bar
	public void actionNaviInsep(final View v) {
	}

	public void actionNaviShopping(final View v) {
		tPopWindow = new TempPopupWindow(MainActivity.this, 2);
		tPopWindow
				.showPopupWindow(MainActivity.this.findViewById(R.id.mapView));
	}

	public void actionNaviMenu(final View v) {
		fullnavibar = !fullnavibar;
		navibar.setVisibility(fullnavibar ? View.VISIBLE : View.GONE);
		naviMenu.setImageResource(fullnavibar ? R.drawable.icon_navi_menu_close
				: R.drawable.icon_navi_menu);
	}

	public void actionNaviTaxi(final View v) {
		tPopWindow = new TempPopupWindow(MainActivity.this, 3);
		tPopWindow
				.showPopupWindow(MainActivity.this.findViewById(R.id.mapView));
	}

	public void actionNaviSetting(final View v) {
	}

	public void actionNaviTeam(final View v) {
		tPopWindow = new TempPopupWindow(MainActivity.this, 4);
		tPopWindow
				.showPopupWindow(MainActivity.this.findViewById(R.id.mapView));
	}

	public void actionNaviShareLoc(final View v) {
		tPopWindow = new TempPopupWindow(MainActivity.this, 5);
		tPopWindow
				.showPopupWindow(MainActivity.this.findViewById(R.id.mapView));
	}

	public void actionNaviRoute(final View v) {

	}

	public void actionNaviSOS(final View v) {
		tPopWindow = new TempPopupWindow(MainActivity.this, 6);
		tPopWindow
				.showPopupWindow(MainActivity.this.findViewById(R.id.mapView));
	}

	public void actionNaviData(final View v) {
		tPopWindow = new TempPopupWindow(MainActivity.this, 7);
		tPopWindow
				.showPopupWindow(MainActivity.this.findViewById(R.id.mapView));
	}

	// TODO MarkerFunc
	public void markerFuncNavigation(final View v) {

	}

	public void markerFunc360cam(final View v) {
	}

	public void markerFuncCallTaxi(final View v) {
	}

	public void markerFuncComments(final View v) {
	}

	public void markerFuncInterpretation(final View v) {
	}

	public void markerFunDescription(final View v) {
	}

	public void markerFuncActivity(final View v) {
	}

	public void markerFuncTicket(final View v) {
	}

	public void markerFuncOnlineQueue(final View v) {
	}

	public void markerFuncZoomIn(final View v) {
		refreshMap(true);

	}

	private void refreshMap(boolean zoomIn) {
		if (selectedMarker != null) {
			selectedMarker.hideInfoWindow();
			selectedMarker = null;
		}
		if (zoomIn) {
			mapLevel++;
		} else {
			mapLevel--;
		}
		refreshFilters();
		refreshCamera();
		refreshMarkers();
		aMap.invalidate();
	}

	private void refreshFilters() {
		switch (mapLevel) {
		case 1:
			topFood.setVisibility(View.VISIBLE);
			topShopping.setVisibility(View.VISIBLE);
			topAttractions.setVisibility(View.GONE);
			topStores.setVisibility(View.GONE);
			topRestaurants.setVisibility(View.GONE);
			topWashrooms.setVisibility(View.GONE);
			break;
		case 2:
			topFood.setVisibility(View.GONE);
			topShopping.setVisibility(View.GONE);
			topAttractions.setVisibility(View.VISIBLE);
			topStores.setVisibility(View.VISIBLE);
			topRestaurants.setVisibility(View.VISIBLE);
			topWashrooms.setVisibility(View.VISIBLE);
			break;
		}
	}

	private void refreshCamera() {
		switch (mapLevel) {
		case 1:
			aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
					1.352222, 103.826294), 12));
			break;
		case 2:
			aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
					1.253547, 103.824401), 15));
			break;
		case 3:
			aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
					1.254307, 103.823112), 17));
			break;
		}
	}

	private void refreshMarkers() {
		int temp;
		switch (mapLevel) {
		case 1:
			temp = l1_attractions.size();
			for (int i = 0; i < temp; i++) {
				l1_attractions.get(i).setVisible(true);
			}
			temp = l1_shopping.size();
			for (int i = 0; i < temp; i++) {
				l1_shopping.get(i).setVisible(topFoodShown);
			}
			temp = l1_food.size();
			for (int i = 0; i < temp; i++) {
				l1_food.get(i).setVisible(topShoppingShown);
			}
			temp = l2_attractions.size();
			for (int i = 0; i < temp; i++) {
				l2_attractions.get(i).setVisible(false);
			}
			temp = l2_stores.size();
			for (int i = 0; i < temp; i++) {
				l2_stores.get(i).setVisible(false);
			}
			temp = l2_restaurants.size();
			for (int i = 0; i < temp; i++) {
				l2_restaurants.get(i).setVisible(false);
			}
			temp = l2_washrooms.size();
			for (int i = 0; i < temp; i++) {
				l2_washrooms.get(i).setVisible(false);
			}
			break;
		case 2:
			temp = l1_attractions.size();
			for (int i = 0; i < temp; i++) {
				l1_attractions.get(i).setVisible(false);
			}
			temp = l1_shopping.size();
			for (int i = 0; i < temp; i++) {
				l1_shopping.get(i).setVisible(false);
			}
			temp = l1_food.size();
			for (int i = 0; i < temp; i++) {
				l1_food.get(i).setVisible(false);
			}
			temp = l2_attractions.size();
			for (int i = 0; i < temp; i++) {
				l2_attractions.get(i).setVisible(topAttractionsShown);
			}
			temp = l2_stores.size();
			for (int i = 0; i < temp; i++) {
				l2_stores.get(i).setVisible(topStoresShown);
			}
			temp = l2_restaurants.size();
			for (int i = 0; i < temp; i++) {
				l2_restaurants.get(i).setVisible(topRestaurantsShown);
			}
			temp = l2_washrooms.size();
			for (int i = 0; i < temp; i++) {
				l2_washrooms.get(i).setVisible(topWashroomsShown);
			}
			temp = l3_attractions.size();
			for (int i = 0; i < temp; i++) {
				l3_attractions.get(i).setVisible(false);
			}
			temp = l3_stores.size();
			for (int i = 0; i < temp; i++) {
				l3_stores.get(i).setVisible(false);
			}
			temp = l3_restaurants.size();
			for (int i = 0; i < temp; i++) {
				l3_restaurants.get(i).setVisible(false);
			}
			temp = l3_washrooms.size();
			for (int i = 0; i < temp; i++) {
				l3_washrooms.get(i).setVisible(false);
			}
			break;
		case 3:
			temp = l2_attractions.size();
			for (int i = 0; i < temp; i++) {
				l2_attractions.get(i).setVisible(false);
			}
			temp = l2_stores.size();
			for (int i = 0; i < temp; i++) {
				l2_stores.get(i).setVisible(false);
			}
			temp = l2_restaurants.size();
			for (int i = 0; i < temp; i++) {
				l2_restaurants.get(i).setVisible(false);
			}
			temp = l2_washrooms.size();
			for (int i = 0; i < temp; i++) {
				l2_washrooms.get(i).setVisible(false);
			}
			temp = l3_attractions.size();
			for (int i = 0; i < temp; i++) {
				l3_attractions.get(i).setVisible(topAttractionsShown);
			}
			temp = l3_stores.size();
			for (int i = 0; i < temp; i++) {
				l3_stores.get(i).setVisible(topStoresShown);
			}
			temp = l3_restaurants.size();
			for (int i = 0; i < temp; i++) {
				l3_restaurants.get(i).setVisible(topRestaurantsShown);
			}
			temp = l3_washrooms.size();
			for (int i = 0; i < temp; i++) {
				l3_washrooms.get(i).setVisible(topWashroomsShown);
			}
			break;
		}
	};

	// TODO Custom info window
	@Override
	public View getInfoContents(Marker marker) {
		return null;
	}

	@Override
	public View getInfoWindow(Marker marker) {
		View infoWindow = null;
		TextView vt = null;
		switch (Integer.valueOf(marker.getObject().toString().substring(0, 1))) {
		case 0:
			return getLayoutInflater().inflate(R.layout._washroom, null);
		case 1:
			infoWindow = getLayoutInflater().inflate(
					R.layout._store_and_restaurant, null);
			break;
		case 2:
			infoWindow = getLayoutInflater().inflate(R.layout._l1_infowindow,
					null);
			break;
		case 3:
			infoWindow = getLayoutInflater().inflate(R.layout._l2_infowindow,
					null);
			break;
		case 4:
			infoWindow = getLayoutInflater().inflate(R.layout._l2_attraction,
					null);
			break;
		case 6:
			infoWindow = getLayoutInflater().inflate(R.layout._l3_attraction,
					null);
			break;
		}
		vt = (TextView) infoWindow.findViewById(R.id.tv_marker_title);
		vt.setText(marker.getTitle());
		vt.setBackgroundResource(MainActivity.this.getResources()
				.getIdentifier(
						"photo_" + marker.getObject().toString().substring(1),
						"drawable", "com.insep.navidemo"));
		return infoWindow;
	}

	// TODO Locate & Route
	public void startLocation(final View v) {
		locating = !locating;
		if (locating) {
			mAMapLocationManager.requestLocationData(
					LocationProviderProxy.AMapNetwork, 5000, 10, this);
		} else {
			mAMapLocationManager.removeUpdates(MainActivity.this);
		}

	}

	// TODO implement
	@Override
	public void activate(OnLocationChangedListener listener) {
		mListener = listener;
		if (mAMapLocationManager == null) {
			mAMapLocationManager = LocationManagerProxy.getInstance(this);
		}
	}

	@Override
	public void deactivate() {
		mListener = null;
		if (mAMapLocationManager != null) {
			mAMapLocationManager.removeUpdates(this);
			mAMapLocationManager.destroy();
		}
		mAMapLocationManager = null;
	}

	@Override
	public void onLocationChanged(AMapLocation aLocation) {
		if (mListener != null && aLocation != null) {
			mListener.onLocationChanged(aLocation);
			curPoint = new LatLonPoint(aLocation.getLatitude(),
					aLocation.getLongitude());
		}
	}

	@Override
	public void onLocationChanged(Location location) {

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {

	}

	@Override
	public void onProviderEnabled(String provider) {

	}

	@Override
	public void onProviderDisabled(String provider) {

	}

	// TODO runtime
	@Override
	protected void onResume() {
		super.onResume();
		mapview.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		mapview.onPause();
	}

	@Override
	protected void onSaveInstanceState(final Bundle outState) {
		super.onSaveInstanceState(outState);
		mapview.onSaveInstanceState(outState);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mapview.onDestroy();
		deactivate();
		mapview = null;
	}
}
