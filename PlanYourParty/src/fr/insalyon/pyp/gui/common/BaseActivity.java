package fr.insalyon.pyp.gui.common;

import java.util.List;
import java.util.logging.Level;

import fr.insalyon.pyp.R;

import fr.insalyon.pyp.gui.common.menu.CustomMenu.OnMenuItemSelectedListener;
import fr.insalyon.pyp.gui.common.menu.CustomMenuHelper;
import fr.insalyon.pyp.gui.common.menu.CustomMenuItem;

import fr.insalyon.pyp.tools.AppTools;
import fr.insalyon.pyp.tools.PYPContext;
import fr.insalyon.pyp.tools.Constants;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * This class acts as a base class to all the activities in the PYP project
 */
public class BaseActivity extends Activity implements OnMenuItemSelectedListener {

	protected static final int ABSTRACT_CONST = 1000;

	private static final String PAUSED_STATE = "PAUSED_STATE";
	private static final String STARTING_ACTIVITY_STATE = "STARTING_ACTIVITY_STATE";

	protected int ACTIVITY_CONST = 1000;
	protected boolean paused;
	protected boolean startingActivity;
	protected List<AsyncTask<?, ?, ?>> asyncTasks;
	protected boolean isMenuActive = false;
	protected CustomMenuHelper myMenu;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		onCreate(savedInstanceState, ABSTRACT_CONST);
	}

	public void onCreate(Bundle savedInstanceState, int activityId) {
		super.onCreate(savedInstanceState);

		// set activity ID
		ACTIVITY_CONST = activityId;

		// sets the context for the rest of the application.
		PYPContext.setContext(this);

		
		AppTools.setToExit(false);

		
		AppTools.log(this.getClass().getSimpleName() + " is creating", Level.INFO);
		

		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.abstract_activity);

		// restoring the state
		if (savedInstanceState != null) {
			paused = savedInstanceState.getBoolean(PAUSED_STATE, false);
			startingActivity = savedInstanceState.getBoolean(STARTING_ACTIVITY_STATE, false);

			AppTools.log(this.getClass().getSimpleName() + " is load from saved state with variables:", Level.INFO);

			AppTools.log(this.getClass().getSimpleName() + " : paused = " + paused, Level.INFO);

			AppTools.log(this.getClass().getSimpleName() + " : startingActivity = " + startingActivity, Level.INFO);
		} else {
			paused = false;
			startingActivity = false;
		}

		// set the active activity
		PYPContext.setActiveActivity(this);
		myMenu = new CustomMenuHelper(1, this);

	}

	/**
	 * Hide or show the header bar
	 * 
	 * @param hide
	 */
	public void hideHeader(boolean hide) {
		RelativeLayout headerView = (RelativeLayout) findViewById(R.id.abstract_header_layout);
		if (hide) {
			headerView.setVisibility(View.GONE);
		} else {
			headerView.setVisibility(View.VISIBLE);
		}

	}

	/**
	 *  Hide or show the home button in the header bar
	 * @param show
	 */
	public void showHomeButton(boolean show) {
		ImageView headerView = (ImageView) findViewById(R.id.abstract_header_picto_left);
		if (!show) {
			headerView.setVisibility(View.GONE);
		} else {
			headerView.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
// home button
				}

			});
			headerView.setVisibility(View.VISIBLE);
		}
	}


	@Override
	public void onRestart() {
		super.onRestart();

		AppTools.log(this.getClass().getSimpleName() + " is restarting", Level.INFO);

	}

	@Override
	protected void onStart() {
		super.onStart();

		AppTools.log(this.getClass().getSimpleName() + " is starting", Level.INFO);
		// set the active activity
		PYPContext.setActiveActivity(this);

		// Check rights
	}

	@Override
	protected void onResume() {
		if (AppTools.isToExit())
			finish();
		super.onResume();

		AppTools.log(this.getClass().getSimpleName() + " is resuming", Level.INFO);

		startingActivity = false;
		if (paused) {
			AppTools.log(this.getClass().getSimpleName() + " has been resumed from a paused state", Level.INFO);
			paused = false;
			startupProcess();
		}
	}

	protected void onResume(boolean forceExitOverrided) {
		if (AppTools.isToExit())
			finish();
		super.onResume();

		AppTools.log(this.getClass().getSimpleName() + " is resuming", Level.INFO);

		startingActivity = false;
		if (paused) {
			AppTools.log(this.getClass().getSimpleName() + " has been resumed from a paused state", Level.INFO);
			paused = false;
			startupProcess();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (!startingActivity) {
			paused = true;

			AppTools.log(this.getClass().getSimpleName() + " is on pause", Level.INFO);
		}

		outState.putBoolean(PAUSED_STATE, paused);
		outState.putBoolean(STARTING_ACTIVITY_STATE, startingActivity);

		AppTools.log(this.getClass().getSimpleName() + " : paused = " + paused, Level.INFO);

		AppTools.log(this.getClass().getSimpleName() + " : startingActivity = " + startingActivity, Level.INFO);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);

		AppTools.log(this.getClass().getSimpleName() + " is restoring bundle", Level.INFO);

	}

	@Override
	protected void onStop() {
		super.onStop();

		AppTools.log(this.getClass().getSimpleName() + " is stopping...", Level.INFO);
		cancelAllRunningTasks();

		// NetworkNotificationUtils.hideNetworkUsageNotification();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		AppTools.log(this.getClass().getSimpleName() + " is dying...", Level.INFO);
		// NetworkNotificationUtils.hideNetworkUsageNotification();

		// Clean all drawable to avoir Out Of Memory
		System.gc();

	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		AppTools.log("Keyboard configuration changed.", Level.FINE);
	}

	/**
	 * Launch the startup process which includes : check appVersion / get IS /
	 * send or not Statistics / Enrollement
	 */
	protected void startupProcess() {
		AppTools.debug(this.getClass().getSimpleName() + " is launching startup process (Check appVersion / get IS / Statistics / Enrollment)");
	}

	/**
	 * Toggle our menu on user pressing the menu key.
	 */
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			myMenu.doMenu();
			return true; // always eat it!
		}
		return super.onKeyDown(keyCode, event);
	}

	public void MenuItemSelectedEvent(CustomMenuItem selection) {
	
	}

	protected void cancelAllRunningTasks() {
		if (asyncTasks != null) {
			for (AsyncTask<?, ?, ?> task : asyncTasks) {

				AppTools.log("Force exiting task : " + task + " (status=" + task.getStatus() + ")", Level.INFO);
				task.cancel(true);
			}
			asyncTasks.clear();
		}
	}


	public void setGraphicalInterface() {
		// this will be overrided by childs
	}

	@Override
	public void startActivity(Intent intent) {
		startingActivity = true;

		AppTools.log(this.getClass().getSimpleName() + " starts an activity", Level.INFO);
		super.startActivity(intent);
	}

	public void setMainLayout(int layoutId) {
		LayoutInflater mInflater = LayoutInflater.from(this);
		LinearLayout abstractView = (LinearLayout) findViewById(R.id.abstractLinearLayout);
		LinearLayout accountView = (LinearLayout) mInflater.inflate(layoutId, null);
		abstractView.addView(accountView);
	}

	/**
	 * Shortcut to set the layout
	 * 
	 * @param layoutId
	 * @param isRelativeLayoutOrFrameLayout
	 *            : if true : Relative else Frame
	 */
	public void setMainLayout(int layoutId, boolean isRelativeLayoutOrFrameLayout) {
		LayoutInflater mInflater = LayoutInflater.from(this);
		LinearLayout abstractView = (LinearLayout) findViewById(R.id.abstractLinearLayout);
		if (isRelativeLayoutOrFrameLayout) {
			RelativeLayout accountView = (RelativeLayout) mInflater.inflate(layoutId, null);
			abstractView.addView(accountView);
		} else {
			FrameLayout accountView = (FrameLayout) mInflater.inflate(layoutId, null);
			abstractView.addView(accountView);
		}

	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
		AppTools.info("*** LOW MEMORY ***");
		printMemoryInfo();
	}

	public void printMemoryInfo() {
		ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		ActivityManager.MemoryInfo mInfo = new ActivityManager.MemoryInfo();
		activityManager.getMemoryInfo(mInfo);
		AppTools.info(" minfo.availMem " + mInfo.availMem);
		AppTools.info(" minfo.lowMemory " + mInfo.lowMemory);
		AppTools.info(" minfo.threshold " + mInfo.threshold);
		AppTools.info(" maxMemory " + Runtime.getRuntime().maxMemory());
		AppTools.info(" totalMem " + Runtime.getRuntime().totalMemory());
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}



}
