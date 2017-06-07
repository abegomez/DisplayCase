package myapp.abrahamjohngomez.com.displaycase;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.app.ActionBar;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Console;
import java.lang.reflect.Array;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import db.DbHandler;
//TODO: Image zoom/edit/delete image
public class SingleItemDisplayActivity extends AppCompatActivity{
    CollectionPagerAdapter mCollectionPagerAdapter;
    ViewPager mViewPager;
    List<Item> items = new ArrayList<>();
    List<Fragment> fragments = new ArrayList<>();
    public static final int ADD_ITEM_RESULT_CODE = 17;
    public static final int UPDATE_ITEM_CODE = 18;
    public static int count = 22;
    private Toolbar toolbar;
    private RelativeLayout bottomToolbar;
    private Item item;
    private final DbHandler db = new DbHandler(this);
    private String sortingOption = "";
    private List<Fragment> fList = new ArrayList<Fragment>();
    public static boolean favorited = false;
    /**
     *
     * String cols = query.get("cols");
     String selection = query.get("where");
     String selectionArgs = query.get("whereArgs");
     String groupBy = query.get("groupBy");
     String having = query.get("having");
     String orderBy = query.get("orderBy");
     *
     * @return
     */



    private List<Fragment> getFragmentsFromDb() {

        Log.d("Reading: ", "Reading all items..");
        HashMap<String, String> sortItemsMap = new HashMap<>();
        sortItemsMap.put("cols", DbHandler.KEY_NAME);
        sortItemsMap.put("where", " ");
        sortItemsMap.put("whereArgs", " ");
        sortItemsMap.put("groupBy", DbHandler.KEY_NAME );
        sortItemsMap.put("having", null);
        sortItemsMap.put("orderBy", DbHandler.KEY_NAME + " COLLATE NOCASE ASC");

        items = db.getAllItems(sortItemsMap);

        //items = db.getAllItems();

        for(Item it : items) {
            fList.add(ArrayListFragment.newInstance(it.getName(),
                it.getIsbn(), it.getId(),it.getDescription(),
                it.getImage(), it.getPurchased(), it.getCondition(),it.isFavorite())
            );
        }
        return fList;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_item_display);
        updateFragments();
        if(savedInstanceState != null) {
            mViewPager.setCurrentItem(savedInstanceState.getInt("currentPage"));
        }
        setupToolbars();
    }

    @Override
    public void onStart() {
        super.onStart();

    }
    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putInt("currentPage", mViewPager.getCurrentItem());
    }

    private void setupToolbars() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_single_item);
        setSupportActionBar(toolbar);
        bottomToolbar = (RelativeLayout) findViewById(R.id.bnButtons);
        if(!favorited) {
            bottomToolbar.findViewById(R.id.action_favorite)
                    .setBackground(getResources()
                            .getDrawable(android.R.drawable.btn_star_big_off));
        } else {
            bottomToolbar.findViewById(R.id.action_favorite)
                    .setBackground(getResources()
                            .getDrawable(android.R.drawable.btn_star_big_on));
        }
        for(int i = 0; i < bottomToolbar.getChildCount(); i++) {
            View child = bottomToolbar.getChildAt(i);
            child.setOnClickListener(new RelativeLayout.OnClickListener() {
                @Override
                public void onClick(View item) {
                int id = item.getId();
                switch (id) {
                    case R.id.action_add:
                        btScanNew();
                        break;
                    case R.id.action_delete:
                        //delete item
                        deleteFragment();
                        break;
                    case R.id.action_edit:
                        //edit item
                        updateCurrentFragment();
                        break;
                    case R.id.action_favorite:

                        break;
                    default:
                        break;
                }
                }
            });
        }


    }

    private void updateCurrentFragment() {
        Intent intent = new Intent(this, AddNewItemActivity.class);
        try {
            Item item = db.getSingleItem(getFragId(getCurrentFragment()));
            System.out.println(item.getName());
            if (item != null) {
                intent.putExtra("requestCode", UPDATE_ITEM_CODE);
                intent.putExtra("title", "Update Item");
                intent.putExtra("buttonText", "Update");
                intent.putExtra(AddNewItemActivity.ITEM, item);
                startActivityForResult(intent, UPDATE_ITEM_CODE);
            }
        } catch (Exception e) {}
    }

    private void deleteFragment() {
        final ArrayListFragment frag = getCurrentFragment();
        if(frag != null) {
            final int fragId = getFragId(frag);

            new AlertDialog.Builder(this)
                .setTitle(frag.getArguments().getString("itemName"))
                .setMessage("Delete?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    db.deleteItem(fragId);
                    db.close();
                    Log.d("Deleting", "Deleting " + frag.getArguments().getString("itemName"));
                    updateFragments();
                    Toast.makeText(SingleItemDisplayActivity.this, "Delete", Toast.LENGTH_SHORT).show();
                    }
                }).setNegativeButton(android.R.string.no, null).show();
        }
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
        intent.putExtra("title", "Add Item");
        intent.putExtra("buttonText", "Add Item");
        startActivityForResult(intent, ADD_ITEM_RESULT_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            item = data.getParcelableExtra("item");
            if(requestCode == ADD_ITEM_RESULT_CODE) {
                db.insertItem(item);
                updateFragments();
                mViewPager.setCurrentItem((mCollectionPagerAdapter.getCount() - 1), true);
            } else if(requestCode == UPDATE_ITEM_CODE) {
                item = data.getParcelableExtra("item");
                System.out.println("update code:" + db.updateItem(item));
                updateFragments();
            }
        } else {
            Log.d("return", "return with code: " + resultCode);
            Log.d("request", "request code: " + requestCode);
        }
        //db.close();



    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu, menu);

        MenuItem item = menu.findItem(R.id.sort);
        Spinner spinner = (Spinner) MenuItemCompat.getActionView(item);
        ArrayAdapter<CharSequence> spinner_Adapter = ArrayAdapter.createFromResource(this, R.array.sort_spinner_item_array, R.layout.sort_spinner);
        spinner_Adapter.setDropDownViewResource(R.layout.sort_spinner);

        spinner.setAdapter(spinner_Adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                ((TextView) view).setText("Sort");
                Toast.makeText(parent.getContext(),
                        "Selected: " + parent.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show();
                sortingOption = parent.getItemAtPosition(position).toString();
                updateFragments();
            }
            public void onNothingSelected(AdapterView<?> arg0) {}
        });
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
        if(mViewPager.getChildCount() >0) {
            int currentPage = mViewPager.getCurrentItem();
            ArrayListFragment aFrag = (ArrayListFragment) mCollectionPagerAdapter.getItem(currentPage);
            if(aFrag != null) {
                Log.d("nullfrag", "current frag found");
            }
            return aFrag;
        } else {
            return null;
        }
    }

    public int getFragId(ArrayListFragment aFrag) {
        return aFrag.getArguments().getInt("itemId");
    }

}


