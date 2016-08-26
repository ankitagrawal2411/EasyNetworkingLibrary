# NetworkRequestWrapper
This Lib is a wrapper for making Rest Request super easy and is similar to retrofit in terms of functionality. 

It Provides inbuilt Gson support too.


Basic Usage:

Initial Global Config:

        new GlobalRequest.Builder().client(new VolleyRequestHandler(getActivity()))
                .converter(new GsonConverter())
                .singleRequest(false)
                .logLevel(LogLevel.ERROR)
                .build();

client(): This method specifies the client to use for sending request , there is no client set by default so have to set one to use this lib,or otherwise an exception will be thrown.

converter(): This method specifies the converter to use if at all you want to use a converter, by default there is no converter added so you have to set one ,or otherwise an exception will be thrown if you try to use converter without setting one.
Note: converter are only required when you dont do the parsing yourself , if you want to parse yourself adding converter is not necessary

singleRequest(): This method allow you to make sure that at a time only one request is in queue of a particular tag , that 2 request of same tag cannot be in queue when singleRequest is set to true , instead a onResponseError will be called with errorCode REQUEST_ALREADY_QUEUED

Request Send Usage:

    new Request.Builder().get().url("your url here").tag("your request tag here")
                                .asJsonObject(new IResponseListener<JSONObject, Data>() {
                                    @Override
                                    public Data onRequestSuccess(JSONObject response) {
                                        return new Gson().fromJson(response.toString(), Data.class);
                                    }

                                    @Override
                                    public void onParseSuccess(Response<Data> response) {
                                    //you get the parsed response here
                                    }

                                    @Override
                                    public void onRequestErrorCode(int errorCode) {

                                    }
                                }).send(getContext());

