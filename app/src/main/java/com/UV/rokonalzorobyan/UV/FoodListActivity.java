package com.UV.rokonalzorobyan.UV;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import java.util.ArrayList;
import java.util.List;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.UV.rokonalzorobyan.UV.Api.UserClient;
import com.UV.rokonalzorobyan.UV.Api.apiRequist;
import com.UV.rokonalzorobyan.UV.Model.Datum;
import com.UV.rokonalzorobyan.UV.Model.ListOfFoods;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FoodListActivity extends Fragment {
    List<String> childList;
    ArrayList<String[]> orders;
    ArrayList<String> groupList;
    ExpandableListAdapter expListAdapter;
    static String itemTitle,itemDetail,itemPrice,itemId,itemImgurl;
    static Drawable itemImage;
    static ListOfFoods wholeData;
    public static Datum datum;
    ProgressBar progressBar;

    ExpandableListView expListView;
    UserClient userClient=apiRequist.loginRequist();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_food_list, container, false);
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        progressBar=getActivity().findViewById(R.id.progressbar1);
        groupList = new ArrayList<String>();
        orders=new ArrayList<>();
        expListView = getActivity().findViewById(R.id.laptop_list);
        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            int previousGroup = -1;
            @Override
            public void onGroupExpand(int groupPosition){
                if(groupPosition != previousGroup)
                    expListView.collapseGroup(previousGroup);
                previousGroup = groupPosition;
            }
        });
        expListAdapter = new ExpandableListAdapter(
        getActivity(),groupList);
        getOrders();
        expListAdapter.notifyDataSetChanged();
        expListView.setAdapter(expListAdapter);
        //setGroupIndicatorToRight();
        expListView.setOnChildClickListener(new OnChildClickListener() {

            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                final String selected = (String) expListAdapter.getChild(
                groupPosition, childPosition);
                TextView price=v.findViewById(R.id.item_cost);
                TextView title=v.findViewById(R.id.item_title);
                TextView idorder=v.findViewById(R.id.id_order);
                TextView desc=v.findViewById(R.id.item_desc);
                ImageView itemImag=v.findViewById(R.id.item_img);
                TextView imgUrl=v.findViewById(R.id.imgUrl);
                itemTitle=title.getText().toString();
                itemPrice=price.getText().toString();
                itemDetail=desc.getText().toString();
                itemId=idorder.getText().toString();
                itemImage=itemImag.getDrawable();
                itemImgurl=imgUrl.getText().toString();
                startActivity(new Intent(getActivity(),FoodDetailActivity.class));
                return true;
            }
        });
    }
    private void getOrders() {
        Call<ListOfFoods> call=userClient.getOrders(MainActivity.tokken);
        call.enqueue(new Callback<ListOfFoods>(){
            @Override
            public void onResponse(Call<ListOfFoods> call, Response<ListOfFoods> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()){
                     wholeData=response.body();
                    for (int i=0;i<response.body().getData().size();i++){
                         datum= response.body().getData().get(i);
                         groupList.add(datum.getCategoryName());
                         expListAdapter.notifyDataSetChanged();
                      }
                }else {
                }
            }
            @Override
            public void onFailure(Call<ListOfFoods> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}