package com.cosw.easyaccess;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.ImageView;


public class MainActivity extends AppCompatActivity {
    EditText t= null;
    ImageView iv=null;
    private String imagePath;
    final int REQUEST_CAMERA = 1;
    final int SELECT_FILE = 2;
    final CharSequence[] items = {"Take Photo", "Choose From Gallery"};
    Bitmap imageSelected;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(savedInstanceState==null)showFragment(new NewPostFragment(), true);
    }

    @Override
    public void onBackPressed(){
        Fragment newpostfrag = new NewPostFragment();
        showFragment(newpostfrag,true);
    }

    public void showFragment(Fragment fragment, boolean addToBackStack)
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        String tag = fragment.getClass().getSimpleName();
        if ( addToBackStack )
        {
            transaction.addToBackStack( tag );
        }
        transaction.replace( R.id.fragment_container, fragment, tag );
        transaction.commitAllowingStateLoss();
    }



}
