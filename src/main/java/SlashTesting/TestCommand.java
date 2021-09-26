package SlashTesting;

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

public class TestCommand implements SlashCommand{
	@Override
	public void run(SlashCommandEvent event) {
		if(event.getOptions().size() > 0){
			String input = event.getOptions().get(0).getAsString();
			event.reply("Thanks for saying: " + input).queue();
		} else {
			event.reply("wow you said nothing ok").queue();
		}
	}
}
