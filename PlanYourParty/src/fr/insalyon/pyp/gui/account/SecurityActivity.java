package fr.insalyon.pyp.gui.account;

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
import android.widget.ScrollView;
import android.widget.TextView;
import fr.insalyon.pyp.R;
import fr.insalyon.pyp.R.id;
import fr.insalyon.pyp.gui.common.BaseActivity;
import fr.insalyon.pyp.gui.common.popup.Popups;
import fr.insalyon.pyp.network.ServerConnection;
import fr.insalyon.pyp.tools.AppTools;
import fr.insalyon.pyp.tools.Constants;
import fr.insalyon.pyp.tools.PYPContext;

public class SecurityActivity extends BaseActivity {
	private LinearLayout abstractView;
	private ScrollView mainView;
	private Button changePassword;
	private Button changeSecretQuestion;
	private TextView currentPassword;
	private TextView secretQuestion;
	private TextView secretAnswer;
	private TextView oldPasswordField;
	private TextView newPasswordField;
	private TextView renewPasswordField;
	private TextView windowTitle;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState, Constants.SECURITY_CONST);
		AppTools.info("on create SecurityActivity");
		initGraphicalInterface();
	}

	private void initGraphicalInterface() {
		// set layouts
		LayoutInflater mInflater = LayoutInflater.from(this);
		abstractView = (LinearLayout) findViewById(R.id.abstractLinearLayout);
		mainView = (ScrollView) mInflater.inflate(R.layout.security_activity,
				null);
		abstractView.addView(mainView);
		
		changeSecretQuestion = (Button) findViewById(R.id.security_question_button);
		currentPassword = (TextView) findViewById(R.id.security_current_password);
		secretAnswer = (TextView) findViewById(id.security_secret_answer);
		secretQuestion = (TextView) findViewById(id.security_secret_question);
		
		changePassword = (Button) findViewById(R.id.security_password_button);
		newPasswordField = (TextView) findViewById(id.security_new_password);
		oldPasswordField = (TextView) findViewById(id.security_old_password);
		renewPasswordField = (TextView) findViewById(id.security_re_new_password);
		
		windowTitle = (TextView) findViewById(R.id.pageTitle);
		windowTitle.setText(R.string.SecurityTitle);
		
		changeSecretQuestion.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				if (currentPassword.getText().toString().equals("")
						|| secretAnswer.getText().toString().equals("")
						|| secretQuestion.getText().toString().equals("")) {
					Popups.showPopup(Constants.IncompleatData);
					return;
				}
				new SecurityQuestionChangeTask().execute();

			}
		});
		
		changePassword.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				if (newPasswordField.getText().toString()
						.equals(renewPasswordField.getText().toString()) == false) {
					Popups.showPopup(Constants.passwordsDontMatch);
					return;
				}
				if (newPasswordField.getText().toString().equals("")
						|| oldPasswordField.getText().toString().equals("")
						|| renewPasswordField.getText().toString().equals("")) {
					Popups.showPopup(Constants.IncompleatData);
					return;
				}
				if (newPasswordField.getText().length() < 4) {
					Popups.showPopup(Constants.passwordTooShort);
					return;
				}
				new PasswordChangeTask().execute();

			}
		});
		/*
		 * Drawable background = ((LinearLayout)
		 * findViewById(R.id.profile_backgorund)) .getBackground();
		 * background.setAlpha(95);
		 */
	}

	@Override
	public void onResume() {
		super.onResume();
		checkLoggedIn();
		new GetSecretQuestionTask().execute();
	}

	private class PasswordChangeTask extends AsyncTask<Void, Void, Void> {

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
						if (error.equals("Wrong password")) {
							Popups.showPopup(Constants.curentPasswordWrong);
							return;
						}
						if (error.equals("Incomplete data")) {
							Popups.showPopup(Constants.IncompleatData);
							return;
						}
					}

					else {
						AppTools.debug("Password update ok");
						oldPasswordField.setText("");
						newPasswordField.setText("");
						renewPasswordField.setText("");
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}

		@Override
		protected void onPreExecute() {
			mProgressDialog = ProgressDialog.show(SecurityActivity.this,
					getString(R.string.app_name), getString(R.string.loading));
		}

		@Override
		protected Void doInBackground(Void... params) {
			// Send request to server for login
			ServerConnection srvCon = ServerConnection.GetServerConnection();
			List<NameValuePair> parameters = new ArrayList<NameValuePair>();
			parameters.add(new BasicNameValuePair("new_password",
					newPasswordField.getText().toString()));
			parameters.add(new BasicNameValuePair("password", oldPasswordField
					.getText().toString()));
			SharedPreferences settings = PYPContext.getContext()
					.getSharedPreferences(AppTools.PREFS_NAME, 0);
			parameters.add(new BasicNameValuePair("auth_token", settings
					.getString("auth_token", "")));
			try {
				res = srvCon.connect(ServerConnection.UPDATE_PASSWORD,
						parameters);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
	}
	
	private class SecurityQuestionChangeTask extends AsyncTask<Void, Void, Void> {

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
						if (error.equals("Wrong password")) {
							Popups.showPopup(Constants.curentPasswordWrong);
							return;
						}
						if (error.equals("Incomplete data")) {
							Popups.showPopup(Constants.IncompleatData);
							return;
						}
					}

					else {
						AppTools.debug("Security question update ok");
						currentPassword.setText("");

					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}

		@Override
		protected void onPreExecute() {
			mProgressDialog = ProgressDialog.show(SecurityActivity.this,
					getString(R.string.app_name), getString(R.string.loading));
		}

		@Override
		protected Void doInBackground(Void... params) {
			// Send request to server for login
			ServerConnection srvCon = ServerConnection.GetServerConnection();
			List<NameValuePair> parameters = new ArrayList<NameValuePair>();
			parameters.add(new BasicNameValuePair("secret_question",
					secretQuestion.getText().toString()));
			parameters.add(new BasicNameValuePair("secret_answer",
					secretAnswer.getText().toString()));
			parameters.add(new BasicNameValuePair("password", currentPassword
					.getText().toString()));
			SharedPreferences settings = PYPContext.getContext()
					.getSharedPreferences(AppTools.PREFS_NAME, 0);
			parameters.add(new BasicNameValuePair("auth_token", settings
					.getString("auth_token", "")));
			try {
				res = srvCon.connect(ServerConnection.UPDATE_QUESTION,
						parameters);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
	}
	
	private class GetSecretQuestionTask extends AsyncTask<Void, Void, Void> {

		JSONObject res;

		@Override
		protected void onPostExecute(Void result) {
			if (res != null) {
				try {
					if (res.has("error")) {
						// Error
						String error;
						error = res.getString("error");
						AppTools.error(error);
					}

					else {
						AppTools.debug("Get secret question question ok");
						secretAnswer.setText(res.getString("secret_answer"));
						secretQuestion.setText(res.getString("secret_question"));
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
			
			SharedPreferences settings = PYPContext.getContext()
					.getSharedPreferences(AppTools.PREFS_NAME, 0);
			parameters.add(new BasicNameValuePair("auth_token", settings
					.getString("auth_token", "")));
			try {
				res = srvCon.connect(ServerConnection.GET_QUESTION_SECRET,
						parameters);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
	}
}
