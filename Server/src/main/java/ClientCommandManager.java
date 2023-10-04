import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ClientCommandManager {

    private final Library library;

    public ClientCommandManager(Library library) {
        this.library = library;
    }

    public void sortCommand(String command, Connector connector, String name) throws IOException {
        String[] strings = command.split("=");
        if (strings.length == 1) {
            if (strings[0].equals("view")) runViewCommand(connector);
            else connector.writeLine("Incorrect command.");
        } else if (strings.length == 2) {
            switch (strings[0]) {
                case "create topic -n" -> runCreateTopicCommand(strings[1], connector);
                case "view -t" -> runViewTopicCommand(strings[1], connector);
                case "create vote -t" -> runCreateVoteCommand(strings[1], connector, name);
                case "login -u" -> runLoginCommand(connector, name);
                default -> connector.writeLine("Incorrect command.");
            }
        } else if (strings.length == 3) {
            String[] temp = strings[1].split(" ");
            if (temp.length == 2 && temp[1].equals("-v")) {
                switch (strings[0]) {
                    case "view -t" -> runViewVoteCommand(temp[0], strings[2], connector);
                    case "vote -t" -> runVoteCommand(temp[0], strings[2], connector, name);
                    case "delete -t" -> runDeleteCommand(temp[0], strings[2], connector, name);
                    default -> connector.writeLine("Incorrect command.");
                }
            } else connector.writeLine("Incorrect command.");
        } else connector.writeLine("Incorrect command.");
    }

    private void runCreateTopicCommand(String title, Connector connector) throws IOException {
        if (library.addTopic(title)) connector.writeLine("The topic was successfully created.");
        else connector.writeLine("This topic already exists.");
    }

    private void runCreateVoteCommand(String title, Connector connector, String creator) throws IOException {
        Topic tempTopic = library.getTopic(title);
        if (tempTopic == null) {
            connector.writeLine("There is not such topic.");
            return;
        }
        connector.writeLine("Enter the name of the vote:");
        String name = connector.readLine();
        connector.writeLine("Enter the voting topic:");
        String topic = connector.readLine();
        connector.writeLine("Enter the number of possible answers:");
        boolean isNumber = false;
        int number = 0;
        while (!isNumber) {
            try {
                number = Integer.parseInt(connector.readLine());
                isNumber = true;
            } catch (NumberFormatException e) {
                connector.writeLine("Please enter a number:");
            }
        }
        List<String> answers = new ArrayList<>();
        for (int i = 0; i < number; i++) {
            connector.writeLine("Enter " + (i + 1) + " answer option:");
            answers.add(connector.readLine());
        }
        boolean isCreate = tempTopic.addVote(creator, name, topic, answers, null);
        if (isCreate) connector.writeLine("The vote was successfully created.");
        else connector.writeLine("This vote already exists.");
    }

    private void runViewCommand(Connector connector) throws IOException {
        connector.writeLine(library.getViewRequest());
    }

    private void runViewTopicCommand(String topic, Connector connector) throws IOException {
        Topic tempTopic = library.getTopic(topic);
        if (tempTopic == null) {
            connector.writeLine("There is not such topic.");
            return;
        }
        connector.writeLine(library.getTopic(topic).getViewRequest());
    }

    private void runViewVoteCommand(String topic, String vote, Connector connector) throws IOException {
        Topic tempTopic = library.getTopic(topic);
        if (tempTopic == null) {
            connector.writeLine("There is not such topic.");
            return;
        }
        Vote tempVote = tempTopic.getVote(vote);
        if (tempVote == null) {
            connector.writeLine("There is not such vote.");
            return;
        }
        connector.writeLine(library.getTopic(topic).getVote(vote).getViewRequest());
    }

    private void runVoteCommand(String topic, String vote, Connector connector, String name) throws IOException {
        Topic tempTopic = library.getTopic(topic);
        if (tempTopic == null) {
            connector.writeLine("There is not such topic.");
            return;
        }
        Vote tempVote = tempTopic.getVote(vote);
        if (tempVote == null) {
            connector.writeLine("There is not such vote.");
            return;
        }
        String str = tempVote.getVoteRequest() + "\n";
        str += "Enter the response number:";
        connector.writeLine(str);
        boolean isNumber = false;
        int number = 0;
        while (!isNumber) {
            try {
                number = Integer.parseInt(connector.readLine());
                if (number > 0 && number <= tempVote.getCountAnswers()) isNumber = true;
                else connector.writeLine("Please enter a correct number:");
            } catch (NumberFormatException e) {
                connector.writeLine("Please enter a correct number:");
            }
        }
        if (tempVote.vote(name, number)) connector.writeLine("You have successfully voted.");
        else connector.writeLine("You have already voted in this vote.");
    }

    private void runDeleteCommand(String topic, String vote, Connector connector, String name) throws IOException {
        Topic tempTopic = library.getTopic(topic);
        if (tempTopic == null) {
            connector.writeLine("There is not such topic.");
            return;
        }
        int deleteCode = tempTopic.deleteVote(name, vote);
        if (deleteCode == 1) connector.writeLine("The vote was successfully deleted.");
        else if (deleteCode == 0)
            connector.writeLine("You are not the creator of the vote, only the creator can delete it.");
        else connector.writeLine("There is no such vote.");
    }

    private void runLoginCommand(Connector connector, String name) throws IOException {
        connector.writeLine(name + ", you are already logged in.");
    }
}
