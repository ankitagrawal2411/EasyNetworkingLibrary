package com.ankit.volleywrapper;

import android.os.AsyncTask;


/**
 * Created by ankit.agrawal on 7/24/2015.
 */
public class ParserTask extends AsyncTask<Object, Integer, Object> {

    private static final String LOG_TAG = ParserTask.class.getSimpleName();

    private String mRequestTag;
    private IParserListener mListener;

    public ParserTask(String requestTag, IParserListener listener) {
        this.mRequestTag = requestTag;
        this.mListener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Object doInBackground(Object... params) {
       return mListener.onParse(mRequestTag);

    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);

        if(o!=null)
        {
            this.mListener.onParseSuccess(this.mRequestTag, o);
        }else{
            this.mListener.onParseError(this.mRequestTag, ErrorCode.VOLLEY_PARSE_ERROR);
        }
    }
}
