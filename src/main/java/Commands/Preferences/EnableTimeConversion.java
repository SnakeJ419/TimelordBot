package Commands.Preferences;

import Commands.ServerCommand;
import Preferences.PreferenceManager;
import Preferences.ServerDataStructure;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class EnableTimeConversion extends ServerCommand {

	public EnableTimeConversion() {
		super("$enabletimeconversion", true);
	}

	@Override
	public void runCommand(MessageReceivedEvent event) {
		//get standard format of raw text
		String messageText = event.getMessage().getContentRaw().toLowerCase();

		//attempt to translate value to boolean
		boolean enabled;
		try{
			enabled = Boolean.parseBoolean(messageText.split(" ")[1]);
		} catch(Exception e) {
			event.getChannel().sendMessage("Command must be formatted as $enableTimeConversion <boolean>").queue();
			return;
		}

		//Save value into preferences
		ServerDataStructure data = PreferenceManager.getData(event.getGuild().getId());
		data.listTimezones = enabled;
		PreferenceManager.writeData();

		event.getChannel().sendMessage("Time Conversion set to " + enabled).queue();
	}
}
