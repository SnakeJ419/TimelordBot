package Commands.Util;

import Commands.ServerCommand;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.logging.Level;

public class Annoy extends ServerCommand {

	public Annoy() {
		super("$annoy", true);
	}

	@Override
	public void runCommand(MessageReceivedEvent event) {
		logger.log(Level.INFO, "User: [" + event.getAuthor().getName() + "] Channel: [" + event.getChannel().getName() + "] Guild: [" + event.getGuild().getName() + ']');

		String[] msgElements = event.getMessage().getContentRaw().split(" ");
		int pings = Integer.parseInt(msgElements[2]);
		if(msgElements.length != 3){
			logger.log(Level.SEVERE, "Command was configured incorrectly, wrong number of elements in array");
			event.getChannel().sendMessage("Command should be: $annoy [message] [repeats]").queue();
		}

		for(int i = 0; i < pings; i++){
			event.getChannel().sendMessage(msgElements[1]).queue();
			try {
				Thread.sleep(1500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
