package com.irfan.draft1.Inbox;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.irfan.draft1.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by irfan on 26/01/2018.
 */

public class ChatTabFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<ChatRoomModel> chatList = new ArrayList<>();
    private ChatRoomAdapter adapter;

    private FloatingActionButton fab;

    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private FirebaseUser user;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chat_tab_fragment, container, false);

        recyclerView = view.findViewById(R.id.chatRooms);
        adapter = new ChatRoomAdapter(getActivity(), chatList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        fab = view.findViewById(R.id.addChatRoom);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                db.collection()
//            }
//        });
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        chatList = getChatListFromFirebase();

    }

    private List<ChatRoomModel> getChatListFromFirebase() {
        final List<ChatRoomModel> chatRooms = new ArrayList<>();
        db.collection("UserDatabase").document(user.getUid()).collection("ChatRoomsID").get().addOnCompleteListener(getActivity(), new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot doc : task.getResult()) {
                        ChatRoomModel temp = doc.toObject(ChatRoomModel.class);
                        chatRooms.add(temp);
                    }

                }
            }
        });


        return chatRooms;
    }


}
