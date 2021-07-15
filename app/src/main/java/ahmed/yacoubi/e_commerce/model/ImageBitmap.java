package ahmed.yacoubi.e_commerce.model;

public class ImageBitmap {
    private byte[] bytes;
    private String name;

    public ImageBitmap(byte[] bytes, String name) {
        this.bytes = bytes;
        this.name = name;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
