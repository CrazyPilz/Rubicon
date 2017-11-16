package de.rubicon.core;

import de.rubicon.util.Info;

import de.rubicon.util.Logger;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.exceptions.RateLimitedException;

import javax.security.auth.login.LoginException;

public class DiscordCore {

    private JDA jda;

    public void start() {
        Info.init();

        JDABuilder builder = new JDABuilder(AccountType.BOT);
        builder.setToken(Info.BOT_TOKEN);
        builder.setGame(Game.of(Info.BOT_NAME + " " + Info.BOT_VERSION));

        new ListenerManager(builder);
        new CommandManager();

        try {
            jda = builder.buildBlocking();
        } catch (LoginException e) {
            Logger.error(e.getMessage());
        } catch (InterruptedException e) {
            Logger.error(e.getMessage());
        } catch (RateLimitedException e) {
            Logger.error(e.getMessage());
        }
    }

    public JDA getJDA() {
        return jda;
    }
}