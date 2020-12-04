package Commands.timezones;

import Commands.ServerCommand;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class AddTimezone extends ServerCommand {
	public AddTimezone() {
		super("$addtimezone", true);
	}

	@Override
	public void runCommand(MessageReceivedEvent event) {
		logger.log(Level.INFO, "User: [" + event.getAuthor().getName() + "] Channel: [" + event.getChannel().getName() + "] Guild: [" + event.getGuild().getName() + ']');

		String message = event.getMessage().getContentRaw().toLowerCase();
		String[] pieces = message.split(" ");
		if(pieces.length != 3 || !checkNumber(pieces[2])){
			event.getChannel().sendMessage("Something with your command is wrong").queue();
			return;
		}

		List<String[]> timezones = null;
		try {
			CSVReader reader = new CSVReader(new FileReader("C:\\Users\\Joshua\\IdeaProjects\\The Time Lord\\src\\main\\resources\\timezones.csv"));
			timezones = reader.readAll();
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		for(String[] timezone : timezones){
			if(pieces[1].equals(timezone[0])){
				event.getChannel().sendMessage("This Timezone Already Exists").queue();
				return;
			}
		}

		timezones.add(new String[]{pieces[1], pieces[2]});

		try {
			CSVWriter writer = new CSVWriter(new FileWriter("C:\\Users\\Joshua\\IdeaProjects\\The Time Lord\\src\\main\\resources\\timezones.csv"));
			writer.writeAll(timezones);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		event.getChannel().sendMessage("Added new timezone of " + pieces[1] + " with an offset of " + pieces[2] + " hours from pst").queue();
	}

	private boolean checkNumber(String numb){
		char[] chars = numb.toCharArray();
		for(char chr : chars){
			if(!Character.isDigit(chr)){
				return false;
			}
		}
		return true;
	}
}
