package com.apaza.proyecto.Interface;

import com.apaza.proyecto.Clases.Menu;
import com.apaza.proyecto.Clases.ResponseMessage;

import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by Julio Cesar on 2/12/2017.
 */

public interface ApiService {

    String API_BASE_URL = "https://proyecto-android-synms.c9users.io";

    @GET("api/v1/menus")
    Call<List<Menu>> getMenus();

    @FormUrlEncoded
    @POST("/api/v1/menus")
    Call<ResponseMessage> createMenu(@Field("tipo") String tipo,
                                         @Field("costo") String costo,
                                         @Field("comida") String comida,
                                         @Field("usuarios_id") Integer usuarios_id
                                         );

    @FormUrlEncoded
    @POST("/api/v1/login")
    Call<ResponseMessage> createAuth(@Field("username") String username,
                                     @Field("password") String password);

    @DELETE("/api/v1/menus/{id}")
    Call<ResponseMessage> destroyMenu(@Path("id") Integer id);



}

