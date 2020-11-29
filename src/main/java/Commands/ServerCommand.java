package Commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import util.LogFormatter;

import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class ServerCommand {
	private final long betaServerID = 766543396222795828L;

	private final String keyword;
	private final boolean adminProtected;
	private final boolean betaCommand;
	protected final Logger logger;

	private static FileHandler fileLog;

	static {
		try {
			fileLog = new FileHandler("src/main/resources/TimeLordLog.log", true);
			fileLog.setFormatter(new LogFormatter());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public ServerCommand(String keyword){
		this(keyword, false);
	}

	public ServerCommand(String keyword, boolean adminProtected){
		this(keyword, adminProtected, false);
	}

	public ServerCommand(String keyword, boolean adminProtected, boolean betaCommand){
		this.betaCommand = betaCommand;
		this.keyword = keyword;
		this.adminProtected = adminProtected;

		logger = Logger.getLogger(this.getClass().getSimpleName());
		logger.setUseParentHandlers(false);
		ConsoleHandler handler = new ConsoleHandler();
		handler.setFormatter(new LogFormatter());
		handler.setLevel(Level.ALL);
		logger.addHandler(handler);
		logger.addHandler(fileLog);
		logger.setLevel(Level.ALL);
	}

	public boolean check(MessageReceivedEvent event){
		if(!betaCommand || event.getGuild().getIdLong() == betaServerID) {
			if (keyword.equals(event.getMessage().getContentRaw().toLowerCase().split(" ")[0])) {
				if (adminProtected && !event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
					event.getChannel().sendMessage("This command is admin protected").queue();
					return false;
				} else {
					return true;
				}
			}
		}
		return false;
	}

	public abstract void runCommand(MessageReceivedEvent event);
}
