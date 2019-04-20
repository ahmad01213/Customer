package com.UV.rokonalzorobyan.UV;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.UV.rokonalzorobyan.UV.Api.UserClient;
import com.UV.rokonalzorobyan.UV.Api.apiRequist;
import com.UV.rokonalzorobyan.UV.Model.Login;
import com.UV.rokonalzorobyan.UV.Model.Register;
import com.UV.rokonalzorobyan.UV.Model.RegisterData;
import com.UV.rokonalzorobyan.UV.Model.User;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.LoggingBehavior;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.json.JSONException;
import org.json.JSONObject;
import java.lang.reflect.Type;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener,FacebookCallback<LoginResult> {
    TextView mIHaveAccuont;
    ProgressDialog progressdialog;
    static int state;
    CallbackManager callbackManager;
    LoginManager loginManager;
     Dialog dialog;
    Button googleSignIn;
    private static final String EMAIL = "email";
    static String nameLogin="";
    Button btnFaceLogin;
    String penddingTokken="",penddingName="";
    final static int GOOGL_REQ_CODE=901;
    TextView forget;
    private GoogleApiClient googleApiClient;
    EditText mEtxName,mEtxEmail,mEtxPhone,mEtxPassword,mEtxConfirmPassword;
    public static  String tokken="";
    ArrayList<String[]> localOrders=new ArrayList<>();
    Button registerlogin;
    UserClient userClient=apiRequist.loginRequist();
    UserClient userClient1=apiRequist.sendRequist();
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        FacebookSdk.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);
        setContentView(R.layout.activity_login);
        Thread.setDefaultUncaughtExceptionHandler(new CrashHandler(getApplicationContext()));
        PackageInfo info;
        try {
            info = getPackageManager().getPackageInfo("com.UV.rokonalzorobyan.UV", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures){
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                //String something = new String(Base64.encodeBytes(md.digest()));
                Log.e("hash key", something);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("name not found", e1.toString());
        } catch (NoSuchAlgorithmException e){
            Log.e("no such an algorithm", e.toString());
        } catch (Exception e){
            Log.e("exception", e.toString());
        }
        btnFaceLogin=findViewById(R.id.fa);
        btnFaceLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               facebookLogin();
            }
        });
        forget=findViewById(R.id.hot_line);
        callbackManager = CallbackManager.Factory.create();
        loginManager = com.facebook.login.LoginManager.getInstance();
        loginManager.registerCallback(callbackManager, LoginActivity.this);
        forget.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                   dialog(R.layout.forget_dialog,"الرجاء كتابة بريدك الإلكتروني",LoginActivity.this);
            }
        });
        GoogleSignInOptions googleSignInOptions=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleApiClient=new GoogleApiClient.Builder(this).enableAutoManage(this,this).addApi(Auth.GOOGLE_SIGN_IN_API,googleSignInOptions).build();
        googleSignIn=findViewById(R.id.google);
        googleSignIn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
               googleSignIn();
            }
        });
        closeKeyBoard(R.id.scroll);
        closeKeyBoard(R.id.lo);
        mIHaveAccuont=findViewById(R.id.i_have_accunt_text);
        registerlogin=findViewById(R.id.btn_login);
        mEtxName=findViewById(R.id.full_nam);
        mEtxEmail=findViewById(R.id.email);
        mEtxPhone=findViewById(R.id.login_phon_num);
        //Typeface typeface=Typeface.createFromAsset(getAssets(),"fonts/ar.ttf");
        mEtxPassword=findViewById(R.id.password);
        mEtxConfirmPassword=findViewById(R.id.confirm);
        if (state==1){
            mEtxConfirmPassword.setVisibility(View.VISIBLE);
            mEtxName.setVisibility(View.VISIBLE);
            mEtxPhone.setVisibility(View.VISIBLE);
            mIHaveAccuont.setText("لدي حساب");
            registerlogin.setText("سجل الاّن");
            state=5;
        }
        registerlogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                         if (mIHaveAccuont.getText().toString().trim().equals("لدي حساب")){
                             if (mEtxPassword.getText().toString().trim().length()>5){
                                 if (mEtxPassword.getText().toString().trim().equals(mEtxConfirmPassword.getText().toString().trim())){
                                     if (mEtxPhone.getText().length()>9&&mEtxPhone.getText().toString().substring(0,2).trim().equals("05")){
                                         showdialog();
                                         registerlogin.setEnabled(false);
                                         register(mEtxEmail.getText().toString().trim(),mEtxPassword.getText().toString().trim(),mEtxName.getText().toString().trim(),mEtxPhone.getText().toString().trim());
                                     }else {
                                         registerlogin.setEnabled(true);

                                         Toast.makeText(LoginActivity.this, "برجاء إكمال الحقول واستخدام رقم هاتف صحيح يبدأ ب 05", Toast.LENGTH_LONG).show();
                                     }
                                 }else {
                                     registerlogin.setEnabled(true);
                                     Toast.makeText(LoginActivity.this, "كلمتا السر غير متطابقتين", Toast.LENGTH_SHORT).show();
                                 }
                             }else {
                                 registerlogin.setEnabled(true);
                                 Toast.makeText(LoginActivity.this, "الرجاء كتابة كلمة سر تتكون من 6 أحرف أو أرقام علي الأقل ", Toast.LENGTH_SHORT).show();
                             }

                     }else if (mIHaveAccuont.getText().toString().trim().equals("مستخدم جديد")){
                             showdialog();
                             login();
                             registerlogin.setEnabled(false);
                             }
            }
        });
        mIHaveAccuont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if (mIHaveAccuont.getText().toString().trim().equals("لدي حساب")){
                        mEtxName.setVisibility(View.GONE);
                        mEtxPhone.setVisibility(View.GONE);
                        mEtxConfirmPassword.setVisibility(View.GONE);
                        mEtxName.setVisibility(View.GONE);
                        mEtxPhone.setVisibility(View.GONE);
                        mEtxConfirmPassword.setVisibility(View.GONE);
                        mIHaveAccuont.setText("مستخدم جديد");
                        registerlogin.setText("تسجيل الدخول");
                    }else{

                        mEtxName.setVisibility(View.VISIBLE);
                        mEtxPhone.setVisibility(View.VISIBLE);
                        mEtxName.setVisibility(View.VISIBLE);
                        mEtxPhone.setVisibility(View.VISIBLE);
                        mEtxConfirmPassword.setVisibility(View.VISIBLE);
                        mEtxConfirmPassword.setVisibility(View.VISIBLE);
                        mIHaveAccuont.setText("لدي حساب");
                        registerlogin.setText(" سجل الأن");
                    }
            }
        });
    }

    private void login(){
        getLocalOrders();
        String strEmail=mEtxEmail.getText().toString().trim();
        String strPasswoord=mEtxPassword.getText().toString().trim();
        Login login=new Login(strEmail,strPasswoord);
        Call<User> call=userClient.login(login);
        call.enqueue(new Callback<User>(){
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.body().isStatus()==true){
                    if (response.body().getPhone().trim().equals("1")){
                        progressdialog.cancel();
                        MainActivity.loginName=response.body().getName();
                        tokken="Bearer"+" "+response.body().getToken();
                        SharedPreferences settings = LoginActivity.this.getSharedPreferences("zorobian_customer_pref_name", 0);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString("berior_token", tokken);
                        editor.putString("name",response.body().getName());
                        editor.apply();
                        MainActivity.tokken=tokken;
                        for (int i=0;i<localOrders.size();i++){
                            sendFoodToCart(Integer.parseInt(localOrders.get(i)[4]),Double.parseDouble(localOrders.get(i)[3]),Integer.parseInt(localOrders.get(i)[1]));
                        }
                        Toast.makeText(LoginActivity.this, "مرحبا "+response.body().getName(), Toast.LENGTH_SHORT).show();
                        SharedPreferences settings1 = LoginActivity.this.getSharedPreferences("zorobian_customer_FOODS_pref_name", 0);
                        SharedPreferences.Editor editor1 = settings1.edit();
                        editor1.clear();
                        editor1.apply();
                        finish();
                    }else {
                        progressdialog.cancel();
                        penddingName=response.body().getName();
                        penddingTokken="Bearer"+" "+response.body().getToken();
                        dialogPhone(R.layout.dialog_phon,"اكتب رقم الهاتف",LoginActivity.this);
                    }
                }else {
                    progressdialog.cancel();
                    registerlogin.setEnabled(true);
                    progressdialog.cancel();
                    registerlogin.setEnabled(true);
                    progressdialog.cancel();
                    Toast.makeText(LoginActivity.this, "البريد الإلكتروني أو كلمة السر غير صحيحة", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<User> call, Throwable t){
                progressdialog.cancel();
                registerlogin.setEnabled(true);
                Toast.makeText(LoginActivity.this,"الرجاء التأكد من الإتصال بالإنترنت ",Toast.LENGTH_LONG).show();
            }
        });
    }
    private void register(String strEmail,String strPasswoord,String strName,String strOhone){
        nameLogin=mEtxName.getText().toString().trim();
        Register regist=new Register(strName,strEmail,strOhone,strPasswoord);
        Call<RegisterData> call=userClient.register(regist);
        call.enqueue(new Callback<RegisterData>(){
            @Override
            public void onResponse(Call<RegisterData> call, Response<RegisterData> response){
                if (response.isSuccessful()){
                    progressdialog.cancel();
                    login();
                }else{
                    String email="",phone="";
                    registerlogin.setEnabled(true);
                    progressdialog.cancel();
                    Gson gson = new Gson();
                    Type type = new TypeToken<RegisterData>() {}.getType();
                    RegisterData errorResponse = gson.fromJson(response.errorBody().charStream(),type);
                    try {
                        email=errorResponse.getError().getEmail().get(0)+"";
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        phone=errorResponse.getError().getPhone().get(0)+"";
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                     Toast.makeText(LoginActivity.this, email+phone, Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<RegisterData> call, Throwable t){
                registerlogin.setEnabled(true);
                progressdialog.cancel();
                Toast.makeText(LoginActivity.this,"الرجاءالتأكد من الاتصال بالإنترنت",Toast.LENGTH_LONG).show();
            }
        });
    }
    public void getLocalOrders(){
        try{
            localOrders=new ArrayList<>();
            SharedPreferences settings =getSharedPreferences("zorobian_customer_FOODS_pref_name", 0);
            Map<String,?> keys = settings.getAll();
            for(Map.Entry<String,?> entry : keys.entrySet()){
                localOrders.add(entry.getValue().toString().split(","));
            }
        }catch (Exception e){
        }
    }
    private void sendFoodToCart(int id,double cost,int quantity){
        Call<ResponseBody> call=userClient1.sendRequistToCart(MainActivity.tokken,id ,cost,quantity);
        call.enqueue(new Callback<ResponseBody>(){
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                }else{
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });
    }
    void closeKeyBoard(int id){
            findViewById(id).setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event){
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                try {
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return false;
            }
        });
    }
    public void showdialog(){
        progressdialog = new ProgressDialog(this);
        progressdialog.setMessage("الرجاء الإنتظار ....");
        progressdialog.setCancelable(false);
        progressdialog.show();
    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }
    void googleSignIn(){
         Intent intent=Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
         startActivityForResult(intent,GOOGL_REQ_CODE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        callbackManager.onActivityResult(requestCode,resultCode,data);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==GOOGL_REQ_CODE){
            GoogleSignInResult result=Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleResaulte(result);
        }
    }
    void handleResaulte(GoogleSignInResult result){
        if (result.isSuccess()){
          registerGoogle(result.getSignInAccount().getEmail()+"","google",result.getSignInAccount().getId()+"",result.getSignInAccount().getDisplayName()+"");
        }else Toast.makeText(this, "notSucces", Toast.LENGTH_SHORT).show();
    }
    private void registerGoogle(String strEmail,String prov,String id,String name){
        showdialog();
        getLocalOrders();
        UserClient userClient3=apiRequist.loginRequist();
        Call<User> call=userClient3.googleLogin(strEmail,prov,id,name);
        call.enqueue(new Callback<User>(){
            @Override
            public void onResponse(Call<User> call, Response<User> response){
                if (response.isSuccessful()){
                    if (response.body().getPhone().trim().equals("1")){
                        MainActivity.loginName=response.body().getName();
                        tokken="Bearer"+" "+response.body().getToken();
                        SharedPreferences settings = LoginActivity.this.getSharedPreferences("zorobian_customer_pref_name", 0);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString("berior_token", tokken);
                        editor.putString("name",response.body().getName());
                        editor.apply();
                        MainActivity.tokken=tokken;
                        for (int i=0;i<localOrders.size();i++){
                            sendFoodToCart(Integer.parseInt(localOrders.get(i)[4]),Double.parseDouble(localOrders.get(i)[3]),Integer.parseInt(localOrders.get(i)[1]));
                        }
                        Toast.makeText(LoginActivity.this, "مرحبا "+response.body().getName(), Toast.LENGTH_SHORT).show();
                        SharedPreferences settings1 = LoginActivity.this.getSharedPreferences("zorobian_customer_FOODS_pref_name", 0);
                        SharedPreferences.Editor editor1 = settings1.edit();
                        editor1.clear();
                        editor1.apply();
                        finish();
                    }else {
                        penddingName=response.body().getName();
                        penddingTokken="Bearer"+" "+response.body().getToken();
                        dialogPhone(R.layout.dialog_phon,"اكتب رقم الهاتف",LoginActivity.this);
                    }
                }else{
                    Toast.makeText(LoginActivity.this, "error", Toast.LENGTH_SHORT).show();
                }
                    progressdialog.cancel();
            }
            @Override
            public void onFailure(Call<User> call, Throwable t){
                Toast.makeText(LoginActivity.this,"الرجاءالتأكد من الاتصال بالإنترنت",Toast.LENGTH_LONG).show();
                    progressdialog.cancel();
            }
        });
    }
    void dialog(int id, String string, Context context){
        final EditText email;
        Button btnSendEmail;
        dialog = new Dialog(context);
        dialog.setContentView(id);
        dialog.setTitle(string);
        dialog.show();
        email=dialog.findViewById(R.id.etx_email);
        btnSendEmail=dialog.findViewById(R.id.btn_email);
        btnSendEmail.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if (email.getText().toString().trim().length()>5){
                    sendEmail(email.getText().toString());
                }else{
                    Toast.makeText(LoginActivity.this, "الرجاء كتابة بريد إلكتروني صحيح", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void facebookLogin(){
        loginManager.logInWithReadPermissions(this, Arrays.asList("email", "public_profile"));
    }
    @Override
    public void onSuccess(final LoginResult loginResult) {

        if (loginResult != null) {
            final Profile profile = Profile.getCurrentProfile();
            if (profile != null) {
            }
            GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback(){
                @Override
                public void onCompleted(JSONObject object, GraphResponse response){
                    final JSONObject json = response.getJSONObject();
                    String string="";
                    try {
                        string=json.getString("email");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    registerGoogle(string,"facebook",profile.getId(),profile.getFirstName()+" "+profile.getLastName()+"");
                }
                });
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,link,email,picture");
            request.setParameters(parameters);
            request.executeAsync();
        }
            }
    @Override
    public void onCancel() {
    }
    @Override
    public void onError(FacebookException error){
        Toast.makeText(this, error.getMessage()+"", Toast.LENGTH_SHORT).show();
    }
    public void sendEmail(String email){
        UserClient userClient = apiRequist.loginRequist();
        Call<ResponseBody> call = userClient.sendEmail(email);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    Toast.makeText(LoginActivity.this, "تم إرسال رسالة الي بريدك الإلكتروني", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LoginActivity.this, "البريد الإلكتروني الذي أدخلته غير مسجل", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t){
            }
        });
    }
    public void sendPhone(String phone){
        UserClient userClient = apiRequist.sendRequist();
        Call<ResponseBody> call = userClient.sendPhon(penddingTokken,phone);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    dialog.dismiss();
                    MainActivity.loginName=penddingName;
                    tokken=penddingTokken;
                    SharedPreferences settings = LoginActivity.this.getSharedPreferences("zorobian_customer_pref_name", 0);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString("berior_token", tokken);
                    editor.putString("name",penddingName);
                    editor.apply();
                    MainActivity.tokken=tokken;
                    for (int i=0;i<localOrders.size();i++){
                        sendFoodToCart(Integer.parseInt(localOrders.get(i)[4]),Double.parseDouble(localOrders.get(i)[3]),Integer.parseInt(localOrders.get(i)[1]));
                    }
                    Toast.makeText(LoginActivity.this, "مرحبا "+penddingName, Toast.LENGTH_SHORT).show();
                    SharedPreferences settings1 = LoginActivity.this.getSharedPreferences("zorobian_customer_FOODS_pref_name", 0);
                    SharedPreferences.Editor editor1 = settings1.edit();
                    editor1.clear();
                    editor1.apply();
                    finish();
                } else {
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t){
            }
        });
    }
    void dialogPhone(int id, String string, Context context){
        final EditText phonNum;
        Button btnSendPhon;
        dialog = new Dialog(context);
        dialog.setContentView(id);
        dialog.setTitle(string);
        dialog.show();
        phonNum=dialog.findViewById(R.id.etx_email);
        btnSendPhon=dialog.findViewById(R.id.btn_email);
        btnSendPhon.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if (phonNum.getText().toString().trim().length()>5&&phonNum.getText().toString().substring(0,2).trim().equals("05")){
                    sendPhone(phonNum.getText().toString());
                }else{
                    Toast.makeText(LoginActivity.this, "الرجاء كتابة رقم هاتف صحيح يبدأ ب 05", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
