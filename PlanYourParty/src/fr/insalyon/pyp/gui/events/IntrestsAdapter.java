package fr.insalyon.pyp.gui.events;

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

public class IntrestsAdapter extends BaseAdapter {
 
    private BaseActivity activity;
    private ArrayList<String[]> data;
    private static LayoutInflater inflater=null;
 
    public IntrestsAdapter(BaseActivity a, ArrayList<String[]>d) {
        activity = a;
        data=d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.create_event_intrests_row, null);
        
        TextView title = (TextView)vi.findViewById(R.id.item_title);
        TextView description = (TextView)vi.findViewById(R.id.item_description);
        ImageView full = (ImageView) vi.findViewById(R.id.item_full);
        ImageView empty = (ImageView) vi.findViewById(R.id.item_empty);
        String[] row_data = data.get(position);

        vi.setTag(row_data[3]);
        // Setting all values in listview
        title.setText(row_data[0]);
        description.setText(row_data[1]);
        if(row_data[2].equals("false")){
        	full.setVisibility(View.GONE);
        	empty.setVisibility(View.VISIBLE);
        }
        else {
        	empty.setVisibility(View.GONE);
        	full.setVisibility(View.VISIBLE);
        }

        return vi;
    }
    
  
}

