import java.util.*;

public class Vote {

    private final String creator;
    private final String topic;
    // name - number of votes
    private final Map<String, Integer> answers;
    // option number - name
    private final Map<Integer, String> answerNumbers;
    private final Set<String> votedUsers;

    public Vote(String creator, String topic, List<String> answers) {
        this.creator = creator;
        this.topic = topic;
        this.answers = new HashMap<String, Integer>();
        answerNumbers = new HashMap<Integer, String>();
        int number = 0;
        for (String answer: answers) {
            this.answers.put(answer, 0);
            answerNumbers.put(++number, answer);
        }
        votedUsers = new HashSet<String>();
    }

    public String getViewRequest() {
        StringBuilder result = new StringBuilder(topic + "\n");
        result.append("********************\n");
        for (String answer: answers.keySet()) {
            result.append(answer).append(" - ").append(answers.get(answer)).append("\n");
        }
        result.append("********************\n");
        return result.toString();
    }

    public String getVoteRequest() {
        StringBuilder result = new StringBuilder();
        result.append("********************\n");
        int number = 0;
        for (String answer: answers.keySet()) {
            result.append(++number).append(") ").append(answer).append("\n");
        }
        result.append("********************\n");
        return result.toString();
    }

    public boolean isUserVote(String name) {
        return votedUsers.contains(name);
    }

    public void vote(String name, int number) {
        String answer = answerNumbers.get(number);
        answers.put(answer, answers.get(answer) + 1);
        votedUsers.add(name);
    }

    public String getCreator() {
        return creator;
    }
}
