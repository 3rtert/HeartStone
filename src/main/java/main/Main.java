package main;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;

public class Main 
{
	public static void main(String[] args) {
        CommandLine commandLine;

        Option playerOption1 = Option.builder("p1")
                .desc("Type of player 1")
                .longOpt("player1")
                .hasArg()
                .required()
                .build();

        Option playerOption2 = Option.builder("p2")
                .desc("Type of player 2")
                .longOpt("player2")
                .hasArg()
                .required()
                .build();

        Options options = new Options();

        options.addOption(playerOption1);
        options.addOption(playerOption2);
        options.addOption("n", "numberOfPlays", true, "Number of plays");

        options.addOption("gui", "withGui", false, "Game with gui");

        CommandLineParser parser = new DefaultParser();

        try {
            commandLine = parser.parse(options, args);
            boolean withGui = false;
            if (commandLine.hasOption("gui")) {
                withGui = true;
            }

            int numberOfPlays = 1;
            if(!withGui && commandLine.hasOption("n")) {
                numberOfPlays = Integer.parseInt(commandLine.getOptionValue("n"));
            }

            String player1 = commandLine.getOptionValue("p1");
            String player2 = commandLine.getOptionValue("p2");

            GameController controller = new GameController(withGui);

            int firstPlayerWins = 0;
            for(int i = 0; i < numberOfPlays; i++) {
                controller.init();
                controller.setPlayersAI(player1, player2);
                if(controller.startGame() == 0) {
                    firstPlayerWins++;
                }
            }

            System.out.println("First player wins: " + firstPlayerWins);
            System.out.println("Second player wins: " + (numberOfPlays - firstPlayerWins));

        } catch (Exception exception) {
            System.out.print("Parse error: ");
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }

	}
}
