package db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import myapp.abrahamjohngomez.com.displaycase.Item;

/**
 * Created by ryuhyoko on 4/23/2017.
 */

public class DbHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    //database name
    private static final String DATABASE_NAME = "collectionInventory";
    //table name
    private static final String TABLE_ITEMS = "collection";

    //table columns
    private static final String KEY_ID ="id";
    private static final String KEY_NAME = "name";
    private static final String KEY_ISBN = "isbn";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_IMAGE = "image";
    private static final String KEY_PURCHASE_DATE = "purchase_date";
    private static final String KEY_CONDITION = "condition";

    /*ql
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
                + KEY_CONDITION + " TEXT"
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
        values.put(KEY_NAME, item.getName());
        values.put(KEY_DESCRIPTION, item.getDescription());
        values.put(KEY_ISBN, item.getIsbn());
        values.put(KEY_IMAGE, item.getImage());
        values.put(KEY_PURCHASE_DATE, item.getPurchased());
        values.put(KEY_CONDITION, item.getCondition());
        return db.update(TABLE_ITEMS, values, KEY_ID + " = ?", new String[]{String.valueOf(item.getId())});
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

            //get string from each column and put into item
            itemArrayList.add(item);
            res.moveToNext();
        }
        db.close();
        return itemArrayList;
    }

    public Item getSingleItem(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = null;
        Item item = null;
        String sql = "SELECT * FROM " + TABLE_ITEMS + " WHERE id = " + id;

        try {
            res = db.rawQuery(sql, null);
            if (res.moveToFirst()) {
                item = new Item();
                item.setId(id);
                item.setName(res.getString(res.getColumnIndex(KEY_NAME)));
                item.setDescription(res.getString(res.getColumnIndex(KEY_DESCRIPTION)));
                item.setIsbn(res.getString(res.getColumnIndex(KEY_ISBN)));
                item.setImage(res.getString(res.getColumnIndex(KEY_IMAGE)));
                item.setPurchased(res.getString(res.getColumnIndex(KEY_PURCHASE_DATE)));
                item.setCondition(res.getString(res.getColumnIndex(KEY_CONDITION)));
            }
            return item;
        } finally {
            db.close();
            res.close();
        }
    }
}
