package com.UV.rokonalzorobyan.UV;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;
import com.UV.rokonalzorobyan.UV.Api.UserClient;
import com.UV.rokonalzorobyan.UV.Api.apiRequist;
import com.UV.rokonalzorobyan.UV.Model.PreviousModel.Example;
import com.UV.rokonalzorobyan.UV.Model.SliderMosel1;
import com.UV.rokonalzorobyan.UV.SliderModel.SliderSubData;
import com.bumptech.glide.Glide;
import java.util.ArrayList;
import java.util.List;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class HomePage extends Fragment {
    String[] imgsurls;
    ViewFlipper flip;
    static int i=0;
    Dialog dialog;
    Button btnrightSlide,btnLeftSlide;
    ArrayList<Example> orders;
    ProgressBar progressBar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        i=100;
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_home_page, container, false);
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        progressBar=getActivity().findViewById(R.id.progressbar1);
        try {
            getVersionName();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        flip = (ViewFlipper)getActivity(). findViewById(R.id.viewFlipper1);
        Button deliveryReq=getActivity().findViewById(R.id.delivery_req);
        Button fromBranch =getActivity().findViewById(R.id.btn_food_list);
        btnLeftSlide=getActivity().findViewById(R.id.slid_left);
        btnrightSlide=getActivity().findViewById(R.id.slid_right);
        getSliderImages();
        fromBranch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.statuus=0;
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container_lay,
                        new FoodListActivity()).addToBackStack("tag").commit();            }
        });
        deliveryReq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                MainActivity.statuus=1;
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container_lay,
                        new FoodListActivity()).addToBackStack("tag").commit();
            }
        });
        if (MainActivity.tokken.length()>10){
            firstOrderrate();
        }
        btnrightSlide.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                flip.setInAnimation(getActivity(),R.anim.pull_in_right);
                flip.setOutAnimation(getActivity(),R.anim.push_out_right);
                flip.showNext();
            }
        });
        btnLeftSlide.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                flip.setInAnimation(getActivity(),R.anim.pull_in_left);
                flip.setOutAnimation(getActivity(),R.anim.push_out_left);
                flip.setInAnimation(getActivity(),R.anim.pull_in_right);
                flip.setOutAnimation(getActivity(),R.anim.push_out_right);
                flip.showPrevious();
            }
        });
      /*  flip.setOnTouchListener(new View.OnTouchListener(){
            public boolean onTouch(View v, MotionEvent event){
                int FirstX=0;
                // TODO Auto-generated method stub
                if (event.getAction() == MotionEvent.ACTION_DOWN){
                    FirstX = (int) event.getX();
                }
                if (event.getAction() == MotionEvent.ACTION_MOVE){
                    int LastX = (int) event.getX();
                    if (FirstX - LastX > 750) {

                    } else if (LastX - FirstX > 750){
                        flip.setInAnimation(getActivity(),R.anim.pull_in_left);
                        flip.setOutAnimation(getActivity(),R.anim.push_out_left);
                        flip.setInAnimation(getActivity(),R.anim.pull_in_right);
                        flip.setOutAnimation(getActivity(),R.anim.push_out_right);
                    }
                }
                return true;
            }
        });*/
        }
    public void getSliderImages(){
        UserClient userClient = apiRequist.loginRequist();
        Call<SliderSubData> call = userClient.getSliderImages(MainActivity.tokken);
        call.enqueue(new Callback<SliderSubData>(){
            @Override
            public void onResponse(Call<SliderSubData> call, Response<SliderSubData> response){
                progressBar.setVisibility(View.GONE);
                try {
                    imgsurls=new String[response.body().getData().size()];
                } catch (Exception e){
                    e.printStackTrace();
                }
                if (response.isSuccessful()) {
                    try {
                        if (!response.body().getData().get(0).getAndroid().equals(getVersionName())){
                            updateDialog(R.layout.update_dialog,"",getActivity());
                        }
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }

                    try{   for (int i = 0; i < response.body().getData().size(); i++){
                        ImageView image = new ImageView(getActivity());
                        image.setScaleType(ImageView.ScaleType.FIT_XY);
                        Glide.with(getActivity())
                                .load(response.body().getData().get(i).getSliderImage())
                                .into(image);
                        flip.addView(image);
                    }}catch (Exception e){
                    }
                }
            }
            @Override
            public void onFailure(Call<SliderSubData> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
            }
        });
    }
    private void closeRating(int id){
        UserClient userClient = apiRequist.sendRequist();
        Call<ResponseBody> call = userClient.coseRating(MainActivity.tokken,id);
        call.enqueue(new Callback<ResponseBody>() {
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
        Button accept,cancel;
        final RatingBar orderBar,delBar;
        accept=dialog.findViewById(R.id.btn_rat);
        cancel=dialog.findViewById(R.id.cancle);
        orderBar =dialog.findViewById(R.id.ratingBar_order);
        delBar=dialog.findViewById(R.id.ratingBar_del);
        TextView delBarStr= dialog.findViewById(R.id.rate_del_str);;
        accept.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if (orders.get(orders.size()-1).getType().equals("طلب توصيل")){
                    if (Integer.parseInt((orderBar.getRating()+"").substring(0,1))>0&&Integer.parseInt((delBar.getRating()+"").substring(0,1))>0){
                        sendRateValues(Integer.parseInt((orderBar.getRating()+"").substring(0,1)),
                                orders.get(orders.size()-1).getKey(),
                                Integer.parseInt((delBar.getRating()+"").substring(0,1)));
                    }else {
                        Toast.makeText(getActivity(), "الرجاء تحديد قيمة التقييم", Toast.LENGTH_SHORT).show();
                    }
                    }
                            else {
                    if (Integer.parseInt((orderBar.getRating()+"").substring(0,1))>0){
                        sendRateValues(Integer.parseInt((orderBar.getRating()+"").substring(0,1)),
                                orders.get(orders.size()-1).getKey(),
                                6);
                    }else {
                        Toast.makeText(getActivity(), "الرجاء تحديد قيمة التقييم", Toast.LENGTH_SHORT).show();
                    }
                    }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                closeRating(orders.get(orders.size()-1).getKey());
            }
        });
        if (orders.get(orders.size()-1).getType().equals("طلب توصيل")){
            delBar.setVisibility(View.VISIBLE);
        }else{
            delBar.setVisibility(View.GONE);
            delBarStr.setVisibility(View.GONE);
        }
    }
    public void firstOrderrate(){
        orders=new ArrayList<>();
        UserClient userClient = apiRequist.sendRequist();
        Call<List<Example>> call = userClient.getPrev(MainActivity.tokken);
        call.enqueue(new Callback<List<Example>>(){
            @Override
            public void onResponse(Call<List<Example>> call, Response<List<Example>> response){
                if (response.isSuccessful()){
                    orders=(ArrayList<Example>)response.body();
                    if (orders.size()>0){
                        if (orders.get(orders.size()-1).getRate()==0){
                            dialog(R.layout.rating_lay,"",getActivity());
                        }
                    }
                }else {
                }
            }
            @Override
            public void onFailure(Call<List<Example>> call, Throwable t){
            }
        });
    }
    private void sendRateValues(int orderValue,int id,int del){
        UserClient userClient = apiRequist.sendRequist();
        Call<ResponseBody> call = userClient.sendRating(MainActivity.tokken,orderValue,id,del);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response){
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

    String getVersionName() throws PackageManager.NameNotFoundException {
        PackageInfo pinfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
        int versionNumber = pinfo.versionCode;
        String versionName = pinfo.versionName;
        return versionName;
    }

    void updateDialog(int id,String string,Context context){
        Button update;
        dialog = new Dialog(context);
        dialog.setContentView(id);
        dialog.setTitle(string);
        dialog.setCancelable(false);
        dialog.show();
        update=dialog.findViewById(R.id.btn_email);
        update.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                    openGooglePlay();
            }
        });
    }

    void openGooglePlay(){
        final String appPackageName = getActivity().getPackageName(); // getPackageName() from Context or Activity object
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        }
        catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }
}
