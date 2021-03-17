//package com.example.plank;
//
//import android.animation.ValueAnimator;
//import android.util.SparseBooleanArray;
//import android.view.View;
//import android.widget.ImageButton;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.constraintlayout.widget.ConstraintLayout;
//import androidx.recyclerview.widget.RecyclerView;
//
//public class ViewHolderPlank extends RecyclerView.ViewHolder {
//
//    TextView tv_plank,tv_plank2;
//    ImageView iv_plank,iv_plank2;
//    ImageButton ib_plank;
//    ConstraintLayout constraintLayout,constraintLayout2;
//
//    RVAdapter.OnViewHolderItemClickListener onViewHolder;
//
//    public ViewHolderPlank(@NonNull View itemView){
//        super(itemView);
//
//        iv_plank = itemView.findViewById(R.id.arrow);
//        tv_plank = itemView.findViewById(R.id.day);
//        iv_plank2 = itemView.findViewById(R.id.time);
//        tv_plank2 = itemView.findViewById(R.id.day2);
//        ib_plank = itemView.findViewById(R.id.que);
//        constraintLayout = itemView.findViewById(R.id.constLayout);
//        constraintLayout2 = itemView.findViewById(R.id.acco);
//        constraintLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                onViewHolder.onViewHolderItemClick();
//            }
//        });
//
//    }
//    public void onBind(DataPlank data,int position, SparseBooleanArray selectedItems){
//        iv_plank.setImageResource(data.getImage());
//        tv_plank.setText(data.getDay());
//
//        tv_plank2.setText("플랭크");
//        iv_plank2.setImageResource(data.getImage());
//        changeVisibility(selectedItems.get(position));
//    }
//
//    public void changeVisibility(final  boolean isExpanded){
//        // ValueAnimator.ofInt(int... values)는 View가 변할 값을 지정, 인자는 int 배열
//        ValueAnimator va = isExpanded ? ValueAnimator.ofInt(0, 600) : ValueAnimator.ofInt(600, 0);
//        // Animation이 실행되는 시간, n/1000초
//        va.setDuration(500);
//        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator animation) {
//                // imageView의 높이 변경
//                iv_plank2.getLayoutParams().height = (int) animation.getAnimatedValue();
//                iv_plank2.requestLayout();
//                // imageView가 실제로 사라지게하는 부분
//                constraintLayout2.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
//            }
//        });
//        // Animation start
//        va.start();
//    }
//    public void setOnViewHolderItemClickListener(RVAdapter.OnViewHolderItemClickListener onViewHolder){
//        this.onViewHolder = onViewHolder;
//    }
//}
