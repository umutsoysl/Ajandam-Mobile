package com.umutsoysal.ajandam.conversation.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.umutsoysal.ajandam.R;
import com.umutsoysal.ajandam.conversation.model.Message;

import java.util.ArrayList;


public class CustomAdapter extends ArrayAdapter<Message>
{

    private String username;
    private Context context;

    public CustomAdapter(Context context, ArrayList<Message> chatList, String username)
    {
        super(context, 0, chatList);
        this.context=context;
        this.username = username;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {

        Message message = getItem(position);
        if (username.equalsIgnoreCase(message.getGonderici()))
        {

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.right_item_layout, parent, false);

            // TextView txtUser = (TextView) convertView.findViewById(R.id.username);
            TextView txtMessage = (TextView) convertView.findViewById(R.id.content);
            TextView txtTime = (TextView) convertView.findViewById(R.id.time);

            //  txtUser.setText(message.getGonderici());
            txtMessage.setText(message.getMesajText());
            String taemp[] = message.getZaman().split(" ");
            txtTime.setText(taemp[1]);

        }
        else
        {

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.left_item_layout, parent, false);

            TextView txtUser = (TextView) convertView.findViewById(R.id.username);
            TextView txtMessage = (TextView) convertView.findViewById(R.id.content);
            TextView txtTime = (TextView) convertView.findViewById(R.id.time);
            RelativeLayout frame=(RelativeLayout) convertView.findViewById(R.id.frame);
            TextView name = (TextView) convertView.findViewById(R.id.name);

            String color[] = new String[]{"#0095ff", "#ed5d5d", "#9a4bed", "#f38d46", "#5fba7d", "#b7701b", "#dfb956", "#a0514d", "#81c6c1", "#e280d0", "#8bc34a"};
            int bgcolor[]=new int[]{R.color.chat1,R.color.chat2,R.color.chat3,R.color.chat4,R.color.chat5,R.color.chat6,R.color.chat7,R.color.chat8,R.color.chat9,R.color.chat10,R.color.chat11};
            String nu = message.getGonderici().substring(message.getGonderici().length() - 1);
            if (nu.equals("0"))
            {
                txtUser.setTextColor(Color.parseColor(color[0]));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                {
                    frame.setBackgroundTintList(context.getResources().getColorStateList(bgcolor[0]));
                }
            }
            else if (nu.equals("1"))
            {
                txtUser.setTextColor(Color.parseColor(color[1]));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                {
                    frame.setBackgroundTintList(context.getResources().getColorStateList(bgcolor[1]));
                }

            }
            else if (nu.equals("2"))
            {
                txtUser.setTextColor(Color.parseColor(color[2]));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                {
                    frame.setBackgroundTintList(context.getResources().getColorStateList(bgcolor[2]));
                }

            }
            else if (nu.equals("3"))
            {
                txtUser.setTextColor(Color.parseColor(color[3]));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                {
                    frame.setBackgroundTintList(context.getResources().getColorStateList(bgcolor[3]));
                }

            }
            else if (nu.equals("4"))
            {
                txtUser.setTextColor(Color.parseColor(color[4]));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                {
                    frame.setBackgroundTintList(context.getResources().getColorStateList(bgcolor[4]));
                }

            }
            else if (nu.equals("5"))
            {
                txtUser.setTextColor(Color.parseColor(color[5]));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                {
                    frame.setBackgroundTintList(context.getResources().getColorStateList(bgcolor[5]));
                }

            }
            else if (nu.equals("6"))
            {
                txtUser.setTextColor(Color.parseColor(color[6]));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                {
                    frame.setBackgroundTintList(context.getResources().getColorStateList(bgcolor[6]));
                }

            }
            else if (nu.equals("7"))
            {
                txtUser.setTextColor(Color.parseColor(color[7]));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                {
                    frame.setBackgroundTintList(context.getResources().getColorStateList(bgcolor[7]));
                }

            }
            else if (nu.equals("8"))
            {
                txtUser.setTextColor(Color.parseColor(color[8]));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                {
                    frame.setBackgroundTintList(context.getResources().getColorStateList(bgcolor[8]));
                }

            }
            else if (nu.equals("9"))
            {
                txtUser.setTextColor(Color.parseColor(color[9]));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                {
                    frame.setBackgroundTintList(context.getResources().getColorStateList(bgcolor[9]));
                }

            }
            else
            {
                txtUser.setTextColor(Color.parseColor(color[10]));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                {
                    frame.setBackgroundTintList(context.getResources().getColorStateList(bgcolor[10]));
                }

            }


            String temp[] = message.getGonderici().split("-");
            String names[]=temp[0].split(" ");
            name.setText(names[0].substring(0,1)+names[1].substring(0,1));
            txtUser.setText(Html.fromHtml("<b>" + temp[0] + "</b>"));
            txtMessage.setText(message.getMesajText());
            String taemp[] = message.getZaman().split(" ");
            txtTime.setText(taemp[1]);

        }

        return convertView;
    }
}