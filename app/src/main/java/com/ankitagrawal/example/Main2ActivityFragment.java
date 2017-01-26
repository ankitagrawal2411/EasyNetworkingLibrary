package com.ankitagrawal.example;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.ankitagrawal.volleywrapper.VolleyClient;
import com.ankitagrawal.wrapper.ErrorCode;
import com.ankitagrawal.wrapper.GlobalRequest;
import com.ankitagrawal.wrapper.LogLevel;
import com.ankitagrawal.wrapper.ParsedResponseListener;
import com.ankitagrawal.wrapper.RequestBuilder;
import com.ankitagrawal.wrapper.Response;
import com.ankitagrawal.wrapper.ResponseListener;
import com.ankitagrawal.wrapper.interfaces.Builder;
import com.ankitagrawal.converters.GsonConverter;
import com.ankitagrawal.okhttpwrapper.OkHttpClient;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class Main2ActivityFragment extends Fragment {

    public Main2ActivityFragment() {
    }
    private static final String URL = "";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main2, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Spinner spinnerclient = (Spinner) view.findViewById(R.id.spinner_client);
       final TextView textView = (TextView) view.findViewById(R.id.resp);

        new GlobalRequest.Builder().client(new VolleyClient(getActivity()))
                .converter(new GsonConverter())
                .singleRequest(false)
                .logLevel(LogLevel.ERROR)
                .build();
        List<String> clientarray = new ArrayList<>();
        clientarray.add("Volley");
        clientarray.add("okhttp");

        spinnerclient.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout
                .simple_list_item_1, clientarray));
        spinnerclient.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.e("pos", "pos is" + position);
                switch (position) {
                    case 0:
                        GlobalRequest.newBuilder().client(new VolleyClient(getActivity()))
                                .converter(new GsonConverter())
                                .singleRequest(false)
                                .logLevel(LogLevel.ERROR)
                                .build();
                        break;
                    case 1:
                        GlobalRequest.newBuilder().client(new OkHttpClient())
                                .converter(new GsonConverter())
                                .singleRequest(false)
                                .logLevel(LogLevel.ERROR)
                                .build();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        Spinner spinner = (Spinner) view.findViewById(R.id.spinner);
        List<String> array = new ArrayList<>();
        array.add("JSon Get Request");
        array.add("JSon Post Request");
        array.add("GSon Get Request");
        array.add("GSon Post Request");
        array.add("String Get Request");
        array.add("String Post Request");
        array.add("Disk Cached Response");
        array.add("Memory Cache Response");
        array.add("Cache Invalidate Response");
        array.add("Clear Cache");
        spinner.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout
                .simple_list_item_1, array));
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.e("pos","pos is"+position);
                switch (position) {
                    case 0:
                        new RequestBuilder().get().url(URL).tag("tag")
                                .asJsonObject(new ResponseListener<JSONObject, Data>() {
                                    @Override
                                    public Data onRequestSuccess(JSONObject response) {
                                        return new Gson().fromJson(response.toString(), Data.class);
                                    }

                                    @Override
                                    public void onParseSuccess(Response<Data> response) {
                                    }

                                    @Override
                                    public void onRequestErrorCode(int errorCode) {

                                    }
                                }).send(getContext());
                        break;
                    case 1:
                        break;
                    case 2:
                        Builder.BuildOptions req = new RequestBuilder().get
                                ().url
                                (URL).tag("tag")
                                .asClass(Data.class, new ParsedResponseListener<JSONObject, Data>()
                                         {
                                    @Override
                                    public void onParseSuccess(Response<Data> response) {

                                        Log.e("response", response.networkTimeMs + " " + response.loadedFrom);
                                        textView.setText("time:"+ response.networkTimeMs+"\n"+
                                                "parse time:"+response.parseTime);
                                    }

                                    @Override
                                    public void onRequestErrorCode(int errorCode) {
                                          if(errorCode== ErrorCode.REQUEST_ALREADY_QUEUED){
                                              Log.e("response", "true");
                                          }
                                    }
                                }).build();
                        req.send(getContext());
                        req.send(getContext());
                        req.send(getContext());
                        req.send(getContext());
                        break;
                    case 3:
                        break;
                    case 4:
                        new RequestBuilder().get().url(URL).tag("tag")
                                .asString(new ResponseListener<String, Data>() {
                                    @Override
                                    public Data onRequestSuccess(String response) {
                                        return new Gson().fromJson(response, Data.class);
                                    }

                                    @Override
                                    public void onParseSuccess(Response<Data> response) {
                                        Log.e("response", response.networkTimeMs + " " + response.loadedFrom);
                                        textView.setText("time:"+ response.networkTimeMs+"\n"+
                                                "parse time:"+response.parseTime);
                                    }

                                    @Override
                                    public void onRequestErrorCode(int errorCode) {

                                    }
                                }).send(getActivity());
                        break;
                    case 5:
                        break;
                    case 6:
                        new RequestBuilder().get().url(URL).tag("tag").diskCache(true)
                                .cacheTime(15000)
                                .asString(new ResponseListener<String, Data>() {
                                    @Override
                                    public Data onRequestSuccess(String response) {
                                        return new Gson().fromJson(response, Data.class);
                                    }

                                    @Override
                                    public void onParseSuccess(Response<Data> response) {

                                        Log.e("response", response.networkTimeMs + " " + response.loadedFrom);
                                        textView.setText("time:" + response.networkTimeMs + "\n" +
                                                "parse time:" + response.parseTime);
                                    }

                                    @Override
                                    public void onRequestErrorCode(int errorCode) {

                                    }
                                }).send(getContext());
                        break;
                    case 7:
                        new RequestBuilder().get().url(URL).tag("tag").memoryCache(true)
                                .cacheTime(15000)
                                .asString(new ResponseListener<String, Data>() {
                                    @Override
                                    public Data onRequestSuccess(String response) {
                                        return new Gson().fromJson(response, Data.class);
                                    }

                                    @Override
                                    public void onParseSuccess(Response<Data> response) {

                                        Log.e("response", response.networkTimeMs + " " + response.loadedFrom);
                                        textView.setText("time:" + response.networkTimeMs + "\n" +
                                                "parse time:" + response.parseTime);
                                    }

                                    @Override
                                    public void onRequestErrorCode(int errorCode) {

                                    }
                                }).send(getContext());
                        break;
                    case 8:
                        new RequestBuilder().invalidate(getActivity(), "tag");
                        break;
                    case 9:
                        new RequestBuilder().clearCache(getActivity());
                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




    }

    private class Model {
    }
}
