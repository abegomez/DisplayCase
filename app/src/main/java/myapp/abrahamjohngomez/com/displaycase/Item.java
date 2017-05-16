package myapp.abrahamjohngomez.com.displaycase;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by ryuhyoko on 4/17/2017.
 */
//TODO: finish doing the updateFragment method reusing the additem activity
public class Item implements Parcelable{
    protected String name;
    protected String isbn;
    protected int id;
    protected String description;
    protected String image;
    protected String purchased;
    protected String condition;
    protected String collection;

    public  Item() {}

    public Item(String name, String isbn, int id, String description, String image, String purchased, String condition) {
        this.name = name;
        this.isbn = isbn;
        this.id = id;
        this.description = description;
        this.image = image;
        this.purchased = purchased;
        this.condition = condition;
    }
    public Item(String name, String isbn, String description, String image, String purchased, String condition) {
        this.name = name;
        this.isbn = isbn;
        this.description = description;
        this.image = image;
        this.purchased = purchased;
        this.condition = condition;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPurchased() {
        return purchased;
    }

    public void setPurchased(String purchased) {
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

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.isbn);
        dest.writeInt(this.id);
        dest.writeString(this.description);
        dest.writeString(this.image);
        dest.writeString(this.purchased);
        dest.writeString(this.condition);
        dest.writeString(this.collection);
    }

    protected Item(Parcel in) {
        this.name = in.readString();
        this.isbn = in.readString();
        this.id = in.readInt();
        this.description = in.readString();
        this.image = in.readString();
        this.purchased = in.readString();
        this.condition = in.readString();
        this.collection = in.readString();
    }

    public static final Creator<Item> CREATOR = new Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel source) {
            return new Item(source);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };
}
