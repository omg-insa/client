package fr.insalyon.pyp.tools;

import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.content.SharedPreferences;
import fr.insalyon.pyp.R;
import fr.insalyon.pyp.tools.logger.AbstractLogger;
import fr.insalyon.pyp.tools.logger.ConsolLog;
import fr.insalyon.pyp.tools.logger.FileLog;

/**
 * @author A510944
 * 
 */
public abstract class AppTools {

	public static final String PREFS_NAME = "PYP";
	private static final String V1_FIRST_LAUNCH = "FistLaunchV1";
	private static final String MAIL_ADDRESS = "mail_address";
	private static final String PASSWORD = "password";
	private static final String IS_BUGED = "bug_camera";
	private static final String IS_ERROR = "errorLabel";
	private static final String ERROR_CODE = "errorCode";
	private static AbstractLogger myLog;

	/**
	 * @param state
	 *            true to exit the aplication
	 */
	public static void setToExit(Boolean state) {
		SharedPreferences settings = PYPContext.getContext().getSharedPreferences(PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putBoolean("exit", state);
		editor.commit();
	}

	public static boolean isEmailValid(String email) {
	    boolean isValid = false;

	    String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
	    CharSequence inputStr = email;
	    Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
	    Matcher matcher = pattern.matcher(inputStr);
	    if (matcher.matches()) {
	        isValid = true;
	    }
	    return isValid;
	}
	
	/**
	 * @return the stat of the appilcation: true for exiting
	 */
	public static boolean isToExit() {
		SharedPreferences settings = PYPContext.getContext().getSharedPreferences(PREFS_NAME, 0);
		return settings.getBoolean("exit", true);
	}

	public static void info(String message) {
		log(message, Level.INFO);
	}

	public static void debug(String message) {
		log(message, Level.FINEST);
	}

	public static void warn(String message) {
		log(message, Level.WARNING);
	}

	public static void error(String message) {
		log(message, Level.SEVERE);
	}

	public static void logTest(String message) {
		log("[TEST]" + message, Level.INFO);
	}

	/**
	 * logger for logcat or file depending on the config file
	 * 
	 * @param message
	 *            to log
	 * @param level
	 *            of the gravity of the message
	 */
	public static void log(String message, Level level) {
		Context context = PYPContext.getContext();
		if (myLog == null && context != null) {
			if (context.getString(R.string.prop_logging).equals("console")) {
				// Console logcat logging
				myLog = new ConsolLog();
			} else if (context.getString(R.string.prop_logging).equals("file")) {
				// File logging
				myLog = new FileLog();
			} else if ((context.getString(R.string.prop_logging).equals("no"))) {
				// no logging (production configuration)
				return;
			}
		}
		String messageLog = message;
		if (PYPContext.getActiveActivity() != null) {
			messageLog = "[" + PYPContext.getActiveActivity().getClass().getSimpleName() + "]" + message;
		}
		myLog.write(messageLog, level);
	}

	/**
	 * @return the email adress of the account using the application
	 */
	public static String getEmailAddress() {
		SharedPreferences settings = PYPContext.getContext().getSharedPreferences(PREFS_NAME, 0);
		return settings.getString(MAIL_ADDRESS, null);

	}

	/**
	 * @param email
	 *            set the adress of the account using the application
	 */
	public static void setEmailAddress(String email) {
		SharedPreferences settings = PYPContext.getContext().getSharedPreferences(PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString(MAIL_ADDRESS, email);
		editor.commit();
	}

	/**
	 * @return the password of the account(saved temporaly)
	 */
	public static String getPassword() {
		SharedPreferences settings = PYPContext.getContext().getSharedPreferences(PREFS_NAME, 0);
		return settings.getString(PASSWORD, null);

	}

	/**
	 * @param password
	 *            the password to store temporaly
	 */
	public static void setPassword(String password) {
		SharedPreferences settings = PYPContext.getContext().getSharedPreferences(PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString(PASSWORD, password);
		editor.commit();
	}

	/**
	 * @return the status of the device: true if bugged false otherwhise
	 */
	public static boolean getBugged() {
		SharedPreferences settings = PYPContext.getContext().getSharedPreferences(PREFS_NAME, 0);
		return settings.getBoolean(IS_BUGED, false);

	}

	/**
	 * @param bug
	 *            sets the status of the device( bugged or not)
	 */
	public static void setBugged(boolean bug) {
		SharedPreferences settings = PYPContext.getContext().getSharedPreferences(PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putBoolean(IS_BUGED, bug);
		editor.commit();
	}

	/**
	 * 
	 * @return true if first launch of the app. If so, inform to the app that it
	 *         has been started for the first time.
	 */
	public static boolean isFirstLaunch() {
		SharedPreferences settings = PYPContext.getContext().getSharedPreferences(PREFS_NAME, 0);
		boolean firstLaunch = settings.getBoolean(V1_FIRST_LAUNCH, true);
		return firstLaunch;

	}

	/**
	 * @param firstLaunch
	 *            sets the flag of the first launch
	 */
	public static void setFirstLaunch(boolean firstLaunch) {
		SharedPreferences settings = PYPContext.getContext().getSharedPreferences(PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putBoolean(V1_FIRST_LAUNCH, firstLaunch);
		editor.commit();
	}

	/**
	 * @return if there was an error to show the label of the error in certain
	 *         views
	 */
	public static boolean isError() {
		SharedPreferences settings = PYPContext.getContext().getSharedPreferences(PREFS_NAME, 0);
		if (settings.getBoolean(IS_ERROR, false)) {
			setError(false);
			return true;
		}
		return false;
	}

	/**
	 * @param error
	 *            sets a flag to notify certain views that there was an error
	 */
	public static void setError(boolean error) {
		SharedPreferences settings = PYPContext.getContext().getSharedPreferences(PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putBoolean(IS_ERROR, error);
		editor.commit();
	}

	/**
	 * @return return the error code if there was an error for the labels in
	 *         ceratin views
	 */
	public static int getErrorCode() {
		SharedPreferences settings = PYPContext.getContext().getSharedPreferences(PREFS_NAME, 0);
		return Integer.parseInt(settings.getString(ERROR_CODE, "0"));

	}


	/**
	 * @param code
	 *            * sets the code error flag to notify certain views that there
	 *            was an error
	 */
	public static void setErrorCode(int code) {
		SharedPreferences settings = PYPContext.getContext().getSharedPreferences(PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString(ERROR_CODE, String.valueOf(code));
		editor.commit();
	}

}
