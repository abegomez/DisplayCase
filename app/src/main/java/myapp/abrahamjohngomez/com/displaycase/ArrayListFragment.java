/**
 * Copyright (C) 2017  Some Copyright Goes Here
 */

package myapp.abrahamjohngomez.com.displaycase;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by ryuhyoko on 4/18/2017.
 * Fragments that contain individual item data.
 * Fragments are created using Item object or item data parameters
 * of the following:
 *
 * protected String name;
 * protected String isbn;
 * protected int id;
 * protected String description;
 * protected String image;
 * protected String purchased;
 * protected String condition;
 *
 * @author Abraham Gomez
 */

public class ArrayListFragment extends Fragment {

    //create a new instance of Counting frag, providing num as an argument
    static ArrayListFragment newInstance(int num) {
        ArrayListFragment f = new ArrayListFragment();

        //supply num input as an argument
        Bundle args = new Bundle();
        args.putInt("num", num);
        f.setArguments(args);
        return f;
    }
    static ArrayListFragment newInstance(String itemName, String isbn, int id, String itemDescription,
                                         String image, String purchased, String condition) {
        ArrayListFragment f = new ArrayListFragment();
        Bundle args = new Bundle();
        args.putString("itemName", itemName);
        args.putString("itemIsbn", isbn);
        args.putInt("itemId", id);
        args.putString("itemDescription", itemDescription);
        args.putString("itemImageSrc", image);
        args.putString("itemPurchased", purchased);
        args.putString("itemCondition", condition);
        f.setArguments(args);
        return f;
    }

    static ArrayListFragment newInstance(Item item) {
        ArrayListFragment f = new ArrayListFragment();
        Bundle args = new Bundle();
        args.putString("itemName", item.getName());
        args.putString("itemIsbn", item.getIsbn());
        args.putInt("itemId", item.getId());
        args.putString("itemDescription", item.getDescription());
        args.putString("itemImageSrc", item.getImage());
        args.putString("itemPurchased", item.getPurchased());
        args.putString("itemCondition", item.getCondition());

        f.setArguments(args);
        return f;
    }
    //retreive this instance's number from its arguments
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    //fragment ui
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle args = getArguments();
        View v = inflater.inflate(R.layout.fragment_object_layout, container, false);
        View tvName = v.findViewById(R.id.tvItemName);
        View tvDescription = v.findViewById(R.id.tvDescription);
        View tvIsbn = v.findViewById(R.id.tvIsbn);
        View tvId = v.findViewById(R.id.tvId);
        View ivImage = v.findViewById(R.id.ivImage);
        View tvPurchased = v.findViewById(R.id.tvPurchased);
        View tvCondition = v.findViewById(R.id.tvCondition);
        ((TextView)tvName).setText(args.getString("itemName"));
        ((TextView)tvDescription).setText(args.getString("itemDescription"));
        ((TextView)tvIsbn).setText("ISBN:" + args.getString("itemIsbn"));
        ((TextView)tvId).setText("ID:" + String.valueOf(args.getInt("itemId")));
        ((TextView)tvPurchased).setText("Purchased:" + args.getString("itemPurchased"));
        ((TextView)tvCondition).setText("Condition:" + args.getString("itemCondition"));

        try {
            boolean validUri = args.getString("itemImageSrc")!= null;
            if(validUri) {
                RequestOptions options = new RequestOptions();
                options.fitCenterTransform();
                Glide
                    .with(ivImage.getContext())
                    .load(args.getString("itemImageSrc"))
                    .apply(RequestOptions.fitCenterTransform())
                    .apply(RequestOptions.centerCropTransform())
                    .into((ImageView)ivImage);
//                BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
//                InputStream input = getActivity().getContentResolver()
//                        .openInputStream(Uri.parse(args.getString("itemImageSrc")));
//                Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
//                input.close();
//                ((ImageView) ivImage).setImageBitmap(bitmap);
            }

        } catch (NullPointerException e1) {
            e1.printStackTrace();
        }
        return v;
    }

//    @Override
//    public void onActivityCreated(Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        setListAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1));
//    }
}
