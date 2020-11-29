package Commands;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.logging.Level;

public class iSearch extends ServerCommand {
	private static final String GOOGLE_SEARCH_URL = "https://images.google.com/search";
	private static final int results = 10;

	public iSearch() {
		super("i", false, true);
	}

	@Override
	public void runCommand(MessageReceivedEvent event) {
		logger.log(Level.INFO, "User: [" + event.getAuthor().getName() + "] Channel: [" + event.getChannel().getName() + "] Guild: [" + event.getGuild().getName() + ']');

		String keyword = event.getMessage().getContentRaw().split(" ")[1];

		String searchURL = GOOGLE_SEARCH_URL + "?q="+keyword+"&num="+results;
		//without proper User-Agent, we will get 403 error
		Document doc = null;
		try {
			doc = Jsoup.connect(searchURL).userAgent("Mozilla/5.0").get();
		} catch (IOException e) {
			e.printStackTrace();
		}

		//below will print HTML data, save it to a file and open in browser to compare
		//System.out.println(doc.html());

		//If google search results HTML change the <h3 class="r" to <h3 class="r1"
		//we need to change below accordingly
		Elements results = doc.select("h3.r > a");
		System.out.println(results.toString());

	}
}
