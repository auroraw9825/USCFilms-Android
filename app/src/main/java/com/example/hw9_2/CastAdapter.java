package com.example.hw9_2;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class CastAdapter extends RecyclerView.Adapter<CastAdapter.CastViewHolder> {
    private Context context;
    private ArrayList<CastData> items;

    public CastAdapter(Context context, ArrayList<CastData> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public CastAdapter.CastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CastAdapter.CastViewHolder(LayoutInflater.from(context).inflate(R.layout.cast_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CastAdapter.CastViewHolder holder, int position) {
        CastData item = items.get(position);
        holder.cast_name.setText(item.getName());

        // Glide is use to load image from url in your imageview.
        Glide.with(holder.itemView)
                .load(item.getProfile_path())
                .fitCenter()
                .into(holder.cast_profile);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    public class CastViewHolder extends RecyclerView.ViewHolder {
        // Adapter class for initializing
        // the views of our slider view.
        View itemView;
        CircleImageView cast_profile;
        TextView cast_name;

        public CastViewHolder(View itemView) {
            super(itemView);
            cast_profile = itemView.findViewById(R.id.cast_profile);
            this.itemView = itemView;
            cast_name = itemView.findViewById(R.id.cast_name);

        }
    }
}
