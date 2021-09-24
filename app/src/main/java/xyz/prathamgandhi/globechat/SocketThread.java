package xyz.prathamgandhi.globechat;


import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.util.ArrayList;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class SocketThread extends Thread {
    public static Handler handler;
    Handler uiHandler = new Handler(Looper.getMainLooper());
    Socket socket;
    String userName;
    MessageListAdapter adapter;
    RecyclerView recyclerView;
    ArrayList<JSONObject> items;
    SocketThread(String userName, MessageListAdapter adapter, ArrayList<JSONObject> items, RecyclerView recyclerView) {
        this.userName = userName;
        this.adapter = adapter;
        this.items = items;
        this.recyclerView = recyclerView;
    }
    Socket getSocket() {
        return socket;
    }
    JSONObject msg, allUsers;
    @Override
    public void run() {
        URI uri = URI.create("http://192.168.29.140:5000");
        socket = IO.socket(uri);
        socket.connect();

        socket.on("message", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                msg = (JSONObject) args[0];
                items.add(msg);
                uiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyItemInserted(items.size());
                        recyclerView.smoothScrollToPosition(items.size());
                    }
                });
            }
        });

        socket.on("allUsers", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                allUsers = (JSONObject) args[0];
            }
        });
        JSONObject jname = new JSONObject();
        try {
            jname.put("name", userName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        socket.emit("join", jname);

        Looper.prepare();

        handler = new Handler();

        Looper.loop();

    }
}
/*
I/System.out: {"user":"admin","text":"adsf has joined the chat","users":[{"id":"rcrICCvyj0p3TVHDAAAH","name":"adsf"}]}
I/System.out: {"user":"typing","text":"adsf is typing ..."}
I/System.out: {"user":"typing","text":"adsf is typing ..."}
I/System.out: {"user":"typing","text":"adsf is typing ..."}
I/System.out: {"user":"typing","text":"adsf is typing ..."}
I/System.out: {"user":"typing","text":"adsf is typing ..."}
I/System.out: {"user":"typing","text":"adsf is typing ..."}
I/System.out: {"user":"typing","text":"adsf is typing ..."}
I/System.out: {"user":"adsf","text":"kjfkjsf","time":"4:16 pm"}
 */