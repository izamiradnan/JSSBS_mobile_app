package com.example.jssbs.ViewHolder;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.jssbs.Model.User;
import com.example.jssbs.R;

import java.util.List;

public class UserList extends ArrayAdapter<User> {
    private Activity context;
    private List<User> userList;


    public UserList(Activity context, List<User> userList){
        super(context, R.layout.activity_delete_user, userList);
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.activity_delete_user,null,true);

        TextView textViewName = listViewItem.findViewById(R.id.textViewName);
        TextView textViewStudentId = listViewItem.findViewById(R.id.textViewStudentId);

        User user = userList.get(position);

        textViewName.setText(user.getName());
        textViewStudentId.setText(user.getStudentID());

        return listViewItem;
    }
}
