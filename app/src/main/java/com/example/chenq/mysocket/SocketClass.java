package com.example.chenq.mysocket;

import android.support.annotation.NonNull;
import android.util.Log;

import com.vilyever.socketclient.SocketClient;
import com.vilyever.socketclient.helper.SocketClientAddress;
import com.vilyever.socketclient.helper.SocketClientDelegate;
import com.vilyever.socketclient.helper.SocketClientReceivingDelegate;
import com.vilyever.socketclient.helper.SocketClientSendingDelegate;
import com.vilyever.socketclient.helper.SocketPacket;
import com.vilyever.socketclient.helper.SocketResponsePacket;

/**
 * Created by chenq on 2018/4/24.
 */

public class SocketClass  {

    SocketClient socketClient;
    SocketClientAddress  socketClientAddress;
    public SocketClass() {

        socketClientAddress = new SocketClientAddress("192.168.1.126",30000);
        socketClientAddress.setConnectionTimeout(1000*30);
        socketClient = new SocketClient(socketClientAddress);


        socketClient.registerSocketClientDelegate(new SocketClientDelegate() {
            /**
             * 连接上远程端时的回调
             */
            @Override
            public void onConnected(SocketClient client) {
               // SocketPacket packet = socketClient.sendData(new byte[]{0x02}); // 发送消息
              // packet = socketClient.sendString("sy hi!"); // 发送消息

                Log.i("chenqian","连接上远程端时的回调:"+client.getCharsetName());
             //   socketClient.cancelSend(packet); // 取消发送，若在等待发送队列中则从队列中移除，若正在发送则无法取消
            }

            /**
             * 与远程端断开连接时的回调
             */
            @Override
            public void onDisconnected(SocketClient client) {
                // 可在此实现自动重连

                Log.i("chenqian","与远程端断开连接时的回调:"+client.getCharsetName());

                //  socketClient.connect();
            }

            /**
             * 接收到数据包时的回调
             */
            @Override
            public void onResponse(final SocketClient client, @NonNull SocketResponsePacket responsePacket) {
                byte[] data = responsePacket.getData(); // 获取接收的byte数组，不为null
                String message = responsePacket.getMessage(); // 获取按默认设置的编码转化的String，可能为null
                Log.i("chenqian","接收到数据包时的回调:"+client.getCharsetName()+"  message:"+message);

            }
        });

        // 对应removeSocketClientSendingDelegate
        socketClient.registerSocketClientSendingDelegate(new SocketClientSendingDelegate() {
            /**
             * 数据包开始发送时的回调
             */
            @Override
            public void onSendPacketBegin(SocketClient client, SocketPacket packet) {

                Log.i("chenqian","数据包开始发送时的回调:"+client.getCharsetName());


            }

            /**
             * 数据包取消发送时的回调
             * 取消发送回调有以下情况：
             * 1. 手动cancel仍在排队，还未发送过的packet
             * 2. 断开连接时，正在发送的packet和所有在排队的packet都会被取消
             */
            @Override
            public void onSendPacketCancel(SocketClient client, SocketPacket packet) {

                Log.i("chenqian","数据包取消发送时的回调:"+client.getCharsetName());

            }

            /**
             * 数据包发送的进度回调
             * progress值为[0.0f, 1.0f]
             * 通常配合分段发送使用
             * 可用于显示文件等大数据的发送进度
             */
            @Override
            public void onSendingPacketInProgress(SocketClient client, SocketPacket packet, float progress, int sendedLength) {

                Log.i("chenqian","数据包发送的进度回调:"+client.getCharsetName()+"  progress:"+progress);

            }

            /**
             * 数据包完成发送时的回调
             */
            @Override
            public void onSendPacketEnd(SocketClient client, SocketPacket packet) {
                Log.i("chenqian","数据包完成发送时的回调:"+client.getCharsetName());

            }
        });

        // 对应removeSocketClientReceiveDelegate
        socketClient.registerSocketClientReceiveDelegate(new SocketClientReceivingDelegate() {
            /**
             * 开始接受一个新的数据包时的回调
             */
            @Override
            public void onReceivePacketBegin(SocketClient client, SocketResponsePacket packet) {
                Log.i("chenqian","开始接受一个新的数据包时的回调:"+client.getCharsetName());

            }

            /**
             * 完成接受一个新的数据包时的回调
             */
            @Override
            public void onReceivePacketEnd(SocketClient client, SocketResponsePacket packet) {
                Log.i("chenqian","完成接受一个新的数据包时的回调:"+client.getCharsetName());

            }

            /**
             * 取消接受一个新的数据包时的回调
             * 在断开连接时会触发
             */
            @Override
            public void onReceivePacketCancel(SocketClient client, SocketResponsePacket packet) {
                Log.i("chenqian","取消接受一个新的数据包时的回调:"+client.getCharsetName());

            }

            /**
             * 接受一个新的数据包的进度回调
             * progress值为[0.0f, 1.0f]
             * 仅作用于ReadStrategy为AutoReadByLength的自动读取
             * 因AutoReadByLength可以首先接受到剩下的数据包长度
             */
            @Override
            public void onReceivingPacketInProgress(SocketClient client, SocketResponsePacket packet, float progress, int receivedLength) {
                Log.i("chenqian","接受一个新的数据包的进度回调:"+client.getCharsetName()+"  progress:"+ progress);

            }
        });
        socketClient.connect();
    }

    public  void sendMesser(String str){

        if (socketClient!=null){

            socketClient.sendData(str.getBytes());
        }


    }

}
