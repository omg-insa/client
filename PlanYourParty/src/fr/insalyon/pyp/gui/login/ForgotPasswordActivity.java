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
import android.widget.ScrollView;
import fr.insalyon.pyp.R;
import fr.insalyon.pyp.gui.common.BaseActivity;
import fr.insalyon.pyp.gui.common.IntentHelper;
import fr.insalyon.pyp.gui.common.popup.Popups;
import fr.insalyon.pyp.network.ServerConnection;
import fr.insalyon.pyp.tools.AppTools;
import fr.insalyon.pyp.tools.Constants;

public class ForgotPasswordActivity extends BaseActivity {
	private LinearLayout abstractView;
	private ScrollView mainView;
	
	private Button cancelButton;
	private Button validateButton;
	private EditText usernameText;
	private EditText secretQuestionText;
	private EditText secretAnswerText;
	private EditText daysText;
	private EditText monthsText;
	private EditText yearsText;
	private boolean usernameFilled = false;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState, Constants.REGISTER_CONST);
		AppTools.info("on create ForgotPasswordActivity");
		initGraphicalInterface();
	}

	private void initGraphicalInterface() {
		// set layouts
		LayoutInflater mInflater = LayoutInflater.from(this);
		abstractView = (LinearLayout) findViewById(R.id.abstractLinearLayout);
		mainView = (ScrollView) mInflater.inflate(R.layout.forgot_password_activity, null);
		abstractView.addView(mainView);
		
		validateButton = (Button) findViewById(R.id.validate);
		cancelButton = (Button) findViewById(R.id.cancel);
		usernameText = (EditText) findViewById(R.id.username);
		secretQuestionText = (EditText) findViewById(R.id.secretQuestion);
		secretAnswerText = (EditText) findViewById(R.id.secretQuestionAnswer);
		daysText = (EditText) findViewById(R.id.days);
		monthsText = (EditText) findViewById(R.id.months);
		yearsText = (EditText) findViewById(R.id.years);
		

		hideSecretQuestion();
		
		// Birthday
		validateButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if(usernameFilled)
				{
					if (usernameText.getText().toString().equals("")){
						Popups.showPopup(Constants.IncompleatData);
						return;
					}
					
					// Send request to get the secret question
					new ForgotPasswordTask().execute();
					usernameFilled = true;
					showSecretQuestion();
				}
				else{
					if (usernameText.getText().toString().equals("")
							&& secretAnswerText.getText().toString().equals("")){
							//&& birthday){ TODO: check birthday
						Popups.showPopup(Constants.IncompleatData);
						return;
					}
					// Verify answer and birthday
					new CheckAnswerTask().execute();
				}
			}
		});
		
		cancelButton.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				finish();
			}
		});
		hideHeader(false);
	}
	
	private void hideSecretQuestion()
	{
		secretQuestionText.setVisibility(View.GONE);
		secretAnswerText.setVisibility(View.GONE);
		daysText.setVisibility(View.GONE);
		monthsText.setVisibility(View.GONE);
		yearsText.setVisibility(View.GONE);
	}
	
	private void showSecretQuestion()
	{
		secretQuestionText.setVisibility(View.VISIBLE);
		secretAnswerText.setVisibility(View.VISIBLE);
		daysText.setVisibility(View.VISIBLE);
		monthsText.setVisibility(View.VISIBLE);
		yearsText.setVisibility(View.VISIBLE);
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
		if (error.equals("Username does not exist")) {
			Popups.showPopup(Constants.usernameDoesNotExist);
		}
		if (error.equals("Incomplete data")) {
			Popups.showPopup(Constants.IncompleatData);
		}
	}
	
	/**
	 * Class that get the secret question regarding the username
	 * @author Daniel
	 *
	 */
	private class ForgotPasswordTask extends AsyncTask<Void, Void, Void> {

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
						ForgotPasswordActivity.this.networkError(error);
					}

					else {
						// OK
						String secretQuestion = res.getString("secret_question");
						SharedPreferences settings = getSharedPreferences(Constants.TAG, 0);
					    settings.edit().putString("secret_question", secretQuestion);
					    // TODO: Print the secret question
					    secretQuestionText.setText(secretQuestion);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}

		@Override
		protected void onPreExecute() {
			mProgressDialog = ProgressDialog.show(ForgotPasswordActivity.this,
					getString(R.string.app_name), getString(R.string.loading));
		}

		@Override
		protected Void doInBackground(Void... params) {
			// Send request to server for forgot password
			ServerConnection srvCon = ServerConnection.GetServerConnection();
			List<NameValuePair> parameters = new ArrayList<NameValuePair>();
			parameters.add(new BasicNameValuePair("username", usernameText
					.getText().toString()));
			try {
				res = srvCon.connect(ServerConnection.GET_SECRET_QUESTION_FOR_RECOVERY, parameters);
			} catch (Exception e) {
				if (e.getMessage().equals("403")) {
					SharedPreferences settings = getSharedPreferences(Constants.TAG, 0);
				    settings.edit().remove("auth_token");
				} else {
					e.printStackTrace();
				}
			}
			return null;
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
						ForgotPasswordActivity.this.networkError(error);
					}

					else {
						// OK
						String tmpToken = res.getString("tmp_token");
						SharedPreferences settings = getSharedPreferences(Constants.TAG, 0);
					    settings.edit().putString("tmp_token", tmpToken);
					    // TODO: Save the username ??
					    
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
			mProgressDialog = ProgressDialog.show(ForgotPasswordActivity.this,
					getString(R.string.app_name), getString(R.string.loading));
		}

		@Override
		protected Void doInBackground(Void... params) {
			// Send request to server for forgot password
			ServerConnection srvCon = ServerConnection.GetServerConnection();
			List<NameValuePair> parameters = new ArrayList<NameValuePair>();
			parameters.add(new BasicNameValuePair("username", usernameText
					.getText().toString()));
			parameters.add(new BasicNameValuePair("answer", secretAnswerText
					.getText().toString()));
			//TODO: get the birthday with the right fields
			String birthday = "01/01/1990";
			parameters.add(new BasicNameValuePair("birthday", birthday));
			
			try {
				res = srvCon.connect(ServerConnection.CHECK_SECRET_ANSWER, parameters);
			} catch (Exception e) {
					e.printStackTrace();
			}
			return null;
		}
	}
	
}
