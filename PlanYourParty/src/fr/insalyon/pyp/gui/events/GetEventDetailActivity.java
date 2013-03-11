package fr.insalyon.pyp.gui.events;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
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
import fr.insalyon.pyp.tools.PYPContext;

public class GetEventDetailActivity extends BaseActivity {
	private LinearLayout abstractView;
	private ScrollView mainView;
	private TextView windowTitle;
	private TextView eventNameField;
	private TextView eventTypeField;
	private TextView eventHoursField;
	
	private TextView eventPriceField;
	private TextView eventDescriptionField;
	private TextView eventAgeAverageField;
	private TextView eventFemaleRatioField;
	private TextView eventSingleRatioField;
	private TextView eventHeadcountField;
	
	private TextView eventPlaceNameField;
	private TextView eventPlaceDescriptionField;
	private TextView eventPlaceAddressField;
	
	private ImageView star1;
	private ImageView star2;
	private ImageView star3;
	private ImageView star4;
	private ImageView star5;
	
	private Button checkInButton;
	
	private String eventGrade;
	private String id;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState,
				Constants.GET_EVENT_DETAIL_CONST);
		AppTools.info("on create GetEventDetailActivity");
		initGraphicalInterface();
	}

	private void initGraphicalInterface() {
		// set layouts
		LayoutInflater mInflater = LayoutInflater.from(this);
		abstractView = (LinearLayout) findViewById(R.id.abstractLinearLayout);
		mainView = (ScrollView) mInflater.inflate(
				R.layout.get_event_detail_activity, null);
		abstractView.addView(mainView);
		windowTitle = (TextView) findViewById(R.id.pageTitle);
		windowTitle.setText(R.string.GetEventDetail);
		
		eventNameField = (TextView) findViewById(R.id.event_name);
		eventHoursField = (TextView) findViewById(R.id.event_hours);
		eventTypeField = (TextView) findViewById(R.id.event_type);
		
		eventPriceField = (TextView) findViewById(R.id.event_price);
		eventDescriptionField = (TextView) findViewById(R.id.event_description);
		eventAgeAverageField = (TextView) findViewById(R.id.event_age);
		eventFemaleRatioField = (TextView) findViewById(R.id.event_female);
		eventSingleRatioField = (TextView) findViewById(R.id.event_single);
		eventHeadcountField = (TextView) findViewById(R.id.event_headcount);
		
		eventPlaceNameField = (TextView) findViewById(R.id.event_place_name);
		eventPlaceDescriptionField = (TextView) findViewById(R.id.event_place_description);
		eventPlaceAddressField = (TextView) findViewById(R.id.event_place_address);
		
		checkInButton = (Button) findViewById(R.id.checkin);
		checkInButton.setOnClickListener( new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//TODO: give a mark to the party
				//TODO: are you sure you want to send this grade
				new GradeRateEvent().execute(id);
			}
			
		});
		
		star1 = (ImageView) findViewById(R.id.star1);
		star2 = (ImageView) findViewById(R.id.star2);
		star3 = (ImageView) findViewById(R.id.star3);
		star4 = (ImageView) findViewById(R.id.star4);
		star5 = (ImageView) findViewById(R.id.star5);
		
		star1.setOnClickListener( new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				star1.setImageDrawable(PYPContext.getContext().getResources()
						.getDrawable(android.R.drawable.btn_star_big_on));
				star2.setImageDrawable(PYPContext.getContext().getResources()
						.getDrawable(android.R.drawable.btn_star_big_off));
				star3.setImageDrawable(PYPContext.getContext().getResources()
						.getDrawable(android.R.drawable.btn_star_big_off));
				star4.setImageDrawable(PYPContext.getContext().getResources()
						.getDrawable(android.R.drawable.btn_star_big_off));
				star5.setImageDrawable(PYPContext.getContext().getResources()
						.getDrawable(android.R.drawable.btn_star_big_off));
				eventGrade = "1";
			}
		});
		
		star2.setOnClickListener( new OnClickListener() {
					
			@Override
			public void onClick(View v) {
				star1.setImageDrawable(PYPContext.getContext().getResources()
						.getDrawable(android.R.drawable.btn_star_big_on));
				star2.setImageDrawable(PYPContext.getContext().getResources()
						.getDrawable(android.R.drawable.btn_star_big_on));
				star3.setImageDrawable(PYPContext.getContext().getResources()
						.getDrawable(android.R.drawable.btn_star_big_off));
				star4.setImageDrawable(PYPContext.getContext().getResources()
						.getDrawable(android.R.drawable.btn_star_big_off));
				star5.setImageDrawable(PYPContext.getContext().getResources()
						.getDrawable(android.R.drawable.btn_star_big_off));
				eventGrade = "2";
			}
		});
		
		star3.setOnClickListener( new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				star1.setImageDrawable(PYPContext.getContext().getResources()
						.getDrawable(android.R.drawable.btn_star_big_on));
				star2.setImageDrawable(PYPContext.getContext().getResources()
						.getDrawable(android.R.drawable.btn_star_big_on));
				star3.setImageDrawable(PYPContext.getContext().getResources()
						.getDrawable(android.R.drawable.btn_star_big_on));
				star4.setImageDrawable(PYPContext.getContext().getResources()
						.getDrawable(android.R.drawable.btn_star_big_off));
				star5.setImageDrawable(PYPContext.getContext().getResources()
						.getDrawable(android.R.drawable.btn_star_big_off));
				eventGrade = "3";
			}
		});
		
		star4.setOnClickListener( new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				star1.setImageDrawable(PYPContext.getContext().getResources()
						.getDrawable(android.R.drawable.btn_star_big_on));
				star2.setImageDrawable(PYPContext.getContext().getResources()
						.getDrawable(android.R.drawable.btn_star_big_on));
				star3.setImageDrawable(PYPContext.getContext().getResources()
						.getDrawable(android.R.drawable.btn_star_big_on));
				star4.setImageDrawable(PYPContext.getContext().getResources()
						.getDrawable(android.R.drawable.btn_star_big_on));
				star5.setImageDrawable(PYPContext.getContext().getResources()
						.getDrawable(android.R.drawable.btn_star_big_off));
				eventGrade = "4";
			}
		});
		
		star5.setOnClickListener( new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				star1.setImageDrawable(PYPContext.getContext().getResources()
						.getDrawable(android.R.drawable.btn_star_big_on));
				star2.setImageDrawable(PYPContext.getContext().getResources()
						.getDrawable(android.R.drawable.btn_star_big_on));
				star3.setImageDrawable(PYPContext.getContext().getResources()
						.getDrawable(android.R.drawable.btn_star_big_on));
				star4.setImageDrawable(PYPContext.getContext().getResources()
						.getDrawable(android.R.drawable.btn_star_big_on));
				star5.setImageDrawable(PYPContext.getContext().getResources()
						.getDrawable(android.R.drawable.btn_star_big_on));
				eventGrade = "5";
			}
		});
		
		final String[] data = IntentHelper.getActiveIntentParam(String[].class);
