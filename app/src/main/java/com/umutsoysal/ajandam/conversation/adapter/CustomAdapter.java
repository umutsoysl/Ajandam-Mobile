package com.umutsoysal.ajandam.conversation.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import com.umutsoysal.ajandam.R;
import com.umutsoysal.ajandam.conversation.model.Message;
import de.hdodenhof.circleimageview.CircleImageView;
import hani.momanii.supernova_emoji_library.Helper.EmojiconTextView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;


public class CustomAdapter extends ArrayAdapter<Message>
{

    private String username;
    Context context;
    public static  int width;
    public static  int height;
    Dialog dialog;

    public CustomAdapter(Context context, ArrayList<Message> chatList, String username)
    {
        super(context, 0, chatList);
        this.context = context;
        this.username = username;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {

        final Message message = getItem(position);
        width= context.getResources().getDisplayMetrics().widthPixels;
        height= context.getResources().getDisplayMetrics().heightPixels;
        if (username.equalsIgnoreCase(message.getGonderici()))
        {

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.right_item_layout, parent, false);

            // TextView txtUser = (TextView) convertView.findViewById(R.id.username);
            TextView txtMessage = (TextView) convertView.findViewById(R.id.content);
            TextView txtTime = (TextView) convertView.findViewById(R.id.time);
            ImageView resfoto=(ImageView) convertView.findViewById(R.id.images);
            txtMessage.setMovementMethod(LinkMovementMethod.getInstance());

            resfoto.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    image_show(context,message.getUri(),message.getZaman());
                }
            });


            if(message.getUri()!=null&&message.getUri().length()>5)
            {

                resfoto.setVisibility(View.VISIBLE);
                txtMessage.setVisibility(View.GONE);
                Picasso.with(context).load(Uri.parse(message.getUri())).centerCrop().centerCrop().resize(((width /2)+200 ) , ((height /3))).into(resfoto);
            }
            else{
                resfoto.setVisibility(View.GONE);
                txtMessage.setVisibility(View.VISIBLE);
            }
            //  txtUser.setText(message.getGonderici());
            txtMessage.setText(message.getMesajText());
            String taemp[] = message.getZaman().split(" ");
            txtTime.setText(taemp[1]);

        }
        else
        {

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.left_item_layout, parent, false);

            TextView txtUser = (TextView) convertView.findViewById(R.id.username);
            EmojiconTextView txtMessage = (EmojiconTextView) convertView.findViewById(R.id.content);
            TextView txtTime = (TextView) convertView.findViewById(R.id.time);
            RelativeLayout frame = (RelativeLayout) convertView.findViewById(R.id.frame);
            TextView name = (TextView) convertView.findViewById(R.id.name);
            CircleImageView profile=(CircleImageView)convertView.findViewById(R.id.profile_image);
            ImageView resfoto=(ImageView) convertView.findViewById(R.id.images);
            txtMessage.setMovementMethod(LinkMovementMethod.getInstance());





            profile.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    image_show(context,message.getGondericiPP(),message.getGonderici());
                }
            });


            resfoto.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                   image_show(context,message.getUri(),message.getZaman());
                }
            });

            if(message.getUri()!=null&&message.getUri().length()>5)
            {
                resfoto.setVisibility(View.VISIBLE);
                txtMessage.setVisibility(View.GONE);
                Picasso.with(context).load(Uri.parse(message.getUri())).centerCrop().centerCrop().resize(((width / 2)+200 ) , ((height /3))).into(resfoto);
            }
            else{
                resfoto.setVisibility(View.GONE);
                txtMessage.setVisibility(View.VISIBLE);
            }

            if(message.getGondericiPP()!=null&&message.getGondericiPP().length()>5)
            {
                profile.setVisibility(View.VISIBLE);
                frame.setVisibility(View.GONE);
                Picasso.with(context).load(message.getGondericiPP()).resize(200,200).into(profile);
            }else
            {
                profile.setVisibility(View.GONE);
                frame.setVisibility(View.VISIBLE);

                String color[] = new String[]{"#0095ff", "#ed5d5d", "#9a4bed", "#f38d46", "#5fba7d", "#b7701b", "#dfb956", "#a0514d", "#81c6c1", "#e280d0", "#8bc34a"};
                int bgcolor[] = new int[]{R.color.chat1, R.color.chat2, R.color.chat3, R.color.chat4, R.color.chat5, R.color.chat6, R.color.chat7, R.color.chat8, R.color.chat9, R.color.chat10, R.color.chat11};
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
            }


            if (position - 1 >= 0)
            {
                Message message2 = getItem(position - 1);
                if (message.getGonderici().equals(message2.getGonderici()))
                {
                    frame.setVisibility(View.GONE);
                    profile.setVisibility(View.GONE);
                }
                else
                {
                    if(message.getGondericiPP()!=null&&message.getGondericiPP().length()>5)
                    {
                        profile.setVisibility(View.VISIBLE);
                        frame.setVisibility(View.GONE);
                        Picasso.with(context).load(message.getGondericiPP()).resize(200,200).into(profile);
                    }else
                    {
                        frame.setVisibility(View.VISIBLE);
                        profile.setVisibility(View.GONE);
                    }
                }
            }
            else
            {
                if(message.getGondericiPP()!=null&&message.getGondericiPP().length()>5)
                {
                    profile.setVisibility(View.VISIBLE);
                    frame.setVisibility(View.GONE);
                    Picasso.with(context).load(message.getGondericiPP()).resize(200,200).into(profile);
                }else
                {
                    frame.setVisibility(View.VISIBLE);
                    profile.setVisibility(View.GONE);
                }
            }


            String temp[] = message.getGonderici().split("-");
            String names[] = temp[0].split(" ");
            name.setText(names[0].substring(0, 1) + names[1].substring(0, 1));
            txtUser.setText(Html.fromHtml("<b>" + temp[0] + "</b>"));
            txtMessage.setText(message.getMesajText());
            String taemp[] = message.getZaman().split(" ");
            txtTime.setText(taemp[1]);

        }

        return convertView;
    }

    public void image_show(Context context1,final String uri,final String time)
    {
        dialog = new Dialog(context1,android.R.style.Theme_Light);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.image_show);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        Window window = dialog.getWindow();
        WindowManager.LayoutParams param = window.getAttributes();
        param.gravity = Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL;
        dialog.setCanceledOnTouchOutside(true);
        final View dis =(View) dialog.findViewById(R.id.arka_layout2);
        final ImageView rem2=(ImageView) dialog.findViewById(R.id.view_resim);
        Picasso.with(context).load(Uri.parse(uri)).centerCrop().centerCrop().resize(((width / 2) ) , ((height /3))).into(rem2);
        final ImageButton close=(ImageButton) dialog.findViewById(R.id.geri);
        final TextView page_date=(TextView) dialog.findViewById(R.id.image_create_date);

        page_date.setText(time);

        dis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }



}