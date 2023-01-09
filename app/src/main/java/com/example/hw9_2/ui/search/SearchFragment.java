package com.example.hw9_2.ui.search;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
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
import com.example.hw9_2.HoriScrollData;
import com.example.hw9_2.R;
import com.example.hw9_2.SearchAdapter;
import com.example.hw9_2.SearchData;
import com.example.hw9_2.SliderAdapter;
import com.example.hw9_2.SliderData;
import com.smarteist.autoimageslider.SliderView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SearchFragment extends Fragment {

    private SearchViewModel searchViewModel;

    ArrayList<SearchData> searchResultArrayList;
    SearchView searchView;
    RecyclerView searchResultView;
    TextView noResult;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        searchViewModel = new ViewModelProvider(this).get(SearchViewModel.class);
        View root = inflater.inflate(R.layout.fragment_search, container, false);
//        final TextView textView = root.findViewById(R.id.text_search);
//        searchViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });



        noResult = root.findViewById(R.id.noResultFoundText); // show up when no result found.

        searchView = root.findViewById(R.id.search_view); // the will for toll bar and searchView
        searchView.requestFocusFromTouch(); // automatically focus with the cursor on hte screen

        searchResultView = root.findViewById(R.id.searchResult); // the view for result cards recyclerView

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) { // the new content of the query text field.
                //need to check if the string s is a empty string, or will cause error!!!!!!!
                Log.d("111", s+"111");
                if(s.length() == 0) { //  if s=="" after delete the last query
                    return false;
                }
                else{
                    String searchUrl = "http://10.0.2.2:8080/multiSearch/" + s;
                    StringRequest request = new StringRequest(searchUrl, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response != null) {
//                    dialog.dismiss();//dismiss dialog
                                try {
                                    JSONArray jsonarray = new JSONArray(response); // initialize response json array
                                    parseSearchResults(jsonarray);
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


                    //	false if the SearchView should perform the default action of showing any suggestions if available,
                    //	true if the action was handled by the listener.
                    return true;
                }

            }
        });


        return root;
    }

    private void parseSearchResults(JSONArray jsonarray){
        if(jsonarray.length() == 0){
            noResult.setVisibility(TextView.VISIBLE);
            searchResultView.setVisibility(View.INVISIBLE); // hide the result recycler view
            return;
        }

        noResult.setVisibility(TextView.INVISIBLE); // when have results, make this textView invisible.
        searchResultView.setVisibility(View.VISIBLE);
        searchResultArrayList = new ArrayList<>(); // every search starts with an empty arraylist
        for (int i = 0; i < jsonarray.length(); i++) {
            try {
                JSONObject object = jsonarray.getJSONObject(i);
                SearchData searchData = new SearchData(object.getInt("id"), object.getString("title"), object.getString("backdrop_path"));
                searchData.setMedia_type(object.getString("media_type"));
                searchData.setRating(object.getInt("vote_average"));
                searchData.setRelease_year(object.getString("release_year"));
                searchResultArrayList.add(searchData);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        searchResultView.setHasFixedSize(true);
        // passing this array list inside our adapter class.
        SearchAdapter adapter = new SearchAdapter(getActivity().getApplicationContext(), searchResultArrayList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        searchResultView.setLayoutManager(layoutManager);
        searchResultView.setAdapter(adapter);

    }
}