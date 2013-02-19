package fr.insalyon.pyp.gui.common;

import java.io.Serializable;

import android.content.Intent;
import fr.insalyon.pyp.tools.PYPContext;
import fr.insalyon.pyp.tools.Constants;

/**
 * @author A510944
 * 
 */
public class IntentHelper {

	/**
	 * Open a new activity
	 * 
	 * @param cls
	 *            the activity to open
	 * @param params
	 *            the params to add in the new intent ( a serializable object)
	 * @param forceExit
	 *            if true, the initiator will be overpassed on back action
	 */
	public static void openNewActivity(Class<? extends BaseActivity> cls,
			Serializable params, boolean forceExit, boolean waitForResults) {

		Intent intent = new Intent(PYPContext.getContext(), cls);
		intent.putExtra(Constants.PARAMNAME, params);
		if (!waitForResults) {
			PYPContext.getContext().startActivity(intent);
		} else {
			PYPContext.getActiveActivity().startActivityForResult(intent,100);
		}
	}

	/**
	 * Gets the params from the intent that lunched the activity
	 * 
	 * @param clazz
	 *            the type of object expected in return
	 * @return the params of the intent
	 */
	public static <T> T getActiveIntentParam(Class<T> clazz) {
		Object param = PYPContext.getActiveActivity().getIntent().getExtras()
				.get(Constants.PARAMNAME);
		return clazz.cast(param);
	}

}
