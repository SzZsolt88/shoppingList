package com.example.recyclerview2.user;


import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface UsersService {

    @Headers(
            {
    "Host: shoppinglist.byethost10.com",
    "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:91.0) Gecko/20100101 Firefox/91.0",
    "Accept: */*",
    "Accept-Language: en-US,en;q=0.5",
    "Accept-Encoding: gzip, deflate",
    "Content-Type: text/plain;charset=UTF-8",
    "Cookie: __test=2d88c5cb9b4d450d7b6c1da95f45f4db",
    "DNT: 1",
    "Connection: keep-alive"
            }
    )
    @POST("/")
    Call<ResponseBody> insertUser(@Body User user);
}
