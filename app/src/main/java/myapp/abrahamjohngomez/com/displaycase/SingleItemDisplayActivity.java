package myapp.abrahamjohngomez.com.displaycase;

import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SingleItemDisplayActivity extends FragmentActivity {
    CollectionPagerAdapter mCollectionPagerAdapter;
    ViewPager mViewPager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_item_display);

        mCollectionPagerAdapter = new CollectionPagerAdapter(
                getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.pagersingleitem);
        mViewPager.setAdapter(mCollectionPagerAdapter);

    }
}


