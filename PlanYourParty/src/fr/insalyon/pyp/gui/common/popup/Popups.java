package fr.insalyon.pyp.gui.common.popup;

import java.util.HashMap;
import fr.insalyon.pyp.R;
import fr.insalyon.pyp.entities.PopupEntity;
import fr.insalyon.pyp.tools.Constants;

/**
 * @author A510944
 * 
 */
public class Popups {

	private static HashMap<String, PopupEntity> popupList = new HashMap<String, PopupEntity>();

	/**
	 * init the list of popups
	 */
	private static void init() {
		initWrongUsernameOrPassword();
		initIncompleatData();
		initEmailAlreadyUsed();
		initInvalidEmail();
		initpasswordsDontMatch();
		initpasswordTooShort();
		initUsernameAlreadyUsed();
		initcurentPasswordWrong();
	}

	/**
	 * show a popup
	 * 
	 * @param id
	 *            the id of the popup to show
	 */
	public static void showPopup(String id) {
		if (popupList.size() == 0) {
			init();
		}
		AbstractPopup.showPopup(popupList.get(id));
	}

	private static void initWrongUsernameOrPassword() {
		PopupEntity popup = new PopupEntity(
				R.string.initWrongUsernameOrPassword_title,
				R.string.initWrongUsernameOrPassword_text,
				R.string.alert_dialog_ok, 0, android.R.drawable.ic_dialog_info,
				null, null, false);
		popupList.put(Constants.WrongUsernameOrPassword, popup);

	}
	
	private static void initcurentPasswordWrong() {
		PopupEntity popup = new PopupEntity(
				R.string.curentPasswordWrong_title,
				R.string.curentPasswordWrong_text,
				R.string.alert_dialog_ok, 0, android.R.drawable.ic_dialog_info,
				null, null, false);
		popupList.put(Constants.curentPasswordWrong, popup);

	}


	private static void initIncompleatData() {
		PopupEntity popup = new PopupEntity(
				R.string.initIncompleatData_title,
				R.string.initIncompleatData_text,
				R.string.alert_dialog_ok, 0, android.R.drawable.ic_dialog_info,
				null, null, false);
		popupList.put(Constants.IncompleatData, popup);

	}
	
	private static void initEmailAlreadyUsed() {
		PopupEntity popup = new PopupEntity(
				R.string.EmailAlreadyUsed_title,
				R.string.EmailAlreadyUsed_text,
				R.string.alert_dialog_ok, 0, android.R.drawable.ic_dialog_info,
				null, null, false);
		popupList.put(Constants.EmailAlreadyUsed, popup);

	}
	private static void initInvalidEmail() {
		PopupEntity popup = new PopupEntity(
				R.string.InvalidEmail_title,
				R.string.InvalidEmail_text,
				R.string.alert_dialog_ok, 0, android.R.drawable.ic_dialog_info,
				null, null, false);
		popupList.put(Constants.InvalidEmail, popup);

	}
	private static void initpasswordsDontMatch() {
		PopupEntity popup = new PopupEntity(
				R.string.passwordsDontMatch_title,
				R.string.passwordsDontMatch_text,
				R.string.alert_dialog_ok, 0, android.R.drawable.ic_dialog_info,
				null, null, false);
		popupList.put(Constants.passwordsDontMatch, popup);

	}
	
	private static void initpasswordTooShort() {
		PopupEntity popup = new PopupEntity(
				R.string.passwordsDontMatch_title,
				R.string.passwordTooShort_text,
				R.string.alert_dialog_ok, 0, android.R.drawable.ic_dialog_info,
				null, null, false);
		popupList.put(Constants.passwordTooShort, popup);
	}
	
	private static void initUsernameAlreadyUsed() {
		PopupEntity popup = new PopupEntity(
				R.string.UsernameAlreadyUsed_title,
				R.string.UsernameAlreadyUsed_text,
				R.string.alert_dialog_ok, 0, android.R.drawable.ic_dialog_info,
				null, null, false);
		popupList.put(Constants.UsernameAlreadyUsed, popup);
	}
	
}
