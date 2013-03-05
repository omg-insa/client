package fr.insalyon.pyp.gui.main;

import java.util.ArrayList;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import fr.insalyon.pyp.R;
import fr.insalyon.pyp.gui.common.BaseActivity;
import fr.insalyon.pyp.tools.PYPContext;

public class PersonalEventsAdapter extends BaseAdapter {

	private BaseActivity activity;
	private ArrayList<String[]> data;
	private static LayoutInflater inflater = null;

	public PersonalEventsAdapter(BaseActivity a, ArrayList<String[]> d) {
		activity = a;
		data = d;
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public int getCount() {
		return data.size();
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View vi = convertView;
		if (convertView == null)
			vi = inflater.inflate(R.layout.personal_events_row, null);

		TextView title = (TextView) vi
				.findViewById(R.id.personal_events_item_title);
		TextView hours = (TextView) vi
				.findViewById(R.id.persoanl_events_item_hours);
		ImageView next = (ImageView) vi
				.findViewById(R.id.personal_events_next_button);
		String[] row_data = data.get(position);
		if (position == 0) {
			title.setText(row_data[0]);
			hours.setText("");
			next.setImageDrawable(PYPContext.getContext().getResources().getDrawable(android.R.drawable.ic_menu_edit));
			next.setVisibility(View.VISIBLE);
		} else {
			vi.setTag(new String[]{row_data[3],row_data[2]});

			// Setting all values in listview
			title.setText(row_data[0]);
			hours.setText(row_data[1]);
			if (row_data[2].equals("false")) {
				next.setVisibility(View.GONE);
			} else {
				next.setVisibility(View.VISIBLE);
			}
		}
		return vi;
	}

}
