package eu.chitic.pyp.gui.events;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import eu.chitic.pyp.gui.common.BaseActivity;
import eu.chitic.pyp.gui.common.popup.Popups;
import eu.chitic.pyp.network.ServerConnection;
import eu.chitic.pyp.tools.AppTools;
import eu.chitic.pyp.tools.Constants;
import eu.chitic.pyp.tools.PYPContext;
import eu.chitic.pyp.R;

public class ChatEventActivity extends BaseActivity {
	private LinearLayout abstractView;
	private LinearLayout mainView;
	private ListView list;
	private ChatAdapter adapter;

	private EditText MessageChat;
	private Button SendButtonChat;
	private TextView windowTitle;

	private String event_id = "49018";

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
				R.layout.chat_event_activity, null);
		abstractView.addView(mainView);

		windowTitle = (TextView) findViewById(R.id.pageTitle);
		windowTitle.setText(R.string.NoTitle);

		hideHeader(false);

		MessageChat = (EditText) findViewById(R.id.MessageChat);
		SendButtonChat = (Button) findViewById(R.id.sendButtonChat);
		/*String[] data = IntentHelper.getActiveIntentParam(String[].class);
		if (data != null) {
			//event_id = data[0];
			new GetConversation().execute(event_id);
		}*/
		new GetConversation().execute(event_id);

		SendButtonChat.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if( "".equals(MessageChat.getText().toString())){
					Popups.showPopup(Constants.IncompleatData);
					return;
				}
				new SendMessageTask().execute();
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
		/*AppTools.error("Autocompleting...");
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
	    }*/
	}
	
	private void buildList(ArrayList<String[]> data) {

		list = (ListView) findViewById(R.id.get_chat_list);

		// Getting adapter by passing xml data ArrayList
		adapter = new ChatAdapter(this, data);
		list.setAdapter(adapter);
	}
	
	

	private class SendMessageTask extends AsyncTask<Void, Void, Void> {

		//ProgressDialog mProgressDialog;
		JSONObject res;

		@Override
		protected void onPostExecute(Void result) {
			if (res != null) {
				try {
					if (res.has("error")) {
						// Error
						String error;
						error = res.getString("error");
						ChatEventActivity.this.networkError(error);
					}
					else
					{
						MessageChat.setText("");
						new GetConversation().execute(event_id);
					}
				} catch (JSONException e) {
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
			List<NameValuePair> parameters = new ArrayList<NameValuePair>();
			parameters.add(new BasicNameValuePair("message", MessageChat.getText()
					.toString()));
			if (event_id != null)
				parameters.add(new BasicNameValuePair("event_id", event_id));
			parameters.add(new BasicNameValuePair("auth_token", PYPContext
					.getContext().getSharedPreferences(AppTools.PREFS_NAME, 0)
					.getString("auth_token", "")));
			try {
				res = srvCon.connect(ServerConnection.ADD_MESSAGE,
						parameters);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

	}

	private class GetConversation extends AsyncTask<String, Void, Void> {

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
						data.add(new String[] { obj.getString("date"),
								obj.getString("message"),
								obj.getString("user")});
					}
					ChatEventActivity.this.buildList(data);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		@Override
		protected void onPreExecute() {
			mProgressDialog = ProgressDialog.show(ChatEventActivity.this,
					getString(R.string.app_name), getString(R.string.loading));
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
				res = srvCon.connect(ServerConnection.GET_CONVERSATION,
						parameters);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
	}
}
