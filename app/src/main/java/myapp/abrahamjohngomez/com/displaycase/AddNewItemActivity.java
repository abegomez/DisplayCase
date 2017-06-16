/**
 * Copyright (C) 2017  Some Copyright Goes Here
 */

package myapp.abrahamjohngomez.com.displaycase;

import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Activity to add a new item or update an item in database.
 * On an update intent, sets the input fields to current value.
 * Image on click function will zoom image, onLongClick will start camera intent to take a new picture.
 * Scan button allows scanning the barcode of an item to grab isbn number.
 * Isbn will be used to pull up information on item in future update.
 *
 * @author Abraham Gomez
 */
public class AddNewItemActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    private Button btScan, btAddItem, btPurchaseDate;
    private TextView tvFormatTxt, tvContentTxt, tvItemName, tvItemDescription, tvIsbn, tvCondition;
    private ImageButton btZoomOrAddImage;
    private Toolbar toolbar;
    protected Intent intent;
    public static final String ITEM = "Item";
    private static final int CAMERA_IMAGE_REQUEST = 19;
    private Item item;
    public Uri photoUri;
    private Bitmap bitmap;
    private File photoFile = null;
    private String mImageFileLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item_scan);
        intent = getIntent();

        toolbar = (Toolbar) findViewById(R.id.toolbar_add_item);
        btScan = (Button) findViewById(R.id.btScan);
        btAddItem = (Button) findViewById(R.id.btAddNewItem);
        tvItemName = (TextView) findViewById(R.id.tvAddItemName);
        tvItemDescription = (TextView) findViewById(R.id.tvAddItemDescription);
        tvIsbn = (TextView) findViewById(R.id.tvAddItemIsbn);
        tvCondition = (TextView) findViewById(R.id.tvAddItemCondition);
        btZoomOrAddImage = (ImageButton) findViewById(R.id.ibZoomOrAddImage);
        btPurchaseDate = (Button) findViewById(R.id.btPurchaseDate);

        toolbar.setTitle(intent.getStringExtra("title"));
        btAddItem.setText(intent.getStringExtra("buttonText"));
        setSupportActionBar(toolbar);

        btScan.setOnClickListener(this);
        btAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddItemClick();
            }
        });
        btZoomOrAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onImageClick();
            }
        });
        btZoomOrAddImage.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                onImageLongClick();
                return true;
            }
        });

        //if coming from update button get info from previous activity
        if(intent.getIntExtra("requestCode", -1) == SingleItemDisplayActivity.UPDATE_ITEM_CODE) {
            item = this.getIntent().getParcelableExtra(ITEM);
            System.out.println("updating:" + item.getId());
            tvItemName.setText(item.getName());
            tvItemDescription.setText(item.getDescription());
            tvIsbn.setText(item.getIsbn());
            tvCondition.setText(item.getCondition());
            btPurchaseDate.setText(item.getPurchased());
            //try using item image
            try {
                RequestOptions options = new RequestOptions();
                options.centerCrop(this).fitCenter();
                Glide.with(this).load(item.getImage()).apply(options).into(btZoomOrAddImage);
            } catch (NullPointerException e1) {
                e1.printStackTrace();
            }
        } else {
            item = new Item();
        }
    }

    public void onClick(View v) {
        //responds to button click
        if(v.getId() == R.id.btScan){
            //scan
            IntentIntegrator scanIntegrator = new IntentIntegrator(this);
            scanIntegrator.initiateScan();
        }
    }

    /**
     * Return result from camera intent or scan barcode intent.
     * Camera intent sets image saved to preview
     * Barcode scan result sets isbn number to received data
     *
     * @param requestCode camera intent or scan barcode intent identifier
     * @param resultCode image or barcode scan successful
     * @param intent intent data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        //retrieve scan result
        if(resultCode == RESULT_OK){
            switch(requestCode) {
                case CAMERA_IMAGE_REQUEST:
                    //get image saved from camera intent and set to preview image
                    System.out.println(photoFile.getAbsolutePath());
                    try {
                        //this is being called too many times, move to its own class or method
                        RequestOptions options = new RequestOptions();
                        options.centerCrop(this);
                        options.fitCenter();
                        Glide.with(btZoomOrAddImage.getContext()).load(photoFile).apply(options).into(btZoomOrAddImage);
                    } catch(Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case IntentIntegrator.REQUEST_CODE:
                    IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
                    if(scanningResult != null) {
                        //we have a result
                        String scanContent = scanningResult.getContents();
                        tvIsbn.setText(scanContent);
                    } else {
                        Toast toast = Toast.makeText(getApplicationContext(),"No scan data received!", Toast.LENGTH_SHORT);
                        toast.show();
                    }
            }
        }
    }

    /**
     * Send item back to previous view with RESULT_OK flag
     * Create intent with Item details and return to previous activity
     */
    private void onAddItemClick() {
        Intent intent = new Intent();
        item.setName(tvItemName.getText().toString());
        item.setDescription(tvItemDescription.getText().toString());
        item.setIsbn(tvIsbn.getText().toString());
        item.setPurchased(btPurchaseDate.getText().toString());
        if(photoUri != null) {
            item.setImage(photoUri.toString());
        }
        //item.setPurchased(tvItemDescription.getText().toString());
        item.setCondition(tvCondition.getText().toString());
        intent.putExtra("item", item);
        setResult(RESULT_OK, intent);
        finish();
    }

    /**
     * Make a temp file to store picture from camera intent
     * @return File temp file location
     * @throws IOException image was not able to be created
     */
    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        mImageFileLocation = image.getAbsolutePath();
        return image;
    }

    /**
     * Zooms in or out when image is clicked
     */
    private void onImageClick() {
        System.out.println("image clicked");
        ImageView zoomedImaged =  (ImageView) findViewById(R.id.expanded_image);
        if(zoomedImaged.getVisibility() != View.VISIBLE) {
            zoomedImaged.setImageBitmap(bitmap);
            zoomedImaged.setVisibility(View.VISIBLE);
        }else {
            zoomedImaged.setVisibility(View.GONE);
        }
    }

    /**
     * Change image when image is long-clicked.  Opens a new camera intent to get a picture for result.
     * Image location is created and intent is started if able to create a temp file with createImageFile()
     * externalfilesdir is directory defined in xml/file-path:
     * <external-path name="external_files" path="Android/data/myapp.abrahamjohngomez.com.displaycase/files/Pictures"/>
     */
    private void onImageLongClick() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        System.out.println("image long clicked");

        Log.d("output dir: ", getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath());

        //if intent has available activity(camera)
        if(takePictureIntent.resolveActivity(getPackageManager()) != null) {
            try {
                photoFile = createImageFile();
            } catch (IOException e) {

                Log.d("ioerror", e.toString());
            }
            if(photoFile !=null) {
                photoUri = FileProvider.getUriForFile(this, "myapp.abrahamjohngomez.com.displaycase.fileprovider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(takePictureIntent, CAMERA_IMAGE_REQUEST);
            }
        }
    }

    /**
     * Open dialog to input purchase date
     * @param v purchase button
     */
    public void pickPurchaseDate(View v) {
        DatePickerFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(),"datePicker");
    }

    /**
     * Set purchase date on button after setting date in dialog
     * @param view dialog view
     * @param year
     * @param month
     * @param day
     */
    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        cal.set(year, month, day);
        cal.set(Calendar.YEAR, year);
        //Date date = new Date(cal.get(Calendar.YEAR, year), month, day);
        btPurchaseDate.setText("Purchased " + dateFormat.format(cal.getTime()));
    }
}//end of class
