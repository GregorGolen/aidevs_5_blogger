import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Properties;

@Slf4j
public class Main {
    public static void main(String[] args) throws URISyntaxException, IOException, InterruptedException {
        Properties prop = new Properties();
        try (InputStream input = Main.class.getResourceAsStream("/config.properties")) {
            prop.load(input);
        } catch (IOException e) {
            log.error("Error loading properties file: ", e);
        }
        String myKey = prop.getProperty("api.key");

        TaskObtainer taskObtainer = new TaskObtainer(myKey);
        String task = taskObtainer.getTask();
        TaskProcessor taskProcessor = new TaskProcessor();
        String[] answer = taskProcessor.processTask(task);
        TaskReporter taskReporter = new TaskReporter();
        taskReporter.reportTask(taskObtainer.getToken(), answer);
    }
}
