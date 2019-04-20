package com.UV.rokonalzorobyan.UV;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.UV.rokonalzorobyan.UV.Api.UserClient;
import com.UV.rokonalzorobyan.UV.Api.apiRequist;
import com.UV.rokonalzorobyan.UV.Model.CartData;
import com.UV.rokonalzorobyan.UV.Model.cartItem;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class Chart extends Fragment{
    RecyclerView recyclerView;
   static ArrayList<cartItem> orders;
    static ArrayList<String[]> localOrders;
    static int start=0;
    Timer timer;
    int i=1000;
    int d=1000;
    TimerTask timerTask;
    TextView emptyCart;
    ProgressBar progressBar;
    final Handler handler = new Handler();
    Button btnSendCartReq,menuBack;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_chart, container, false);
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        progressBar=getActivity().findViewById(R.id.progressbar1);
        menuBack=getActivity().findViewById(R.id.beck_to_menu);
        emptyCart=getActivity().findViewById(R.id.empty_cart);
        menuBack.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container_lay,
                        new FoodListActivity()).addToBackStack("tag").commit();
            }
        });
        start=1;
        recyclerView=getActivity().findViewById(R.id.cart_rec);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        localOrders=new ArrayList<>();
        orders=new ArrayList<>();
        btnSendCartReq=getActivity().findViewById(R.id.btn_complete_req);
        btnSendCartReq.setOnClickListener(
            new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (getCurrentTime()>39600&&getCurrentTime()<82800){
                    if (MainActivity.tokken.length()>10){
                        if (MainActivity.tokken.length()>10){
                            if (MainActivity.statuus==0){
                                startActivity(new Intent(getActivity(),branshs.class));
                            }else if (MainActivity.statuus==1){
                                startActivity(new Intent(getActivity(),LocationActivity.class));
                            }else {
                                dialog(R.layout.dialog,"",getActivity());
                            }
                        }else {
                            startActivity(new Intent(getActivity(),LoginActivity.class));
                        }
                    }
                    else{
                        startActivity(new Intent(getActivity(),LoginActivity.class));
                    }
                }else {
                    showAlartDialog();
                }

            }
        });
    }
    public void getOrders(){
        orders=new ArrayList<>();
        UserClient userClient = apiRequist.sendRequist();
        Call<CartData> call = userClient.getCartItems(MainActivity.tokken);
        call.enqueue(new Callback<CartData>() {
            @Override
            public void onResponse(Call<CartData> call, Response<CartData> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()){
                    try {
                        orders=(ArrayList<cartItem>)response.body().getItems().get(0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (i!=orders.size()){
                        MyAdapter.dataSize=orders.size();
                        MyAdapter adapter=new MyAdapter(orders,null,getActivity());
                        recyclerView.setAdapter(adapter);
                        i= orders.size();
                        if (i==0){
                            btnSendCartReq.setVisibility(View.INVISIBLE);
                            emptyCart.setVisibility(View.VISIBLE);
                        }else {
                            btnSendCartReq.setVisibility(View.VISIBLE);
                            emptyCart.setVisibility(View.INVISIBLE);
                        }
                    }
                }else {
                }
            }
            @Override
            public void onFailure(Call<CartData> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
            }
        });
        }
        public void getLocalOrders(){
        progressBar.setVisibility(View.GONE);
            localOrders = new ArrayList<>();
            SharedPreferences settings = getActivity().getSharedPreferences("zorobian_customer_FOODS_pref_name", 0);
            Map<String, ?> keys = settings.getAll();
            for (Map.Entry<String, ?> entry : keys.entrySet()){
                localOrders.add(entry.getValue().toString().split(","));
            }
            if (d!=localOrders.size()){
                MyAdapter.dataSize=localOrders.size();
                MyAdapter adapter=new MyAdapter(null,localOrders,getActivity());
                recyclerView.setAdapter(adapter);
                d= localOrders.size();
            }
            if (d==0){
                btnSendCartReq.setVisibility(View.INVISIBLE);
                emptyCart.setVisibility(View.VISIBLE);
            }else {
                btnSendCartReq.setVisibility(View.VISIBLE);
                emptyCart.setVisibility(View.INVISIBLE);
            }
            }
    public void stoptimertask() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }
    public void startTimer(){
        timer = new Timer();
        initializeTimerTask();
        timer.schedule(timerTask, 0, 6000);
    }
    public void initializeTimerTask() {
        timerTask = new TimerTask() {
            public void run(){
                handler.post(new Runnable() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    public void run(){
                        if (MainActivity.tokken.length()>10){
                            getOrders();
                        }else{
                            getLocalOrders();
                        }
                    }
                });
            }
        };
    }
    @Override
    public void onStop() {
        super.onStop();
        stoptimertask();
    }
    @Override
    public void onStart() {
        super.onStart();
        startTimer();
    }
    @Override
    public void onResume() {
        super.onResume();
        if (MainActivity.tokken.length()>10){
            i=1000;
            d=1000;
            getOrders();
        }else{
            i=1000;
            d=1000;
            getLocalOrders();
        }
    }
     void dialog(int id,String string,Context context){
             Button bransh,del;
             final Dialog dialog;
             dialog = new Dialog(context);
             dialog.setContentView(id);
             bransh=dialog.findViewById(R.id.btn_food_list);
             del=dialog.findViewById(R.id.delivery_req);
             dialog.setTitle(string);
             dialog.show();
        bransh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               startActivity(new Intent(getActivity(),branshs.class));
               dialog.dismiss();
            }
        });
        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),LocationActivity.class));
                dialog.dismiss();
            }
        });
    }
    int getCurrentTime(){
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+3:00"));
        Date currentLocalTime = cal.getTime();
        DateFormat date = new SimpleDateFormat("HH:mm a",Locale.ENGLISH);
         // you can get seconds by adding  "...:ss" to it
        date.setTimeZone(TimeZone.getTimeZone("GMT+3:00"));
        String localTime = date.format(currentLocalTime);
        int sum=((Integer.parseInt(localTime.substring(0,2))*60*60)+(Integer.parseInt(localTime.substring(3,5))*60));
        return sum;
    }

    void showAlartDialog(){
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.setTitle("");
        alertDialog.setMessage("نأسف : لا يمكننا استقبال طلبك الآن \n - مواعيد العمل : من الحادية عشر صباحا وحتي الحادية عشر مساء "
        );
        // Alert dialog button
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int which){
                        // Alert dialog action goes here
                        // onClick button code here
                        dialog.dismiss();
                        // use dismiss to cancel alert dialog
                    }
                });
        alertDialog.show();
    }
}