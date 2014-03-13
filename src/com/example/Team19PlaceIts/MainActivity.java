package com.example.Team19PlaceIts;

import java.util.Iterator;
import java.util.List;

import com.example.Team19PlaceIts.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.CancelableCallback;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import com.example.Team19PlaceIts.ListMediator;

import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;

public class MainActivity extends Activity implements OnMapClickListener, 
	CancelableCallback, OnMarkerClickListener, OnInfoWindowClickListener,
	LocationListener, ConnectionCallbacks, OnConnectionFailedListener{

	public  GoogleMap mMap;
	private ListMediator myListMediator = new ListMediator();
	private WriteToFile myWrite = new WriteToFile();
	private ReadFromFile myRead = new ReadFromFile();

	private List<Marker> mMarkers = myListMediator.getList();
	private List<Marker> completedMarkers = myListMediator.getCompletedList();
	private Iterator marker = myListMediator.getMarkerIterator();
	final String activeList = "activeList";  //file name for active list 
	private LocationClient mLocationClient;
	private MoveToCurrentLocation moveMap = new MoveToCurrentLocation();
	private int ACTIVE_LIST_REQUEST_CODE = 1;
	private int COMPLETED_LIST_REQUEST_CODE = 2;
	boolean follow = false;
	private TextView mLatLong;
	
	//- - - - -  - - - - -  - -- ProximityAlert - - - - - - - - - - - - - - - 
	private ProximityAlert mProximityAlert = new ProximityAlert();
	private ProximityReceiver mProximityReceiver = new ProximityReceiver();
	
	private Intent mTry = null;
	//- - - - -  - - - - -  - -- ProximityAlert - - - - - - - - - - - - - - - 
	
	// Define an object that holds accuracy and frequency parameters
		LocationRequest mLocationRequest;
		private static final long UPDATE_INTERVAL = 1000;
		private static final long FASTEST_INTERVAL = 1000;
		boolean mUpdatesRequested;
		private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
				
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setUpMapIfNeeded();
		mLatLong = (TextView) findViewById(R.id.latLong);
		
// not working ? why		
		IntentFilter filter = new IntentFilter();
		filter.addAction("com.example.Team19PlaceIts.ProximityAlert");
		//this.registerReceiver(mProximityReceiver, filter);
		getApplicationContext().registerReceiver(mProximityReceiver, filter);
		
		
		mMap.setMyLocationEnabled(true);
		mMap.setOnMapClickListener(this);
		mMap.setOnMarkerClickListener(this);
		mMap.setOnInfoWindowClickListener(this);
		
		
		
		
		
		
		
		
		

		
		
		
		
		
		
	
		
		
		
		// Create the LocationRequest object
				mLocationRequest = LocationRequest.create();
				// Use high accuracy
				mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
				// Set the update interval to 5 seconds
				mLocationRequest.setInterval(UPDATE_INTERVAL);
				// Set the fastest update interval to 1 second
				mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
				// Start with updates turned on
				mUpdatesRequested = true;
				/*
				 * Create a new location client, using the enclosing class to handle
				 * callbacks.
				 */
				mLocationClient = new LocationClient(this, this, this);

		// fix the orientation to portrait
		// setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		Button btnReTrack = (Button) findViewById(R.id.retrack);
		btnReTrack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				marker = myListMediator.getMarkerIterator();
				if (marker.hasNext()) {
					Marker current = (Marker) marker.next();
					mMap.animateCamera(CameraUpdateFactory.newLatLng(current
							.getPosition()), 2000, MainActivity.this);
					current.showInfoWindow();
				}
			}
		});
