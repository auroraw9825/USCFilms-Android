package com.example.hw9_2;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {
    private Context context;
    private ArrayList<SearchData> items;

    public SearchAdapter(Context context, ArrayList<SearchData> items) {
        this.context = context;
        this.items = items;
    }


    @NonNull
    @Override
    public SearchAdapter.SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SearchAdapter.SearchViewHolder(LayoutInflater.from(context).inflate(R.layout.search_cards, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SearchAdapter.SearchViewHolder holder, int position) {
        SearchData item = items.get(position);
        // holder.resultImage.setImageResource(item.getBackdrop_path());
        holder.resultTitle.setText(item.getTitle());
        holder.resultMedia_type.setText(item.getMedia_type().toUpperCase() + item.getRelease_year());
        holder.resultRating.setText(item.getRating());


        // Glide is use to load image from url in your imageview.
        Glide.with(holder.itemView)
                .load(item.getBackdrop_path())
                .fitCenter()
                .into(holder.resultImage);

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Toast.makeText(context, items.get(position), Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(context, DetailsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("id", items.get(position).getId());
                intent.putExtra("media_type", items.get(position).getMedia_type());
                context.startActivity(intent); // if in MainActivity can just use startActivity(), no "context."
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    public class SearchViewHolder extends RecyclerView.ViewHolder {
        // Adapter class for initializing
        // the views of our slider view.
        View itemView;
        ImageView resultImage;
        TextView resultTitle;
        TextView resultMedia_type;
        TextView resultRating;
        CardView parentLayout;

        public SearchViewHolder(View itemView) {
            super(itemView);
            resultImage = itemView.findViewById(R.id.resultImg);
            this.itemView = itemView;
            resultTitle = itemView.findViewById(R.id.resultTitle);
            resultMedia_type = itemView.findViewById(R.id.resultMedia_type);
            resultRating = itemView.findViewById(R.id.resultRaing);
            parentLayout = itemView.findViewById(R.id.resultCard);
        }
    }
}
