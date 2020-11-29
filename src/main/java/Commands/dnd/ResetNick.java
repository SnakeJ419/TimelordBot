package Commands.dnd;

import Commands.ServerCommand;
import com.opencsv.CSVReader;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;

public class ResetNick extends ServerCommand {
	public ResetNick() {
		super("$resetnick");
	}

	@Override
	public void runCommand(MessageReceivedEvent event) {
		logger.log(Level.INFO, "User: [" + event.getAuthor().getName() + "] Channel: [" + event.getChannel().getName() + "] Guild: [" + event.getGuild().getName() + ']');

		List<String[]> nicks = null;
		try {
			File file = new File("src\\main\\resources\\nick " + event.getGuild().getName() + ".csv");
			file.createNewFile();
			CSVReader reader = new CSVReader(new FileReader(file));
			nicks = reader.readAll();
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Guild guild = event.getGuild();
		for(String[] nick : nicks){
			guild.retrieveMemberById(nick[0]).queue(member -> {
				logger.log(Level.FINE, "Changed: [" + member.getEffectiveName() + "] to: [" + nick[1] + ']');
				member.modifyNickname(nick[1]).queue();
			});
		}

		event.getChannel().sendMessage("Set to normal nicknames").queue();
	}
}