//		Drawable background = ((LinearLayout) findViewById(R.id.event_detail_backgorund))
//				.getBackground();
//		background.setAlpha(95);
		id = data[0];
		//AppTools.debug(data[1]);

		new GetEventDetails().execute(id);
		hideHeader(false);
	}

	@Override
	public void onResume() {
		super.onResume();

	}
	
	public void networkError(String error) {
		if (error.equals("Incomplete data")) {
			Popups.showPopup(Constants.IncompleatData);
		}
	}

	private class GetEventDetails extends AsyncTask<String, Void, Void> {

		JSONObject res;
		ProgressDialog mProgressDialog;

		@Override
		protected void onPostExecute(Void result) {
			mProgressDialog.dismiss();
			if (res != null) {
				try {
					if (res.has("error")) {
						// Error
						String error;
						error = res.getString("error");
						GetEventDetailActivity.this.networkError(error);
					}else{
						// Put the data in form
						eventNameField.setText(res.getString("name"));
						eventTypeField.setText(res.getString("type"));
						eventHoursField.setText(res.getString("start_time")+" - "+res.getString("end_time"));
						
						eventPriceField.setText(res.getString("price"));
						eventDescriptionField.setText(res.getString("description"));
						eventAgeAverageField.setText(res.getString("age_average"));
						eventFemaleRatioField.setText(res.getString("female_ratio"));
						eventSingleRatioField.setText(res.getString("single_ratio"));
						eventHeadcountField.setText(res.getString("headcount"));
						
						eventPlaceNameField.setText(res.getString("place_name"));
						eventPlaceDescriptionField.setText(res.getString("place_description"));
						eventPlaceAddressField.setText(res.getString("place_address"));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		@Override
		protected void onPreExecute() {
			mProgressDialog = ProgressDialog.show(GetEventDetailActivity.this,
					getString(R.string.app_name), getString(R.string.loading));
			AppTools.debug("Loading events");
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
				parameters.add(new BasicNameValuePair("id", params[0]));
				AppTools.debug("ID of the event: " + params[0]);
				res = srvCon.connect(ServerConnection.GET_EVENT_FULL_INFO,
						parameters);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
	}
	
	
	
	private class GradeRateEvent extends AsyncTask<String, Void, Void> {

		JSONObject res;
		ProgressDialog mProgressDialog;

		@Override
		protected void onPostExecute(Void result) {
			mProgressDialog.dismiss();
			if (res != null) {
				try {
					if (res.has("error")) {
						// Error
						String error;
						error = res.getString("error");
						GetEventDetailActivity.this.networkError(error);
					}else{
						// Disable button
						// TODO: already grade it
						checkInButton.setVisibility(View.GONE);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		@Override
		protected void onPreExecute() {
			mProgressDialog = ProgressDialog.show(GetEventDetailActivity.this,
					getString(R.string.app_name), getString(R.string.loading));
			AppTools.debug("Loading events");
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
				parameters.add(new BasicNameValuePair("stars", eventGrade));
				AppTools.debug("ID of the event: " + params[0]);
				res = srvCon.connect(ServerConnection.STAR,
						parameters);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
	}

}
