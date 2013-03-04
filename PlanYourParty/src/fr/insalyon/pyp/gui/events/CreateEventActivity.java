package fr.insalyon.pyp.gui.events;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import fr.insalyon.pyp.R;
import fr.insalyon.pyp.gui.common.BaseActivity;
import fr.insalyon.pyp.gui.common.popup.Popups;
import fr.insalyon.pyp.network.ServerConnection;
import fr.insalyon.pyp.tools.AppTools;
import fr.insalyon.pyp.tools.Constants;
import fr.insalyon.pyp.tools.PYPContext;

public class CreateEventActivity extends BaseActivity {
			private LinearLayout abstractView;
			private LinearLayout mainView;
			
			private TextView EventName;
			private TextView StartEvent;
			private TextView EndEvent;
			
			private TextView PriceEvent;
			private TextView DescriptionEvent;
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
				mainView = (LinearLayout) mInflater.inflate(R.layout.main_layout,
						null);
				abstractView.addView(mainView);
				hideHeader(false);
				
				
				EventName = (TextView) findViewById(R.id.EventName);
				StartEvent = (TextView) findViewById(R.id.StartEvent);
				EndEvent = (TextView) findViewById(R.id.EndEvent);
				PriceEvent = (TextView) findViewById(R.id.PriceEvent);
				DescriptionEvent = (TextView) findViewById(R.id.DescriptionEvent);
				NextStepBtn = (Button) findViewById(R.id.NextStepBtn);

				
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
								CreateEventActivity.this.networkError(error);
							}

							else {
								// OK
								//populateEvents(res.getJSONArray("results"));

							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}

				@Override
				protected void onPreExecute() {
					mProgressDialog = ProgressDialog.show(CreateEventActivity.this,
							getString(R.string.app_name), getString(R.string.loading));
				}

				@Override
				protected Void doInBackground(Void... params) {
					// Send request to server for login

					ServerConnection srvCon = ServerConnection.GetServerConnection();
					List<NameValuePair> parameters = new ArrayList<NameValuePair>();
					parameters.add(new BasicNameValuePair("name", EventName
							.getText().toString()));
					parameters.add(new BasicNameValuePair("description", DescriptionEvent
							.getText().toString()));
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
