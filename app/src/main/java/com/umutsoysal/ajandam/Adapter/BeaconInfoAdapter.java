package com.umutsoysal.ajandam.Adapter;


import android.app.Dialog;
import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.umutsoysal.ajandam.R;


public class BeaconInfoAdapter extends BaseAdapter
{

    // Declare Variables
    Dialog dialog;
    Context context = null;
    String[] BeaconName;
    String[] DeviceMAc;
    String[] BeaconUid;
    String[] Major;
    String[] Minor;

    LayoutInflater inflater;

    //ListviewAdapter constructor
    //Gelen değerleri set ediyor
    public BeaconInfoAdapter(Context context, String[] name, String[] DeviceMac, String[] Beaconuid, String[] major,String[] minor)
    {
        this.context = context;
        this.BeaconName = name;
        this.DeviceMAc = DeviceMac;
        this.BeaconUid = Beaconuid;
        this.Major = major;
        this.Minor = minor;
    }

    @Override
    public int getCount()
    {
        return BeaconUid.length;
    }

    @Override
    public Object getItem(int position)
    {
        return null;
    }

    @Override
    public long getItemId(int position)
    {
        return 0;
    }

    public View getView(final int position, View convertView, ViewGroup parent)
    {

        // Declare Variables
        TextView name,deviceMac,uid,major,minor;
        ConstraintLayout layout;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = inflater.inflate(R.layout.beacon_info_list_item, parent, false);//list_item_row dan yeni bir view oluşturuyoruz
        name=(TextView)itemView.findViewById(R.id.Beaconname);
        deviceMac=(TextView)itemView.findViewById(R.id.deviceMac);
        uid=(TextView)itemView.findViewById(R.id.proximity_uuid);
        major=(TextView)itemView.findViewById(R.id.major);
        minor=(TextView)itemView.findViewById(R.id.minor);
        layout=(ConstraintLayout)itemView.findViewById(R.id.BeaconLayout);


        if(position%2==0)
        {
            layout.setBackgroundResource(R.drawable.beacon_bg);
        }else{
            layout.setBackgroundResource(R.drawable.ibeacon_bg);
        }

        name.setText(BeaconName[position]);
        deviceMac.setText(DeviceMAc[position]);
        uid.setText(BeaconUid[position]);
        major.setText("Major-"+Major[position]);
        minor.setText("Minor-"+Minor[position]);

        return itemView;
    }


}