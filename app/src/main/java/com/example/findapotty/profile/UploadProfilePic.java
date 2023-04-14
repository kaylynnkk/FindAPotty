package com.example.findapotty.profile;

import static androidx.databinding.DataBindingUtil.setContentView;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.findapotty.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import android.content.ContentResolver;


public class UploadProfilePic extends Fragment {
    private ImageView imageViewUploadPic;
    private FirebaseAuth authProfile;
    private StorageReference storageReference;
    private FirebaseUser firebaseUser;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri uriImage;
    private View rootView;

    /*
    Here, I am binding variables with their respective UI ID.
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = inflater.inflate(R.layout.fragment_upload_profile_pic, container, false);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Upload Profile Picture");

        Button buttonUploadPicChoose = rootView.findViewById(R.id.upload_pic_choose_button);
        Button buttonUploadPic = rootView.findViewById(R.id.upload_pic_button);
        imageViewUploadPic = rootView.findViewById(R.id.imageView_profile_dp);

        authProfile = FirebaseAuth.getInstance();
        firebaseUser = authProfile.getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference("DisplayPics");

        Uri uri = firebaseUser.getPhotoUrl();

        //loading image into the imageView from Firebase
        Picasso.get().load(uri).into(imageViewUploadPic);

        buttonUploadPicChoose.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                openFileChooser();
            }
        });

        buttonUploadPic.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                UploadPic();
                NavController controller = Navigation.findNavController(rootView);
                controller.navigate(R.id.action_uploadProfilePic_to_nav_profile);
//                binding.flAccountButton.setOnClickListener(view -> {
//                    NavController navController = Navigation.findNavController(binding.getRoot());
//                    navController.navigate(R.id.action_nav_signup_fragment_to_navg_login_fragment);
//                    //need to change to the login page again
//                });
            }
        });

        return rootView;

    }

    //Here, is where I upload the picture from the image view in the UploadProfilePicActivity
    // to Firebase cloud Storage.
    private void UploadPic()
    {
        if(uriImage != null)
        {
            StorageReference fileReference = storageReference.child(authProfile.getCurrentUser().getUid() + "." +
                    getFileExtension(uriImage));

            fileReference.putFile(uriImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>()
            {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>(){

                        @Override
                        public void onSuccess(Uri uri) {
                            Uri downloadUri = uri;
                            firebaseUser = authProfile.getCurrentUser();

                            //updates profile on database end
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setPhotoUri(downloadUri).build();
                            firebaseUser.updateProfile(profileUpdates);
                        }
                    });

                    Toast.makeText(rootView.getContext(), "Upload Successful!", Toast.LENGTH_SHORT).show();
//
//                    Intent intent = new Intent(UploadProfilePicActivity.this, NotLogin.class);
//                    startActivity(intent);
//                    finish();
                      //need to go back to Profile Page - also make done button
                      //more accessible
                }
            }).addOnFailureListener(new OnFailureListener() {
                //displays the exception
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(rootView.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }//gives an error if user doesn't selected a new file
        else
        {
            Toast.makeText(rootView.getContext(), "No file selected!", Toast.LENGTH_SHORT).show();
        }
    }

    //gets the file extension of the image to concatenate it with the rest of the necessary info.
    private String getFileExtension(Uri uri)
    {
        ContentResolver cR = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    //for choosing a pic from your file system
    private void openFileChooser()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    //ensures that a valid image is selected from file system
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null)
        {
            uriImage = data.getData();
            imageViewUploadPic.setImageURI(uriImage);
        }
    }

}