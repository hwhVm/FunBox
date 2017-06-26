package com.beini.net;

import com.beini.utils.BLog;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

/**
 * Created by beini on 2017/6/26.
 * 1 连接失败重连
 * 2 掉线重连
 */

public class OkhttpWebSocketUtil {
    private final OkHttpClient okHttpClient;
    private String address;
    public   WebSocket  webSocket;

    public OkhttpWebSocketUtil() {
        okHttpClient = new OkHttpClient.Builder()
                .readTimeout(3000, TimeUnit.SECONDS)//设置读取超时时间
                .writeTimeout(3000, TimeUnit.SECONDS)//设置写的超时时间
                .connectTimeout(3000, TimeUnit.SECONDS)//设置连接超时时间
                .pingInterval(10, TimeUnit.SECONDS) // websocket 轮训间隔
                .build();
        String ip = "10.0.0.35";
        String port = "8080/webSocketServer";
        address = String.format("ws://%s:%s", ip, port);
        BLog.d("  address==" + address);
    }

    public void webSocketHandler() {
        Request request = new Request.Builder().url(address).build();
        webSocket = okHttpClient.newWebSocket(request, new WebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                super.onOpen(webSocket, response);
                BLog.d("    WebSocket    onOpen  ");
            }

            @Override
            public void onMessage(WebSocket webSocket, String text) {//使用EventBus进行广播
                super.onMessage(webSocket, text);
                BLog.d("    WebSocket    onMessage  text="+text);
            }

            @Override
            public void onMessage(WebSocket webSocket, ByteString bytes) {///除了文本内容外，还可以将如图像，声音，视频等内容转为ByteString发送
                super.onMessage(webSocket, bytes);
                BLog.d("    WebSocket    onMessage  bytes="+bytes.toString());
            }

            @Override
            public void onClosing(WebSocket webSocket, int code, String reason) {
                super.onClosing(webSocket, code, reason);
                BLog.d("    WebSocket    onClosing  reason="+reason);
            }

            @Override
            public void onClosed(WebSocket webSocket, int code, String reason) {
                super.onClosed(webSocket, code, reason);
                BLog.d("    WebSocket    onClosed  reason="+reason);
            }

            @Override
            public void onFailure(WebSocket webSocket, Throwable t, Response response) {
                super.onFailure(webSocket, t, response);
                BLog.d("    WebSocket    onFailure  "+t.getLocalizedMessage());
            }
        });
    }
}