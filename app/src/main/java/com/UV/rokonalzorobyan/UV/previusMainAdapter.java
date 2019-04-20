package com.UV.rokonalzorobyan.UV;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.UV.rokonalzorobyan.UV.Api.UserClient;
import com.UV.rokonalzorobyan.UV.Api.apiRequist;
import com.UV.rokonalzorobyan.UV.Model.PreviousModel.Example;
import com.UV.rokonalzorobyan.UV.Model.PreviousModel.Item;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class previusMainAdapter extends RecyclerView.Adapter<previusMainAdapter.MyViewHolder> {
    ArrayList<Example> orders;
    int d=0;
    Dialog dialog;
    UserClient userClient=apiRequist.sendRequist();
    Context context;
    public previusMainAdapter(ArrayList<Example> mOrders, Context ctx) {
        this.orders=mOrders;
        context=ctx;
    }
    @NonNull
    @Override
    public previusMainAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View layoutView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.previous_raw, null);
        return new previusMainAdapter.MyViewHolder(layoutView);
    }
    @Override
    public void onBindViewHolder(@NonNull final previusMainAdapter.MyViewHolder myViewHolder, final int i){
        myViewHolder.itemsCost.setText(orders.get(i).getWholePrice()+"");
        myViewHolder.delCost.setText(orders.get(i).getDeliveryCost()+"");
        myViewHolder.itemDate.setText("التاريخ  :  "+orders.get(i).getDate().substring(0,10));
        myViewHolder.itemTime.setText("التوقيت  :  "+orders.get(i).getDate().substring(10,orders.get(i).getDate().length()));
        myViewHolder.itemId.setText("رقم الطلب    :  "+orders.get(i).getKey()+"");
        myViewHolder.show.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                if (d==0){
                    myViewHolder.relativeLayout.setVisibility(View.VISIBLE);
                    myViewHolder.show.setBackground(context.getDrawable(R.drawable.arrow));
                    d=1;
                }else{
                    myViewHolder.relativeLayout.setVisibility(View.GONE);
                    myViewHolder.show.setBackground(context.getDrawable(R.drawable.back));
                    d=0;
                }
            }
        });
        double mcompletCost=0;
        double delcost = 0;
              delcost = Double.parseDouble(myViewHolder.delCost.getText().toString());
              double wholcost = Double.parseDouble(myViewHolder.itemsCost.getText().toString());
              double mtax = 0.05 * (wholcost);
               mcompletCost = mtax + delcost + wholcost;
                myViewHolder.completCost.setText(mcompletCost + " ");
                myViewHolder.tax.setText(mtax + " ");
        try {
            myViewHolder. delCost.setText(formatFigureTwoPlaces(Float.parseFloat(orders.get(i).getDeliveryCost()+"")));
            myViewHolder.tax.setText(formatFigureTwoPlaces(Float.parseFloat(mtax + " ")));
            myViewHolder.completCost.setText(formatFigureTwoPlaces(Float.parseFloat(mcompletCost + " ")));
        }catch (NumberFormatException e){
            e.printStackTrace();
        }
               getItems((ArrayList<Item>)orders.get(i).getItems().get(0),myViewHolder.recyclerView);
               myViewHolder.btnRateorder.setOnClickListener(new View.OnClickListener(){
                   @Override
                   public void onClick(View v) {
                       dialog(R.layout.rating_lay,"",context);
                       Button accept,cancel;
                       final RatingBar orderBar,delBar;
                       TextView delBarStr;
                       accept=dialog.findViewById(R.id.btn_rat);
                       cancel=dialog.findViewById(R.id.cancle);
                       orderBar =dialog.findViewById(R.id.ratingBar_order);
                       delBarStr=dialog.findViewById(R.id.rate_del_str);
                       delBar=dialog.findViewById(R.id.ratingBar_del);
                       if (orders.get(i).getType().equals("طلب توصيل")){
                           delBar.setVisibility(View.VISIBLE);
                       }else{
                           delBar.setVisibility(View.GONE);
                           delBarStr.setVisibility(View.GONE);
                       }
                       accept.setOnClickListener(new View.OnClickListener(){
                           @Override
                           public void onClick(View v){
                               if (orders.get(i).getType().equals("طلب توصيل")){
                                   if (Integer.parseInt((orderBar.getRating()+"").substring(0,1))>0&&Integer.parseInt((delBar.getRating()+"").substring(0,1))>0){
                                       sendRateValues(Integer.parseInt((orderBar.getRating()+"").substring(0,1)),
                                               orders.get(i).getKey(),
                                               Integer.parseInt((delBar.getRating()+"").substring(0,1)));
                                   }else {
                                       Toast.makeText(context, "الرجاء تحديد قيمة التقييم", Toast.LENGTH_SHORT).show();
                                   }
                               }else {
                                   if (Integer.parseInt((orderBar.getRating()+"").substring(0,1))>0){
                                       sendRateValues(Integer.parseInt((orderBar.getRating()+"").substring(0,1)),
                                               orders.get(i).getKey(),
                                               6);                                   }else {
                                       Toast.makeText(context, "الرجاء تحديد قيمة التقييم", Toast.LENGTH_SHORT).show();
                                   }
                               }
                               myViewHolder.btnRateorder.setVisibility(View.GONE);
                           }
                       });
                       cancel.setOnClickListener(new View.OnClickListener(){
                           @Override
                           public void onClick(View v){
                               dialog.dismiss();
                           }
                       });
                   }
               });
        if (orders.get(i).getRateId()!=0){
            myViewHolder.btnRateorder.setVisibility(View.GONE);
        }
    }
    @Override
    public int getItemCount(){
        return orders.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView itemsCost, delCost, tax, completCost, delCostText,itemDate,itemId,itemTime;
        Button btnRateorder,show;
        RecyclerView recyclerView;
        RelativeLayout relativeLayout;
        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            delCost = itemView.findViewById(R.id.int_delcost);
            tax = itemView.findViewById(R.id.int_tax);
            completCost = itemView.findViewById(R.id.int_complet_cost);
            delCostText = itemView.findViewById(R.id.del_cost);
            itemsCost = itemView.findViewById(R.id.int_complet_cost_items);
            itemDate = itemView.findViewById(R.id.date_order);
            itemTime = itemView.findViewById(R.id.time_order);
            itemId = itemView.findViewById(R.id.order_id);
            btnRateorder=itemView.findViewById(R.id.rat_order);
            show=itemView.findViewById(R.id.btn_show);
            relativeLayout=itemView.findViewById(R.id.whole_table_lay);
            recyclerView = itemView.findViewById(R.id.list_view);
            recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        }
    }
    public void getItems(ArrayList<Item> items, RecyclerView recyclerView){
        PreviousOrdersAdapter adapter=new PreviousOrdersAdapter(items,context);
        recyclerView.setAdapter(adapter);
    }
    private void sendRateValues(int orderValue,int id,int delValue){
            UserClient userClient = apiRequist.sendRequist();
            Call<ResponseBody> call = userClient.sendRating(MainActivity.tokken,orderValue,id,delValue);
            call.enqueue(new Callback<ResponseBody>(){
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()){
                        dialog.dismiss();
                    }else{
                    }
                }
                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t){
                }
            });
    }
    private void sendRateValuesorder(int value,int id){
        UserClient userClient = apiRequist.sendRequist();
        Call<ResponseBody> call = userClient.sendRatingOrder(MainActivity.tokken,value,id);
        call.enqueue(new Callback<ResponseBody>(){
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    dialog.dismiss();
                }else{
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t){
            }
        });
    }
    void dialog(int id,String string,Context context){
        dialog = new Dialog(context);
        dialog.setContentView(id);
        dialog.setTitle(string);
        dialog.show();
    }
    public String formatFigureTwoPlaces(float value){
        NumberFormat nf = NumberFormat.getNumberInstance(Locale.US);
        DecimalFormat myFormatter = (DecimalFormat)nf;
        myFormatter.applyPattern("##0.00");
        return myFormatter.format(value);
    }
}