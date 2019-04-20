package com.UV.rokonalzorobyan.UV;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
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
public class currentOrderAdapter extends RecyclerView.Adapter<currentOrderAdapter.MyViewHolder> {
    ArrayList<Example> orders;
    ProgressDialog progressdialog;
    private Parcelable recyclerViewState;
    UserClient userClient=apiRequist.sendRequist();
    Context context;
    FragmentManager fragmentManager;
    static int d=0;
    int o=0;
    public currentOrderAdapter(ArrayList<Example> mOrders, Context ctx, FragmentManager fragmentManager){
        this.orders=mOrders;
        context=ctx;
        this.fragmentManager=fragmentManager;
        d++;
    }
    @NonNull
    @Override
    public currentOrderAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View layoutView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.current_order_raw, null);
        return new currentOrderAdapter.MyViewHolder(layoutView);
    }
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(@NonNull final currentOrderAdapter.MyViewHolder myViewHolder, final int i){
        if (MainActivity.currentorederShwowState==0){
            myViewHolder.relativeLayout1.setVisibility(View.GONE);
            myViewHolder.show.setBackground(context.getDrawable(R.drawable.back));
        }else {
            myViewHolder.relativeLayout1.setVisibility(View.VISIBLE);
            myViewHolder.show.setBackground(context.getDrawable(R.drawable.arrow));
        }
        myViewHolder.show.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(MainActivity.currentorederShwowState==0){
                    myViewHolder.relativeLayout1.setVisibility(View.VISIBLE);
                    myViewHolder.show.setBackground(context.getDrawable(R.drawable.arrow));
                    MainActivity.currentorederShwowState=1;
                }else {
                    myViewHolder.relativeLayout1.setVisibility(View.GONE);
                    myViewHolder.show.setBackground(context.getDrawable(R.drawable.back));
                    MainActivity.currentorederShwowState=0;
                }
            }
        });
        o++;
        myViewHolder.itemsCost.setText(orders.get(i).getWholePrice()+"");
        myViewHolder.delCost.setText(orders.get(i).getDeliveryCost()+"");
        myViewHolder.itemId.setText("رقم الطلب    :  "+orders.get(i).getKey()+"");
        myViewHolder.itemDate.setText("التاريخ  :  "+orders.get(i).getDate().substring(0,10));
        myViewHolder.itemTime.setText("التوقيت  :  "+orders.get(i).getDate().substring(10,orders.get(i).getDate().length()));
        myViewHolder.orederType.setText(orders.get(i).getType()+"");
        double mcompletCost=0;
        double delcost = 0;
        myViewHolder.btnDeleteReq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showdialog();
                deleteOrder(orders.get(i).getKey());
            }
        });
        try {
            delcost = Double.parseDouble(myViewHolder.delCost.getText().toString());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        double wholcost = Double.parseDouble(myViewHolder.itemsCost.getText().toString());
        if (wholcost>0){
            double mtax = 0.05 * (wholcost);
            mcompletCost = mtax + delcost + wholcost;
            myViewHolder.completCost.setText(mcompletCost + " ");
            myViewHolder.tax.setText(mtax + " ");
            if (o<=orders.size()){
                getItems((ArrayList<Item>)orders.get(i).getItems().get(0),myViewHolder.recyclerView);
            }
            myViewHolder. delCost.setText(formatFigureTwoPlaces(Float.parseFloat(orders.get(i).getDeliveryCost()+"")));
            myViewHolder.tax.setText(formatFigureTwoPlaces(Float.parseFloat(mtax + " ")));
            myViewHolder.completCost.setText(formatFigureTwoPlaces(Float.parseFloat(mcompletCost + " "))+"  "+"ريال");
        }
        if (orders.get(i).getType().equals("طلب توصيل")){
            myViewHolder.typText.setText("جاري التوصيل");
            myViewHolder.delSearchLay.setVisibility(View.VISIBLE);
        }else{
            myViewHolder.typText.setText("جاهز  للاستلام");
        }
        switch (orders.get(i).getStatus()){
            case 1:
                       myViewHolder.neword.setBackground(context.getResources().getDrawable(R.drawable.right));
                       myViewHolder.delSearchLay.setVisibility(View.GONE);
                       myViewHolder.delNam.setText("اسم المندوب : "+orders.get(i).getDeliveryName());
                       myViewHolder.delNum.setText("رقم المندوب : "+orders.get(i).getDeliveryPhone());
                break;
            case 7: myViewHolder.neword.setBackground(context.getResources().getDrawable(R.drawable.right));
                myViewHolder.delSearchLay.setVisibility(View.GONE);
                break;
            case 2:
                myViewHolder.neword.setBackground(context.getResources().getDrawable(R.drawable.right));
                myViewHolder. prebOrd.setBackground(context.getResources().getDrawable(R.drawable.right));
                myViewHolder.delSearchLay.setVisibility(View.GONE);
                break;
            case 3:
                myViewHolder.neword.setBackground(context.getResources().getDrawable(R.drawable.right));
                myViewHolder. prebOrd.setBackground(context.getResources().getDrawable(R.drawable.right));
                myViewHolder.delSearchLay.setVisibility(View.GONE);
                break;
            case 8:
                myViewHolder. neword.setBackground(context.getResources().getDrawable(R.drawable.right));
                myViewHolder.prebOrd.setBackground(context.getResources().getDrawable(R.drawable.right));
                myViewHolder.waitrec.setBackground(context.getResources().getDrawable(R.drawable.right));
                myViewHolder.allDon.setVisibility(View.VISIBLE);
                myViewHolder.delSearchLay.setVisibility(View.GONE);
                break;
            case 5 :
                myViewHolder. neword.setBackground(context.getResources().getDrawable(R.drawable.right));
                myViewHolder.prebOrd.setBackground(context.getResources().getDrawable(R.drawable.right));
                myViewHolder.waitrec.setBackground(context.getResources().getDrawable(R.drawable.right));
                myViewHolder.allDon.setVisibility(View.VISIBLE);
                myViewHolder.delSearchLay.setVisibility(View.GONE);
                break;
            case 9 :
                myViewHolder.relativeLayout.setVisibility(View.GONE);
                myViewHolder.linearLayout.setVisibility(View.VISIBLE);
                myViewHolder.delSearchLay.setVisibility(View.GONE);
                break;
//           case 6 :
//               ((FragmentActivity) context).getSupportFragmentManager().beginTransaction()
//                       .replace(R.id.container_lay, new HomePage())
//                       .commit();
//               fragmentManager.beginTransaction().replace(R.id.container_lay,  new HomePage()).addToBackStack(null).commit();
//               break;
        }
    }
    @Override
    public int getItemCount(){
        return orders.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout linearLayout;
        TextView itemsCost, delCost, tax, completCost, delCostText,itemDate,itemId,itemTime,typText,orederType,delNam,delNum;
        ImageView neword,prebOrd,waitrec,recived,allDon;
        RelativeLayout relativeLayout,relativeLayout1;
        LinearLayout delSearchLay;
        Button show;
        Button btnDeleteReq;
        RecyclerView recyclerView;
        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            btnDeleteReq=itemView.findViewById(R.id.btn_delet);
            delCost = itemView.findViewById(R.id.int_delcost);
            tax = itemView.findViewById(R.id.int_tax);
            completCost = itemView.findViewById(R.id.int_complet_cost);
            delCostText = itemView.findViewById(R.id.del_cost);
            itemsCost = itemView.findViewById(R.id.int_complet_cost_items);
            show=itemView.findViewById(R.id.btn_show);
            itemDate = itemView.findViewById(R.id.date_order);
            itemTime = itemView.findViewById(R.id.time_order);
            itemId = itemView.findViewById(R.id.order_id);
            delNam = itemView.findViewById(R.id.del_num);
            delNum = itemView.findViewById(R.id.del_nam);
            orederType = itemView.findViewById(R.id.order_type);
            recyclerView = itemView.findViewById(R.id.list_view);
            recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
            neword=itemView.findViewById(R.id.new_);
            prebOrd=itemView.findViewById(R.id.prep);
            waitrec=itemView.findViewById(R.id.del);
            recived=itemView.findViewById(R.id.ready);
            allDon=itemView.findViewById(R.id.complete_ready);
            typText=itemView.findViewById(R.id.typ_tex);
            linearLayout=itemView.findViewById(R.id.suspend_lay);
            relativeLayout=itemView.findViewById(R.id.desh_lay);
            relativeLayout1=itemView.findViewById(R.id.whole_table_lay);
            delSearchLay=itemView.findViewById(R.id.del_search);
        }
    }
    public void getItems(ArrayList<Item> items, RecyclerView recyclerView){
        PreviousOrdersAdapter adapter=new PreviousOrdersAdapter(items,context);
        recyclerViewState = recyclerView.getLayoutManager().onSaveInstanceState();
        recyclerView.setAdapter(adapter);
        recyclerView.getLayoutManager().onRestoreInstanceState(recyclerViewState);
    }
    public String formatFigureTwoPlaces(float value){
        NumberFormat nf = NumberFormat.getNumberInstance(Locale.US);
        DecimalFormat myFormatter = (DecimalFormat)nf;
        myFormatter.applyPattern("##0.00");
        return myFormatter.format(value);
    }
    public void deleteOrder(int orderId){
        UserClient userClient = apiRequist.sendRequist();
        Call<ResponseBody> call = userClient.deletCurrentOrder(MainActivity.tokken,orderId);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    Toast.makeText(context, "تم حذف الطلب بنجاح ... شكرا لك ...", Toast.LENGTH_SHORT).show();
                    progressdialog.cancel();
                }else{
                    progressdialog.cancel();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t){
                Toast.makeText(context, "الرجاء التأكد من الإتصال بالإنترنت", Toast.LENGTH_SHORT).show();
                progressdialog.cancel();
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