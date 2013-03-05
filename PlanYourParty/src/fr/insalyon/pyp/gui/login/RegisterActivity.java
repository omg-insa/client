package fr.insalyon.pyp.gui.login;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
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
import fr.insalyon.pyp.gui.common.popup.Popups;
import fr.insalyon.pyp.network.ServerConnection;
import fr.insalyon.pyp.tools.AppTools;
import fr.insalyon.pyp.tools.Constants;
import fr.insalyon.pyp.tools.PYPContext;

public class RegisterActivity extends BaseActivity {
	private LinearLayout abstractView;
	private ScrollView mainView;
	private Button registerButton;
	private Button cancelButton;
	private TextView usernameField;
	private TextView passwordField;
	private TextView repasswrodField;
	private TextView emailField;
	private TextView windowTitle;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState, Constants.REGISTER_CONST);
		AppTools.info("on create RegisterActivity");
		initGraphicalInterface();
	}

	private void initGraphicalInterface() {
		// set layouts
		LayoutInflater mInflater = LayoutInflater.from(this);
		abstractView = (LinearLayout) findViewById(R.id.abstractLinearLayout);
		mainView = (ScrollView) mInflater.inflate(R.layout.register_activity,
				null);
		abstractView.addView(mainView);
		registerButton = (Button) findViewById(R.id.Register_buttonConfirm);
		cancelButton = (Button) findViewById(R.id.Register_buttonCancel);
		usernameField = (TextView) findViewById(R.id.Register_UserName);
		passwordField = (TextView) findViewById(R.id.Register_Password);
		emailField = (TextView) findViewById(R.id.Register_Email);
		repasswrodField = (TextView) findViewById(R.id.Register_RePassword);
		Drawable background = ((LinearLayout) findViewById(R.id.register_backgorund))
				.getBackground();
		background.setAlpha(95);
		
		windowTitle = (TextView) findViewById(R.id.pageTitle);
		windowTitle.setText(R.string.RegisterTitle);
		
		hideHeader(false);
		cancelButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				RegisterActivity.this.finish();
			}
		});
		registerButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (usernameField.getText().toString().equals("")
						|| passwordField.getText().toString().equals("")
						|| emailField.getText().toString().equals("")) {
					Popups.showPopup(Constants.IncompleatData);
					return;
				}
				if (passwordField.getText().toString()
						.equals(repasswrodField.getText().toString()) == false) {
					Popups.showPopup(Constants.passwordsDontMatch);
					return;
				}
				if (passwordField.getText().length() < 4) {
					Popups.showPopup(Constants.passwordTooShort);
					return;
				}
				if (!AppTools.isEmailValid(emailField.getText().toString())) {
					Popups.showPopup(Constants.InvalidEmail);
					return;
				}
				new RegisterTask().execute();
			}
		});
		
		cancelButton.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				finish();
			}
		});
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	private class RegisterTask extends AsyncTask<Void, Void, Void> {

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
						if (error.equals("Invalid email")) {
							Popups.showPopup(Constants.InvalidEmail);
							return;
						}
						if (error.equals("Password too short")) {
							Popups.showPopup(Constants.passwordTooShort);
							return;
						}
						if (error.equals("Email already used")) {
							Popups.showPopup(Constants.EmailAlreadyUsed);
							return;
						}
						if (error.equals("User already registered")) {
							Popups.showPopup(Constants.UsernameAlreadyUsed);
							return;
						}
						if (error.equals("Incomplete data")) {
							Popups.showPopup(Constants.IncompleatData);
							return;
						}
					} else {
						String[] params = new String[2];
						params[0] = usernameField.getText().toString();
						params[1] = passwordField.getText().toString();
						Intent intent = new Intent(PYPContext.getContext(),
								RegisterActivity.class);
						intent.putExtra(Constants.PARAMNAME, params);
						setResult(RESULT_OK, intent);
						RegisterActivity.this.finish();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}
		}

		@Override
		protected void onPreExecute() {
			mProgressDialog = ProgressDialog.show(RegisterActivity.this,
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
			parameters.add(new BasicNameValuePair("email", emailField.getText()
					.toString()));
			try {
				res = srvCon.connect(ServerConnection.REGISTER, parameters);
			} catch (Exception e) {
					e.printStackTrace();
			}
			return null;
		}
	}
}
