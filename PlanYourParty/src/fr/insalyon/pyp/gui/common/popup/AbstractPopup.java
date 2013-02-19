package fr.insalyon.pyp.gui.common.popup;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;
import fr.insalyon.pyp.R;
import fr.insalyon.pyp.entities.PopupEntity;
import fr.insalyon.pyp.tools.AppTools;
import fr.insalyon.pyp.tools.PYPContext;

public class AbstractPopup {

	/**
	 * The function to build a custom alert dialog
	 * @param popup 
	 * 			popup enitity to show in the alert dialog
	 */
	public static void showPopup(final PopupEntity popup) {
		AlertDialog.Builder builder = new AlertDialog.Builder(PYPContext.getActiveActivity());
		if (popup.getIdTitle() != 0) {
			builder.setTitle(popup.getIdTitle());
		}
		if (popup.getIdIcon() != 0) {
			builder.setIcon(popup.getIdIcon());
		}
		if(popup.getIdButton1Text()!=0){
			if(popup.getFirstButtonListener()!=null){
				builder.setPositiveButton(popup.getIdButton1Text(), popup.getFirstButtonListener());
			}
			else{
				DialogInterface.OnClickListener newListner = new DialogInterface.OnClickListener() {
					
					public void onClick(DialogInterface dialog, int which) {
						if(popup.isStop()){
							PYPContext.getActiveActivity().finish();
						}
					}
				};
				builder.setPositiveButton(popup.getIdButton1Text(),newListner);
			}
		}
		if(popup.getIdButton2Text()!=0){
			if(popup.getSecondButtonListener()!=null){
				builder.setNegativeButton(popup.getIdButton2Text(), popup.getSecondButtonListener());
			}
			else{
				DialogInterface.OnClickListener newListner = new DialogInterface.OnClickListener() {
					
					public void onClick(DialogInterface dialog, int which) {
						if(popup.isStop()){
							AppTools.setToExit(true);
							PYPContext.getActiveActivity().finish();
						}
					}
				};
				builder.setNegativeButton(popup.getIdButton2Text(),newListner);
			}
		}
		
		LinearLayout linearLayout = new LinearLayout(PYPContext.getActiveActivity());
		LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		linearLayout.setLayoutParams(layoutParams);

		TextView textView = new TextView(PYPContext.getActiveActivity());
		textView.setGravity(Gravity.CENTER);
		LayoutParams textLayoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		textLayoutParams.setMargins(15, 15, 15, 15);
		textView.setLayoutParams(textLayoutParams);
		textView.setTextAppearance(PYPContext.getActiveActivity().getApplicationContext(), R.style.Dialogbox);
		
		if (popup.getIdText() != 0) {
			textView.setText(PYPContext.getActiveActivity().getString(popup.getIdText()));
		}
		linearLayout.addView(textView);

		builder.setView(linearLayout);
		builder.create().show();
		
		return;
	}
}
