package fr.insalyon.pyp.gui.account;

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
import android.widget.ImageView;
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
		mainView = (LinearLayout) mInflater.inflate(R.layout.intersts_activity,
				null);
		abstractView.addView(mainView);
		
		windowTitle = (TextView) findViewById(R.id.pageTitle);
		windowTitle.setText(R.string.InterestTitle);

	}

	@Override
	public void onResume() {
		super.onResume();
		checkLoggedIn();
		new GetIntrestsList().execute();
	}

	private void buildList(ArrayList<String[]> data) {

		list = (ListView) findViewById(R.id.intrests_list);

		// Getting adapter by passing xml data ArrayList
		adapter = new IntrestsAdapter(this, data);
		list.setAdapter(adapter);

		// Click event for single list row
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (arg1 != null) {
					ImageView full = (ImageView) arg1.findViewById(R.id.item_full);
					ImageView empty = (ImageView) arg1.findViewById(R.id.item_empty);
					if (full.getVisibility() == View.VISIBLE) {
						full.setVisibility(View.GONE);
						empty.setVisibility(View.VISIBLE);
					} else {
						empty.setVisibility(View.GONE);
						full.setVisibility(View.VISIBLE);
					}
				}
				new UpdateUserIntrest().execute(arg1.getTag().toString());
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
					AppTools.debug("Number of intrests:" + array.length());
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
				res = srvCon.connect(ServerConnection.GET_INTRESTS_LIST,
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
				parameters.add(new BasicNameValuePair("intrest",
						(String) params[0]));
				srvCon.connect(ServerConnection.UPDATE_USER_INTREST,
						parameters);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
	}
}