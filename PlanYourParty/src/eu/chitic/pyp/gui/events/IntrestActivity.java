package eu.chitic.pyp.gui.events;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import eu.chitic.pyp.gui.common.BaseActivity;
import eu.chitic.pyp.gui.common.IntentHelper;
import eu.chitic.pyp.network.ServerConnection;
import eu.chitic.pyp.tools.AppTools;
import eu.chitic.pyp.tools.Constants;
import eu.chitic.pyp.tools.PYPContext;
import eu.chitic.pyp.R;

public class IntrestActivity extends BaseActivity {
	private LinearLayout abstractView;
	private LinearLayout mainView;
	private ListView list;
	private IntrestsAdapter adapter;
	private TextView windowTitle;
	private Button validateCreateEvent;
	private String event_id;
	private String place_id;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState, Constants.INTRESTS_CONST);
		AppTools.info("on create IntrestsInfoActivity");
		initGraphicalInterface();
	}

	private void initGraphicalInterface() {
		// set layouts
		LayoutInflater mInflater = LayoutInflater.from(this);
		abstractView = (LinearLayout) findViewById(R.id.abstractLinearLayout);
		abstractView.setVisibility(LinearLayout.GONE);
		abstractView = (LinearLayout) findViewById(R.id.abstractLinearLayoutTop);
		abstractView.setVisibility(LinearLayout.VISIBLE);
		mainView = (LinearLayout) mInflater.inflate(
				R.layout.create_event_intersts_activity, null);
		abstractView.addView(mainView);

		windowTitle = (TextView) findViewById(R.id.pageTitle);
		windowTitle.setText(R.string.InterestTitle);

		validateCreateEvent = (Button) findViewById(R.id.validate_create_event);
		validateCreateEvent.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(PYPContext.getContext(),
						IntrestActivity.class);
				intent.putExtra(Constants.PARAMNAME, new String[] { event_id,place_id,"forceClose" });
				setResult(RESULT_OK, intent);
				IntrestActivity.this.finish();
			}
		});

		String[] params = IntentHelper.getActiveIntentParam(String[].class);
		event_id = params[0];
		if (params.length == 2) {
			place_id = params[1];
		}

	}

	@Override
	public void onBackPressed() {
		if (place_id != null) {
			Intent intent = new Intent(PYPContext.getContext(),
					IntrestActivity.class);
			intent.putExtra(Constants.PARAMNAME, new String[] { event_id,place_id });
			setResult(RESULT_OK, intent);
			this.finish();
			return;
		}
		super.onBackPressed();
	}

	@Override
	public void onResume() {
		super.onResume();
		checkLoggedIn();
		new GetIntrestsList().execute();
	}

	private void buildList(ArrayList<String[]> data) {

		list = (ListView) findViewById(R.id.create_event_intrests_list);

		// Getting adapter by passing xml data ArrayList
		adapter = new IntrestsAdapter(this, data);
		list.setAdapter(adapter);

		// Click event for single list row
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				String interestId = null;
				if (arg1 != null) {

					AppTools.debug("On Item Click");

					ImageView full = (ImageView) arg1
							.findViewById(R.id.item_full);
					ImageView empty = (ImageView) arg1
							.findViewById(R.id.item_empty);
					if (full.getVisibility() == View.VISIBLE) {
						full.setVisibility(View.GONE);
						empty.setVisibility(View.VISIBLE);
					} else {
						empty.setVisibility(View.GONE);
						full.setVisibility(View.VISIBLE);
					}

					interestId = arg1.getTag().toString();
				}
				if (interestId != null)
					new UpdateUserIntrest().execute(interestId);
			}
		});
	}

	private class GetIntrestsList extends AsyncTask<Void, Void, Void> {

		JSONObject res;
		ProgressDialog mProgressDialog;

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
								obj.getString("description"),
								obj.getString("selected"), obj.getString("id") });
					}
					/*
					 * for (int i = 0; i < 10; i++) { data.add(new String[] {
					 * "name"+Integer.toString(i),
					 * "description"+Integer.toString(i), "false",
					 * "id"+Integer.toString(i) }); }
					 */
					// AppTools.debug("Number of intrests:" + array.length());
					IntrestActivity.this.buildList(data);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		@Override
		protected void onPreExecute() {
			mProgressDialog = ProgressDialog.show(IntrestActivity.this,
					getString(R.string.app_name), getString(R.string.loading));
			AppTools.debug("Loading events");
		}

		@Override
		protected Void doInBackground(Void... params) {
			// Send request to server for login
			ServerConnection srvCon = ServerConnection.GetServerConnection();
			try {
				List<NameValuePair> parameters = new ArrayList<NameValuePair>();
				SharedPreferences settings = PYPContext.getContext()
						.getSharedPreferences(AppTools.PREFS_NAME, 0);
				parameters.add(new BasicNameValuePair("auth_token", settings
						.getString("auth_token", "")));
				parameters.add(new BasicNameValuePair("event_id", event_id));
				res = srvCon.connect(ServerConnection.GET_EVENT_INTREST,
						parameters);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
	}

	private class UpdateUserIntrest extends AsyncTask<Object, Void, Void> {

		@Override
		protected void onPostExecute(Void result) {

		}

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected Void doInBackground(Object... params) {
			// Send request to server for login
			ServerConnection srvCon = ServerConnection.GetServerConnection();

			try {
				List<NameValuePair> parameters = new ArrayList<NameValuePair>();
				SharedPreferences settings = PYPContext.getContext()
						.getSharedPreferences(AppTools.PREFS_NAME, 0);
				parameters.add(new BasicNameValuePair("auth_token", settings
						.getString("auth_token", "")));
				parameters.add(new BasicNameValuePair("event_id", event_id));
				parameters.add(new BasicNameValuePair("intrest_id",
						(String) params[0]));
				srvCon.connect(ServerConnection.SAVE_EVENT_INTREST, parameters);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
	}
}