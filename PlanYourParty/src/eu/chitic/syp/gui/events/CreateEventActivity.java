package eu.chitic.syp.gui.events;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import eu.chitic.syp.R;
import eu.chitic.syp.gui.common.BaseActivity;
import eu.chitic.syp.gui.common.IntentHelper;
import eu.chitic.syp.gui.common.popup.Popups;
import eu.chitic.syp.network.ServerConnection;
import eu.chitic.syp.tools.AppTools;
import eu.chitic.syp.tools.Constants;
import eu.chitic.syp.tools.PYPContext;

public class CreateEventActivity extends BaseActivity {
	private LinearLayout abstractView;
	private ScrollView mainView;

	private TextView EventName;
	private TextView StartEventHour;
	private TextView StartEventMinute;
	private TextView EndEventHour;
	private TextView EndEventMinute;

	private TextView PriceEvent;
	private TextView DescriptionEvent;
	private Button NextStepBtn;
	private TextView windowTitle;

	private String event_id;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState, Constants.CREATE_EVENTS_CONST);
		AppTools.info("on create CreateEvent");
		initGraphicalInterface();
	}

	private void initGraphicalInterface() {
		// set layouts
		LayoutInflater mInflater = LayoutInflater.from(this);
		abstractView = (LinearLayout) findViewById(R.id.abstractLinearLayout);
		mainView = (ScrollView) mInflater.inflate(
				R.layout.create_event_activity, null);
		abstractView.addView(mainView);

		windowTitle = (TextView) findViewById(R.id.pageTitle);
		windowTitle.setText(R.string.CreateEventTitle);

		hideHeader(false);

		EventName = (TextView) findViewById(R.id.EventName);
		StartEventHour = (TextView) findViewById(R.id.StartEventHour);
		StartEventMinute = (TextView) findViewById(R.id.StartEventMinute);
		EndEventHour = (TextView) findViewById(R.id.EndEventHour);
		EndEventMinute = (TextView) findViewById(R.id.EndEventMinute);
		PriceEvent = (TextView) findViewById(R.id.PriceEvent);
		DescriptionEvent = (TextView) findViewById(R.id.DescriptionEvent);
		NextStepBtn = (Button) findViewById(R.id.NextStepBtn);
		String[] data = IntentHelper.getActiveIntentParam(String[].class);
		if (data != null) {
			event_id = data[0];
			new GetEventInfo().execute(event_id);
		}

		NextStepBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if( "".equals(EventName.getText().toString()) ||
						"".equals(StartEventHour.getText().toString()) ||
						"".equals(StartEventMinute.getText().toString()) ||
						"".equals(EndEventHour.getText().toString()) ||
						"".equals(EndEventMinute.getText().toString()) ||
						"".equals(PriceEvent.getText().toString()) ||
						"".equals(DescriptionEvent.getText().toString()) ){
					Popups.showPopup(Constants.IncompleatData);
					return;
				}
				if (!checkTime()){
					Popups.showPopup(Constants.timeFormatWrong);
					return;
				}
				new CreateEventTask().execute();
			}
		});
	}

	@Override
	public void onResume() {
		super.onResume();
		// check if logged in
		checkLoggedIn();
	}

	public void networkError(String error) {
		Popups.showPopup("broken : " + error);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		AppTools.error("Autocompleating...");
	    if (resultCode == Activity.RESULT_OK) {
	    	String[] row_values = data.getExtras().getStringArray(Constants.PARAMNAME);
	    	if(row_values.length == 3){
	    		Intent intent = new Intent(PYPContext.getContext(),
						CreateEventActivity.class);
				intent.putExtra(Constants.PARAMNAME, row_values);
				setResult(RESULT_OK, intent);
	    		this.finish();
	    	}
	    	event_id = row_values[0];
			new GetEventInfo().execute(event_id);
	    }
	}

	private boolean checkTime() {
		int StartHour = Integer.parseInt(StartEventHour.getText().toString());
		int StartMinute = Integer.parseInt(StartEventMinute.getText()
				.toString());
		int EndHour = Integer.parseInt(EndEventHour.getText().toString());
		int EndMinute = Integer.parseInt(EndEventMinute.getText().toString());
		if (StartHour <= 23 && StartMinute <= 59 && EndHour <= 23
				&& EndMinute <= 59) {
			return true;
		}

		return false;
	}

	private class CreateEventTask extends AsyncTask<Void, Void, Void> {

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
						String[] params = new String[1];
						params[0] = res.getString("id");
						Intent i =  new Intent(CreateEventActivity.this, GetPlacesActivity.class);
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
			mProgressDialog = ProgressDialog.show(CreateEventActivity.this,
					getString(R.string.app_name), getString(R.string.loading));
		}

		@Override
		protected Void doInBackground(Void... params) {
			// Send request to server for login

			ServerConnection srvCon = ServerConnection.GetServerConnection();
			List<NameValuePair> parameters = new ArrayList<NameValuePair>();
			parameters.add(new BasicNameValuePair("name", EventName.getText()
					.toString()));
			parameters.add(new BasicNameValuePair("description",
					Html.fromHtml(DescriptionEvent.getText().toString()).toString()));
			parameters.add(new BasicNameValuePair("start_time", StartEventHour.getText()
					+ ":" + StartEventMinute.getText().toString()));
			parameters.add(new BasicNameValuePair("end_time", EndEventHour.getText()
					+ ":" + EndEventMinute.getText().toString()));
			parameters.add(new BasicNameValuePair("price", Html.fromHtml(PriceEvent.getText()
					.toString()).toString()));
			if (event_id != null)
				parameters.add(new BasicNameValuePair("id", event_id));
			parameters.add(new BasicNameValuePair("auth_token", PYPContext
					.getContext().getSharedPreferences(AppTools.PREFS_NAME, 0)
					.getString("auth_token", "")));
			try {
				res = srvCon.connect(ServerConnection.ADD_EVENT_INFO,
						parameters);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

	}

	private class GetEventInfo extends AsyncTask<String, Void, Void> {

		JSONObject res;

		@Override
		protected void onPostExecute(Void result) {
			if (res != null) {
				try {

					EventName.setText(res.getString("name"));
					
					String startTimeTmp = res.getString("start_time");
					String[] parts = startTimeTmp.trim().split(":");
					StartEventHour.setText(parts[0]);
					StartEventMinute.setText(parts[1]);
					String endTimeTmp = res.getString("end_time");
					parts = endTimeTmp.trim().split(":");
					EndEventHour.setText(parts[0]);
					EndEventMinute.setText(parts[1]);
					PriceEvent.setText(res.getString("price"));
					DescriptionEvent.setText(res.getString("description"));

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected Void doInBackground(String... params) {
			// Send request to server for login
			ServerConnection srvCon = ServerConnection.GetServerConnection();
			try {
				List<NameValuePair> parameters = new ArrayList<NameValuePair>();
				SharedPreferences settings = PYPContext.getContext()
						.getSharedPreferences(AppTools.PREFS_NAME, 0);
				parameters.add(new BasicNameValuePair("auth_token", settings
						.getString("auth_token", "")));
				parameters.add(new BasicNameValuePair("id", params[0]));
				AppTools.debug("ID of the event: " + params[0]);
				res = srvCon.connect(ServerConnection.GET_EVENT_INFO,
						parameters);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
	}
}
