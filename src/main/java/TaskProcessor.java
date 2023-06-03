import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

public class TaskProcessor {
    String[] processTask(String task) throws IOException {
        JSONObject taskJson = new JSONObject(task);
        String[] blogSubjects = getBlogSubjects(taskJson);

        OpenAICommunicator openAICommunicator = new OpenAICommunicator();
        String[] blogPost = new String[4];

        for(int i=0; i < blogSubjects.length; i++) {
            String prompt = "Draft a blog post on the subject: " + blogSubjects[i] + ". Please write at least 2 sentences. Do not make introduction, write to the point";
            String responseString = openAICommunicator.getAnswer(prompt);
            JSONObject jsonObj = new JSONObject(responseString);
            JSONArray choicesArray = jsonObj.getJSONArray("choices");
            JSONObject firstChoiceObj = choicesArray.getJSONObject(0);
            String chapter = firstChoiceObj.getString("text").trim();
            blogPost[i] = chapter + " ";
        }
        return blogPost;
    }

    String[] getBlogSubjects(JSONObject taskJson) {
        JSONArray blogArray = taskJson.getJSONArray("blog");
        String[] subjects = new String[blogArray.length()];

        for (int i = 0; i < blogArray.length(); i++) {
            subjects[i] = blogArray.getString(i);
        }
        return subjects;
    }
}

