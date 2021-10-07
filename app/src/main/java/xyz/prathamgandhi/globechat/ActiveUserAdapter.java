/*package xyz.prathamgandhi.globechat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ActiveUserAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    Context context;
    ArrayList<String> users;
    public ActiveUserAdapter(Context context, ArrayList<String> users) {
        this.context = context;
        this.users = users;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.acitive_user_txt,
                parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        UserViewHolder userViewHolder = (UserViewHolder) holder;
        userViewHolder.user.setText(users.get(position));
    }

    @Override
    public int getItemCount() {
        return users.size();
    }
}

class UserViewHolder extends RecyclerView.ViewHolder {
    TextView user;
    public UserViewHolder(@NonNull View itemView) {
        super(itemView);
        user = itemView.findViewById(R.id.user);
    }
}
*/
//allUsers : {"users":[{"id":"cEHjYpZFjaHBqDkqAAAH","name":"sdf"},{"id":"tETzXtGnQChWq7asAABD","name":"Fjf"}]}
