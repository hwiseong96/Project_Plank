package com.example.plank;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.List;

public class OnboardingAdapter extends PagerAdapter {

    Context context;
    List<String> obj;
    OnboardingAdapter(List<String> res, Context context){
        obj = res;
        this.context = context;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        View view = null;
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.activity_guide,container,false);
        TextView textView = (TextView)view.findViewById(R.id.explane);
        textView.setText((CharSequence) obj.get(position));
        container.addView(view);

        return view;
    }

    @Override
    public int getCount() {
        return obj.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return false;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
//        super.destroyItem(container, position, object);
    }
}
