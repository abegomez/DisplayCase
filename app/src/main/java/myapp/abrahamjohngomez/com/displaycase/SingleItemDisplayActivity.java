package myapp.abrahamjohngomez.com.displaycase;

import android.content.Intent;
import android.support.v4.app.Fragment;

import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class SingleItemDisplayActivity extends FragmentActivity {
    CollectionPagerAdapter mCollectionPagerAdapter;
    ViewPager mViewPager;
    private static final int ADD_ITEM_RESULT_CODE = 17;

    public static int count = 10;

    private List<Fragment> getFragments() {
        Item newItem = new Item();
        newItem.setName("Another Item");
        newItem.setDescription("Here is a different item.");
        newItem.setId(344442);
        newItem.setIsbn(2342345);
        List<Fragment> fList = new ArrayList<Fragment>();
        fList.add(ArrayListFragment.newInstance("Test name", 1234, 2345, "Item Description"));
        fList.add(ArrayListFragment.newInstance("A different name", 233, 44555, "This is a different item"));
        fList.add(ArrayListFragment.newInstance(newItem));
        return fList;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_item_display);

        List<Fragment> fragments = getFragments();
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


