//package com.example.brishna.icrsavefile;
package com.smd.project.ahmad.intellignizer;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.pdf.PdfDocument;
import android.media.MediaScannerConnection;
import android.os.AsyncTask;
import android.widget.ShareActionProvider;
import android.os.Environment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
//
//import android.support.v4.view.MenuItemCompat;
//import android.support.v7.widget.Toolbar;
//import android.support.v7.app.AppCompatActivity;


import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;

import android.support.v7.app.ActionBarActivity;
import android.widget.Toolbar;

public class saveFile extends Activity {

    private Toolbar toolbar;
    private ShareActionProvider mShareActionProvider;
    static final int DOWNLOADING = 1;
    static final int DOWNLOADED = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.activity_save_file);
//        if(getActionBar() != null)
//            getActionBar().show();

        //set action bar
        toolbar=(Toolbar) findViewById(R.id.app_bar);

        toolbar.setTitleTextColor(getResources().getColor(R.color.colorAccent));
        setActionBar(toolbar);


        //set content of editText
        String content = this.getIntent().getStringExtra("content");
        ((EditText) (findViewById(R.id.result_txt))).setText(content);


        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, ((EditText) (findViewById(R.id.result_txt))).getText().toString());
                sendIntent.setType("text/plain");

                if(mShareActionProvider != null)
                    mShareActionProvider.setShareIntent(sendIntent);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
        ((EditText)findViewById(R.id.result_txt)).addTextChangedListener(textWatcher);

        //set onCLick events for buttons
        findViewById(R.id.pdf_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                savePDF(view);
            }
        });


        //set onCLick events for buttons
        findViewById(R.id.txt_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveTXT(view);
            }
        });

//        this.invalidateOptionsMenu();
//        setHasOptionsMenu(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        super.onCreateOptionsMenu(menu);
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_save, menu);


//        return super.onCreateOptionsMenu(menu);

        // Locate MenuItem with ShareActionProvider
        MenuItem item = menu.findItem(R.id.action_share);
//        MenuItemCompat item = ;
//        MenuItemCompat i = menu.getItem(R.id.action_share);


//        mShareActionProvider=(ShareActionProvider) MenuItemCompat.getActionProvider(item);
//        mShareActionProvider.setOnShareTargetSelectedListener(this);

        // Fetch and store ShareActionProvider
//        mShareActionProvider = (ShareActionProvider) item.getActionProvider();
        mShareActionProvider = (ShareActionProvider) item.getActionProvider();


        //Create and set share intent
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, ((EditText) (findViewById(R.id.result_txt))).getText().toString());
        sendIntent.setType("text/plain");
        mShareActionProvider.setShareIntent(sendIntent);

        return super.onCreateOptionsMenu(menu);
