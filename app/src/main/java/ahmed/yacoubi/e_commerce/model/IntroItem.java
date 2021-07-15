package ahmed.yacoubi.e_commerce.model;

public class IntroItem {

    private int image;
    private String text;
    private String text2;

    public IntroItem() {
    }

    public IntroItem(int image, String text, String text2) {
        this.image = image;
        this.text = text;
        this.text2 = text2;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText2() {
        return text2;
    }

    public void setText2(String text2) {
        this.text2 = text2;
    }
}
