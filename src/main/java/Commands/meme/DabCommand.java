package Commands.meme;

import Commands.ServerCommand;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;

public class DabCommand extends ServerCommand {
	public DabCommand() {
		super("$yeet");
	}

	@Override
	public boolean check(MessageReceivedEvent event) {
		return event.getMessage().getContentRaw().toLowerCase().contains("dab");
	}

	@Override
	public void runCommand(MessageReceivedEvent event) {
		logger.log(Level.INFO, "User: [" + event.getAuthor().getName() + "] Channel: [" + event.getChannel().getName() + "] Guild: [" + event.getGuild().getName() + ']');

		User user = event.getAuthor();

		try {
			Image userPhoto = ImageIO.read(new URL(user.getAvatarUrl()));
			Image dabArms = ImageIO.read(new File("src\\main\\resources\\DAB.png"));

			// create the new image, canvas size is the max. of both image sizes
			int w = Math.max(userPhoto.getWidth(null), dabArms.getWidth(null));
			int h = Math.max(userPhoto.getHeight(null), dabArms.getHeight(null));
			BufferedImage combined = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

// paint both images, preserving the alpha channels
			Graphics g = combined.getGraphics();
			g.drawImage(userPhoto, 82, 41, null);
			g.drawImage(dabArms, 0, 0, null);

			File outputFile = new File("src\\main\\resources\\dankdaber.png");
			ImageIO.write(combined, "png", outputFile);
			event.getChannel().sendFile(new File("src\\main\\resources\\dankdaber.png")).queue();
		} catch (IOException e){
			logger.log(Level.SEVERE, e.getMessage());
		}
	}
}
