package fr.insalyon.pyp.gui.account;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
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

public class AcountActivity extends BaseActivity {
	private LinearLayout abstractView;
	private LinearLayout mainView;
	private Button personalInfo;
	private Button intrestInfo;
	private Button securityInfo;

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
		securityInfo.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				IntentHelper.openNewActivity(SecurityActivity.class, null, false);
			}
		});
		personalInfo = (Button) findViewById(R.id.profils_info_perso);
		personalInfo.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				IntentHelper.openNewActivity(PersoanlInfoActivity.class, null, false);
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