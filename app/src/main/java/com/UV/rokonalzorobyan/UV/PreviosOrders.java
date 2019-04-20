package com.UV.rokonalzorobyan.UV;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.UV.rokonalzorobyan.UV.Api.UserClient;
import com.UV.rokonalzorobyan.UV.Api.apiRequist;
import com.UV.rokonalzorobyan.UV.Model.PreviousModel.Example;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PreviosOrders extends Fragment {
    ArrayList<Example> orders;
    RecyclerView recyclerView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_previos_orders, container, false);
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recyclerView=getActivity().findViewById(R.id.prev_orders_rec);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        if (MainActivity.tokken.length()>10){
            getCartOrders();
        }
    }
    public void getCartOrders(){
        orders=new ArrayList<>();
        UserClient userClient = apiRequist.sendRequist();
        Call<List<Example>> call = userClient.getPrev(MainActivity.tokken);
        call.enqueue(new Callback<List<Example>>(){
            @Override
            public void onResponse(Call<List<Example>> call, Response<List<Example>> response){
                if (response.isSuccessful()){
                    orders=(ArrayList<Example>)response.body();
                    Collections.reverse(orders);
                    previusMainAdapter adapter=new previusMainAdapter(orders, getActivity());
                     recyclerView.setAdapter(adapter);
                }else{
                }
            }
            @Override
            public void onFailure(Call<List<Example>> call, Throwable t){
            }
        });
    }
}
