package eu.chitic.syp.gui.login;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import eu.chitic.syp.R;
import eu.chitic.syp.gui.common.BaseActivity;
import eu.chitic.syp.gui.common.IntentHelper;
import eu.chitic.syp.gui.common.popup.Popups;
import eu.chitic.syp.gui.main.MainActivity;
import eu.chitic.syp.network.ServerConnection;
import eu.chitic.syp.tools.AppTools;
import eu.chitic.syp.tools.Constants;

public class ResetPasswordAfterRecoveryActivity extends BaseActivity{
	private LinearLayout abstractView;
	private ScrollView mainView;
	
	private Button cancelButton;
	private Button validateButton;
	
	private EditText newPassword;
	private EditText renewPassword;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState, Constants.RESET_PASSWORD_AFTER_RECOVERY_CONST);
		AppTools.info("on create ResetPasswordAfterRecovery");
		initGraphicalInterface();
	}

	private void initGraphicalInterface() {
		// set layouts
		LayoutInflater mInflater = LayoutInflater.from(this);
		abstractView = (LinearLayout) findViewById(R.id.abstractLinearLayout);
		mainView = (ScrollView) mInflater.inflate(R.layout.reset_password_after_recovery_activity, null);
		abstractView.addView(mainView);
		
		validateButton = (Button) findViewById(R.id.validate);
		cancelButton = (Button) findViewById(R.id.cancel);
		newPassword = (EditText) findViewById(R.id.newPassword);
		renewPassword = (EditText) findViewById(R.id.renewPassword);
		Drawable background = ((LinearLayout) findViewById(R.id.login3_backgorund))
				.getBackground();
		background.setAlpha(95);
		cancelButton.setOnClickListener( new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		
		validateButton.setOnClickListener( new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (newPassword.getText().toString().equals("")
						|| renewPassword.getText().toString().equals("")){
					Popups.showPopup(Constants.IncompleatData);
					return;
				}
				if(!newPassword.getText().toString().equals(renewPassword.getText().toString())){
					Popups.showPopup(Constants.DifferentPassword);
					return;
				}
				// Send request to change the password
				new CheckAnswerTask().execute();
			}
		});
		
		hideHeader(false);
	}

	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	
	public void networkError(String error) {
		if (error.equals("Incomplete data")) {
			Popups.showPopup(Constants.IncompleatData);
		}
		if (error.equals("Wrong data")) {
			Popups.showPopup(Constants.WrongData);
		}
	}
	
	
	/**
	 * Class that check if the answer is correct regarding username, answer & birthday
	 * @author Daniel
	 *
	 */
	private class CheckAnswerTask extends AsyncTask<Void, Void, Void> {

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
						ResetPasswordAfterRecoveryActivity.this.networkError(error);
					}else {
						// OK
						//TODO: Make pop up new password ok!
						IntentHelper.openNewActivity(MainActivity.class, null, false);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}

		@Override
		protected void onPreExecute() {
			mProgressDialog = ProgressDialog.show(ResetPasswordAfterRecoveryActivity.this,
					getString(R.string.app_name), getString(R.string.loading));
		}

		@Override
		protected Void doInBackground(Void... params) {
			// Send request to server for forgot password
			ServerConnection srvCon = ServerConnection.GetServerConnection();
			List<NameValuePair> parameters = new ArrayList<NameValuePair>();
			// Get the token & the username
			String[] paramsGet = IntentHelper.getActiveIntentParam(String[].class);
			String username = paramsGet[0];
			String tmpToken = paramsGet[1];
		    if( tmpToken == null || username == null)
		    	return null; // TODO: popup error
			parameters.add(new BasicNameValuePair("tmp_token", tmpToken));
			parameters.add(new BasicNameValuePair("user", Html.fromHtml(username).toString()));
			parameters.add(new BasicNameValuePair("new_password", newPassword
					.getText().toString()));
			try {
				res = srvCon.connect(ServerConnection.UPDATE_PASSWORD_AFTER_RECOVERY, parameters);
			} catch (Exception e) {
					e.printStackTrace();
			}
			return null;
		}
	}

	
	
}
