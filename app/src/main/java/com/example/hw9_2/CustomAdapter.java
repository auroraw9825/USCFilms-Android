package com.example.hw9_2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

// For Horizontal cards top rated and popular
public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder> {

    private Context context;
    private ArrayList<HoriScrollData> items;

    public CustomAdapter(Context context, ArrayList<HoriScrollData> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CustomViewHolder(LayoutInflater.from(context).inflate(R.layout.hori_scroll_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        // holder.itemTitle.setText(items.get(position).getTitle());
        // holder.itemImage.setImageResource(items.get(position).getImageUrl());

        final HoriScrollData item = items.get(position);

        // Glide is use to load image
        // from url in your imageview.
        Glide.with(holder.itemView)
                .load(item.getPoster_path())
                .fitCenter()
                .into(holder.itemImage);


        // click on the card, go to detail page
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Toast.makeText(context, items.get(position), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, DetailsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("id", item.getId());
                intent.putExtra("media_type", item.getMedia_type());
                context.startActivity(intent); // if in MainActivity can just use startActivity(), no "context."
            }
        });

        // popup menu
        // if the show more menu icon is clicked
        holder.showMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String media = item.getMedia_type();
                String id = item.getId();
                String poster_path = item.getPoster_path();
                String title = item.getTitle();

                /* Initializing the popup menu and giving the reference as current context */
                PopupMenu popupMenu = new PopupMenu(context, holder.showMenuButton);
                // Inflating popup menu from popup_menu.xml file
                popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());

                // check if should display Remove or Add
                SharedPreferences sharedpreferences = context.getSharedPreferences("watchlist", MODE_PRIVATE);
                String mediaType_id = media + id; // the key
                String storedMedia_Id_path_title = media+ " - " + id + " - " + poster_path + " - " + title;
                MenuItem watchlist_menu_item = popupMenu.getMenu().findItem(R.id.pop_up_watchlist);
                if(sharedpreferences.contains(mediaType_id)){
                    watchlist_menu_item.setTitle("Remove from Watchlist");
                }
                else{ // this movie or TV is not in watchlist, make removeFromWatchlist GONE
                    watchlist_menu_item.setTitle("Add to Watchlist");
                }

                // if an item of the show more menu icon is clicked
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        // Toast message on menu item clicked
                        // Toast.makeText(context, "You Clicked " + menuItem.getTitle(), Toast.LENGTH_SHORT).show();
                        String menuClicked = (String) menuItem.getTitle();
                        if(menuClicked.equals("Open in TMDB")){
                            String url =  "https://www.themoviedb.org/" + media + "/" + id;
                            Intent TMDBIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                            TMDBIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(TMDBIntent);
                        }
                        else if(menuClicked.equals("Add to Watchlist")){
                            // add to the watchlist
                            Map<String,?> keys = sharedpreferences.getAll();
                            String watchlistSize = String.valueOf(keys.size()+1); // the index of the item is adding in the watchlist sharedPreference a
                            String valueString = storedMedia_Id_path_title + " - "  + watchlistSize;
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putString(mediaType_id, valueString); // media_type id poster_path title
                            editor.apply();

                            //Make the toast
                            String toastAddString = title + " was added to Watchlist";
                            Toast.makeText(context, toastAddString, Toast.LENGTH_SHORT).show();
                        }
                        else if(menuClicked.equals("Remove from Watchlist")){
                            // get the index of this item that need to be deleted
                            String value = sharedpreferences.getString(mediaType_id, "");
                            String[] valueArray = value.split(" - ");
                            String indexString  = valueArray[4];
                            int index = Integer.parseInt(indexString);

                            //Remove from the watchlist
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.remove(mediaType_id);

                            // change other keys' index
                            Map<String,?> keys = sharedpreferences.getAll();
                            for(Map.Entry<String,?> entry : keys.entrySet()){
                                String[] l = entry.getValue().toString().split(" - ");
                                int ind = Integer.parseInt(l[4]);
                                if(ind > index){
                                    ind -= 1;
                                    String newIndex = String.valueOf(ind);
                                    String newValue = l[0] + " - " + l[1] + " - " + l[2] + " - " + l[3] + " - " + newIndex;
                                    editor.putString(entry.getKey(), newValue);
                                }
                                // Log.d("watchlist values",entry.getKey() + ": " + entry.getValue().toString());
                            }

                            editor.apply();
                            //editor.commit();

                            //Make the toast
                            String toastRemoveString = title + " was removed from Watchlist";
                            Toast.makeText(context, toastRemoveString, Toast.LENGTH_SHORT).show();
                        }
                        else if(menuClicked.equals("Share on Facebook")){
                            String url = "https://www.facebook.com/sharer/sharer.php?u=" + "https%3A%2F%2Fwww.themoviedb.org%2F" + media + "%2F" + id;;
                            Intent FacebookIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                            FacebookIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(FacebookIntent);
                        }
                        else if(menuClicked.equals("Share on Twitter")){
                            // share TMDB page on Twitter
                            String url = "https://twitter.com/intent/tweet?text=Check%20this%20out!%20" + "https%3A%2F%2Fwww.themoviedb.org%2F" + media + "%2F" + id;
                            Intent twitterIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                            twitterIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(twitterIntent);
                        }
//                        else{ // Facebook and Twitter share, share the title and trailer
//                            share_facebook_twitter(media, id, menuClicked);
//                            //get Details
//                            //get Video and facebook twitter share url
//                        }
                        return true;
                    }
                });
                // Showing the popup menu
                popupMenu.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

