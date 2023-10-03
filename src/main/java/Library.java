import java.util.HashMap;
import java.util.Map;

public class Library {

    private final Map<String, Topic> topics;

    public Library() {
        topics = new HashMap<String, Topic>();
    }

    public boolean isTopicExists(String title) {
        return topics.containsKey(title);
    }

    private void addTopic(String title) {
        topics.put(title, new Topic());
    }

    public String getViewRequest() {
        StringBuilder result = new StringBuilder();
        result.append("********************\n");
        for (String title: topics.keySet()) {
            result.append(title).append("\n");
        }
        result.append("********************\n");
        return result.toString();
    }

    public Topic getTopic(String title) {
        return topics.get(title);
    }
}
