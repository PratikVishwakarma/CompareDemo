package com.example.rajpa.fblogindemo.Adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rajpa.fblogindemo.DatabaseHandler.Single_post;
import com.example.rajpa.fblogindemo.FBDatabase.PostImage;
import com.example.rajpa.fblogindemo.FBDatabase.Users;
import com.example.rajpa.fblogindemo.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by prati on 16-Sep-16.
 */
public class SinglePostAdapter extends RecyclerView.Adapter<SinglePostAdapter.MyViewHolder>{

    private List<Single_post> single_postList;
    private Context context;

    private StorageReference mStorage;
    private FirebaseStorage storage;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView username, like_count, time;
        public ImageView user_image, imageView1, imageView2, imageView3, imageView4,  imageView_like, imageView_liked, imageView_comment,
        imageView1_like, imageView1_liked;

        public MyViewHolder(View view) {
            super(view);
            username = (TextView) view.findViewById(R.id.item_singlePost_username);
            user_image = (ImageView) view.findViewById(R.id.item_singlePost_userImage);
            imageView1 = (ImageView) view.findViewById(R.id.item_singlePost_image1);
            imageView2 = (ImageView) view.findViewById(R.id.item_singlePost_image2);
            imageView3 = (ImageView) view.findViewById(R.id.item_singlePost_image3);
            imageView4 = (ImageView) view.findViewById(R.id.item_singlePost_image4);
            like_count = (TextView) view.findViewById(R.id.item_singlePost_like_count);
            imageView_like = (ImageView) view.findViewById(R.id.item_singlePost_like);
            imageView_liked = (ImageView) view.findViewById(R.id.item_singlePost_liked);
            imageView_comment = (ImageView) view.findViewById(R.id.item_singlePost_comment);
            time = (TextView) view.findViewById(R.id.item_singlePost_time);

            imageView1_like = (ImageView) view.findViewById(R.id.item_singlePost_imageView1_like);
            imageView1_liked = (ImageView) view.findViewById(R.id.item_singlePost_imageView1_liked);
        }
    }

    public SinglePostAdapter(List<Single_post> single_postList, Context context, StorageReference mStorage,FirebaseStorage storage) {
        this.single_postList = single_postList;
        this.context = context;
        this.mStorage = mStorage;
        this.storage = storage;

    }

    @Override
    public SinglePostAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_single_post_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final Single_post single_post = single_postList.get(position);
        holder.imageView_liked.setVisibility(View.GONE);
        holder.imageView1_liked.setVisibility(View.GONE);

        holder.username.setText(single_post.getUserName());
        holder.like_count.setText(String.valueOf(single_post.getPostLike()));
        holder.time.setText(single_post.getDate());
        Log.e("Adapter image url ","image"+single_post.getFirst_image());
        mStorage.child("image").child(single_post.getUser_id()).child(single_post.getFirst_image()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.with(context).load(uri).error(R.drawable.add_image).into(holder.imageView1);
            }
        });
        mStorage.child("image").child(single_post.getUser_id()).child(single_post.getSecond_image()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.with(context).load(uri).error(R.drawable.add_image).into(holder.imageView2);
            }
        });
        mStorage.child("image").child(single_post.getUser_id()).child(single_post.getThird_image()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.with(context).load(uri).error(R.drawable.add_image).into(holder.imageView3);
            }
        });
        if(single_post.getFirst_image().length()==0){

        }else {
            mStorage.child("image").child(single_post.getUser_id()).child(single_post.getFourth_image()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.with(context).load(uri).error(R.drawable.add_image).into(holder.imageView4);
                }
            });
        }
        Picasso.with(context)
                .load(single_post.getProfile_picture())
                .into(holder.user_image);
        holder.imageView_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.imageView_like.setVisibility(View.GONE);
                holder.imageView_liked.setVisibility(View.VISIBLE);
            }
        });
        holder.imageView_liked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.imageView_liked.setVisibility(View.GONE);
                holder.imageView_like.setVisibility(View.VISIBLE);
            }
        });
        holder.imageView1_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.imageView1_like.setVisibility(View.GONE);
                holder.imageView1_liked.setVisibility(View.VISIBLE);
            }
        });
        holder.imageView1_liked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.imageView1_liked.setVisibility(View.GONE);
                holder.imageView1_like.setVisibility(View.VISIBLE);
            }
        });

    }

    @Override
    public int getItemCount() {
        return single_postList.size();
    }
}
