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
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import fr.insalyon.pyp.R;
import fr.insalyon.pyp.gui.common.BaseActivity;
import fr.insalyon.pyp.gui.common.popup.Popups;
import fr.insalyon.pyp.network.ServerConnection;
import fr.insalyon.pyp.tools.AppTools;
import fr.insalyon.pyp.tools.Constants;

public class ForgotPasswordActivity extends BaseActivity {
	private LinearLayout abstractView;
	private LinearLayout mainView;
	
	private Button cancelButton;
	private Button validateButton;
	private EditText usernameText;
	private EditText secretQuestionText;
	private EditText secretAnswerText;
	
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
		mainView = (LinearLayout) mInflater.inflate(R.layout.forgot_password_activity, null);
		abstractView.addView(mainView);
		
		validateButton = (Button) findViewById(R.id.validate);
		cancelButton = (Button) findViewById(R.id.cancel);
		usernameText = (EditText) findViewById(R.id.username);
		secretQuestionText = (EditText) findViewById(R.id.secretQuestion);
		secretAnswerText = (EditText) findViewById(R.id.secretQuestionAnswer);
		
		// Birthday
		
		
		usernameText.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (usernameText.getText().toString().equals("")){
					Popups.showPopup(Constants.IncompleatData);
					return;
				}
				// Send request to get the secret question
				new ForgotPasswordTask().execute();
			}
		});
		
		validateButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO: Verify answer and birthday
			}
		});
		
		cancelButton.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				finish();
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
		if (error.equals("Username does not exist")) {
			Popups.showPopup(Constants.usernameDoesNotExist);
		}
		if (error.equals("Incomplete data")) {
			Popups.showPopup(Constants.IncompleatData);
		}
	}
	
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
	
}
