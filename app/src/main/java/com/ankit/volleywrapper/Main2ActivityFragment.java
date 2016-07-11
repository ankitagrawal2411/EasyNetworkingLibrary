package com.ankit.volleywrapper;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONObject;

/**
 * A placeholder fragment containing a simple view.
 */
public class Main2ActivityFragment extends Fragment {

    public Main2ActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main2, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //for post request
        new RequestBuilder().post(new JSONObject()).url("enter url here").tag("tag").send(getActivity());

        //for get request
        new RequestBuilder().get().url("enter url here").tag("tag").send(getActivity());
    }
}
