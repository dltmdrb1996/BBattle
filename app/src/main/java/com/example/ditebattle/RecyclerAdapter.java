package com.example.ditebattle;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>{
    private ArrayList<RecyclerItemData> mData;
    public RecyclerAdapter(ArrayList<RecyclerItemData> dates) {
        this.mData = dates;
    }

    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.matchinglistview,parent,false);
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

            listLayout.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent motionEvent) {
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
                            break;
                    }
                    return true;
                }
            });

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


