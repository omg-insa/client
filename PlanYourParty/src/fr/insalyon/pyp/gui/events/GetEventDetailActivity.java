package fr.insalyon.pyp.gui.events;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import fr.insalyon.pyp.R;
import fr.insalyon.pyp.gui.common.BaseActivity;
import fr.insalyon.pyp.gui.common.IntentHelper;
import fr.insalyon.pyp.network.ServerConnection;
import fr.insalyon.pyp.tools.AppTools;
import fr.insalyon.pyp.tools.Constants;
import fr.insalyon.pyp.tools.PYPContext;

public class GetEventDetailActivity extends BaseActivity {
	private LinearLayout abstractView;
	private LinearLayout mainView;
	private TextView windowTitle;
	private EditText eventNameField;
	private EditText eventTypeField;
	private TextView checkInTxt;
	private String id;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState,
				Constants.GET_EVENT_DETAIL_CONST);
		AppTools.info("on create GetEventDetailActivity");
		initGraphicalInterface();
	}

	private void initGraphicalInterface() {
		// set layouts
		LayoutInflater mInflater = LayoutInflater.from(this);
		abstractView = (LinearLayout) findViewById(R.id.abstractLinearLayout);
		mainView = (LinearLayout) mInflater.inflate(
				R.layout.manage_personal_event_activity, null);
		abstractView.addView(mainView);
		windowTitle = (TextView) findViewById(R.id.pageTitle);
		windowTitle.setText(R.string.GetEventDetail);
		
		eventNameField = (EditText) findViewById(R.id.event_name);
		eventTypeField = (EditText) findViewById(R.id.event_type);
		
		checkInTxt = (TextView) findViewById(R.id.check_in);
		
		
		final String[] data = IntentHelper.getActiveIntentParam(String[].class);
		Drawable background = ((LinearLayout) findViewById(R.id.event_detail_backgorund))
				.getBackground();
		background.setAlpha(95);
		id = data[0];
		AppTools.debug(data[1]);
		if (data[1].equals("false")) {
			checkInTxt.setText("Your in!!!");
		} else {
			checkInTxt.setText("Your out!!!");
		}
		new GetEventStatus().execute(id);
		hideHeader(false);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		AppTools.error("Autocompleating...");
	    if (resultCode == Activity.RESULT_OK) {
	    	String[] row_values = data.getExtras().getStringArray(Constants.PARAMNAME);
	    	if(row_values.length == 3){
	    		this.finish();
	    	}
	    }
	}
	
	@Override
	public void onResume() {
		super.onResume();

	}

	private class GetEventStatus extends AsyncTask<String, Void, Void> {

		JSONObject res;

		@Override
		protected void onPostExecute(Void result) {
			if (res != null) {
				try {
//					if (res.getString("status").equals("Closed")) {
//						open.setVisibility(View.VISIBLE);
//						close.setVisibility(View.GONE);
//
//					} else {
//						close.setVisibility(View.VISIBLE);
//						open.setVisibility(View.GONE);
//					}

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
				parameters.add(new BasicNameValuePair("event_id", params[0]));
				AppTools.debug("ID of the event: " + params[0]);
				res = srvCon.connect(ServerConnection.GET_EVENT_STATUS,
						parameters);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
	}

}
