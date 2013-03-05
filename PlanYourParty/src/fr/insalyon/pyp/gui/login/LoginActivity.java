package fr.insalyon.pyp.gui.login;

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
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.Drawable;
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
import fr.insalyon.pyp.gui.common.BaseActivity;
import fr.insalyon.pyp.gui.common.IntentHelper;
import fr.insalyon.pyp.gui.common.popup.Popups;
import fr.insalyon.pyp.network.ServerConnection;
import fr.insalyon.pyp.tools.AppTools;
import fr.insalyon.pyp.tools.Constants;
import fr.insalyon.pyp.tools.TerminalInfo;

public class LoginActivity extends BaseActivity {
	private LinearLayout abstractView;
	private ScrollView mainView;
	private Button loginButton;
	private Button registerButton;
	private TextView usernameField;
	private TextView passwordField;
	private TextView forgotPasswordText;
	private TextView windowTitle;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState, Constants.LOGIN_CONST);
		AppTools.info("on create LoginActivity");
		initGraphicalInterface();
	}

	private void initGraphicalInterface() {
		// set layouts
		LayoutInflater mInflater = LayoutInflater.from(this);
		abstractView = (LinearLayout) findViewById(R.id.abstractLinearLayout);
		mainView = (ScrollView) mInflater.inflate(R.layout.login_activity,
				null);
		abstractView.addView(mainView);
		loginButton = (Button) findViewById(R.id.login);
		registerButton = (Button) findViewById(R.id.register);
		usernameField = (TextView) findViewById(R.id.username);
		passwordField = (TextView) findViewById(R.id.password);
		forgotPasswordText = (TextView) findViewById(R.id.forgotPassword);
		Drawable background = ((LinearLayout) findViewById(R.id.login_backgorund))
				.getBackground();
		background.setAlpha(95);
		
		windowTitle = (TextView) findViewById(R.id.pageTitle);
		windowTitle.setText(R.string.LoginTitle);

		loginButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (usernameField.getText().toString().equals("") || passwordField.getText().toString().equals("")){
					Popups.showPopup(Constants.IncompleatData);
					return;
				}
				new LoginTask().execute();
			}
		});
		registerButton.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				Intent i =  new Intent(LoginActivity.this, RegisterActivity.class);
				startActivityForResult(i,1);
			}
		});
		forgotPasswordText.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				IntentHelper.openNewActivity(ForgotPasswordActivity.class, null, false);
			}
		});
		hideHeader(true);
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	public void networkError(String error) {
		if (error.equals("Username and password not matching")) {
			Popups.showPopup(Constants.WrongUsernameOrPassword);
		}
		if (error.equals("Incomplete data")) {
			Popups.showPopup(Constants.IncompleatData);
		}
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		AppTools.error("Autocompleating...");
	    if (resultCode == Activity.RESULT_OK) {
	    	String[] row_values = data.getExtras().getStringArray(Constants.PARAMNAME);
	    	usernameField.setText(row_values[0]);
	    	passwordField.setText(row_values[1]);
			new LoginTask().execute();
	    }
	}

	private class LoginTask extends AsyncTask<Void, Void, Void> {

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
						LoginActivity.this.networkError(error);
					}

					else {
						// OK
						String token = res.getString("auth_token");
						SharedPreferences settings = getSharedPreferences(AppTools.PREFS_NAME, MODE_PRIVATE);
					    Editor editor = settings.edit();
					    editor.putString("auth_token", token);
					    editor.commit();
					    LoginActivity.this.finish();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}

		@Override
		protected void onPreExecute() {
			mProgressDialog = ProgressDialog.show(LoginActivity.this,
					getString(R.string.app_name), getString(R.string.loading));
		}

		@Override
		protected Void doInBackground(Void... params) {
			// Send request to server for login
			ServerConnection srvCon = ServerConnection.GetServerConnection();
			List<NameValuePair> parameters = new ArrayList<NameValuePair>();
			parameters.add(new BasicNameValuePair("username", usernameField
					.getText().toString()));
			parameters.add(new BasicNameValuePair("password", passwordField
					.getText().toString()));
			parameters.add(new BasicNameValuePair("device_type", TerminalInfo
					.getTerminalName()));
			parameters.add(new BasicNameValuePair("device_manufacture",
					TerminalInfo.getTerminalManufacturer()));
			parameters.add(new BasicNameValuePair("device_os", TerminalInfo
					.getOSName()));
			parameters.add(new BasicNameValuePair("os_version", TerminalInfo
					.getOSVersion()));
			parameters.add(new BasicNameValuePair("device_id", TerminalInfo
					.getAndroidID()));
			try {
				res = srvCon.connect(ServerConnection.LOGIN, parameters);
			} catch (Exception e) {
					e.printStackTrace();
			}
			return null;
		}
	}
}
