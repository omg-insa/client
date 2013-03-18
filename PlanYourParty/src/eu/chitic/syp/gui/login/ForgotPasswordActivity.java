package eu.chitic.syp.gui.login;

import java.util.ArrayList;
import java.util.Calendar;
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
import android.widget.TextView;
import eu.chitic.syp.R;
import eu.chitic.syp.gui.common.BaseActivity;
import eu.chitic.syp.gui.common.IntentHelper;
import eu.chitic.syp.gui.common.popup.Popups;
import eu.chitic.syp.network.ServerConnection;
import eu.chitic.syp.tools.AppTools;
import eu.chitic.syp.tools.Constants;

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
	private TextView windowTitle;
	private boolean usernameFilled = false;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState, Constants.FORGOT_PASSWORD_CONST);
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
		
		windowTitle = (TextView) findViewById(R.id.pageTitle);
		windowTitle.setText(R.string.ForgotPasswordTitle);
		Drawable background = ((LinearLayout) findViewById(R.id.login2_backgorund))
				.getBackground();
		background.setAlpha(95);
		hideSecretQuestion();
		
		// Birthday
		validateButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if(!usernameFilled)
				{
					if (usernameText.getText().toString().equals("")){
						Popups.showPopup(Constants.IncompleatData);
						return;
					}
					
					// Send request to get the secret question
					new ForgotPasswordTask().execute();
				}
				else{
					if (usernameText.getText().toString().equals("")
							 || secretAnswerText.getText().toString().equals("")
							|| !checkDate()){
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
	
	
	private boolean checkDate() {
		try {
			if (Integer.parseInt(daysText.getText().toString()) > 31
					|| Integer.parseInt(daysText.getText().toString()) < 1
					|| Integer.parseInt(monthsText.getText().toString()) > 12
					|| Integer.parseInt(monthsText.getText().toString()) < 1
					|| Integer.parseInt(yearsText.getText().toString()) > Calendar.getInstance().get(Calendar.YEAR)
					|| Integer.parseInt(yearsText.getText().toString()) < 1900) {
				Popups.showPopup(Constants.dateFormatWrong);
				AppTools.debug("Wrong numbers in date" + daysText.getText() + monthsText.getText() + yearsText.getText());
				return false;

			}
		} catch (NumberFormatException e) {
			AppTools.debug("NumberFormatException numbers in date" + daysText.getText() + monthsText.getText() + yearsText.getText());
			Popups.showPopup(Constants.dateFormatWrong);
			return false;
		}
		return true;
	}
	
	
	public void networkError(String error) {
		if (error.equals("User does not exists")) {
			Popups.showPopup(Constants.WrongUsername);
		}
		if (error.equals("User does not have security questions")) {
			Popups.showPopup(Constants.NoSecretQuestion);
		}
		if (error.equals("Incomplete data")) {
			Popups.showPopup(Constants.IncompleatData);
		}
		if (error.equals("Wrong answer")) {
			Popups.showPopup(Constants.WrongAnswer);
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
					    // Print the secret question
						usernameFilled = true;
						showSecretQuestion();
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
			parameters.add(new BasicNameValuePair("username", Html.fromHtml(usernameText
					.getText().toString()).toString()));
			try {
				res = srvCon.connect(ServerConnection.GET_SECRET_QUESTION_FOR_RECOVERY, parameters);
			} catch (Exception e) {
				e.printStackTrace();
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
					}else {
						// OK
						String tmpToken = res.getString("tmp_token");
						
					    String[] params = new String[2];
					    params[0] = Html.fromHtml(usernameText.getText().toString()).toString();
					    params[1] = tmpToken;
					    
					    // Redirect to reset password after recovery
					    IntentHelper.openNewActivity(ResetPasswordAfterRecoveryActivity.class, params, false);
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
			parameters.add(new BasicNameValuePair("username", Html.fromHtml(usernameText
					.getText().toString()).toString()));
			parameters.add(new BasicNameValuePair("answer", Html.fromHtml(secretAnswerText
					.getText().toString()).toString()));
			parameters.add(new BasicNameValuePair("birthday", yearsText.getText()
					.toString()
					+ monthsText.getText().toString()
					+ daysText.getText().toString()));
			try {
				res = srvCon.connect(ServerConnection.CHECK_SECRET_ANSWER, parameters);
			} catch (Exception e) {
					e.printStackTrace();
			}
			return null;
		}
	}
	
	
}
