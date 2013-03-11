package fr.insalyon.pyp.gui.events;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Location;
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
import fr.insalyon.pyp.gui.events.IntrestActivity;
import fr.insalyon.pyp.gui.common.BaseActivity;
import fr.insalyon.pyp.gui.common.IntentHelper;
import fr.insalyon.pyp.gui.common.popup.Popups;
import fr.insalyon.pyp.network.ServerConnection;
import fr.insalyon.pyp.tools.AppTools;
import fr.insalyon.pyp.tools.Constants;
import fr.insalyon.pyp.tools.PYPContext;
import fr.insalyon.pyp.tools.TerminalInfo;

public class GetPlacesActivity extends BaseActivity {
	private LinearLayout abstractView;
	private LinearLayout mainView;
	private TextView windowTitle;
	private ListView list;
	private String event_id;
	private GetPlacesAdapter adapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState, Constants.GET_PLACES_CONST);
		AppTools.info("on create GetPlacesActivity");
		event_id = IntentHelper.getActiveIntentParam(String[].class)[0];
		initGraphicalInterface();
	}

	private void initGraphicalInterface() {
		// set layouts
		LayoutInflater mInflater = LayoutInflater.from(this);
		abstractView = (LinearLayout) findViewById(R.id.abstractLinearLayout);
		mainView = (LinearLayout) mInflater.inflate(
				R.layout.get_places_activity, null);
		abstractView.addView(mainView);

		windowTitle = (TextView) findViewById(R.id.pageTitle);
		windowTitle.setText(R.string.GetPlaces);

		hideHeader(false);

		new GetPlacesTask().execute();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		AppTools.error("Autocompleating...");
	    if (resultCode == Activity.RESULT_OK) {
	    	AppTools.error("ClosingGETPLACEACTIVITY");
	    	String[] row_values = data.getExtras().getStringArray(Constants.PARAMNAME);
	    	if(row_values.length == 3){
	    		Intent intent = new Intent(PYPContext.getContext(),
						GetPlacesActivity.class);
				intent.putExtra(Constants.PARAMNAME, row_values);
				setResult(RESULT_OK, intent);
	    		this.finish();
	    	}
	    }
	}
	
	@Override
	public void onBackPressed() {
		Intent intent = new Intent(PYPContext.getContext(),
				GetPlacesActivity.class);
		intent.putExtra(Constants.PARAMNAME, new String[] { event_id });
		setResult(RESULT_OK, intent);
		this.finish();
		return;
	}

	@Override
	public void onResume() {
		super.onResume();
		// check if logged in
		checkLoggedIn();
		new GetPlacesTask().execute();
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
				String[] tagData = (String[])arg1.getTag();
				if (tagData[0]!= "last")
					new SelectPlaceTask().execute(tagData[0],tagData[1],tagData[2]);
				else {
					String[] params = IntentHelper
							.getActiveIntentParam(String[].class);
					Intent i =  new Intent(GetPlacesActivity.this, CreateLocalPlaceActivity.class);
					i.putExtra(Constants.PARAMNAME,params);
					startActivityForResult(i,1);

				}
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
								obj.getString("address"), obj.getString("id"),obj.getString("source"),obj.getString("reference")});
					}
					data.add(new String[] { "Add location" });
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
			Location l = TerminalInfo.getPosition();
			parameters.add(new BasicNameValuePair("radius", String
					.valueOf(Constants.AREA_RADIUS)));
			parameters.add(new BasicNameValuePair("latitude", String.valueOf(l
					.getLatitude())));
			parameters.add(new BasicNameValuePair("longitude", String.valueOf(l
					.getLongitude())));
			parameters.add(new BasicNameValuePair("auth_token", PYPContext
					.getContext().getSharedPreferences(AppTools.PREFS_NAME, 0)
					.getString("auth_token", "")));
			try {
				res = srvCon.connect(ServerConnection.GET_PLACES, parameters);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
	}

	private class SelectPlaceTask extends AsyncTask<String, Void, Void> {

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
						Intent i =  new Intent(GetPlacesActivity.this, IntrestActivity.class);
						i.putExtra(Constants.PARAMNAME,params);
						startActivityForResult(i,1);
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
		protected Void doInBackground(String... params) {
			// Send request to server for login

			ServerConnection srvCon = ServerConnection.GetServerConnection();
			List<NameValuePair> parameters = new ArrayList<NameValuePair>();
			// Get the token & the username
			

			parameters.add(new BasicNameValuePair("event_id", event_id));
			parameters.add(new BasicNameValuePair("place_id",params[0]));
			parameters.add(new BasicNameValuePair("is_local", params[1]));
			parameters.add(new BasicNameValuePair("place_reference", params[2]));

			parameters.add(new BasicNameValuePair("auth_token", PYPContext
					.getContext().getSharedPreferences(AppTools.PREFS_NAME, 0)
					.getString("auth_token", "")));

			try {
				res = srvCon.connect(ServerConnection.SAVE_EVENT_PLACE,
						parameters);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
	}

}
