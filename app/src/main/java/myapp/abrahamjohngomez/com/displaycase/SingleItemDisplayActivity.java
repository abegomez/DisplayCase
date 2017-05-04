package myapp.abrahamjohngomez.com.displaycase;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.Fragment;
import android.app.ActionBar;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;

import java.io.Console;
import java.lang.reflect.Array;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import db.DbHandler;
//TODO: Image zoom/edit/delete image
public class SingleItemDisplayActivity extends AppCompatActivity{
    CollectionPagerAdapter mCollectionPagerAdapter;
    ViewPager mViewPager;
    List<Item> items = new ArrayList<>();
    List<Fragment> fragments = new ArrayList<>();
    private static final int ADD_ITEM_RESULT_CODE = 17;
    public static int count = 22;
    private Toolbar toolbar;
    private RelativeLayout bottomToolbar;

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

        setupToolbars();
        updateFragments();
    }

    private void setupToolbars() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_single_item);
        setSupportActionBar(toolbar);
        bottomToolbar = (RelativeLayout) findViewById(R.id.bnButtons);

        for(int i = 0; i < bottomToolbar.getChildCount(); i++) {
            View child = bottomToolbar.getChildAt(i);

            child.setOnClickListener(new RelativeLayout.OnClickListener() {
                @Override
                public void onClick(View item) {
                    int id = item.getId();
                    switch (id) {
                        case R.id.action_add:
                            Log.d("hello", "testing the add button");
                            btScanNew();
                            break;
                        case R.id.action_delete:
                            //delete item
                            deleteFragment();
                            break;
                        case R.id.action_edit:
                            //edit item
                            break;
                        case R.id.action_favorite:
                            //favorite item
                            break;
                        default:
                            break;
                    }
                }
            });
        }
    }

    private void deleteFragment() {
        //TODO: add delete warning
        DbHandler db = new DbHandler(this);
        ArrayListFragment frag = getCurrentFragment();
        int fragId = getFragId(frag);
        db.deleteItem(fragId);
        db.close();
        Log.d("Deleting", "Deleting " + frag.getArguments().getString("itemName"));
        updateFragments();
    }

    private void updateFragments() {
        fragments = getFragmentsFromDb();
        if(mCollectionPagerAdapter == null) {
            mCollectionPagerAdapter = new CollectionPagerAdapter(
                    getSupportFragmentManager(), fragments);

            mViewPager = (ViewPager) findViewById(R.id.pagersingleitem);
            mViewPager.setAdapter(mCollectionPagerAdapter);
        } else {
            mCollectionPagerAdapter.updateData(fragments);
        }
    }

    public void btScanNew() {
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
            db.close();
            updateFragments();
            mViewPager.setCurrentItem((mCollectionPagerAdapter.getCount() - 1), true);
        } else {
            System.out.println(resultCode);
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.menuCardView:
                Intent newIntent = new Intent(this, CardViewActivity.class);
                startActivity(newIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public ArrayListFragment getCurrentFragment() {
        int currentPage = mViewPager.getCurrentItem();
        ArrayListFragment aFrag = (ArrayListFragment) mCollectionPagerAdapter.getItem(currentPage);
        return aFrag;
    }

    public int getFragId(ArrayListFragment aFrag) {
        return aFrag.getArguments().getInt("itemId");
    }

}


