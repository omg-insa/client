package eu.chitic.pyp.gui.account;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import eu.chitic.pyp.gui.common.BaseActivity;
import eu.chitic.pyp.gui.common.popup.Popups;
import eu.chitic.pyp.network.ServerConnection;
import eu.chitic.pyp.tools.AppTools;
import eu.chitic.pyp.tools.Constants;
import eu.chitic.pyp.tools.PYPContext;
import eu.chitic.pyp.R;

public class PersonalInfoActivity extends BaseActivity {
	private LinearLayout abstractView;
	private ScrollView mainView;
	Spinner sexSpinner;
	Spinner statusSpinner;
	private TextView fullName;
	private TextView email;
	private TextView day;
	private TextView month;
	private TextView year;
	private Button sendButton;
	private TextView windowTitle;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState, Constants.PERSONAINFO_CONST);
		AppTools.info("on create PersonalInfoActivity");
		initGraphicalInterface();
	}

	private void initGraphicalInterface() {
		// set layouts
		LayoutInflater mInflater = LayoutInflater.from(this);
		abstractView = (LinearLayout) findViewById(R.id.abstractLinearLayout);
		mainView = (ScrollView) mInflater.inflate(
				R.layout.personal_info_activity, null);
		abstractView.addView(mainView);
		fullName = (TextView) findViewById(R.id.persoanl_fullname);
		email = (TextView) findViewById(R.id.perosal_Email);
		day = (TextView) findViewById(R.id.persoanl_days);
		month = (TextView) findViewById(R.id.persoanl_months);
		year = (TextView) findViewById(R.id.personal_years);
		sendButton = (Button) findViewById(R.id.personal_info_send);
		sexSpinner = (Spinner) findViewById(R.id.sex_spinner);
		// Create an ArrayAdapter using the string array and a default spinner
		// layout
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.sex_array, android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		sexSpinner.setAdapter(adapter);
		statusSpinner = (Spinner) findViewById(R.id.status_spinner);
		// Create an ArrayAdapter using the string array and a default spinner
		// layout
		ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(
				this, R.array.status_array,
				android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		statusSpinner.setAdapter(adapter2);

		Drawable background = ((LinearLayout) findViewById(R.id.personal_info_backgorund))
				.getBackground();
		background.setAlpha(95);
		
		windowTitle = (TextView) findViewById(R.id.pageTitle);
		windowTitle.setText(R.string.PersonalInfoTitle);
		
		sendButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (day.getText().toString().equals("")
						|| month.getText().toString().equals("")
						|| year.getText().toString().equals("")
						|| fullName.getText().toString().equals("")
						|| email.getText().toString().equals("")
						|| sexSpinner.getSelectedItemId() == 0
						|| statusSpinner.getSelectedItemId() == 0) {
					Popups.showPopup(Constants.IncompleatData);
					return;
				}
				if (AppTools.isEmailValid(email.getText().toString()) == false) {
					Popups.showPopup(Constants.InvalidEmail);
					return;
				}
				if (!checkDate())
					return;
				new PersonalInfoChangeTask().execute();
			}
		});
	}

	private boolean checkDate() {
		try {
			if (Integer.parseInt(day.getText().toString()) > 31
					|| Integer.parseInt(day.getText().toString()) < 1
					|| Integer.parseInt(month.getText().toString()) > 12
					|| Integer.parseInt(month.getText().toString()) < 1
					|| Integer.parseInt(year.getText().toString()) > Calendar
							.getInstance().get(Calendar.YEAR)
					|| Integer.parseInt(year.getText().toString()) < 1900) {
				Popups.showPopup(Constants.dateFormatWrong);
				AppTools.debug("Wrong numbers in date" + day.getText()
						+ month.getText() + year.getText());
				return false;

			}
		} catch (NumberFormatException e) {
			AppTools.debug("NumberFormatException numbers in date"
					+ day.getText() + month.getText() + year.getText());
			Popups.showPopup(Constants.dateFormatWrong);
			return false;
		}
		return true;
	}

	@Override
	public void onResume() {
		super.onResume();
		checkLoggedIn();
		new GetPersonalInfoTask().execute();
	}

	private class PersonalInfoChangeTask extends AsyncTask<Void, Void, Void> {

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
						if (error.equals("Email already used")) {
							Popups.showPopup(Constants.EmailAlreadyUsed);
							return;
						}
						if (error.equals("Incomplete data")) {
							Popups.showPopup(Constants.IncompleatData);
							return;
						}
					}

					else {
						// Update ok pop up
						Popups.showPopup(Constants.UpdatePersonalInfoOk);
						AppTools.debug("Pesonal info update ok");
						return;
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}

		@Override
		protected void onPreExecute() {
			mProgressDialog = ProgressDialog.show(PersonalInfoActivity.this,
					getString(R.string.app_name), getString(R.string.loading));
		}

		@Override
		protected Void doInBackground(Void... params) {
			// Send request to server for login
			ServerConnection srvCon = ServerConnection.GetServerConnection();
			List<NameValuePair> parameters = new ArrayList<NameValuePair>();
			parameters.add(new BasicNameValuePair("full_name", fullName
					.getText().toString()));
			parameters.add(new BasicNameValuePair("email", email.getText()
					.toString()));
			parameters.add(new BasicNameValuePair("sex", String
					.valueOf(sexSpinner.getSelectedItemId())));
			;
			parameters.add(new BasicNameValuePair("status", String
					.valueOf(statusSpinner.getSelectedItemId())));
			parameters.add(new BasicNameValuePair("birthday", year.getText()
					.toString()
					+ month.getText().toString()
					+ day.getText().toString()));
			SharedPreferences settings = PYPContext.getContext()
					.getSharedPreferences(AppTools.PREFS_NAME, 0);
			parameters.add(new BasicNameValuePair("auth_token", settings
					.getString("auth_token", "")));
			try {
				res = srvCon.connect(ServerConnection.UPDATE_PERSONAL_INFO,
						parameters);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
	}

	private class GetPersonalInfoTask extends AsyncTask<Void, Void, Void> {

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
						AppTools.debug("Get persoanl info ok");
						email.setText(res.getString("email"));
						fullName.setText(res.getString("full_name"));
						try {
							sexSpinner.setSelection(Integer.parseInt(res
									.getString("sex")));
							statusSpinner.setSelection(Integer.parseInt(res
									.getString("status")));
						} catch (Exception e) {
							e.printStackTrace();
						}
						if (res.getString("birthday").length() == 8) {
							day.setText(res.getString("birthday").substring(6,
									8));
							year.setText(res.getString("birthday").substring(0,
									4));
							month.setText(res.getString("birthday").substring(
									4, 6));
						}

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
				res = srvCon.connect(ServerConnection.GET_PERSONAL_INFO,
						parameters);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
	}

}
