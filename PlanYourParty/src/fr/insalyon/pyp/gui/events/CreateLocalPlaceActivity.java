package fr.insalyon.pyp.gui.events;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import fr.insalyon.pyp.R;
import fr.insalyon.pyp.gui.common.BaseActivity;
import fr.insalyon.pyp.gui.common.popup.Popups;
import fr.insalyon.pyp.network.ServerConnection;
import fr.insalyon.pyp.tools.AppTools;
import fr.insalyon.pyp.tools.Constants;
import fr.insalyon.pyp.tools.PYPContext;

public class CreateLocalPlaceActivity extends BaseActivity {
			private LinearLayout abstractView;
			private LinearLayout mainView;
			
			private TextView LocalPlaceName;
			private TextView AddressLocalPlace;
			private TextView DescriptionLocalPlace;
			private Spinner TypeLocalPlace;
			
			private Button NextStepBtn;
			
			@Override
			public void onCreate(Bundle savedInstanceState) {
				super.onCreate(savedInstanceState, Constants.EVENTS_CONST);
				AppTools.info("on create CreateEvent");
				initGraphicalInterface();
			}

			private void initGraphicalInterface() {
				// set layouts
				LayoutInflater mInflater = LayoutInflater.from(this);
				abstractView = (LinearLayout) findViewById(R.id.abstractLinearLayout);
				mainView = (LinearLayout) mInflater.inflate(R.layout.create_local_place_activity,
						null);
				abstractView.addView(mainView);
				hideHeader(false);
				
				
				LocalPlaceName = (TextView) findViewById(R.id.LocalPlaceName);
				AddressLocalPlace = (TextView) findViewById(R.id.AddressLocalPlace);
				// Get the address from the current location
				// TODO: get the real data from the phone
				double latitude = 45.78;
				double longitude = 4.8;
				
				new GetCurrentAddressTask().execute();
				
//				Geocoder geocoder;
//				List<Address> addresses;
//				geocoder = new Geocoder(this, Locale.getDefault());
//				try {
//					addresses = geocoder.getFromLocation(latitude, longitude, 1);
//					String country = addresses.get(0).getAddressLine(2);
//					String city = addresses.get(0).getAddressLine(1);
//					String address = addresses.get(0).getAddressLine(0);
//					
//					AddressLocalPlace.setText(address);
//				} catch (IOException e) {
//					e.printStackTrace();
//				}

				DescriptionLocalPlace = (TextView) findViewById(R.id.DescriptionLocalPlace);
				TypeLocalPlace = (Spinner) findViewById(R.id.TypeLocalPlace);
				// Create an ArrayAdapter using the string array and a default spinner
				// layout
				ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
						this, R.array.place_type_array, android.R.layout.simple_spinner_item);
				// Specify the layout to use when the list of choices appears
				adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				// Apply the adapter to the spinner
				TypeLocalPlace.setAdapter(adapter);
				
				NextStepBtn = (Button) findViewById(R.id.NextStepBtn);
				
				NextStepBtn.setOnClickListener( new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO: Check if fields are ok
						new CreateLocalPlaceTask().execute();
					}
				});
				
			}


			@Override
			public void onResume() {
				super.onResume();
				//check if logged in
//				checkLoggedIn();
			}
			
			public void networkError(String error) {
				Popups.showPopup("broken");
			}

			private class CreateLocalPlaceTask extends AsyncTask<Void, Void, Void> {

				ProgressDialog mProgressDialog;
				JSONObject res;

				@Override
				protected void onPostExecute(Void result) {
					mProgressDialog.dismiss();
					if (res != null) {
						try {
							if (res.has("error")) {
								// Error
								String error;
								error = res.getString("error");
								CreateLocalPlaceActivity.this.networkError(error);
							}

							else {
								// OK
								String id = res.getString("id");
								// TODO: stocker id
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}

				@Override
				protected void onPreExecute() {
					mProgressDialog = ProgressDialog.show(CreateLocalPlaceActivity.this,
							getString(R.string.app_name), getString(R.string.loading));
				}

				@Override
				protected Void doInBackground(Void... params) {
					// Send request to server for login

					ServerConnection srvCon = ServerConnection.GetServerConnection();
					List<NameValuePair> parameters = new ArrayList<NameValuePair>();
					
					parameters.add(new BasicNameValuePair("name", LocalPlaceName
							.getText().toString()));
					parameters.add(new BasicNameValuePair("type", TypeLocalPlace
							.getSelectedItem().toString()));
					parameters.add(new BasicNameValuePair("description", DescriptionLocalPlace
							.getText().toString()));
					parameters.add(new BasicNameValuePair("auth_token", PYPContext.getContext().getSharedPreferences(AppTools.PREFS_NAME, 0).getString("auth_token", "")));
					// TODO: if ID exist
					// Longitude
					// Latitude
					String latitude = "45.78";
					String longitude = "4.87";
					parameters.add(new BasicNameValuePair("latitude", latitude));
					parameters.add(new BasicNameValuePair("longitude", longitude));
					
					try {
						res = srvCon.connect(ServerConnection.ADD_LOCAL_PLACE, parameters);
					} catch (Exception e) {
						e.printStackTrace();
					}
					return null;
				}
			}

			
			private class GetCurrentAddressTask extends AsyncTask<Void, Void, Void> {

				ProgressDialog mProgressDialog;
				JSONObject res;

				@Override
				protected void onPostExecute(Void result) {
					mProgressDialog.dismiss();
					if (res != null) {
						try {
							if (res.has("error")) {
								// Error
								String error;
								error = res.getString("error");
								CreateLocalPlaceActivity.this.networkError(error);
							}

							else {
								// OK
								String address = res.getString("address");
								AddressLocalPlace.setText(address);
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}

				@Override
				protected void onPreExecute() {
					mProgressDialog = ProgressDialog.show(CreateLocalPlaceActivity.this,
							getString(R.string.app_name), getString(R.string.loading));
				}

				@Override
				protected Void doInBackground(Void... params) {
					// Send request to server for login
					ServerConnection srvCon = ServerConnection.GetServerConnection();
					List<NameValuePair> parameters = new ArrayList<NameValuePair>();
					
					// TODO: Longitude
					// Latitude
					String latitude = "45.78";
					String longitude = "4.87";
					parameters.add(new BasicNameValuePair("latitude", latitude));
					parameters.add(new BasicNameValuePair("longitude", longitude));
					
					parameters.add(new BasicNameValuePair("auth_token", PYPContext.getContext().getSharedPreferences(AppTools.PREFS_NAME, 0).getString("auth_token", "")));

					try {
						res = srvCon.connect(ServerConnection.GET_CURRENT_ADDRESS, parameters);
					} catch (Exception e) {
						e.printStackTrace();
					}
					return null;
				}
			}
}
