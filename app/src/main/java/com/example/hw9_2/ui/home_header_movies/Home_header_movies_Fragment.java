package com.example.hw9_2.ui.home_header_movies;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.hw9_2.CustomAdapter;
import com.example.hw9_2.HoriScrollData;
import com.example.hw9_2.SliderAdapter;
import com.example.hw9_2.SliderData;
import com.smarteist.autoimageslider.SliderView;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hw9_2.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

public class Home_header_movies_Fragment extends Fragment {
//    private Context mContext;

    String API_KEY = "f9acad7a4b064909be30cfd5e0064bf4";

    View root;

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
    private Home_header_movies_ViewModel home_header_movies_ViewModel;

    LinearLayout homeMovieSpinner;

    // loading screen spinner
//    ProgressDialog nDialog;

//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        mContext=context;
//    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

//        home_header_movies_ViewModel =
//                new ViewModelProvider(this).get(Home_header_movies_ViewModel.class);
        root = inflater.inflate(R.layout.home_header_movie_page, container, false);
//        final TextView textView = root.findViewById(R.id.text_notifications);
//        home_header_movies_ViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
//        return root;


//
//        nDialog = new ProgressDialog(getActivity());
//        nDialog.setMessage("Loading..");
//        nDialog.setTitle("Get Data");
//        nDialog.setIndeterminate(false);
//        nDialog.setCancelable(true);
//        nDialog.show();

        homeMovieSpinner = root.findViewById(R.id.home_movie_spinner1);
        ProgressBar spinnerIcon = root.findViewById(R.id.home_movie_spinnerIcon1);
        spinnerIcon.getIndeterminateDrawable().setColorFilter(0xFF0b5fb3, PorterDuff.Mode.MULTIPLY);

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

        // shouldn't set visibility here, 因为异步调用，所以return root在实际request之前
//        // make Loading spinner disappear
//        homeMovieSpinner = root.findViewById(R.id.home_movie_spinner);
//        homeMovieSpinner.setVisibility(View.GONE);
//        // make the details content page visible
//        sliderView.setVisibility(View.VISIBLE);
//        TextView movieText1 = root.findViewById(R.id.movieText1);
//        movieText1.setVisibility(View.VISIBLE);

        return root;
    }

//    public void onResume(){
//        super.onResume();
//        // initializing the slider view.
//        sliderView = root.findViewById(R.id.slider);
//        // Show the now Playing slider
//        showSlider();
//
//        // Top Rated movie
//        recyclerView = root.findViewById(R.id.recycler_view);
//        showTopRatedMovies();
//
//        // Popular movie
//        popularView = root.findViewById(R.id.popular_view);
//        showPopularMovies();
//
//        // footer
//        footer = root.findViewById(R.id.footerLink);
//        showFooter();
//
//        // make Loading spinner disappear
//        homeMovieSpinner = root.findViewById(R.id.home_movie_spinner);
//        homeMovieSpinner.setVisibility(View.GONE);
//        // make the details content page visible
//        sliderView.setVisibility(View.VISIBLE);
//        TextView movieText1 = root.findViewById(R.id.movieText1);
//        movieText1.setVisibility(View.VISIBLE);
//    }

    private void showSlider() {
        String url = "http://10.0.2.2:8080/currentPlayingMovies";  // https://rongwmovie.wl.r.appspot.com/currentPlayingMovies
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
                SliderData nowData = new SliderData("movie", object.getInt("id"), object.getString("poster_path"));
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
        sliderView.setScrollTimeInSec(3);

        // to set it scrollable automatically
        // we use below method.
        sliderView.setAutoCycle(true);

        // to start autocycle below method is used.
        sliderView.startAutoCycle();
    }


    private void showTopRatedMovies() {
        String url = "http://10.0.2.2:8080/topRatedMovies"; // https://rongwmovie.wl.r.appspot.com/topRatedMovies
        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("request", "in onResponse");
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
                HoriScrollData data = new HoriScrollData(object.getString("poster_path"), object.getInt("id"), object.getString("title"), "movie");
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
    }

    // Popular movies
    private void showPopularMovies() {
        String url = "http://10.0.2.2:8080/popularMovies";
        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Log.d("request", "in onResponse");
                if (response != null) {
//                    dialog.dismiss();//dismiss dialog

                    // here, because异步,return root后这个request还没被call
                    // make Loading spinner disappear
                    homeMovieSpinner.setVisibility(View.GONE);
                    // make the home movie content page visible
                    sliderView.setVisibility(View.VISIBLE);
                    TextView movieText1 = root.findViewById(R.id.movieText1);
                    movieText1.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.VISIBLE);

                    TextView movieText2 = root.findViewById(R.id.movieText2);
                    movieText2.setVisibility(View.VISIBLE);
                    popularView.setVisibility(View.VISIBLE);

                    footer.setVisibility(View.VISIBLE);
                    TextView footText = root.findViewById(R.id.footerText);
                    footText.setVisibility(View.VISIBLE);

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
                HoriScrollData data = new HoriScrollData(object.getString("poster_path"), object.getInt("id"), object.getString("title"), "movie");
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