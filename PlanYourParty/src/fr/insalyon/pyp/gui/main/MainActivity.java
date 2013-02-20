package fr.insalyon.pyp.gui.main;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
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
import fr.insalyon.pyp.gui.common.popup.Popups;
import fr.insalyon.pyp.network.ServerConnection;
import fr.insalyon.pyp.tools.AppTools;
import fr.insalyon.pyp.tools.Constants;

public class MainActivity extends BaseActivity {
			private LinearLayout abstractView;
			private LinearLayout mainView;
			private Button eventsBtn;
			
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
				
				eventsBtn = (Button) findViewById(R.id.button1);

				eventsBtn.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						new LoginTask().execute();
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

			private class LoginTask extends AsyncTask<Void, Void, Void> {

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
					try {
						res = srvCon.connect(ServerConnection.GETEVT, parameters);
					} catch (Exception e) {
						if (e.getMessage().equals("403")) {
							SharedPreferences settings = getSharedPreferences(
									Constants.TAG, 0);
							settings.edit().remove("auth_token");
						} else {
							e.printStackTrace();
						}
					}
					return null;
				}
			}
	
}