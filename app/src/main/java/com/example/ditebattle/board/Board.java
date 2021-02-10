package com.example.ditebattle.board;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ditebattle.OnSingleClickListener;
import com.example.ditebattle.R;
import com.example.ditebattle.database.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class Board extends AppCompatActivity {

    Button btnBoardWrite;
    ImageView ivBoardExit, boardsearchicon;
    EditText etBoardTitle, etBoardMemo, boardsearch;
    TextView tvBoardCreate;

    RecyclerView view;
    BoardRecyclerAdapter adapter;
    ArrayList<BoardRecyclerItem> items = new ArrayList<>();
    Context context;
    Dialog boardWriteDialog;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    String title, memo, nickname, dates;
    int cYear, cMonth, cDay;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);
        ActionBar bar = getSupportActionBar();
        bar.hide();

        btnBoardWrite = (Button)findViewById(R.id.btnBoardWrite);
        boardsearchicon = (ImageView)findViewById(R.id.boardsearchicon);
        boardsearch = (EditText)findViewById(R.id.boardsearch);

        view = (RecyclerView) findViewById(R.id.view);
        view.setHasFixedSize(true); // 리사이클러뷰 기존성능 강화
        LinearLayoutManager layoutManager = new LinearLayoutManager(context,
                LinearLayoutManager.VERTICAL, false);
        view.setLayoutManager(layoutManager);

        showChatList();

        // 작성 버튼 클릭
        btnBoardWrite.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                boardWriteDialog = new Dialog(Board.this);
                boardWriteDialog.setContentView(R.layout.activity_board_write_dialog);

                ivBoardExit = (ImageView) boardWriteDialog.findViewById(R.id.ivBoardExit);
                etBoardTitle = (EditText) boardWriteDialog.findViewById(R.id.etBoardTitle);
                etBoardMemo = (EditText) boardWriteDialog.findViewById(R.id.etBoardMemo);
                tvBoardCreate = (TextView) boardWriteDialog.findViewById(R.id.tvBoardCreate);

                boardWriteDialog.show();
                boardWriteDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User get = snapshot.child("User").child(user.getUid()).getValue(User.class);
                        nickname = get.nickname;
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                ivBoardExit.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        switch (motionEvent.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                ivBoardExit.setImageResource(R.drawable.exit2);
                                break;
                            case MotionEvent.ACTION_CANCEL:
                                ivBoardExit.setImageResource(R.drawable.exit);
                                break;
                            case MotionEvent.ACTION_UP:
                                ivBoardExit.setImageResource(R.drawable.exit);
                                boardWriteDialog.dismiss();
                                break;
                        }
                        return true;
                    }
                });

                tvBoardCreate.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        switch (motionEvent.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                tvBoardCreate.setBackgroundDrawable(ContextCompat.getDrawable(Board.this,R.drawable.edgeborderbtn));
                                break;
                            case MotionEvent.ACTION_CANCEL:
                                tvBoardCreate.setBackgroundDrawable(ContextCompat.getDrawable(Board.this,R.drawable.edgeborderbtn3));
                                break;
                            case MotionEvent.ACTION_UP:
                                tvBoardCreate.setBackgroundDrawable(ContextCompat.getDrawable(Board.this,R.drawable.edgeborderbtn3));
                                Calendar cal = Calendar.getInstance(); // 핸드폰의 날짜와 시간을 가져와 시간을 넣어준다.
                                cYear = cal.get(Calendar.YEAR);
                                cMonth = cal.get(Calendar.MONTH);
                                cDay = cal.get(Calendar.DAY_OF_MONTH); // 그 달의 일수

                                dates = cYear + "." + (cMonth + 1) + "." + cDay;

                                title = etBoardTitle.getText().toString();
                                memo = etBoardMemo.getText().toString();

                                BoardRecyclerItem boardRecyclerItem = new BoardRecyclerItem(nickname, title, dates, memo);

                                databaseReference.child("Board").child(title).setValue(boardRecyclerItem);
                                showChatList();
                                showToast("작성 되었습니다.");
                                boardWriteDialog.dismiss();
                                break;
                        }
                        return true;
                    }
                });
            }
        });

        //검색 버튼 클릭
        boardsearchicon.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        boardsearchicon.setBackgroundColor(Color.parseColor("#99ffffff"));
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        boardsearchicon.setBackgroundColor(0);
                        break;
                    case MotionEvent.ACTION_UP:
                        boardsearchicon.setBackgroundColor(0);
                        showCahtSerch();
                        showToast("검색하였습니다.");
                        break;
                }
                return true;
            }
        });

    }

    private void showChatList() {

        databaseReference.child("Board").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // 파이어베이스 데이터베이스의 데이터를 받아오는 곳
                items.clear(); // 기존 배열리스트가 존재하지않게 초기화
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) { // 반복문으로 데이터 List를 추출해냄
                    BoardRecyclerItem roomList = snapshot.getValue(BoardRecyclerItem.class); // 만들어뒀던 RecyclerItemData 객체에 데이터를 담는다.
                    items.add(roomList); // 담은 데이터들을 배열리스트에 넣고 리사이클러뷰로 보낼 준비
                }
                adapter.notifyDataSetChanged(); // 리스트 저장 및 새로고침해야 반영이 됨
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // 디비를 가져오던중 에러 발생 시
                Log.e("Fraglike", String.valueOf(error.toException())); // 에러문 출력
            }
        });

        adapter = new BoardRecyclerAdapter(items);
        view.setAdapter(adapter);
    }

    private void showCahtSerch() {
        databaseReference.child("Board").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // 파이어베이스 데이터베이스의 데이터를 받아오는 곳
                items.clear(); // 기존 배열리스트가 존재하지않게 초기화
                String str = boardsearch.getText().toString();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) { // 반복문으로 데이터 List를 추출해냄
                    BoardRecyclerItem roomList = snapshot.getValue(BoardRecyclerItem.class); // 만들어뒀던 RecyclerItemData 객체에 데이터를 담는다.
                    if(roomList.getTitle().contains(str)){
                        items.add(roomList); // 담은 데이터들을 배열리스트에 넣고 리사이클러뷰로 보낼 준비
                    }
                }
                adapter.notifyDataSetChanged(); // 리스트 저장 및 새로고침해야 반영이 됨
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // 디비를 가져오던중 에러 발생 시
                Log.e("Fraglike", String.valueOf(error.toException())); // 에러문 출력
            }
        });

        adapter = new BoardRecyclerAdapter(items);
        view.setAdapter(adapter);
    }
    void showToast(String msg){
        Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
    }
}
