package eu.chitic.syp.gui.events;

import java.util.ArrayList;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import eu.chitic.syp.R;
import eu.chitic.syp.gui.common.BaseActivity;

public class ChatAdapter extends BaseAdapter {
 
    private BaseActivity activity;
    private ArrayList<String[]> data;
    private static LayoutInflater inflater=null;
 
    public ChatAdapter(BaseActivity a, ArrayList<String[]>d) {
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
            vi = inflater.inflate(R.layout.chat_row, null);
        
        TextView user = (TextView)vi.findViewById(R.id.chat_item_user);
        TextView message = (TextView)vi.findViewById(R.id.chat_item_message);
        TextView date = (TextView)vi.findViewById(R.id.chat_item_date);
        String[] row_data = data.get(position);
        
    	String s_message = Html.fromHtml(row_data[1]).toString();
        date.setText(row_data[0]);
        message.setText(s_message);
        user.setText(row_data[2]);
        return vi;
    }
    
  
}

