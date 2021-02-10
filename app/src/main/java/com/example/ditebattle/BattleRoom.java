package com.example.ditebattle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ditebattle.database.Battle;
import com.example.ditebattle.database.Chating;
import com.example.ditebattle.database.User;
import com.example.ditebattle.database.ExerciseRoutine;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class BattleRoom extends AppCompatActivity {
    ImageView battleRoomIvMission, battleRoomIvChat, battleRoomIvPoint,
            ivMissionExit, ivPointExit, ivPointBuy1, ivPointBuy2, ivPointBuy3,
            ivChatingExit;
    ImageView[] ivMissionCheckBox = new ImageView[5];
    TextView[] tvMission = new TextView[5];
    int CheckBox[] = {R.id.ivMissionCheckBox1, R.id.ivMissionCheckBox2, R.id.ivMissionCheckBox3,
            R.id.ivMissionCheckBox4, R.id.ivMissionCheckBox5};
    int tv[] = {R.id.tvMission1, R.id.tvMission2, R.id.tvMission3, R.id.tvMission4, R.id.tvMission5};
    ImageView battleRoomBattleFragmentBtn2, battleRoomBattleInfoFragmentBtn2;
    ListView lvChating;
    EditText edtChating;
    TextView battleRoomPointTv, battleRoomDateTv;
    Button btnChating;
    BattleFragment battleFragment;
    BattleInfoFragment battleInfoFragment;
    FragmentManager fragmentManager = getSupportFragmentManager();
    FragmentTransaction fragmentTransaction;
    Dialog missiondialog, pointdialog, chatdialog, winDialog, loseDialog;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference();
    ArrayList<String> myUserDb = new ArrayList<String>();
    ArrayList<String> myBattleDb = new ArrayList<String>();
    Boolean firstLogin = true , gameEnd=false;
    long now = System.currentTimeMillis();
    Date mDate = new Date(now);
    SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd");
    String getTime = simpleDate.format(mDate);
    InputMethodManager imm;
    ChildEventListener maddChildEventListener;
    DatabaseReference refbattle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battle);
        ActionBar ac = getSupportActionBar();
        ac.hide();

        imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);

        battleRoomDateTv = (TextView) findViewById(R.id.battleRoomDateTv);
        battleRoomPointTv = (TextView) findViewById(R.id.battleRoomPointTv);
        battleRoomIvMission = (ImageView) findViewById(R.id.battleRoomIvMission);
        battleRoomIvChat = (ImageView) findViewById(R.id.battleRoomIvChat);
        battleRoomIvPoint = (ImageView) findViewById(R.id.battleRoomIvPoint);
        battleRoomBattleFragmentBtn2 = (ImageView) findViewById(R.id.battleRoomBattleFragmentBtn2);
        battleRoomBattleInfoFragmentBtn2 = (ImageView) findViewById(R.id.battleRoomBattleInfoFragmentBtn2);

        battleRoomDateTv.setText(getTime);
        battleFragment = new BattleFragment();
        battleInfoFragment = new BattleInfoFragment();
        fragmentTransaction = fragmentManager.beginTransaction();

        firstReadDB();

        // 미션 버튼 클릭 시
        battleRoomIvMission.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        battleRoomIvMission.setBackgroundResource(R.drawable.layoutborderbuttonclick);
                        break;
                    case MotionEvent.ACTION_UP:
                        battleRoomIvMission.setBackgroundResource(R.drawable.layoutborderbutton);
                        missiondialog = new Dialog(BattleRoom.this);
                        missiondialog.setContentView(R.layout.activity_battle_mission_dialog);

                        for(int i=0; i<ivMissionCheckBox.length; i++){
                            ivMissionCheckBox[i] = (ImageView) missiondialog.findViewById(CheckBox[i]);
                            tvMission[i] = (TextView) missiondialog.findViewById(tv[i]);
                        }

                        ivMissionExit = (ImageView) missiondialog.findViewById(R.id.ivMissionExit);

                        missiondialog.show();
                        missiondialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                        ivMissionExit.setOnTouchListener(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View view, MotionEvent motionEvent) {
                                switch (motionEvent.getAction()) {
                                    case MotionEvent.ACTION_DOWN:
                                        ivMissionExit.setImageResource(R.drawable.exit2);
                                        break;
                                    case MotionEvent.ACTION_UP:
                                        ivMissionExit.setImageResource(R.drawable.exit);
                                        missiondialog.dismiss();
                                        break;
                                }
                                return true;
                            }
                        });
                        ivMissionExit.setOnClickListener(new OnSingleClickListener() {
                            @Override
                            public void onSingleClick(View v) {
                                ivMissionExit.setImageDrawable(getResources().getDrawable(R.drawable.exit2));
                                missiondialog.dismiss();
                            }
                        });

                        /// 미션클리어시 클릭 비활성화 마스터
                        if(myBattleDb.get(1).equals(user.getUid())) {
                            for (int i = 0; i < 5; i++) {
                                int index = i + 13;
                                if (myBattleDb.get(index).equals("미션클리어")) {
                                    ivMissionCheckBox[i].setImageResource(R.drawable.checkbox2);
                                    ivMissionCheckBox[i].setEnabled(false);
                                }
                            }
                        }
                        /// 미션클리어시 클릭 비활성화 게스트
                        if(!myBattleDb.get(1).equals(user.getUid())) {
                            for (int i = 0; i < 5; i++) {
                                int index = i + 8;
                                if (myBattleDb.get(index).equals("미션클리어")) {
                                    ivMissionCheckBox[i].setImageResource(R.drawable.checkbox2);
                                    ivMissionCheckBox[i].setEnabled(false);
                                }
                            }
                        }

                        /// 미션클리어 리스너
                        for(int i=0; i<ivMissionCheckBox.length; i++){
                            int index = i;
                            ivMissionCheckBox[index].setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(BattleRoom.this);
                                    builder.setTitle("미션을 완료하시겠습니까?");
                                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int id) {
                                            ivMissionCheckBox[index].setImageResource(R.drawable.checkbox2);
                                            String curMission=null;
                                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Battle").child(myUserDb.get(10));

                                            /// 게스트 마스터 판별후 그에따른 미션 클리어 및 DB 반영
                                            if(myBattleDb.get(1).equals(user.getUid())){
                                                int select = index+13;
                                                if(!tvMission[index].getText().toString().equals("미션클리어")) {
                                                    curMission = "gmission" + (index+1);
                                                    ref.child("masterHP").setValue(Integer.parseInt(myBattleDb.get(3)) - 70);
                                                    ref.child(curMission).setValue("미션클리어");
                                                    tvMission[index].setText("미션클리어");
                                                    ivMissionCheckBox[index].setImageResource(R.drawable.checkbox2);
                                                    ivMissionCheckBox[index].setEnabled(false);
                                                }else{
                                                    Toast.makeText(getApplicationContext(),"이미 완료한 미션입니다.",Toast.LENGTH_SHORT).show();
                                                }
                                            }else{
                                                if(!tvMission[index].getText().toString().equals("미션클리어")) {
                                                    curMission = "mmission" + (index+1);
                                                    ref.child("guestHP").setValue(Integer.parseInt(myBattleDb.get(4)) - 70);
                                                    ref.child(curMission).setValue("미션클리어");
                                                    tvMission[index].setText("미션클리어");
                                                    ivMissionCheckBox[index].setImageResource(R.drawable.checkbox2);
                                                    ivMissionCheckBox[index].setEnabled(false);
                                                }else{
                                                    Toast.makeText(getApplicationContext(),"이미 완료한 미션입니다.",Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        }
                                    });
                                    builder.setNegativeButton("Cancel", null);
                                    AlertDialog alertDialog = builder.create();
                                    alertDialog.show();
                                }
                            });
                        }
                        /// 미션 텍스트 출력
                        if(myBattleDb.get(1).equals(user.getUid())) {
                            for (int i = 0; i < 5; i++) {
                                tvMission[i].setText(myBattleDb.get(13 + i));
                            }
                        }else {
                            for (int i = 0; i < 5; i++) {
                                tvMission[i].setText(myBattleDb.get(8 + i));
                            }
                        }
                        break;
                }
                return true;
            }
        });

        // 포인트 버튼 클릭 시
        battleRoomIvPoint.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        battleRoomIvPoint.setBackgroundResource(R.drawable.layoutborderbuttonclick);
                        break;
                    case MotionEvent.ACTION_UP:
                        battleRoomIvPoint.setBackgroundResource(R.drawable.layoutborderbutton);
                        pointdialog = new Dialog(BattleRoom.this);
                        pointdialog.setContentView(R.layout.activity_battle_point_dialog);
                        ivPointExit = (ImageView) pointdialog.findViewById(R.id.ivPointExit);
                        ivPointBuy1 = (ImageView) pointdialog.findViewById(R.id.ivPointBuy1);
                        ivPointBuy2 = (ImageView) pointdialog.findViewById(R.id.ivPointBuy2);
                        ivPointBuy3 = (ImageView) pointdialog.findViewById(R.id.ivPointBuy3);
                        pointdialog.show();
                        pointdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                        ivPointExit.setOnTouchListener(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View view, MotionEvent motionEvent) {
                                switch (motionEvent.getAction()) {
                                    case MotionEvent.ACTION_DOWN:
                                        ivPointExit.setImageResource(R.drawable.exit2);
                                        break;
                                    case MotionEvent.ACTION_UP:
                                        ivPointExit.setImageResource(R.drawable.exit);
                                        pointdialog.dismiss();
                                        break;
                                }
                                return true;
                            }
                        });

                        ivPointBuy1.setOnTouchListener(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View view, MotionEvent motionEvent) {
                                switch (motionEvent.getAction()) {
                                    case MotionEvent.ACTION_DOWN:
                                        ivPointBuy1.setImageResource(R.drawable.buyimage2);
                                        break;
                                    case MotionEvent.ACTION_UP:
                                        ivPointBuy1.setImageResource(R.drawable.buyimage);
                                        break;
                                }
                                return true;
                            }
                        });
                        ivPointBuy2.setOnTouchListener(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View view, MotionEvent motionEvent) {
                                switch (motionEvent.getAction()) {
                                    case MotionEvent.ACTION_DOWN:
                                        ivPointBuy2.setImageResource(R.drawable.buyimage2);
                                        break;
                                    case MotionEvent.ACTION_UP:
                                        ivPointBuy2.setImageResource(R.drawable.buyimage);
                                        break;
                                }
                                return true;
                            }
                        });
                        ivPointBuy3.setOnTouchListener(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View view, MotionEvent motionEvent) {
                                switch (motionEvent.getAction()) {
                                    case MotionEvent.ACTION_DOWN:
                                        ivPointBuy3.setImageResource(R.drawable.buyimage2);
                                        break;
                                    case MotionEvent.ACTION_UP:
                                        ivPointBuy3.setImageResource(R.drawable.buyimage);
                                        break;
                                }
                                return true;
                            }
                        });
                        break;
                }
                return true;
            }
        });

        // 채팅 버튼 클릭 시
        battleRoomIvChat.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        battleRoomIvChat.setBackgroundResource(R.drawable.layoutborderbuttonclick);
                        break;
                    case MotionEvent.ACTION_UP:
                        battleRoomIvChat.setBackgroundResource(R.drawable.layoutborderbutton);
                        chatdialog = new Dialog(BattleRoom.this);
                        chatdialog.setContentView(R.layout.activity_battle_chat_dialog);

                        ivChatingExit = (ImageView) chatdialog.findViewById(R.id.ivChatingExit);
                        lvChating = (ListView) chatdialog.findViewById(R.id.lvChating);
                        edtChating = (EditText) chatdialog.findViewById(R.id.edtChating);
                        btnChating = (Button)chatdialog.findViewById(R.id.btnChating);

                        openChat();
                        chatdialog.show();
                        chatdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                        btnChating.setOnClickListener(new OnSingleClickListener() {
                            @Override
                            public void onSingleClick(View v) {
                                Chating chat = new Chating(user.getEmail(),
                                        edtChating.getText().toString()); // RecyclerItemData를 이용하여 데이터를 묶는다.
                                databaseReference.child("Battle").child(myUserDb.get(10)).child("chating").push().setValue(chat); // 데이터 푸쉬 리스너에사 데이터처리
                                edtChating.setText(""); //입력창 초기화
                            }
                        });

                        ivChatingExit.setOnTouchListener(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View view, MotionEvent motionEvent) {
                                switch (motionEvent.getAction()) {
                                    case MotionEvent.ACTION_DOWN:
                                        ivChatingExit.setImageResource(R.drawable.exit2);
                                        break;
                                    case MotionEvent.ACTION_UP:
                                        ivChatingExit.setImageResource(R.drawable.exit);
                                        chatdialog.dismiss();
                                        break;
                                }
                                return true;
                            }
                        });
                        break;
                }
                return true;
            }
        });

        /// 상단 메뉴 클릭시 프래그먼트 전환
        battleRoomBattleFragmentBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.battleRoomFragContainer2, battleFragment, "myFrag").commit();
                battleRoomBattleFragmentBtn2.setImageResource(R.drawable.battlefragbtnclick);
                battleRoomBattleInfoFragmentBtn2.setImageResource(R.drawable.battlgrapbtn2);
            }
        });
        battleRoomBattleInfoFragmentBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.battleRoomFragContainer2, battleInfoFragment, "myFrag").commit();
                battleRoomBattleFragmentBtn2.setImageResource(R.drawable.battlfragbtn);
                battleRoomBattleInfoFragmentBtn2.setImageResource(R.drawable.battlegrapbtnclick);
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(!gameEnd) {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Battle").child(myUserDb.get(10));
            /// 사용자가 나갈시 DB에 상태저장
            if (myBattleDb.get(1).equals(user.getUid())) {
                Log.e("onstopguest",myBattleDb.get(19));
                ref.child("guestExit").setValue("true");
            } else {
                Log.e("onstopmaster",myBattleDb.get(20));
                ref.child("masterExit").setValue("true");
            }
        }
        refbattle.removeEventListener(maddChildEventListener);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }


    // 처음 액티비티에 들어왔을떄 DB정보를 불러와 저장해주는 리스너
    void firstReadDB() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                /// 유저 DB 정보 저장
                if (firstLogin) {
                    User get = snapshot.child("User").child(user.getUid()).getValue(User.class);
                    String[] user = {get.email, get.nickname, String.valueOf(get.age), String.valueOf(get.weight), String.valueOf(get.height), String.valueOf(get.bmi),
                            String.valueOf(get.total_point), String.valueOf(get.current_point), get.gender, String.valueOf(get.flag), get.battle };
                    for (int i=0; i < user.length; i++) {
                        myUserDb.add(user[i]);
                    }
                }

                readExit(); ///사용자가 들어왔는지 확인해주는 리스너


                /// 배틀 DB 정보 저장
                Battle get2 = snapshot.child("Battle").child(myUserDb.get(10)).getValue(Battle.class);
                String[] battle = {get2.master, get2.guest, String.valueOf(get2.finish_time), String.valueOf(get2.masterHP), String.valueOf(get2.guestHP),String.valueOf(get2.grade),String.valueOf(get2.masterDay),String.valueOf(get2.guestDay)
                                    ,get2.mmission1, get2.mmission2, get2.mmission3, get2.mmission4, get2.mmission5 ,get2.gmission1 ,get2.gmission2 ,get2.gmission3 ,get2.gmission4 ,get2.gmission5, get2.win , get2.masterExit, get2.guestExit};
                for (int i = 0; i < battle.length; i++) {
                    myBattleDb.add(battle[i]);
                }
                DatabaseReference ref3 = FirebaseDatabase.getInstance().getReference().child("Battle").child(myUserDb.get(10));
                /// 사용자 액티비티 접속 확인
                if (myBattleDb.get(1).equals(user.getUid())) {
                    ref3.child("guestExit").setValue("false");
                } else if(!myBattleDb.get(1).equals(user.getUid())) {
                    ref3.child("masterExit").setValue("false");
                }
