package com.UV.rokonalzorobyan.UV;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.UV.rokonalzorobyan.UV.Api.UserClient;
import com.UV.rokonalzorobyan.UV.Api.apiRequist;
import com.UV.rokonalzorobyan.UV.Model.cartItem;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReceitAdapter extends RecyclerView.Adapter<ReceitAdapter.MyViewHolder> {
    ArrayList<cartItem> orders;
    UserClient userClient=apiRequist.sendRequist();
    Context context;
    public ReceitAdapter(ArrayList<cartItem> mOrders, Context ctx) {
        this.orders=mOrders;
        context=ctx;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View layoutView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.receit_item, null);
        return new MyViewHolder(layoutView);
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int i) {
        try {
                bindViews(myViewHolder,orders.get(i).getItemdetails().get(0).getItemName(),
                        orders.get(i).getCartQuantity(),
                        orders.get(i).getItemdetails().get(0).getItemImage(),
                        orders.get(i).getItemdetails().get(0).getItemPrice(),
                        orders.get(i).getItemdetails().get(0).getItemKey()+"",
                        orders.get(i).getItemdetails().get(0).getItemDetails());
        }catch (Exception e){
        }
    }
    @Override
    public int getItemCount() {
        return orders.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView itemName,itemPrice,itemQuantity,itemDesc,itemId;
        ImageView itemImage;
        Button btnDeletfromcart;
        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            itemName=itemView.findViewById(R.id.food_raw_title);
            itemPrice=itemView.findViewById(R.id.food_raw_cost);
            itemDesc=itemView.findViewById(R.id.food_raw_desc);
            itemQuantity=itemView.findViewById(R.id.res_count);
            btnDeletfromcart=itemView.findViewById(R.id.food_raw_delete);
            itemImage=itemView.findViewById(R.id.food_raw_image);
            itemId=itemView.findViewById(R.id.id_order);
            itemId=itemView.findViewById(R.id.id_order);
        }
    }
    private void bindViews(MyViewHolder myViewHolder,String name,String quantity,String img,String cost,String idOrder,String itemDetail){
        myViewHolder.itemName.setText(name);
        myViewHolder.itemPrice.setText(cost+" ريال");
        myViewHolder.itemQuantity.setText(quantity);
        myViewHolder.itemDesc.setText(itemDetail);
        Glide.with(context)
                .load(img)
                .into(myViewHolder.itemImage);
    }
}
