package myapp.abrahamjohngomez.com.displaycase;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import db.DbHandler;

public class CardViewActivity extends AppCompatActivity {
    private List<Item> items = new ArrayList<>();
    private RecyclerView recyclerView;
    private CardAdapter cardAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_view);
        
        DbHandler db = new DbHandler(this);
        items = db.getAllItems();
        cardAdapter = new CardAdapter(items);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(cardAdapter);
        prepareItemData();

    }

    private void prepareItemData() {
        cardAdapter.notifyDataSetChanged();
    }
}
