package com.irfan.draft1.Inbox;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.irfan.draft1.R;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by irfan on 27/01/2018.
 */

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatHolder> {

    private Context context;
    private List<ChatModel> chatList;
    private ChatModel chat;

    private SimpleDateFormat sdf = new SimpleDateFormat("d MMM yyyy");


    public ChatAdapter(Context context, List<ChatModel> chatList) {
        this.context = context;
        this.chatList = chatList;


    }


    @Override
    public ChatAdapter.ChatHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_sender_interface, parent, false);
        return new ChatAdapter.ChatHolder(view);
    }

    @Override
    public void onBindViewHolder(final ChatAdapter.ChatHolder holder, int position) {
        chat = chatList.get(position);
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class ChatHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        public ChatHolder(View itemView) {
            super(itemView);


        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
            }

        }

    }

}
