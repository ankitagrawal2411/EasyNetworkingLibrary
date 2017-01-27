
### About Easy Networking Library

Easy Networking Library is a powerful library for doing any type of networking in Android applications which allows you to specify clients.

Easy Networking Library takes care of each and everything. So you don't have to do anything, just make request and listen for the response.

It even provides inbuilt Parsing Support.

### Why use Easy Networking Library ?
* Recent removal of HttpClient in Android Marshmallow(Android M) made other networking library obsolete.
* No other single library allow you to specify the client to use(Retrofit removed support for different clients in 2.0).
* You get response parsed so no need to parse the response manually.
* If you are using OkHttpClient then it uses [Okio](https://github.com/square/okio) , No more GC overhead in android application.
  [Okio](https://github.com/square/okio) is made to handle GC overhead while allocating memory.
  [Okio](https://github.com/square/okio) do some clever things to save CPU and memory.
* If you are using OkHttpClient then it supports HTTP/2.  

## Requirements

Easy Android Networking Library can be included in any Android application. 

Easy Android Networking Library supports Android 2.3 (Gingerbread) and later. 

## Using Easy Networking Library in your application

Add this in your build.gradle

Do not forget to add internet permission in manifest if already not present
```xml
<uses-permission android:name="android.permission.INTERNET" />
```
## Basic Usage

### Initial Global Config

```java
new GlobalRequest.Builder().client(new VolleyRequestClient(getActivity()))
                           .converter(new GsonConverter())
                           .singleRequest(false)
                           .logLevel(LogLevel.ERROR)
                           .build();
```

### Methods Used

#### client()
Specifies the client to use for sending request. No client set by default. If you do not specify a client, an exception will be thrown.

#### converter()
Using a converter is optional. A user can specify a custom converter. If no converter is specified, and you used auto parsing an exception will be thrown.

*Note:* Converter is required when you dont want to do the parsing yourself. If you want to parse yourself, adding a converter is not necessary.

#### singleRequest()
This method allows you to make sure that at a time only one request is in queue of a particular tag.
2 request of same tag cannot be in queue when `singleRequest` is set to true. Instead, a `onResponseError` will be called with errorCode `REQUEST_ALREADY_QUEUED`

## Request Send Usage


### Making a GET Request

```java
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
```

### Making a POST Request

```java  
   new RequestBuilder().post(new JSONObject()).url(URL).tag("tag")
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
```

### Using Gson To get Parsing Done Automatically

Requirement is to add a Converter to GlobalBuilder.

```java  
new GlobalRequest.Builder().client(new VolleyRequestClient(getActivity()))
                           .converter(new GsonConverter())
                           .singleRequest(false)
                           .logLevel(LogLevel.ERROR)
                           .build();
```
```java  
new RequestBuilder().get().url("your url here").tag("your request tag here")
                          .asClass(Data.class, new ParsedResponseListener<JSONObject, Data>() {
                              @Override
                              public void onParseSuccess(Response<Data> response) {
                                  Log.e("response", response.response.getRestrictedBrand());
                                  Log.e("response", response.networkTimeMs + " " + response.loadedFrom);
                                  textView.setText("time:"+ response.networkTimeMs+"\n" + "parse time:"+response.parseTime);
                              }

                              @Override
                              public void onRequestErrorCode(int errorCode) {
                                  if(errorCode== ErrorCode.REQUEST_ALREADY_QUEUED){
                                     Log.e("response", "true");
                                  }
                              }
                          }).send(getContext());
```              
               
### Methods Used
 
#### get()
This method indicates that the request is of `GET` type
 
 
#### asClass()
This method specifies that the response is to be auto-parsed into a class with the converter specified in the global request builder 
the library only includes a GsonConverter(). You have to make your own if you want to use any other auto parsing lib other than Gson
 
 
#### asJsonObject()
This method specifies that the response is jsonObject and you are doing the parsing yourself

### Cancelling a request.
Any request with a given tag can be cancelled. Just do like this.

```java
client.cancelPendingRequests("tag"); // All the requests with the given tag will be cancelled.
client.cancelAllRequests();  // All the requests will be cancelled.  
```
