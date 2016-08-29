# NetworkRequestWrapper
This Lib is a wrapper for making Rest Request super easy and is similar to retrofit in terms of functionality. 

It Provides inbuilt Gson support too.


## Basic Usage:

#### Initial Global Config:

```java
new GlobalRequest.Builder().client(new VolleyRequestHandler(getActivity()))
        .converter(new GsonConverter())
        .singleRequest(false)
        .logLevel(LogLevel.ERROR)
        .build();
```

> `client()`: <br>
 This method specifies the client to use for sending request , there is no client set by default so have to set one to use this lib,or otherwise an exception will be thrown.

> `converter()`: <br> 
This method specifies the converter to use if at all you want to use a converter, by default there is no converter added so you have to set one ,or otherwise an exception will be thrown if you try to use converter without setting one.
Note: converter are only required when you dont do the parsing yourself , if you want to parse yourself adding converter is not necessary

> `singleRequest()`: <br> 
This method allow you to make sure that at a time only one request is in queue of a particular tag , that 2 request of same tag cannot be in queue when `singleRequest` is set to true , instead a `onResponseError` will be called with errorCode `REQUEST_ALREADY_QUEUED`

## Request Send Usage:

#### Using JSon Request:-

```java
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
```
 
  
#### Using Gson:-

```java  
new RequestBuilder().get().url("your url here").tag("your request tag here")
                        .asClass(Data.class, new IParsedResponseListener<JSONObject, Data>
                                () {
                            @Override
                            public void onParseSuccess(Response<Data> response) {
                                Log.e("response", response.response.getRestrictedBrand());
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
                        }).send(getContext());
```
                      
               
#### Explanations of Methods:
 
> `get()` :- This method indicates request is of GET type
 
 
> `asClass()` :- <br> This method specifies that the response is to be autoparsed into a class with the converter specified in the global request builder the library only includes a GsonCOnverter() you have to make your own if you want to use any other auto parsing lib other than Gson
 
 
> `asJsonObject()`:- <br> This method specifies that the response is jsonObject and you are doing the parsing yourself
