package me.jesusmx.hubcore;

import lombok.Getter;
import lombok.SneakyThrows;
import me.jesusmx.hubcore.commands.impl.media.*;
import me.jesusmx.hubcore.commands.impl.spawn.SetSpawnCommand;
import me.jesusmx.hubcore.listeners.*;
import me.jesusmx.hubcore.menus.selector.listeners.SelectorListener;
import me.jesusmx.hubcore.permissions.PermissionCore;
import me.jesusmx.hubcore.permissions.type.*;
import me.jesusmx.hubcore.queue.QueueManager;
import me.jesusmx.hubcore.scoreboard.sprovider;
import me.jesusmx.hubcore.tablist.tprovider;
import me.jesusmx.hubcore.util.CC;
import me.jesusmx.hubcore.util.bukkit.JesusMXLicense;
import me.jesusmx.hubcore.util.bungee.BungeeListener;
import me.jesusmx.hubcore.util.bungee.BungeeUpdateTask;
import me.jesusmx.hubcore.util.files.ConfigFile;
import me.jesusmx.hubcore.util.scoreboard.Assemble;
import me.jesusmx.hubcore.util.scoreboard.AssembleStyle;
import me.jesusmx.hubcore.util.tablist.shared.TabHandler;
import me.jesusmx.hubcore.util.tablist.v1_7_R4.v1_7_R4TabAdapter;
import me.jesusmx.hubcore.util.tablist.v1_8_R3.v1_8_R3TabAdapter;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

@Getter
public final class TacoHubLite extends JavaPlugin {

    @Getter private static TacoHubLite instance;

    public static Chat chat;

    private QueueManager queueManager;
    private PermissionCore permissionCore;

    private ConfigFile togglesConfig;
    private ConfigFile scoreboardConfig;
    private ConfigFile settingsConfig;
    private ConfigFile tablistConfig;
    private ConfigFile mainConfig;
    private ConfigFile selectorConfig;
    private ConfigFile placeholdersConfig;

    @Override
    public void onEnable() {
        TacoHubLite.instance = this;

        /*
        if (!this.getDescription().getName().equals("TacoHub") || !this.getDescription().getAuthors().contains("JesusMX")) {
            Bukkit.getPluginManager().disablePlugin(this);
            Bukkit.getConsoleSender().sendMessage(CC.translate("&cNO CHANGE AUTHOR IN PLUGIN.YML :)!"));
         */

        if (!getDataFolder().exists()) getDataFolder().mkdir();
        this.saveDefaultConfig();
        this.settingsConfig = new ConfigFile(this, "settings");


        if (!new JesusMXLicense(settingsConfig.getString("system.license")
                , "https://mexicanservices.000webhostapp.com/verify.php"
                , this).register())
            return;


        this.mainConfig = new ConfigFile(this, "config");
        this.togglesConfig = new ConfigFile(this, "toggles");
        this.scoreboardConfig = new ConfigFile(this, "scoreboard");
        this.tablistConfig = new ConfigFile(this, "tablist");
        this.selectorConfig = new ConfigFile(this, "selector");
        this.placeholdersConfig = new ConfigFile(this, "placeholders");
        this.getServer().getConsoleSender().sendMessage(CC.translate("&8[&bHub&8] &aThe files have been loaded correctly."));

        for (World world : Bukkit.getWorlds()) {
            world.setGameRuleValue("doDaylightCycle", "false");
            world.setGameRuleValue("doMobSpawning", "false");
            world.setTime(0L);
            world.setStorm(false);
        }

        queueManager = new QueueManager();
        this.commands();
        this.registerListeners();

        if (togglesConfig.getBoolean("features.scoreboard")) {
            Assemble assemble = new Assemble(this, new sprovider());
            assemble.setTicks(2);
            assemble.setAssembleStyle(AssembleStyle.CUSTOM.descending(true).startNumber(16));
            this.getServer().getConsoleSender().sendMessage(CC.translate("&8[&bHub&8] &aThe scoreboard has been loaded successfully."));
        }

        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        this.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new BungeeListener());
        this.getServer().getScheduler().runTaskTimerAsynchronously(this, new BungeeUpdateTask(), 0L, 20L);

        this.getServer().getConsoleSender().sendMessage(CC.translate("&8[&bHub&8] &aThe rank system " + this.permissions() + " &aHas been loaded successfully"));

        if (togglesConfig.getBoolean("features.tablist")) {
            if (Bukkit.getVersion().contains("1.7")) {
                new TabHandler(new v1_7_R4TabAdapter(), new tprovider(), this, 20L);
            }
            if (Bukkit.getVersion().contains("1.8")) {
                new TabHandler(new v1_8_R3TabAdapter(), new tprovider(), this, 20L);
            }
            Bukkit.getConsoleSender().sendMessage(CC.translate("&8[&bHub&8] &aThe tablist has been loaded correctly"));
        }

        RegisteredServiceProvider<Chat> chatProvider = this.getServer().getServicesManager().getRegistration(Chat.class);
        if (chatProvider != null) {
            chat = chatProvider.getProvider();
        }
    }

    @Override
    public void onDisable() {
    }

    private void commands() {
        Arrays.asList(new DiscordCommand(), new TeamSpeakCommand(), new TwitterCommand(), new SetSpawnCommand(), new TelegramCommand(), new StoreCommand()).forEach(this::registerCommand);
    }

    private String permissions() {
        String core = settingsConfig.getString("system.rank");
        switch (core) {
            case "AquaCore":
                permissionCore = new AquaCorePermissionCore();
                return "AquaCore";
            case "Vault":
                permissionCore = new VaultPermissionCore();
                return "Vault";
        }
        return "Nothing";
    }

    private void registerListeners() {
        Arrays.asList(new WorldListener(), new ProtectionListener(), new DoubleJumpListener(), new JoinPlayerListener(), new ChatListener(), new MovePlayerListener(), new EnderButtListener(), new SelectorListener(), new HidePlayersListener()).forEach(listener -> this.getServer().getPluginManager().registerEvents(listener, this));
    }

    @SneakyThrows
    public void registerCommand(Command cmd) {
        Field field = getServer().getClass().getDeclaredField("commandMap");
        field.setAccessible(true);
        CommandMap commandMap = (CommandMap) field.get(getServer());
        commandMap.register(this.getName(), cmd);
    }
    public Collection<? extends Player> getOnlinePlayers() {
        Collection<Player> collection = new ArrayList<>();
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            collection.add(player);
        }
        return collection;
    }
}
