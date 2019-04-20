package com.UV.rokonalzorobyan.UV.Api;

import com.UV.rokonalzorobyan.UV.Model.CartData;
import com.UV.rokonalzorobyan.UV.Model.PreviousModel.Example;
import com.UV.rokonalzorobyan.UV.Model.ListOfFoods;
import com.UV.rokonalzorobyan.UV.Model.Login;
import com.UV.rokonalzorobyan.UV.Model.PreviousModel.mainModel;
import com.UV.rokonalzorobyan.UV.Model.ReceiptData;
import com.UV.rokonalzorobyan.UV.Model.Register;
import com.UV.rokonalzorobyan.UV.Model.RegisterData;
import com.UV.rokonalzorobyan.UV.Model.SliderMosel1;
import com.UV.rokonalzorobyan.UV.Model.User;
import com.UV.rokonalzorobyan.UV.Model.branshsModle1;
import com.UV.rokonalzorobyan.UV.Model.previousAdressesModel;
import com.UV.rokonalzorobyan.UV.SliderModel.SliderSubData;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
public interface UserClient {
    @POST("loginclient")
    Call<User> login(@Body Login login);
    @POST("registerclient")
    Call<RegisterData> register(@Body Register register);
    @GET("data/menu")
    Call<ListOfFoods> getOrders(@Header("Authorization")String token);
    @GET("data/slider")
    Call<SliderSubData> getSliderImages(@Header("Authorization")String token);
    @FormUrlEncoded
    @POST("data/addtocartsingle")
    Call<ResponseBody> sendRequistToCart(@Header("Authorization")String token,
                                   @Field("cart_id") double id,
                                   @Field("cart_price") double itemPrice,
                                   @Field("cart_quantity") double itemQuantity);
    @FormUrlEncoded
    @POST("data/removeitem")
    Call<ResponseBody> delteRequistToCart(@Header("Authorization")String token, @Field("item_id") int id);
    @GET("data/cart")
    Call<CartData> getCartItems(@Header("Authorization")String apitoken);
    @FormUrlEncoded
    @POST("data/ratingcloss")
    Call<ResponseBody> coseRating(@Header("Authorization")String token,@Field("order_id") int ordId);
    @FormUrlEncoded
    @POST("data/reatings")
    Call<ResponseBody> sendRating(@Header("Authorization")String token, @Field("mealstars") int mealStars,
                                  @Field("order_id") int ordId,@Field("stars") int ordStars);
    @FormUrlEncoded
    @POST("data/reatings")
    Call<ResponseBody> sendRatingOrder(@Header("Authorization")String token,@Field("mealstars") int mealStars,
                                  @Field("order_id") int ordId);
    @FormUrlEncoded
    @POST("data/Branch")
    Call<branshsModle1> getBranshs(@Header("Authorization")String apitoken , @Field("lat") String lat, @Field("lon") String lon);
    @FormUrlEncoded
    @POST("data/checkout")
    Call<ResponseBody> sendCartReq(@Header("Authorization")String token,
                                         @Field("branch") String branshid,
                                         @Field("type") String status
    );
    @FormUrlEncoded
    @POST("data/checkout")
    Call<ResponseBody> sendCartReqWithLocation(@Header("Authorization")String token,
                                   @Field("type") String status,
                                   @Field("lat") String lat,
                                   @Field("lon") String lon,
                                   @Field("address") String adress);
    @FormUrlEncoded
    @POST("data/addtocart")
    Call<ResponseBody> sendLocalorders(@Header("Authorization")String token,
                                   @Field("cart_id") String[] cart_id,
                                   @Field("cart_price") String[] price,
                                   @Field("cart_quantity") String[] cart_quantity
    );
    @GET("data/address")
    Call<List<previousAdressesModel>> getPrevious(@Header("Authorization")String apitoken);

    @GET("data/abouts")
    Call<ResponseBody> aboutUs();

    @GET("data/Receipt")
    Call<ReceiptData> getReciptData(@Header("Authorization")String apitoken);
    @GET("data/Previous")
    Call<List<Example>> getPrev(@Header("Authorization")String apitoken);

    @POST("data/resend")
    Call<ResponseBody> sendReciet(@Header("Authorization")String token);
    @FormUrlEncoded
    @POST("data/contactus")
    Call<ResponseBody> contactUs(@Header("Authorization")String token,
                                               @Field("name") String name,
                                               @Field("email") String email,
                                               @Field("phone") String phone,
                                               @Field("message") String massage);
    @FormUrlEncoded
    @POST("data/quantity")
    Call<ResponseBody> ChartPlusMinus(@Header("Authorization")String token,
                                   @Field("item_id") String itemId,
                                   @Field("type") String type);
    @FormUrlEncoded
    @POST("data/quantity")
    Call<ResponseBody> minus(@Header("Authorization")String token,
                             @Field("item_id") String itemId
    );
    @GET("data/trackorders")
    Call<List<Example>> getCurrentOrder(@Header("Authorization")String apitoken);

    @FormUrlEncoded
    @POST("data/cancellation")
    Call<ResponseBody> deletCurrentOrder(@Header("Authorization")String token,
                                   @Field("order_id") int branshid);
    @FormUrlEncoded
    @POST("callback")
    Call<User> googleLogin(@Field("email") String email,@Field("provider") String prov,@Field("id") String id,@Field("name") String name);
    @FormUrlEncoded
    @POST("forgetpassword")
    Call<ResponseBody> sendEmail(@Field("email") String email);
    @FormUrlEncoded
    @POST("data/updatphone")
    Call<ResponseBody> sendPhon(@Header("Authorization")String token,@Field("phone") String phone);
}
