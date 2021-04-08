package Commands.meme;

import Commands.ServerCommand;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.io.File;

public class Proof extends ServerCommand {
	public Proof(){
		super("$proof");
	}

	@Override
	public void runCommand(MessageReceivedEvent event) {
		event.getChannel().sendFile(new File("C:\\Users\\Joshua\\Downloads\\Screenshot 2021-01-27 084629.jpg")).queue();
	}
}
