package com.example.rajpa.fblogindemo.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rajpa.fblogindemo.FBDatabase.Users;
import com.example.rajpa.fblogindemo.OtherUserProfile;
import com.example.rajpa.fblogindemo.R;
import com.example.rajpa.fblogindemo.UserProfile;
import com.squareup.picasso.Picasso;


import java.util.List;


/**
 * Created by prati on 30-Aug-16.
 */
public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder>
{
    private List<Users> userList;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView username, name;
        public ImageView user_image;

        public MyViewHolder(View view) {
            super(view);
            username = (TextView) view.findViewById(R.id.item_search_username);
            name = (TextView) view.findViewById(R.id.item_search_name);
            user_image = (ImageView) view.findViewById(R.id.item_search_image);
        }
    }

    public UserAdapter(List<Users> userList, Context context) {
        this.userList = userList;
        this.context = context;
    }


    @Override
    public UserAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_search_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(UserAdapter.MyViewHolder holder, int position) {
        final Users users = userList.get(position);
        holder.username.setText(users.getUsername());
        holder.name.setText(users.getName());
        Picasso.with(context)
                .load(users.getProfile_picture())
                .into(holder.user_image);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, OtherUserProfile.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("other_user_id", users.getId());
                context.getApplicationContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }
}
