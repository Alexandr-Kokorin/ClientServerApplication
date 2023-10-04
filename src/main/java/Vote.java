import java.util.*;

public class Vote {

    private final String creator;
    private final String topic;
    // name - number of votes
    private final Map<String, Integer> answers;
    // option number - name
    private final Map<Integer, String> answerNumbers;
    private final Set<String> votedUsers;

    public Vote(String creator, String topic, List<String> answers, Set<String> votedUsers) {
        this.creator = creator;
        this.topic = topic;
        this.answers = new HashMap<>();
        answerNumbers = new HashMap<>();
        int number = 0;
        for (String answer: answers) {
            this.answers.put(answer, 0);
            answerNumbers.put(++number, answer);
        }
        this.votedUsers = votedUsers == null? new HashSet<>(): votedUsers;
    }

    public synchronized String getViewRequest() {
        StringBuilder result = new StringBuilder(topic + "\n");
        result.append("**********\n");
        for (int i = 1; i <= answerNumbers.size(); i++) {
            result.append(answerNumbers.get(i)).append(" - ").append(answers.get(answerNumbers.get(i))).append("\n");
        }
        result.append("**********");
        return result.toString();
    }

    public synchronized String getVoteRequest() {
        StringBuilder result = new StringBuilder();
        result.append("**********\n");
        for (int i = 1; i <= answerNumbers.size(); i++) {
            result.append(i).append(") ").append(answerNumbers.get(i)).append("\n");
        }
        result.append("**********");
        return result.toString();
    }

    public synchronized boolean vote(String name, int number) {
        if (votedUsers.contains(name)) return false;
        String answer = answerNumbers.get(number);
        answers.put(answer, answers.get(answer) + 1);
        votedUsers.add(name);
        return true;
    }

    public synchronized int getCountAnswers() {
        return answers.size();
    }

    public String getCreator() {
        return creator;
    }
}
