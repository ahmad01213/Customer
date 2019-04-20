package com.UV.rokonalzorobyan.UV.Api;


import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class apiRequist {
    public static UserClient sendRequist(){
        Retrofit.Builder builder=new Retrofit.Builder()
                .baseUrl("https://rokon-alzorobyan.com.sa/public/api/v3/")
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit=builder.build();
        UserClient userClient=retrofit.create(UserClient.class);
        return userClient;
    }
    public static UserClient ratingReq(){
                Retrofit.Builder builder=new Retrofit.Builder()
                .baseUrl("https://www.rokon-alzorobyan.com.sa/")
                .addConverterFactory(GsonConverterFactory.create());
                Retrofit retrofit=builder.build();
                UserClient userClient=retrofit.create(UserClient.class);
                return userClient;
    }
    public static UserClient loginRequist(){
        Retrofit.Builder builder=new Retrofit.Builder()
                .baseUrl("https://rokon-alzorobyan.com.sa/public/api/")
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit=builder.build();
        UserClient userClient=retrofit.create(UserClient.class);
        return userClient;
    }
}
