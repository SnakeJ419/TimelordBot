package Commands.Util;

import Commands.ServerCommand;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Threadify extends ServerCommand {
    public Threadify() {
        super("$threadify", false, true);
    }

    @Override
    public void runCommand(MessageReceivedEvent event) {
        //TODO: Parse the requested number of messages and who sent them

        //TODO: Start a new thread

        //TODO: Send The messages and attribute them to the user
    }
}
