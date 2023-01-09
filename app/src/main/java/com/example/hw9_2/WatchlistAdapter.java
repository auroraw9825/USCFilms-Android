package com.example.hw9_2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import java.util.Collections;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.MODE_PRIVATE;


public class WatchlistAdapter extends RecyclerView.Adapter<WatchlistAdapter.WatchlistViewHolder> implements ItemMoveCallback.ItemTouchHelperAdapter{
    private Context context;
    private ArrayList<WatchlistData> items;

    EventListener listener;

    public interface EventListener {
        void showText();
        void showWatchlist();
    }

    public WatchlistAdapter(Context context, ArrayList<WatchlistData> items, EventListener listener) {
        this.context = context;
        this.items = items;
        this.listener = listener; // for calling to the fragment
    }
    public WatchlistAdapter(Context context, EventListener listener) {
        this.context = context;
        this.listener = listener; // for calling to the fragment
    }
    public void setData(ArrayList<WatchlistData> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public WatchlistAdapter.WatchlistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new WatchlistAdapter.WatchlistViewHolder(LayoutInflater.from(context).inflate(R.layout.watchlist_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull WatchlistAdapter.WatchlistViewHolder holder, int position) {
        WatchlistData item = items.get(position);
        String m = item.getMedia_type();
        if (m.equals("movie")){
            holder.watchlist_mediaType.setText("Movie");
        }
        else if (m.equals("tv")){
            holder.watchlist_mediaType.setText("TV");
        }
        String mediaType_id = m + item.getId();

        // Glide is use to load image from url in your imageview.
        Glide.with(holder.itemView)
                .load(item.getPoster_path())
                .fitCenter()
                .into(holder.watchlist_posterPath);

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

        // if click on the remove icon, remove it from the sharedpreference
        holder.removeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedpreferences = context.getSharedPreferences("watchlist", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.remove(mediaType_id);
                editor.apply();
                //editor.commit();
                // Remove from the arraylist
                items.remove(position);

                // update the page
                notifyDataSetChanged();

                //Make the toast
                String toastRemoveString = item.getTitle() + " was removed from Watchlist";
                Toast.makeText(context, toastRemoveString, Toast.LENGTH_SHORT).show();

                // if remove all movies and TVs from watchlist, show the text nothing saved to watchlist
                if(items.size() == 0){
                    listener.showText();
                }

            }
        });

    }


    @Override
    public int getItemCount() {
        return items.size();
    }


    public class WatchlistViewHolder extends RecyclerView.ViewHolder {
        // Adapter class for initializing
        // the views of our slider view.
        View itemView;
        ImageView watchlist_posterPath;
        TextView watchlist_mediaType;
        ImageView removeIcon; // remove icon on the right bottom side of the card
        CardView parentLayout;

        public WatchlistViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            watchlist_posterPath = itemView.findViewById(R.id.watchlist_posterPath);
            watchlist_mediaType = itemView.findViewById(R.id.watchlist_mediaType);
            removeIcon = itemView.findViewById(R.id.removeIcon);
            parentLayout = itemView.findViewById(R.id.watchlist_resultCard);
        }
    }



//    @Override
//    public void onRowMoved(int fromPosition, int toPosition) {
//        // (entire list moves or just 2 items are swapped)
//        // shift every item in the middle
//        // the position here starts with 0
//        if (fromPosition < toPosition) {
//            for  (int i = fromPosition; i < toPosition; i++) {
//                Collections.swap(items, i, i + 1);
//            }
//        } else {
//            for  (int i = fromPosition; i > toPosition; i--) {
//                Collections.swap(items, i, i - 1);
//            }
//        }
//
////        // swap 2 items
////      Collections.swap(items, fromPosition, toPosition);
//        notifyItemMoved(fromPosition,toPosition);
//        notifyDataSetChanged();
//
//
//        // Change the index of each item in arraylist, and in the SharedPreferences
//        SharedPreferences sharedpreferences = context.getSharedPreferences("watchlist", MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedpreferences.edit();
//        for (int i = 0; i < items.size(); i++){
//            WatchlistData oneItem = items.get(i);
//            oneItem.setIndex(String.valueOf(i+1));
//            String itemTitle = oneItem.getTitle();
//            String itemId = oneItem.getId();
//            String itemPoster = oneItem.getPoster_path();
//            String itemMedia = oneItem.getMedia_type();
//            String itemIndex = oneItem.getIndexString();
//            String itemKey = itemMedia + itemId;
//            String itemValue = itemMedia+ " - " + itemId + " - " + itemPoster + " - " + itemTitle + " - " + itemIndex;
//            editor.putString(itemKey, itemValue);
//        }
//        editor.apply();
//
//        // Collections.sort(items);
//
//        // listener.onResume();
//
//    }

    @Override
    public void onRowMoved(int fromPosition, int toPosition) {
        Collections.swap(items, fromPosition, toPosition);
        // notifyItemMoved(fromPosition,toPosition);
        notifyDataSetChanged();

        // Change the index of each item in arraylist, and in the SharedPreferences
        SharedPreferences sharedpreferences = context.getSharedPreferences("watchlist", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        for (int i = 0; i < items.size(); i++){
            WatchlistData oneItem = items.get(i);
            oneItem.setIndex(String.valueOf(i+1));
            String itemTitle = oneItem.getTitle();
            String itemId = oneItem.getId();
            String itemPoster = oneItem.getPoster_path();
            String itemMedia = oneItem.getMedia_type();
            String itemIndex = oneItem.getIndexString();
            String itemKey = itemMedia + itemId;
            String itemValue = itemMedia+ " - " + itemId + " - " + itemPoster + " - " + itemTitle + " - " + itemIndex;
            editor.putString(itemKey, itemValue);
        }
        editor.apply();

        // listener.showWatchlist();

    }

    @Override
    public void onRowSelected(WatchlistViewHolder holder) {

    }
    @Override
    public void onRowClear(WatchlistViewHolder holder) {

    }


}