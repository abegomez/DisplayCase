package myapp.abrahamjohngomez.com.displaycase;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

//TODO: change to single onClick method with switch: case:R.id.buttonID
public class AddNewItemActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btScan, btAddItem;
    private TextView tvFormatTxt, tvContentTxt, tvItemName, tvItemDescription, tvIsbn, tvCondition;
    private ImageButton btZoomOrAddImage;
    private Toolbar toolbar;
    protected Intent intent;
    public static final String ITEM = "Item";
    private static final int CAMERA_IMAGE_REQUEST = 19;
    private Item item;

    Uri fileUri = null;
    String mCurrentPhotoPath;
    String imageFolderPath = null;
    String imageName;
    Uri photoUri;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item_scan);
        intent = getIntent();

        toolbar = (Toolbar) findViewById(R.id.toolbar_add_item);
        toolbar.setTitle(intent.getStringExtra("title"));
        setSupportActionBar(toolbar);

        btScan = (Button) findViewById(R.id.btScan);
        btScan.setOnClickListener(this);
        btAddItem = (Button) findViewById(R.id.btAddNewItem);
        btAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddItemClick();
            }
        });
        btAddItem.setText(intent.getStringExtra("buttonText"));

        tvItemName = (TextView) findViewById(R.id.tvAddItemName);
        tvItemDescription = (TextView) findViewById(R.id.tvAddItemDescription);
        tvIsbn = (TextView) findViewById(R.id.tvAddItemIsbn);
        tvCondition = (TextView) findViewById(R.id.tvAddItemCondition);

        btZoomOrAddImage = (ImageButton) findViewById(R.id.ibZoomOrAddImage);
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

        if(intent.getIntExtra("requestCode", -1) == SingleItemDisplayActivity.UPDATE_ITEM_CODE) {
            item = this.getIntent().getParcelableExtra(ITEM);
            System.out.println("updating:" + item.getId());
            tvItemName.setText(item.getName());
            tvItemDescription.setText(item.getDescription());
            tvIsbn.setText(item.getIsbn());
            tvCondition.setText(item.getCondition());
            try {
                BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                InputStream input = this.getContentResolver()
                        .openInputStream(Uri.parse(item.getImage()));
                Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
                input.close();
                btZoomOrAddImage.setImageBitmap(bitmap);
            } catch (IOException e ) {
                e.printStackTrace();
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
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        //retrieve scan result
        if(resultCode == RESULT_OK){
            switch(requestCode) {
                case CAMERA_IMAGE_REQUEST:
                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                    try {
                        InputStream input = this.getContentResolver().openInputStream(photoUri);
                        bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
                        input.close();
                        btZoomOrAddImage.setImageBitmap(bitmap);
                    } catch(IOException e) {
                        e.printStackTrace();
                    }
                    break;

                default:
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

    private void onAddItemClick() {
        Intent intent = new Intent();
        item.setName(tvItemName.getText().toString());
        item.setDescription(tvItemDescription.getText().toString());
        item.setIsbn(tvIsbn.getText().toString());
        if(photoUri != null) {
            item.setImage(photoUri.toString());
        }
        //item.setPurchased(tvItemDescription.getText().toString());
        item.setCondition(tvCondition.getText().toString());
        intent.putExtra("item", item);
        setResult(RESULT_OK, intent);
        finish();
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }
    //zoom image
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
    //change/delete image
    private void onImageLongClick() {
        System.out.println("image long clicked");
        Log.d("output dir: ", getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath());
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
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
}
