package com.UV.rokonalzorobyan.UV.Model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;
public class cuurentItem {

    @SerializedName("Cart_quantity")
    @Expose
    private String cartQuantity;
    @SerializedName("itemdetails")
    @Expose
    private List<Itemdetail> itemdetails = null;

    public String getCartQuantity() {
        return cartQuantity;
    }

    public void setCartQuantity(String cartQuantity) {
        this.cartQuantity = cartQuantity;
    }

    public List<Itemdetail> getItemdetails() {
        return itemdetails;
    }

    public void setItemdetails(List<Itemdetail> itemdetails) {
        this.itemdetails = itemdetails;
    }

}
