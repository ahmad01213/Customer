package com.UV.rokonalzorobyan.UV;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.UV.rokonalzorobyan.UV.Api.UserClient;
import com.UV.rokonalzorobyan.UV.Api.apiRequist;
import com.UV.rokonalzorobyan.UV.Model.CartData;
import com.UV.rokonalzorobyan.UV.Model.ReceiptData;
import com.UV.rokonalzorobyan.UV.Model.cartItem;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Receit extends AppCompatActivity {
     ArrayList<cartItem> orders;
    TextView itemsCost,delCost,tax,completCost,delCostText;
    static Button btnSendAllReq;
    ProgressDialog progressdialog;
    RecyclerView recyclerView;
    int status=7;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reciet);
        delCost = findViewById(R.id.int_delcost);
        tax = findViewById(R.id.int_tax);
        completCost =findViewById(R.id.int_complet_cost);
        delCostText=findViewById(R.id.del_cost);
        itemsCost=findViewById(R.id.int_complet_cost_items);
        Button btnBack=findViewById(R.id.receit_back);
        this.setFinishOnTouchOutside(false);
       recyclerView=findViewById(R.id.list_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        getOrders();
        SharedPreferences settings =getSharedPreferences("zorobian_customer_pref_name", 0);
        status=settings.getInt("status",5);
        if (status==0){
            delCostText.setVisibility(View.GONE);
            delCost.setVisibility(View.GONE);
        }
        btnSendAllReq=findViewById(R.id.btn_complete_req);
        getCartOrders();
        btnSendAllReq.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                showdialog();
                sendRequistsWithLocation();
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    public void getOrders(){
        UserClient userClient = apiRequist.sendRequist();
        Call<ReceiptData> call = userClient.getReciptData(MainActivity.tokken);
        call.enqueue(new Callback<ReceiptData>(){
            @Override
            public void onResponse(Call<ReceiptData> call, Response<ReceiptData> response) {
                if (response.isSuccessful()){
                    double mcompletCost=0;
                        delCost.setText(response.body().getDeliveryCost()+"");
                        itemsCost.setText(response.body().getPrice()+"");
                    double delcost = 0;
                    try {
                        delcost = Double.parseDouble(delCost.getText().toString());
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                    double wholcost = Double.parseDouble(itemsCost.getText().toString());
                    double mtax = 0.05 * (wholcost);
                    if (status==0){
                         mcompletCost = mtax + wholcost;
                    }else {
                         mcompletCost = mtax + delcost + wholcost;
                    }
                    completCost.setText(mcompletCost + " ");
                    tax.setText(mtax + "");
                    try {
                        delCost.setText(formatFigureTwoPlaces(Float.parseFloat(response.body().getDeliveryCost()+"")));
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                    try {
                        tax.setText(formatFigureTwoPlaces(Float.parseFloat(mtax + " ")));
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                    try {
                        completCost.setText(formatFigureTwoPlaces(Float.parseFloat(mcompletCost + " ")));
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }else {
                }
            }
            @Override
            public void onFailure(Call<ReceiptData> call, Throwable t) {
            }
        });
    }
    public void getCartOrders(){
        orders=new ArrayList<>();
        UserClient userClient = apiRequist.sendRequist();
        Call<CartData> call = userClient.getCartItems(MainActivity.tokken);
        call.enqueue(new Callback<CartData>(){
            @Override
            public void onResponse(Call<CartData> call, Response<CartData> response) {
                if (response.isSuccessful()){
                    try {
                        orders=(ArrayList<cartItem>)response.body().getItems().get(0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    ReceitAdapter adapter=new ReceitAdapter(orders,Receit.this);
                    recyclerView.setAdapter(adapter);
                }else{
                }
            }
            @Override
            public void onFailure(Call<CartData> call, Throwable t) {
            }
        });
    }
    public void sendRequistsWithLocation(){
        UserClient userClient = apiRequist.sendRequist();
        Call<ResponseBody> call = userClient.sendReciet(MainActivity.tokken);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    progressdialog.cancel();
                    Receit.btnSendAllReq.setVisibility(View.INVISIBLE);
                    Toast.makeText(Receit.this, "... تم إرسال طلبك بنجاح شكرا لك ... ", Toast.LENGTH_SHORT).show();
                    try {
                        branshs.fa.finish();
                        LocationActivity.fa.finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    MainActivity.statuus=5;
                    finish();
                }else {
                    progressdialog.cancel();
                    Toast.makeText(Receit.this, "الرجاءالإنتظار لحين الحصول علي موقعك", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressdialog.cancel();
            }
        });
    }
    public String formatFigureTwoPlaces(float value){
        NumberFormat nf = NumberFormat.getNumberInstance(Locale.US);
        DecimalFormat myFormatter = (DecimalFormat)nf;
        myFormatter.applyPattern("##0.00");
        return myFormatter.format(value);
    }
    public void showdialog(){
        progressdialog = new ProgressDialog(this);
        progressdialog.setMessage("الرجاء الإنتظار ....");
        progressdialog.setCancelable(false);
        progressdialog.show();
    }
}
