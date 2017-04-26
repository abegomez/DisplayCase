package myapp.abrahamjohngomez.com.displaycase;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

//TODO: change to single onClick method with switch: case:R.id.buttonID
public class AddNewItemActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btScan, btAddItem;
    private TextView tvFormatTxt, tvContentTxt, tvItemName, tvItemDescription, tvIsbn, tvCondition;
    private ImageButton btZoomOrAddImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_barcode);
        setResult(RESULT_CANCELED);
        btScan = (Button) findViewById(R.id.btScan);
        btScan.setOnClickListener(this);
        btAddItem = (Button) findViewById(R.id.btAddNewItem);
        btAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddItemClick();
            }
        });

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
        tvFormatTxt = (TextView) findViewById(R.id.tvScan_format);
        tvContentTxt = (TextView) findViewById(R.id.tvScan_content);
        tvItemName = (TextView) findViewById(R.id.tvAddItemName);
        tvItemDescription = (TextView) findViewById(R.id.tvAddItemDescription);
        tvIsbn = (TextView) findViewById(R.id.tvAddItemIsbn);
        tvCondition = (TextView) findViewById(R.id.tvAddItemCondition);
    }
    public void onClick(View v) {
        //responds to button click
        if(v.getId() == R.id.btScan){
            //scan
            IntentIntegrator scanIntegrator = new IntentIntegrator(this);
            scanIntegrator.initiateScan();
        }
    }
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        //retrieve scan result
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if(scanningResult != null) {
            //we have a result
            String scanContent = scanningResult.getContents();
            String scanFormat = scanningResult.getFormatName();
            tvFormatTxt.setText("Format: " + scanFormat);
            tvContentTxt.setText("Content: " + scanContent);
        } else {
            Toast toast = Toast.makeText(getApplicationContext(),"No scan data received!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
    private void onAddItemClick() {
        Intent intent = new Intent();
        intent.putExtra("itemName", tvItemName.getText().toString());
        intent.putExtra("itemDescription", tvItemDescription.getText().toString());
        intent.putExtra("itemIsbn", tvIsbn.getText().toString());
        intent.putExtra("itemCondition", tvCondition.getText().toString());
        setResult(RESULT_OK, intent);
        finish();
    }

    //zoom image
    private void onImageClick() {

        System.out.println("image clicked");
    }
    //change/delete image
    private void onImageLongClick() {

        System.out.println("image long clicked");

    }

}
