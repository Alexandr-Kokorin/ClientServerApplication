import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Topic {

    private final Map<String, Vote> votes;

    public Topic() {
        votes = new HashMap<>();
    }

    public synchronized boolean addVote(String creator, String title, String topic, List<String> answers, Set<String> votedUsers) {
        if (votes.containsKey(title)) return false;
        votes.put(title, new Vote(creator, topic, answers, votedUsers));
        return true;
    }

    public synchronized int deleteVote(String name, String title) {
        if (!votes.containsKey(title)) return -1;
        if (!votes.get(title).getCreator().equals(name)) return 0;
        votes.remove(title);
        return 1;
    }

    public synchronized String getViewRequest() {
        StringBuilder result = new StringBuilder();
        result.append("**********\n");
        for (String title: votes.keySet()) {
            result.append(title).append("\n");
        }
        result.append("**********");
        return result.toString();
    }

    public synchronized int getCountVotes() {
        return votes.size();
    }

    public synchronized Vote getVote(String title) {
        return votes.get(title);
    }
}
