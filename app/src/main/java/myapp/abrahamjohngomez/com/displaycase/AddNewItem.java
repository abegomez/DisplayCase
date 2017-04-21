package myapp.abrahamjohngomez.com.displaycase;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


public class AddNewItem extends AppCompatActivity implements View.OnClickListener{

    private Button btScan, btAddItem;
    private TextView tvFormatTxt, tvContentTxt, tvItemName, tvItemDescription, tvIsbn, tvCondition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_barcode);
        btScan = (Button) findViewById(R.id.btScan);
        btAddItem = (Button) findViewById(R.id.btAddNewItem);
        btAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddItemClick();
            }
        });
        btScan.setOnClickListener(this);
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

    public void onAddItemClick() {

        Intent intent = new Intent();
        intent.putExtra("name", tvItemName.getText().toString());
        intent.putExtra("description", tvItemDescription.getText().toString());
        intent.putExtra("isbn", tvIsbn.getText().toString());
        intent.putExtra("condition", tvCondition.getText().toString());
        setResult(RESULT_OK, intent);
        finish();
    }

}
