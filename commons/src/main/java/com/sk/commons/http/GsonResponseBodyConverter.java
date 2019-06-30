package com.sk.commons.http;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.io.StringReader;

import okhttp3.ResponseBody;
import retrofit2.Converter;


public class GsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final Gson gson;
    private final TypeAdapter<T> adapter;

    GsonResponseBodyConverter(Gson gson, TypeAdapter<T> adapter) {
        this.gson = gson;
        this.adapter = adapter;
    }

    @Override public T convert(ResponseBody value) throws IOException {
        String response = value.string();

        if (response.contains("\"result\":\"\"")) {//包含 "result":""，过滤掉
            int pos = response.lastIndexOf(",");
            response = response.substring(0,pos) + "}";
        }

        JsonReader jsonReader = gson.newJsonReader(new StringReader(response));
        try {
            return adapter.read(jsonReader);
        } finally {
            value.close();
        }
    }


}