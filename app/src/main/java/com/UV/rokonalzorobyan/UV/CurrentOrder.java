package com.UV.rokonalzorobyan.UV;
import android.os.Build;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.UV.rokonalzorobyan.UV.Api.UserClient;
import com.UV.rokonalzorobyan.UV.Api.apiRequist;
import com.UV.rokonalzorobyan.UV.Model.PreviousModel.Example;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class CurrentOrder extends Fragment {
    String i="0";
    int d=0;
    int count=0;
    private Timer timer;
    TimerTask timerTask;
    ArrayList<Example> orders;
    RecyclerView recyclerView;
    private Parcelable recyclerViewState;
    currentOrderAdapter adapter;
    ProgressBar progressBar;
    private Handler handler = new Handler();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_current_order, container, false);
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        progressBar=getActivity().findViewById(R.id.progressbar1);
        count=0;
        recyclerView=getActivity().findViewById(R.id.prev_orders_rec);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        if (MainActivity.tokken.length()>10){
              startTimer();
        }
        }
    public void startTimer() {
        timer = new Timer();
        initializeTimerTask();
        timer.schedule(timerTask, 2, 5000);
    }
    public void stoptimertask(){
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }
    @Override
    public void onStop() {
        super.onStop();
    }
    public void initializeTimerTask(){
        timerTask = new TimerTask(){
            public void run(){
                handler.post(new Runnable() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    public void run(){
                        count++;
                        getCurrentOrder();
                    }
                });
            }
        };
    }
    public void getCurrentOrder(){
        orders=new ArrayList<>();
        UserClient userClient = apiRequist.sendRequist();
        Call<List<Example>> call = userClient.getCurrentOrder(MainActivity.tokken);
        call.enqueue(new Callback<List<Example>>(){
            @Override
            public void onResponse(Call<List<Example>> call, Response<List<Example>> response){
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()){
                    orders=(ArrayList<Example>)response.body();
                    Collections.reverse(orders);
                   adapter = new currentOrderAdapter(orders, getActivity(),getActivity().getSupportFragmentManager());
                        recyclerViewState = recyclerView.getLayoutManager().onSaveInstanceState();
                        recyclerView.setAdapter(adapter);
                        recyclerView.getLayoutManager().onRestoreInstanceState(recyclerViewState);
                }else{
                }
            }
            @Override
            public void onFailure(Call<List<Example>> call, Throwable t){
                progressBar.setVisibility(View.GONE);
            }
        });
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        stoptimertask();
    }
}
