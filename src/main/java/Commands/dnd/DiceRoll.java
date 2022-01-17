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
		event.getMessage().getChannel().sendTyping().queue();
		String message = event.getMessage().getContentRaw().toLowerCase().replace("$", "");
		String[] elms = message.split(" ");
		int total = 0;
		int mult = 1;
		String outputString = "";

		for(String elm : elms){
			if(elm.equals("-")){
				mult = -1;
			} else if(elm.equals("+")) {
				mult = 1;
			} else if(elm.contains("d")) {
				outputString += (mult == -1 ? " - " : " + ");
				String[] diceParts = elm.split("d");
				if(diceParts.length > 2){
					continue;
				}
				outputString += "[";
				int num = diceParts[0].equals("") ? 1 : Integer.parseInt(diceParts[0]);
				int sides = Integer.parseInt(diceParts[1]);
				for(int i = 0; i < num; i++){
					int rand = random.nextInt(sides) + 1;
					outputString += rand;
					if(i != num-1) outputString += ", ";
					total += mult * rand;
				}
				outputString += "]";
			} else {
				try{
					int change = Integer.parseInt(elm);
					total += mult * Integer.parseInt(elm);
					outputString += (mult == -1 ? " - " : " + ") + change;
				} catch (NumberFormatException e){
					event.getChannel().sendMessage("Your command had an improper element, we've ignored it").queue();
				}
			}
		}
		event.getChannel().sendMessage("```diff\n" +
				"- " + total + "\n" +
				outputString + "\n" +
				"```").queue();
	}

	@Override
	public String getHelpMessage() {
		return "Rolls x dice with y sides\n" +
				"```ini\nUsage: $[number of dice]d[sides on each dice]\n```";
	}
}
