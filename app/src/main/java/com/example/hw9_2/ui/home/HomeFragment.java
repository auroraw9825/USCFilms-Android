package com.example.hw9_2.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.hw9_2.R;
import com.example.hw9_2.ui.home_header_movies.Home_header_movies_Fragment;
import com.example.hw9_2.ui.home_header_tvs.Home_header_tvs_Fragment;
import android.util.Log;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    LinearLayout homeSpinner;
    LinearLayout homeHearder;

    TextView b1;
    TextView b2;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);


        //这一段是针对Homepage header movie, tv tabs
        b1 = root.findViewById(R.id.home_header_button_movie);
        // b1.setPressed(true);
        b1.setSelected(true); // text color change because in layout_home: android:textColor="@drawable/home_header_tab_text_color"
        Home_header_movies_Fragment home_header_movies_fragment = new Home_header_movies_Fragment();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.home_content, home_header_movies_fragment);
        transaction.commit();

        b2 = root.findViewById(R.id.home_header_button_tv);
        b2.setSelected(false);

//        // hide the loading screen spinner
//        progress_loader = root.findViewById(R.id.progress_loader);
//        progress_loader.setVisibility(View.GONE);

        b1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View root){
//                b1.setPressed(true);
//                b2.setPressed(false);
                b1.setSelected(true);
                b2.setSelected(false);
                Home_header_movies_Fragment home_header_movies_fragment = new Home_header_movies_Fragment();
                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                transaction.replace(R.id.home_content, home_header_movies_fragment);
                transaction.commit();
            }
        });
        b2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View root){
//                b2.setPressed(true);
//                b1.setPressed(false);
                b2.setSelected(true);
                b1.setSelected(false);
                Home_header_tvs_Fragment home_header_tvs_fragment = new Home_header_tvs_Fragment();
                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                transaction.replace(R.id.home_content, home_header_tvs_fragment);
                transaction.commit();
            }
        });
        //end for Homepage Movie, TV tabs

        //让home一进来就是movie的页面
//        FrameLayout homeContent = root.findViewById(R.id.home_content);
//        homeContent.setSelectedItemId(R.id.home_header_button_movie);//当做一进来movie button就被选中了


        final TextView textView = root.findViewById(R.id.text_home);
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });


//        // make Loading spinner disappear
//        homeSpinner = root.findViewById(R.id.home_spinner1);
//        homeSpinner.setVisibility(View.GONE);
//        // make the details content page visible
//        homeHearder = root.findViewById(R.id.linearLayout);
//        homeHearder.setVisibility(View.VISIBLE);

        return root;
    }
}