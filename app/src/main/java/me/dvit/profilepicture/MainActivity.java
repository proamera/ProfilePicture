package me.dvit.profilepicture;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    public static final int PICK_IMAGE = 1;
    private static final int CAMERA_REQUEST = 1888;
    ImageView profile, camera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        camera = (ImageView) findViewById(R.id.camera_icon);
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initPopup(v);
            }
        });

    }

    //create the popup and add actions to the buttons
    private void initPopup(View v) {
        LayoutInflater inflater = (LayoutInflater) MainActivity.this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.popup_layout,
                (ViewGroup) findViewById(R.id.popup_layout));
        final PopupWindow popup = new PopupWindow(layout, 500, 500);
        popup.showAtLocation(v, Gravity.CENTER, 0, 0);
        Button camera_button, gallery_button, remove_button, cancel_button;
        camera_button = (Button) layout.findViewById(R.id.camera_pic);
        gallery_button = (Button) layout.findViewById(R.id.gallery_pic);
        remove_button = (Button) layout.findViewById(R.id.remove_pic);
        cancel_button = (Button) layout.findViewById(R.id.cancel);
        camera_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, 1888);
                popup.dismiss();
            }
        });
        gallery_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(gallery, 0);
                popup.dismiss();

            }
        });
        remove_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                profile = (ImageView) findViewById(R.id.user_picture);
                profile.setImageResource(R.drawable.whiteimage);
                popup.dismiss();
            }
        });
        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup.dismiss();
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case 0:
                if (resultCode == RESULT_OK ) {
                    Uri imageUri = data.getData();

                    try {
                        Bitmap bitmaps = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);

                        final ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitmaps.compress(Bitmap.CompressFormat.PNG, 90, stream);
                        byte[] byteArray = stream.toByteArray();

                        String   encodeded = Base64.encodeToString(byteArray, Base64.DEFAULT);

                        byte[] decodedString = Base64.decode(encodeded, Base64.DEFAULT);
                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        profile = (ImageView) findViewById(R.id.user_picture);
                        profile.setImageBitmap(bitmaps);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                break;
            case 1888:
                if(requestCode == 1&& resultCode == Activity.RESULT_OK ){

                   Bitmap photo = (Bitmap) data.getExtras().get("data");
                    profile.setImageBitmap(photo);
                }
                break;
        }


    }
}