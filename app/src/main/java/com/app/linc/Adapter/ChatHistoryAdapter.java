package com.app.linc.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.app.linc.Fragment.ChatFragment;

import com.app.linc.Model.Chat.ChatHistory.ChatHistory;
import com.app.linc.Model.Chat.ChatHistory.Chat_;
import com.app.linc.R;

import java.util.List;

public class ChatHistoryAdapter extends RecyclerView.Adapter<ChatHistoryAdapter.TasksViewHolder> {

    private Context mContext;
  
    private List<ChatHistory>chatHistoryList;
    ChatFragment chatFragment;


    public ChatHistoryAdapter(Context mContext, List<ChatHistory> chatHistoryList) {
        this.mContext = mContext;
        this.chatHistoryList = chatHistoryList;
        this.chatFragment=chatFragment;
    }

    @Override
    public ChatHistoryAdapter.TasksViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.chat_history_adapter_layout, parent, false);

        return new ChatHistoryAdapter.TasksViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ChatHistoryAdapter.TasksViewHolder holder, final int position) {
        Log.v("chatHistoryList", "" + chatHistoryList.size());
        final ChatHistory chatHistoryModel = chatHistoryList.get(position);
        Chat_ chatModel=new Chat_();


        String name = chatHistoryModel.getName();
        String userType=chatHistoryModel.getUserType();

        chatModel=chatHistoryModel.getChat();
        String msg=chatModel.getMessage();


        if (name != null && !name.isEmpty() && !name.equals("null")) {

           if(!name.equals("you"))
           {
               holder.txtUserName.setText(name);
           }

        }

        if (msg != null && !msg.isEmpty() && !msg.equals("null")) {

            if(name.equals("you"))
            {
                holder.txtTo.setText(msg);
                holder.linearFrom.setVisibility(View.GONE);
                holder.linearTo.setVisibility(View.VISIBLE);

            }
            else
            {
                holder.linearFrom.setVisibility(View.VISIBLE);
                holder.linearTo.setVisibility(View.GONE);
                if(userType.equals("student"))
                {
                    holder.txtUserName.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
                }
                else if(userType.equals("parent"))
                {
                    holder.txtUserName.setTextColor(mContext.getResources().getColor(R.color.colorOrange));
                }
                else if(userType.equals("staff"))
                {
                    holder.txtUserName.setTextColor(mContext.getResources().getColor(R.color.colorgray));
                }
                holder.txtFrom.setText(msg);

            }
        }


    }

    @Override
    public int getItemCount() {
        return chatHistoryList.size();
    }

    class TasksViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        TextView txtUserName,txtFrom,txtTo;
        LinearLayout lineaerClick,linearTo,linearFrom,linearFromMain;


        public TasksViewHolder(View itemView) {
            super(itemView);


            txtUserName = itemView.findViewById(R.id.txtUserName);
            txtFrom= itemView.findViewById(R.id.txtFrom);
            txtTo= itemView.findViewById(R.id.txtTo);
            lineaerClick = itemView.findViewById(R.id.lineaerClick);
            linearTo= itemView.findViewById(R.id.linearTo);
            linearFrom= itemView.findViewById(R.id.linearFrom);
            linearFromMain= itemView.findViewById(R.id.linearFromMain);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

        }
    }

}