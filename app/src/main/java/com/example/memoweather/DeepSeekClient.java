package com.example.memoweather;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DeepSeekClient {

    private static final String API_KEY = "填DeepSeek的Apikey";

    private static final String BASE_URL = "https://api.deepseek.com/v1/chat/completions";

    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    private final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build();

    public static class Result {
        public String summary;
        public String keywords;
        public Result(String s, String k) { summary = s; keywords = k; }
    }

    public Result summarize(String text) throws Exception {
        String prompt =
                "请对下面文本生成摘要与关键词，并且严格只输出 JSON（不要输出任何多余文字）：\n" +
                        "{\n" +
                        "  \"summary\": \"不超过120字的中文摘要\",\n" +
                        "  \"keywords\": [\"关键词1\",\"关键词2\",\"关键词3\",\"关键词4\",\"关键词5\",\"关键词6\"]\n" +
                        "}\n\n" +
                        "文本：\n" + text;

        JSONObject body = new JSONObject();
        body.put("model", "deepseek-chat");
        body.put("temperature", 0.3);

        JSONArray messages = new JSONArray();
        messages.put(new JSONObject().put("role", "system").put("content", "你是一个中文文本摘要助手。"));
        messages.put(new JSONObject().put("role", "user").put("content", prompt));
        body.put("messages", messages);

        Request req = new Request.Builder()
                .url(BASE_URL)
                .addHeader("Authorization", "Bearer " + API_KEY)
                .addHeader("Content-Type", "application/json")
                .post(RequestBody.create(body.toString(), JSON))
                .build();

        try (Response resp = client.newCall(req).execute()) {
            String respStr = resp.body() != null ? resp.body().string() : "";

            if (!resp.isSuccessful()) {
                throw new IOException("HTTP " + resp.code() + " " + resp.message() + "\n" + respStr);
            }

            JSONObject root = new JSONObject(respStr);

            JSONArray choices = root.optJSONArray("choices");
            if (choices == null || choices.length() == 0) {
                throw new IOException("返回缺少 choices: " + respStr);
            }

            JSONObject msg = choices.getJSONObject(0).optJSONObject("message");
            if (msg == null) {
                throw new IOException("返回缺少 message: " + respStr);
            }

            String content = msg.optString("content", "").trim();
            if (content.isEmpty()) {
                throw new IOException("返回 content 为空: " + respStr);
            }

            JSONObject out;
            try {
                out = new JSONObject(content);
            } catch (Exception parseErr) {
                return new Result(content, "");
            }

            String summary = out.optString("summary", "");
            JSONArray kwArr = out.optJSONArray("keywords");

            StringBuilder kws = new StringBuilder();
            if (kwArr != null) {
                for (int i = 0; i < kwArr.length(); i++) {
                    if (i > 0) kws.append(", ");
                    kws.append(kwArr.optString(i, ""));
                }
            }

            return new Result(summary, kws.toString());
        }
    }
}
