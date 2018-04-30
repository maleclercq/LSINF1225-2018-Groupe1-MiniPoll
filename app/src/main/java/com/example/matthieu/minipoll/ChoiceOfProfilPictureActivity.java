package com.example.matthieu.minipoll;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import java.io.File;

public class ChoiceOfProfilPictureActivity extends ProfileActivity {

    public static final int CAMERA_REQUEST = 10;
    public static final int REQUEST_CODE = 20;
    public static final int IMAGE_GALLERY_REQUEST = 20;
    private ImageView imgSpecimenPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice_of_profil_picture);

        //get access to the image view
        imgSpecimenPhoto = (ImageView) findViewById(R.id.imgSpecimenPhoto);
    }

    public void retour(View v){
        finish();
    }

    /**
     * This method will be called when the Take Picture button will be clicked.
     * @param v
     */
    public void takePicture(View v){
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // if the user choose ok, we execute the code
        if (requestCode==RESULT_OK){
            if (requestCode == CAMERA_REQUEST){
                //on appelle la caméra
                Bitmap cameraImage = (Bitmap) data.getExtras().get("data");
                // on a l'image de la caméra, il faut maintenant l'afficher
                imgSpecimenPhoto.setImageBitmap(cameraImage);
            }
        }
    }

    /**
     * This method will be invoke when the Image Gallery button will be invoke.
     * @param v
     */
    public void onImageGalleryClicked(View v){
        //invoke the image gallery using an implicit intent
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);

        //place where we get the picture
        File pictureDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        String pictureDirectoryPath = pictureDirectory.getPath();

        //set the uri representation
        Uri data = Uri.parse(pictureDirectoryPath);

        //set the data and type
        photoPickerIntent.setDataAndType(data, "image/*");
        startActivityForResult(photoPickerIntent, IMAGE_GALLERY_REQUEST);
    }
}
