package Commands.timezones;

import Commands.ServerCommand;
import com.opencsv.CSVReader;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;

public class ListTimezones extends ServerCommand {
	public ListTimezones() {
		super("$listtimezones");
	}

	@Override
	public void runCommand(MessageReceivedEvent event) {
		logger.log(Level.INFO, "User: [" + event.getAuthor().getName() + "] Channel: [" + event.getChannel().getName() + "] Guild: [" + event.getGuild().getName() + ']');

		List<String[]> timezones = null;
		try {
			CSVReader reader = new CSVReader(new FileReader("C:\\Users\\Joshua\\IdeaProjects\\The Time Lord\\src\\main\\resources\\timezones.csv"));
			timezones = reader.readAll();
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		String message = "";
		for (String[] zone : timezones) {
			message += zone[0] + ": " + zone[1] + " hours from pst \n";
		}
		event.getChannel().sendMessage(message).queue();
	}
}
