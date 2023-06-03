import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

@Slf4j
public class OpenAICommunicator {
    String getAnswer(String prompt) throws IOException {

        Properties prop = new Properties();
        try (InputStream input = Main.class.getResourceAsStream("/config.properties")) {
            prop.load(input);
        } catch (IOException e) {
            log.error("Error loading properties file: ", e);
        }
        String openAIKey = prop.getProperty("openai.api.key");

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build();
        MediaType mediaType = MediaType.parse("application/json");

        JSONObject json = new JSONObject();
        json.put("prompt", prompt);
        json.put("max_tokens", 120);

        RequestBody body = RequestBody.create(mediaType, json.toString());

        Request request = new Request.Builder()
                .url("https://api.openai.com/v1/engines/text-davinci-003/completions")
                .post(body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer " + openAIKey)
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }
}
