package eu.chitic.syp.gui.main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import eu.chitic.syp.R;
import eu.chitic.syp.entities.EventEnitity;
import eu.chitic.syp.gui.common.FragmentBaseActivity;
import eu.chitic.syp.gui.common.IntentHelper;
import eu.chitic.syp.gui.common.popup.Popups;
import eu.chitic.syp.gui.common.selector.ActionItem;
import eu.chitic.syp.gui.common.selector.QuickAction;
import eu.chitic.syp.gui.events.CreateEventActivity;
import eu.chitic.syp.gui.events.EventActivity;
import eu.chitic.syp.gui.events.ManagePersonalEvents;
import eu.chitic.syp.network.ServerConnection;
import eu.chitic.syp.tools.AppTools;
import eu.chitic.syp.tools.Constants;
import eu.chitic.syp.tools.PYPContext;
import eu.chitic.syp.tools.TerminalInfo;

public class MainActivity extends FragmentBaseActivity {
	private LinearLayout abstractView;
	private LinearLayout mainView;
	private TextView windowTitle;
	private ListView list;
	private ListView events_list;
	private PersonalEventsAdapter personalEventsadapter;
	private EventsAdapter eventsAdapter;
	private Location lastLocation;
	private Long lastRefresh = System.currentTimeMillis() / 1000;
	private ViewFlipper vf;
	private float lastX;

