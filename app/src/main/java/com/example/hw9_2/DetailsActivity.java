package com.example.hw9_2;

import android.app.AppComponentFactory;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.borjabravo.readmoretextview.ReadMoreTextView;
import com.bumptech.glide.Glide;
import com.example.hw9_2.ui.home_header_movies.Home_header_movies_Fragment;
import com.example.hw9_2.ui.search.SearchFragment;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.smarteist.autoimageslider.SliderView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

// Created a new activity for Details Page, don't forget to add this activity in manifest file.
public class DetailsActivity extends AppCompatActivity {
    YouTubePlayerView youTubePlayerView;
    ImageView detailImg;
    TextView title;
    ReadMoreTextView overview;
    // TextView overview_showmore;
    TextView genres;
    TextView year;
    ImageView addToWatchlist;
    ImageView removeFromWatchlist;
    ImageView facebookShare;
    ImageView twitterShare;

    TextView castHeaderText;
    RecyclerView castView;
    ArrayList<CastData> castArrayList;

    TextView reviewHeaderText;
    RecyclerView reviewView;
    ArrayList<ReviewData> reviewArrayList;

    SharedPreferences sharedpreferences;

    ProgressDialog nDialog;
    ProgressBar spinnerIcon;
    LinearLayout detailsSpinner;
    ScrollView detailContent;

    ArrayList<HoriScrollData> items = new ArrayList<>();
    RecyclerView recommendView;
    TextView recommendHeaderText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

//        nDialog = new ProgressDialog(this);
//        nDialog.setMessage("Loading..");
//        nDialog.setTitle("Get Data");
//        nDialog.setIndeterminate(false);
//        nDialog.setCancelable(true);
//        nDialog.show();

        // change spinner color to dark blue
        spinnerIcon = findViewById(R.id.spinnerIcon);
        spinnerIcon.getIndeterminateDrawable().setColorFilter(0xFF0b5fb3, android.graphics.PorterDuff.Mode.MULTIPLY);

