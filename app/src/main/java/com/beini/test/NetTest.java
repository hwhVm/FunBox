package com.beini.test;

import android.os.Environment;

import com.beini.net.retrofit.NetUtil;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by beini on 2017/3/30.
 */

public class NetTest {

    public static void main(String[] args) {
        NetUtil.getSingleton().downloadFile("http://120.76.41.61/source/sound/sleep/Sleep_Bird_Chirping.mp3").enqueue(new Callback<ResponseBody>() {


            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                System.out.println(" onResponse=  ");
                try {
                    byte[] data = response.body().bytes();
                    RandomAccessFile fos = new RandomAccessFile(Environment.getExternalStorageDirectory() + "/mm.mp3", "rw");
                    FileChannel fileChannel = fos.getChannel();
                    MappedByteBuffer buffer = fileChannel.map(FileChannel.MapMode.READ_WRITE, 0, data.length);
                    buffer.put(data);
                    fileChannel.close();
                    fos.close();
                } catch (IOException e) {
                    System.out.println(" "+e.getLocalizedMessage());
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                System.out.println(" onFailure=  "+t.getLocalizedMessage());
            }
        });
    }
}
