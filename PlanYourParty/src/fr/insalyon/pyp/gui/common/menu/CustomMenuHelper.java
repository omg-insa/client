package fr.insalyon.pyp.gui.common.menu;

import java.util.ArrayList;

import android.content.res.Resources;
import fr.insalyon.pyp.R;
import fr.insalyon.pyp.gui.common.menu.CustomMenu.OnMenuItemSelectedListener;
import fr.insalyon.pyp.tools.AppTools;
import fr.insalyon.pyp.tools.PYPContext;
import fr.insalyon.pyp.tools.Constants;

public class CustomMenuHelper {

	private CustomMenu mMenu;

	public CustomMenuHelper(int size, OnMenuItemSelectedListener listener) {
		mMenu = new CustomMenu(PYPContext.getContext(), listener, PYPContext.getActiveActivity().getLayoutInflater());
		mMenu.setHideOnSelect(true);
		mMenu.setItemsPerLineInPortraitOrientation(size);
		loadMenuItems(size);
	}

	/**
	 * Load up our menu.
	 */
	private void loadMenuItems(int size) {
		// This is kind of a tedious way to load up the menu items.
		// Am sure there is room for improvement.
		Resources res = PYPContext.getContext().getResources();
		ArrayList<CustomMenuItem> menuItems = new ArrayList<CustomMenuItem>();
		CustomMenuItem cmi = new CustomMenuItem();
		cmi.setCaption(res.getString(R.string.settings));
		cmi.setImageResourceId(android.R.drawable.ic_menu_preferences);
		cmi.setId(Constants.MENU_ITEM_1);
		menuItems.add(cmi);

		cmi = new CustomMenuItem();
		cmi.setCaption(res.getString(R.string.logout));
		cmi.setImageResourceId(android.R.drawable.ic_menu_rotate);
		cmi.setId(Constants.MENU_ITEM_2);
		menuItems.add(cmi);
		
		if (!mMenu.isShowing())
			try {
				mMenu.setMenuItems(menuItems);
			} catch (Exception e) {
				AppTools.error(e.getMessage());
			}
	}

	public void doMenu() {
		if (mMenu.isShowing()) {
			mMenu.hide();
		} else {
			// Note it doesn't matter what widget you send the menu as long as
			// it gets view.
			mMenu.show(PYPContext.getActiveActivity().findViewById(R.id.abstract_header_layout));
		}
	}
}