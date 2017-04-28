package myapp.abrahamjohngomez.com.displaycase;

/**
 * Created by ryuhyoko on 4/27/2017.
 */
import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.ViewGroup;
import android.view.LayoutInflater;

import java.util.ArrayList;
import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.MyViewHolder> {

    private List<Item> items = new ArrayList<Item>();
    Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView itemName, itemDescription;
        public ImageView imageView;
        public CardView cv;

        public MyViewHolder(View view) {
            super(view);
            cv = (CardView) view.findViewById(R.id.cvItems);
            itemName = (TextView) view.findViewById(R.id.tvCardItemName);
            itemDescription = (TextView) view.findViewById(R.id.tvCardItemDescription);
        }
    }
    public CardAdapter(List<Item> itemsList) { this.items = itemsList; }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate
                (R.layout.card_object_layout, parent, false);
        return new MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Item item = items.get(position);
        holder.itemName.setText(item.getName());
        holder.itemDescription.setText(item.getDescription());
    }
    @Override
    public int getItemCount() { return items.size(); }
}
