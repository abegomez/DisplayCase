package myapp.abrahamjohngomez.com.displaycase;

import android.os.Parcel;

import java.util.Date;

/**
 * Created by ryuhyoko on 4/17/2017.
 */

public class Item {

    protected String name;
    protected long isbn;
    protected int id;
    protected String description;
    protected String image;
    protected Date purchased;
    protected String condition;


    protected String collection;

    public  Item() {

    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Date getPurchased() {
        return purchased;
    }

    public void setPurchased(Date purchased) {
        this.purchased = purchased;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getIsbn() {
        return isbn;
    }

    public void setIsbn(long isbn) {
        this.isbn = isbn;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCollection() { return collection; }

    public void setCollection(String collection) { this.collection = collection; }
}
