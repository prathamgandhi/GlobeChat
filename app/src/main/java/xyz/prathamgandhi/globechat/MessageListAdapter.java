package xyz.prathamgandhi.globechat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MessageListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private LayoutInflater inflater;
    private Context context;
    private ArrayList<JSONObject> items;
    private String userName;

    public MessageListAdapter(Context context, ArrayList<JSONObject> items, String userName) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.items = items;
        this.userName = userName;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType){
            case 1 : {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rec_msg, parent, false);
                return new ReceiveViewHolder(view);
            }
            case 2 : {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.send_msg, parent, false);
                return new SendViewHolder(view);
            }
            case 3 : {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.typing_msg, parent, false);
                return new TypingViewHolder(view);
            }
            default:
                throw new IllegalStateException("Unexpected value: " + viewType);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case 1: {
                ReceiveViewHolder receiveViewHolder = (ReceiveViewHolder) holder;
                try {
                    receiveViewHolder.receiverName.setText(items.get(position).getString("user"));
                    receiveViewHolder.receiverTime.setText(items.get(position).getString("time"));
                    receiveViewHolder.receiverMessage.setText(items.get(position).getString("text"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            }
            case 2 : {
                SendViewHolder sendViewHolder = (SendViewHolder) holder;
                try {
                    sendViewHolder.senderName.setText(items.get(position).getString("user"));
                    sendViewHolder.senderTime.setText(items.get(position).getString("time"));
                    sendViewHolder.senderMessage.setText(items.get(position).getString("text"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            }
            case 3 : {
                TypingViewHolder typingViewHolder = (TypingViewHolder) holder;
                try {
                    typingViewHolder.textView.setText(items.get(position).getString("text"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        JSONObject jsonObject = items.get(position);
        try {
            if (jsonObject.getString("user").equals("admin")) {
                return 3;
            }
            else if (jsonObject.getString("user").equals(userName)) {
                return 2;
            }
            else {
                return 1;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return -1;
    }
}

class ReceiveViewHolder extends RecyclerView.ViewHolder {
    TextView receiverName, receiverTime, receiverMessage;
    public ReceiveViewHolder(@NonNull View itemView) {
        super(itemView);
        receiverName = itemView.findViewById(R.id.receiverName);
        receiverTime = itemView.findViewById(R.id.receiverTime);
        receiverMessage = itemView.findViewById(R.id.receiverMessage);
    }


}

class SendViewHolder extends RecyclerView.ViewHolder {
    TextView senderName, senderTime, senderMessage;

    public SendViewHolder(@NonNull View itemView) {
        super(itemView);
        senderName = itemView.findViewById(R.id.senderName);
        senderTime = itemView.findViewById(R.id.senderTime);
        senderMessage = itemView.findViewById(R.id.senderMessage);
    }

}

class TypingViewHolder extends RecyclerView.ViewHolder {
    TextView textView;

    public TypingViewHolder(@NonNull View itemView) {
        super(itemView);
        textView = itemView.findViewById(R.id.typingmsgtext);
    }
}