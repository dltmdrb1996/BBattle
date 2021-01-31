package com.example.ditebattle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ditebattle.database.Battle;
import com.example.ditebattle.database.GuestInfo;
import com.example.ditebattle.database.MasterInfo;
import com.example.ditebattle.database.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MatchingRoom extends AppCompatActivity {

    Button matchingRoomStartBtn, matchingRoomChatBtn, matchingRoomOtherInfo, dialog_info_ban_btn;
    EditText matchingRoomChatEdt;
    TextView matchingRoomNum, matchingRoomTitle, matchingRoomOption, tvNickname, tvLevel, tvHeight, tvWeight, tvBmi;
    ListView matchingRoomChatList;
    ImageView ivExit, matchingRoomOtherImg;
    Dialog infoDialog;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    DatabaseReference battleRef = databaseReference.child("Battle");
    HashMap<String, Object> childUpdates = new HashMap<>();
    Map<String, Object> battleValue = null;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String number, title, memo, masterUid, guestUid, grade, matchingMasterUID, matchingGuestUID, masterUID, guestUID, nickname;
    Boolean master, Login = true;
    Boolean flag = false, onBattle = false, guestOut=false, masterOut=false;
    Battle battle;
    ValueEventListener battleValueEventListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matching_room);
        matchingRoomOtherImg = (ImageView)findViewById(R.id.matchingRoomOtherImg);
        matchingRoomStartBtn = (Button) findViewById(R.id.matchingRoomStartBtn);
        matchingRoomChatBtn = (Button) findViewById(R.id.matchingRoomChatBtn);
        matchingRoomOtherInfo = (Button) findViewById(R.id.matchingRoomOtherInfo);
        matchingRoomChatEdt = (EditText) findViewById(R.id.matchingRoomChatEdt);
        matchingRoomNum = (TextView) findViewById(R.id.matchingRoomNum);
        matchingRoomTitle = (TextView) findViewById(R.id.matchingRoomTitle);
        matchingRoomOption = (TextView) findViewById(R.id.matchingRoomOption);
        matchingRoomChatList = (ListView) findViewById(R.id.matchingRoomChatList);

        // 로그인 화면에서 받아온 제목, 성별, 무게, 학년 저장
        Intent intent = getIntent();
        number = intent.getStringExtra("number");
        title = intent.getStringExtra("title");
        memo = intent.getStringExtra("memo");
        master = intent.getBooleanExtra("master", false);
        grade = intent.getStringExtra("grade");
        matchingMasterUID = intent.getStringExtra("masteruid");
        matchingGuestUID = intent.getStringExtra("guest");
        readDB();
        readMaster();
        readBattle();
        matchingRoomNum.setText(number);
        matchingRoomTitle.setText(title);
        matchingRoomOption.setText(memo);
        matchingRoomOtherImg.setVisibility(View.INVISIBLE);
        // 받아온 데이터 저장

        if (master == true) {

            RecyclerItemData roomName = new RecyclerItemData(number, title,
                    memo, flag); // RecyclerItemData를 이용하여 데이터를 묶는다.
            databaseReference.child("chat").child(title).setValue(roomName);
            databaseReference.child("chat").child(title).child("masteruid").setValue(matchingMasterUID);
            masterUid = user.getUid();
            matchingRoomStartBtn.setText("시작");
            matchingRoomStartBtn.setEnabled(false);
            matchingRoomOtherInfo.setEnabled(false);
        } else {
            databaseReference.child("chat").child(title).child("guestuid").setValue(matchingGuestUID);
            guestUid = user.getUid();
            matchingRoomStartBtn.setText("준비대기");
        }
        // 채팅 방 입장
        openChat(title);

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

        // 채팅 글 보내기 버튼
        matchingRoomChatBtn.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {

                RecyclerItemData chat = new RecyclerItemData(nickname,
                        matchingRoomChatEdt.getText().toString()); // RecyclerItemData를 이용하여 데이터를 묶는다.
                databaseReference.child("chat").child(title).child("chating").push().setValue(chat); // 데이터 푸쉬
                matchingRoomChatEdt.setText(""); //입력창 초기화
            }
        });

        // 정보 확인 버튼
        matchingRoomOtherInfo.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                infoDialog = new Dialog(MatchingRoom.this);
                infoDialog.setContentView(R.layout.infomationdialog);
                tvNickname = (TextView) infoDialog.findViewById(R.id.tvNickname);
                tvLevel = (TextView) infoDialog.findViewById(R.id.tvLevel);
                tvHeight = (TextView) infoDialog.findViewById(R.id.tvHeight);
                tvWeight = (TextView) infoDialog.findViewById(R.id.tvWeight);
                tvBmi = (TextView) infoDialog.findViewById(R.id.tvBmi);
                ivExit = (ImageView) infoDialog.findViewById(R.id.ivExit);
                dialog_info_ban_btn = (Button) infoDialog.findViewById(R.id.dialog_info_ban_btn);
                if(!master){
                    dialog_info_ban_btn.setVisibility(View.INVISIBLE);
                }
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        masterUID = snapshot.child("chat").child(title).child("masteruid").getValue(String.class);
                        guestUID = snapshot.child("chat").child(title).child("guestuid").getValue(String.class);
                        if (user.getUid().equals(masterUID)) {
                            GuestInfo guestInfo = snapshot.child("User").child(guestUID).getValue(GuestInfo.class);

                            int Level = guestInfo.total_point / 500;

                            tvNickname.setText("닉네임 : " + guestInfo.nickname);
                            tvLevel.setText("Level : " + Level);
                            tvHeight.setText("키 : " + guestInfo.height);
                            tvWeight.setText("몸무게 : " + guestInfo.weight);
                            tvBmi.setText("BMI지수 : " + guestInfo.bmi);
                        } else {
                            MasterInfo masterInfo = snapshot.child("User").child(masterUID).getValue(MasterInfo.class);

                            int Level = masterInfo.total_point / 500;

                            tvNickname.setText("닉네임 : " + masterInfo.nickname);
                            tvLevel.setText("Level : " + Level);
                            tvHeight.setText("키 : " + masterInfo.height);
                            tvWeight.setText("몸무게 : " + masterInfo.weight);
                            tvBmi.setText("BMI지수 : " + masterInfo.bmi);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


                infoDialog.show();
                infoDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                dialog_info_ban_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        guestOut=false;
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("chat").child(title).child("guestuid");
                        ref.removeValue();
                    }
                });
                ivExit.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        switch (motionEvent.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                ivExit.setImageResource(R.drawable.exit2);
                                break;
                            case MotionEvent.ACTION_UP:
                                ivExit.setImageResource(R.drawable.exit);
                                infoDialog.dismiss();
                                break;
                        }
                        return true;
                    }
                });
            }
        });

        // 대결 시작 버튼 누를 시 방장과 다르게 눌리는 버튼
        matchingRoomStartBtn.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                if (master == true) {
                    long t = System.currentTimeMillis() / 1000;
                    onBattle = true;
                    battle = new Battle(
                            title,
                            "GuestUID",
                            (t + 604799),
                            300,
                            300,
                            grade,
                            6,
                            6,
                            "day",
                            "day",
                            "day",
                            "day",
                            "day",
                            "day",
                            "day",
                            "day",
                            "day",
                            "day"
                    );
                    battleValue = battle.toMap();
                    childUpdates.put("/Battle/" + title, battleValue);
                    databaseReference.updateChildren(childUpdates);
                } else {
                    if (!flag) {
                        flag = true;
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("chat").child(title).child("master");
                        ref.setValue(flag);
                    } else {
                        flag = false;
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("chat").child(title).child("master");
                        ref.setValue(flag);
                    }
                }
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("test", "onStop");
        battleRef.removeEventListener(battleValueEventListener);
    }

    private void addMessage(DataSnapshot dataSnapshot, ArrayAdapter<String> adapter) {
        RecyclerItemData chat = dataSnapshot.getValue(RecyclerItemData.class);
        adapter.add(chat.getUserName() + " : " + chat.getMessage());
    }

    private void removeMessage(DataSnapshot dataSnapshot, ArrayAdapter<String> adapter) {
        RecyclerItemData chat = dataSnapshot.getValue(RecyclerItemData.class);
        adapter.remove(chat.getUserName() + " : " + chat.getMessage());
    }

    private void openChat(String chatName) {
        // 리스트 어댑터 생성 및 세팅
        ArrayAdapter<String> adapter
                = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1){
            @Override
            public View getView(int position, View convertView, ViewGroup parent)
            {
                View view = super.getView(position, convertView, parent);
                TextView tv = (TextView) view.findViewById(android.R.id.text1);
                tv.setTextColor(Color.BLACK);
                return view;
            }
        };
        matchingRoomChatList.setAdapter(adapter);

        // 데이터 받아오기 및 어댑터 데이터 추가 및 삭제 등..리스너 관리
        databaseReference.child("chat").child(chatName).child("chating").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                addMessage(dataSnapshot, adapter);
                Log.e("LOG", "s:" + s);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                removeMessage(dataSnapshot, adapter);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    void readDB() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("chat").child(title);
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.getKey().equals("guestuid")) {
                    matchingRoomOtherImg.setVisibility(View.VISIBLE);
                    if(master) {
                            Toast.makeText(getApplicationContext(), "상대방이 입장했습니다.", Toast.LENGTH_SHORT).show();
                            matchingRoomOtherInfo.setEnabled(true);
                        }
                } else if(snapshot.getKey().equals("masterOut")){
                    masterOut=true;
                }else  if(snapshot.getKey().equals("guestOut")){
                    DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference().child("chat").child(title);
                    ref2.child("guestOut").removeValue();
                    guestOut=true;
                }else if(snapshot.getKey().equals("battleStart")){
                    onBattle=true;
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                if(onBattle){
                    Toast.makeText(getApplicationContext(), "배틀이 시작됩니다", Toast.LENGTH_SHORT).show();
                }else {
                    if (masterOut) {
                        if (!master) {
                            masterOut = false;
                            guestOut = true;
                            Toast.makeText(getApplicationContext(), "방장이 방을 나갔습니다", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            finish();
                        }
                    } else if (snapshot.getKey().equals("guestuid")) {
                        if (master) {
                            matchingRoomOtherImg.setVisibility(View.INVISIBLE);
                            if (guestOut) {
                                Toast.makeText(getApplicationContext(), "상대방이 나갔습니다", Toast.LENGTH_SHORT).show();
                                matchingRoomOtherInfo.setEnabled(false);
                                guestOut = false;
                            } else {
                                matchingRoomOtherInfo.setEnabled(false);
                                Toast.makeText(getApplicationContext(), "상대방을 강퇴했습니다.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            if (guestOut) {
                                guestOut = false;
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(), "강퇴당하셨습니다.", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }
                    }
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    void readBattle() {
        battleValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(title).getValue() != null) {
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("User").child(user.getUid()).child("battle");
                    ref.setValue(title);
                    if (!master) {
                        DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference().child("Battle").child(title).child("guest");
                        ref2.setValue(guestUid);
                    }
                    if (Login) {
                        Login = false;
                        databaseReference.child("chat").child(title).child("battleStart").setValue("true");

                        DatabaseReference ref3 = FirebaseDatabase.getInstance().getReference().child("chat").child(title);
                        ref3.removeValue();
                        Intent intent = new Intent(MatchingRoom.this, BattleRoom.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK); // 이전의 스택을 다 지운다.
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // 새로운 루트 스택을 생성해준다.
                        startActivity(intent);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        battleRef.addValueEventListener(battleValueEventListener);
    }

    void readMaster() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("chat").child(title).child("master");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Boolean ready = Boolean.parseBoolean(String.valueOf(snapshot.getValue()));
                if (ready) {
                    if (master) {
                        matchingRoomStartBtn.setEnabled(true);
                    } else {
                        matchingRoomStartBtn.setText("준비완료");
                    }
                } else {
                    if (master) {
                        matchingRoomStartBtn.setEnabled(false);
                    } else {
                        matchingRoomStartBtn.setText("준비대기");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("채팅방을 나가시겠습니까?");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                if (master == true) {
                    databaseReference.child("chat").child(title).child("masterOut").setValue("true");
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("chat").child(title);
                    ref.removeValue();
                    Toast.makeText(getApplicationContext(), "방을 나갔습니다.", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    databaseReference.child("chat").child(title).child("guestOut").setValue("true");
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("chat").child(title).child("guestuid");
                    ref.removeValue();
                    finish();
                }
            }
        });
        builder.setNegativeButton("Cancel", null);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
