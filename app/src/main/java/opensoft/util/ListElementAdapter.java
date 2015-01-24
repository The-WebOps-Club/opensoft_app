package opensoft.util;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import opensoft.com.opensoft.R;

/**
 * Created by vigneshm on 24/01/15.
 */
public class ListElementAdapter extends ArrayAdapter<ListElement> implements Filterable {
    Context context;
    int layoutResourceId;
    List<ListElement> data = new ArrayList<>();
    List<ListElement> filteredData=new ArrayList<>();
    public ListElementAdapter(Context context, int layoutResourceId, List<ListElement> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
        this.filteredData.addAll(data);
    }

    public Filter getFilter(){
        return new ElementFilter();
    }
    public int getCount(){
        return filteredData.size();
    }
    public View getView(int position, View convertView, ViewGroup parent){
        View row = convertView;
        ElementHolder holder=null;
        if(row==null){
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new ElementHolder();
            holder.icon = (ImageView)row.findViewById(R.id.icon);
            holder.title = (TextView)row.findViewById(R.id.title);
            holder.info = (TextView)row.findViewById(R.id.info);
            row.setTag(holder);
        } else {
            holder=(ElementHolder) row.getTag();
        }
        if(position<filteredData.size()){
            ListElement element=filteredData.get(position);
            holder.title.setText(element.title);
            holder.info.setText(element.info);
            holder.icon.setImageResource(element.icon);
        } else {
            holder=null;
        }
        return row;
    }

    static class ElementHolder{
        ImageView icon;
        TextView title,info;
    }

    private class ElementFilter extends Filter{
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            // We implement here the filter logic
            if (constraint == null || constraint.length() == 0) {
                // No filter implemented we return all the list
                results.values = data;
                results.count = data.size();
            }
            else {
                // We perform filtering operation
                List<ListElement> filteredList = new ArrayList<ListElement>();

                for (ListElement p : data) {
                    if ((p.title.toUpperCase().contains(constraint.toString().toUpperCase() ) )||(p.info.toUpperCase().contains(constraint.toString().toUpperCase() ) ) )
                        filteredList.add(p);
                }

                results.values = filteredList;
                results.count = filteredList.size();

            }
            filteredData = (List<ListElement>) results.values;
            return results;
        }
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {

            // Now we have to inform the adapter about the new list filtered
            if (results.count == 0)
                notifyDataSetInvalidated();
            else {
                filteredData = (List<ListElement>) results.values;
                System.out.println(filteredData.size());
                for(ListElement i:filteredData) System.out.println(i.title+"\t"+i.info);
                notifyDataSetChanged();
            }

        }
    }
}