        getIncomingIntent();
    }

    private void getIncomingIntent(){

        if(getIntent().hasExtra("id") && getIntent().hasExtra("media_type")){

            String id = getIntent().getStringExtra("id");
            String media_type = getIntent().getStringExtra("media_type");

            getDetails(id, media_type);
        }
    }

    private void getDetails(String id, String media_type){
        String url = "http://10.0.2.2:8080/" + media_type + "Details/" + id;
        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response != null) {
//                    dialog.dismiss();//dismiss dialog
                    try {
                        JSONObject res = new JSONObject(response);
                        parseDetails(res, id, media_type);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //display toast
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }

        });

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }

    private void parseDetails(JSONObject res, String id, String media_type) {

        try {
            // YouTube or backdrop_path image, share FaceBook and Twitter
            getYouTubeOrImageAndShare(res, media_type, id);

            // title
            title = findViewById(R.id.title);
            title.setText(res.getString("title"));

            // overview
            overview = findViewById(R.id.overview);
            String s = res.getString("overview");
            if(s.equals("")){
                TextView overviewHeaderText = findViewById(R.id.overviewHeaderText);
                overviewHeaderText.setVisibility(View.GONE);
                overview.setVisibility(View.GONE);
            }
            else{
                overview.setText(s);
            }

            //genres
            genres=findViewById(R.id.genres);
            genres.setText(res.getString("genres"));

            // year
            year=findViewById(R.id.year);
            year.setText(res.getString("release_year"));

            // Add to watchlist, remove from watchlist
            addToWatchlist=findViewById(R.id.addToWatchlist);
            removeFromWatchlist= findViewById(R.id.removewFromWatchlist);
            // find out this movie or TV is in the watchlist, so we know which icon + or - to display
            watchlist(res.getString("title"), media_type, id, res.getString("poster_path")); // title for show toast message



        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Cast
        showCast(media_type, id);

        // Reviews
        showReviews(media_type, id);

        // Recommended picks
        showRecommend(media_type, id);


    }

    // Get YouTube Video, or the backdrop_path image.
    // set the Facebook and Twitter Share button browser page.
    public void getYouTubeOrImageAndShare(JSONObject res, String media_type,  String id){
        // YouTube of backdrop_path image
        youTubePlayerView = findViewById(R.id.youtube_player_view);
        getLifecycle().addObserver(youTubePlayerView);
        detailImg = findViewById(R.id.detailImg);

        // get video information
        String url = "http://10.0.2.2:8080/" + media_type + "Video/" + id;
        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response != null) {
//                    dialog.dismiss();//dismiss dialog
                    try {
                        JSONObject resVideo = new JSONObject(response);
                        String videoKey = resVideo.getString("key");
                        if(!videoKey.equals("tzkWB85ULJY")){ // has a trailer or Teaser
                            // load the video with the key
                            youTubePlayerView.getYouTubePlayerWhenReady(youTubePlayer -> {
                                youTubePlayer.cueVideo(videoKey, 0); // the second parameter is float start time
                            });
                        }
                        else{ // Get teh image
                            youTubePlayerView.setVisibility(View.INVISIBLE);
                            // Glide is use to load image from url in your imageview.
                            Glide.with(getApplicationContext())
                                    .load(res.getString("backdrop_path"))
                                    .fitCenter()
                                    .into(detailImg);
                            detailImg.setVisibility(View.VISIBLE);
                        }

                        // Share on Facebook
                        facebookShare = findViewById(R.id.share_facebook);
                        String FacebookShareLink = "https://www.facebook.com/sharer/sharer.php?u=" + resVideo.getString("FBURL");
                        facebookShare.setOnClickListener(new View.OnClickListener(){
                            public void onClick(View v) {
                                Intent FacebookIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(FacebookShareLink));
                                startActivity(FacebookIntent);
                            }
                        });

                        // Share on Twitter
                        twitterShare = findViewById(R.id.share_twitter);
                        String twitterLink = res.getString("twitterText")+ resVideo.getString("FBURL");
                        twitterShare.setOnClickListener(new View.OnClickListener(){
                            public void onClick(View v) {
                                Intent twitterIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(twitterLink));
                                startActivity(twitterIntent);
                            }
                        });

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //display toast
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }

        });

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);

    }


    // Cast
    public void showCast(String media_type, String id){
        // get cast information
        String url = "http://10.0.2.2:8080/" + media_type + "Cast/" + id;
        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response != null) {
//                    dialog.dismiss();//dismiss dialog
                    try {
                        JSONArray jsonarray = new JSONArray(response); // initialize response json array
                        parseCast(jsonarray);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //display toast
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }

        });

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);

    }

    public void parseCast(@NotNull JSONArray jsonarray){
        castView = findViewById(R.id.castView);
        int numberOfColumns = 3;

        // if no cast data
        if(jsonarray.length() == 0){
            castHeaderText = findViewById(R.id.castHeaderText);
            castHeaderText.setVisibility(TextView.GONE);
            return;
        }

        // if has cast data
//        castHeaderText.setVisibility(TextView.VISIBLE);
        castArrayList = new ArrayList<>(); // every search starts with an empty arraylist
        for (int i = 0; i < jsonarray.length(); i++) {
            try {
                JSONObject object = jsonarray.getJSONObject(i);
                CastData castData = new CastData(object.getString("name"), object.getString("profile_path"));
                castData.setId(object.getInt("id"));
                castData.setCharacter(object.getString("character"));
                castArrayList.add(castData);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        // castView.setHasFixedSize(true);
        // passing this array list inside our adapter class.
        CastAdapter adapter = new CastAdapter(this, castArrayList);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, numberOfColumns);
        castView.setLayoutManager(layoutManager);
        castView.setAdapter(adapter);
    }


    //Reviews
    public void showReviews(String media_type, String id){
        // get cast information
        String url = "http://10.0.2.2:8080/" + media_type + "Review/" + id;
        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response != null) {
                    // nDialog.dismiss();
//                    dialog.dismiss();//dismiss dialog
                    try {
                        JSONArray jsonarray = new JSONArray(response); // initialize response json array
                        parseReviews(jsonarray);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //display toast
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }

    public void parseReviews(@NotNull JSONArray jsonarray){
        reviewView = findViewById(R.id.reviewView);

        // if no cast data
        if(jsonarray.length() == 0){
            reviewHeaderText = findViewById(R.id.reviewHeaderText);
            reviewHeaderText.setVisibility(TextView.GONE);
            return;
        }

        // if has cast data
//        castHeaderText.setVisibility(TextView.VISIBLE);
        reviewArrayList = new ArrayList<>(); // every search starts with an empty arraylist
        for (int i = 0; i < jsonarray.length(); i++) {
            try {
                JSONObject object = jsonarray.getJSONObject(i);
                ReviewData reviewData = new ReviewData(object.getString("author"), object.getString("content"));
                reviewData.setRating(object.getInt("rating"));
                // convert createdTime format
                String year = object.getString("year");
                String month = object.getString("month");
                String date = object.getString("date");
//  A year y is represented by the integer y - 1900.
//  A month is represented by an integer from 0 to 11; 0 is January, 1 is February, and so forth; thus 11 is December.
//  A date (day of month) is represented by an integer from 1 to 31 in the usual manner.
                Date createdOn = new Date(Integer.parseInt(year)-1900, Integer.parseInt(month)-1, Integer.parseInt(date));
                DateFormat df5 = new SimpleDateFormat("E, MMM dd yyyy");
                String str5 = df5.format(createdOn);
                reviewData.setCreatTime(str5);
                reviewArrayList.add(reviewData);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        // passing this array list inside the adapter class.
        ReviewAdapter adapter = new ReviewAdapter(this, reviewArrayList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        reviewView.setLayoutManager(layoutManager);
        reviewView.setAdapter(adapter);
    }


    //Recommend picks
    public void showRecommend(String media_type, String id){
        String m = "";
        if(media_type.equals("movie")){
            m = "Movies";
        }
        else{ //  tv
            m = "TVs";
        }
        String url = "http://10.0.2.2:8080/" + "recommended"  + m  + "/" + id;
        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response != null) {

                    // make Loading spinner disappear
                    detailsSpinner = findViewById(R.id.detail_spinner);
                    detailsSpinner.setVisibility(View.GONE);
                    // make the details content page visible
                    detailContent = findViewById(R.id.detail_content);
                    detailContent.setVisibility(View.VISIBLE);

                    try {
                        JSONArray jsonarray = new JSONArray(response); // initialize response json array
                        parseRecommend(jsonarray, media_type);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //display toast
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);

    }

    private void parseRecommend(JSONArray jsonarray, String media_type) {
        // if no recommend data
        if(jsonarray.length() == 0){
            recommendHeaderText = findViewById(R.id.recommendHeaderText);
            recommendHeaderText.setVisibility(TextView.GONE);
            return;
        }

        for (int i = 0; i < jsonarray.length(); i++) {
            try {
                JSONObject object = jsonarray.getJSONObject(i);
                HoriScrollData data = new HoriScrollData(object.getString("poster_path"), object.getInt("id"), object.getString("title"), media_type);
//                nowData.setImgUrl(object.getString("backdrop_path"));
                //add data in arraylist
                items.add(data);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        recommendView = findViewById(R.id.recommend_view);
        CustomAdapter adapter = new CustomAdapter(this, items);
        recommendView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recommendView.setAdapter(adapter);
    }




    // Watchlist
    // need title for showing toast message
    // need poster_path for showing the image in the watchlist fragment
    public void watchlist(String titleString, String media_type, String id, String poster_path){
        sharedpreferences = getSharedPreferences("watchlist", MODE_PRIVATE);
        String mediaType_id = media_type + id; // the key
        String storedMedia_Id_path_title = media_type + " - " + id + " - " + poster_path + " - "  + titleString; // the value
        Map<String,?> keys = sharedpreferences.getAll();
        String watchlistSize = String.valueOf(keys.size()+1); // the index of the item is adding in the watchlist sharedPreference a
        String valueString = storedMedia_Id_path_title + " - "  + watchlistSize;

        // First, find out this movie or TV is in the watchlist, so we know which icon + or - to display
        // if this movie or TV is in watchlist, then make addToWatchlist GONE
        if(sharedpreferences.contains(mediaType_id)){
            addToWatchlist.setVisibility(View.GONE);
        }
        else{ // this movie or TV is not in watchlist, make removeFromWatchlist GONE
            removeFromWatchlist.setVisibility(View.GONE);
        }

        // Second, set the onClickListener
        // If click on add button
        addToWatchlist.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                // Show remove - button, hide add + button
                removeFromWatchlist.setVisibility(View.VISIBLE);
                addToWatchlist.setVisibility(View.GONE);
                // add to the watchlist
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString(mediaType_id, valueString); // media_type id poster_path_index title
                editor.apply();

                //Make the toast
                String toastAddString = titleString + " was added to Watchlist";
                Toast.makeText(DetailsActivity.this, toastAddString, Toast.LENGTH_LONG).show();
                //LENGTH_SHORT Show the view or text notification for a short period of time.
            }
        });
        // If click on remove button
        removeFromWatchlist.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                // Show add + button, hide remove - button
                removeFromWatchlist.setVisibility(View.GONE);
                addToWatchlist.setVisibility(View.VISIBLE);

                //Remove from the watchlist
//                SharedPreferences.Editor editor = sharedpreferences.edit();
//                editor.remove(mediaType_id);
//                editor.apply();
//                //editor.commit();

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

                // notifyDataSetChanged();


                //Make the toast
                String toastRemoveString = titleString + " was removed from Watchlist";
                Toast.makeText(DetailsActivity.this, toastRemoveString, Toast.LENGTH_LONG).show();
            }
        });
    }


}
