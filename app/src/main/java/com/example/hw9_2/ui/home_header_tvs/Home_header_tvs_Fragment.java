package com.example.hw9_2.ui.home_header_tvs;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.hw9_2.CustomAdapter;
import com.example.hw9_2.HoriScrollData;
import com.example.hw9_2.R;
import com.example.hw9_2.SliderAdapter;
import com.example.hw9_2.SliderData;
import com.example.hw9_2.ui.home_header_movies.Home_header_movies_ViewModel;
import com.smarteist.autoimageslider.SliderView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Home_header_tvs_Fragment extends Fragment {

    private Home_header_tvs_ViewModel home_header_tvs_ViewModel;

    SliderView sliderView;
    // we are creating array list for storing our image urls.
    ArrayList<SliderData> sliderDataArrayList = new ArrayList<>();

    //Top rated
    RecyclerView recyclerView;
    // top rated movies horizontal scroll view
    ArrayList<HoriScrollData> items = new ArrayList<>();

    // Popoular
    RecyclerView popularView;
    ArrayList<HoriScrollData> items_popular = new ArrayList<>();

    // Footer
    TextView footer;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.home_header_tv_page, container, false);

        // initializing the slider view.
        sliderView = root.findViewById(R.id.slider);
        // Show the now Playing slider
        showSlider();

        // Top Rated movie
        recyclerView = root.findViewById(R.id.recycler_view);
        showTopRatedMovies();

        // Popular movie
        popularView = root.findViewById(R.id.popular_view);
        showPopularMovies();

        // footer
        footer = root.findViewById(R.id.footerLink);
        showFooter();


        return root;
    }

    private void showSlider() {
        String url = "http://10.0.2.2:8080/trendingTVs";
        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Log.d("request", "in onResponse");
                if (response != null) {
//                    dialog.dismiss();//dismiss dialog
                    try {
                        JSONArray jsonarray = new JSONArray(response); // initialize response json array
                        parseSliderArray(jsonarray);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //display toast
                Toast.makeText(getActivity().getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }

        });

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        queue.add(request);


    }

    private void parseSliderArray(JSONArray jsonarray) {
        for (int i = 0; i < jsonarray.length(); i++) {
            try {
                JSONObject object = jsonarray.getJSONObject(i);
                SliderData nowData = new SliderData("tv", object.getInt("id"), object.getString("poster_path"));
//                nowData.setImgUrl(object.getString("backdrop_path"));
                //add data in arraylist
                sliderDataArrayList.add(nowData);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        // passing this array list inside our adapter class.
        SliderAdapter adapter = new SliderAdapter(getActivity(), sliderDataArrayList);

        // below method is used to
        // setadapter to sliderview.
        sliderView.setSliderAdapter(adapter);
        // below method is used to set auto cycle direction in left to
        // right direction you can change according to requirement.
        sliderView.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);

        // below method is used to
        // setadapter to sliderview.
        sliderView.setSliderAdapter(adapter);

        // below method is use to set
        // scroll time in seconds.
        sliderView.setScrollTimeInSec(3); // or set it to 6?

        // to set it scrollable automatically
        // we use below method.
        sliderView.setAutoCycle(true);

        // to start autocycle below method is used.
        sliderView.startAutoCycle();
    }


    private void showTopRatedMovies() {
        String url = "http://10.0.2.2:8080/topRatedTVs";
        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response != null) {
//                    dialog.dismiss();//dismiss dialog
                    try {
                        JSONArray jsonarray = new JSONArray(response); // initialize response json array
                        parseHoriScrollArray(jsonarray);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //display toast
                Toast.makeText(getActivity().getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }

        });

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        queue.add(request);


    }

    private void parseHoriScrollArray(JSONArray jsonarray) {
        for (int i = 0; i < jsonarray.length(); i++) {
            try {
                JSONObject object = jsonarray.getJSONObject(i);
                HoriScrollData data = new HoriScrollData(object.getString("poster_path"), object.getInt("id"), object.getString("title"), "tv");
//                nowData.setImgUrl(object.getString("backdrop_path"));
                //add data in arraylist
                items.add(data);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }


        CustomAdapter adapter = new CustomAdapter(getActivity(), items);


        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        recyclerView.setAdapter(adapter);

// let’s create 10 random items

//        for (int i = 0; i < 10; i++) {
//            items.add(new HoriScrollData(R.drawable.ic_notifications_black_24dp, "Title " + i));
//            adapter.notifyDataSetChanged();
//        }

    }

    // Popular movies
    private void showPopularMovies() {
        String url = "http://10.0.2.2:8080/popularTVs";
        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response != null) {
//                    dialog.dismiss();//dismiss dialog
                    try {
                        JSONArray jsonarray = new JSONArray(response); // initialize response json array
                        parseHoriScrollPopularArray(jsonarray);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //display toast
                Toast.makeText(getActivity().getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }

        });

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        queue.add(request);
    }
    private void parseHoriScrollPopularArray(JSONArray jsonarray) {
        for (int i = 0; i < jsonarray.length(); i++) {
            try {
                JSONObject object = jsonarray.getJSONObject(i);
                HoriScrollData data = new HoriScrollData(object.getString("poster_path"), object.getInt("id"), object.getString("title"), "tv");
//                nowData.setImgUrl(object.getString("backdrop_path"));
                //add data in arraylist
                items_popular.add(data);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        CustomAdapter adapter = new CustomAdapter(getActivity(), items_popular);

        popularView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        popularView.setAdapter(adapter);
    }

    private void showFooter(){
        footer.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View root){
                String url="https://www.themoviedb.org/"; // https%3A%2F%2Fwww.themoviedb.org%2F
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            }
        });
    }
}




