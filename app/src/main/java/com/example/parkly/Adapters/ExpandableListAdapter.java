package com.example.parkly.Adapters;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.example.parkly.R;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> listDataHeader;
    private HashMap<String, List<String>> listDataChild;
    private String parkingTime[] = {"<font color='#175261'>8-18h</font>","<font color='#175261'>8-18h</font>","<font color='#175261'>8-18h</font>","<font color='#175261'>8-18h</font>","<font color='#175261'>8-24h</font>"};
    private String parkingDays[] = {"<font color='#175261'>I-V</font>", "<font color='#175261'>I-V</font>", "<font color='#175261'>I-V</font>", "<font color='#175261'>I-V</font>", "<font color='#175261'>I-VII</font>"};

    public ExpandableListAdapter(Context context, List<String> listDataHeader,HashMap<String, List<String>> listChildData)
    {
        this.context = context;
        this.listDataHeader = listDataHeader;
        this.listDataChild = listChildData;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this.listDataChild.get(this.listDataHeader.get(groupPosition)).get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,boolean isLastChild, View convertView, ViewGroup parent) {

        final String childText = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_item, null);
        }

        TextView txtListChild = convertView.findViewById(R.id.lblListItem);

        txtListChild.setText(childText);
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.listDataChild.get(this.listDataHeader.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.listview_layout, null);
        }

        TextView lblListHeader = convertView.findViewById(R.id.txt_zones);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

        TextView time = convertView.findViewById(R.id.txt_time);
        time.setTypeface(null, Typeface.BOLD);
        time.setText(Html.fromHtml(parkingTime[groupPosition]));

        TextView days = convertView.findViewById(R.id.txt_days);
        days.setTypeface(null, Typeface.BOLD);
        days.setText(Html.fromHtml(parkingDays[groupPosition]));

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}