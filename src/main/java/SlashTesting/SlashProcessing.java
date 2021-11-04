package SlashTesting;

import Commands.meme.Suppress;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class SlashProcessing extends ListenerAdapter {
	private final HashMap<String, SlashCommand> commandMap = new HashMap<>();

	public void load(JDA jda){
		CommandListUpdateAction commands = jda.getGuildById(766543396222795828L).updateCommands();

		commandMap.put("test", new TestCommand());
		commands.addCommands(new CommandData("test", "This is for Testing")
				.addOptions(new OptionData(OptionType.STRING, "input", "if you want to have an input on the command put it here")
						.setRequired(false)));

		commandMap.put("suppress", new Suppress());
		commands.addCommands(new CommandData("suppress", "prevents a user from messaging or speaking")
				.addOptions(new OptionData(OptionType.USER, "user", "user to be suppressed")
						.setRequired(true)));

		commands.queue();
	}

	@Override
	public void onSlashCommand(@NotNull SlashCommandEvent event) {
		commandMap.get(event.getName()).run(event);
	}
}
