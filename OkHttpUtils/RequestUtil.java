package com.example.okhttpdemo.okhttp;

import android.text.TextUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;

class RequestUtil {
    private String mMethodType; // 請求方式
    private String mUrl; // URL
    private Map<String, String> mParamsMap; // key/value參數, 有分post和get
    private String mJsonStr; // JSON類型的參數, post
    private File mFile; // 檔案的參數, post
    private List<File> mFileList; // 檔案list, post
    private String mFileKey; // 上傳檔案的key
    private Map<String, File> mFileMap; // 檔案的map
    private String mFileType; // 檔案類型
    private Map<String, String> mHeaderMap; // header參數
    private CallBackUtil mCallBack; // callback
    private OkHttpClient mOkHttpClient; // OkHttpClient實體
    private Request mOkHttpRequest; // Request實體
    private Request.Builder mRequestBuilder; //Request.Builder實體


    RequestUtil(String methodType, String url, Map<String, String> paramsMap, Map<String, String> headerMap, CallBackUtil callBack) {
        this(methodType, url, null, null, null, null, null, null, paramsMap, headerMap, callBack);
    }

    RequestUtil(String methodType, String url, String jsonStr, Map<String, String> headerMap, CallBackUtil callBack) {
        this(methodType, url, jsonStr, null, null, null, null, null, null, headerMap, callBack);
    }

    RequestUtil(String methodType, String url, Map<String, String> paramsMap, File file, String fileKey, String fileType, Map<String, String> headerMap, CallBackUtil callBack) {
        this(methodType, url, null, file, null, fileKey, null, fileType, paramsMap, headerMap, callBack);
    }

    RequestUtil(String methodType, String url, Map<String, String> paramsMap, List<File> fileList, String fileKey, String fileType, Map<String, String> headerMap, CallBackUtil callBack) {
        this(methodType, url, null, null, fileList, fileKey, null, fileType, paramsMap, headerMap, callBack);
    }

    RequestUtil(String methodType, String url, Map<String, String> paramsMap, Map<String, File> fileMap, String fileType, Map<String, String> headerMap, CallBackUtil callBack) {
        this(methodType, url, null, null, null, null, fileMap, fileType, paramsMap, headerMap, callBack);
    }

    private RequestUtil(String methodType, String url, String jsonStr, File file, List<File> fileList, String fileKey, Map<String, File> fileMap, String fileType, Map<String, String> paramsMap, Map<String, String> headerMap, CallBackUtil callBack) {
        mMethodType = methodType;
        mUrl = url;
        mJsonStr = jsonStr;
        mFile = file;
        mFileList = fileList;
        mFileKey = fileKey;
        mFileMap = fileMap;
        mFileType = fileType;
        mParamsMap = paramsMap;
        mHeaderMap = headerMap;
        mCallBack = callBack;
        getInstance();
    }


    /**
     * 建立OkHttpClient實體
     */
    private void getInstance() {
        mOkHttpClient = new OkHttpClient();
        mRequestBuilder = new Request.Builder();
        if (mFile != null || mFileList != null || mFileMap != null) {//先判断是否有文件，
            setFile();
        } else {
            // 設定參數
            switch (mMethodType) {
                case OkHttpUtil.METHOD_GET:
                    setGetParams();
                    break;
                case OkHttpUtil.METHOD_POST:
                    mRequestBuilder.post(getRequestBody());
                    break;
                case OkHttpUtil.METHOD_PUT:
                    mRequestBuilder.put(getRequestBody());
                    break;
                case OkHttpUtil.METHOD_DELETE:
                    mRequestBuilder.delete(getRequestBody());
                    break;
            }
        }
        mRequestBuilder.url(mUrl);
        if (mHeaderMap != null) {
            setHeader();
        }
        //mRequestBuilder.addHeader("Authorization","Bearer "+"token"); // 設定token
        mOkHttpRequest = mRequestBuilder.build();
    }

    /**
     * 取得body
     */
    private RequestBody getRequestBody() {
        /**
         * 判斷mJsonStr是否為空,由於mJsonStr與mParamsMap不可能同時存在,所以先判斷mJsonStr
         */
        if (!TextUtils.isEmpty(mJsonStr)) {
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");//数据类型为json格式，
            return RequestBody.create(JSON, mJsonStr);//json数据，
        }

        /**
         * post,put,delete都需要body, 就算沒參數也要給空的body
         */
        FormBody.Builder formBody = new FormBody.Builder();
        if (mParamsMap != null) {
            for (String key : mParamsMap.keySet()) {
                formBody.add(key, mParamsMap.get(key));
            }
        }
        return formBody.build();
    }


    /**
     * get請求的參數
     */
    private void setGetParams() {
        if (mParamsMap != null) {
            mUrl = mUrl + "?";
            for (String key : mParamsMap.keySet()) {
                mUrl = mUrl + key + "=" + mParamsMap.get(key) + "&";
            }
            mUrl = mUrl.substring(0, mUrl.length() - 1);
        }
    }


