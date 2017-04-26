package com.beini.net;

import android.os.Environment;

import com.beini.base.BaseApplication;
import com.beini.constants.NetConstants;
import com.beini.net.request.BaseRequestJson;
import com.beini.net.response.BaseResponseJson;
import com.beini.utils.BLog;
import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by beini on 2017/2/10.
 * http://www.jianshu.com/p/16994e49e2f6
 * 是否加入统一管理所有请求
 */

public class NetUtil {
    private static NetUtil instance;
    private static Retrofit retrofit;
    private static ApiServer apiServer;
    private static int DEFAULT_TIMEOUT = 5;

    public static NetUtil getSingleton() {
        if (instance == null) {
            synchronized (NetUtil.class) {
                if (instance == null) {
                    instance = new NetUtil();
                    ClearableCookieJar cookieJar =
                            new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(BaseApplication.getInstance()));
                    OkHttpClient client = new OkHttpClient//添加头信息，cookie等
                            .Builder().connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
//                            .addNetworkInterceptor(new Interceptor() {
//                                @Override
//                                public okhttp3.Response intercept(Chain chain) throws IOException {
//                                    return null;
//                                }
//                            })
                            .cookieJar(cookieJar)
                            .cache(new Cache(new File(Environment.getExternalStorageDirectory() + "/tmp"), 10 * 1024 * 1024))   //设置缓存目录和10M缓存
                            .build();
                    retrofit = new Retrofit.Builder()
                            .client(client)
                            .baseUrl(NetConstants.baseUrl)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    apiServer = retrofit.create(ApiServer.class);
                }
            }
        }
        return instance;
    }

    /**
     * 参数
     */
    public Call<ResponseBody> getMethod(String url) {
        return apiServer.sendRequestGetNoP(url);
    }

    public Call<String> verCode(String url, String code) {
        return apiServer.verCode(url, code);
    }

    public Call<String> verCodePost(String url, String code) {

        return apiServer.sendRequestGetWithValue(url, code);
    }

    /**
     * json
     */
    public Call<BaseResponseJson> sendRequestGet(String url, BaseRequestJson baseRequest) {
        return apiServer.sendRequestGet(url, baseRequest);
    }

    public Call<BaseResponseJson> sendRequestPost(String url, Object baseRequest) {
        return apiServer.sendRequestPost(url, baseRequest);
    }

    /**
     * 带参数
     */
    public Call<String> sendPostWithParm(String url, Map<String, String> maps) {

        return apiServer.postForm(url, maps);
    }
//***********************************************************上传**********************************************************

    /**
     * 上传 单张
     *
     * @param file
     * @return
     */
    public Call<ResponseBody> uploadFileSingle(File file) {
        // create RequestBody instance from file
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);

        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("file", file.getName(), requestFile);

        // add another part within the multipart request
//        String descriptionString = "hello, this is description speaking";
//        RequestBody description =
//                RequestBody.create(
//                        MediaType.parse("multipart/form-data"), descriptionString);

        // finally, execute the request

        return apiServer.uploadFile("upload", body);
    }

    /**
     * 上传 多张不确定数量   MultipartBody
     *
     * @param fileList
     * @return
     */
    public Call<ResponseBody> uploadFileMultiPart(List<File> fileList) {//多张不确定数量
        MultipartBody.Builder builder = new MultipartBody.Builder();

        for (File file : fileList) {
            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            builder.addFormDataPart("file", file.getName(), requestBody);
        }
        builder.setType(MultipartBody.FORM);
        MultipartBody multipartBody = builder.build();

        return apiServer.uploadFileMultipartBody("multipart_upload", multipartBody);
    }

    /**
     * 上传 多张不确定数量    List<MultipartBody.Part>
     *
     * @param fileList
     * @return
     */
    public Call<ResponseBody> uploadFilePart(List<File> fileList) {

        List<MultipartBody.Part> parts = new ArrayList<>(fileList.size());
        for (File file : fileList) {
            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part part = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
            parts.add(part);
        }
        return apiServer.uploadFilePart("multipart_upload", parts);
    }
    /**
     * 断点续传
     * 1、记录断点位置 2、缓存保存 3、服务器支持。
     */
//***********************************************************下载**********************************************************

    /**
     * 文件下载
     *
     * @param fileUrl
     * @return
     */
    public Call<ResponseBody> downloadFile(String fileUrl) {

        return apiServer.downloadFile(fileUrl);
    }

    /**
     * 断点下载
     */
    public Call<ResponseBody> downLoadFile(String rang, String fileUrl) {
        String rangStr = "bytes=" + rang + "-";
        BLog.d("        rangStr= " + rangStr);
        return apiServer.downloadBreakpoint(rangStr, fileUrl);
    }

}
