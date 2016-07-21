package com.ankit.volleywrapper;


import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import org.json.JSONException;
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
        GlobalRequest.newBuilder().setRequestManager(new VolleyRequestHandler(getActivity()));
        TelephonyManager telephonyManager = (TelephonyManager) getActivity()
                .getSystemService(Context.TELEPHONY_SERVICE);
        JSONObject params = new JSONObject();
        try {
            params.put("XBkey", "XB123");
            params.put("UserID", "7750");
            params.put("Password", "123");
            params.put("DeviceID", telephonyManager.getDeviceId());
            params.put("AppType", "UA");
            params.put("AppVersion", "0.6");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //for post request
        new RequestBuilder().post(params).url("http://192.168.0.51:804/LogisticAndroidService.svc/DeliveryUserValidation").tag("tag").asGsonObject(LoginModel.class,new
                                                                                                                                                           IRequestListener<LoginModel>() {


            @Override
            public Object onRequestSuccess(LoginModel response) {
                return null;
            }

            @Override
            public void onParseSuccess(Object response) {

            }

            @Override
            public LoginModel onNetworkResponse(NetworkResponse response) {
                return null;
            }

            @Override
            public void onRequestErrorCode(int errorCode) {

            }
        }).send
                (getActivity());

    }
}
