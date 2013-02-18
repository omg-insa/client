package fr.insalyon.pyp.gui.login;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import fr.insalyon.pyp.R;
import fr.insalyon.pyp.gui.common.*;
import fr.insalyon.pyp.tools.AppTools;
import fr.insalyon.pyp.tools.Constants;
import fr.insalyon.pyp.tools.PYPContext;

public class LoginActivity extends BaseActivity {
	private LinearLayout abstractView;
	private LinearLayout mainView;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState, Constants.HOME_CONST);
		AppTools.info("on create AccueilActivity");

		initGraphicalInterface();
	}

	private void initGraphicalInterface() {
		// set layouts
		LayoutInflater mInflater = LayoutInflater.from(this);
		abstractView = (LinearLayout) findViewById(R.id.abstractLinearLayout);
		mainView = (LinearLayout) mInflater.inflate(R.layout.login_activity, null);
		abstractView.addView(mainView);
		hideHeader(true);
		
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

}
