package fr.insalyon.pyp.gui.common;

import fr.insalyon.pyp.R;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

public class LoadingScreen {
	private static RelativeLayout loadingSreen;
	private static ViewGroup MainView;
	private static ViewGroup ContentView;
	private static BaseActivity ActivityS;

	/**
	 * 
	 * @param show
	 * @param mainView
	 *            : the layout use to add the laoding screen on it
	 * @param contentView
	 *            : the layout to show/hide when loading/finish loading
	 * @param activity
	 *            : the current activity
	 */
	public static void showLoadingScreen(ViewGroup mainView,
			ViewGroup contentView, BaseActivity activity) {
		MainView = mainView;
		ContentView = contentView;
		ActivityS = activity;
		
		LayoutInflater mInflater = LayoutInflater.from(activity);
		loadingSreen = (RelativeLayout) mInflater.inflate(
				R.layout.abstract_loading_screen, null);
		loadingSreen.setLayoutParams(mainView.getLayoutParams());
		mainView.addView(loadingSreen);
		mainView.removeView(ContentView);

	}
	
	/**
	 *  The function will hide the splash screen
	 */
	public static void hideLoadingScreen()
	{
		AbstractRightToLeftAnimation.setRelativeLayoutAnim_slidefromLeftOut(
				loadingSreen,MainView,ContentView, ActivityS);
	}

}
