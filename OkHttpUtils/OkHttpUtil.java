package com.example.okhttpdemo.okhttp;

import java.io.File;
import java.util.List;
import java.util.Map;

public class OkHttpUtil {

    public static final String METHOD_GET = "GET";
    public static final String METHOD_POST = "POST";
    public static final String METHOD_PUT = "PUT";
    public static final String METHOD_DELETE = "DELETE";

    public static final String FILE_TYPE_FILE = "file/*";
    public static final String FILE_TYPE_IMAGE = "image/*";
    public static final String FILE_TYPE_AUDIO = "audio/*";
    public static final String FILE_TYPE_VIDEO = "video/*";

    public static void okHttpGet(String url, CallBackUtil callBack) {
        okHttpGet(url, null, null, callBack);
    }

    public static void okHttpGet(String url, Map<String, String> paramsMap, CallBackUtil callBack) {
        okHttpGet(url, paramsMap, null, callBack);
    }

    public static void okHttpGet(String url, Map<String, String> paramsMap, Map<String, String> headerMap, CallBackUtil callBack) {
        new RequestUtil(METHOD_GET, url, paramsMap, headerMap, callBack).execute();
    }

    public static void okHttpPost(String url, CallBackUtil callBack) {
        okHttpPost(url, null, callBack);
    }

    public static void okHttpPost(String url, Map<String, String> paramsMap, CallBackUtil callBack) {
        okHttpPost(url, paramsMap, null, callBack);
    }

    public static void okHttpPost(String url, Map<String, String> paramsMap, Map<String, String> headerMap, CallBackUtil callBack) {
        new RequestUtil(METHOD_POST, url, paramsMap, headerMap, callBack).execute();
    }

    public static void okHttpPut(String url, CallBackUtil callBack) {
        okHttpPut(url, null, callBack);
    }

    public static void okHttpPut(String url, Map<String, String> paramsMap, CallBackUtil callBack) {
        okHttpPut(url, paramsMap, null, callBack);
    }

    public static void okHttpPut(String url, Map<String, String> paramsMap, Map<String, String> headerMap, CallBackUtil callBack) {
        new RequestUtil(METHOD_PUT, url, paramsMap, headerMap, callBack).execute();
    }

    public static void okHttpDelete(String url, CallBackUtil callBack) {
        okHttpDelete(url, null, callBack);
    }

    public static void okHttpDelete(String url, Map<String, String> paramsMap, CallBackUtil callBack) {
        okHttpDelete(url, paramsMap, null, callBack);
    }

    public static void okHttpDelete(String url, Map<String, String> paramsMap, Map<String, String> headerMap, CallBackUtil callBack) {
        new RequestUtil(METHOD_DELETE, url, paramsMap, headerMap, callBack).execute();
    }

    public static void okHttpPostJson(String url, String jsonStr, CallBackUtil callBack) {
        okHttpPostJson(url, jsonStr, null, callBack);
    }

    public static void okHttpPostJson(String url, String jsonStr, Map<String, String> headerMap, CallBackUtil callBack) {
        new RequestUtil(METHOD_POST, url, jsonStr, headerMap, callBack).execute();
    }

    public static void okHttpUploadFile(String url, File file, String fileKey, String fileType, CallBackUtil callBack) {
        okHttpUploadFile(url, file, fileKey, fileType, null, callBack);
    }

    public static void okHttpUploadFile(String url, File file, String fileKey, String fileType, Map<String, String> paramsMap, CallBackUtil callBack) {
        okHttpUploadFile(url, file, fileKey, fileType, paramsMap, null, callBack);
    }

    public static void okHttpUploadFile(String url, File file, String fileKey, String fileType, Map<String, String> paramsMap, Map<String, String> headerMap, CallBackUtil callBack) {
        new RequestUtil(METHOD_POST, url, paramsMap, file, fileKey, fileType, headerMap, callBack).execute();
    }

    public static void okHttpUploadListFile(String url, List<File> fileList, String fileKey, String fileType, CallBackUtil callBack) {
        okHttpUploadListFile(url, null, fileList, fileKey, fileType, callBack);
    }

    public static void okHttpUploadListFile(String url, Map<String, String> paramsMap, List<File> fileList, String fileKey, String fileType, CallBackUtil callBack) {
        okHttpUploadListFile(url, paramsMap, fileList, fileKey, fileType, null, callBack);
    }

    public static void okHttpUploadListFile(String url, Map<String, String> paramsMap, List<File> fileList, String fileKey, String fileType, Map<String, String> headerMap, CallBackUtil callBack) {
        new RequestUtil(METHOD_POST, url, paramsMap, fileList, fileKey, fileType, headerMap, callBack).execute();
    }

    public static void okHttpUploadMapFile(String url, Map<String, File> fileMap, String fileType, CallBackUtil callBack) {
        okHttpUploadMapFile(url, fileMap, fileType, null, callBack);
    }

    public static void okHttpUploadMapFile(String url, Map<String, File> fileMap, String fileType, Map<String, String> paramsMap, CallBackUtil callBack) {
        okHttpUploadMapFile(url, fileMap, fileType, paramsMap, null, callBack);
    }

    public static void okHttpUploadMapFile(String url, Map<String, File> fileMap, String fileType, Map<String, String> paramsMap, Map<String, String> headerMap, CallBackUtil callBack) {
        new RequestUtil(METHOD_POST, url, paramsMap, fileMap, fileType, headerMap, callBack).execute();
    }

    public static void okHttpDownloadFile(String url, CallBackUtil.CallBackFile callBack) {
        okHttpDownloadFile(url, null, callBack);
    }

    public static void okHttpDownloadFile(String url, Map<String, String> paramsMap, CallBackUtil.CallBackFile callBack) {
        okHttpGet(url, paramsMap, null, callBack);
    }

    public static void okHttpGetBitmap(String url, CallBackUtil.CallBackBitmap callBack) {
        okHttpGetBitmap(url, null, callBack);
    }

    public static void okHttpGetBitmap(String url, Map<String, String> paramsMap, CallBackUtil.CallBackBitmap callBack) {
        okHttpGet(url, paramsMap, null, callBack);
    }

}