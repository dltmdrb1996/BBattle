package com.example.BBATTLE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MatchingList extends AppCompatActivity {

    Button btn1, btn2, btn3, btnMatchingListRoomMakeMake, btnMatchingListRoomMakeCancel,
            btnMatchingListRoomMakeMan, btnMatchingListRoomMakeGirl,
            btnMatchingListRoomMakeTop, btnMatchingListRoomMakeMiddle,
            btnMatchingListRoomMakeBottom, matchingRoomSerchBtn;
    EditText etMatchingListRoomMakeTitle, etMatchingListRoomMakeWeight, matchingRoomSerchEdt;
    ArrayList<RecyclerItemData> items = new ArrayList<>();
    RecyclerView rView1;
    RecyclerAdapter rAdapter;
    Context context;
    Dialog roomMakeDialog, roomSearchDialog;
    String gender = null, grade = null, title, weight;
    CardView cvList;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance(); // 파이어베이스 데이터베이스 연동
    DatabaseReference databaseReference = firebaseDatabase.getReference("chat"); // DB 테이블 연결
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    Boolean master=true;
    int i = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matchinglist);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        btn1 = (Button) findViewById(R.id.btn1);
        btn2 = (Button) findViewById(R.id.btn2);
        btn3 = (Button) findViewById(R.id.btn3);
        cvList = (CardView) findViewById(R.id.cvList);

        // 방만들기 버튼
        btn1.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                roomMakeDialog = new Dialog(MatchingList.this);
                roomMakeDialog.setContentView(R.layout.activity_matchinglist_create_dialog);
                {
                    btnMatchingListRoomMakeMake = (Button) roomMakeDialog.findViewById(R.id.btnMatchingListRoomMakeMake);
                    btnMatchingListRoomMakeCancel = (Button) roomMakeDialog.findViewById(R.id.btnMatchingListRoomMakeCancle);
                    etMatchingListRoomMakeTitle = (EditText) roomMakeDialog.findViewById(R.id.etMatchingListRoomMakeTitle);
                    etMatchingListRoomMakeWeight = (EditText) roomMakeDialog.findViewById(R.id.etMatchingListRoomMakeWeight);
                    btnMatchingListRoomMakeMan = (Button) roomMakeDialog.findViewById(R.id.btnMatchingListRoomMakeMan);
                    btnMatchingListRoomMakeGirl = (Button) roomMakeDialog.findViewById(R.id.btnMatchingListRoomMakeGirl);
                    btnMatchingListRoomMakeTop = (Button) roomMakeDialog.findViewById(R.id.btnMatchingListRoomMakeTop);
                    btnMatchingListRoomMakeMiddle = (Button) roomMakeDialog.findViewById(R.id.btnMatchingListRoomMakeMiddle);
                    btnMatchingListRoomMakeBottom = (Button) roomMakeDialog.findViewById(R.id.btnMatchingListRoomMakeBottom);
                } // 다이럴로그 버튼 연결


                /// 방만들기 설정 선택
                roomMakeDialog.show();
                roomMakeDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                btnMatchingListRoomMakeMan.setOnClickListener(new OnSingleClickListener() {
                    @Override
                    public void onSingleClick(View v) {
                        gender = "남";
                        btnMatchingListRoomMakeMan.setSelected(true);
                        btnMatchingListRoomMakeGirl.setSelected(false);
                    }
                });
                btnMatchingListRoomMakeGirl.setOnClickListener(new OnSingleClickListener() {
                    @Override
                    public void onSingleClick(View v) {
                        gender = "여";
                        btnMatchingListRoomMakeMan.setSelected(false);
                        btnMatchingListRoomMakeGirl.setSelected(true);
                    }
                });
                btnMatchingListRoomMakeTop.setOnClickListener(new OnSingleClickListener() {
                    @Override
                    public void onSingleClick(View v) {
                        grade = "extream";
                        btnMatchingListRoomMakeTop.setSelected(true);
                        btnMatchingListRoomMakeMiddle.setSelected(false);
                        btnMatchingListRoomMakeBottom.setSelected(false);
                    }
                });
                btnMatchingListRoomMakeMiddle.setOnClickListener(new OnSingleClickListener() {
                    @Override
                    public void onSingleClick(View v) {
                        grade = "hard";
                        btnMatchingListRoomMakeTop.setSelected(false);
                        btnMatchingListRoomMakeMiddle.setSelected(true);
                        btnMatchingListRoomMakeBottom.setSelected(false);
                    }
                });
                btnMatchingListRoomMakeBottom.setOnClickListener(new OnSingleClickListener() {
                    @Override
                    public void onSingleClick(View v) {
                        grade = "normal";
                        btnMatchingListRoomMakeTop.setSelected(false);
                        btnMatchingListRoomMakeMiddle.setSelected(false);
                        btnMatchingListRoomMakeBottom.setSelected(true);
                    }
                });

                //방 만들기 버튼
                btnMatchingListRoomMakeMake.setOnClickListener(new OnSingleClickListener() {
                    @Override
                    public void onSingleClick(View v) {
                        title = etMatchingListRoomMakeTitle.getText().toString();
                        weight = etMatchingListRoomMakeWeight.getText().toString();
                        if (title.getBytes().length <= 0) {
                            Toast.makeText(getApplicationContext(), "제목을 입력해주세요", Toast.LENGTH_SHORT).show();
                        } else if (gender.getBytes().length <= 0) {
                            Toast.makeText(getApplicationContext(), "성별을 선택해주세요", Toast.LENGTH_SHORT).show();
                        } else if (weight.getBytes().length <= 0) {
                            Toast.makeText(getApplicationContext(), "몸무게를 입력해주세요", Toast.LENGTH_SHORT).show();
                        } else if (grade.getBytes().length <= 0) {
                            Toast.makeText(getApplicationContext(), "난이도를 선택해주세요", Toast.LENGTH_SHORT).show();
                        } else {
//                            items.add(i, new RecyclerItemData(String.valueOf(i + 1), title,
//                                    gender + "/" + weight +
//                                            "/" + grade));
//                            i++;
                            String memo = gender + "/" + weight + "/" + grade;
                            Intent intent = new Intent(MatchingList.this, MatchingRoom.class);
                            intent.putExtra("number", String.valueOf(i));
                            intent.putExtra("title", title);
                            intent.putExtra("memo", memo);
                            intent.putExtra("master", master);
                            intent.putExtra("grade", grade);
                            intent.putExtra("masteruid", user.getUid());
                            i=1;
                            startActivity(intent);
                            roomMakeDialog.dismiss();

                        }
                    }
                });
                btnMatchingListRoomMakeCancel.setOnClickListener(new OnSingleClickListener() {
                    @Override
                    public void onSingleClick(View v) {
                        roomMakeDialog.dismiss();
                    }
                });

            }
        });

        // 방 찾기 버튼
        btn2.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                roomSearchDialog = new Dialog(MatchingList.this);
                roomSearchDialog.setContentView(R.layout.activity_matchinglist_search_dialog);
                matchingRoomSerchBtn = (Button) roomSearchDialog.findViewById(R.id.matchingRoomSerchBtn);
                matchingRoomSerchEdt = (EditText) roomSearchDialog.findViewById(R.id.matchingRoomSerchEdt);
                roomSearchDialog.show();
                roomSearchDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                matchingRoomSerchBtn.setOnClickListener(new OnSingleClickListener() {
                    @Override
                    public void onSingleClick(View v) {
                        showCahtSerch();
                        Toast.makeText(getApplicationContext(), "검색 되었습니다.", Toast.LENGTH_SHORT).show();
                        roomSearchDialog.dismiss();
                    }
                });
            }
        });

        // 새로고침 버튼
        btn3.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                showChatList();
            }
        });
        //리싸이클러뷰 레이아웃 매니저를 통해 형태 설정
        rView1 = (RecyclerView) findViewById(R.id.rView1);
        rView1.setHasFixedSize(true); // 리사이클러뷰 기존성능 강화
        LinearLayoutManager layoutManager = new LinearLayoutManager(context,
                LinearLayoutManager.VERTICAL, false);
        rView1.setLayoutManager(layoutManager);
        showChatList();

    }


    private void showChatList() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // 파이어베이스 데이터베이스의 데이터를 받아오는 곳
                items.clear(); // 기존 배열리스트가 존재하지않게 초기화
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) { // 반복문으로 데이터 List를 추출해냄
                    RecyclerItemData roomList = snapshot.getValue(RecyclerItemData.class); // 만들어뒀던 RecyclerItemData 객체에 데이터를 담는다.
                    items.add(roomList); // 담은 데이터들을 배열리스트에 넣고 리사이클러뷰로 보낼 준비
                }
                rAdapter.notifyDataSetChanged(); // 리스트 저장 및 새로고침해야 반영이 됨
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // 디비를 가져오던중 에러 발생 시
                Log.e("Fraglike", String.valueOf(error.toException())); // 에러문 출력
            }
        });
        rAdapter = new RecyclerAdapter(items);
        rView1.setAdapter(rAdapter);
    }

    private void showCahtSerch() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // 파이어베이스 데이터베이스의 데이터를 받아오는 곳
                items.clear(); // 기존 배열리스트가 존재하지않게 초기화
                String str = matchingRoomSerchEdt.getText().toString();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) { // 반복문으로 데이터 List를 추출해냄
                    RecyclerItemData roomList = snapshot.getValue(RecyclerItemData.class); // 만들어뒀던 RecyclerItemData 객체에 데이터를 담는다.
                    if(roomList.getTitle().contains(str)){
                        items.add(roomList); // 담은 데이터들을 배열리스트에 넣고 리사이클러뷰로 보낼 준비
                    }
                }
                rAdapter.notifyDataSetChanged(); // 리스트 저장 및 새로고침해야 반영이 됨
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // 디비를 가져오던중 에러 발생 시
                Log.e("Fraglike", String.valueOf(error.toException())); // 에러문 출력
            }
        });

        rAdapter = new RecyclerAdapter(items);
        rView1.setAdapter(rAdapter);
    }
}