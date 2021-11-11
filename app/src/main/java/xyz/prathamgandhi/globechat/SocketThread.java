package xyz.prathamgandhi.globechat;

import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.util.ArrayList;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class SocketThread extends Thread {
    public Handler handler;
    Handler uiHandler = new Handler(Looper.getMainLooper());
    Socket socket;
    String userName;
    MessageListAdapter adapter;
    RecyclerView recyclerView;
    ArrayList<JSONObject> items;
    ArrayList<String> userNames;
    TextView userState;
 //   ActiveUserAdapter activeUserAdapter;
    SocketThread(String userName, MessageListAdapter adapter, ArrayList<JSONObject> items, RecyclerView recyclerView, TextView userState) {
        this.userName = userName;
        this.adapter = adapter;
        this.items = items;
        this.recyclerView = recyclerView;
        this.userState = userState;
    }
    Socket getSocket() {
        return socket;
    }
    JSONObject msg;
    JSONArray allUsers;
    @Override
    public void run() {
        URI uri = URI.create("https://global-chat001.herokuapp.com/");
//        URI uri = URI.create("http://192.168.29.140:5000");
        socket = IO.socket(uri);
        socket.connect();

        socket.on("message", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                msg = (JSONObject) args[0];
                uiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if(!msg.getString("user").equals("typing")) {
                                items.add(msg);
                                adapter.notifyItemInserted(items.size());
                                recyclerView.smoothScrollToPosition(items.size());
                                userState.setText(userName);
                            }
                            else if(msg.getString("user").equals("typing")){
                                userState.setText(msg.getString("text"));
                            }
                            else {
                                userState.setText(userName);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

/*        socket.on("allUsers", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                try {
                    allUsers = ((JSONObject) args[0]).getJSONArray("users");
                    userNames.clear();
                    for(int i = 0; i < allUsers.length(); i++) {
                        userNames.add(allUsers.getJSONObject(i).getString("name"));
                    }
                    uiHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyItemInserted(items.size());
                        }
                    });
                }
               catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

 */
        JSONObject jname = new JSONObject();
        try {
            jname.put("name", userName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        socket.emit("join", jname);
        socket.emit("askUsers");

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

allUsers : {"users":[{"id":"cEHjYpZFjaHBqDkqAAAH","name":"sdf"},{"id":"tETzXtGnQChWq7asAABD","name":"Fjf"}]}
 */