package myapp.abrahamjohngomez.com.displaycase;

import android.os.Bundle;
import android.support.v4.app.BundleCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewGroupCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * Created by ryuhyoko on 10/30/2016.
 */

public class ObjectFragment extends Fragment {
    public static final String ITEM_NAME = "Item name here";
    public static final String ITEM_DESCRIPTION = "Item description here";

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.fragment_object_layout, container, false);

        Bundle args = getArguments();
        TextView itemName = (TextView) rootView.findViewById(R.id.tvItemName);
        itemName.setText(args.getString(ITEM_NAME));
        TextView textTest = (TextView) rootView.findViewById(R.id.tvDescription);
        textTest.setText(args.getString(ITEM_DESCRIPTION));
        return rootView;
    }
}
