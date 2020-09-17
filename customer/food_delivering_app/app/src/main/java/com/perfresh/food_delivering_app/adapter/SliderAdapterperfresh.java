package com.perfresh.food_delivering_app.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.perfresh.food_delivering_app.R;
import com.bumptech.glide.Glide;

import com.perfresh.food_delivering_app.activities.Category;
import com.perfresh.food_delivering_app.model.SliderItem;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class SliderAdapterperfresh extends SliderViewAdapter<SliderAdapterperfresh.SliderAdapter> {


    private Context context;
    private Activity activity;
    private List<SliderItem> mSliderItems = new ArrayList<>();

    public SliderAdapterperfresh(Context context, List<SliderItem> mSliderItems, Activity activity) {
        this.context = context;
        this.mSliderItems = mSliderItems;
        this.activity = activity;
    }

    @Override
    public SliderAdapter onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_slider_layout_item, null);
        return new SliderAdapter(inflate);
    }

    @Override
    public void onBindViewHolder(SliderAdapter viewHolder, final int position) {
        final SliderItem sliderItem = mSliderItems.get(position);

//        viewHolder.textViewDescription.setText(sliderItem.getTitle());
//        viewHolder.textViewDescription.setTextSize(16);
//        viewHolder.textViewDescription.setTextColor(Color.WHITE);
        Glide.with(viewHolder.itemView)
                .load(sliderItem.getImageUrl())
                .fitCenter()
                .into(viewHolder.imageViewBackground);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sliderItem.getRec().equals("rec1") || sliderItem.getRec().equals("rec2")){
                    Intent intent=new Intent(context, Category.class);
                    intent.putExtra("name", sliderItem.getTitle());
                    intent.putExtra("rec", sliderItem.getRec());
                    activity.startActivityForResult(intent, 1);
                }

            }
        });
    }

    @Override
    public int getCount() {
        return mSliderItems.size();
    }

    public static class SliderAdapter extends SliderViewAdapter.ViewHolder {

        View itemView;
        ImageView imageViewBackground;
        ImageView imageGifContainer;
        TextView textViewDescription;

        public SliderAdapter(View itemView) {
            super(itemView);
            imageViewBackground = itemView.findViewById(R.id.iv_auto_image_slider);
            imageGifContainer = itemView.findViewById(R.id.iv_gif_container);
            textViewDescription = itemView.findViewById(R.id.tv_auto_image_slider);
            this.itemView = itemView;
        }
    }
}