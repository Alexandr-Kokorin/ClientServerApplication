import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Topic {

    private final Map<String, Vote> votes;

    public Topic() {
        votes = new HashMap<String, Vote>();
    }

    public boolean isVoteExists(String title) {
        return votes.containsKey(title);
    }

    public void addVote(String creator, String title, String topic, List<String> answers) {
        votes.put(title, new Vote(creator, topic, answers));
    }

    public boolean deleteVote(String name, String title) {
        if (!votes.get(title).getCreator().equals(name)) return false;
        votes.remove(title);
        return true;
    }

    public String getViewRequest() {
        StringBuilder result = new StringBuilder();
        result.append("********************\n");
        for (String title: votes.keySet()) {
            result.append(title).append("\n");
        }
        result.append("********************\n");
        return result.toString();
    }

    public Vote getVote(String title) {
        return votes.get(title);
    }
}
