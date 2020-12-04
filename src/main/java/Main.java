import Commands.Scheduling.EventCoordinator;
import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.RichPresence;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.concurrent.Task;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.security.auth.login.LoginException;
import java.util.List;

public class Main {
	private static JDA jda;
	private static String token = "NzMzNDA3NzA5MTMwMzkxNTgy.XxCtRA.J1502_EgZrAPggmaCpUvJ0AZJ1Y";
	private static final MessageProcessing messageProcessor = new MessageProcessing();
	private static final EventCoordinator eventCoordinator = new EventCoordinator();

	public static void main(String[] args) throws LoginException {
		jda = JDABuilder.createDefault(token).setActivity(Activity.of(Activity.ActivityType.DEFAULT, "with half a ship")).addEventListeners(messageProcessor).addEventListeners(eventCoordinator).enableIntents(GatewayIntent.GUILD_MEMBERS).build();
	}
}
