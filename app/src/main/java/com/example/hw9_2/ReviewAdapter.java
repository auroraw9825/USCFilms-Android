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

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {
    private Context context;
    private ArrayList<ReviewData> items;

    public ReviewAdapter(Context context, ArrayList<ReviewData> items) {
        this.context = context;
        this.items = items;
    }


    @NonNull
    @Override
    public ReviewAdapter.ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ReviewAdapter.ReviewViewHolder(LayoutInflater.from(context).inflate(R.layout.review_cards, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewAdapter.ReviewViewHolder holder, int position) {
        ReviewData item = items.get(position);
        // the string for author and created time.
        String authorCreatedTime = "by " + item.getAuthor() + " on " + item.getCreatTime();// missing 星期几!!!!!!!!!!!!!!!!!!!!
        holder.review_author.setText(authorCreatedTime);
        // rating string with /5
        String ratingStar = item.getRating() + "/5";
        holder.review_rating.setText(ratingStar);
        // review content
        String content = item.getContent();
        holder.review_content.setText(content);

        // make review cards clickable, and will open Review Activity and show new page.
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Toast.makeText(context, items.get(position), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, ReviewActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("authorCreatedTime", authorCreatedTime); // the string for author and created time.
                intent.putExtra("ratingStar", ratingStar); // rating with /5
                intent.putExtra("content", content);
                context.startActivity(intent); // if in MainActivity can just use startActivity(), no "context."
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    public class ReviewViewHolder extends RecyclerView.ViewHolder {
        // Adapter class for initializing
        // the views of our slider view.
        View itemView;
        TextView review_author;
        TextView review_rating;
        TextView review_content;
        CardView parentLayout;

        public ReviewViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            review_author = itemView.findViewById(R.id.review_author);
            review_rating = itemView.findViewById(R.id.review_rating);
            review_content = itemView.findViewById(R.id.review_content);
            parentLayout = itemView.findViewById(R.id.reviewCard);
        }
    }
}
