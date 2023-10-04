import java.util.HashMap;
import java.util.Map;

public class Library {

    private final Map<String, Topic> topics;

    public Library() {
        topics = new HashMap<>();
    }

    public synchronized boolean addTopic(String title) {
        if (topics.containsKey(title)) return false;
        topics.put(title, new Topic());
        return true;
    }

    public synchronized String getViewRequest() {
        StringBuilder result = new StringBuilder();
        result.append("**********\n");
        for (String title: topics.keySet()) {
            result.append(title).append("(votes in topic=").append(topics.get(title).getCountVotes()).append(")\n");
        }
        result.append("**********");
        return result.toString();
    }

    public synchronized Topic getTopic(String title) {
        return topics.get(title);
    }

    public synchronized Map<String, Topic> getTopics() {
        return topics;
    }
}
