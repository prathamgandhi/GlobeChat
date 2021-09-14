package xyz.prathamgandhi.globechat;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class NamePage extends AppCompatActivity {
    private JSONArray jarray;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name_page);
        EditText personName = findViewById(R.id.personName);
        Button submitButton = findViewById(R.id.submitName);
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        URI uri = URI.create("http://192.168.29.140:5000");
        Socket socket = IO.socket(uri);
        socket.connect();
        socket.emit("askUsers");
        socket.on("allUsers", args -> {
            try {
                jarray = ((JSONObject) args[0]).getJSONArray("users");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
        submitButton.setOnClickListener(v -> {
            if (!socket.connected()) {
                Toast.makeText(NamePage.this, "Please wait a second", Toast.LENGTH_SHORT).show();
            }
            else {
                socket.emit("askUsers");
                boolean flag = false;
                String userName = personName.getText().toString().trim();
                if(userName.length() == 0) {
                    Toast.makeText(NamePage.this, "Please Enter a name to continue", Toast.LENGTH_SHORT).show();
                    flag = true;
                }
                else if(userName.length() > 20){
                    Toast.makeText(NamePage.this, "Username too long!!", Toast.LENGTH_SHORT).show();
                    flag = true;
                }
                else {
                    for(int i = 0; i < jarray.length(); i++) {
                        try {
                            if (userName.equals(jarray.getJSONObject(i).getString("name"))) {
                                Toast.makeText(NamePage.this, "That name has already been taken", Toast.LENGTH_SHORT).show();
                                flag = true;
                                break;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
                if(!flag){
                    socket.disconnect();
                    Intent intent = new Intent(NamePage.this, MainActivity.class);
                    NamePage.this.startActivity(intent);
                    NamePage.this.finish();
                }
            }
        });
    }
}
