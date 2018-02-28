package com.asmat.rolando.popularmovies.activities;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.asmat.rolando.popularmovies.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class SearchResultsActivityFragment extends Fragment {

    public SearchResultsActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search_results, container, false);
    }
}
