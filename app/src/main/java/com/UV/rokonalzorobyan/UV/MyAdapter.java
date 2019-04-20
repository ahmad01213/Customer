package com.UV.rokonalzorobyan.UV;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.UV.rokonalzorobyan.UV.Api.UserClient;
import com.UV.rokonalzorobyan.UV.Api.apiRequist;
import com.UV.rokonalzorobyan.UV.Model.cartItem;
import java.util.ArrayList;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    ArrayList<cartItem> orders;
    ArrayList<String[]> localOrders;
    static int whichOpened=0;
    static int dataSize=0;
    ProgressDialog progressdialog;
    String itemId="";
    UserClient userClient=apiRequist.sendRequist();
    Context context;
    public MyAdapter(ArrayList<cartItem> mOrders,ArrayList<String[]> local, Context ctx) {
        this.orders=mOrders;
        this.localOrders=local;
        context=ctx;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View layoutView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.cart_food_raw, null);
        return new MyViewHolder(layoutView);
    }
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i){
        try {
            itemId=orders.get(i).getItemdetails().get(0).getItemKey()+"";
        } catch (Exception e) {
            e.printStackTrace();
        }
        myViewHolder.itemId.setText(itemId);
        try {
            if (MainActivity.tokken.length()>10){
                bindViews(myViewHolder,orders.get(i).getItemdetails().get(0).getItemName(),
                        orders.get(i).getCartQuantity(),
                        orders.get(i).getItemdetails().get(0).getItemImage(),
                        orders.get(i).getItemdetails().get(0).getItemPrice(),
                        orders.get(i).getItemdetails().get(0).getItemKey()+"",
                        orders.get(i).getItemdetails().get(0).getItemDetails(),
                        orders.get(i).getItemdetails().get(0).getItemPrice()+" ريال"
                );
            }
            else {
                bindViews(myViewHolder,localOrders.get(i)[0],localOrders.get(i)[1],
                        localOrders.get(i)[2],localOrders.get(i)[3],localOrders.get(i)[4],localOrders.get(i)[5],localOrders.get(i)[3]+"  ريال");
            }
            myViewHolder.btnDeletfromcart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (MainActivity.tokken.length()>10){
                        showdialog();
                        try {
                            itemId=orders.get(i).getItemdetails().get(0).getItemKey()+"";
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        deleteFoodToCart(itemId);
                    }
                    else{
                        SharedPreferences settings =context.getSharedPreferences("zorobian_customer_FOODS_pref_name", 0);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.remove(localOrders.get(i)[4]);
                        editor.apply();
                    }
                }
            });
        }catch (Exception e){
        }
        myViewHolder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                LinearLayout relativeLayout=(LinearLayout) v.getParent();
                TextView tex=relativeLayout.findViewById(R.id.id_order);
                int intres=0;
                intres=Integer.parseInt(myViewHolder.itemQuantity.getText().toString())+1;
                myViewHolder.itemQuantity.setText(intres+"");
                if (MainActivity.tokken.length()>10){
                    plusMinus(tex.getText().toString(),"1");
                }else{
                }
            }
        });
        myViewHolder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                LinearLayout relativeLayout=(LinearLayout) v.getParent();
                TextView tex=relativeLayout.findViewById(R.id.id_order);
                if (Integer.parseInt(myViewHolder.itemQuantity.getText().toString())!=1){
                    int intres=0;
                    intres=Integer.parseInt(myViewHolder.itemQuantity.getText().toString())-1;
                    myViewHolder.itemQuantity.setText(intres+"");
                    if (MainActivity.tokken.length()>10){
                        try {
                        } catch (Exception e){
                            e.printStackTrace();
                        }
                        plusMinus(tex.getText().toString(),"0");
                    }
                }else{
                }
            }
        });
    }
    @Override
    public int getItemCount() {
        return dataSize;
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView itemName,itemPrice,itemQuantity,itemDesc,itemId,plus,minus;
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
            plus=itemView.findViewById(R.id.plus);
            minus=itemView.findViewById(R.id.minus);
        }
    }
    private void deleteFoodToCart(String itemId){
        Call<ResponseBody> call=userClient.delteRequistToCart(MainActivity.tokken,Integer.parseInt(itemId));
        call.enqueue(new Callback<ResponseBody>(){
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response){
                if (response.isSuccessful()){
                    progressdialog.cancel();
                    Toast.makeText(context, "تمت إزالة الطلب من سلة المشتريات", Toast.LENGTH_SHORT).show();
                }else{
                    progressdialog.cancel();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t){
                progressdialog.cancel();
                Toast.makeText(context,"الرجاءالتأكد من الاتصال بالإنترنت",Toast.LENGTH_LONG).show();
            }
        });
    }
    private void bindViews(MyViewHolder myViewHolder,String name,String quantity,String img,String cost,String idOrder,String itemDetail,String itemCost){
        myViewHolder.itemName.setText(name);
        myViewHolder.itemPrice.setText(cost+" ريال");
        myViewHolder.itemQuantity.setText(quantity);
        myViewHolder.itemDesc.setText(itemDetail);
        myViewHolder.itemPrice.setText(itemCost);
        Glide.with(context)
                .load(img)
                .into(myViewHolder.itemImage);
    }
    public void plusMinus(String id,String plus){
        orders=new ArrayList<>();
        UserClient userClient = apiRequist.sendRequist();
        Call<ResponseBody> call = userClient.ChartPlusMinus(MainActivity.tokken,id,plus);
        call.enqueue(new Callback<ResponseBody>(){
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                }else{
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });
    }
    public void minus(String id){
        orders=new ArrayList<>();
        UserClient userClient = apiRequist.sendRequist();
        Call<ResponseBody> call = userClient.minus(MainActivity.tokken,id);
        call.enqueue(new Callback<ResponseBody>(){
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                }else{
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });
    }
    public void showdialog(){
        progressdialog = new ProgressDialog(context);
        progressdialog.setMessage("الرجاء الإنتظار ....");
        progressdialog.setCancelable(false);
        progressdialog.show();
    }
}
