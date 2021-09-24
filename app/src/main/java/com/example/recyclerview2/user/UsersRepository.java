package com.example.recyclerview2.user;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public final class UsersRepository {
    private static UsersRepository instance;

    private UsersService usersService;

    public static UsersRepository getInstance() {
        if (instance == null) {
            instance = new UsersRepository();
        }
        return instance;
    }

    public UsersRepository() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://shoppinglist.byethost10.com/site/list/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        usersService = retrofit.create(UsersService.class);
    }

    public UsersService getUsersService() {
        return usersService;
    }
}
