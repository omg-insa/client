package fr.insalyon.pyp.gui.events;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
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

public class ManagePersonalEvents extends BaseActivity {
	private LinearLayout abstractView;
	private LinearLayout mainView;
	private TextView windowTitle;
	private Button edit;
	private Button close;
	private Button delete;
	private Button open;
	private String id;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState,
				Constants.MANAGE_PERSONAL_EVENTS_CONST);
		AppTools.info("on create LoginActivity");
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
		windowTitle.setText(R.string.ManagePersonalEvent);
		open = (Button) findViewById(R.id.ManagePersonalEventActivity_Open_button);
		close = (Button) findViewById(R.id.ManagePersonalEventActivity_Close_button);
		edit = (Button) findViewById(R.id.ManagePersonalEventActivity_Edit_button);
		delete = (Button) findViewById(R.id.ManagePersonalEventActivity_Delete_button);
		final String[] data = IntentHelper.getActiveIntentParam(String[].class);
		Drawable background = ((LinearLayout) findViewById(R.id.profile_event_personal_backgorund))
				.getBackground();
		background.setAlpha(95);
		id = data[0];
		AppTools.debug(data[1]);
		if (data[1].equals("false")) {
			edit.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Popups.showPopup(Constants.cantEditEvent);
				}
			});
		} else {
			edit.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent i =  new Intent(ManagePersonalEvents.this, CreateEventActivity.class);
					i.putExtra(Constants.PARAMNAME,data);
					startActivityForResult(i,1);
				}
			});
		}
		open.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new closeOpenEvent().execute(id);
			}
		});

		close.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new closeOpenEvent().execute(id);
			}
		});

		delete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				DialogInterface.OnClickListener newListner = new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						AppTools.debug("Deleting....");
						new deleteEvent().execute(id);
					}
				};
				Popups.showPopup(Constants.deleteQuestion, newListner);
			}
		});
		open.setVisibility(View.GONE);
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
					if (res.getString("status").equals("Closed")) {
						open.setVisibility(View.VISIBLE);
						close.setVisibility(View.GONE);

					} else {
						close.setVisibility(View.VISIBLE);
						open.setVisibility(View.GONE);
					}

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

	private class deleteEvent extends AsyncTask<String, Void, Void> {

		@Override
		protected void onPostExecute(Void result) {
			ManagePersonalEvents.this.finish();
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
				srvCon.connect(ServerConnection.DELETE_EVENT, parameters);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
	}

	private class closeOpenEvent extends AsyncTask<String, Void, Void> {

		@Override
		protected void onPostExecute(Void result) {
			if (open.getVisibility() == View.GONE) {
				open.setVisibility(View.VISIBLE);
				close.setVisibility(View.GONE);

			} else {
				close.setVisibility(View.VISIBLE);
				open.setVisibility(View.GONE);
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
				srvCon.connect(ServerConnection.CLOSE_OPEN_EVENT, parameters);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
	}
}
