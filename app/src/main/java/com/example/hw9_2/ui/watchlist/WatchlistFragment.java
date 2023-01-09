package com.example.hw9_2.ui.watchlist;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.hw9_2.CastAdapter;
import com.example.hw9_2.CastData;
import com.example.hw9_2.ItemMoveCallback;
import com.example.hw9_2.R;
import com.example.hw9_2.WatchlistAdapter;
import com.example.hw9_2.WatchlistData;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;


public class WatchlistFragment extends Fragment implements WatchlistAdapter.EventListener{
// add implements WatchlistAdapter.EventListener, and in adapter add the listener, so we can call from adapter to the fragment.
    private WatchlistViewModel watchlistViewModel;

    SharedPreferences sharedpreferences;
    ArrayList<WatchlistData> watchlistArrayList = new ArrayList<>();
    WatchlistAdapter adapter;

    RecyclerView watchlistView;
    TextView nothingWatch;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        watchlistViewModel =
                new ViewModelProvider(this).get(WatchlistViewModel.class);
        View root = inflater.inflate(R.layout.fragment_watchlist, container, false);
//        final TextView textView = root.findViewById(R.id.text_watchlist);
//        watchlistViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });

        watchlistView = root.findViewById(R.id.watchlistView);
        nothingWatch = root.findViewById(R.id.nothingWatch);

        int numberOfColumns = 3;
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity().getApplicationContext(), numberOfColumns);
        watchlistView.setLayoutManager(layoutManager);
        adapter = new WatchlistAdapter(getActivity().getApplicationContext(),this);
//        watchlistView.setAdapter(adapter);

        // Dragging features
        ItemTouchHelper.Callback callback = new ItemMoveCallback(adapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(watchlistView);

        // showWatchlist();

        return root;
    }


    public void showWatchlist(){
        // ArrayList<WatchlistData> watchlistArrayList = new ArrayList<>();
        // int numberOfColumns = 3;

        sharedpreferences = this.getActivity().getSharedPreferences("watchlist", MODE_PRIVATE); // this.getActivity()
        Map<String,?> keys = sharedpreferences.getAll();
//        if(keys.size()==0){
//            //show text Nothing saved to Watchlist
//            nothingWatch.setVisibility(View.VISIBLE);
//            return;
//        }

        for(Map.Entry<String,?> entry : keys.entrySet()){
            String value = entry.getValue().toString();
            String[] l = value.split(" - "); // title may contains space, so use " - " to separate
            String media_type = l[0];
            String id = l[1];
            String poster_path = l[2];
            String title = l[3]; // title may contains space, so use ",,," to separate
            String index = l[4];
            WatchlistData data = new WatchlistData(id, media_type, poster_path, title);
            data.setIndex(index);
            watchlistArrayList.add(data);

            // Log.d("watchlist values",entry.getKey() + ": " + entry.getValue().toString());
        }

        // sort the arraylist each item's index
        Collections.sort(watchlistArrayList);

//        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity().getApplicationContext(), numberOfColumns);
//        watchlistView.setLayoutManager(layoutManager);
        //adapter = new WatchlistAdapter(getActivity().getApplicationContext(), watchlistArrayList, this);
        adapter.setData(watchlistArrayList);
        watchlistView.setAdapter(adapter);

        if(keys.size()==0){
            //show text Nothing saved to Watchlist
            nothingWatch.setVisibility(View.VISIBLE);
            return;
        }

        // Dragging features
//        ItemTouchHelper.Callback callback = new ItemMoveCallback(adapter);
//        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
//        touchHelper.attachToRecyclerView(watchlistView);

    }

    public void showText(){ //when delete all movies and TVs from watchlist, show the text nothing saved to watchlist
        nothingWatch.setVisibility(View.VISIBLE);
    }


    @Override
    public void onResume() {
        super.onResume();
        // if from the watchlist page, open detail page, then remove from watchlist
        // when close the details page, go back to the watchlist page, need to update the watchlist view.
        watchlistArrayList.clear();
        showWatchlist();
        // adapter.notifyDataSetChanged();

    }



}