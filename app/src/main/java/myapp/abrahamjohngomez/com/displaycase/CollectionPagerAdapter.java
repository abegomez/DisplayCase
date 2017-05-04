package myapp.abrahamjohngomez.com.displaycase;

import android.support.v4.app.Fragment;
import android.os.Bundle;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.util.Log;

import java.io.Console;
import java.util.List;

/**
 * Created by ryuhyoko on 10/30/2016.
 */

public class CollectionPagerAdapter extends FragmentStatePagerAdapter {
    private static int count = 1;
    private List<Fragment> fragments;

    public CollectionPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }
    public void updateData(List<Fragment> data) {
        fragments = data;
        this.notifyDataSetChanged();
    }
    @Override
    public Fragment getItem(int position) {
        return this.fragments.get(position);
    }

    @Override
    public int getItemPosition(Object object) {
        int index = fragments.indexOf(object);
        if(index == -1)
            return PagerAdapter.POSITION_NONE;
        else
            return index;
    }
    @Override
    public int getCount() {
        return this.fragments.size();
    }
    @Override
    public CharSequence getPageTitle(int position) {
        return "OBJECT " + (position + 1);
    }
}
