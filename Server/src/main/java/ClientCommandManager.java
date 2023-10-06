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
            sortOnePartCommand(strings, connector);
        } else if (strings.length == 2) {
            sortTwoPartCommand(strings, connector, name);
        } else if (strings.length == 3) {
            sortThreePartCommand(strings, connector, name);
        } else connector.writeLine("Incorrect command.");
    }

    private void sortOnePartCommand(String[] command, Connector connector) throws IOException {
        if (command[0].equals("view")) runViewCommand(connector);
        else connector.writeLine("Incorrect command.");
    }

    private void sortTwoPartCommand(String[] command, Connector connector, String name) throws IOException {
        switch (command[0]) {
            case "create topic -n" -> runCreateTopicCommand(command[1], connector);
            case "view -t" -> runViewTopicCommand(command[1], connector);
            case "create vote -t" -> runCreateVoteCommand(command[1], connector, name);
            case "login -u" -> runLoginCommand(connector, name);
            default -> connector.writeLine("Incorrect command.");
        }
    }

    private void sortThreePartCommand(String[] command, Connector connector, String name) throws IOException {
        String[] temp = command[1].split(" ");
        if (temp.length == 2 && temp[1].equals("-v")) {
            switch (command[0]) {
                case "view -t" -> runViewVoteCommand(temp[0], command[2], connector);
                case "vote -t" -> runVoteCommand(temp[0], command[2], connector, name);
                case "delete -t" -> runDeleteCommand(temp[0], command[2], connector, name);
                default -> connector.writeLine("Incorrect command.");
            }
        } else connector.writeLine("Incorrect command.");
    }

    private void runCreateTopicCommand(String title, Connector connector) throws IOException {
        if (library.addTopic(title)) connector.writeLine("The topic was successfully created.");
        else connector.writeLine("This topic already exists.");
    }

    private void runCreateVoteCommand(String title, Connector connector, String creator) throws IOException {
        if (isTopicNotCreate(library.getTopic(title), connector)) return;
        connector.writeLine("Enter the name of the vote:");
        String name = connector.readLine();
        if (library.getTopic(title).isVoteExists(name)) {
            connector.writeLine("This vote already exists.");
            return;
        }
        connector.writeLine("Enter the voting topic:");
        String topic = connector.readLine();
        connector.writeLine("Enter the number of possible answers:");
        int number = getNumberForCreate(connector);
        List<String> answers = new ArrayList<>();
        for (int i = 0; i < number; i++) {
            connector.writeLine("Enter " + (i + 1) + " answer option:");
            answers.add(connector.readLine());
        }
        boolean isCreate = library.getTopic(title).addVote(creator, name, topic, answers);
        if (isCreate) connector.writeLine("The vote was successfully created.");
        else connector.writeLine("This vote already exists.");
    }

    private void runViewCommand(Connector connector) throws IOException {
        connector.writeLine(library.getViewRequest());
    }

    private void runViewTopicCommand(String topic, Connector connector) throws IOException {
        if (isTopicNotCreate(library.getTopic(topic), connector)) return;
        connector.writeLine(library.getTopic(topic).getViewRequest());
    }

    private void runViewVoteCommand(String topic, String vote, Connector connector) throws IOException {
        if (isTopicNotCreate(library.getTopic(topic), connector)) return;
        if (isVoteNotCreate(library.getTopic(topic).getVote(vote), connector)) return;
        connector.writeLine(library.getTopic(topic).getVote(vote).getViewRequest());
    }

    private void runVoteCommand(String topic, String title, Connector connector, String name) throws IOException {
        if (isTopicNotCreate(library.getTopic(topic), connector)) return;
        if (isVoteNotCreate(library.getTopic(topic).getVote(title), connector)) return;
        if (library.getTopic(topic).getVote(title).isUserExists(name)) return;
        Vote vote = library.getTopic(topic).getVote(title);
        String str = vote.getVoteRequest() + "\nEnter the response number:";
        connector.writeLine(str);
        int number = getNumberForVote(connector, vote);
        if (vote.vote(name, number)) connector.writeLine("You have successfully voted.");
        else connector.writeLine("You have already voted in this vote.");
    }

    private void runDeleteCommand(String topic, String vote, Connector connector, String name) throws IOException {
        if (isTopicNotCreate(library.getTopic(topic), connector)) return;
        int deleteCode = library.getTopic(topic).deleteVote(name, vote);
        if (deleteCode == 1) connector.writeLine("The vote was successfully deleted.");
        else if (deleteCode == 0) connector.writeLine("You are not the creator of the vote, only the creator can delete it.");
        else connector.writeLine("There is no such vote.");
    }

    private void runLoginCommand(Connector connector, String name) throws IOException {
        connector.writeLine(name + ", you are already logged in.");
    }

    private int getNumberForCreate(Connector connector) throws IOException {
        int number = 0;
        boolean isNumber = false;
        while (!isNumber) {
            try {
                number = Integer.parseInt(connector.readLine());
                isNumber = true;
            } catch (NumberFormatException e) {
                connector.writeLine("Please enter a number:");
            }
        }
        return number;
    }

    private int getNumberForVote(Connector connector, Vote vote) throws IOException {
        int number = 0;
        boolean isNumber = false;
        while (!isNumber) {
            try {
                number = Integer.parseInt(connector.readLine());
                if (number > 0 && number <= vote.getCountAnswers()) isNumber = true;
                else connector.writeLine("Please enter a correct number:");
            } catch (NumberFormatException e) {
                connector.writeLine("Please enter a correct number:");
            }
        }
        return number;
    }

    private boolean isTopicNotCreate(Topic topic, Connector connector) throws IOException {
        if (topic == null) {
            connector.writeLine("There is not such topic.");
            return true;
        } else return false;
    }

    private boolean isVoteNotCreate(Vote vote, Connector connector) throws IOException {
        if (vote == null) {
            connector.writeLine("There is not such vote.");
            return true;
        } else return false;
    }
}
