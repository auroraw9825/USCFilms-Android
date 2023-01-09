package com.example.hw9_2;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class SliderAdapter extends SliderViewAdapter<SliderAdapter.SliderAdapterViewHolder> {

    // list for storing urls of images.
    private final List<SliderData> mSliderItems;
    private Context context;

    // Constructor
    public SliderAdapter(Context context, ArrayList<SliderData> sliderDataArrayList) {
        this.mSliderItems = sliderDataArrayList;
        this.context = context;
    }

    // We are inflating the slider_layout
    // inside on Create View Holder method.
    @Override
    public SliderAdapterViewHolder onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.slider_layout, null);
        return new SliderAdapterViewHolder(inflate);
    }
    // Inside on bind view holder we will
    // set data to item of Slider View.
    @Override
    public void onBindViewHolder(SliderAdapterViewHolder viewHolder, final int position) {

        final SliderData sliderItem = mSliderItems.get(position);

        // Glide is use to load image
        // bottom blured out Image
        // can not use fitCenter(), centerCrop() when using transforms
        Glide.with(viewHolder.itemView)
                .load(sliderItem.getPoster_path())
                .transform(new jp.wasabeef.glide.transformations.BlurTransformation(25, 3))
                .into(viewHolder.bottomImage);
        // top Image
        Glide.with(viewHolder.itemView)
                .load(sliderItem.getPoster_path())
                .fitCenter()
                .into(viewHolder.topImage);

        // click on the card, go to detail page
        viewHolder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Toast.makeText(context, items.get(position), Toast.LENGTH_SHORT).show();
//                ProgressDialog nDialog;
//                nDialog = new ProgressDialog(context);
//                nDialog.setMessage("Loading..");
//                nDialog.setTitle("Get Data");
//                nDialog.setIndeterminate(false);
//                nDialog.setCancelable(true);
//                nDialog.show();

                Intent intent = new Intent(context, DetailsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("id", sliderItem.getId());
                intent.putExtra("media_type", sliderItem.getMedia_type());
//                nDialog.dismiss();
                context.startActivity(intent); // if in MainActivity can just use startActivity(), no "context."
            }
        });

    }

    // this method will return
    // the count of our list.
    @Override
    public int getCount() {
        return mSliderItems.size();
    }

    static class SliderAdapterViewHolder extends SliderViewAdapter.ViewHolder {
        // Adapter class for initializing
        // the views of our slider view.
        View itemView;
        ImageView topImage;
        ImageView bottomImage;
        RelativeLayout parentLayout;
        public SliderAdapterViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            topImage = itemView.findViewById(R.id.topImage);
            bottomImage = itemView.findViewById(R.id.bottomImage);
            parentLayout=itemView.findViewById(R.id.slider_parent);
        }
    }
}