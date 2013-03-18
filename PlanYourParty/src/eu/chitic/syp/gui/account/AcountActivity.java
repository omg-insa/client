package eu.chitic.syp.gui.account;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import  eu.chitic.syp.R;
import eu.chitic.syp.gui.common.BaseActivity;
import eu.chitic.syp.gui.common.IntentHelper;
import eu.chitic.syp.tools.AppTools;
import eu.chitic.syp.tools.Constants;

public class AcountActivity extends BaseActivity {
	private LinearLayout abstractView;
	private LinearLayout mainView;
	private Button personalInfo;
	private Button intrestInfo;
	private Button securityInfo;
	private TextView windowTitle;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState, Constants.PROFILE_CONST);
		AppTools.info("on create ProfileActivity");
		initGraphicalInterface();
	}

	private void initGraphicalInterface() {
		// set layouts
		LayoutInflater mInflater = LayoutInflater.from(this);
		abstractView = (LinearLayout) findViewById(R.id.abstractLinearLayout);
		mainView = (LinearLayout) mInflater.inflate(R.layout.profils_activity,
				null);
		abstractView.addView(mainView);
		securityInfo = (Button) findViewById(R.id.profils_activity_securite);
		
		windowTitle = (TextView) findViewById(R.id.pageTitle);
		windowTitle.setText(R.string.AccountTitle);
		
		securityInfo.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				IntentHelper.openNewActivity(SecurityActivity.class, null, false);
			}
		});
		personalInfo = (Button) findViewById(R.id.profils_info_perso);
		personalInfo.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				IntentHelper.openNewActivity(PersonalInfoActivity.class, null, false);
			}
		});
		intrestInfo = (Button) findViewById(R.id.profils_activity_interets);

		intrestInfo.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				IntentHelper.openNewActivity(IntrestActivity.class, null, false);
			}
		});
		
		Drawable background = ((LinearLayout) findViewById(R.id.profile_backgorund))
				.getBackground();
		background.setAlpha(95);
		}


	@Override
	public void onResume() {
		super.onResume();
		checkLoggedIn();
	}
}