//        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id == R.id.action_share)
        {
            //Create and set share intent
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, ((EditText) (findViewById(R.id.result_txt))).getText().toString());
            sendIntent.setType("text/plain");
            mShareActionProvider.setShareIntent(sendIntent);
        }
        return super.onOptionsItemSelected(item);
    }

    public void saveTXT(View v)
    {

        final NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        final Notification.Builder notificationBuilder = new Notification.Builder(this);

        notificationBuilder.setAutoCancel(false);
        notificationBuilder.setColor(getResources().getColor(R.color.colorPrimaryDark));
        notificationBuilder.setContentTitle("downloading file");
        notificationBuilder.setSmallIcon(android.R.drawable.stat_sys_download);

//        notificationBuilder.setOngoing(true);
//        notificationBuilder.setContentIntent(PendingIntent.getActivity(this, 0, new Intent(this,
//                saveFile.class), PendingIntent.FLAG_UPDATE_CURRENT));
//        notificationBuilder.setContentText(getString(R.string.notification_text));
        notificationBuilder.setTicker("");
        notificationManager
                .notify(DOWNLOADING, notificationBuilder.build());

        Toast.makeText(saveFile.this , "Saving File" , Toast.LENGTH_SHORT).show();

        //get text
        String text =  ((EditText)(findViewById(R.id.result_txt))).getText().toString();

        //get file name
        String filename = ((EditText)(findViewById(R.id.filename_txt))).getText().toString();
//        if(filename == "")
        if(filename.isEmpty())
            filename = "intellitext";

        //write text to file in worker thread
        AsyncTask<String , Void , Void> saveTask = new AsyncTask<String , Void , Void>() {
            @Override
            protected Void doInBackground(String... params) {

                try
                {
                    //get final filename
                    File file = getTXTFile(params[1]);

                    //write text to file
                    FileWriter writer = new FileWriter(file , false);
                    writer.write(params[0]);
                    writer.close();

                    // Refresh the data so it can seen when the device is plugged in a
                    // computer
                    MediaScannerConnection.scanFile((saveFile.this),
                            new String[]{file.toString()},
                            null,
                            null);

                }
                catch (IOException e)
                {
                    Log.e("com.brishna.icrsavefile", "Unable to write to the txt file.");
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

                Toast.makeText(saveFile.this , "Saved" , Toast.LENGTH_SHORT).show();

                notificationBuilder.setContentTitle("file downloaded");
                notificationBuilder.setSmallIcon(android.R.drawable.stat_sys_download_done);
                notificationManager
                        .notify(DOWNLOADING, notificationBuilder.build());

            }

        };

        saveTask.execute(text , filename);
    }

    public void savePDF(View v)
    {
        final NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        final Notification.Builder notificationBuilder = new Notification.Builder(this);

        notificationBuilder.setAutoCancel(false);
        notificationBuilder.setColor(getResources().getColor(R.color.colorPrimaryDark));
        notificationBuilder.setContentTitle("downloading file");
        notificationBuilder.setSmallIcon(android.R.drawable.stat_sys_download);

//        notificationBuilder.setOngoing(true);
//        notificationBuilder.setContentIntent(PendingIntent.getActivity(this, 0, new Intent(this,
//                saveFile.class), PendingIntent.FLAG_UPDATE_CURRENT));
//        notificationBuilder.setContentText(getString(R.string.notification_text));
        notificationBuilder.setTicker("");
        notificationManager
                .notify(DOWNLOADING, notificationBuilder.build());

        Toast.makeText(saveFile.this , "Saving File" , Toast.LENGTH_SHORT).show();

        //get file name
        String filename = ((EditText)(findViewById(R.id.filename_txt))).getText().toString();
        if(filename .isEmpty())
            filename = "intellitext";

        //create a pdf document
        PdfDocument doc = new PdfDocument();

        View content = findViewById(R.id.result_txt);
        int pageNumber = 1;
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(content.getWidth(),
                content.getHeight() - 20, pageNumber).create();


        PdfDocument.Page page = doc.startPage(pageInfo);
        content.draw(page.getCanvas());
        doc.finishPage(page);

        //write pdf to file in worker thread
        AsyncTask<Object, Void , Void> saveTask = new AsyncTask<Object , Void , Void>() {
            @Override
            protected Void doInBackground(Object... params) {
                //get final filename
                File file = getPDFFile((String) params[1]);

                try
                {
//                    file.createNewFile();
                    OutputStream ostream = new FileOutputStream(file);
                    ((PdfDocument)params[0]).writeTo(ostream);
                    ((PdfDocument)params[0]).close();
                    ostream.close();

                    // Refresh the data so it can seen when the device is plugged in a
                    // computer
                    MediaScannerConnection.scanFile((saveFile.this),
                            new String[]{file.toString()},
                            null,
                            null);

                }
                catch (IOException e)
                {
                    Log.e("com.brishna.icrsavefile", "Unable to write to the txt file.");
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

                Toast.makeText(saveFile.this , "Saved" , Toast.LENGTH_SHORT).show();

                notificationBuilder.setContentTitle("file downloaded");
                notificationBuilder.setSmallIcon(android.R.drawable.stat_sys_download_done);
                notificationManager
                        .notify(DOWNLOADING, notificationBuilder.build());
            }
        };

        saveTask.execute(doc , filename);
    }

//    public void saveHTML(View v)
//    {
//        //get text
//        Toast.makeText(getApplicationContext() , v.toString() , Toast.LENGTH_SHORT).show();
//        String text =  ((EditText)(findViewById(R.id.result_txt))).getText().toString();
//        Toast.makeText(getApplicationContext() , text , Toast.LENGTH_SHORT).show();
//
//        //get file name
//        String filename = ((EditText)(findViewById(R.id.filename_txt))).getText().toString();
//        if(filename == "")
//            filename = "intellitext";
////        filename = "testTXT";
//
//        //write text to file in worker thread
//        AsyncTask<String , Void , Void> saveTask = new AsyncTask<String , Void , Void>() {
//            @Override
//            protected Void doInBackground(String... params) {
//
//                try
//                {
//                    //get final filename
//                    File file = getHTMLFile(params[1]);
//
//                    //write text to file
//                    FileWriter writer = new FileWriter(file , false);
//                    writer.write(params[0]);
//                    writer.close();
////
////                    OutputStream ostream = new FileOutputStream(file);
////                    ostream.write("this");
////                    (()params[0]).writeTo(ostream);
////                    ostream.close();
////
//
//
//                    // Refresh the data so it can seen when the device is plugged in a
//                    // computer
//                    MediaScannerConnection.scanFile((Context) (Save.this),
//                            new String[]{file.toString()},
//                            null,
//                            null);
//
//                }
//                catch (IOException e)
//                {
//                    Log.e("com.brishna.icrsavefile", "Unable to write to the doc file.");
//                }
//
//                return null;
//            }
//        };
//
//        saveTask.execute(text , filename);
//    }
//

    public File getTXTFile(String name)
    {
        String filename = name;
        String type = ".txt";
        int counter = 0;
        File file = null;

        try {
            file = new File(getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), filename+type);
            while (file.exists())
            {
                counter++;
                filename = name + counter;
                file = new File(getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), filename+type);
            }

            file.createNewFile();

        }
        catch (IOException e)
        {
            Log.e("com.brishna.icrsavefile", "Unable to create .txt file.");
        }

        return file;
    }


    public File getPDFFile(String name)
    {
        String filename = name;
        String type = ".pdf";
        int counter = 0;
        File file = null;

        try {
            file = new File(getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), filename+type);
            while (file.exists())
            {
                counter++;
                filename = name + counter;
                file = new File(getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), filename+type);
            }

            file.createNewFile();

        }
        catch (IOException e)
        {
            Log.e("com.brishna.icrsavefile", "Unable to create .pdf file.");
        }

        return file;
    }

    public void Share(View v)
    {
        // Locate MenuItem with ShareActionProvider
        Button shareBtn = (Button) findViewById(R.id.action_share);

//        // Fetch and store ShareActionProvider
//        getApplicationContext()
//        mShareActionProvider = (ShareActionProvider) getActionProvider();

        //Create and set share intent
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, ((EditText) (findViewById(R.id.result_txt))).getText().toString());
        sendIntent.setType("text/plain");
        mShareActionProvider.setShareIntent(sendIntent);



    }

//    public File getHTMLFile(String name)
//    {
//        String filename = name;
//        String type = ".html";
//        int counter = 0;
//        File file = null;
//
//        try {
//            file = new File(getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), filename+type);
//            while (file.exists())
//            {
//                counter++;
//                filename = name + counter;
//                file = new File(getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), filename+type);
//            }
//
//            file.createNewFile();
//
//        }
//        catch (IOException e)
//        {
//            Log.e("com.brishna.icrsavefile", "Unable to create .html file.");
//        }
//
//        return file;
//    }

}

