package Commands.dnd;

import Commands.ServerCommand;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Arrays;
import java.util.Random;

public class DiceRoll extends ServerCommand {
	private final static Random random = new Random();

	public DiceRoll() {
		super("$d", false, true);
	}

	@Override
	public boolean check(MessageReceivedEvent event) {
		String message = event.getMessage().getContentRaw().toLowerCase();
		return message.length() > 2 && message.charAt(0) == '$' && message.contains("d") && Character.isDigit(message.charAt(message.indexOf('d') + 1));
	}

	@Override
	public void runCommand(MessageReceivedEvent event) {
		String message = event.getMessage().getContentRaw().toLowerCase();
		int diceCount = 0;
		int startIndex = 2;
		if(message.charAt(1) == 'd'){
			diceCount = 1;
		} else {
			startIndex = message.indexOf('d') + 1;
			diceCount = Integer.parseInt(message.substring(1, startIndex - 1));
		}

		String diceValue = "";
		char nextChar;
		while(startIndex < message.length() && Character.isDigit(nextChar = message.charAt(startIndex))){
			diceValue += Integer.parseInt(String.valueOf(nextChar));
			startIndex++;
		}
		int diceType = Integer.parseInt(diceValue);

		int[] diceValues = new int[diceCount];
		int total = 0;
		for(int i = 0; i < diceCount; i++){
			diceValues[i] = random.nextInt(diceType) + 1;
			total += diceValues[i];
		}

		if(message.contains("+")){
			String addString = message.split("\\+")[1];
			int add = Integer.parseInt(addString.replace(" ", ""));
			total += add;
			event.getChannel().sendMessage("```diff\n" +
					"- " + total + "\n" +
					Arrays.toString(diceValues) + " + " + add + "\n" +
					"```").queue();
			return;
		}
		event.getChannel().sendMessage("```diff\n" +
				"- " + total + "\n" +
				Arrays.toString(diceValues) + "\n" +
				"```").queue();
	}

	@Override
	public String getHelpMessage() {
		return "Rolls x dice with y sides\n" +
				"```ini\nUsage: $[number of dice]d[sides on each dice]\n```";
	}
}
