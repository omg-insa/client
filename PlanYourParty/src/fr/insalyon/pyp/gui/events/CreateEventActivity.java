package fr.insalyon.pyp.gui.events;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import fr.insalyon.pyp.R;
import fr.insalyon.pyp.gui.common.BaseActivity;
import fr.insalyon.pyp.gui.common.IntentHelper;
import fr.insalyon.pyp.gui.common.popup.Popups;
import fr.insalyon.pyp.network.ServerConnection;
import fr.insalyon.pyp.tools.AppTools;
import fr.insalyon.pyp.tools.Constants;
import fr.insalyon.pyp.tools.PYPContext;

public class CreateEventActivity extends BaseActivity {
	private LinearLayout abstractView;
	private LinearLayout mainView;

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
		mainView = (LinearLayout) mInflater.inflate(
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
				if (!checkTime()) {
					Popups.showPopup(Constants.dateFormatWrong);
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
						IntentHelper.openNewActivity(GetPlacesActivity.class,
								params, false);
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
					DescriptionEvent.getText().toString()));
			parameters.add(new BasicNameValuePair("start_time", StartEventHour
					+ ":" + StartEventMinute.getText().toString()));
			parameters.add(new BasicNameValuePair("end_time", EndEventHour
					+ ":" + EndEventMinute.getText().toString()));
			parameters.add(new BasicNameValuePair("price", PriceEvent.getText()
					.toString()));
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
					// TODO: Start Event hour minute
					// StartEvent.setText(res.getString("start_time"));
					// EndEvent.setText(res.getString("end_time"));
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
