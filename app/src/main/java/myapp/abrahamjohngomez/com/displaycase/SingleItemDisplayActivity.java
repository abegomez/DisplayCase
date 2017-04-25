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

public class SingleItemDisplayActivity extends FragmentActivity {
    CollectionPagerAdapter mCollectionPagerAdapter;
    ViewPager mViewPager;
    private static final int ADD_ITEM_RESULT_CODE = 17;

    public static int count = 10;

//    private List<Fragment> getFragments() {
//        Item newItem = new Item();
//        //String name, String isbn, int id, String description, String image, Date purchased, String condition
//        newItem.setName("Another Item");
//        newItem.setDescription("Here is a different item.");
//        newItem.setId(344442);
//        newItem.setIsbn("2342345");
//        List<Fragment> fList = new ArrayList<Fragment>();
//        fList.add(ArrayListFragment.newInstance(newItem));
//        return fList;
//    }
    private List<Fragment> getFragmentsFromDb() {
        List<Fragment> fList = new ArrayList<Fragment>();
        DbHandler db = new DbHandler(this);
        Log.d("Reading: ", "Reading all items..");
        List<Item> items = db.getAllItems();

        for(Item it : items) {
            fList.add(
                    ArrayListFragment.newInstance(it.getName(), it.getIsbn(), it.getId(),it.getDescription()
                    , it.getImage(), it.getPurchased(), it.getCondition()
                    ));
        }
        return fList;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_item_display);





        //test db
        Item item = new Item("test name", "isbn number", 4, "item description", "image src", DateFormat.getDateTimeInstance().format(new Date()), "condition");

        DbHandler db = new DbHandler(this);
        Log.d("Insert: ", "Inserting ..");
        db.insertItem(item);

        //print db
        Log.d("Reading: ", "Reading all items..");
        List<Item> items = db.getAllItems();

        for(Item it : items) {
            String log = "ID: " + it.getId() + ", Name: " + it.getName() + ", Description: " + it.getDescription()
                    + ", ISBN: " + it.getIsbn() + ", Image src: " + it.getImage() + ", Purchased: " + it.getPurchased()
                    + ", Condition: " + it.getCondition();

            //Writing items to log
            Log.d("Item: : ", log);

        }

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

        if (resultCode == RESULT_OK) {
            System.out.println("back with results");
        } else {
            System.out.println(resultCode);
        }
    }

}


