package com.UV.rokonalzorobyan.UV;

import android.content.Context;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.UV.rokonalzorobyan.UV.Model.branchModel2;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
public class BranshsAdapter extends RecyclerView.Adapter<BranshsAdapter.MyViewHolder>{
    ArrayList<branchModel2> orders;
    FragmentManager fragmentManager;
    static String branId="";
    Context context;
    int selectedPosition=-1;
    public BranshsAdapter(ArrayList<branchModel2> mOrders, Context ctx, FragmentManager fragmentManager){
        this.orders=mOrders;
        context=ctx;
        this.fragmentManager=fragmentManager;
        addFragment(new MapFragment(),"map",Double.parseDouble(orders.get(0).getLat()),Double.parseDouble(orders.get(0).getLong()));
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i){
        View layoutView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.branshs_raw, null);
        return new BranshsAdapter.MyViewHolder(layoutView);
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int i) {
        myViewHolder.branName.setText(orders.get(i).getBranchName());
        myViewHolder.branshDistance.setText(formatFigureTwoPlaces(Float.parseFloat(orders.get(i).getDistance().toString()))+"  كم");
        myViewHolder.branshAdress.setText(getCompleteAddressString(Double.parseDouble(orders.get(i).getLat()),Double.parseDouble(orders.get(i).getLong())));
        if(selectedPosition==i) {
            myViewHolder.itemView.setBackgroundColor(Color.parseColor("#a2ceff"));
        }
        else
            myViewHolder.itemView.setBackgroundColor(Color.parseColor("#00ffffff"));
        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedPosition=i;
                notifyDataSetChanged();
                branId=orders.get(i).getKey()+"";
                selectedPosition=i;
                addFragment(new MapFragment(),"map",Double.parseDouble(orders.get(i).getLat()),Double.parseDouble(orders.get(i).getLong()));
                notifyDataSetChanged();
            }
        });
    }
    @Override
    public int getItemCount() {
        return orders.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView branName,branshAdress,branshDistance;
        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            branName=itemView.findViewById(R.id.bransh_name);
            branshAdress=itemView.findViewById(R.id.bransh_adress);
            branshDistance=itemView.findViewById(R.id.bransh_distance);
        }
    }
    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");
                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
            } else {
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strAdd;
    }
    public String formatFigureTwoPlaces(float value){
        NumberFormat nf = NumberFormat.getNumberInstance(Locale.US);
        DecimalFormat myFormatter = (DecimalFormat)nf;
        myFormatter.applyPattern("##0.00");
        return myFormatter.format(value);
    }
    public void addFragment(Fragment fragment,String tag,double lat,double lng){
        MapFragment.lat=lat;
        MapFragment.lng=lng;
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.map_view, fragment, tag);
        ft.commitAllowingStateLoss();
    }
}
