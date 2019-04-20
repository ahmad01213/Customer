package com.UV.rokonalzorobyan.UV;
import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.UV.rokonalzorobyan.UV.Api.UserClient;
import com.UV.rokonalzorobyan.UV.Api.apiRequist;
import com.UV.rokonalzorobyan.UV.Model.CartData;
import com.UV.rokonalzorobyan.UV.Model.cartItem;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class MainActivity extends AppCompatActivity implements LocationListener, NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, ResultCallback<LocationSettingsResult> {
    private DrawerLayout mDrawerLayout;
    public static NavigationView nVDrawer;
    static String loginName;
    static int currentorederShwowState=0;
    int i = 5;
    static int d1=1;
    Button login, reg;
    static String placeName = "";
    TextView loginNameview;
    static int statuus=5;
    public static String tokken = "";
    ArrayList<String[]> localOrders;
    ImageView imgHome;
    static TextView notifCount, cartImg;
    protected LocationRequest locationRequest;
    static String lng = "", lat = "";
    protected static final String TAG = "LocationOnOff";
    private GoogleApiClient googleApiClient;
    final static int REQUEST_LOCATION = 199;
    int d = 5;
    protected GoogleApiClient mGoogleApiClient;
    LocationManager locationManager;
    String mprovider;
    static String imgUrl="";
    Timer timer;
    TimerTask timerTask;
    final Handler handler = new Handler();
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    static Toolbar toolbar;
    static ArrayList<cartItem> orders;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Thread.setDefaultUncaughtExceptionHandler(new CrashHandler(getApplicationContext()));
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1){
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();
        mGoogleApiClient.connect();
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(30 * 1000);
        locationRequest.setFastestInterval(5 * 1000);
        locationSettingsReq();
        checkLocationPermission();
        initialize();
        SharedPreferences settings = getSharedPreferences("zorobian_customer_pref_name", 0);
        tokken = settings.getString("berior_token", "");
        loginName = settings.getString("name", "");
        toolbar = findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.nav_icon);
        mDrawerLayout = findViewById(R.id.drawer_layout1);
        notifCount = findViewById(R.id.notif_count);
        cartImg = findViewById(R.id.cart_img);
        nVDrawer = findViewById(R.id.nav_view1);
        imgHome=findViewById(R.id.logoo);
        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.container_lay,
                        new HomePage()).addToBackStack("tag").commit();
            }
        });
        View header = nVDrawer.getHeaderView(0);
        login = header.findViewById(R.id.login);
        loginNameview = header.findViewById(R.id.login_nam);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.closeDrawer(GravityCompat.START);
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        });
        reg = header.findViewById(R.id.register);
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.closeDrawer(GravityCompat.START);
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                intent.putExtra("reg", "1");
                startActivity(intent);
                LoginActivity.state=1;
            }
        });
        nVDrawer.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container_lay,
                    new HomePage()).commit();
            nVDrawer.setCheckedItem(android.R.id.home);
        }
        cartImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.container_lay,
                        new Chart()).addToBackStack("tag").commit();
            }
        });
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDrawerLayout.isDrawerOpen(Gravity.RIGHT)) {
                        mDrawerLayout.closeDrawer(Gravity.RIGHT);
                }else {
                        mDrawerLayout.openDrawer(Gravity.RIGHT);
                }
            }
        });
        toolbar.setTitle("");
        try {
            try {
                getSupportActionBar().setDisplayShowTitleEnabled(false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        TextView navHom = header.findViewById(R.id.nav_hom);
        navHom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                mDrawerLayout.closeDrawer(GravityCompat.START);
            }
        });
        ImageView logoHom = header.findViewById(R.id.logo_hom);
        logoHom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.closeDrawer(GravityCompat.START);
                getSupportFragmentManager().beginTransaction().replace(R.id.container_lay,
                        new HomePage()).addToBackStack("tag").commit();
            }
        });
        mDrawerLayout.setScrimColor(getResources().getColor(android.R.color.transparent));
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem){
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.home_page:
                getSupportFragmentManager().beginTransaction().replace(R.id.container_lay,
                        new HomePage()).addToBackStack("tag").commit();
                break;
            case R.id.about_us:
                getSupportFragmentManager().beginTransaction().replace(R.id.container_lay,
                        new AboutUs()).addToBackStack("tag").commit();
                break;
            case R.id.chart:
                getSupportFragmentManager().beginTransaction().replace(R.id.container_lay,
                        new Chart()).addToBackStack("tag").commit();
                break;
            case R.id.prev_orders:
                getSupportFragmentManager().beginTransaction().replace(R.id.container_lay,
                        new PreviosOrders()).addToBackStack("tag").commit();
                break;
            case R.id.current:
                getSupportFragmentManager().beginTransaction().replace(R.id.container_lay,
                        new CurrentOrder()).addToBackStack("tag").commit();
                break;
            case R.id.contact_us:
                getSupportFragmentManager().beginTransaction().replace(R.id.container_lay,
                        new ContactUs()).addToBackStack("tag").commit();
                break;
            case R.id.food_list:
                getSupportFragmentManager().beginTransaction().replace(R.id.container_lay,
                        new FoodListActivity()).addToBackStack("tag").commit();
                break;
            case R.id.logout:
                tokken = "";
                notifCount.setText("0");
                SharedPreferences settings = MainActivity.this.getSharedPreferences("zorobian_customer_pref_name", 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.remove("berior_token");
                editor.apply();
                Toast.makeText(this, "تم تسجيل الخروج", Toast.LENGTH_SHORT).show();
                Menu nav_Menu = nVDrawer.getMenu();
                nav_Menu.findItem(R.id.logout).setVisible(false);
                break;
        }
        toolbar.setTitle("");
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
    public void getOrders() {
        orders = new ArrayList<>();
        UserClient userClient = apiRequist.sendRequist();
        Call<CartData> call = userClient.getCartItems(MainActivity.tokken);
        call.enqueue(new Callback<CartData>() {
            @Override
            public void onResponse(Call<CartData> call, Response<CartData> response) {
                if (response.isSuccessful()) {
                    try {
                        orders = (ArrayList<cartItem>) response.body().getItems().get(0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    notifCount.setText(orders.size() + "");
                } else {
                }
            }
            @Override
            public void onFailure(Call<CartData> call, Throwable t) {
            }
        });
    }
    public void getLocalOrders() {
        localOrders = new ArrayList<>();
        SharedPreferences settings = getSharedPreferences("zorobian_customer_FOODS_pref_name", 0);
        Map<String, ?> keys = settings.getAll();
        for (Map.Entry<String, ?> entry : keys.entrySet()){
            localOrders.add(entry.getValue().toString().split(","));
        }
        notifCount.setText(localOrders.size() + "");
    }
    public void startTimer() {
        timer = new Timer();
        initializeTimerTask();
        timer.schedule(timerTask, 0, 4000);
    }
    public void stoptimertask() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }
    public void initializeTimerTask() {
        timerTask = new TimerTask() {
            public void run() {
                handler.post(new Runnable() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    public void run() {
                        if (tokken.length() > 10) {
                                getLocation();
                            if (loginNameview.getVisibility()==View.GONE||loginNameview.getVisibility()==View.INVISIBLE){
                                Menu nav_Menu = nVDrawer.getMenu();
                                nav_Menu.findItem(R.id.logout).setVisible(true);
                                login.setVisibility(View.GONE);
                                reg.setVisibility(View.GONE);
                                loginNameview.setVisibility(View.VISIBLE);
                                loginNameview.setText(loginName);
                            }
                            try {
                                getOrders();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }else{
                            if ( nVDrawer.getMenu().findItem(R.id.logout).isVisible()){
                                nVDrawer.getMenu().findItem(R.id.logout).setVisible(false);
                            }
                            if (reg.getVisibility()==View.GONE||reg.getVisibility()==View.INVISIBLE){
                                login.setVisibility(View.VISIBLE);
                                reg.setVisibility(View.VISIBLE);
                                try {
                                    loginNameview.setVisibility(View.GONE);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            getLocalOrders();
                        }
                    }
                });
            }
        };
    }
    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
                    Criteria criteria = new Criteria();
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    //  locationManager.requestLocationUpdates(mprovider, 15000, 1, this);
                    // Location location = locationManager.getLastKnownLocation(mprovider);
                    //  mLongtute =location.getLongitude()+" ";
                    // mLatitud =location.getLatitude()+" ";
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        //Request location updates:
                    }

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_LOCATION) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(getApplicationContext(), "تم تفعيل خاصية الموقع", Toast.LENGTH_LONG).show();
            } else {
                finish();
                Toast.makeText(getApplicationContext(), "الرجاء تفعيل خاصية تحديد الموقع أولاً للدخول", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);
        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(
                        mGoogleApiClient,
                        builder.build()
                );
        result.setResultCallback(this);
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    @Override
    public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
    }

    @Override
    public void onLocationChanged(Location location) {
        lat=location.getLatitude()+"";
        lng=location.getLongitude()+"";
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    public void initialize() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);
        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(
                        mGoogleApiClient,
                        builder.build()
                );
        result.setResultCallback(this);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        mprovider = locationManager.getBestProvider(criteria, false);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria1 = new Criteria();
        mprovider = locationManager.getBestProvider(criteria, false);
        if (mprovider != null && !mprovider.equals("")) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            Location location = locationManager.getLastKnownLocation(mprovider);
            locationManager.requestLocationUpdates(mprovider, 3600000, 500, this);
            if (location != null)
                onLocationChanged(location);
        }

    }

    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
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

    @Override
    public void onBackPressed() {
        if (!mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
            if (fm.getBackStackEntryCount() > 0) {
                Fragment f = getSupportFragmentManager().findFragmentById(R.id.container_lay);
                if (f instanceof HomePage) {
                    finish();
                } else {
                    fm.popBackStack();
                }
            } else {
                super.onBackPressed();
            }
        } else {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        stoptimertask();
    }

    @Override
    protected void onResume() {
        super.onResume();
        startTimer();
    }

    void dialog(int id, String string, Context context) {
        Button bransh, del;
        final Dialog dialog;
        dialog = new Dialog(context);
        dialog.setContentView(id);
        bransh = dialog.findViewById(R.id.btn_food_list);
        del = dialog.findViewById(R.id.delivery_req);
        dialog.setTitle(string);
        dialog.show();
        bransh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.container_lay,
                        new FoodListActivity()).addToBackStack("tag").commit();
                SharedPreferences settings = getSharedPreferences("zorobian_customer_pref_name", 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putInt("status", 0);
                editor.apply();
                dialog.dismiss();
            }
        });
        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.container_lay,
                        new FoodListActivity()).addToBackStack("tag").commit();
                SharedPreferences settings = getSharedPreferences("zorobian_customer_pref_name", 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putInt("status", 1);
                editor.apply();
                dialog.dismiss();
            }
        });
    }

    public void locationSettingsReq() {
        final LocationManager manager = (LocationManager) MainActivity.this.getSystemService(Context.LOCATION_SERVICE);
        if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && hasGPSDevice(MainActivity.this)) {
        }
        // Todo Location Already on  ... end

        if (!hasGPSDevice(MainActivity.this)) {
            finish();
        }

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && hasGPSDevice(MainActivity.this)) {
            enableLoc();
        } else {
        }
    }
    private boolean hasGPSDevice(Context context) {
        final LocationManager mgr = (LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE);
        if (mgr == null)
            return false;
        final List<String> providers = mgr.getAllProviders();
        if (providers == null)
            return false;
        return providers.contains(LocationManager.GPS_PROVIDER);
    }

    private void enableLoc() {
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(MainActivity.this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                        @Override
                        public void onConnected(Bundle bundle) {
                        }

                        @Override
                        public void onConnectionSuspended(int i) {
                            googleApiClient.connect();
                        }
                    })
                    .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                        @Override
                        public void onConnectionFailed(ConnectionResult connectionResult) {
                        }
                    }).build();
            googleApiClient.connect();
            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(30 * 1000);
            locationRequest.setFastestInterval(5 * 1000);
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest);
            builder.setAlwaysShow(true);
            PendingResult<LocationSettingsResult> result =
                    LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                @Override
                public void onResult(LocationSettingsResult result) {
                    final Status status = result.getStatus();
                    switch (status.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            try {
                                // Show the dialog by calling startResolutionForResult(),
                                // and check the result in onActivityResult().
                                status.startResolutionForResult(MainActivity.this, REQUEST_LOCATION);
                            } catch (IntentSender.SendIntentException e) {
                                // Ignore the error.
                            }
                            break;
                    }
                }
            });
        }
    }
    void getLocation() {
        Location mLastLocation;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest);
            builder.setAlwaysShow(true);
            PendingResult<LocationSettingsResult> result =
                    LocationServices.SettingsApi.checkLocationSettings(
                            mGoogleApiClient,
                            builder.build()
                    );
            result.setResultCallback(this);
            lat=mLastLocation.getLatitude()+"";
            lng=mLastLocation.getLongitude()+"";
            getCompleteAddressString(Double.parseDouble(lat),Double.parseDouble(lng));
        }
    }
}




