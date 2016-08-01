package com.ankit.wrapper;

import android.os.AsyncTask;


/**
 * Created by ankitagrawal on 6/7/16. yay
 */
public class ParserTask<F> extends AsyncTask<Object, Integer, Response<F>> {

    private static final String LOG_TAG = ParserTask.class.getSimpleName();

    private String mRequestTag;
    private IParserListener<F> mListener;

    public ParserTask(String requestTag, IParserListener<F> listener) {
        this.mRequestTag = requestTag;
        this.mListener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Response<F> doInBackground(Object... params) {
       return mListener.onParse(mRequestTag);

    }

    @Override
    protected void onPostExecute(Response<F> o) {
        super.onPostExecute(o);

        if(o!=null)
        {
            this.mListener.onParseSuccess(this.mRequestTag, o);
        }else{
            this.mListener.onParseError(this.mRequestTag, ErrorCode.PARSE_ERROR);
        }
    }
    /**
     * Created by ankitagrawal on 6/7/16. yay
     */
    protected interface IParserListener<T> {

        void onParseSuccess(String requestTag, Response<T> object);
        void onParseError(String requestTag, int errorCode);
        Response<T> onParse(String requestTag);
    }
}
