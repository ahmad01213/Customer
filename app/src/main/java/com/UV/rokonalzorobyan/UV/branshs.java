package com.UV.rokonalzorobyan.UV;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.UV.rokonalzorobyan.UV.Api.UserClient;
import com.UV.rokonalzorobyan.UV.Api.apiRequist;
import com.UV.rokonalzorobyan.UV.Model.branchModel2;
import com.UV.rokonalzorobyan.UV.Model.branshsModle1;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class branshs extends AppCompatActivity {
    static ArrayList<branchModel2> orders;
    RecyclerView recyclerView;
    Dialog dialog;
    Button btnSend;
    ProgressDialog progressdialog;
    TextView loc1,loc2,loc3;
    ProgressBar progressBar;
    static Activity fa;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_branshs);
        BranshsAdapter.branId="";
        progressBar=findViewById(R.id.progressbar1);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1){
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        }
        fa=this;
        recyclerView=findViewById(R.id.cart_rec);
        TextView btnCompletreq=findViewById(R.id.complet);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        getOrders();
        btnCompletreq.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (BranshsAdapter.branId.trim().length()>0){
                    showdialog();
                    sendCartOrders();
                    startActivity(new Intent(branshs.this,Receit.class));
                }else{
                    Toast.makeText(branshs.this, "الرجاء اختيار الفرع", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void getOrders(){
        orders=new ArrayList<>();
        UserClient userClient = apiRequist.sendRequist();
        Call<branshsModle1> call = userClient.getBranshs(MainActivity.tokken,MainActivity.lat,MainActivity.lng);
        call.enqueue(new Callback<branshsModle1>(){
            @Override
            public void onResponse(Call<branshsModle1> call, Response<branshsModle1> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()){
                    orders=(ArrayList<branchModel2>) response.body().getData();
                    BranshsAdapter adapter=new BranshsAdapter(orders,branshs.this,getSupportFragmentManager());
                    recyclerView.setAdapter(adapter);
                }
            }
            @Override
            public void onFailure(Call<branshsModle1> call, Throwable t){
                progressBar.setVisibility(View.GONE);
            }
        });
    }
    public void sendCartOrders(){
        orders=new ArrayList<>();
        UserClient userClient = apiRequist.sendRequist();
        Call<ResponseBody> call = userClient.sendCartReq(MainActivity.tokken,BranshsAdapter.branId,"1");
        call.enqueue(new Callback<ResponseBody>(){
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response){
                if (response.isSuccessful()){
                    progressdialog.cancel();
                }else{
                    progressdialog.cancel();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t){
                progressdialog.cancel();
            }
        });
    }
    void goToBransh(String loc){
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:"+loc+"?q=("+loc+")@"+loc));
        startActivity(intent);
    }
    public void showdialog(){
        progressdialog = new ProgressDialog(this);
        progressdialog.setMessage("الرجاء الإنتظار ....");
        progressdialog.setCancelable(false);
        progressdialog.show();
    }
//    public void addFragment(Fragment fragment,String tag,double lat,double lng){
//        MapFragment.lat=lat;
//        MapFragment.lng=lng;
//        FragmentManager manager = getSupportFragmentManager();
//        FragmentTransaction ft = manager.beginTransaction();
//        ft.replace(R.id.map_view, fragment, tag);
//        ft.commitAllowingStateLoss();
//    }

    @Override
    protected void onStart() {
        super.onStart();
        View decorView = getWindow().getDecorView();
// Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
// Remember that you should never show the action bar if the
// status bar is hidden, so hide that too if necessary.
        ActionBar actionBar = getActionBar();
        try {
            actionBar.hide();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        BranshsAdapter.branId="";
    }
}
