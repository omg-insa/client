package fr.insalyon.pyp.gui.main;

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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import fr.insalyon.pyp.R;
import fr.insalyon.pyp.gui.common.BaseActivity;
import fr.insalyon.pyp.gui.common.IntentHelper;
import fr.insalyon.pyp.gui.common.popup.Popups;
import fr.insalyon.pyp.gui.events.CreateEventActivity;
import fr.insalyon.pyp.network.ServerConnection;
import fr.insalyon.pyp.tools.AppTools;
import fr.insalyon.pyp.tools.Constants;
import fr.insalyon.pyp.tools.PYPContext;

public class MainActivity extends BaseActivity {
			private LinearLayout abstractView;
			private LinearLayout mainView;
			private Button buttonGetEvent;
			private Button buttonAddEvent;
			
			@Override
			public void onCreate(Bundle savedInstanceState) {
				super.onCreate(savedInstanceState, Constants.MAIN_CONST);
				AppTools.info("on create MainActivity");
				initGraphicalInterface();
			}

			private void initGraphicalInterface() {
				// set layouts
				LayoutInflater mInflater = LayoutInflater.from(this);
				abstractView = (LinearLayout) findViewById(R.id.abstractLinearLayout);
				mainView = (LinearLayout) mInflater.inflate(R.layout.main_layout,
						null);
				abstractView.addView(mainView);
				hideHeader(false);
				
				buttonGetEvent = (Button) findViewById(R.id.buttonGetEvent);

				buttonGetEvent.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						new EventTask().execute();
					}
				});
				
				buttonAddEvent = (Button) findViewById(R.id.buttonAddEvent);
				
				buttonAddEvent.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						IntentHelper.openNewActivity(CreateEventActivity.class, null, false);
					}
				});
			}

			@Override
			public void onResume() {
				super.onResume();
				//check if logged in
				checkLoggedIn();
			}
			
			public void networkError(String error) {
				Popups.showPopup("broken");
			}

			public void populateEvents(JSONArray events) {

				TableLayout table = (TableLayout) findViewById(R.id.eventsTable);

				for (int i = 0; i < events.length(); i++) {

					TableRow row = new TableRow(this);

					TextView tv = new TextView(this);

					try {
						tv.setText(events.getJSONObject(i).getString("name"));
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					tv.setGravity(Gravity.LEFT);

					row.addView(tv);

					table.addView(row);

				}

			}

			private class EventTask extends AsyncTask<Void, Void, Void> {

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
								MainActivity.this.networkError(error);
							}

							else {
								// OK
								populateEvents(res.getJSONArray("results"));

							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}

				@Override
				protected void onPreExecute() {
					mProgressDialog = ProgressDialog.show(MainActivity.this,
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
					parameters.add(new BasicNameValuePair("auth_token", PYPContext.getContext().getSharedPreferences(AppTools.PREFS_NAME, 0).getString("auth_token", "")));
					try {
						res = srvCon.connect(ServerConnection.GETEVT, parameters);
					} catch (Exception e) {
						e.printStackTrace();
					}
					return null;
				}
			}
	
}
