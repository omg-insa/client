package fr.insalyon.pyp.gui.events;

import java.util.ArrayList;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import fr.insalyon.pyp.R;
import fr.insalyon.pyp.gui.common.BaseActivity;
import fr.insalyon.pyp.tools.AppTools;

public class IntrestsAdapter extends BaseAdapter {
 
    private BaseActivity activity;
    private ArrayList<String[]> data;
    private static LayoutInflater inflater=null;
 
    public IntrestsAdapter(BaseActivity a, ArrayList<String[]>d) {
        activity = a;
        data=d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        AppTools.debug("Adapter");
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
        //CheckBox interest_checkBox = (CheckBox) vi.findViewById(R.id.interest_checkBox);
        String[] row_data = data.get(position);

        vi.setTag(row_data[3]);
        // Setting all values in listview
        title.setText(row_data[0]);
        description.setText(row_data[1]);
        //interest_checkBox.setChecked(row_data[2].equals("true"));

        return vi;
    }
    
  
}

