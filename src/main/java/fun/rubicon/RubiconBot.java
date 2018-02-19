/*
 * Copyright (c) 2018  Rubicon Bot Development Team
 * Licensed under the GPL-3.0 license.
 * The full license text is available in the LICENSE file provided with this project.
 */

package fun.rubicon;

import fun.rubicon.command.CommandManager;
import fun.rubicon.commands.botowner.CommandBotPlay;
import fun.rubicon.commands.botowner.CommandEval;
import fun.rubicon.commands.test.CommandFirstCommandEver;
import fun.rubicon.listener.BotJoinListener;
import fun.rubicon.mysql.DatabaseGenerator;
import fun.rubicon.mysql.MySQL;
import fun.rubicon.permission.PermissionManager;
import fun.rubicon.util.*;
import net.dv8tion.jda.bot.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.bot.sharding.ShardManager;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.User;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Rubicon-bot's main class. Initializes all components.
 *
 * @author tr808axm
 */
public class RubiconBot {
    private static final SimpleDateFormat timeStampFormatter = new SimpleDateFormat("MM.dd.yyyy HH:mm:ss");
    private static final String[] CONFIG_KEYS = {"token", "mysql_host", "mysql_database", "mysql_user", "mysql_password","playingStatus","dbl_token","discord_pw_token"};
    private static RubiconBot instance;
    private final Configuration configuration;
    private final MySQL mySQL;
    private final CommandManager commandManager;
    private final PermissionManager permissionManager;
    private ShardManager shardManager;

    private static final int SHARD_COUNT = 5;


    /**
     * Constructs the RubiconBot.
     */
    private RubiconBot() {
        instance = this;

        new File("rubicon_logs").mkdirs();
        new File("data/").mkdirs();
        Logger.logInFile(Info.BOT_NAME, Info.BOT_VERSION, "rubicon_logs/");

        //Init config
        configuration = new Configuration(new File(Info.CONFIG_FILE));
        for (String configKey : CONFIG_KEYS) {
            if (!configuration.has(configKey)) {
                String input = ConfigSetup.prompt(configKey);
                configuration.set(configKey, input);
            }
        }
        //Init MySQL Connection
        mySQL = new MySQL(
                configuration.getString("mysql_host"),
                "3306", configuration.getString("mysql_user"),
                configuration.getString("mysql_password"),
                configuration.getString("mysql_database"));
        mySQL.connect();

        DatabaseGenerator.createAllDatabasesIfNecessary();
        //Post Guild Stats
        BotListHandler.postStats(false);

        commandManager = new CommandManager();
        registerCommands();
        permissionManager = new PermissionManager();

        initShardManager();
    }

    private void registerCommands() {
        commandManager.registerCommandHandlers(
                new CommandFirstCommandEver(),
                new CommandEval(),
                new CommandBotPlay()
        );
    }

    /**
     * Initializes the bot.
     *
     * @param args command line parameters.
     */
    public static void main(String[] args) {
        if (instance != null)
            throw new RuntimeException("RubiconBot has already been initialized in this VM.");
        new RubiconBot();
    }

    /**
     * Initializes the JDA instance.
     */
    private void initShardManager() {
        if (instance == null)
            throw new NullPointerException("RubiconBot has not been initialized yet.");

        DefaultShardManagerBuilder builder = new DefaultShardManagerBuilder();
        builder.setToken(instance.configuration.getString("token"));
        builder.setGame(Game.playing("Starting..."));
        builder.setStatus(OnlineStatus.DO_NOT_DISTURB);
        builder.setShardsTotal(SHARD_COUNT);

        //Register Event Listeners

        builder.addEventListeners(
                new BotJoinListener(),
                commandManager
        );
        try {
            shardManager = builder.build();
            shardManager.setGame(Game.playing("Started!"));
            shardManager.setStatus(OnlineStatus.ONLINE);
        } catch (LoginException e) {
            Logger.error(e);
        }
    }

    /**
     * @return the {@link ShardManager} that is used in the Rubicon project
     */
    public static ShardManager getShardManager() {
        return instance == null ? null : instance.shardManager;
    }

    /**
     * @return the Rubicon {@link User} instance
     */
    public static User getSelfUser() {
        return instance == null ? null : instance.shardManager.getApplicationInfo().getJDA().getSelfUser();
    }

    /**
     * @return the bot configuration.
     */
    public static Configuration getConfiguration() {
        return instance == null ? null : instance.configuration;
    }

    public static CommandManager getCommandManager() {
        return instance == null ? null : instance.commandManager;
    }

    public PermissionManager getPermissionManager() {
        return instance.permissionManager;
    }

    /**
     * @return the {@link PermissionManager} via a static reference.
     */
    public static PermissionManager sGetPermissionManager() {
        return instance == null ? null : instance.permissionManager;
    }

    /**
     * @return the rubicon instance
     */
    public static RubiconBot getRubiconBot() {
        return instance;
    }

    /**
     * @return the maximum shard count
     */
    public static int getMaximumShardCount() {
        return SHARD_COUNT;
    }

    /**
     * @return a freshly generated timestamp in the 'dd.MM.yyyy HH:mm:ss' format.
     */
    public static String getNewTimestamp() {
        return timeStampFormatter.format(new Date());
    }

    /**
     * @return the {@link MySQL} instance
     */
    public static MySQL getMySQL() {
        return instance == null ? null : instance.mySQL;
    }
}