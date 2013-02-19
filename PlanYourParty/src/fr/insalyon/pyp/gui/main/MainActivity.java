package fr.insalyon.pyp.gui.main;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import fr.insalyon.pyp.R;
import fr.insalyon.pyp.gui.common.BaseActivity;
import fr.insalyon.pyp.gui.common.IntentHelper;
import fr.insalyon.pyp.gui.common.popup.Popups;
import fr.insalyon.pyp.gui.login.ForgotPasswordActivity;
import fr.insalyon.pyp.gui.login.LoginActivity;
import fr.insalyon.pyp.gui.login.RegisterActivity;
import fr.insalyon.pyp.tools.AppTools;
import fr.insalyon.pyp.tools.Constants;

public class MainActivity extends BaseActivity {
			private LinearLayout abstractView;
			private LinearLayout mainView;
			
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
				mainView = (LinearLayout) mInflater.inflate(R.layout.main_layout,
						null);
				abstractView.addView(mainView);
				hideHeader(false);
			}


			@Override
			public void onResume() {
				super.onResume();
				//check if logged in
				checkLoggedIn();
			}
	
}
