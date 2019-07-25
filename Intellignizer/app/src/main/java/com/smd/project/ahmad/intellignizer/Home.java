package com.smd.project.ahmad.intellignizer;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.widget.DrawerLayout;
//import android.support.v7.app.ActionBarActivity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import org.neuroph.core.NeuralNetwork;
import org.neuroph.imgrec.image.Image;
import org.neuroph.imgrec.image.ImageFactory;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class Home extends AppCompatActivity {

    private Toolbar toolbar;
    ImageView iv;

//FROM MAINAVTIVITY>JAVA
    private final int SELECT_PHOTO = 1;
    private final int CAPTURE_PHOTO = 2;
    private final int LOADING_DATA_DIALOG = 3;
    private final int RECOGNIZING_IMAGE_DIALOG = 4;

    private Bitmap bitmap;
    private Image image;
    private NeuralNetwork nnet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_bar);


        toolbar=(Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        iv=(ImageView)findViewById(R.id.imageView);

        NavigationDrawerFragment drawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);

        drawerFragment.setUp(R.id.fragment_navigation_drawer,(DrawerLayout)findViewById(R.id.drawer_layout), toolbar);


        loadData();
    }



    public boolean onOptionsItemsSelected(MenuItem item)
    {
        return super.onOptionsItemSelected(item);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub

        //Camera
        if(requestCode == CAPTURE_PHOTO) {
            if (resultCode != RESULT_CANCELED) {
            try
            {
                super.onActivityResult(requestCode, resultCode, data);


                //get image
                Bitmap bp = (Bitmap) data.getExtras().get("data");
                iv.setImageBitmap(bp);

                //recognise text
                String result = recognize(bp);

                //go to editing and saving screen
                Intent i = new Intent(this , saveFile.class);
                i.putExtra("content" , result);
                startActivity(i);

            } catch (Exception ce) {
                Log.d("Intellitext", "Problem capturing image");

                Toast.makeText(this , "Image Unclear" , Toast.LENGTH_SHORT).show();
            }
            }
        }

        //Gallery
        else if(requestCode == SELECT_PHOTO)
        {
            if (resultCode == RESULT_OK) {
                try {
                    // get file path of selected image
                    Uri selectedImage = data.getData();
                    String filePath = getRealPathFromURI(selectedImage);

                    // get image
                    image = ImageFactory.getImage(filePath);
                    InputStream imageStream = getContentResolver().openInputStream(selectedImage);
                    bitmap = Bitmap.createBitmap(BitmapFactory.decodeStream(imageStream));
                    iv.setImageBitmap(bitmap);

                    // show image
//                    txtAnswer.setCompoundDrawablesWithIntrinsicBounds(null, null, null, new BitmapDrawable(bitmap));
                    // show image name
//                        String img="";
//                        for (int j = 0; j < bitmap.getHeight(); j++) {
//                            for (int i = 0; i < bitmap.getWidth(); i++) {
//                                int dig=bitmap.getPixel(i, j);
////                                if(dig!=-1)
//                                    img=img+(String.valueOf(dig));
//                            }
//                        }
//                        txtAnswer.setText(img);
//                        InputStream is = getResources().openRawResource(R.raw.text);
                    // load neural network
//                        nnet = NeuralNetwork.load(is);
//                        txtAnswer.setText(recognize(bitmap));

                    //recognise image
                    String result = recognize(bitmap);
//                    txtAnswer.setText(result);

                    //go to editing and saving screen
                    Intent i = new Intent(this , saveFile.class);
                    i.putExtra("content" , result);
                    startActivity(i);

                } catch (FileNotFoundException fnfe) {
                    Log.d("Intellitext", "File not found");
                }
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    public void cameraTap(MenuItem menu)
    {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAPTURE_PHOTO);

    }

    public void galleryTap(MenuItem menu)
    {
        Intent imageIntent = new Intent(Intent.ACTION_PICK);
        imageIntent.setType("image/*");
        // show gallery
        startActivityForResult(imageIntent, SELECT_PHOTO);
    }


    private Runnable loadDataRunnable = new Runnable() {
        public void run() {
            // open neural network
            InputStream is = getResources().openRawResource(R.raw.text);
            // load neural network
            nnet = NeuralNetwork.load(is);
//            imageRecognition = (ImageRecognitionPlugin) nnet.getPlugin(ImageRecognitionPlugin.class);
            // dismiss loading dialog
            dismissDialog(LOADING_DATA_DIALOG);
        }
    };

    private String recognize(Bitmap image) {
        showDialog(RECOGNIZING_IMAGE_DIALOG);
        // recognize image
        Cleaner c = new Cleaner();
        image=c.blackAndLightGrayCleaning(image);

//        Toast.makeText(this , "getting image" , Toast.LENGTH_SHORT).show();
        ImageRender imgRender=new ImageRender(this,image,nnet);
        //HashMap<String, Double> output = imageRecognition.recognizeImage(image);
        dismissDialog(RECOGNIZING_IMAGE_DIALOG);
        return imgRender.rendering();
        //return getAnswer(output);
    }

    private void loadData() {
        showDialog(LOADING_DATA_DIALOG);
        // load neural network in separate thread with stack size = 32000
        new Thread(null, loadDataRunnable, "dataLoader", 64000000).start();
    }

    public String getRealPathFromURI(Uri contentUri) {

        // converts uri to file path, converts /external/images/media/9 to /sdcard/neuroph/fish.jpg
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(contentUri, projection, null, null, null);
        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();

        return cursor.getString(columnIndex);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        ProgressDialog dialog;

        if (id == LOADING_DATA_DIALOG) {
            dialog = new ProgressDialog(this);
            dialog.setTitle("Neuroph Example");
            dialog.setMessage("Loading data...");
            dialog.setCancelable(false);

            return dialog;
        } else if (id == RECOGNIZING_IMAGE_DIALOG) {
            dialog = new ProgressDialog(this);
            dialog.setTitle("Neuroph Example");
            dialog.setMessage("Recognizing image...");
            dialog.setCancelable(false);

            return dialog;
        }
        return null;
    }


    private String getAnswer(HashMap<String, Double> output) {
        double highest = 0;
        String answer = "";
        for (Map.Entry<String, Double> entry : output.entrySet()) {
            if (entry.getValue() > highest) {
                highest = entry.getValue();
                answer = entry.getKey();
            }
        }

        return answer;
    }
}
