package com.UV.rokonalzorobyan.UV;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.UV.rokonalzorobyan.UV.Api.UserClient;
import com.UV.rokonalzorobyan.UV.Api.apiRequist;
import com.UV.rokonalzorobyan.UV.Model.previousAdressesModel;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import java.util.List;
import java.util.Locale;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class LocationActivity extends Activity {
    String placeName="";
    double longitud,lat;
    Button btnMy,map,prev;
    ProgressDialog progressdialog;
    static Activity fa;
    String [] previousAdress;
    static Spinner spBranshs;
    TextView btnSendreq,StringprevAdress;
    int i=0;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        btnMy=findViewById(R.id.yourLocation);
        map=findViewById(R.id.map);
        prev=findViewById(R.id.prev);
        btnMy.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v){
                btnMy.setBackground(getResources().getDrawable(R.drawable.loc_btns_shadow));
                map.setBackground(getResources().getDrawable(R.drawable.button_contact_us));
                i=1;
                Toast.makeText(LocationActivity.this,"موقعك هو  :"+MainActivity.placeName, Toast.LENGTH_SHORT).show();
                try {
                    spBranshs.setVisibility(View.GONE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        map.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v){
                btnMy.setBackground(getResources().getDrawable(R.drawable.button_contact_us));
             i=2;
                try {
                    spBranshs.setVisibility(View.GONE);
                } catch (Exception e){
                    e.printStackTrace();
                }
                int PLACE_PICKER_REQUEST = 99;
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    startActivityForResult(builder.build(LocationActivity.this), PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e){
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               i=3;
               }
        });
        fa=this;
        btnSendreq=findViewById(R.id.complet);
        spBranshs =findViewById(R.id.spin_customer_location);
        StringprevAdress =findViewById(R.id.loc_string);
        btnSendreq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                showdialog();
                String string="";
                try {
                    if (i==2){
                        sendRequistsWithLocation("2",lat+"",longitud+"",getCompleteAddressString(lat,longitud),"0");
                    }else if (i==1){
                        sendRequistsWithLocation("2",MainActivity.lat,MainActivity.lng,MainActivity.placeName,"0");
                    }
                    else{
                    }
                }catch (Exception e){
                }
            }
        });
    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK&&requestCode==99){
                    Place place = PlacePicker.getPlace(this, data);
                     lat = place.getLatLng().latitude;
                     longitud =place.getLatLng().longitude;
            map.setBackground(getResources().getDrawable(R.drawable.loc_btns_shadow));
        }
    }
    public void sendRequistsWithLocation(String status,String lat,String lon,String adress,String prevAdress){
        UserClient userClient = apiRequist.sendRequist();
        Call<ResponseBody> call = userClient.sendCartReqWithLocation(MainActivity.tokken,status,lat,lon,adress);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response){
                if (response.isSuccessful()){
                    progressdialog.cancel();
                    startActivity(new Intent(LocationActivity.this,Receit.class));
                    finish();
                }else {
                    Toast.makeText(LocationActivity.this, "الرجاء التأكد من الإتصال بالإنترنت", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t){
                progressdialog.cancel();
            }
        });
    }
    public void getpreviousAdresses(){
        UserClient userClient = apiRequist.sendRequist();
        Call<List<previousAdressesModel>> call = userClient.getPrevious(MainActivity.tokken);
        call.enqueue(new Callback<List<previousAdressesModel>>(){
            @Override
            public void onResponse(Call<List<previousAdressesModel>> call, Response<List<previousAdressesModel>> response) {
                    if (response.isSuccessful()){
                        previousAdress=new String[response.body().size()+1];
                        previousAdress[0]="حدد موقعك";
                        for (int i=1;i<response.body().size();i++){
                            try{
                                previousAdress[i]=response.body().get(i).getAddress()+"";
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        try{
                            spBranshs.setAdapter(new ArrayAdapter<String>(LocationActivity.this,
                            android.R.layout.simple_spinner_dropdown_item, previousAdress));
                        } catch (Exception e){
                            e.printStackTrace();
                        }
                    }else{
                        }
            }
            @Override
            public void onFailure(Call<List<previousAdressesModel>> call, Throwable t){
            }
        });
    }
    public void showdialog(){
        progressdialog = new ProgressDialog(this);
        progressdialog.setMessage("الرجاء الإنتظار ....");
        progressdialog.setCancelable(false);
        progressdialog.show();
    }
    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(LocationActivity.this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");
                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
                placeName = strReturnedAddress.toString();
            } else {
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strAdd;
    }
}
