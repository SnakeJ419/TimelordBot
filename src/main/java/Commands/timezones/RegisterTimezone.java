package Commands.timezones;

import Commands.ServerCommand;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;

public class RegisterTimezone extends ServerCommand {
	public RegisterTimezone() {
		super("$register");
	}

	@Override
	public void runCommand(MessageReceivedEvent event) {
		logger.log(Level.INFO, "User: [" + event.getAuthor().getName() + "] Channel: [" + event.getChannel().getName() + "] Guild: [" + event.getGuild().getName() + ']');

		String[] pieces = event.getMessage().getContentRaw().toLowerCase().split(" ");
		if(pieces.length != 2){
			event.getChannel().sendMessage("Something with your command is wrong").queue();
			return;
		}

		List<String[]> timezones = null;
		List<String[]> users = null;
		try {
			CSVReader reader = new CSVReader(new FileReader("C:\\Users\\Joshua\\IdeaProjects\\The Time Lord\\src\\main\\resources\\timezones.csv"));
			CSVReader userReader = new CSVReader(new FileReader("C:\\Users\\Joshua\\IdeaProjects\\The Time Lord\\src\\main\\resources\\users.csv"));
			timezones = reader.readAll();
			users = userReader.readAll();
			reader.close();
			userReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		String[] removeUser = null;
		for(String[] user : users){
			if(user[0].equals(event.getMember().getId())){
				removeUser = user;
				event.getChannel().sendMessage("Your registration is being changed").queue();
			}
		}
		if(removeUser != null){
			users.remove(removeUser);
		}

		boolean valid = false;
		for(String[] zone : timezones){
			if(zone[0].equals(pieces[1])){
				valid = true;
				break;
			}
		}
		if(!valid){
			event.getChannel().sendMessage("The Requested Timezone isn't Valid").queue();
			return;
		}

		users.add(new String[]{event.getMember().getId(), pieces[1]});
		try {
			CSVWriter writer = new CSVWriter(new FileWriter("C:\\Users\\Joshua\\IdeaProjects\\The Time Lord\\src\\main\\resources\\users.csv"));
			writer.writeAll(users);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		event.getChannel().sendMessage("Registered User: " + event.getMember().getEffectiveName() + " to timezone: " + pieces[1]).queue();
	}
}
