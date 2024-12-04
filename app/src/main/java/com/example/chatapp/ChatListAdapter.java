package com.example.chatapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ViewHolder> {
    private Context context;
    private ArrayList<ChatRoom> chatRooms;
    private OnChatRoomClickListener listener;

    public interface OnChatRoomClickListener {
        void onChatRoomClicked(ChatRoom chatRoom);
    }

    public ChatListAdapter(Context context, ArrayList<ChatRoom> chatRooms, OnChatRoomClickListener listener) {
        this.context = context;
        this.chatRooms = chatRooms;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_chat_room, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ChatRoom chatRoom = chatRooms.get(position);
        holder.roomName.setText(chatRoom.getName());
        holder.itemView.setOnClickListener(view -> listener.onChatRoomClicked(chatRoom));
    }

    @Override
    public int getItemCount() {
        return chatRooms.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView roomName;

        ViewHolder(View itemView) {
            super(itemView);
            roomName = itemView.findViewById(R.id.roomName);
        }
    }
}