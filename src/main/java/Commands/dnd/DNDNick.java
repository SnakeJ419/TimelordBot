package Commands.dnd;

import Commands.ServerCommand;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.apache.commons.collections.ListUtils;
import org.apache.commons.lang3.ArrayUtils;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class DNDNick extends ServerCommand {

	public DNDNick() {
		super("$dndnick");
	}

	@Override
	public void runCommand(MessageReceivedEvent event) {
		logger.log(Level.INFO, "User: [" + event.getAuthor().getName() + "] Channel: [" + event.getChannel().getName() + "] Guild: [" + event.getGuild().getName() + ']');

		List<String[]> nicks = null;
		Guild guild = event.getGuild();

		try {
			File file = new File("src\\main\\resources\\dndnick " + event.getGuild().getName() + ".csv");
			file.createNewFile();
			CSVReader reader = new CSVReader(new FileReader(file));
			nicks = reader.readAll();
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			File file = new File("src\\main\\resources\\nick " + event.getGuild().getName() + ".csv");
			file.createNewFile();
			CSVWriter fileCleaner = new CSVWriter(new FileWriter(file, false));
			fileCleaner.flush();
			fileCleaner.close();

			for(String[] nick : nicks){
				guild.retrieveMemberById(nick[0]).queue(member -> {
					logger.log(Level.CONFIG, ArrayUtils.toString(new String[]{member.getId(), member.getNickname()}));
					try {
						CSVWriter writer = new CSVWriter(new FileWriter(file, true));
						writer.writeNext(new String[]{member.getId(), member.getNickname()});
						writer.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
					logger.log(Level.FINE, "Changed: [" + member.getEffectiveName() + "] to: [" + nick[1] + ']');
					member.modifyNickname(nick[1]).queue();
				});
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		event.getChannel().sendMessage("Set to DND Nicknames").queue();
	}
}
