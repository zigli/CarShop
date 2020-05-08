package cf.tilgiz.parse.utils;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiRequest {
    @FormUrlEncoded
    @POST("/api/do_auth.php")
    Call<Object> requestPost(@Field("username") String username, @Field("password") String password);

    @POST("/api/do_order.php")
    Call<Object> requestPostOrder(@Body RequestBody orderJson);
}
