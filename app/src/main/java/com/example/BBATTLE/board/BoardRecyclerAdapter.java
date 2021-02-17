package com.example.BBATTLE.board;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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

import com.example.BBATTLE.R;

import java.util.ArrayList;

public class BoardRecyclerAdapter extends RecyclerView.Adapter<BoardRecyclerAdapter.ViewHolder>{
    private ArrayList<BoardRecyclerItem> mData;
    public BoardRecyclerAdapter(ArrayList<BoardRecyclerItem> dates) {
        this.mData = dates;
    }
    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.activity_board_recycler_viewholder,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;

    }

    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.boardDate.setText(mData.get(position).date);
        holder.boardTitle.setText(mData.get(position).title);
        holder.boardNickname.setText(mData.get(position).nickname);
    }

    // getItemCount() - 전체 데이터 갯수 리턴.
    @Override
    public int getItemCount() {
        return mData.size();
    }

    // 아이템 뷰를 저장하는 뷰홀더 클래스.
    class ViewHolder extends RecyclerView.ViewHolder{ // 자료를 담고 있는 클래스
        TextView boardDate, boardNickname, boardTitle,
                boardInfoNickName, boardInfoTitle, boardInfoDate, boardInfoMemo;
        CardView boardCardView;
        LinearLayout boardlayout;
        Dialog boardInfo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // 뷰 객체에 대한 참조. (hold strong reference)

            boardDate = itemView.findViewById(R.id.boardDate);
            boardNickname = itemView.findViewById(R.id.boardNickname);
            boardTitle = itemView.findViewById(R.id.boardTitle);
            boardCardView = itemView.findViewById(R.id.boardCardView);
            boardlayout = itemView.findViewById(R.id.boardlayout);

            boardlayout.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    switch (motionEvent.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            boardlayout.setBackgroundDrawable(ContextCompat.getDrawable(boardlayout.getContext(),R.drawable.edgeborderbtn));
                            break;
                        case MotionEvent.ACTION_CANCEL: {
                            boardlayout.setBackgroundDrawable(ContextCompat.getDrawable(boardlayout.getContext(),R.drawable.edgeborderbtn3));
                            break;
                        }
                        case MotionEvent.ACTION_UP:
                            boardlayout.setBackgroundDrawable(ContextCompat.getDrawable(boardlayout.getContext(),R.drawable.edgeborderbtn3));

                            boardInfo = new Dialog(boardlayout.getContext());
                            boardInfo.setContentView(R.layout.activity_board_info_dialog);

                            boardInfoNickName = (TextView) boardInfo.findViewById(R.id.boardInfoNickName);
                            boardInfoTitle = (TextView) boardInfo.findViewById(R.id.boardInfoTitle);
                            boardInfoDate = (TextView) boardInfo.findViewById(R.id.boardInfoDate);
                            boardInfoMemo = (TextView) boardInfo.findViewById(R.id.boardInfoMemo);

                            boardInfo.show();
                            boardInfo.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                            int pos = getAdapterPosition();
                            if (pos != RecyclerView.NO_POSITION) {
                                boardInfoNickName.setText(mData.get(pos).nickname);
                                boardInfoTitle.setText(mData.get(pos).title);
                                boardInfoDate.setText(mData.get(pos).date);
                                boardInfoMemo.setText(mData.get(pos).memo);
                            }
                            break;
                    }
                    return true;
                }
            });
        }
    }
}
