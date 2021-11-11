package xyz.prathamgandhi.globechat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import io.socket.client.Ack;


public class MainActivity extends AppCompatActivity {

    ArrayList<JSONObject> msgs = new ArrayList<>();
    ArrayList<String> userNames = new ArrayList<>();
    SocketThread socketThread;
//    ActiveUserAdapter activeUserAdapter = new ActiveUserAdapter(this, userNames);
    public String userName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            userName = extras.getString("userName");
        }
        TextView userState = findViewById(R.id.userState);
        userState.setText(userName);
        RecyclerView recyclerView = findViewById(R.id.recycler);
        MessageListAdapter adapter = new MessageListAdapter(this, msgs, userName);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        socketThread = new SocketThread(userName, adapter, msgs, recyclerView, userState);
        socketThread.start();

        ImageButton sendBtn = findViewById(R.id.sendBtn);
        EditText sendMsg = findViewById(R.id.sendMsg);
        sendBtn.setOnClickListener(v -> {
            String sendMsgText = sendMsg.getText().toString();
            if(sendMsgText.length() != 0) {
                socketThread.handler.post(() -> socketThread.getSocket().emit("sendMessage", sendMsg.getText().toString(), new Ack() {
                    @Override
                    public void call(Object... args) {
                        runOnUiThread(() -> sendMsg.setText(""));
                    }
                }));
            }
        });

        sendMsg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                socketThread.handler.post(() -> socketThread.getSocket().emit("typing"));
            }

            @Override
            public void afterTextChanged(Editable s) { } }); }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.customize) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Customize");
            String[] options = getResources().getStringArray(R.array.customize_options);
            builder.setItems(options, (dialog, option) -> {
                   switch(option) {
                       case 0: {
                           ColorPickerDialogBuilder
                                   .with(MainActivity.this)
                                   .setTitle("Choose color")
                                   .initialColor(R.color.background)
                                   .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                                   .density(12)
                                   .setOnColorSelectedListener(new OnColorSelectedListener() {
                                       @Override
                                       public void onColorSelected(int selectedColor) {
                                       }
                                   })
                                   .setPositiveButton("ok", new ColorPickerClickListener() {
                                       @Override
                                       public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                                           View mainConstraintLayout = findViewById(R.id.mainConstraintLayout);
                                           mainConstraintLayout.setBackgroundColor(selectedColor);
                                       }
                                   })
                                   .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                                       @Override
                                       public void onClick(DialogInterface dialog, int which) {
                                       }
                                   })
                                   .build()
                                   .show();
                           break;
                       }
                       case 1: {
                           ColorPickerDialogBuilder
                                   .with(MainActivity.this)
                                   .setTitle("Choose color")
                                   .initialColor(R.color.background)
                                   .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                                   .density(12)
                                   .setOnColorSelectedListener(new OnColorSelectedListener() {
                                       @Override
                                       public void onColorSelected(int selectedColor) {
                                       }
                                   })
                                   .setPositiveButton("ok", new ColorPickerClickListener() {
                                       @Override
                                       public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                                           Objects.requireNonNull(getSupportActionBar()).setBackgroundDrawable(new ColorDrawable(selectedColor));
                                       }
                                   })
                                   .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                                       @Override
                                       public void onClick(DialogInterface dialog, int which) {
                                       }
                                   })
                                   .build()
                                   .show();
                           break;
                       }

                   }
            });
            builder.create().show();
        }
        else if(id == R.id.meet) {
            Uri webpage = Uri.parse("https://www.linkedin.com/in/pratham-gandhi-167a63218/");
            Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
            try {
                startActivity(intent);
            }
            catch (ActivityNotFoundException e) {
                Toast.makeText(this, "Some error occurred, can't open browser :(", Toast.LENGTH_SHORT).show();
            }
        }
        else if(id == R.id.active_user){
/*            socketThread.handler.post(() -> socketThread.socket.emit("askUsers"));

            LayoutInflater layoutInflater = getLayoutInflater();
            View customView = layoutInflater.inflate(R.layout.active_user_popup, null);

            RecyclerView rv = findViewById(R.id.active_user_rv);
            rv.setAdapter(activeUserAdapter);
            rv.setLayoutManager(new LinearLayoutManager(this));

            ConstraintLayout mainConstraintLayout = findViewById(R.id.mainConstraintLayout);
            PopupWindow popupWindow = new PopupWindow(customView, RecyclerView.LayoutParams.WRAP_CONTENT, RecyclerView.LayoutParams.WRAP_CONTENT);

            popupWindow.showAtLocation(mainConstraintLayout, Gravity.CENTER, 0, 0);
*/


        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        socketThread.handler.post(() -> socketThread.getSocket().disconnect());
        SystemClock.sleep(500);
        Looper looper = socketThread.handler.getLooper();
        looper.quit();
        super.onDestroy();
    }
}
