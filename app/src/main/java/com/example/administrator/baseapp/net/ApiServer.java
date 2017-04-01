package com.example.administrator.baseapp.net;

import com.example.administrator.baseapp.net.request.BaseRequestJson;
import com.example.administrator.baseapp.net.request.LoginRequest;
import com.example.administrator.baseapp.net.response.BaseResponseJson;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Created by beini on 2017/2/10.
 */

public interface ApiServer<T> {
    /**
     * 参数
     * Query 其实就是 Url 中 ‘?’ 后面的 key-value
     * QueryMap :多个参数
     */
    @GET("/")
    Call<ResponseBody> sendRequestGet(@Query("page") String page);

    /**
     * Field & FieldMap
     */
    @FormUrlEncoded
    @POST("/")
    Call<ResponseBody> sendRequestPost(
            @Field("name") String name,
            @Field("occupation") String occupation);

    /**
     * json
     */
    @GET("{url}")
    Call<BaseResponseJson> sendRequestGet(@Path("url") String url, @Body BaseRequestJson baseRequestJson);

    @POST("{url}")
    Call<BaseResponseJson> sendRequestPost(@Path("url") String url, @Body BaseRequestJson baseRequestJson);

    @POST("{url}")
    Call<BaseResponseJson> sendRequestPost1(@Path("url") String url, @Body T baseRequestJson);

    /**
     * @param file
     * @return
     * @Part(“description”) 就是RequestBody实例中包裹的字符串值
     * @Part MultipartBody.Part file 我们使用MultipartBody.Part类，使我们能够发送实际文件 file就是你要往服务器上传的文件。
     */
//    @Multipart
//    @POST("{url}")
//    //单张
//    Call<ResponseBody> uploadFile(@Path("url") String url, @Part("description") RequestBody description,
//                                  @Part MultipartBody.Part file);
    @Multipart
    @POST("{url}")
    //单张
    Call<ResponseBody> uploadFile(@Path("url") String url,
                                  @Part MultipartBody.Part file);

    /**
     * http://blog.csdn.net/sk719887916/article/details/51755427
     *
     * @param url
     * @return
     */
    @Multipart
    @POST("{url}")
//多张
    Call<ResponseBody> uploadFile(@Path("url") String url, @PartMap() List<MultipartBody.Part> parts);

    //多张
    Call<ResponseBody> uploadFile(@Path("url") String url, @Body MultipartBody multipartBody);
    /**
     * 断点续传
     * 可以做取消、继续下载的功能，或者从异常情况下恢复的再下载功能。
     */

    /**
     * 文件下载
     *
     * @param fileUrl
     * @return content-length:文件大小
     * content- type：文件类型
     * RANGE：文件哪里下载
     * 如果对下载来源校验可以加入referer, 不是目标来源的可以不予下载权限。
     */
    @Streaming//下载大文件时候使用
    @GET
    Call<ResponseBody> downloadFile(@Url String fileUrl);

    /**
     * 断点下载
     * 请求文件总大小
     * <p>
     * 根据机型高低，分配多个线程下载
     * <p>
     * 记录下载进度，大小，类型等到数据库
     * <p>
     * 同时更新UI和通知栏，提示用户
     * <p>
     * 下载结束后更新数据库下载数据，追加组合文件
     * <p>
     * 判断文件大小，检验文件大小
     *
     * @param range
     * @param url
     * @return
     */
    @GET
    @Streaming
    Call<ResponseBody> downloadBreakpoint(@Header("RANGE") String range, @Url String url);
}
