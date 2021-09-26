package SlashTesting;

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

public interface SlashCommand {
	void run(SlashCommandEvent event);
}