//    public class CustomViewHolder extends RecyclerView.ViewHolder {
//
//        private ImageView itemImage;
//        private TextView itemTitle;
//
//        public CustomViewHolder(View view) {
//            super(view);
//            itemImage = view.findViewById(R.id.item_image);
//            itemTitle = view.findViewById(R.id.item_title);
//        }
//    }
    public class CustomViewHolder extends RecyclerView.ViewHolder {
        // Adapter class for initializing
        // the views of our slider view.
        View itemView;
        ImageView itemImage;
        CardView parentLayout;
        ImageView showMenuButton;
       // private TextView itemTitle;

        public CustomViewHolder(View itemView) {
            super(itemView);
            itemImage = itemView.findViewById(R.id.item_image);
            this.itemView = itemView;
            // itemTitle = itemView.findViewById(R.id.item_title);
            parentLayout=itemView.findViewById(R.id.horiCard);
            showMenuButton = itemView.findViewById(R.id.showMenuButton);
        }
    }


//    // Facebook and Twitter share, share the title and trailer
//    private void share_facebook_twitter(String media_type, String id, String menuClicked){
//        String url = "http://10.0.2.2:8080/" + media_type + "Details/" + id;
////        final String[] shareURL = new String[2];// final one element array, 0 facebook, 1 twitter
//        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                if (response != null) {
////                    dialog.dismiss();//dismiss dialog
//                    try {
//                        JSONObject res = new JSONObject(response);
//                        getVideo(res, media_type, id, menuClicked);
////                        shareURL[0] = ""; // facebook
////                        shareURL[1] = res.getString("twitterText");  // twitter
//
////                        Intent TMDBIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("www.google.com"));
////                        TMDBIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////                        context.startActivity(TMDBIntent);
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                //display toast
//                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
//            }
//
//        });
//
//        // Instantiate the RequestQueue.
//        RequestQueue queue = Volley.newRequestQueue(context);
//        queue.add(request);
//
//
//    }
//    public void getVideo(JSONObject res, String media_type, String id, String menuClicked){
//        // get video information
//        String url = "http://10.0.2.2:8080/" + media_type + "Video/" + id;
//        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                if (response != null) {
////                    dialog.dismiss();//dismiss dialog
//                    try {
//                        JSONObject resVideo = new JSONObject(response);
//                        // Facebook Share URL
//                        String FacebookShareLink = "https://www.facebook.com/sharer/sharer.php?u=" + resVideo.getString("FBURL");
//                        // Twitter Share URL
//                        String twitterLink = res.getString("twitterText")+ resVideo.getString("FBURL");
//
//                        if(menuClicked.equals("Share on Facebook")){
//                            Intent FacebookIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(FacebookShareLink));
//                            FacebookIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            context.startActivity(FacebookIntent);
//                        }
//                        else if(menuClicked.equals("Share on Twitter")){
//                            Intent twitterIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(twitterLink));
//                            twitterIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            context.startActivity(twitterIntent);
//                        }
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                //display toast
//                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
//            }
//
//        });
//
//        // Instantiate the RequestQueue.
//        RequestQueue queue = Volley.newRequestQueue(context);
//        queue.add(request);
//
//    }
}
