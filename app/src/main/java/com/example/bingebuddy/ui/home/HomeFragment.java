package com.example.bingebuddy.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bingebuddy.R;
import com.example.bingebuddy.ui.shows.Show;
import com.example.bingebuddy.ui.shows.ShowAdapter;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private RecyclerView                mRecyclerView;
    private RecyclerView.Adapter        mAdapter;
    //private RecyclerView.LayoutManager  mLayoutManager;

    //private HomeViewModel homeViewModel;
    public HomeFragment(){}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        List<Show> showList = new ArrayList<>();
        showList.add(new Show(1,"Captain Marvel","A really awesome movie"));
        showList.add(new Show(1,"Captain Marvel","A really awesome movie"));
        showList.add(new Show(1,"Captain Marvel","A really awesome movie"));

        mRecyclerView = (RecyclerView) root.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new ShowAdapter(showList);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mAdapter);
        return root;
    }
}