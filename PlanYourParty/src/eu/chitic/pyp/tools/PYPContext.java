package eu.chitic.pyp.tools;

import java.util.logging.Level;

import android.content.Context;
import eu.chitic.pyp.gui.common.BaseActivity;
import eu.chitic.pyp.gui.common.FragmentBaseActivity;

public abstract class PYPContext {

	private static Context context = null;
	private static BaseActivity activeActivity = null;
	private static FragmentBaseActivity fragmentActiveActivity = null;

	/**
	 * Sets the context for the rest of the application. <b>The first thing to
	 * do when the app starts!</b>
	 * 
	 * @param context
	 *            the application's context to set
	 */
	public static void setContext(Context context) {

		if (PYPContext.context == null) {
			AssertUtils.notNull(context, "context is null");
			// Log.i(AppTools.TAG, "Application's Context has been set");
			PYPContext.context = context;
		}
	}

	/**
	 * @return the application's context
	 */
	public static Context getContext() {

		return context;
	}

	/**
	 * @param activity
	 *            the cureent active activity to set
	 */
	public static void setActiveActivity(BaseActivity activity) {
		AssertUtils.notNull(activity, "activity is null");
		if (activeActivity != activity) {
			AppTools.log("Application's active activity has been set", Level.INFO);
			PYPContext.activeActivity = activity;
		}
	}
	
	/**
	 * @param activity
	 *            the cureent active activity to set
	 */
	public static void setActiveActivity(FragmentBaseActivity activity) {
		AssertUtils.notNull(activity, "activity is null");
		if (fragmentActiveActivity != activity) {
			AppTools.log("Application's active activity has been set", Level.INFO);
			PYPContext.fragmentActiveActivity = activity;
		}
	}


	/**
	 * @return the activity
	 */
	public static BaseActivity getActiveActivity() {
		return activeActivity;
	}
	
	/**
	 * @return the activity
	 */
	public static FragmentBaseActivity getFragmentActiveActivity() {
		return fragmentActiveActivity;
	}

	/**
	 * Shortcut for {@link Context#getString(int)}
	 * 
	 * @param resId
	 * @return
	 * @see Context#getString(int)
	 */
	public static String getStringResource(int resId) {
		return context.getString(resId);
	}

	/**
	 * Shortcut for {@link Resources#getBoolean(int)}
	 * 
	 * @param resId
	 * @return
	 * @see Resources#getBoolean(int)
	 */
	public static boolean getBooleanResource(int resId) {
		return context.getResources().getBoolean(resId);
	}

	/**
	 * Shortcut for {@link Resources#getInteger(int)}
	 * 
	 * @param resId
	 * @return
	 * @see Resources#getInteger(int)
	 */
	public static int getIntegerResource(int resId) {
		return context.getResources().getInteger(resId);
	}

}
