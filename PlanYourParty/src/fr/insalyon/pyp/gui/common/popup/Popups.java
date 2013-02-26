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
		initWrongDate();
		initWrongUsername();
		initWrongAnswer();
		initNoSecretQuestion();
		initWrongData();
		initDifferentPassword();
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
	
	private static void initWrongDate() {
		PopupEntity popup = new PopupEntity(
				R.string.dateFormatWrong_title,
				R.string.dateFormatWrong_text,
				R.string.alert_dialog_ok, 0, android.R.drawable.ic_dialog_info,
				null, null, false);
		popupList.put(Constants.dateFormatWrong, popup);

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
	
	private static void initWrongUsername() {
		PopupEntity popup = new PopupEntity(
				R.string.WrongUsername_title,
				R.string.WrongUsername_text,
				R.string.alert_dialog_ok, 0, android.R.drawable.ic_dialog_info,
				null, null, false);
		popupList.put(Constants.WrongUsername, popup);
	}
	
	private static void initNoSecretQuestion() {
		PopupEntity popup = new PopupEntity(
				R.string.NoSecretQuestion_title,
				R.string.NoSecretQuestion_text,
				R.string.alert_dialog_ok, 0, android.R.drawable.ic_dialog_info,
				null, null, false);
		popupList.put(Constants.NoSecretQuestion, popup);
	}

	private static void initWrongAnswer() {
		PopupEntity popup = new PopupEntity(
				R.string.WrongAnswer_title,
				R.string.WrongAnswer_text,
				R.string.alert_dialog_ok, 0, android.R.drawable.ic_dialog_info,
				null, null, false);
		popupList.put(Constants.WrongAnswer, popup);
	}
	
	private static void initWrongData() {
		PopupEntity popup = new PopupEntity(
				R.string.WrongData_title,
				R.string.WrongData_text,
				R.string.alert_dialog_ok, 0, android.R.drawable.ic_dialog_info,
				null, null, false);
		popupList.put(Constants.WrongData, popup);
	}
	
	private static void initDifferentPassword() {
		PopupEntity popup = new PopupEntity(
				R.string.DifferentPassword_title,
				R.string.DifferentPassword_text,
				R.string.alert_dialog_ok, 0, android.R.drawable.ic_dialog_info,
				null, null, false);
		popupList.put(Constants.DifferentPassword, popup);
	}
	
}
