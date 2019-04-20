package com.UV.rokonalzorobyan.UV;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.UV.rokonalzorobyan.UV.Api.UserClient;
import com.UV.rokonalzorobyan.UV.Api.apiRequist;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class FoodDetailActivity extends AppCompatActivity implements View.OnClickListener {
    Button add;
    UserClient userClient=apiRequist.sendRequist();
    TextView plus,minus,res;
    Button completePrice;
    ProgressDialog progressdialog;
    Button detailBack;
    TextView title;
    String tokken="";
    TextView itemtitle,itemdetail,itemId;
    ImageView itemimg;
    int i=0;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1){
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }
        itemtitle=findViewById(R.id.player_detail_state);
        SharedPreferences settings = getSharedPreferences("zorobian_customer_pref_name", 0);
        tokken=settings.getString("berior_token","");
        itemdetail=findViewById(R.id.food_detail);
        itemimg=findViewById(R.id.player_detail_img);
        itemId=findViewById(R.id.id_order);
        title=findViewById(R.id.food_detail_title);
        itemtitle.setText(FoodListActivity.itemTitle);
        title.setText(FoodListActivity.itemTitle);
        itemdetail.setText(FoodListActivity.itemDetail);
        itemimg.setScaleType(ImageView.ScaleType.FIT_CENTER);
        itemimg.setImageDrawable(FoodListActivity.itemImage);
        itemId.setText(FoodListActivity.itemId);
        add=findViewById(R.id.btn_add);
        plus=findViewById(R.id.plus);
        minus=findViewById(R.id.minus);
        res=findViewById(R.id.res);
        detailBack=findViewById(R.id.back_arrow);
        detailBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        completePrice=findViewById(R.id.food_pric_);
        plus.setOnClickListener(this);
        minus.setOnClickListener(this);
        completePrice.setText("السعر "+FoodListActivity.itemPrice);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                if (MainActivity.tokken.length()>0){
                    showdialog();
                }
                add.setEnabled(false);
                if(MainActivity.tokken.length()>10){
                             sendFoodToCart();
                     }else {
                           sendToLocalCart();
                     }
            }
        });
    }
    private void sendFoodToCart(){
        Call<ResponseBody> call=userClient.sendRequistToCart(tokken,Double.parseDouble(itemId.getText().toString()),Double.parseDouble(FoodListActivity.itemPrice),Double.parseDouble(res.getText().toString()));
        call.enqueue(new Callback<ResponseBody>(){
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response){
                if (response.isSuccessful()){
                    progressdialog.cancel();
                    Toast.makeText(FoodDetailActivity.this, "تمت إضافة الطلب إلي سلة المشتريات", Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    add.setEnabled(true);
                    progressdialog.cancel();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressdialog.cancel();
                add.setEnabled(true);
                Toast.makeText(FoodDetailActivity.this,"الرجاءالتأكد من الاتصال بالإنترنت",Toast.LENGTH_LONG).show();
            }
        });
    }
    @Override
    public void onClick(View v) {
        int intres=0;
        double payResault=0;
        switch (v.getId()){
            case  R.id.plus:
                intres=Integer.parseInt(res.getText().toString())+1;
                res.setText(intres+"");
                payResault=Double.parseDouble(FoodListActivity.itemPrice)*intres;
                completePrice.setText("السعر "+payResault);
                break;
            case R.id.minus :
                if (Double.parseDouble(res.getText().toString())==1){
                }else{
                    intres=Integer.parseInt(res.getText().toString())-1;
                    res.setText(intres+"");
                    payResault=Double.parseDouble(FoodListActivity.itemPrice)*intres;
                    completePrice.setText("السعر "+payResault);
                }
                break;
        }
    }
    public void sendToLocalCart(){
        SharedPreferences settings = getSharedPreferences("zorobian_customer_FOODS_pref_name", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(itemId.getText().toString(),
                FoodListActivity.itemTitle+","
                +res.getText().toString().trim() +","
                +FoodListActivity.itemImgurl+","
                +FoodListActivity.itemPrice+","+
                itemId.getText().toString()+","+
                itemdetail.getText().toString()+".....");
                editor.apply();
                Toast.makeText(this, "تمت إضافة الطلب إلي سلة المشتريات", Toast.LENGTH_SHORT).show();
                finish();
    }
    public void showdialog(){
        progressdialog = new ProgressDialog(this);
        progressdialog.setMessage("الرجاء الإنتظار ....");
        progressdialog.setCancelable(false);
        progressdialog.show();
    }
}