	private HashMap<String, EventEnitity> eventsLis = new HashMap<String, EventEnitity>();
	private HashMap<String, EventEnitity> tmpList = new HashMap<String, EventEnitity>();
	private HashMap<String, Marker> markersList = new HashMap<String, Marker>();
	private GoogleMap map;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState, Constants.MAIN_CONST);
		AppTools.info("on create MainActivity");
		initGraphicalInterface();
	}

	private void initGraphicalInterface() {
		// set layouts
		LayoutInflater mInflater = LayoutInflater.from(this);
		abstractView = (LinearLayout) findViewById(R.id.abstractLinearLayout);
		abstractView.setVisibility(LinearLayout.GONE);
		abstractView = (LinearLayout) findViewById(R.id.abstractLinearLayoutTop);
		abstractView.setVisibility(LinearLayout.VISIBLE);
		mainView = (LinearLayout) mInflater.inflate(R.layout.main_layout, null);
		abstractView.addView(mainView);
		createMenu();
		vf = (ViewFlipper) findViewById(R.id.view_flipper);
		vf.showNext();
		windowTitle = (TextView) findViewById(R.id.pageTitle);
		windowTitle.setText(R.string.NoTitle);

		map = ((SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.map)).getMap();
		ArrayList<String[]> data = new ArrayList<String[]>();
		data.add(new String[] { getString(R.string.Add_Event) });
		buildList(data);
		new CheckCompletionStatusEvent().execute();
		hideHeader(false);

	}

	ActionItem radiousItem;
	ActionItem intrestsItem;
	ActionItem hourItem;
	ActionItem prixItem;
	String filter_Radiou = String.valueOf(Constants.AREA_RADIUS);
	String filter_intrest = "";
	String filter_prix = "";
	String filter_time = "";

	public void createMenu() {

		radiousItem = new ActionItem(Constants.ID_radious,
				getString(R.string.radious_pop_filt), getResources()
						.getDrawable(R.drawable.checkbox_unchecked));
		intrestsItem = new ActionItem(Constants.ID_Intrests,
				getString(R.string.int_pop_filt), getResources().getDrawable(
						R.drawable.checkbox_unchecked));
		hourItem = new ActionItem(Constants.ID_hour,
				getString(R.string.hours_pop_filt), getResources().getDrawable(
						R.drawable.checkbox_unchecked));
		prixItem = new ActionItem(Constants.ID_prix,
				getString(R.string.prix_pop_filt), getResources().getDrawable(
						R.drawable.checkbox_unchecked));

		radiousItem.setSticky(true);
		intrestsItem.setSticky(true);
		hourItem.setSticky(true);
		prixItem.setSticky(true);
		radiousItem.image_id = R.drawable.checkbox_unchecked;
		intrestsItem.image_id = R.drawable.checkbox_unchecked;
		hourItem.image_id = R.drawable.checkbox_unchecked;
		prixItem.image_id = R.drawable.checkbox_unchecked;

		final QuickAction quickAction = new QuickAction(this,
				QuickAction.VERTICAL);

		// add action items into QuickAction
		quickAction.addActionItem(radiousItem);
		quickAction.addActionItem(intrestsItem);
		quickAction.addActionItem(hourItem);
		quickAction.addActionItem(prixItem);

		// Set listener for action item clicked
		final QuickAction.OnActionItemClickListener item_action = new QuickAction.OnActionItemClickListener() {
			@Override
			public void onItemClick(QuickAction source, int pos, int actionId) {
				final ActionItem actionItem = quickAction.getActionItem(pos);
				final QuickAction Source = source;
				final int Pos = pos;
				if (actionItem.image_id == R.drawable.checkbox_unchecked) {
					actionItem.image_id = R.drawable.checkbox_checked;
					actionItem.setIcon(getResources().getDrawable(
							R.drawable.checkbox_checked));
				} else {
					actionItem.setIcon(getResources().getDrawable(
							R.drawable.checkbox_unchecked));
					actionItem.image_id = R.drawable.checkbox_unchecked;
					switch (actionItem.getActionId()) {
					case Constants.ID_Intrests:
						actionItem.setTitle(getString(R.string.int_pop_filt));
						filter_intrest = "";
						break;
					case Constants.ID_hour:
						actionItem.setTitle(getString(R.string.hours_pop_filt));
						filter_time = "";
						break;
					case Constants.ID_prix:
						actionItem.setTitle(getString(R.string.prix_pop_filt));
						filter_prix = "";
						break;
					default:
						actionItem
								.setTitle(getString(R.string.radious_pop_filt));
						filter_Radiou = String.valueOf(Constants.AREA_RADIUS);
						break;
					}
					source.reDraw(pos);

				}
				AppTools.debug(actionItem.getActionId() + " ");
				if (actionItem.getActionId() != Constants.ID_Intrests
						&& actionItem.image_id == R.drawable.checkbox_checked) {
					// get
					// prompts.xml
					// view
					LayoutInflater li = LayoutInflater
							.from(source.getContext());
					View promptsView = li.inflate(R.layout.prompts, null);
					AppTools.error(this.getClass().toString());
					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
							source.getContext());

					// set prompts.xml to alertdialog builder
					alertDialogBuilder.setView(promptsView);

					final EditText userInput = (EditText) promptsView
							.findViewById(R.id.editTextDialogUserInput);
					final EditText minutesInput = (EditText) promptsView
							.findViewById(R.id.StartEventMinute_popup);
					final EditText hoursInput = (EditText) promptsView
							.findViewById(R.id.StartEventHour_popup);
					final LinearLayout layout = (LinearLayout) promptsView
							.findViewById(R.id.hours_popup);
					final TextView textExplaner = (TextView) promptsView
							.findViewById(R.id.textView1_promp);
					switch (actionItem.getActionId()) {
					case Constants.ID_Intrests:
						textExplaner.setText(R.string.int_pop_filt);
						break;
					case Constants.ID_hour:
						layout.setVisibility(View.VISIBLE);
						userInput.setVisibility(View.GONE);
						hoursInput.requestFocus();
						textExplaner.setText(R.string.hours_pop_filt);
						break;
					case Constants.ID_prix:
						textExplaner.setText(R.string.prix_pop_filt);
						break;
					default:
						textExplaner.setText(R.string.radious_pop_filt);
						break;
					}
					// set dialog message
					alertDialogBuilder
							.setCancelable(false)
							.setPositiveButton(
									getString(R.string.alert_dialog_ok),
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog, int id) {
											// get user input and set it
											// to
											// result
											// edit text
											AppTools.debug(userInput.getText()
													.toString());
											switch (actionItem.getActionId()) {
											case Constants.ID_hour:
												actionItem.setTitle(actionItem
														.getTitle()
														+ " - "
														+ hoursInput.getText()
																.toString()
														+ ":"
														+ minutesInput
																.getText()
																.toString()
														+ " h");
												filter_time = hoursInput
														.getText().toString()
														+ ":"
														+ minutesInput
																.getText()
																.toString();
												break;
											case Constants.ID_prix:
												actionItem.setTitle(actionItem
														.getTitle()
														+ " - "
														+ userInput.getText()
																.toString()
														+ " euro");
												filter_prix = userInput
														.getText().toString();
												break;
											default:
												actionItem.setTitle(actionItem
														.getTitle()
														+ " - "
														+ userInput.getText()
																.toString()
														+ " m");
												filter_Radiou = userInput
														.getText().toString();
												break;
											}
											Source.reDraw(Pos);

										}
									})
							.setNegativeButton(getString(R.string.cancel),
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog, int id) {
											actionItem
													.setIcon(getResources()
															.getDrawable(
																	R.drawable.checkbox_unchecked));
											actionItem.image_id = R.drawable.checkbox_unchecked;
											Source.reDraw(Pos);
											dialog.cancel();

										}
									});

					// create alert dialog
					AlertDialog alertDialog = alertDialogBuilder.create();
					alertDialog.show();
				}
				if (actionItem.getActionId() == Constants.ID_Intrests
						&& actionItem.image_id == R.drawable.checkbox_checked) {
					filter_intrest = "true";
				}

			}
		};
		quickAction.setOnActionItemClickListener(item_action);

		// set listnener for on dismiss event, this listener will be called only
		// if QuickAction dialog was dismissed
		// by clicking the area outside the dialog.
		quickAction.setOnDismissListener(new QuickAction.OnDismissListener() {
			@Override
			public void onDismiss() {
				AppTools.debug(filter_intrest + " " + filter_prix + " "
						+ filter_time + " " + filter_Radiou);
				lastLocation = null;
				show_force = true;
				new GetEvents().execute();

			}
		});

		ImageView filter = (ImageView) findViewById(R.id.abstract_header_picto_search_left);
		filter.setVisibility(View.VISIBLE);
		filter.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AppTools.debug("Showing search");
				quickAction.show(v);
			}
		});

	}

	// Added Maps //
	private void buildMap() {

		Location l = TerminalInfo.getPosition();
		LatLng device = new LatLng(l.getLatitude(), l.getLongitude());

		markersList.clear();
		int zoomLevel = 15;
		try {
			double scale = Integer.parseInt(filter_Radiou) / 500;
			zoomLevel = (int) (16 - Math.log(scale) / Math.log(2)) + 1;
		} catch (Exception e) {
			zoomLevel = 15;
		}
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(device, zoomLevel));
		Marker m = map
				.addMarker(new MarkerOptions()
						.position(device)
						.title("Device position")
						.icon(BitmapDescriptorFactory
								.fromResource(R.drawable.ic_maps_indicator_current_position)));
		markersList.put("position", m);
		map.setOnMapClickListener(new OnMapClickListener() {

			@Override
			public void onMapClick(LatLng arg0) {
				Projection projection = map.getProjection();
				Point p = projection.toScreenLocation(arg0);
				Display display = getWindowManager().getDefaultDisplay();

				if (p.x > display.getWidth() - 100) {
					vf.setInAnimation(MainActivity.this, R.anim.in_from_right);
					vf.setOutAnimation(MainActivity.this, R.anim.out_to_left);
					lastLocation = null;
					ImageView filter = (ImageView) findViewById(R.id.abstract_header_picto_search_left);
					filter.setVisibility(View.VISIBLE);
					new GetEvents().execute();
					vf.showNext();
				}

			}
		});
		map.setOnMarkerClickListener(new OnMarkerClickListener() {

			@Override
			public boolean onMarkerClick(Marker arg0) {

				Enumeration<String> strEnum = Collections
						.enumeration(markersList.keySet());

				while (strEnum.hasMoreElements()) {
					String id = strEnum.nextElement();
					Marker e = markersList.get(id);
					if (e.equals(arg0)) {
						if (id.equals("position"))
							return false;
						String[] tagData = new String[] { id };
						IntentHelper.openNewActivity(EventActivity.class,
								tagData, false);
						break;
					}
				}
				return false;
			}
		});

		Enumeration<String> strEnum = Collections.enumeration(eventsLis
				.keySet());

		while (strEnum.hasMoreElements()) {
			EventEnitity e = eventsLis.get(strEnum.nextElement());
			if (e != null) {
				m = map.addMarker(new MarkerOptions()
						.position(new LatLng(e.getLat(), e.getLon()))
						.title(e.getName())
						.snippet(e.getDescription())
						.icon(BitmapDescriptorFactory
								.fromResource(R.drawable.marker)));
				markersList.put(e.getId(), m);
			}
		}

	}

	@Override
	public void onResume() {
		super.onResume();
		// check if logged in
		if (!checkLoggedIn()) {
			return;
		}
		final Handler handler = new Handler();
		Timer timer = new Timer();
		TimerTask doAsynchronousTask = new TimerTask() {
			@Override
			public void run() {
				handler.post(new Runnable() {
					public void run() {
						try {
							GetEvents ev = new GetEvents();
							ev.execute();
						} catch (Exception e) {
							AppTools.error(e.getMessage());
						}
					}
				});
			}
		};
		timer.schedule(doAsynchronousTask, 0, 20000);
		GetEvents ev = new GetEvents();
		ev.execute();
		new GetPersonalEvents().execute();
	}

	@Override
	public boolean onTouchEvent(MotionEvent touchevent) {
		switch (touchevent.getAction()) {
		case MotionEvent.ACTION_DOWN: {
			lastX = touchevent.getX();
			break;
		}

		case MotionEvent.ACTION_UP: {

			AppTools.error(vf.getDisplayedChild() + " ");
			float currentX = touchevent.getX();
			AppTools.debug("Old:" + lastX + "New:" + currentX);

			ImageView filter = (ImageView) findViewById(R.id.abstract_header_picto_search_left);
			if (lastX < currentX && lastX != 0 && currentX - lastX > 100) {

				if (vf.getDisplayedChild() == 0)
					break;
				vf.setInAnimation(this, R.anim.in_from_left);
				vf.setOutAnimation(this, R.anim.out_to_right);

				if (vf.getDisplayedChild() == 1) {
					filter.setVisibility(View.INVISIBLE);
					buildMap();
				}
				if (vf.getDisplayedChild() == 2) {
					filter.setVisibility(View.VISIBLE);

					lastLocation = null;
					new GetEvents().execute();
				}

				vf.showPrevious();

			}

			if (lastX > currentX && lastX != 0 && lastX - currentX > 100) {
				if (vf.getDisplayedChild() == 2)
					break;
				vf.setInAnimation(this, R.anim.in_from_right);
				vf.setOutAnimation(this, R.anim.out_to_left);
				if (vf.getDisplayedChild() == 0) {
					filter.setVisibility(View.VISIBLE);
					lastLocation = null;
					new GetEvents().execute();
				}
				if (vf.getDisplayedChild() == 1) {
					filter.setVisibility(View.INVISIBLE);
					new GetPersonalEvents().execute();
				}

				vf.showNext();
			}
			lastX = 0;
			break;
		}
		}
		return false;
	}

	@Override
	public void onBackPressed() {
		if (vf.getDisplayedChild() == 0) {
			vf.setInAnimation(this, R.anim.in_from_right);
			vf.setOutAnimation(this, R.anim.out_to_left);
			ImageView filter = (ImageView) findViewById(R.id.abstract_header_picto_search_left);

			filter.setVisibility(View.VISIBLE);
			lastLocation = null;
			new GetEvents().execute();
			vf.showNext();

			return;
		}
		super.onBackPressed();
	}

	private void buildList(ArrayList<String[]> data) {

		list = (ListView) findViewById(R.id.personal_events_list);

		// Getting adapter by passing xml data ArrayList
		personalEventsadapter = new PersonalEventsAdapter(this, data);
		if (personalEventsadapter == null) {
			AppTools.info("Adapter is null");
			return;
		}
		list.setAdapter(personalEventsadapter);

		// Click event for single list row
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (arg2 == 0) {
					IntentHelper.openNewActivity(CreateEventActivity.class,
							null, false);
				} else {
					String[] tagData = (String[]) arg1.getTag();
					IntentHelper.openNewActivity(ManagePersonalEvents.class,
							tagData, false);

				}
			}
		});

		list.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {

				return MainActivity.this.onTouchEvent(event);
			}
		});
	}

	private class GetPersonalEvents extends AsyncTask<Void, Void, Void> {

		JSONObject res;

		@Override
		protected void onPostExecute(Void result) {
			if (res != null) {
				try {
					JSONArray array = res.getJSONArray("list");
					ArrayList<String[]> data = new ArrayList<String[]>();
					data.add(new String[] { getString(R.string.Add_Event) });
					for (int i = 0; i < array.length(); i++) {
						JSONObject obj = array.getJSONObject(i);
						double lon = Double.parseDouble(obj.getString("lon"));
						double lat = Double.parseDouble(obj.getString("lat"));
						AppTools.error(lon + " " + lat);
						data.add(new String[] {
								obj.getString("name"),
								obj.getString("start_time") + " - "
										+ obj.getString("end_time"),
								AppTools.checkInArea(lon, lat,
										Constants.AREA_RADIUS).toString(),
								obj.getString("id"),
								obj.getString("description") });

					}
					AppTools.debug("Number of personal events:" + data.size());
					MainActivity.this.buildList(data);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		@Override
		protected void onPreExecute() {
			AppTools.debug("Loading persoanl events");
		}

		@Override
		protected Void doInBackground(Void... params) {
			// Send request to server for login
			ServerConnection srvCon = ServerConnection.GetServerConnection();
			try {
				List<NameValuePair> parameters = new ArrayList<NameValuePair>();
				SharedPreferences settings = PYPContext.getContext()
						.getSharedPreferences(AppTools.PREFS_NAME, 0);
				parameters.add(new BasicNameValuePair("auth_token", settings
						.getString("auth_token", "")));
				res = srvCon.connect(ServerConnection.GET_PERSONAL_EVENTS,
						parameters);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
	}

	private class CheckIn extends AsyncTask<EventEnitity, Void, Void> {

		JSONObject res;
		EventEnitity entity;

		@Override
		protected void onPostExecute(Void result) {
			if (res != null) {
				try {
					entity.setIsCheckedIn(true);
					Bitmap icon = BitmapFactory.decodeResource(PYPContext
							.getContext().getResources(),
							R.drawable.logo_notification);
					NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
							MainActivity.this)
							.setSmallIcon(R.drawable.logo)
							.setContentTitle("Share your party")
							.setContentText(
									getString(R.string.checked_in)
											+ " "
											+ entity.getName()
											+ " "
											+ getString(R.string.checked_in_opinion))
							.setAutoCancel(true).setLargeIcon(icon);
					// Creates an explicit intent for an Activity in your app
					Intent resultIntent = new Intent(MainActivity.this,
							EventActivity.class);
					resultIntent.putExtra(Constants.PARAMNAME,
							new String[] { entity.getId() });
					// The stack builder object will contain an artificial back
					// stack for the
					// started Activity.
					// This ensures that navigating backward from the Activity
					// leads out of
					// your application to the Home screen.
					TaskStackBuilder stackBuilder = TaskStackBuilder
							.create(MainActivity.this);
					// Adds the back stack for the Intent (but not the Intent
					// itself)
					stackBuilder.addParentStack(EventActivity.class);
					// Adds the Intent that starts the Activity to the top of
					// the stack
					stackBuilder.addNextIntent(resultIntent);
					PendingIntent resultPendingIntent = stackBuilder
							.getPendingIntent(0,
									PendingIntent.FLAG_UPDATE_CURRENT);
					mBuilder.setContentIntent(resultPendingIntent);
					NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
					int mId = 1;
					// mId allows you to update the notification later on.
					mNotificationManager.notify(mId, mBuilder.build());

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		@Override
		protected void onPreExecute() {
			AppTools.debug("Checking in..");

		}

		@Override
		protected Void doInBackground(EventEnitity... params) {
			// Send request to server for login
			entity = params[0];
			ServerConnection srvCon = ServerConnection.GetServerConnection();
			try {
				List<NameValuePair> parameters = new ArrayList<NameValuePair>();
				SharedPreferences settings = PYPContext.getContext()
						.getSharedPreferences(AppTools.PREFS_NAME, 0);
				parameters.add(new BasicNameValuePair("auth_token", settings
						.getString("auth_token", "")));
				parameters.add(new BasicNameValuePair("event_id", params[0]
						.getId()));
				res = srvCon.connect(ServerConnection.CHECK_IN, parameters);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
	}

	private class CheckOut extends AsyncTask<EventEnitity, Void, Void> {

		JSONObject res;
		EventEnitity entity;

		@Override
		protected void onPostExecute(Void result) {
			if (res != null) {
				try {
					entity.setIsCheckedIn(false);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		@Override
		protected void onPreExecute() {
			AppTools.debug("Checking out..");

		}

		@Override
		protected Void doInBackground(EventEnitity... params) {
			// Send request to server for login
			entity = params[0];
			ServerConnection srvCon = ServerConnection.GetServerConnection();
			try {
				List<NameValuePair> parameters = new ArrayList<NameValuePair>();
				SharedPreferences settings = PYPContext.getContext()
						.getSharedPreferences(AppTools.PREFS_NAME, 0);
				parameters.add(new BasicNameValuePair("auth_token", settings
						.getString("auth_token", "")));
				parameters.add(new BasicNameValuePair("event_id", params[0]
						.getId()));
				res = srvCon.connect(ServerConnection.CHECK_OUT, parameters);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
	}

	private void buildListEvents(ArrayList<String[]> data) {

		events_list = (ListView) findViewById(R.id.events_list);

		// Getting adapter by passing xml data ArrayList
		eventsAdapter = new EventsAdapter(this, data);
		if (eventsAdapter == null) {
			AppTools.info("Adapter is null");
			return;
		}
		events_list.setAdapter(eventsAdapter);
		events_list.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {

				return MainActivity.this.onTouchEvent(event);
			}
		});
		// Click event for single list row
		events_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				String[] tagData = (String[]) arg1.getTag();
				IntentHelper.openNewActivity(EventActivity.class, tagData,
						false);
			}
		});
	}

	private Boolean show_force = false;

	private class GetEvents extends AsyncTask<Void, Void, Void> {

		JSONObject res;
		ProgressDialog mProgressDialog;

		@Override
		protected void onPostExecute(Void result) {
			if (mProgressDialog != null && mProgressDialog.isShowing()) {
				show_force = false;
				mProgressDialog.dismiss();
				if (AppTools.isFirstLaunch()) {
					LayoutInflater inflater = getLayoutInflater();
					View layout = inflater.inflate(R.layout.tutorial_layout,
							(ViewGroup) findViewById(R.id.toast_layout_root));

					Toast toast = new Toast(getApplicationContext());
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.setDuration(Toast.LENGTH_LONG);
					toast.setView(layout);
					toast.show();

					AppTools.setFirstLaunch(false);
				}

			}

			if (res != null) {
				try {
					JSONArray array = res.getJSONArray("list");
					tmpList = new HashMap<String, EventEnitity>();

					ArrayList<String[]> data = new ArrayList<String[]>();
					for (int i = 0; i < array.length(); i++) {
						JSONObject obj = array.getJSONObject(i);
						double lon = Double.parseDouble(obj.getString("lon"));
						double lat = Double.parseDouble(obj.getString("lat"));
						tmpList.put(
								obj.getString("id"),
								new EventEnitity(obj.getString("id"), lon, lat,
										obj.getString("name"), obj
												.getString("description")));
						data.add(new String[] {
								obj.getString("name"),
								obj.getString("start_time") + " - "
										+ obj.getString("end_time"),
								AppTools.checkInArea(lon, lat,
										Constants.AREA_RADIUS).toString(),
								obj.getString("id"),
								obj.getString("type"),
								obj.getString("start_time") + " - "
										+ obj.getString("end_time"),
								obj.getString("id") });
					}
					Enumeration<String> strEnum = Collections
							.enumeration(eventsLis.keySet());

					while (strEnum.hasMoreElements()) {
						EventEnitity ev = eventsLis.get(strEnum.nextElement());
						if (tmpList.get(ev.getId()) == null) {
							eventsLis.remove(ev);
						}
					}
					strEnum = Collections.enumeration(tmpList.keySet());

					while (strEnum.hasMoreElements()) {

						EventEnitity ev = tmpList.get(strEnum.nextElement());
						AppTools.error(ev.getId());
						if (eventsLis.get(ev.getId()) == null) {
							eventsLis.put(ev.getId(), ev);
							Bitmap icon = BitmapFactory.decodeResource(
									PYPContext.getContext().getResources(),
									R.drawable.logo_notification);
							NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
									MainActivity.this)
									.setSmallIcon(R.drawable.logo)
									.setContentTitle("Share your party")
									.setContentText(
											getString(R.string.new_event) + " "
													+ ev.getName())
									.setAutoCancel(true).setLargeIcon(icon);
							// Creates an explicit intent for an Activity in
							// your app
							Intent resultIntent = new Intent(MainActivity.this,
									EventActivity.class);
							resultIntent.putExtra(Constants.PARAMNAME,
									new String[] { ev.getId() });
							// The stack builder object will contain an
							// artificial back
							// stack for the
							// started Activity.
							// This ensures that navigating backward from the
							// Activity
							// leads out of
							// your application to the Home screen.
							TaskStackBuilder stackBuilder = TaskStackBuilder
									.create(MainActivity.this);
							// Adds the back stack for the Intent (but not the
							// Intent
							// itself)
							stackBuilder.addParentStack(EventActivity.class);
							// Adds the Intent that starts the Activity to the
							// top of
							// the stack
							stackBuilder.addNextIntent(resultIntent);
							PendingIntent resultPendingIntent = stackBuilder
									.getPendingIntent(0,
											PendingIntent.FLAG_UPDATE_CURRENT);
							mBuilder.setContentIntent(resultPendingIntent);
							NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
							int mId = 2;
							// mId allows you to update the notification later
							// on.
							mNotificationManager.notify(mId, mBuilder.build());
						}
					}
					AppTools.debug("Number of events:" + data.size());
					MainActivity.this.buildListEvents(data);
					res = null;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}

		@Override
		protected void onPreExecute() {
			if (events_list == null || show_force)
				mProgressDialog = ProgressDialog.show(MainActivity.this,
						getString(R.string.app_name),
						getString(R.string.loading));
			AppTools.debug("Loading events");
		}

		@Override
		protected Void doInBackground(Void... params) {
			Long currentTimeStamp = System.currentTimeMillis() / 1000;
			// Check for check_in - check_out
			Enumeration<String> strEnum = Collections.enumeration(eventsLis
					.keySet());

			while (strEnum.hasMoreElements()) {
				EventEnitity e = eventsLis.get(strEnum.nextElement());
				if (AppTools.checkInArea(e.getLon(), e.getLat(),
						Constants.BAR_RADIOUS) && !e.getIsCheckedIn()) {
					new CheckIn().execute(e);
				}
				if (!AppTools.checkInArea(e.getLon(), e.getLat(),
						Constants.BAR_RADIOUS) && e.getIsCheckedIn()) {
					new CheckOut().execute(e);
				}
			}
			// Check barlist
			if (lastLocation == null
					|| currentTimeStamp - lastRefresh > 60 * 5
					|| AppTools.checkInArea(lastLocation.getLongitude(),
							lastLocation.getLatitude(), Constants.AREA_RADIUS) == false) {
				lastRefresh = currentTimeStamp;
				lastLocation = TerminalInfo.getPosition();
				ServerConnection srvCon = ServerConnection
						.GetServerConnection();
				try {

					List<NameValuePair> parameters = new ArrayList<NameValuePair>();
					SharedPreferences settings = PYPContext.getContext()
							.getSharedPreferences(AppTools.PREFS_NAME, 0);
					parameters.add(new BasicNameValuePair("radius",
							filter_Radiou));
					parameters.add(new BasicNameValuePair("intrest",
							filter_intrest));
					parameters.add(new BasicNameValuePair("prix", filter_prix));
					parameters.add(new BasicNameValuePair("time", filter_time));

					parameters.add(new BasicNameValuePair("latitude", String
							.valueOf(lastLocation.getLatitude())));
					parameters.add(new BasicNameValuePair("longitude", String
							.valueOf(lastLocation.getLongitude())));
					parameters.add(new BasicNameValuePair("auth_token",
							settings.getString("auth_token", "")));
					res = srvCon.connect(ServerConnection.GET_EVENTS,
							parameters);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return null;
		}
	}

	public void networkError(String error) {
		if (error.equals("No data")) {
			Popups.showPopup(Constants.IncompleatData);
		}
	}

	private class CheckCompletionStatusEvent extends
			AsyncTask<String, Void, Void> {

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
						MainActivity.this.networkError(error);
					} else {

					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		@Override
		protected void onPreExecute() {
			mProgressDialog = ProgressDialog.show(MainActivity.this,
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
				res = srvCon.connect(ServerConnection.CHECK_COMPLETION_STATUS,
						parameters);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
	}

}
