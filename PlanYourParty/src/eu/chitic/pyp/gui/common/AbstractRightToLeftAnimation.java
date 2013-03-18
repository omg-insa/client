package eu.chitic.pyp.gui.common;

import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;

/**
 * @author A510944
 * Animation right to left
 */
public class AbstractRightToLeftAnimation {

	private static ViewGroup MainView;
	private static RelativeLayout Panel;
	private static ViewGroup ContentView;
	private static BaseActivity ActivityS;

	public static void setLayoutView_slidefromLeft(ViewGroup panel, BaseActivity activity) {

		AnimationSet set = new AnimationSet(true);
		Animation animation = new AlphaAnimation(0.0f, 1.0f);
		animation.setDuration(100);
		set.addAnimation(animation);
		animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 1.0f,
				Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
				0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
		animation.setDuration(500);
		set.addAnimation(animation);

		LayoutAnimationController controller = new LayoutAnimationController(
				set, 0.25f);
		panel.setLayoutAnimation(controller);

	}

	public static void setRelativeLayoutAnim_slidefromLeftOut(
			RelativeLayout panel, ViewGroup mainView,ViewGroup contentView, BaseActivity activity) {

		Panel = panel;
		MainView = mainView;
		ContentView = contentView;
		ActivityS = activity;
		AnimationSet set = new AnimationSet(true);
		Animation animation = new AlphaAnimation(0.0f, 1.0f);
		animation.setDuration(50);
		set.addAnimation(animation);

		animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
				Animation.RELATIVE_TO_SELF, -1.0f, Animation.RELATIVE_TO_SELF,
				0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
		animation.setDuration(500);
		animation.setAnimationListener(new AnimationListener() {
			public void onAnimationEnd(Animation anim) {
				MainView.removeView(Panel);
				MainView.addView(ContentView);
				ActivityS.setGraphicalInterface();
			}

			public void onAnimationRepeat(Animation animation) {
			}

			public void onAnimationStart(Animation animation) {
			}
		});
		set.addAnimation(animation);

		LayoutAnimationController controller = new LayoutAnimationController(
				set, 0.25f);
		panel.setLayoutAnimation(controller);

	}
}
