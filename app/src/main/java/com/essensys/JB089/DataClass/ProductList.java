package com.essensys.JB089.DataClass;
import com.google.gson.annotations.SerializedName;
public class ProductList
{
    @SerializedName("product_id")
    private String product_id;

    @SerializedName("name")
    private String name;

    @SerializedName("description")
    private String description;

    @SerializedName("price")
    private String price;

    @SerializedName("amazone_url")
    private String amazone_url;

    @SerializedName("flipkart_url")
    private String flipkart_url;

    @SerializedName("image")
    private String image;
    public String getProduct_id() {
        return product_id;
    }
    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getPrice() {
        return price;
    }
    public void setPrice(String price) {
        this.price = price;
    }
    public String getAmazone_url() {
        return amazone_url;
    }
    public void setAmazone_url(String amazone_url) {
        this.amazone_url = amazone_url;
    }
    public String getFlipkart_url() {
        return flipkart_url;
    }
    public void setFlipkart_url(String flipkart_url) {
        this.flipkart_url = flipkart_url;
    }
    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }
}
