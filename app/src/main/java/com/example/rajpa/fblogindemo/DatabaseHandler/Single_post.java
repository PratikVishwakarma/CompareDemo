package com.example.rajpa.fblogindemo.DatabaseHandler;

/**
 * Created by prati on 16-Sep-16.
 */
public class Single_post {
    private String user_id, post_id, userName, profile_picture, time, date, content,
            first_image, second_image, third_image, fourth_image;
    private int total_image, postlike, first_like, second_like, third_like, fourth_like;

    public Single_post() {
    }

    public Single_post(String user_id, String post_id, String userName, String profile_picture, String time, String date, String content,
                       int postlike, String first_image, String second_image, String third_image, String fourth_image, int total_image,
                       int first_like, int second_like, int third_like, int fourth_like) {
        this.user_id = user_id;
        this.post_id = post_id;
        this.userName = userName;
        this.profile_picture = profile_picture;
        this.time = time;
        this.date = date;
        this.content = content;
        this.postlike= postlike;
        this.first_image = first_image;
        this.second_image = second_image;
        this.third_image = third_image;
        this.fourth_image = fourth_image;
        this.total_image = total_image;
        this.first_like = first_like;
        this.second_like = second_like;
        this.third_like = third_like;
        this.fourth_like = fourth_like;
    }

    public String getProfile_picture() {
        return profile_picture;
    }

    public void setProfile_picture(String profile_picture) {
        this.profile_picture = profile_picture;
    }

    public int getPostlike() {
        return postlike;
    }

    public void setPostlike(int postlike) {
        this.postlike = postlike;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getPostLike() {
        return postlike;
    }

    public void setPostLike(int like) {
        this.postlike = like;
    }

    public String getFirst_image() {
        return first_image;
    }

    public void setFirst_image(String first_image) {
        this.first_image = first_image;
    }

    public String getSecond_image() {
        return second_image;
    }

    public void setSecond_image(String second_image) {
        this.second_image = second_image;
    }

    public String getThird_image() {
        return third_image;
    }

    public void setThird_image(String third_image) {
        this.third_image = third_image;
    }

    public String getFourth_image() {
        return fourth_image;
    }

    public void setFourth_image(String fourth_image) {
        this.fourth_image = fourth_image;
    }

    public int getTotal_image() {
        return total_image;
    }

    public void setTotal_image(int total_image) {
        this.total_image = total_image;
    }

    public int getFirst_like() {
        return first_like;
    }

    public void setFirst_like(int first_like) {
        this.first_like = first_like;
    }

    public int getSecond_like() {
        return second_like;
    }

    public void setSecond_like(int second_like) {
        this.second_like = second_like;
    }

    public int getThird_like() {
        return third_like;
    }

    public void setThird_like(int third_like) {
        this.third_like = third_like;
    }

    public int getFourth_like() {
        return fourth_like;
    }

    public void setFourth_like(int fourth_like) {
        this.fourth_like = fourth_like;
    }
}
