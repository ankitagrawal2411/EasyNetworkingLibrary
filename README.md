# NetworkRequestWrapper
NetworkRequestWrapper provides a wrapper for making Rest requests easy. It is similar to retrofit in terms of functionality. 

It provides inbuilt Gson support.

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
Using a converter is optional. A user can specify a custom converter. If no converter is specified, the library will use the default `GsonConverter`.

*Note:* Converter is required when you dont want to do the parsing yourself. If you want to parse yourself, adding a converter is not necessary.

#### singleRequest()
This method allows you to make sure that at a time only one request is in queue of a particular tag.
2 request of same tag cannot be in queue when `singleRequest` is set to true. Instead, a `onResponseError` will be called with errorCode `REQUEST_ALREADY_QUEUED`

## Request Send Usage

### Using JSon Request

```java
new Request.Builder().get().url("your url here").tag("your request tag here")
                           .asJsonObject(new ResponseListener<JSONObject, Data>() {
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
 
  
### Using Gson

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
