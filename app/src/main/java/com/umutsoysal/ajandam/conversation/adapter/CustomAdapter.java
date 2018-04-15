package com.umutsoysal.ajandam.conversation.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.umutsoysal.ajandam.R;
import com.umutsoysal.ajandam.conversation.model.Message;

import java.util.ArrayList;


public class CustomAdapter extends ArrayAdapter<Message>
{

    private String username;

    public CustomAdapter(Context context, ArrayList<Message> chatList, String username)
    {
        super(context, 0, chatList);
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

            String color[] = new String[]{"#0095ff", "#ed5d5d", "#ed5d5d", "#f38d46", "#5fba7d", "#efc2e6", "#efc864", "#a0514d", "#81c6c1", "#e280d0", "#8bc34a"};

            String nu = message.getGonderici().substring(message.getGonderici().length() - 1);
            if (nu.equals("0"))
            {
                txtUser.setTextColor(Color.parseColor(color[0]));

            }
            else if (nu.equals("1"))
            {
                txtUser.setTextColor(Color.parseColor(color[1]));

            }
            else if (nu.equals("2"))
            {
                txtUser.setTextColor(Color.parseColor(color[2]));

            }
            else if (nu.equals("3"))
            {
                txtUser.setTextColor(Color.parseColor(color[3]));

            }
            else if (nu.equals("4"))
            {
                txtUser.setTextColor(Color.parseColor(color[4]));

            }
            else if (nu.equals("5"))
            {
                txtUser.setTextColor(Color.parseColor(color[5]));

            }
            else if (nu.equals("6"))
            {
                txtUser.setTextColor(Color.parseColor(color[6]));

            }
            else if (nu.equals("7"))
            {
                txtUser.setTextColor(Color.parseColor(color[7]));

            }
            else if (nu.equals("8"))
            {
                txtUser.setTextColor(Color.parseColor(color[8]));

            }
            else if (nu.equals("9"))
            {
                txtUser.setTextColor(Color.parseColor(color[9]));

            }
            else
            {
                txtUser.setTextColor(Color.parseColor(color[0]));

            }


            String temp[] = message.getGonderici().split("-");
            txtUser.setText(Html.fromHtml("<b>" + temp[0] + "</b>"));
            txtMessage.setText(message.getMesajText());
            String taemp[] = message.getZaman().split(" ");
            txtTime.setText(taemp[1]);

        }

        return convertView;
    }
}