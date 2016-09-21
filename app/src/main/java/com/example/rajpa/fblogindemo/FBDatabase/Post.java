package com.example.rajpa.fblogindemo.FBDatabase;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by prati on 05-Sep-16.
 */
public class Post {
//    Firebase postLinkRef = new Firebase("https://facebooklogindemo-8f640.firebaseio.com/post");
    public static final String TABLE_NAME = "Post";
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_POST_ID = "post_id";
    public static final String COLUMN_TOTAL_IMAGE = "total_image";
    public static final String COLUMN_CONTENT = "content";
    public static final String COLUMN_TIME = "time";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_LIKE = "like";
//    public static Post Post = new Post();
//    public static boolean status = false;
//    private List<Post> postList = new ArrayList<>();

    public String user_id;
    public String post_id;
    public String content;
    public String time;
    public String date;
    public int total_image, like;
    public Post() {
    }

    public Post(String user_id, String post_id, String content, String time, String date, int total_image, int like) {
        this.user_id = user_id;
        this.post_id = post_id;
        this.content = content;
        this.time = time;
        this.date = date;
        this.total_image = total_image;
        this.like = like;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getTotal_image() {
        return total_image;
    }

    public void setTotal_image(int total_image) {
        this.total_image = total_image;
    }

    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }

//    public List<Post> getAllPost(String uid){
//        Query query = postLinkRef.child(uid);
//        query.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                if (dataSnapshot.exists()){
//                    for(DataSnapshot postdatasnapshot: dataSnapshot.getChildren()){
//                        Post = postdatasnapshot.getValue(Post.class);
//                        postList.add(Post);
//                    }
//                }else{
//                    Post = null;
//                }
//            }
//            @Override
//            public void onCancelled(FirebaseError firebaseError) {
//
//            }
//        });
//        return postList;
//    }
//
//    public boolean addNewPost(String uid, Post Post){
//        Query query = postLinkRef.child(uid);
//        postLinkRef.push().setValue(Post);
//        return status;
//    }
//
}
