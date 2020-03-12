/*
This is a course requirement for CS 192
Software Engineering II under the
supervision of Asst. Prof. Ma. Rowena C.
Solamo of the Department of Computer
Science, College of Engineering, University
of the Philippines, Diliman for the AY 2015-
2016.
*/
package com.example.bingebuddy.ui.home;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.bingebuddy.BuildConfig;
import com.example.bingebuddy.MainActivity;
import com.example.bingebuddy.R;
import com.example.bingebuddy.ui.api.ApiServices;
import com.example.bingebuddy.ui.api.TmdbClient;
import com.example.bingebuddy.ui.api.TraktClient;
import com.example.bingebuddy.ui.shows.Movie;
import com.example.bingebuddy.ui.shows.MovieTraktResponse;
import com.example.bingebuddy.ui.shows.MoviesAdapter;
import com.example.bingebuddy.ui.shows.MoviesResponse;
import com.example.bingebuddy.ui.shows.Show;
import com.example.bingebuddy.ui.shows.ShowAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    private RecyclerView                mRecyclerView;
    private RecyclerView.Adapter        mAdapter;
    private SwipeRefreshLayout          swipeContainer;
    private View root;
    ProgressDialog pd;
    //private RecyclerView.LayoutManager  mLayoutManager;

    //application keys
    private static final String PREF = "testPref";
    private static final String scAccessToken = "AccessToken";
    private SharedPreferences sp;
    private String clientId = "b3825447e716f64c278c9768add7022ebe67644fc9adb4530f343bd8b56b6c6f";
    private String clientSecret = "8bc0fd46dc7ab5b77650ccfd46a4b6ecd618a85dc53bed09875026ce05ba2170";
    private String redirectUri = "bingebuddy://callback";

    //private HomeViewModel homeViewModel;
    public HomeFragment(){}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_home, container, false);
        initShows();

        swipeContainer = root.findViewById(R.id.swiperefresh);
        swipeContainer.setColorSchemeResources(R.color.colorPrimaryDark);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initShows();
                Toast.makeText(getActivity(),"Watchlist Refreshed", Toast.LENGTH_SHORT).show();
                swipeContainer.setRefreshing(false);
                //fetchTimelineAsync(0);
            }
        });
        return root;
    }

    private void initShows(){
        pd = new ProgressDialog(getActivity());
        pd.setMessage("Fetching shows...");
        pd.setCancelable(false);
        pd.show();

        sp = getActivity().getSharedPreferences(PREF, Context.MODE_PRIVATE);
        String accessToken = sp.getString(scAccessToken,"");

        //load from trakt
        TraktClient traktClient = new TraktClient();
        ApiServices apiService = traktClient.getClient().create(ApiServices.class);

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Authorization","Bearer " + accessToken);
        headers.put("trakt-api-version", "2");
        headers.put("trakt-api-key", clientId);

        Call<List<MovieTraktResponse>> call = apiService.getWatchlistMovies(headers);
        call.enqueue(new Callback<List<MovieTraktResponse>>() {
            @Override
            public void onResponse(Call<List<MovieTraktResponse>> call, Response<List<MovieTraktResponse>> response) {
//                if (response.body().size() != 0 ) {
//                    Toast.makeText(getContext(), String.valueOf(response.body().get(0).getTmdbId()), Toast.LENGTH_SHORT).show();
//                }

                //init showList
                List<Show> showList = new ArrayList<>();

                //load watchlist
                List<MovieTraktResponse> traktResponse = response.body();
                for (int i=0;i < traktResponse.size(); i++){
                    Toast.makeText(getContext(),"added 1: "+traktResponse.get(i).getTitle(),Toast.LENGTH_SHORT).show();
                    showList.add(new Show(traktResponse.get(i).getTmdbId(),traktResponse.get(i).getTitle(),"A really awesome movie"));
                }

                mRecyclerView = root.findViewById(R.id.recyclerView);
                mRecyclerView.setHasFixedSize(true);
                mAdapter = new ShowAdapter(getContext(),showList);

                mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                mRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onFailure(Call<List<MovieTraktResponse>> call, Throwable t) {
                if (t instanceof IOException) {
                    Toast.makeText(getContext(), "this is an actual network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                    // logging probably not necessary
                }
                else {
                    Toast.makeText(getContext(), "conversion issue! big problems :(", Toast.LENGTH_SHORT).show();
                    // todo log to some central bug tracking service
                }
            }
        });

//        showList.add(new Show(1,"Captain Marvel","A really awesome movie"));
//        showList.add(new Show(1,"Captain Marvel","A really awesome movie"));
//        showList.add(new Show(1,"Captain Marvel","A really awesome movie"));
        pd.dismiss();
    }

    private void loadDatabase(){
        if (swipeContainer.isRefreshing())
            swipeContainer.setRefreshing(false);
    }
}