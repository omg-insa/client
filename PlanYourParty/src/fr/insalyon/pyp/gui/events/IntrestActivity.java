package fr.insalyon.pyp.gui.events;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import fr.insalyon.pyp.R;
import fr.insalyon.pyp.gui.common.BaseActivity;
import fr.insalyon.pyp.network.ServerConnection;
import fr.insalyon.pyp.tools.AppTools;
import fr.insalyon.pyp.tools.Constants;
import fr.insalyon.pyp.tools.PYPContext;

public class IntrestActivity extends BaseActivity {
	private LinearLayout abstractView;
	private LinearLayout mainView;
	private ListView list;
	private IntrestsAdapter adapter;
	private TextView windowTitle;
	private Button validateCreateEvent;
	private ArrayList<Long> interestsChecked = new ArrayList<Long>();
	private Long event_id = 49003L;

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
		mainView = (LinearLayout) mInflater.inflate(R.layout.create_event_intersts_activity,
				null);
		abstractView.addView(mainView);
		
		windowTitle = (TextView) findViewById(R.id.pageTitle);
		windowTitle.setText(R.string.InterestTitle);
		
		validateCreateEvent = (Button) findViewById(R.id.validate_create_event);

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
				if (arg1 != null) {
					
					AppTools.debug("On Item Click");
					
					CheckBox interest_checkBox = (CheckBox) arg1.findViewById(R.id.interest_checkBox);
					//TODO: add business logic
					
					long interestId = adapter.getItemId(arg2);
					
					if(interest_checkBox.isChecked())
					{
						interestsChecked.add(interestId);
						//TODO: check with the API if multiple interests are allowed
						//list.setEnabled(false);
					}
					else
					{
						interestsChecked.remove(interestId);
						//TODO: check with the API if multiple interests are allowed
						//list.setEnabled(true);
					}
				}
				new UpdateUserIntrest().execute(interestsChecked.get(0));
			}
		});
	}

	private class GetIntrestsList extends AsyncTask<Void, Void, Void> {

		JSONObject res;

		@Override
		protected void onPostExecute(Void result) {
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
					/*for (int i = 0; i < 10; i++) {
						data.add(new String[] { "name"+Integer.toString(i),
								"description"+Integer.toString(i),
								"false", "id"+Integer.toString(i) });
					}*/
					//AppTools.debug("Number of intrests:" + array.length());
					IntrestActivity.this.buildList(data);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		@Override
		protected void onPreExecute() {
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
				parameters.add(new BasicNameValuePair("event_id", event_id.toString()));
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
				parameters.add(new BasicNameValuePair("event_id", event_id.toString()));
				parameters.add(new BasicNameValuePair("intrest_id", params[0].toString()));
				srvCon.connect(ServerConnection.SAVE_EVENT_INTREST,
						parameters);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
	}
}