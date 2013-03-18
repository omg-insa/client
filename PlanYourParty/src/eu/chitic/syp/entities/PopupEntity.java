package eu.chitic.syp.entities;

import android.content.DialogInterface;


/**
 * The entity of a new popup
 */
public class PopupEntity {

	private int idTitle = 0;
	private int idText = 0;
	private int idButton1Text = 0;
	private int idButton2Text = 0;
	private int idIcon = 0;
	private DialogInterface.OnClickListener firstButtonListener;
	private DialogInterface.OnClickListener secondButtonListener;
	private boolean stop;

	public PopupEntity(int idTitle, int idText, int idButton1, int idButton2, int idIcon, DialogInterface.OnClickListener firstButtonListener,
			DialogInterface.OnClickListener secondButtonListener, boolean stop) {
		
		this.idTitle = idTitle;
		this.idText = idText;
		this.idButton1Text = idButton1;
		this.idButton2Text = idButton2;
		this.idIcon = idIcon;
		this.stop = stop;
		this.firstButtonListener = firstButtonListener;
		this.secondButtonListener = secondButtonListener;
	}

	public boolean isStop() {
		return stop;
	}

	public void setStop(boolean stop) {
		this.stop = stop;
	}

	public int getIdTitle() {
		return idTitle;
	}

	public void setIdTitle(int idTitle) {
		this.idTitle = idTitle;
	}

	public int getIdText() {
		return idText;
	}

	public void setIdText(int idText) {
		this.idText = idText;
	}

	public int getIdButton1Text() {
		return idButton1Text;
	}

	public void setIdButton1Text(int idButton1Text) {
		this.idButton1Text = idButton1Text;
	}

	public int getIdButton2Text() {
		return idButton2Text;
	}

	public void setIdButton2Text(int idButton2Text) {
		this.idButton2Text = idButton2Text;
	}

	public int getIdIcon() {
		return idIcon;
	}

	public void setIdIcon(int idIcon) {
		this.idIcon = idIcon;
	}

	public DialogInterface.OnClickListener getFirstButtonListener() {
		return firstButtonListener;
	}

	public void setFirstButtonListener(DialogInterface.OnClickListener firstButtonListener) {
		this.firstButtonListener = firstButtonListener;
	}

	public DialogInterface.OnClickListener getSecondButtonListener() {
		return secondButtonListener;
	}

	public void setSecondButtonListener(DialogInterface.OnClickListener secondButtonListener) {
		this.secondButtonListener = secondButtonListener;
	}

}

