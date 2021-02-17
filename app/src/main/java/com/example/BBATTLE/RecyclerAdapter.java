package com.example.BBATTLE;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>{
    private ArrayList<RecyclerItemData> mData;
    public RecyclerAdapter(ArrayList<RecyclerItemData> dates) {
        this.mData = dates;
    }
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.activity_matchinglist_recycler_viewholder,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;

    }

    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvNumber.setText(mData.get(position).number);
        holder.tvTitle.setText(mData.get(position).title);
        holder.tvMemo.setText(mData.get(position).memo);
    }

    // getItemCount() - 전체 데이터 갯수 리턴.
    @Override
    public int getItemCount() {
        return mData.size();
    }

    // 아이템 뷰를 저장하는 뷰홀더 클래스.
    class ViewHolder extends RecyclerView.ViewHolder{ // 자료를 담고 있는 클래스
        TextView tvNumber, tvTitle, tvMemo;
        CardView cvList;
        LinearLayout listLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // 뷰 객체에 대한 참조. (hold strong reference)

            tvNumber = itemView.findViewById(R.id.tvNumber);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvMemo = itemView.findViewById(R.id.tvMemo);
            cvList = itemView.findViewById(R.id.cvList);
            listLayout = itemView.findViewById(R.id.listLayout);

            itemView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    switch (motionEvent.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            cvList.setCardBackgroundColor(Color.parseColor("#99ffffff"));
                            break;
                        case MotionEvent.ACTION_CANCEL:{
                            cvList.setCardBackgroundColor(Color.parseColor("#ffffff"));
                            break;
                        }
                        case MotionEvent.ACTION_UP:
                            cvList.setCardBackgroundColor(Color.parseColor("#ffffff"));
                            int pos = getAdapterPosition();
                            if (pos != RecyclerView.NO_POSITION) {
                                // 데이터 리스트로부터 아이템 데이터 참조.
                                String number = mData.get(pos).number;
                                String title = mData.get(pos).title;
                                String memo = mData.get(pos).memo;

                                Intent intent = new Intent(listLayout.getContext(), MatchingRoom.class);
                                intent.putExtra("number",number);
                                intent.putExtra("title",title);
                                intent.putExtra("memo",memo);
                                intent.putExtra("master",false);
                                intent.putExtra("guest",user.getUid());
                                ContextCompat.startActivity(listLayout.getContext(),intent,null);
                            }
                            break;
                    }
                    return true;
                }
            });
//            listLayout.setOnTouchListener(new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View v, MotionEvent motionEvent) {
//                    switch (motionEvent.getAction()) {
//                        case MotionEvent.ACTION_DOWN:
//                            cvList.setCardBackgroundColor(Color.parseColor("#99ffffff"));
//                            break;
//                        case MotionEvent.ACTION_CANCEL:{
//                            cvList.setCardBackgroundColor(Color.parseColor("#ffffff"));
//                            break;
//                        }
//                        case MotionEvent.ACTION_UP:
//                            cvList.setCardBackgroundColor(Color.parseColor("#ffffff"));
//                            int pos = getAdapterPosition() ;
//                            if (pos != RecyclerView.NO_POSITION) {
//                                // 데이터 리스트로부터 아이템 데이터 참조.
//                                RecyclerItemData item = mData.get(pos) ;
//                                Log.i("pos", String.valueOf(item));
//                                // TODO : use item.
//                            }
//
////                            Intent intent = new Intent(listLayout.getContext(), MatchingRoom.class);
////                            ContextCompat.startActivity(listLayout.getContext(),intent,null);
//                            break;
//                    }
//                    return true;
//                }
//            });

            // 뷰 클릭 시 실행하는 메소드
            /*itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();

                    // 갱신하는 과정에서 뷰홀더가 참조하는 아이템이 어댑터에서 삭제되면 getAdapterPosition() 메서드는 NO_POSITION을 리턴
                    if (pos != RecyclerView.NO_POSITION) {
                        cvList.setCardBackgroundColor(Color.parseColor("#99ffffff"));
                    }
                }
            });*/
        }
    }
}


