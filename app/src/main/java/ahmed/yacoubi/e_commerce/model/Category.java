package ahmed.yacoubi.e_commerce.model;

import android.graphics.Bitmap;
import android.net.Uri;

public class Category {

    private String id;
    private String name;
    private String desc;
    private Bitmap bitmap;
    private boolean isSmall;
    private String image;

    public Category() {
    }

    public Category(String id, String name, String desc, Bitmap bitmap, boolean isSmall, String image) {
        this.id = id;
        this.name = name;
        this.desc = desc;
        this.bitmap = bitmap;
        this.isSmall = isSmall;
        this.image = image;
    }

    public Category(String id, String name, String desc, String image, boolean isSmall) {
        this.id = id;
        this.name = name;
        this.desc = desc;
        this.image = image;
        this.isSmall = isSmall;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isSmall() {
        return isSmall;
    }

    public void setSmall(boolean small) {
        isSmall = small;
    }
}
