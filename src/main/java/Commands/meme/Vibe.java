package Commands.meme;

import Commands.ServerCommand;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Random;

public class Vibe extends ServerCommand {
	private final static Random r = new Random();

	public Vibe() {
		super("$vibe");
	}

	@Override
	public void runCommand(MessageReceivedEvent event) {
		event.getChannel().sendMessage("Vibe is " + (r.nextInt(12) + 1) + " out of " + (r.nextInt(13) + 1)).queue();
	}
}
