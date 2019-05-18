package com.cosw.easyaccess;

/**
 * Created by martin on 19/03/18.
 */


import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

public class Post implements Parcelable {
    
    private String email;
    private String password;
    private Bitmap imageUri;

    public Post(String email, String password, Bitmap imageUri){
        this.email=email;
        this.imageUri=imageUri;
        this.password=password;
    }

    public Post(){}

    public String getEmail(){return this.email;}
    public Bitmap getImageUri(){return this.imageUri;}
    public void setEmail(String m){this.email=m;}
    public void setImageUri(Bitmap i){this.imageUri=i;}
    public String getPassword() { return password;}
    public void setPassword(String password) { this.password = password;}

    protected Post(Parcel in) {
        email = in.readString();
        password = in.readString();
        imageUri = in.readParcelable(Bitmap.class.getClassLoader());
    }

    public static final Creator<Post> CREATOR = new Creator<Post>() {
        @Override
        public Post createFromParcel(Parcel in) {
            return new Post(in);
        }

        @Override
        public Post[] newArray(int size) {
            return new Post[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(email);
        dest.writeString(password);
        dest.writeParcelable(imageUri, flags);
    }


}
