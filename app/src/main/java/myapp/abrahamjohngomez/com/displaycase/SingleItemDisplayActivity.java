package myapp.abrahamjohngomez.com.displaycase;

import android.content.Intent;
import android.support.v4.app.Fragment;

import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import db.DbHandler;
//TODO: Image zoom/edit/delete image
public class SingleItemDisplayActivity extends FragmentActivity {
    CollectionPagerAdapter mCollectionPagerAdapter;
    ViewPager mViewPager;
    List<Item> items = new ArrayList<>();
    private static final int ADD_ITEM_RESULT_CODE = 17;

    public static int count = 22;

    private List<Fragment> getFragmentsFromDb() {
        List<Fragment> fList = new ArrayList<Fragment>();
        DbHandler db = new DbHandler(this);
        Log.d("Reading: ", "Reading all items..");
        items = db.getAllItems();

        for(Item it : items) {
            fList.add(ArrayListFragment.newInstance(it.getName(),
                    it.getIsbn(), it.getId(),it.getDescription(),
                    it.getImage(), it.getPurchased(), it.getCondition()));
        }
        return fList;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_item_display);

        updateFragments();
    }
    private void updateFragments() {
        List<Fragment> fragments = getFragmentsFromDb();
        mCollectionPagerAdapter = new CollectionPagerAdapter(
                getSupportFragmentManager(), fragments);

        mViewPager = (ViewPager) findViewById(R.id.pagersingleitem);

        mViewPager.setAdapter(mCollectionPagerAdapter);
    }

    public void btScanNew(View v) {
        Intent intent = new Intent(this, AddNewItemActivity.class);
        startActivityForResult(intent, ADD_ITEM_RESULT_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        DbHandler db = new DbHandler(this);

        if (resultCode == RESULT_OK) {
            //
            Item item = new Item(data.getStringExtra("itemName"), data.getStringExtra("itemIsbn"),
                    data.getStringExtra("itemDescription"), data.getStringExtra("itemImageSrc"),
                    data.getStringExtra("itemPurchased"),
                    data.getStringExtra("itemCondition"));
            db.insertItem(item);
            updateFragments();
            mViewPager.setCurrentItem((mCollectionPagerAdapter.getCount() - 1), true);
        } else {
            System.out.println(resultCode);
        }

    }
}


