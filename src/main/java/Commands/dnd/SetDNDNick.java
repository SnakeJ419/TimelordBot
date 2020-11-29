package Commands.dnd;

import Commands.ServerCommand;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;

public class SetDNDNick extends ServerCommand {
	public SetDNDNick() {
		super("$setdndnick");
	}

	@Override
	public void runCommand(MessageReceivedEvent event) {
		logger.log(Level.INFO, "User: [" + event.getAuthor().getName() + "] Channel: [" + event.getChannel().getName() + "] Guild: [" + event.getGuild().getName() + ']');

		String id = event.getMember().getId();

		String newNick;
		try {
			newNick = event.getMessage().getContentRaw().split(" ")[1];
		} catch(Exception e){
			newNick = event.getMessage().getContentRaw().split(" ")[1];
			System.out.println("User with no nickname");
		}

		List<String[]> nicks = null;
		try {
			File file = new File("src\\main\\resources\\dndnick " + event.getGuild().getName() + ".csv");
			file.createNewFile();
			CSVReader reader = new CSVReader(new FileReader(file));
			nicks = reader.readAll();
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		boolean repeat = false;
		String[] nick;
		for(int i = 0; i < nicks.size(); i++){
			nick = nicks.get(i);
			if(nick[0].equals(id)){
				nicks.set(i, new String[]{id, newNick});
				repeat = true;
				break;
			}
		}
		if(!repeat){
			nicks.add(new String[]{id, newNick});
		}

		try {
			CSVWriter writer = new CSVWriter(new FileWriter("src\\main\\resources\\dndnick " + event.getGuild().getName() + ".csv"));
			writer.writeAll(nicks);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		event.getChannel().sendMessage("DND Nick of: " + event.getMember().getEffectiveName() + " saved as: " + newNick).queue();
		logger.log(Level.FINE, "DND Nick of: " + event.getMember().getEffectiveName() + " saved as: " + newNick);
	}
}