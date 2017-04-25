package com.beini.net;


import com.beini.net.request.BaseRequestJson;
import com.beini.net.response.BaseResponseJson;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Created by beini on 2017/2/10.
 */

public interface ApiServer {
    //*************************************************get  method *******************************************************
    @GET("{url}")
    Call<ResponseBody> sendRequestGetNoP(@Path("url") String url);

    /**
     * 参数
     * Query 其实就是 Url 中 ‘?’ 后面的 key-value
     * QueryMap :多个参数
     */
    @GET("{url}")
    Call<String> sendRequestGetWithValue(@Path("url") String url, @Query("code") String page);

    /**
     * json
     */
    @GET("{url}")
    Call<BaseResponseJson> sendRequestGet(@Path("url") String url, @Body BaseRequestJson baseRequestJson);

//*************************************************post  method *******************************************************

    /**
     * 表单 提交
     */
    @FormUrlEncoded
    @POST("{url}")
    Call<String> postForm(@Path("url") String url,
                          @FieldMap Map<String, String> maps);

    /**
     * 对象
     */
    @POST("{url}")
    Call<String> PostBody(@Path("url") String url,
                          @Body Objects objects);

    /**
     * 多参数 @QueryMap
     */
    @PUT("{url}")
    Call<String> queryMap(@Path("url") String url,
            @QueryMap Map<String, String> maps);


    @POST("{url}")
    Call<String> verCode(@Path("url") String url, @Query("code") String code);

    /**
     * Field & FieldMap
     */
    @FormUrlEncoded
    @POST("/")
    Call<ResponseBody> sendRequestPost(
            @Field("name") String name,
            @Field("occupation") String occupation);

    @POST("{url}")
    Call<BaseResponseJson> sendRequestPost(@Path("url") String url, @Body Object baseRequestJson);

    //*************************************************上传 *******************************************************

    /**
     * http://blog.csdn.net/sk719887916/article/details/51755427
     *
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

    @POST("{url}")
        //多张
    Call<ResponseBody> uploadFileMultipartBody(@Path("url") String url, @Body MultipartBody multipartBody);

    //多张
    @Multipart
    @POST("{url}")
    Call<ResponseBody> uploadFilePart(@Path("url") String url, @Part() List<MultipartBody.Part> parts);
    /**
     * 断点续传
     * 可以做取消、继续下载的功能，或者从异常情况下恢复的再下载功能。
     */
    //*************************************************下载 *******************************************************

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
