package com.example.chatapp.ui.dashboard;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.Chat;
import com.example.chatapp.MyAdapter;
import com.example.chatapp.R;
import com.example.chatapp.User;
import com.example.chatapp.UserAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UsersFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;
    private static final String TAG = "UsersFragment";
    FirebaseDatabase database;

    private RecyclerView recyclerView;
    UserAdapter mAdapter; // 생성한 어댑터로 지정
    private RecyclerView.LayoutManager layoutManager;
    ArrayList<User> userArrayList;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        database = FirebaseDatabase.getInstance(); // db 초기화

        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_users, container, false);

        // 뷰모델 주석 처리
//        final TextView textView = root.findViewById(R.id.text_dashboard);
//        dashboardViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });

        SharedPreferences sharedPref = getActivity().getSharedPreferences("shared", Context.MODE_PRIVATE);
        String stEmail = sharedPref.getString("email",""); // 이메일 없을 땐 빈값 디폴트로 출력

        userArrayList = new ArrayList<>();
        recyclerView = (RecyclerView) root.findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)
        String[] myDataset = {"test1", "test2", "test3", "test4"};
        mAdapter = new UserAdapter(userArrayList, stEmail); // 어댑터 클래스도 구현 필요
        recyclerView.setAdapter(mAdapter);

        DatabaseReference ref = database.getReference("users");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, "onDataChange: "+dataSnapshot.getValue().toString());
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){ // getChildren 으로 Json 없이 key-Value 에서 value 만 받을 수 있음

                    Log.d(TAG, "dataSnapshot1: "+dataSnapshot1.getValue().toString());
                    User user = dataSnapshot1.getValue(User.class);
                    Log.d(TAG, "user: "+user.getEmail());
                    userArrayList.add(user);
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return root;
    }
}