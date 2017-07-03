package db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import myapp.abrahamjohngomez.com.displaycase.Item;

/**
 * Created by ryuhyoko on 4/23/2017.
 * Copyright Abraham J Gomez 2017
 *
 */

public class DbHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    //database name
    private static final String DATABASE_NAME = "collectionInventory";
    //table name
    private static final String TABLE_ITEMS = "collection";

    //table columns
    public static final String KEY_ID ="id";
    public static final String KEY_NAME = "name";
    public static final String KEY_ISBN = "isbn";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_PURCHASE_DATE = "purchase_date";
    public static final String KEY_CONDITION = "condition";
    public static final String KEY_DATE_ADDED = "added_date";
    public static final String KEY_FAVORITE = "favorite";
    private String query = "";
    /*
    Item data
    protected String name;
    protected String isbn;
    protected int id;
    protected String description;
    protected String image;
    protected Date purchased;
    protected String condition;

     */

    public DbHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_ITEMS
                + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_NAME + " TEXT,"
                + KEY_DESCRIPTION + " TEXT,"
                + KEY_ISBN + " TEXT,"
                + KEY_IMAGE + " TEXT,"
                + KEY_PURCHASE_DATE + " TEXT,"
                + KEY_CONDITION + " TEXT,"
                + KEY_DATE_ADDED + " TEXT default current_timestamp,"
                + KEY_FAVORITE + " INTEGER"
                + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS);
        onCreate(db);
    }

    public boolean insertItem(Item item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        //store item details in contentvalue
        values.put(KEY_NAME, item.getName());
        values.put(KEY_DESCRIPTION, item.getDescription());
        values.put(KEY_ISBN, item.getIsbn());
        values.put(KEY_IMAGE, item.getImage());
        values.put(KEY_PURCHASE_DATE, item.getPurchased());
        values.put(KEY_CONDITION, item.getCondition());
        values.put(KEY_FAVORITE, item.isFavorite());
        //insert row
        db.insert(TABLE_ITEMS, null, values);
        db.close();

        return true;
    }

    public Integer deleteItem(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_ITEMS, "id = ? ", new String[] {Integer.toString(id)});
    }
    public Integer updateItem(Item item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        String[] whereArgs = {String.valueOf(item.getId())};
        values.put(KEY_NAME, item.getName());
        values.put(KEY_DESCRIPTION, item.getDescription());
        values.put(KEY_ISBN, item.getIsbn());
        values.put(KEY_IMAGE, item.getImage());
        values.put(KEY_PURCHASE_DATE, item.getPurchased());
        values.put(KEY_CONDITION, item.getCondition());
        System.out.println("updating: " +item.getName());
        return db.update(TABLE_ITEMS, values, "id = ?", whereArgs);
    }
    public Integer updateFavorite(Item item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        String[] whereArgs = {String.valueOf(item.getId())};
        values.put(KEY_FAVORITE, item.isFavorite());
        System.out.println(item.getName() + "Favorited!");
        return db.update(TABLE_ITEMS, values, "id=?", whereArgs);
    }

    /**
     * Retrieve database items with specific sorted order
     * order by name:asc/desc, recent:asc/desc, purchase data:asc/desc, favorites
     */
    public List<Item> getAllItems(HashMap<String, String> query) {
        String table = TABLE_ITEMS;
        String cols = query.get("cols");
        String selection = query.get("where");
        String selectionArgs = query.get("whereArgs");
        String groupBy = query.get("groupBy");
        String having = query.get("having");
        String orderBy = query.get("orderBy");

        List<Item> sortedList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.query(table,null,null,null,groupBy,having,orderBy);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            Item item = new Item();
            item.setId(Integer.parseInt(res.getString(0)));
            item.setName(res.getString(1));
            item.setDescription(res.getString(2));
            item.setIsbn(res.getString(3));
            item.setImage(res.getString(4));
            item.setPurchased(res.getString(5));
            item.setCondition(res.getString(6));
            item.setDateAdded(res.getString(7));
            //get string from each column and put into item
            sortedList.add(item);
            res.moveToNext();
        }
        res.close();
        return sortedList;
    }

    public List<Item> getAllItems() {
        List<Item> itemArrayList = new ArrayList<Item>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_ITEMS, null);
        res.moveToFirst();

        while(res.isAfterLast() == false) {
            Item item = new Item();
            item.setId(Integer.parseInt(res.getString(0)));
            item.setName(res.getString(1));
            item.setDescription(res.getString(2));
            item.setIsbn(res.getString(3));
            item.setImage(res.getString(4));
            item.setPurchased(res.getString(5));
            item.setCondition(res.getString(6));
            item.setDateAdded(res.getString(7));
            //get string from each column and put into item
            itemArrayList.add(item);
            res.moveToNext();
        }
        db.close();
        return itemArrayList;
    }

    public Item getSingleItem(int itemId) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = null;
        Item item = null;
        String sql = "SELECT * FROM " + TABLE_ITEMS + " WHERE id = " + itemId;

        try {
            res = db.rawQuery(sql, null);
            if (res.moveToFirst()) {
                item = new Item();
                int id = Integer.parseInt(res.getString(res.getColumnIndex("id")));
                item.setId(id);
                item.setName(res.getString(res.getColumnIndex(KEY_NAME)));
                item.setDescription(res.getString(res.getColumnIndex(KEY_DESCRIPTION)));
                item.setIsbn(res.getString(res.getColumnIndex(KEY_ISBN)));
                item.setImage(res.getString(res.getColumnIndex(KEY_IMAGE)));
                item.setPurchased(res.getString(res.getColumnIndex(KEY_PURCHASE_DATE)));
                item.setCondition(res.getString(res.getColumnIndex(KEY_CONDITION)));
                item.setDateAdded(res.getString(res.getColumnIndex(KEY_DATE_ADDED)));
            }
            return item;
        } finally {
            db.close();
            res.close();
        }
    }
}
