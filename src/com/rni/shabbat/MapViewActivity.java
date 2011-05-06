package com.rni.shabbat;
// This file is MapViewDemoActivity.java
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.OverlayItem;
import com.rni.shabbat.R;
public class MapViewActivity extends MapActivity
{
	private MapView mapView;
	private MapController mapController = null;
	private MyLocationOverlay whereAmI = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mapview);
		mapView = (MapView)findViewById(R.id.mapview);
		
		Drawable marker=getResources().getDrawable(R.drawable.mapmarker);
		marker.setBounds(0, 0, marker.getIntrinsicWidth(),
				marker.getIntrinsicHeight());
		
		mapView.setBuiltInZoomControls(true);
		mapController = mapView.getController();
		mapController.setZoom(15);
		whereAmI = new MyLocationOverlay(this, mapView);
		mapView.getOverlays().add(whereAmI);
		mapView.postInvalidate();
		
		InterestingLocations funPlaces = new InterestingLocations(marker);
		mapView.getOverlays().add(funPlaces);
		GeoPoint pt = funPlaces.getCenter(); // get the first-ranked point
		//mapView.getController().setCenter(pt);
		//mapView.getController().setZoom(10);
		
	}
	class InterestingLocations extends ItemizedOverlay {
		private List<OverlayItem> locations = new ArrayList<OverlayItem>();
		private Drawable marker;
		public InterestingLocations(Drawable marker)
		{
		super(marker);
		this.marker=marker;
		// create locations of interest
		GeoPoint israel = new GeoPoint((int)(31.30*1000000),(int)(34.45*1000000));
		
		GeoPoint disneyMagicKingdom = new
		GeoPoint((int)(28.418971*1000000),(int)(-81.581436*1000000));
		GeoPoint disneySevenLagoon = new
		GeoPoint((int)(28.410067*1000000),(int)(-81.583699*1000000));
		locations.add(new OverlayItem(israel, "Israel","Israel"));
		locations.add(new OverlayItem(disneyMagicKingdom ,
		"Magic Kingdom", "Magic Kingdom"));
		locations.add(new OverlayItem(disneySevenLagoon ,
		"Seven Lagoon", "Seven Lagoon"));
		populate();
		}
		@Override
		public void draw(Canvas canvas, MapView mapView, boolean shadow) {
		super.draw(canvas, mapView, shadow);
		boundCenterBottom(marker);
		}
		@Override
		protected OverlayItem createItem(int i) {
		return locations.get(i);
		}
		@Override
		public int size() {
		return locations.size();
		}
		}
	/*
	@Override
	public void onResume()
	{
		super.onResume();
		whereAmI.enableMyLocation();
		whereAmI.runOnFirstFix(new Runnable() {
			public void run() {
				mapController.setCenter(whereAmI.getMyLocation());
			}
		});
	}
	@Override
	public void onPause()
	{
		super.onPause();
		whereAmI.disableMyLocation();
	}*/
	
	public void myClickHandler(View target) {
		switch(target.getId()) {
		case R.id.zoomin:
			mapView.getController().zoomIn();
			break;
		case R.id.zoomout:
			mapView.getController().zoomOut();
			break;
		case R.id.sat:
			mapView.setSatellite(true);
			break;
		case R.id.street:
			mapView.setStreetView(true);
			break;
		case R.id.traffic:
			mapView.setTraffic(true);
			break;
		case R.id.normal:
			mapView.setSatellite(false);
			mapView.setStreetView(false);
			mapView.setTraffic(false);
			break;
		}
	}
	@Override
	protected boolean isLocationDisplayed() {
		return false;
	}
	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
}