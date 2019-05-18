package com.cosw.easyaccess;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class NewPostFragment extends Fragment {
    EditText t= null;
    ImageView iv=null;
    private String imagePath;
    final int REQUEST_CAMERA = 1;
    final int SELECT_FILE = 2;
    final CharSequence[] items = {"Take Photo", "Choose From Gallery"};
    Bitmap imageSelected;
    Context applicationContext;
    private View v;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    public String currentPhotoPath;

    public NewPostFragment() {
    }


    public static NewPostFragment newInstance(String param1, String param2) {
        NewPostFragment fragment = new NewPostFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.v =inflater.inflate(R.layout.fragment_new_post, container, false);

        this.t= this.v.findViewById(R.id.editText3);
        this.iv=this.v.findViewById(R.id.imageView3);

        final Button button3 = this.v.findViewById(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                photo(v);
            }
        });


        final Button button4 = this.v.findViewById(R.id.button4);
        button4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                save(v);
            }
        });

        return this.v;
    }




    @NonNull
    public static Dialog createSingleChoiceAlertDialog(@NonNull Context context, @Nullable String title,
                                                       @Nullable CharSequence[] items,
                                                       @NonNull DialogInterface.OnClickListener optionSelectedListener,
                                                       @Nullable DialogInterface.OnClickListener cancelListener )
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setItems( items, optionSelectedListener );
        if ( cancelListener != null ) {
            builder.setNegativeButton("Cancel", cancelListener );
        }
        builder.setTitle( title );
        return builder.create();
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch(requestCode) {
            case REQUEST_CAMERA:
                if(resultCode == -1){

                    Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath);
                    //Bitmap bitmap = (Bitmap) imageReturnedIntent.getExtras().get("data");
                    iv.setImageBitmap(bitmap);

                    /*final float densityMultiplier = this.getResources().getDisplayMetrics().density;
                    int h= (int) (100*densityMultiplier);
                    int w= (int) (h * bitmap.getWidth()/((double) bitmap.getHeight()));
                    bitmap= Bitmap.createScaledBitmap(bitmap, w, h, true);
                    */
                    imageSelected=bitmap;
                    break;
                }

                break;
            case SELECT_FILE:
                if(resultCode == -1){
                    try{
                        Uri imageUri = imageReturnedIntent.getData();
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
                        iv.setImageBitmap(bitmap);

                        /*
                        final float densityMultiplier = this.getResources().getDisplayMetrics().density;
                        int h= (int) (100*densityMultiplier);
                        int w= (int) (h * bitmap.getWidth()/((double) bitmap.getHeight()));
                        bitmap= Bitmap.createScaledBitmap(bitmap, w, h, true);
                         */
                        imageSelected=bitmap;
                        break;
                    }catch(Exception e){}

                }
                break;
        }
    }

    public void save(View v){
        if(validation()){
            EditText editText_email = this.v.findViewById(R.id.editText3);
            EditText editText_password = this.v.findViewById(R.id.editText4);
            String email = editText_email.getText().toString();
            String password = editText_password.getText().toString();
            Post p =new Post(email, password, imageSelected);
            PostFragment postfragment= new PostFragment();
            postfragment.setP(p);

            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            String tag =postfragment.getClass().getSimpleName();
            fragmentTransaction.replace(R.id.fragment_container, postfragment,tag);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commitAllowingStateLoss();
        }
    }



    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStorageDirectory();
        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
        requestPermissions(permissions,1);

        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    public void photo(View v){

        DialogInterface.OnClickListener optionSelectedListener =new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (items[which].equals("Take Photo")) {

                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    // Ensure that there's a camera activity to handle the intent

                    // Create the File where the photo should go
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                    } catch (IOException ex) {
                        ex.getStackTrace();
                        // Error occurred while creating the File
                    }
                    // Continue only if the File was successfully created
                    if (photoFile != null) {
                        Uri photoURI = Uri.fromFile(photoFile);
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                        StrictMode.setVmPolicy(builder.build());
                        startActivityForResult(takePictureIntent, REQUEST_CAMERA);
                    }


                    //Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    //startActivityForResult(intent, REQUEST_CAMERA);

                } else if (items[which].equals("Choose From Gallery")) {

                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent,SELECT_FILE);
                }
            }
        };

        DialogInterface.OnClickListener cancelListener =new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        };

        Dialog d=createSingleChoiceAlertDialog(getContext(),"Choose picture",items,optionSelectedListener,cancelListener);
        d.create();
        d.show();
    }


    public boolean validation(){

        if(t.getText().toString().length()==0 && iv.getDrawable()==null){
            t.setError("Please enter either a message or select an image");
            return false;
        }
        if(t.getText().toString().length()<1){
            t.setError("The text field should have a length longer than 50 characters");
            return false;
        }
        return true;
    }

}
