package com.example.matthieu.minipoll;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.matthieu.minipoll.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class ChoiceOfPictureActivity extends Activity {
    //sources:
    // - https://www.youtube.com/watch?v=_xIWkCJZCu0
    // - https://www.youtube.com/watch?v=wBuWqqBWziU
    // - https://www.youtube.com/watch?v=QIbOBrO9OEA

    public static final int CAMERA_REQUEST = 10;
    public static final int IMAGE_GALLERY_REQUEST = 20;
    public ImageView imgSpecimenPhoto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice_of_picture);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*.7), (int)(height*.2));

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = -20;


        // get a reference to the image view that holds what the user will see
        imgSpecimenPhoto = findViewById(R.id.imgSpecimenPhoto);
    }


    /**
     * This method will be called when the Take Picture button will be clicked.
     * @param v
     */
    public void onTakePictureClicked(View v){
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // store the image
        File pictureDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        String pictureName = getPictureName();
        File imageFile = new File(pictureDirectory, pictureName);
        Uri pictureUri = Uri.fromFile(imageFile);
        cameraIntent = cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, pictureUri);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // if the user choose ok, we execute the code inside the braces, wheter we call the camera or the image Gallery
        if (resultCode==RESULT_OK){
            if (requestCode == CAMERA_REQUEST){
                //hearing back from the camera
                //Bitmap cameraImage = (Bitmap) data.getExtras().get("data");
                // we have the image, we ahve to put it in the imageview
                //imgSpecimenPhoto.setImageBitmap(cameraImage);


            }
            if (requestCode == IMAGE_GALLERY_REQUEST)
            //back from the image Gallery
            {
                // adress of image on SD card
                Uri imageUri = data.getData();

                // declare a stream to read the image from the SD card
                InputStream inputStream;

                //getting an inputStream based on the URI of the image choosen
                try {
                    inputStream = getContentResolver().openInputStream(Objects.requireNonNull(imageUri));

                    // get a bitmap from the stream
                    Bitmap image = BitmapFactory.decodeStream(inputStream);

                    //show the image to the user in the imageView
                    imgSpecimenPhoto.setImageBitmap(image);
                    finish();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    // show a message to the user if the image is not available or if we can't open it
                    Toast.makeText(this, "Unable to open image", Toast.LENGTH_LONG).show();
                }
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

        //we will envoke this activity and get something back from it
        startActivityForResult(photoPickerIntent, IMAGE_GALLERY_REQUEST);
    }

    public String getPictureName() {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String timestamp = sdf.format(new Date());
        return "MyPicture" + timestamp + ".jpg";
    }
}
