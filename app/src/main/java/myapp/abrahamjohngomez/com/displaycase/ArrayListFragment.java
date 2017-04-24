package myapp.abrahamjohngomez.com.displaycase;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by ryuhyoko on 4/18/2017.
 */

public class ArrayListFragment extends Fragment {
    int mNum;

    //create a new isntance of Counting frag, providing num as an argument
    static ArrayListFragment newInstance(int num) {
        ArrayListFragment f = new ArrayListFragment();

        //supply num input as an argument
        Bundle args = new Bundle();
        args.putInt("num", num);
        f.setArguments(args);
        return f;
    }
    static ArrayListFragment newInstance(String itemName, long isbn, int id, String itemDescription) {
        ArrayListFragment f = new ArrayListFragment();
        Bundle args = new Bundle();
        args.putString("itemName", itemName);
        args.putLong("itemIsbn", isbn);
        args.putInt("itemId", id);
        args.putString("itemDescription", itemDescription);
        f.setArguments(args);
        return f;
    }
    static ArrayListFragment newInstance(Item item) {
        ArrayListFragment f = new ArrayListFragment();
        Bundle args = new Bundle();
        args.putString("itemName", item.getName());
        args.putLong("itemIsbn", item.getIsbn());
        args.putInt("itemId", item.getId());
        args.putString("itemDescription", item.getDescription());
        f.setArguments(args);
        return f;
    }

    //retreive this instance's number from its arguments
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNum = getArguments() != null ? getArguments().getInt("num") : 1;
    }

    //fragment ui
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle args = getArguments();
        View v = inflater.inflate(R.layout.fragment_object_layout, container, false);
        View tvName = v.findViewById(R.id.tvItemName);
        View tvDescription = v.findViewById(R.id.tvDescription);
        View tvIsbn = v.findViewById(R.id.tvIsbn);
        ((TextView)tvName).setText(args.getString("itemDescription"));
        ((TextView)tvDescription).setText(args.getString("itemDescription"));
        ((TextView)tvIsbn).setText("ISBN:" + String.valueOf(args.getLong("itemIsbn")));

        return v;
    }

//    @Override
//    public void onActivityCreated(Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        setListAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1));
//    }
}