package fr.insalyon.pyp.gui.account;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import fr.insalyon.pyp.R;
import fr.insalyon.pyp.R.id;
import fr.insalyon.pyp.gui.common.BaseActivity;
import fr.insalyon.pyp.gui.common.popup.Popups;
import fr.insalyon.pyp.tools.AppTools;
import fr.insalyon.pyp.tools.Constants;

public class PersoanlInfoActivity  extends BaseActivity {
	private LinearLayout abstractView;
	private ScrollView mainView;
	Spinner sexSpinner;
	Spinner statusSpinner;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState, Constants.PERSONAINFO_CONST);
		AppTools.info("on create PersonalInfoActivity");
		initGraphicalInterface();
	}

	private void initGraphicalInterface() {
		// set layouts
		LayoutInflater mInflater = LayoutInflater.from(this);
		abstractView = (LinearLayout) findViewById(R.id.abstractLinearLayout);
		mainView = (ScrollView) mInflater.inflate(R.layout.personal_info_activity,
				null);
		abstractView.addView(mainView);
		
		sexSpinner = (Spinner) findViewById(R.id.sex_spinner);
		// Create an ArrayAdapter using the string array and a default spinner layout
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
		        R.array.sex_array, android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		sexSpinner.setAdapter(adapter);
		statusSpinner = (Spinner) findViewById(R.id.status_spinner);
		// Create an ArrayAdapter using the string array and a default spinner layout
		ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
		        R.array.status_array, android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		statusSpinner.setAdapter(adapter2);
		
		  Drawable background = ((LinearLayout)
		  findViewById(R.id.personal_info_backgorund)) .getBackground();
		  background.setAlpha(95);
		 
	}

	@Override
	public void onResume() {
		super.onResume();
		checkLoggedIn();
	}


}
