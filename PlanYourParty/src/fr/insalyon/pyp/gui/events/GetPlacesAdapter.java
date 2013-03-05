package fr.insalyon.pyp.gui.events;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
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
        
//        ImageView icon = (ImageView)vi.findViewById(R.id.item_icon);
        TextView title = (TextView)vi.findViewById(R.id.item_name);
        TextView type = (TextView)vi.findViewById(R.id.item_type);
        TextView id = (TextView)vi.findViewById(R.id.item_id);
        TextView description = (TextView)vi.findViewById(R.id.item_address);
        String[] row_data = data.get(position);

        vi.setTag(row_data[3]);
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
		
        title.setText(row_data[0]);
        type.setText(row_data[1]);
        description.setText(row_data[2]);
        id.setText(row_data[3]);
        
        return vi;
    }
    
  
}

