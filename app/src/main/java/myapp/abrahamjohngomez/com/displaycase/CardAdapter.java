package myapp.abrahamjohngomez.com.displaycase;

/**
 * Created by ryuhyoko on 4/27/2017.
 */
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.ViewGroup;
import android.view.LayoutInflater;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.MyViewHolder> {

    private List<Item> items = new ArrayList<Item>();
    Context context;
    Bitmap bitmap;
    GetImageThumbnail imageThumbnail;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView itemName, itemDescription;
        public ImageView imageView;
        public CardView cv;

        View view;
        public MyViewHolder(View view) {
            super(view);
            this.view = view;
            cv = (CardView) view.findViewById(R.id.cvItems);
            itemName = (TextView) view.findViewById(R.id.tvCardItemName);
            itemDescription = (TextView) view.findViewById(R.id.tvCardItemDescription);
            imageView = (ImageView) view.findViewById(R.id.ivCardImage);
            //imageThumbnail = new GetImageThumbnail();
        }
    }
    public CardAdapter(List<Item> itemsList) { this.items = itemsList; }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate
                (R.layout.card_object_layout, parent, false);
        context = parent.getContext();
        return new MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Item item = items.get(position);
        holder.itemName.setText(item.getName());
        //holder.itemName.getBackground().setAlpha(50);
        //holder.itemDescription.setText(item.getDescription());
        if(item.getImage() != null) {
            Glide.with(holder.imageView.getContext())
                    .load(item.getImage()).apply(RequestOptions.centerCropTransform())
                    .into(holder.imageView);
        } else {
            Glide.with(holder.imageView.getContext())
                    .load(R.drawable.placeholder).apply(RequestOptions.centerCropTransform())
                    .into(holder.imageView);
        }
        Log.d("image source", item.getImage() + " item");
    }
    @Override
    public int getItemCount() { return items.size(); }
}
