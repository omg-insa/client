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
import fr.insalyon.pyp.tools.PYPContext;

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


        // Setting all values in listview
        
//		try {
//			URL newurl = new URL(row_data[0]);
//			Bitmap mIcon_val = BitmapFactory.decodeStream(newurl.openConnection().getInputStream());
//	        icon.setImageBitmap(mIcon_val);
//		} catch (MalformedURLException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
        /*if (position == data.size() - 1) {
        	//vi.setTag(new String[] {"last","false"});
			date.setText(row_data[0]);
			message.setText(null);
			user.setText(null);
		} else {*/
	        date.setText(row_data[0]);
	        message.setText(row_data[1]);
	        user.setText(row_data[2]);
		//}
        return vi;
    }
    
  
}

