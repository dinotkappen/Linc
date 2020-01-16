package com.app.linc.API;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {

    private static Retrofit retrofit = null;

    public static Retrofit getClient() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();


        retrofit = new Retrofit.Builder()
                .baseUrl("http://lincapp.cherrydemoserver10.com/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();


        return retrofit;
    }
    public static MultipartBody.Part getRequest(String paramName, File file) {
        return MultipartBody.Part.createFormData(paramName,file.getName(),imageData(file));
    }
    public static RequestBody imageData(File file) {
        return  getImageBody("multipart/form-data",file);
    }
    public static RequestBody getImageBody(String type, File file) {
        return RequestBody.create(MediaType.parse(type),file);
    }
    public static RequestBody plain(String content) {
        return  getRequestBody("text/plain",content);
    }
    public static RequestBody getRequestBody(String type, String content) {
        return RequestBody.create(MediaType.parse(type),content);
    }

}
