package fr.insalyon.pyp.gui.main;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ViewFlipper;
import fr.insalyon.pyp.R;
import fr.insalyon.pyp.gui.common.BaseActivity;
import fr.insalyon.pyp.gui.common.IntentHelper;
import fr.insalyon.pyp.gui.events.CreateEventActivity;
import fr.insalyon.pyp.gui.events.ManagePersonalEvents;
import fr.insalyon.pyp.network.ServerConnection;
import fr.insalyon.pyp.tools.AppTools;
import fr.insalyon.pyp.tools.Constants;
import fr.insalyon.pyp.tools.PYPContext;

public class MainActivity extends BaseActivity {
	private LinearLayout abstractView;
	private LinearLayout mainView;
	private TextView windowTitle;
	private ListView list;
	private PersonalEventsAdapter personalEventsadapter;
	private EventsAdapter eventsAdapter;

	private ViewFlipper vf;
	private float lastX;

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
		abstractView.setVisibility(LinearLayout.GONE);
		abstractView = (LinearLayout) findViewById(R.id.abstractLinearLayoutTop);
		abstractView.setVisibility(LinearLayout.VISIBLE);
		mainView = (LinearLayout) mInflater.inflate(R.layout.main_layout, null);
		abstractView.addView(mainView);
		vf = (ViewFlipper) findViewById(R.id.view_flipper);
		windowTitle = (TextView) findViewById(R.id.pageTitle);
		windowTitle.setText(R.string.NoTitle);
		ArrayList<String[]> data = new ArrayList<String[]>();
		data.add(new String[] {getString(R.string.Add_Event)});
		buildList(data);
		new GetPersonalEvents().execute();
		new GetEvents().execute();
		hideHeader(false);
	}

	@Override
	public void onResume() {
		super.onResume();
		// check if logged in
		checkLoggedIn();
		new GetEvents().execute();
		new GetPersonalEvents().execute();
	}

	@Override
	public boolean onTouchEvent(MotionEvent touchevent) {
		switch (touchevent.getAction()) {
		case MotionEvent.ACTION_DOWN: {
			lastX = touchevent.getX();
			break;
		}

		case MotionEvent.ACTION_UP: {

			float currentX = touchevent.getX();
			AppTools.debug("Old:" + lastX + "New:" + currentX);

			if (lastX < currentX && lastX != 0 && currentX - lastX > 100) {
				if (vf.getDisplayedChild() == 0)
					break;
				vf.setInAnimation(this, R.anim.in_from_left);
				vf.setOutAnimation(this, R.anim.out_to_right);
				new GetEvents().execute();
				vf.showNext();
			}

			if (lastX > currentX && lastX != 0 && lastX - currentX > 100) {
				if (vf.getDisplayedChild() == 1)
					break;
				vf.setInAnimation(this, R.anim.in_from_right);
				vf.setOutAnimation(this, R.anim.out_to_left);
				new GetPersonalEvents().execute();
				vf.showPrevious();
			}
			lastX = 0;
			break;
		}
		}
		return false;
	}
	
	


	private void buildList(ArrayList<String[]> data) {

		list = (ListView) findViewById(R.id.personal_events_list);

		// Getting adapter by passing xml data ArrayList
		personalEventsadapter = new PersonalEventsAdapter(this, data);
		if(personalEventsadapter == null){
			AppTools.info("Adapter is null");
			return;
		}
		list.setAdapter(personalEventsadapter);

		// Click event for single list row
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if(arg2 == 0){
					IntentHelper.openNewActivity(CreateEventActivity.class,null,false);
				}
				else {
					String[] tagData = (String[])arg1.getTag();
					IntentHelper.openNewActivity(ManagePersonalEvents.class,tagData,false);

				}
			}
		});
	}

	private class GetPersonalEvents extends AsyncTask<Void, Void, Void> {

		JSONObject res;

		@Override
		protected void onPostExecute(Void result) {
			if (res != null) {
				try {
					JSONArray array = res.getJSONArray("list");
					ArrayList<String[]> data = new ArrayList<String[]>();
					data.add(new String[] {getString(R.string.Add_Event)});
					for (int i = 0; i < array.length(); i++) {
						JSONObject obj = array.getJSONObject(i);
						double lon = Double.parseDouble(obj.getString("lon"));
						double lat = Double.parseDouble(obj.getString("lat"));
						data.add(new String[] { obj.getString("name"),
								obj.getString("start_time")+" - "+obj.getString("end_time"),
								AppTools.checkInArea(lon, lat, Constants.AREA_RADIUS).toString(), obj.getString("id"),
								obj.getString("description")});
					}
					AppTools.debug("Number of personal events:" + data.size());
					MainActivity.this.buildList(data);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		@Override
		protected void onPreExecute() {
			AppTools.debug("Loading persoanl events");
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
				res = srvCon.connect(ServerConnection.GET_PERSONAL_EVENTS, parameters);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
	}
	
	
	private void buildListEvents(ArrayList<String[]> data) {

		list = (ListView) findViewById(R.id.events_list);

		// Getting adapter by passing xml data ArrayList
		eventsAdapter = new EventsAdapter(this, data);
		if(eventsAdapter == null){
			AppTools.info("Adapter is null");
			return;
		}
		list.setAdapter(eventsAdapter);

		// Click event for single list row
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if(arg2 == 0){
					IntentHelper.openNewActivity(CreateEventActivity.class,null,false);
				}
				else {
					String[] tagData = (String[])arg1.getTag();
					IntentHelper.openNewActivity(ManagePersonalEvents.class,tagData,false);

				}
			}
		});
	}
	
	
	
	
	private class GetEvents extends AsyncTask<Void, Void, Void> {

		JSONObject res;
		ProgressDialog mProgressDialog;

		@Override
		protected void onPostExecute(Void result) {
			mProgressDialog.dismiss();
			if (res != null) {
				try {
					JSONArray array = res.getJSONArray("list");
					ArrayList<String[]> data = new ArrayList<String[]>();
					data.add(new String[] {getString(R.string.Events)});
					for (int i = 0; i < array.length(); i++) {
						JSONObject obj = array.getJSONObject(i);
						double lon = Double.parseDouble(obj.getString("lon"));
						double lat = Double.parseDouble(obj.getString("lat"));
						data.add(new String[] { obj.getString("name"),
								obj.getString("type"),
								obj.getString("start_time")+" - "+obj.getString("end_time"),
								AppTools.checkInArea(lon, lat, Constants.AREA_RADIUS).toString(), obj.getString("id") });
					}
					AppTools.debug("Number of personal events:" + data.size());
					MainActivity.this.buildListEvents(data);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		@Override
		protected void onPreExecute() {
			mProgressDialog = ProgressDialog.show(MainActivity.this,
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
				parameters.add(new BasicNameValuePair("radius", "5000"));
				parameters.add(new BasicNameValuePair("latitude", "45.771758"));
				parameters.add(new BasicNameValuePair("longitude", "4.889826"));
				parameters.add(new BasicNameValuePair("auth_token", settings
						.getString("auth_token", "")));
				res = srvCon.connect(ServerConnection.GET_EVENTS, parameters);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
	}
	
}
