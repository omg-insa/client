package fr.insalyon.pyp.gui.events;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import fr.insalyon.pyp.R;
import fr.insalyon.pyp.gui.common.BaseActivity;

public class GetPlacesAdapter extends BaseAdapter {
 
    private BaseActivity activity;
    private ArrayList<String[]> data;
    private static LayoutInflater inflater=null;
 
    public GetPlacesAdapter(BaseActivity a, ArrayList<String[]>d) {
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
            vi = inflater.inflate(R.layout.get_places_row, null);
        
        TextView title = (TextView)vi.findViewById(R.id.item_name);
        TextView type = (TextView)vi.findViewById(R.id.item_type);
        TextView description = (TextView)vi.findViewById(R.id.item_address);
        String[] row_data = data.get(position);

        vi.setTag(row_data[3]);
        // Setting all values in listview
        title.setText(row_data[0]);
        type.setText(row_data[1]);
        description.setText(row_data[2]);
        
        return vi;
    }
    
  
}

