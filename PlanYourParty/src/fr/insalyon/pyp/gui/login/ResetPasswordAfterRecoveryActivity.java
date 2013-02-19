package fr.insalyon.pyp.gui.login;

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
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import fr.insalyon.pyp.R;
import fr.insalyon.pyp.gui.common.BaseActivity;
import fr.insalyon.pyp.gui.common.IntentHelper;
import fr.insalyon.pyp.gui.common.popup.Popups;
import fr.insalyon.pyp.network.ServerConnection;
import fr.insalyon.pyp.tools.AppTools;
import fr.insalyon.pyp.tools.Constants;

public class ResetPasswordAfterRecoveryActivity extends BaseActivity{
	private LinearLayout abstractView;
	private LinearLayout mainView;
	
	private Button cancelButton;
	private Button validateButton;
	
	private EditText oldPassword;
	private EditText newPassword;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState, Constants.REGISTER_CONST);
		AppTools.info("on create ResetPasswordAfterRecovery");
		initGraphicalInterface();
	}

	private void initGraphicalInterface() {
		// set layouts
		LayoutInflater mInflater = LayoutInflater.from(this);
		abstractView = (LinearLayout) findViewById(R.id.abstractLinearLayout);
		mainView = (LinearLayout) mInflater.inflate(R.layout.reset_password_after_recovery_activity, null);
		abstractView.addView(mainView);
		
		validateButton = (Button) findViewById(R.id.validate);
		cancelButton = (Button) findViewById(R.id.cancel);
		oldPassword = (EditText) findViewById(R.id.oldPassword);
		newPassword = (EditText) findViewById(R.id.newPassword);
		
		cancelButton.setOnClickListener( new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		validateButton.setOnClickListener( new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (oldPassword.getText().toString().equals("")
						&& newPassword.getText().toString().equals("")){
					Popups.showPopup(Constants.IncompleatData);
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
		// TODO Auto-generated method stub
		
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
					}

					else {
						// OK
						String tmpToken = res.getString("tmp_token");
						SharedPreferences settings = getSharedPreferences(Constants.TAG, 0);
					    settings.edit().putString("tmp_token", tmpToken);
					    // Redirect to reset password after recovery
					    IntentHelper.openNewActivity(ResetPasswordAfterRecoveryActivity.class, null, false);
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
			// Get the token
			SharedPreferences settings = getSharedPreferences(Constants.TAG, 0);
		    String tmpToken = settings.getString("tmp_token", null);
		    if( tmpToken == null)
		    	return null; // TODO: popup error
			parameters.add(new BasicNameValuePair("username", tmpToken));
			parameters.add(new BasicNameValuePair("new_password", newPassword
					.getText().toString()));
			//TODO: Get the username
			String username = null;
			parameters.add(new BasicNameValuePair("birthday", username));
			
			try {
				res = srvCon.connect(ServerConnection.CHECK_SECRET_ANSWER, parameters);
			} catch (Exception e) {
					e.printStackTrace();
			}
			return null;
		}
	}

	
	
}
