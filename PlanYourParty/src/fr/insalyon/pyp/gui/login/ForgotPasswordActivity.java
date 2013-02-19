package fr.insalyon.pyp.gui.login;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import fr.insalyon.pyp.R;
import fr.insalyon.pyp.gui.common.BaseActivity;
import fr.insalyon.pyp.gui.common.IntentHelper;
import fr.insalyon.pyp.tools.AppTools;
import fr.insalyon.pyp.tools.Constants;

public class ForgotPasswordActivity extends BaseActivity {
	private LinearLayout abstractView;
	private LinearLayout mainView;
	
	private Button cancelButton;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState, Constants.REGISTER_CONST);
		AppTools.info("on create ForgotPasswordActivity");
		initGraphicalInterface();
	}

	private void initGraphicalInterface() {
		// set layouts
		LayoutInflater mInflater = LayoutInflater.from(this);
		abstractView = (LinearLayout) findViewById(R.id.abstractLinearLayout);
		mainView = (LinearLayout) mInflater.inflate(R.layout.forgot_password_activity, null);
		abstractView.addView(mainView);
		
		cancelButton = (Button) findViewById(R.id.cancel);
		
		cancelButton.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				finish();
			}
		});
		hideHeader(false);
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