/*
		Button btnUpdate = (Button) findViewById(R.id.sendLocationBtn);
		btnUpdate.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				EditText editText = (EditText) findViewById(R.id.sendLocation);
				String geoData = editText.getText().toString();
				String[] coordinate = geoData.split(",");
				double latitude = Double.valueOf(coordinate[0]).doubleValue();
				double longitude = Double.valueOf(coordinate[1]).doubleValue();
				mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
						new LatLng(latitude, longitude), 12), 2000, null);
			}
		});
*/
		final Button followButton = (Button) findViewById(R.id.follow);
		followButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(follow){
					follow = false;
					Toast.makeText(MainActivity.this, "not following",
							Toast.LENGTH_SHORT).show();
					followButton.setText("follow");
				}
				else{
					follow = true;
					Toast.makeText(MainActivity.this, "following", 
							Toast.LENGTH_SHORT).show();
					followButton.setText("not follow");
				}
			}
		});
		// move map to current location
		moveMap.moveToCurrentLocation(this, mMap, 14);
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return true;
	}

	/**
	 * Event Handling for Individual menu item selected Identify single menu
	 * item by it's id
	 * */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.action_settings:
			Toast.makeText(MainActivity.this, "do nothing right now", 
					Toast.LENGTH_SHORT).show();
			
			Intent loginIntent = new Intent(getApplicationContext(), GooglePlusLoginActivity.class);
			loginIntent.putExtra("your_condition", 1);
			loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	 	    startActivity(loginIntent);
		   // finish();
			
			
			
			return true;

		case R.id.Active_list:
			startActivityForResult(new Intent(this, 
					ActiveListLoader.class), ACTIVE_LIST_REQUEST_CODE);
			return true;

		case R.id.Completed_list:
			startActivityForResult(new Intent(this, 
					CompletedListLoader.class), COMPLETED_LIST_REQUEST_CODE);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		// back from ActiveListLoader and delete the markers chosen in ActiveListLoader
		if (resultCode == RESULT_OK && requestCode == ACTIVE_LIST_REQUEST_CODE) {
			for (int Num = 0; data.hasExtra("Key" + Num); Num = Num + 1) {
				Toast.makeText(this, "key :" + Num + " " + data.getExtras().getInt("Key" + Num),
						Toast.LENGTH_SHORT).show();
				mProximityAlert.removeProximityAlert(this, data.getExtras().getInt("Key" + Num));
			}
		}

		// back from CompletedListLoader and repost the markers chosen in CompletedListLoader
		if (resultCode == RESULT_OK && requestCode == COMPLETED_LIST_REQUEST_CODE) {
			for (int Num = 0; data.hasExtra("Key" + Num); Num = Num + 1) {
				Toast.makeText(this, "key :" + Num + " " + data.getExtras().getInt("Key" + Num),
						Toast.LENGTH_SHORT).show();
				int mPosition = data.getExtras().getInt("Key" + Num);
				mProximityAlert.AddProximityAlert(completedMarkers.get(mPosition), this);
				myListMediator.removeCompletedMarker(mPosition);
			}
		}		
		super.onActivityResult(requestCode, resultCode, data);	
	} 

	private void setUpMapIfNeeded() {
		// Do a null check to confirm that we have not already instantiated the
		// map.
		if (mMap == null) {
			mMap = ((MapFragment) getFragmentManager().findFragmentById(
					R.id.map)).getMap();
			// Check if we were successful in obtaining the map.
			if (mMap != null) {
				// The Map is verified. It is now safe to manipulate the map.
			}
		}
	}

	@Override
	public void onMapClick(LatLng position) {

		final LatLng pos = position;

		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		LayoutInflater inflater = this.getLayoutInflater();
		alert.setTitle("New Reminder");
		alert.setMessage("Please edit your reminder:");
		// Set an EditText view to get user input
		// final EditText input = new EditText(this);
		View myInflatorView = inflater.inflate(R.layout.dialog_signin, null);
		alert.setView(myInflatorView);
		// alert.setView(input);
		final EditText titleInput = (EditText) myInflatorView
				.findViewById(R.id.title);
		final EditText contentInput = (EditText) myInflatorView
				.findViewById(R.id.content);
		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				String titleValue = titleInput.getText().toString();
				String contentValue = contentInput.getText().toString();
				Marker added = mMap.addMarker(new MarkerOptions().position(pos)
						.title(titleValue).snippet(contentValue));

				mMarkers.add(added);		
				 
		//- - - - -  - - - - -  - -- ProximityAlert - - - - - - - - - - - - - - - 
				mProximityAlert.AddProximityAlert(added, getApplicationContext());
		//- - - - -  - - - - -  - -- ProximityAlert - - - - - - - - - - - - - - - 
				
				
				
				
				System.out.println("fock ID: " + added.getId());
			}
		});
		// Repost Button
		alert.setNeutralButton("Schedule",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						// add cal. functionality
						String titleValue = titleInput.getText().toString();
						String contentValue = contentInput.getText().toString();
						Toast.makeText(MainActivity.this, "Tag added!",
								Toast.LENGTH_SHORT).show();
						Marker added = mMap.addMarker(new MarkerOptions()
								.position(pos).title(titleValue)
								.snippet(contentValue));

						mMarkers.add(added);
					}

				});

		alert.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						Toast.makeText(MainActivity.this, "Nothing added!",
								Toast.LENGTH_SHORT).show();
					}
				});

		alert.show();
	}

	public boolean onMarkerClick(final Marker marker) {
		// set camera view
		// display options
		marker.showInfoWindow();

		return false; // change to true if needed

	}

	// info window of the marker
	public void onInfoWindowClick(final Marker marker) {

		mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(),
				15));

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setPositiveButton(R.string.schedule_button,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {

						Toast.makeText(MainActivity.this, "Post-It Scheduled!",
								Toast.LENGTH_SHORT).show();
					}
				}).setNegativeButton(R.string.discard_button,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						
						// delete the ProximityAlert associated with the marker
						int requestCode = (int) (100000*marker.getPosition().latitude
								+ 100000*marker.getPosition().longitude);
						mProximityAlert.removeProximityAlert(getApplicationContext(), requestCode);
						
						// delete the marker 
						deleteMarker(marker);
						Toast.makeText(MainActivity.this, "Post-It Removed!",
								Toast.LENGTH_SHORT).show();
					}
				});
		/*
		 * .setNeutralButton(R.string.close_button, new
		 * DialogInterface.OnClickListener() { public void
		 * onClick(DialogInterface dialog, int id) { // User cancelled the
		 * dialog Toast.makeText(MainActivity.this, "No Change!",
		 * Toast.LENGTH_SHORT).show(); } });
		 */

		builder.show();
		// calls the second intent
		// Intent i = new Intent(this, OnClickActivity.class);
		// startActivity(i);
		// finish(); // Call once you redirect to another activity

	}

	@Override
	public void onCancel() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onFinish() {
		// TODO Auto-generated method stub
		if (marker.hasNext()) {
			Marker current = (Marker) marker.next();
			mMap.animateCamera(
					CameraUpdateFactory.newLatLng(current.getPosition()), 2000,
					this);
			current.showInfoWindow();
// this is for testing repost, delete it when finish testing!!
myListMediator.addToCompletedList(current);
		}
	}

	@Override
	protected void onPause() {
		super.onPause();

		// write file to save marker info
		myWrite.writeActive(this);
		myWrite.writeCompleted(this);
		
		// clean the map
		mMap.clear();
//		unregisterReceiver(mProximityReceiver);
	}

	@Override
	protected void onResume() {
		super.onResume();
		// clear the list every time switch between activities
		mMarkers.clear();

		// read the file to retrieve markers
		myRead.readActive(this, mMap);
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		mLocationClient.connect();
	}
	
	//this function is called even ever a intent start this (MainActivity's) activity 
	protected void onNewIntent(Intent intent) {
		// move map to current location
		moveMap.moveToCurrentLocation(this, mMap, 18);
	}

	// internal function to delete marker  !!! NEED CHANGE
	private void deleteMarker(final Marker targetMarker) {
		int index = 0;

		for (Marker marker : mMarkers) {
			if (marker.equals(targetMarker)) {
				marker.setVisible(false);
				// DONT forget to save to discard list
				mMarkers.remove(index);
				marker.remove();
				break;
			} else
				index++;
		}
	}

	public GoogleMap getMap() {
		return mMap;
	}

	public List<Marker> getList() {
		return mMarkers;
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		mLatLong.setText("Lat: "+location.getLatitude()+" \n" + "Long: "+location.getLongitude());
		if(follow){
		 mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new 
				 LatLng(location.getLatitude(),location.getLongitude()), 14)); 
		}
	}
	
	@Override
	public void onConnected(Bundle dataBundle) {
		// Display the connection status
		Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show();
		// If already requested, start periodic updates
		if (mUpdatesRequested) {
			mLocationClient.requestLocationUpdates(mLocationRequest, this);
		}
	}

	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub
		Toast.makeText(this, "Disconnected. Please re-connect.",
				Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
		// TODO Auto-generated method stub
		if (connectionResult.hasResolution()) {
			try {
				// Start an Activity that tries to resolve the error
				connectionResult.startResolutionForResult(this,
						CONNECTION_FAILURE_RESOLUTION_REQUEST);
				/*
				 * Thrown if Google Play services canceled the original
				 * PendingIntent
				 */
			} catch (IntentSender.SendIntentException e) {
				// Log the error
				e.printStackTrace();
			}
		} else {
			/*
			 * If no resolution is available, display a dialog to the user with
			 * the error.
			 */
			Toast.makeText(this, "FAILURE!", Toast.LENGTH_LONG).show();
		}
	}
	
}
