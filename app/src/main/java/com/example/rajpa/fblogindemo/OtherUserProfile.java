package com.example.rajpa.fblogindemo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rajpa.fblogindemo.Adapter.SinglePostAdapter;
import com.example.rajpa.fblogindemo.DatabaseHandler.Single_post;
import com.example.rajpa.fblogindemo.FBDatabase.Post;
import com.example.rajpa.fblogindemo.FBDatabase.PostImage;
import com.example.rajpa.fblogindemo.FBDatabase.Users;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class OtherUserProfile extends AppCompatActivity {

    private static SharedPreferences usersharedpref;
    SharedPreferences.Editor editor;
    private static String USER_ID = "user_id_key";
    private static Boolean LOGIN_STATUS = Boolean.FALSE;
    private static Boolean USERNAME_STATUS = Boolean.FALSE;

    Firebase rootLinkRef = new Firebase("https://facebooklogindemo-8f640.firebaseio.com");
    private FirebaseStorage storage;
    private StorageReference mStorage;

    RecyclerView single_post_RecyclerView;
    private SinglePostAdapter spAdapter;
    private List<Single_post> singlePostList = new ArrayList<>();

    private static final String TAG = "AddPost ";

    public static Users users = new Users();
    public static Post P_post = new Post();
    public Single_post P_single_post = new Single_post();
    public Single_post spp;
    public static PostImage postImageValue;

    private static String user_id = null, other_user_id = null, Uusername = null, Uprofile_picture;
    private static long post_count = 0, post_counter = 0;
    private ImageView profile_picture;
    private TextView username, name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_user_profile);

        storage = FirebaseStorage.getInstance();
        mStorage = storage.getReferenceFromUrl("gs://facebooklogindemo-8f640.appspot.com");
        usersharedpref = PreferenceManager.getDefaultSharedPreferences(this);

        username = (TextView) findViewById(R.id.text_username);
        name = (TextView) findViewById(R.id.text_name);
        profile_picture = (ImageView) findViewById(R.id.otheruserprofile_user_image);
        single_post_RecyclerView = (RecyclerView)findViewById(R.id.recyclerView_retrive_other_user_post);

        Intent intent = new Intent();
        Bundle extras = getIntent().getExtras();
        user_id = usersharedpref.getString(USER_ID,"");
        other_user_id = extras.getString("other_user_id");

        spAdapter = new SinglePostAdapter(singlePostList,getApplicationContext(),mStorage, storage);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        single_post_RecyclerView.setLayoutManager(layoutManager);
        single_post_RecyclerView.setItemAnimator(new DefaultItemAnimator());
        single_post_RecyclerView.setAdapter(spAdapter);

        //singlePostList.clear();
        //spAdapter.notifyDataSetChanged();
        getUser(other_user_id);
        //fetch_all_post(user_id);

    }

    public void getUser(final String o_user_id){
        final Query query = rootLinkRef.child(Users.TABLE_NAME).child(o_user_id);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    users = dataSnapshot.getValue(Users.class);
                    username.setText(users.getUsername());
                    Uusername = users.getUsername();
                    Uprofile_picture = users.getProfile_picture();
                    name.setText(users.getName());
                    Picasso.with(getApplicationContext())
                            .load(Uprofile_picture)
                            .into(profile_picture);
                    Log.e("Inside getUser found", users.getUsername());
                    fetch_all_post(user_id);
                }else{
                    users = null;
                    Log.e("not found", users.getId());
                }
                query.removeEventListener(this);
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
                query.removeEventListener(this);
            }
        });
    }

    public void fetch_all_post(final String user_id){
        Log.e("data ", " fetch_all_post called with "+user_id);
        Query postQuery = rootLinkRef.child(Post.TABLE_NAME).child(user_id);
        postQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    post_count = dataSnapshot.getChildrenCount();
                    post_counter = 0;
                    singlePostList.clear();
                    spAdapter.notifyDataSetChanged();
                    for(DataSnapshot userdatasnapshot: dataSnapshot.getChildren()){
                        final Post postValue = userdatasnapshot.getValue(Post.class);
                        Query postImageQuery = rootLinkRef.child(PostImage.TABLE_NAME).child(postValue.getUser_id()).child(postValue.getPost_id());
                        postImageQuery.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                int i=1;
                                if(dataSnapshot.exists()){
                                    dataSnapshot.getChildrenCount();
                                    Log.e("inside postImage ", "data exist..");
                                    post_counter++;
                                    P_single_post.setPost_id(postValue.getPost_id());
                                    P_single_post.setUser_id(postValue.getUser_id());
                                    P_single_post.setUserName(Uusername);
                                    P_single_post.setProfile_picture(Uprofile_picture);
                                    P_single_post.setTime(postValue.getTime());
                                    P_single_post.setDate(postValue.getDate());
                                    P_single_post.setContent(postValue.getContent());
                                    P_single_post.setPostLike(postValue.getLike());
                                    P_single_post.setTotal_image(postValue.getTotal_image());
                                    for(DataSnapshot postImagedatasnapshot: dataSnapshot.getChildren()){
                                        Log.e("value of ", i+"");
                                        final PostImage postImageValue = postImagedatasnapshot.getValue(PostImage.class);
                                        if(i == 1){
                                            String s = String.valueOf(i - 1);
                                            String imageUrl = P_single_post.getPost_id()+"_img_"+s+".jpg";
                                            P_single_post.setFirst_image(imageUrl);
                                            P_single_post.setFirst_like(postImageValue.getLike());
                                            Log.e("value of ", P_single_post.getFirst_image());
                                        } else if(i == 2){
                                            String s = String.valueOf(i - 1);
                                            String imageUrl = P_single_post.getPost_id()+"_img_"+s+".jpg";
                                            P_single_post.setSecond_image(imageUrl);
                                            P_single_post.setSecond_like(postImageValue.getLike());
                                            Log.e("value of ", P_single_post.getSecond_image());
                                        }  else if(i == 3){
                                            String s = String.valueOf(i - 1);
                                            String imageUrl = P_single_post.getPost_id()+"_img_"+s+".jpg";
                                            P_single_post.setThird_image(imageUrl);
                                            P_single_post.setThird_like(postImageValue.getLike());
                                            Log.e("value of ", P_single_post.getThird_image());
                                        }  else if(i == 4){
                                            String s = String.valueOf(i - 1);
                                            String imageUrl = P_single_post.getPost_id()+"_img_"+s+".jpg";
                                            P_single_post.setFourth_image(imageUrl);
                                            P_single_post.setFourth_like(postImageValue.getLike());
                                            Log.e("value of ", P_single_post.getFourth_image());
                                        }
                                        i++;
                                        Query likePostQuery = rootLinkRef.child(PostImage.TABLE_NAME).child(postValue.getUser_id()).child(postValue.getPost_id());

                                    }
                                    spp = new Single_post(P_single_post.getUser_id(), P_single_post.getPost_id(), Uusername, Uprofile_picture,
                                            P_single_post.getTime(), P_single_post.getDate(), P_single_post.getContent(), P_single_post.getPostLike(),
                                            P_single_post.getFirst_image(), P_single_post.getSecond_image(), P_single_post.getThird_image(), P_single_post.getFourth_image(),
                                            P_single_post.getTotal_image(), P_single_post.getFirst_like(), P_single_post.getSecond_like(),
                                            P_single_post.getThird_like(), P_single_post.getFourth_like());
//                                    P_single_post = new Single_post(postValue.getUser_id(), postValue.getPost_id(), Uusername, Uprofile_picture, postValue.getTime(),
//                                            postValue.getDate(), postValue.getContent(), postValue.getLike(), postImageV);
                                    Log.e("date ", String.valueOf(P_single_post.getDate()));
                                    Log.e("PostId ", String.valueOf(P_single_post.getPost_id()));
                                    singlePostList.add(spp);
                                    Log.e("data counter ", String.valueOf(post_count)+ " total counters "+String.valueOf(post_counter)+" adapter size "+singlePostList.size()+"");
                                    if(post_counter <= post_count){
                                        Log.e("data counter ", " notify adapter "+String.valueOf(post_counter));
                                        spAdapter.notifyDataSetChanged();
                                    }
                                    //spAdapter.notifyDataSetChanged();
                                }else{
                                    Log.e("data ", " else of postImage");
                                }
                            }
                            @Override
                            public void onCancelled(FirebaseError firebaseError) {
                            }
                        });
                    }
                }else {
                    Log.e("data ", " else of Post");
                }
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });
    }
}
