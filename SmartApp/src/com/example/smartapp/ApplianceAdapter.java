package com.example.smartapp;
/*
 * Adapter class to show details of the chosen appliance in listview
 */
import java.util.List;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ApplianceAdapter extends ArrayAdapter<ApplianceList>{
	List<ApplianceList> mData;
	Context mContext;
	int mResource;
	public ApplianceAdapter(Context context, int resource,
			List<ApplianceList> objects) {
		super(context, resource, objects);
		this.mContext = context;
		this.mData = objects;
		this.mResource = resource;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if(convertView == null){
			LayoutInflater inflator = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflator.inflate(mResource, parent, false);
		}
		TextView appName = (TextView) convertView.findViewById(R.id.textView1);
		TextView watt = (TextView) convertView.findViewById(R.id.textView11);
		TextView start = (TextView) convertView.findViewById(R.id.textView4);
		TextView end = (TextView) convertView.findViewById(R.id.textView6);
		TextView run = (TextView) convertView.findViewById(R.id.textView8);
		TextView constraints = (TextView) convertView.findViewById(R.id.textView10);
		
		appName.setText(mData.get(position).getApplianceName());
		watt.setText(mData.get(position).getWattage());
		start.setText(mData.get(position).getStartTime());
		end.setText(mData.get(position).getEndTime());
		run.setText(mData.get(position).getRunTime());
		constraints.setText(mData.get(position).getConstraints());
		
		return convertView;
	}
}
