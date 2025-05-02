package discord.bot;

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.security.auth.login.LoginException;
import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.requests.GatewayIntent;

public class Main {
    public static void main(String[] args) {
        Dotenv dotenv  = Dotenv.load();
        String token = dotenv.get("DISCORD_TOKEN");

        // Включаем интенты для получения содержимого сообщений
        JDABuilder builder = JDABuilder.createDefault(token);
        builder.enableIntents(GatewayIntent.MESSAGE_CONTENT);  // Включаем MESSAGE_CONTENT интент
        builder.enableIntents(GatewayIntent.AUTO_MODERATION_EXECUTION);
        builder.addEventListeners(new MessageListener()).build();
    }
}
