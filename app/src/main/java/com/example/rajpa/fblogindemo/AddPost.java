package com.example.rajpa.fblogindemo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.rajpa.fblogindemo.FBDatabase.PostImage;
import com.example.rajpa.fblogindemo.FBDatabase.Users;
import com.example.rajpa.fblogindemo.FBDatabase.Post;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddPost extends AppCompatActivity {

    private static SharedPreferences usersharedpref;
    private static String USER_ID = "user_id_key";

    Firebase rootLinkRef = new Firebase("https://facebooklogindemo-8f640.firebaseio.com");
    private StorageReference mStorage;
    private FirebaseStorage storage;

    private static final String TAG = "AddPost ";

    public static Users users = new Users();

    private static String uid = null, postid = null, content_post = "n1o_g5eo8ja_81sdaf5";
    private Button upload_image, button_search;
    private ImageView imageView_1, imageView_2, imageView_3, imageView_4;
    private EditText post_content;
    public static final int GALLERY_INTENT_1 = 1,GALLERY_INTENT_2 = 2, GALLERY_INTENT_3 = 3,GALLERY_INTENT_4 = 4;
    private static boolean IMAGE_UPLOAD_STATUS = false;
    Bitmap bitmap1 = null, bitmap2 = null, bitmap3 = null, bitmap4 = null;
    List<String> imageNameList;
    public static Uri uri1 = null, uri2 = null, uri3 = null, uri4 = null;
    public ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        storage = FirebaseStorage.getInstance();
        mStorage = FirebaseStorage.getInstance().getReference();
        setContentView(R.layout.activity_add_post);
        usersharedpref = PreferenceManager.getDefaultSharedPreferences(this);
        uid = (usersharedpref.getString(USER_ID,""));
        upload_image = (Button) findViewById(R.id.button_upload_image);
        button_search = (Button) findViewById(R.id.button_search);
        imageView_1 = (ImageView) findViewById(R.id.imageView_1);
        imageView_2 = (ImageView) findViewById(R.id.imageView_2);
        imageView_3 = (ImageView) findViewById(R.id.imageView_3);
        imageView_4 = (ImageView) findViewById(R.id.imageView_4);
        progressDialog = new ProgressDialog(this);

        button_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Search.class);
                startActivity(intent);
            }
        });

        imageNameList = new ArrayList<String>();
        imageView_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.putExtra("crop", "true");
                intent.putExtra("aspectX", 1);
                intent.putExtra("aspectY", 1);
                intent.putExtra("outputX", 200);
                intent.putExtra("outputY", 200);
                intent.putExtra("return-data", true);
                intent.setType("image/*");
                startActivityForResult(intent, GALLERY_INTENT_1);
            }
        });
        imageView_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.putExtra("crop","true");
                intent.putExtra("aspectX",1);
                intent.putExtra("aspectY",1);
                intent.putExtra("outputX",200);
                intent.putExtra("outputY",200);
                intent.putExtra("return-data",true);
                intent.setType("image/*");
                startActivityForResult(intent, GALLERY_INTENT_2);
            }
        });
        imageView_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.putExtra("crop","true");
                intent.putExtra("aspectX",1);
                intent.putExtra("aspectY",1);
                intent.putExtra("outputX",200);
                intent.putExtra("outputY",200);
                intent.putExtra("return-data",true);
                intent.setType("image/*");
                startActivityForResult(intent, GALLERY_INTENT_3);
            }
        });
        imageView_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.putExtra("crop","true");
                intent.putExtra("aspectX",1);
                intent.putExtra("aspectY",1);
                intent.putExtra("outputX",200);
                intent.putExtra("outputY",200);
                intent.putExtra("return-data",true);
                intent.setType("image/*");
                startActivityForResult(intent, GALLERY_INTENT_4);
            }
        });
        upload_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upload_image.setVisibility(View.INVISIBLE);
                post_content= (EditText)findViewById(R.id.post_content);
                content_post = post_content.getText().toString();
                createPostId();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_INTENT_1 && resultCode == RESULT_OK && data != null){
            uri1 = data.getData();
            imageView_1.setImageURI(null);
            imageView_1.setImageURI(uri1);
            bitmap1 = ((BitmapDrawable)imageView_1.getDrawable()).getBitmap();
        } else if(requestCode == GALLERY_INTENT_2 && resultCode == RESULT_OK && data!=null){
            uri2 = data.getData();
            imageView_2.setImageURI(null);
            imageView_2.setImageURI(uri2);
            bitmap2 = ((BitmapDrawable)imageView_2.getDrawable()).getBitmap();
        } else if(requestCode == GALLERY_INTENT_3 && resultCode == RESULT_OK){
            uri3 = data.getData();
            imageView_3.setImageURI(null);
            imageView_3.setImageURI(uri3);
            bitmap3 = ((BitmapDrawable)imageView_3.getDrawable()).getBitmap();
        } else if(requestCode == GALLERY_INTENT_4 && resultCode == RESULT_OK){
            uri4 = data.getData();
            imageView_4.setImageURI(null);
            imageView_4.setImageURI(uri4);
            bitmap4 = ((BitmapDrawable)imageView_4.getDrawable()).getBitmap();
        }
    }
    public void upload_images(String getpostid) {
        postid = getpostid;
        final Uri[] auri = {uri1, uri2, uri3, uri4};
        final Bitmap[] abitmaps = {bitmap1, bitmap2, bitmap3, bitmap4};

        progressDialog.setMessage("Uploading...");
        progressDialog.show();

        for (int i = 0; i < 4; i++) {
            if (auri[i] != null) {
                final String imageName = postid + "_img_" + i + ".jpg";
                final String postImageId = postid + "_img_" + i;
                StorageReference mainStorage = storage.getReferenceFromUrl("gs://facebooklogindemo-8f640.appspot.com");
                StorageReference photoRef = mainStorage.child("image").child(uid).child(imageName);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                abitmaps[i].compress(Bitmap.CompressFormat.JPEG, 75, baos);
                byte[] data = baos.toByteArray();
                UploadTask uploadTask = photoRef.putBytes(data);
                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        imageNameList.add(imageName);
                        Log.e("Postid ", postid);
                        Toast.makeText(getApplicationContext(), "Update the Post....", Toast.LENGTH_SHORT).show();
                        DateFormat datef = new SimpleDateFormat("d-MMM-yyyy");
                        String currentdate = datef.format(Calendar.getInstance().getTime());
                        DateFormat timef = new SimpleDateFormat("HH:mm");
                        String currenttime = timef.format(Calendar.getInstance().getTime());
                        Post newPost = new Post(uid, postid, content_post, currentdate, currenttime, imageNameList.size(), 0);
                        rootLinkRef.child(Post.TABLE_NAME).child(uid).child(postid).setValue(newPost);
                        PostImage postImage = new PostImage(uid, postid, imageName, 0);
                        rootLinkRef.child(PostImage.TABLE_NAME).child(uid).child(postid).child(postImageId).setValue(postImage);
                        Toast.makeText(getApplicationContext(), "Upload complete....", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
            }
        }
        progressDialog.dismiss();
    }

    public void createPostId(){
        final Query query = rootLinkRef.child(Users.TABLE_NAME).child(uid);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e("Query is", query.toString());
                Log.e("Uid is", uid);
                if (dataSnapshot.exists()){
                    long time= System.currentTimeMillis();
                    users = dataSnapshot.getValue(Users.class);
                    Log.e("Inside getUser found", users.getUsername()+time);
                    postid = users.getUsername()+time;
                    upload_images(postid);
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
}
