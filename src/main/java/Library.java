import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Library {

    private final Map<String, Topic> topics;

    public Library() {
        topics = new HashMap<>();
    }

    public synchronized void save(BufferedWriter writer) throws IOException {
        writer.write("Library: " + getCountTopics() + "\n");
        for (String topicKey: topics.keySet()) {
            Topic topic = getTopic(topicKey);
            writer.write("Topic: " + topic.getCountVotes() + "\n");
            writer.write(topicKey + "\n");
            for (String voteKey: topic.getVotes().keySet()) {
                Vote vote = topic.getVote(voteKey);
                writer.write("Vote: " + vote.getCountAnswers() + "\n");
                writer.write(vote.getCreator() + "\n");
                writer.write(voteKey + "\n");
                writer.write(vote.getTopic() + "\n");
                for (int i = 1; i <= vote.getCountAnswers(); i++) {
                    writer.write(vote.getAnswerNumbers().get(i) + "#-#" + vote.getAnswers().get(vote.getAnswerNumbers().get(i)) + "\n");
                }
                writer.write("Users: " + vote.getCountUsers() + "\n");
                for (String user: vote.getVotedUsers()) {
                    writer.write(user + "\n");
                }
            }
        }
    }

    public synchronized void load(BufferedReader reader, String line) throws IOException {
        int countTopics = Integer.parseInt(line.split(" ")[1]);
        for (int i = 0; i < countTopics; i++) {
            int countVotes =  Integer.parseInt(reader.readLine().split(" ")[1]);
            String name = reader.readLine();
            addTopic(name);
            for (int j = 0; j < countVotes; j++) {
                int countAnswers = Integer.parseInt(reader.readLine().split(" ")[1]);
                String creator = reader.readLine();
                String title = reader.readLine();
                String topic = reader.readLine();
                Map<String, Integer> answers = new HashMap<>();
                Map<Integer, String> answerNumbers = new HashMap<>();
                for (int k = 0; k < countAnswers; k++) {
                    String[] temp = reader.readLine().split("#-#");
                    answers.put(temp[0], Integer.parseInt(temp[1]));
                    answerNumbers.put(k + 1, temp[0]);
                }
                int countUsers = Integer.parseInt(reader.readLine().split(" ")[1]);
                Set<String> votedUsers = new HashSet<>();
                for (int k = 0; k < countUsers; k++) {
                    votedUsers.add(reader.readLine());
                }
                getTopic(name).addVote(title, creator, topic, answers, answerNumbers, votedUsers);
            }
        }
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
            result.append(title).append("(votes in topic = ").append(topics.get(title).getCountVotes()).append(")\n");
        }
        result.append("**********");
        return result.toString();
    }

    public synchronized Topic getTopic(String title) {
        return topics.get(title);
    }

    public synchronized int getCountTopics() {
        return topics.size();
    }
}
