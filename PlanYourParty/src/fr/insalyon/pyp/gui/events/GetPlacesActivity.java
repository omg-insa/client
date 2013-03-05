package fr.insalyon.pyp.gui.events;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import fr.insalyon.pyp.R;
import fr.insalyon.pyp.gui.common.BaseActivity;
import fr.insalyon.pyp.gui.common.IntentHelper;
import fr.insalyon.pyp.gui.common.popup.Popups;
import fr.insalyon.pyp.network.ServerConnection;
import fr.insalyon.pyp.tools.AppTools;
import fr.insalyon.pyp.tools.Constants;
import fr.insalyon.pyp.tools.PYPContext;

public class GetPlacesActivity extends BaseActivity {
			private LinearLayout abstractView;
			private LinearLayout mainView;
			private TextView windowTitle;
			private ListView list;
			private GetPlacesAdapter adapter;
			
			@Override
			public void onCreate(Bundle savedInstanceState) {
				super.onCreate(savedInstanceState, Constants.EVENTS_CONST);
				AppTools.info("on create GetPlacesActivity");
				initGraphicalInterface();
			}

			private void initGraphicalInterface() {
				// set layouts
				LayoutInflater mInflater = LayoutInflater.from(this);
				abstractView = (LinearLayout) findViewById(R.id.abstractLinearLayout);
				mainView = (LinearLayout) mInflater.inflate(R.layout.get_places_activity,
						null);
				abstractView.addView(mainView);
				
				windowTitle = (TextView) findViewById(R.id.pageTitle);
				windowTitle.setText(R.string.GetPlaces);
				
				hideHeader(false);
				
				new GetPlacesTask().execute();
			}

			@Override
			public void onResume() {
				super.onResume();
				//check if logged in
				checkLoggedIn();
			}
			
			
			private void buildList(ArrayList<String[]> data) {

				list = (ListView) findViewById(R.id.get_palces_list);

				// Getting adapter by passing xml data ArrayList
				adapter = new GetPlacesAdapter(this, data);
				list.setAdapter(adapter);

				// Click event for single list row
				list.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
							long arg3) {
//						new SelectPlaceTask().execute(arg1.getTag().toString());
					}
				});
			}
			
			public void networkError(String error) {
				Popups.showPopup("broken");
			}


			private class GetPlacesTask extends AsyncTask<Void, Void, Void> {

				ProgressDialog mProgressDialog;
				JSONObject res;

				@Override
				protected void onPostExecute(Void result) {
					mProgressDialog.dismiss();
					if (res != null) {
						try {
							JSONArray array = res.getJSONArray("list");
							ArrayList<String[]> data = new ArrayList<String[]>();
							for (int i = 0; i < array.length(); i++) {
								JSONObject obj = array.getJSONObject(i);
								data.add(new String[] { obj.getString("name"),
										obj.getString("type"),
										obj.getString("address"), obj.getString("id") });
							}
							AppTools.debug("Number of places:" + array.length());
							GetPlacesActivity.this.buildList(data);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}

				@Override
				protected void onPreExecute() {
					mProgressDialog = ProgressDialog.show(GetPlacesActivity.this,
							getString(R.string.app_name), getString(R.string.loading));
				}

				@Override
				protected Void doInBackground(Void... params) {
					// Send request to server for login

					ServerConnection srvCon = ServerConnection.GetServerConnection();
					List<NameValuePair> parameters = new ArrayList<NameValuePair>();
					parameters.add(new BasicNameValuePair("radius", "5000"));
					parameters.add(new BasicNameValuePair("latitude", "45.771758"));
					parameters.add(new BasicNameValuePair("longitude", "4.889826"));
					// TODO: change
					parameters.add(new BasicNameValuePair("auth_token", PYPContext.getContext().getSharedPreferences(AppTools.PREFS_NAME, 0).getString("auth_token", "")));
					try {
						res = srvCon.connect(ServerConnection.GET_PLACES, parameters);
					} catch (Exception e) {
						e.printStackTrace();
					}
					return null;
				}
			}
			
			
			private class SelectPlaceTask extends AsyncTask<Object, Void, Void> {

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
								GetPlacesActivity.this.networkError(error);
							}

							else {
								// OK
								// Get id of the object
								String[] params = new String[1];
								params[0] = res.getString("id");
								//TODO: link to the new activity
//								IntentHelper.openNewActivity(ChooseInterestActivity.class, params, false);
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}

				@Override
				protected void onPreExecute() {
					mProgressDialog = ProgressDialog.show(GetPlacesActivity.this,
							getString(R.string.app_name), getString(R.string.loading));
				}

				@Override
				protected Void doInBackground(Object... params) {
					// Send request to server for login

					ServerConnection srvCon = ServerConnection.GetServerConnection();
					List<NameValuePair> parameters = new ArrayList<NameValuePair>();
					// Get the token & the username
					String[] paramsGet = IntentHelper.getActiveIntentParam(String[].class);
					String eventId = paramsGet[0];

					parameters.add(new BasicNameValuePair("event_id", eventId));
					parameters.add(new BasicNameValuePair("place_id", (String) params[0]));
					parameters.add(new BasicNameValuePair("is_local", "False"));
					parameters.add(new BasicNameValuePair("auth_token", PYPContext.getContext().getSharedPreferences(AppTools.PREFS_NAME, 0).getString("auth_token", "")));
					
					
					
					try {
						res = srvCon.connect(ServerConnection.SAVE_EVENT_PLACE, parameters);
					} catch (Exception e) {
						e.printStackTrace();
					}
					return null;
				}
			}
	
}