    /**
     * 設定上傳檔案
     */
    private void setFile() {
        if (mFile != null) {
            if (mParamsMap == null) {
                setPostFile(); // 只有1個檔案
            } else {
                setPostParamsAndFile(); // 檔案+參數
            }
        } else if (mFileList != null) {
            setPostParamsAndListFile(); // 檔案集合
        } else if (mFileMap != null) {
            setPostParamsAndMapFile(); // 檔案集合(key不同)
        }

    }

    /**
     * 只有1個檔案, 無參數
     */
    private void setPostFile() {
        if (mFile != null && mFile.exists()) {
            MediaType fileType = MediaType.parse(mFileType);
            RequestBody body = RequestBody.create(fileType, mFile);//json数据，
            mRequestBuilder.post(new ProgressRequestBody(body, mCallBack));
        }
    }

    /**
     * 只有1個檔案, 有參數
     */
    private void setPostParamsAndFile() {
        if (mParamsMap != null && mFile != null) {
            MultipartBody.Builder builder = new MultipartBody.Builder();
            builder.setType(MultipartBody.FORM);
            for (String key : mParamsMap.keySet()) {
                builder.addFormDataPart(key, mParamsMap.get(key));
            }
            builder.addFormDataPart(mFileKey, mFile.getName(), RequestBody.create(MediaType.parse(mFileType), mFile));
            mRequestBuilder.post(new ProgressRequestBody(builder.build(), mCallBack));
        }
    }

    /**
     * 檔案集合
     */
    private void setPostParamsAndListFile() {
        if (mFileList != null) {
            MultipartBody.Builder builder = new MultipartBody.Builder();
            builder.setType(MultipartBody.FORM);
            if (mParamsMap != null) {
                for (String key : mParamsMap.keySet()) {
                    builder.addFormDataPart(key, mParamsMap.get(key));
                }
            }
            for (File f : mFileList) {
                builder.addFormDataPart(mFileKey, f.getName(), RequestBody.create(MediaType.parse(mFileType), f));
            }
            mRequestBuilder.post(builder.build());
        }
    }

    /**
     * 檔案集合(不同key)
     */
    private void setPostParamsAndMapFile() {
        if (mFileMap != null) {
            MultipartBody.Builder builder = new MultipartBody.Builder();
            builder.setType(MultipartBody.FORM);
            if (mParamsMap != null) {
                for (String key : mParamsMap.keySet()) {
                    builder.addFormDataPart(key, mParamsMap.get(key));
                }
            }

            for (String key : mFileMap.keySet()) {
                builder.addFormDataPart(key, mFileMap.get(key).getName(), RequestBody.create(MediaType.parse(mFileType), mFileMap.get(key)));
            }
            mRequestBuilder.post(builder.build());
        }
    }


    /**
     * 設定header參數
     */
    private void setHeader() {
        if (mHeaderMap != null) {
            for (String key : mHeaderMap.keySet()) {
                mRequestBuilder.addHeader(key, mHeaderMap.get(key));
            }
        }
    }


    // 執行
    void execute() {
        mOkHttpClient.newCall(mOkHttpRequest).enqueue(new Callback() {
            @Override
            public void onFailure(final Call call, final IOException e) {
                if (mCallBack != null) {
                    mCallBack.onError(call, e);
                }
            }

            @Override
            public void onResponse(final Call call, final Response response) throws IOException {
                if (mCallBack != null) {
                    mCallBack.onSuccess(call, response);
                }
            }

        });
    }


    /**
     * 自定義RequestBody類別，取得文件上傳進度
     */
    private static class ProgressRequestBody extends RequestBody {

        private final RequestBody requestBody;
        private BufferedSink bufferedSink;
        private CallBackUtil callBack;

        ProgressRequestBody(RequestBody requestBody, CallBackUtil callBack) {
            this.requestBody = requestBody;
            this.callBack = callBack;
        }

        @Override
        public MediaType contentType() {
            return requestBody.contentType();
        }

        @Override
        public long contentLength() throws IOException {
            return requestBody.contentLength();
        }

        @Override
        public void writeTo(BufferedSink sink) throws IOException {
            if (bufferedSink == null) {
                bufferedSink = Okio.buffer(sink(sink));
            }
            requestBody.writeTo(bufferedSink);
            bufferedSink.flush();
        }

        /**
         * 寫入, callback進度
         */
        private Sink sink(BufferedSink sink) {
            return new ForwardingSink(sink) {
                long bytesWritten = 0L;
                long contentLength = 0L;

                @Override
                public void write(Buffer source, long byteCount) throws IOException {
                    super.write(source, byteCount);
                    if (contentLength == 0) {
                        contentLength = contentLength();
                    }
                    bytesWritten += byteCount;
                    final float progress = bytesWritten * 1.0f / contentLength;
                    CallBackUtil.mMainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            callBack.onProgress(progress, contentLength);
                        }
                    });
                }
            };
        }
    }


}