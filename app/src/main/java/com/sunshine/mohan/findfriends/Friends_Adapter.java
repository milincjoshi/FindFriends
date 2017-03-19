package com.sunshine.mohan.findfriends;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by mohan on 6/7/16.
 */
public class Friends_Adapter extends ArrayAdapter<Friend>{

        ArrayList<Friend> friends_list = null;
        Context context;

        public Friends_Adapter(Context context, ArrayList<Friend> resource) {
            super(context,R.layout.single_friend_item,resource);

            this.context = context;
            this.friends_list = resource;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            convertView = inflater.inflate(R.layout.single_friend_item, parent, false);

            TextView friend_first_name = (TextView) convertView.findViewById(R.id.friend_first_name);
            TextView friend_last_name = (TextView) convertView.findViewById(R.id.friend_last_name);
            TextView friend_email = (TextView) convertView.findViewById(R.id.friend_email);
            TextView friend_number = (TextView) convertView.findViewById(R.id.friend_number);
            TextView friend_distance = (TextView) convertView.findViewById(R.id.distance);

            //
            TextView friend_lat = (TextView) convertView.findViewById(R.id.friend_lat);
            TextView friend_lon = (TextView) convertView.findViewById(R.id.friend_lon);
            //
            //CheckBox cb = (CheckBox) convertView.findViewById(R.id.checkBox1);

            friend_first_name.setText(friends_list.get(position).getFirst_name());
            friend_last_name.setText(friends_list.get(position).getLast_name());
            friend_email.setText(friends_list.get(position).getEmail());
            friend_number.setText(String.valueOf(friends_list.get(position).getNumber()));
            friend_distance.setText(String.valueOf(friends_list.get(position).getDistance()));
            friend_lat.setText(String.valueOf(friends_list.get(position).getLat()));
            friend_lon.setText(String.valueOf(friends_list.get(position).getLon()));

            //name.setText(modelItems.get(position).getName());
            //int checked = modelItems.get(position).getValue();
            //if(checked == 1){
            //    cb.setChecked(true);
            //}
            // if(modelItems.get(position).getValue() == 1)
            //     cb.setChecked(true);
            // else
            //     cb.setChecked(false);

            return convertView;
        }
}