//                Battle get3 = snapshot.child("Battle").child(myUserDb.get(10)).getValue(Battle.class);
//                String[] battle2 = {get3.master, get3.guest, String.valueOf(get3.finish_time), String.valueOf(get3.masterHP), String.valueOf(get3.guestHP),String.valueOf(get3.grade),String.valueOf(get3.masterDay),String.valueOf(get3.guestDay)
//                        ,get3.mmission1, get3.mmission2, get3.mmission3, get3.mmission4, get3.mmission5 ,get3.gmission1 ,get3.gmission2 ,get3.gmission3 ,get3.gmission4 ,get3.gmission5, get3.win , get3.masterExit, get3.guestExit};
//                for (int i = 0; i < battle2.length; i++) {
//                    myBattleDb.set(i,battle2[i]);
//                }
                if(firstLogin) {
                    fragmentTransaction.replace(R.id.battleRoomFragContainer2, battleFragment, "myFrag").commit();
                    firstLogin = false;
                }
                readBattle();


                long t = System.currentTimeMillis() / 1000;
                long totalSec = Long.parseLong(myBattleDb.get(2)) - t;
                int currentday = (int) (totalSec / 86400);  //현재 하루가 지난 시점인지 검사 1일=6 2일=5

                battleRoomPointTv.setText(myUserDb.get(7));

                // 신체 부위 가져오기
                String[] bodypartsStr = {"chest", "core", "fullbody", "legs"};
                // 운동 종목 가져오기
                String[][] ExerciseEx= {{"declinepush", "tripush"}, {"regraise", "twistplank"},
                        {"burpee", "mountainclimer"}, {"lunge", "squat"}};
                String[][] ExerciseHard = {{"inclinepush", "pushup"}, {"palnk", "sidebend"},
                        {"burpee", "jumpingjack"}, {"lunge", "squat"}};
                String[][] ExerciseNormal = {{"kneepushup", "pushup"}, {"superman", "toetouch"},
                        {"burpee", "mountainclimer"}, {"squat", "widesquat"}};


                /// 현재 시점이 하루가 지난시점인지 계산해서 마스터와 게스트에 각각 랜덤한 운동 미션생성후 미션DB저장
                //게스트
                if(myBattleDb.get(1).equals(user.getUid())){
                    //날짜 검사
                    if (Integer.parseInt(myBattleDb.get(7)) == currentday) {
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Battle").child(myUserDb.get(10)).child("guestDay");
                        Integer guestDay = snapshot.child("Battle").child(myUserDb.get(10)).child("guestDay").getValue(Integer.class);
                        ref.setValue(guestDay - 1);
                        String[] missionArray = new String[5];
                        for (int i = 0; i < bodypartsStr.length; i++) {
                            int random = (int) (Math.random() * 2);
                            //난이도 검사
                            if(myBattleDb.get(5).equals("extream")) {
                                ExerciseRoutine routine = snapshot.child("misson").child(myBattleDb.get(5)).child(bodypartsStr[i])
                                        .child(ExerciseEx[i][random]).getValue(ExerciseRoutine.class);
                                String[] ExerciseRoutineInt = {String.valueOf(routine.reps), String.valueOf(routine.set),
                                        String.valueOf(routine.total), routine.name};
                                missionArray[i] = ExerciseRoutineInt[3] + " " + ExerciseRoutineInt[0] + "개 " + ExerciseRoutineInt[1] + "셋트 하기";
                            }else if(myBattleDb.get(5).equals("hard")){
                                ExerciseRoutine routine = snapshot.child("misson").child(myBattleDb.get(5)).child(bodypartsStr[i])
                                        .child(ExerciseHard[i][random]).getValue(ExerciseRoutine.class);
                                String[] ExerciseRoutineInt = {String.valueOf(routine.reps), String.valueOf(routine.set),
                                        String.valueOf(routine.total), routine.name};
                                missionArray[i] = ExerciseRoutineInt[3] + " " + ExerciseRoutineInt[0] + "개 " + ExerciseRoutineInt[1] + "셋트 하기";
                            }else{
                                ExerciseRoutine routine = snapshot.child("misson").child(myBattleDb.get(5)).child(bodypartsStr[i])
                                        .child(ExerciseNormal[i][random]).getValue(ExerciseRoutine.class);
                                String[] ExerciseRoutineInt = {String.valueOf(routine.reps), String.valueOf(routine.set),
                                        String.valueOf(routine.total), routine.name};
                                missionArray[i] = ExerciseRoutineInt[3] + " " + ExerciseRoutineInt[0] + "개 " + ExerciseRoutineInt[1] + "셋트 하기";
                            }
                        }
                        DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference().child("Battle").child(myUserDb.get(10));
                        ref2.child("gmission1").setValue(missionArray[0]);
                        ref2.child("gmission2").setValue(missionArray[1]);
                        ref2.child("gmission3").setValue(missionArray[2]);
                        ref2.child("gmission4").setValue(missionArray[3]);
                        ref2.child("gmission5").setValue("7시기상");
                    }
                }
                //마스터
                else {
                    //날짜 검사
                    if (Integer.parseInt(myBattleDb.get(6)) == currentday) {
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Battle").child(myUserDb.get(10)).child("masterDay");
                        Integer masterDay = snapshot.child("Battle").child(myUserDb.get(10)).child("masterDay").getValue(Integer.class);
                        ref.setValue(masterDay - 1);
                        String[] missionArray = new String[5];
                        for (int i = 0; i < bodypartsStr.length; i++) {
                            int random = (int) (Math.random() * 2);
                            /// 난이도검사
                            if(myBattleDb.get(5).equals("extream")) {
                                ExerciseRoutine routine = snapshot.child("misson").child(myBattleDb.get(5)).child(bodypartsStr[i])
                                        .child(ExerciseEx[i][random]).getValue(ExerciseRoutine.class);
                                String[] ExerciseRoutineInt = {String.valueOf(routine.reps), String.valueOf(routine.set),
                                        String.valueOf(routine.total), routine.name};
                                missionArray[i] = ExerciseRoutineInt[3] + " " + ExerciseRoutineInt[0] + "개 " + ExerciseRoutineInt[1] + "셋트 하기";
                            }else if(myBattleDb.get(5).equals("hard")){
                                ExerciseRoutine routine = snapshot.child("misson").child(myBattleDb.get(5)).child(bodypartsStr[i])
                                        .child(ExerciseHard[i][random]).getValue(ExerciseRoutine.class);
                                String[] ExerciseRoutineInt = {String.valueOf(routine.reps), String.valueOf(routine.set),
                                        String.valueOf(routine.total), routine.name};
                                missionArray[i] = ExerciseRoutineInt[3] + " " + ExerciseRoutineInt[0] + "개 " + ExerciseRoutineInt[1] + "셋트 하기";
                            }else{
                                ExerciseRoutine routine = snapshot.child("misson").child(myBattleDb.get(5)).child(bodypartsStr[i])
                                        .child(ExerciseNormal[i][random]).getValue(ExerciseRoutine.class);
                                String[] ExerciseRoutineInt = {String.valueOf(routine.reps), String.valueOf(routine.set),
                                        String.valueOf(routine.total), routine.name};
                                missionArray[i] = ExerciseRoutineInt[3] + " " + ExerciseRoutineInt[0] + "개 " + ExerciseRoutineInt[1] + "셋트 하기";
                            }
                        }
                        DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference().child("Battle").child(myUserDb.get(10));
                        ref2.child("mmission1").setValue(missionArray[0]);
                        ref2.child("mmission2").setValue(missionArray[1]);
                        ref2.child("mmission3").setValue(missionArray[2]);
                        ref2.child("mmission4").setValue(missionArray[3]);
                        ref2.child("mmission5").setValue("7시기상");
                    }
                }

                // 승패가 결정될 시점에 배틀룸에 없던 사용자에게 후회 접속했을때
                if(snapshot.child("Battle").child(myUserDb.get(10)).child("win").getValue().equals("master")){
                    if(myBattleDb.get(1).equals(user.getUid())){
                        poploseDial("guest");
                    }else{
                    }
                }else if(snapshot.child("Battle").child(myUserDb.get(10)).child("win").getValue().equals("guest")){
                    if(myBattleDb.get(1).equals(user.getUid())){
                    }else{
                        poploseDial("master");
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    void readBattle(){
        refbattle =  FirebaseDatabase.getInstance().getReference().child("Battle").child(myUserDb.get(10));
        maddChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            ///이긴 사랑이 정해질시 그에맞는 승패 다이얼로그 띄어주는 부분
                if(snapshot.getKey().equals("win")){
                    if(snapshot.getValue().equals("master")){
                        if(myBattleDb.get(1).equals(user.getUid())){
                            if(myBattleDb.get(20).equals("false")){
                                poploseDial("guest");
                            }
                        }else{
                            popWinDial("master");
                        }
                    }else{
                        if(myBattleDb.get(1).equals(user.getUid())){
                            popWinDial("guest");
                        }else{
                            if(myBattleDb.get(19).equals("false")) {
                                poploseDial("master");
                            }
                        }
                    }
                }

                //게스트 HP 갱신
                if(snapshot.getKey().equals("guestHP")){
                    myBattleDb.set(4,String.valueOf(Integer.parseInt(myBattleDb.get(4))-70));
                    if(Integer.parseInt(myBattleDb.get(4))<=0){
                        //마스터 승리
                        DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference().child("Battle").child(myUserDb.get(10)).child("win");
                        ref2.setValue("master");
                    }
                //마스터 HP 갱신
                }else if(snapshot.getKey().equals("masterHP")){
                    myBattleDb.set(3,String.valueOf(Integer.parseInt(myBattleDb.get(3))-70));
//                    myBattleDb.set(3,String.valueOf(snapshot.getValue()));
                    if(Integer.parseInt(myBattleDb.get(3))<=0){
                        // 게스트 승리
                        DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference().child("Battle").child(myUserDb.get(10)).child("win");
                        ref2.setValue("guest");
                    }

                    ///미션이 새로 갱신 될시 DB배열도 수정
                }else if(snapshot.getKey().equals("mmission1")){
                    myBattleDb.set(8,String.valueOf(snapshot.getValue()));
                }else if(snapshot.getKey().equals("mmission2")){
                    myBattleDb.set(9,String.valueOf(snapshot.getValue()));
                }else if(snapshot.getKey().equals("mmission3")){
                    myBattleDb.set(10,String.valueOf(snapshot.getValue()));
                }else if(snapshot.getKey().equals("mmission4")){
                    myBattleDb.set(11,String.valueOf(snapshot.getValue()));
                }else if(snapshot.getKey().equals("mmission5")){
                    myBattleDb.set(12,String.valueOf(snapshot.getValue()));
                }else if(snapshot.getKey().equals("gmission1")){
                    myBattleDb.set(13,String.valueOf(snapshot.getValue()));
                }else if(snapshot.getKey().equals("gmission2")){
                    myBattleDb.set(14,String.valueOf(snapshot.getValue()));
                }else if(snapshot.getKey().equals("gmission3")){
                    myBattleDb.set(15,String.valueOf(snapshot.getValue()));
                }else if(snapshot.getKey().equals("gmission4")){
                    myBattleDb.set(16,String.valueOf(snapshot.getValue()));
                }else if(snapshot.getKey().equals("gmission5")){
                    myBattleDb.set(17,String.valueOf(snapshot.getValue()));
                }
            }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        refbattle.addChildEventListener(maddChildEventListener);
    }

    /// 사용자가 현재 배틀룸 액티비티에 존재하는지 안하는지 검사해주는 리스너
    /// 현재 배틀룸 액티비티에 존재하지않는다면 모든 작업을 중지해 에러 방지
    void readExit(){
        refbattle =  FirebaseDatabase.getInstance().getReference().child("Battle").child(myUserDb.get(10));
        refbattle.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if(snapshot.getKey().equals("masterExit")){
                    String result = snapshot.getValue(String.class);
                    Log.e("testmaster",result);
                    myBattleDb.set(19,result);
                } else if(snapshot.getKey().equals("guestExit")){
                    String result = snapshot.getValue(String.class);
                    Log.e("testguest",result);
                    myBattleDb.set(20, result);
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    /// 채팅 추가 메소드
    private void addMessage(DataSnapshot dataSnapshot, ArrayAdapter<String> adapter) {
        Chating chat = dataSnapshot.getValue(Chating.class);
        adapter.add(chat.getUserName() + " : " + chat.getMessage());
    }
    /// 체팅 삭제 메소드
    private void removeMessage(DataSnapshot dataSnapshot, ArrayAdapter<String> adapter) {
        Chating chat = dataSnapshot.getValue(Chating.class);
        adapter.remove(chat.getUserName() + " : " + chat.getMessage());
    }

    /// DB에 채팅이 추가되면 화면에 출력되도록 해줘 실시간 채팅을 가능하게 해주는 리스너
    private void openChat() {
        // 리스트 어댑터 생성 및 세팅
        final ArrayAdapter<String> adapter
                = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1);
        lvChating.setAdapter(adapter);

        // 데이터 받아오기 및 어댑터 데이터 추가 및 삭제 등..리스너 관리
        databaseReference.child("Battle").child(myUserDb.get(10)).child("chating").addChildEventListener(new ChildEventListener() {
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
    /// 프래그먼트에 갱신된 DB정보를 전달해주는 리턴메소드;
   ArrayList<String> deliverUser(){
      return myUserDb;
    };
   ArrayList<String> deliverBattle(){
      return myBattleDb;
    };

   /// 이긴 사람에게 띄어주는 다이얼로그
    void popWinDial(String win){
        gameEnd=true;
        winDialog = new Dialog(BattleRoom.this);
        winDialog.setContentView(R.layout.activity_battle_win_dialog);
        TextView win_Dial_Get_Exp = (TextView)winDialog.findViewById(R.id.win_Dial_Get_Exp);
        TextView win_Dial_Get_Point = (TextView)winDialog.findViewById(R.id.win_Dial_Get_Point);

        ImageView win_Dial_Exit = (ImageView) winDialog.findViewById(R.id.win_Dial_Exit);
        winDialog.show();
        winDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        winDialog.setCancelable(false);
        win_Dial_Exit.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        ivMissionExit.setImageResource(R.drawable.exit2);
                        break;
                    case MotionEvent.ACTION_UP:
                        ivMissionExit.setImageResource(R.drawable.exit3);
                        missiondialog.dismiss();
                        if(win.equals("master")){
                            if(!myBattleDb.get(1).equals(user.getUid())){
                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("User").child(user.getUid());
                                ref.child("battle").setValue("false");
                                ref.child("total_point").setValue((Integer.parseInt(myUserDb.get(6)))+500);
                                ref.child("current_point").setValue((Integer.parseInt(myUserDb.get(7)))+500);
                                Intent intent = new Intent(BattleRoom.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK); // 이전의 스택을 다 지운다.
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // 새로운 루트 스택을 생성해준다.
                                startActivity(intent);
                                break;
                            }
                        }else {
                            if (myBattleDb.get(1).equals(user.getUid())) {
                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("User").child(user.getUid());
                                ref.child("battle").setValue("false");
                                ref.child("total_point").setValue((Integer.parseInt(myUserDb.get(6))) + 500);
                                ref.child("current_point").setValue((Integer.parseInt(myUserDb.get(7))) + 500);
                                Intent intent = new Intent(BattleRoom.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK); // 이전의 스택을 다 지운다.
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // 새로운 루트 스택을 생성해준다.
                                startActivity(intent);
                                break;
                            }
                        }
                }
                return true;
            }
        });

    }

    /// 진 사람에게 띄어주는 다이얼로그
    void poploseDial(String win){
        gameEnd=true;
        loseDialog = new Dialog(BattleRoom.this);
        loseDialog.setContentView(R.layout.activity_battle_lose_dialog);
        TextView lose_Dial_Get_Exp = (TextView)loseDialog.findViewById(R.id.lose_Dial_Get_Exp);
        TextView lose_Dial_Get_Point = (TextView)loseDialog.findViewById(R.id.lose_Dial_Get_Point);
        ImageView lose_Dial_Exit = (ImageView) loseDialog.findViewById(R.id.lose_Dial_Exit);
        loseDialog.show();
        loseDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loseDialog.setCancelable(false);
        lose_Dial_Exit.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        lose_Dial_Exit.setImageResource(R.drawable.exit2);
                        break;
                    case MotionEvent.ACTION_UP:
                        lose_Dial_Exit.setImageResource(R.drawable.exit3);
                        loseDialog.dismiss();
                        if(win.equals("master")){
                            if(myBattleDb.get(1).equals(user.getUid())){
                            }else{
                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("User").child(user.getUid());
                                ref.child("battle").setValue("false");
                                ref.child("total_point").setValue(Integer.parseInt(myUserDb.get(6))+100);
                                ref.child("current_point").setValue(Integer.parseInt(myUserDb.get(7))+100);
                                DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference().child("Battle").child(myUserDb.get(10));
                                ref2.removeValue();
                                Intent intent = new Intent(BattleRoom.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK); // 이전의 스택을 다 지운다.
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // 새로운 루트 스택을 생성해준다.
                                startActivity(intent);
                                break;
                            }
                        }else{
                            if(myBattleDb.get(1).equals(user.getUid())){
                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("User").child(user.getUid());
                                ref.child("battle").setValue("false");
                                ref.child("total_point").setValue(Integer.parseInt(myUserDb.get(6))+100);
                                ref.child("current_point").setValue(Integer.parseInt(myUserDb.get(7))+100);
                                DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference().child("Battle").child(myUserDb.get(10));
                                ref2.removeValue();
                                Intent intent = new Intent(BattleRoom.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK); // 이전의 스택을 다 지운다.
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // 새로운 루트 스택을 생성해준다.
                                startActivity(intent);
                                break;
                            }else{

                            }
                        }

                }
                return true;
            }
        });
    }

    /// 배틀이 진행될시 불필요한 매칭관련 액티비티들을 정리해주며 홈으로 이동하도록 오버라이딩
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(BattleRoom.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK); // 이전의 스택을 다 지운다.
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // 새로운 루트 스택을 생성해준다.
        startActivity(intent);
    }
}