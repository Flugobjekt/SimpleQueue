package us.ajg0702.queue.platforms.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.connection.PluginMessageEvent;
import com.velocitypowered.api.event.player.KickedFromServerEvent;
import com.velocitypowered.api.event.player.ServerPostConnectEvent;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier;
import net.kyori.adventure.text.Component;
import org.bstats.charts.SimplePie;
import org.bstats.velocity.Metrics;
import org.slf4j.Logger;
import us.ajg0702.queue.api.Implementation;
import us.ajg0702.queue.api.commands.IBaseCommand;
import us.ajg0702.queue.commands.BaseCommand;
import us.ajg0702.queue.commands.commands.leavequeue.LeaveCommand;
import us.ajg0702.queue.commands.commands.listqueues.ListCommand;
import us.ajg0702.queue.commands.commands.manage.ManageCommand;
import us.ajg0702.queue.commands.commands.queue.QueueCommand;
import us.ajg0702.queue.common.QueueMain;
import us.ajg0702.queue.platforms.velocity.commands.VelocityCommand;
import us.ajg0702.queue.platforms.velocity.players.VelocityPlayer;
import us.ajg0702.queue.platforms.velocity.server.VelocityServer;

import com.velocitypowered.api.event.player.ServerPreConnectEvent;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import us.ajg0702.queue.api.queues.QueueServer;

import java.io.File;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Plugin(
        id = "simplequeue",
        name = "SimpleQueue",
        version = "@VERSION@",
        url = "https://ajg0702.us",
        description = "Queue for servers",
        authors = {"Flugobjekt"}
)

public class VelocityQueue implements Implementation {
    final ProxyServer proxyServer;
    final VelocityLogger logger;

    QueueMain main;

    final File dataFolder;

    private final Metrics.Factory metricsFactory;

    private static final Set<UUID> allowedConnects = Collections.newSetFromMap(new ConcurrentHashMap<>());

    public static void allowNextConnect(UUID playerUuid) {
        allowedConnects.add(playerUuid);
    }

    public static boolean consumeAllowedConnect(UUID playerUuid) {
        return allowedConnects.remove(playerUuid);
    }

    @Inject
    public VelocityQueue(ProxyServer proxyServer, Logger logger, @DataDirectory Path dataFolder, Metrics.Factory metricsFactory) {
        this.proxyServer = proxyServer;
        this.logger = new VelocityLogger(logger);

        this.dataFolder = dataFolder.toFile();

        this.metricsFactory = metricsFactory;
    }

    List<IBaseCommand> commands;

    CommandManager commandManager;

    @Subscribe
    public void onProxyInit(ProxyInitializeEvent e) {

        commandManager = proxyServer.getCommandManager();

        main = new QueueMain(
                this,
                logger,
                new VelocityMethods(this, proxyServer, logger),
                dataFolder
        );

        commands = Arrays.asList(
                new QueueCommand(main),
                new LeaveCommand(main),
                new ListCommand(main),
                new ManageCommand(main)
        );


        proxyServer.getChannelRegistrar().register(MinecraftChannelIdentifier.create("simplequeue", "tospigot"));
        proxyServer.getChannelRegistrar().register(MinecraftChannelIdentifier.from("simplequeue:toproxy"));
        proxyServer.getChannelRegistrar().register(MinecraftChannelIdentifier.create("ajqueue", "tospigot"));
        proxyServer.getChannelRegistrar().register(MinecraftChannelIdentifier.from("ajqueue:toproxy"));


        for(IBaseCommand command : commands) {
            registerCommand(command);
        }


        Metrics metrics = metricsFactory.make(this, 7404);

        metrics.addCustomChart(new SimplePie("premium", () -> String.valueOf(main.getLogic().isPremium())));
        metrics.addCustomChart(new SimplePie("implementation", () -> main.getPlatformMethods().getImplementationName()));
    }

    @Subscribe
    public void onProxyShutdown(ProxyShutdownEvent e) {
        main.shutdown();
    }

    @Subscribe
    public void onPluginMessage(PluginMessageEvent e) {

        if(e.getIdentifier().getId().equals("ajqueue:tospigot") || e.getIdentifier().getId().equals("simplequeue:tospigot")) {
            e.setResult(PluginMessageEvent.ForwardResult.handled());
            return;
        }
        if(!e.getIdentifier().getId().equals("ajqueue:toproxy") && !e.getIdentifier().getId().equals("simplequeue:toproxy")) return;
        e.setResult(PluginMessageEvent.ForwardResult.handled());

        if(!(e.getTarget() instanceof Player)) return;

        main.getEventHandler().handleMessage(new VelocityPlayer((Player) e.getTarget()), e.getData());
    }

    @Subscribe
    public void onPreConnect(ServerPreConnectEvent e) {
        Player player = e.getPlayer();
        if (consumeAllowedConnect(player.getUniqueId())) {
            return;
        }

        if (!player.getCurrentServer().isPresent()) {
            return;
        }

        if (!main.getConfig().getBoolean("intercept-server-switch")) {
            return;
        }

        RegisteredServer target = e.getOriginalServer();
        if (target == null) {
            return;
        }

        String currentServerName = player.getCurrentServer().get().getServerInfo().getName();
        String targetName = target.getServerInfo().getName();

        if (currentServerName.equalsIgnoreCase(targetName)) {
            return;
        }

        if (player.hasPermission("simplequeue.bypass") ||
                player.hasPermission("ajqueue.bypass") ||
                player.hasPermission("simplequeue.serverbypass." + targetName) ||
                player.hasPermission("ajqueue.serverbypass." + targetName) ||
                player.hasPermission("simplequeue.joinfullandbypass") ||
                player.hasPermission("ajqueue.joinfullandbypass") ||
                player.hasPermission("simplequeue.joinfullandbypassserver." + targetName) ||
                player.hasPermission("ajqueue.joinfullandbypassserver." + targetName)) {
            return;
        }

        e.setResult(ServerPreConnectEvent.ServerResult.denied());

        QueueServer queueServer = main.getQueueManager().findServer(targetName);
        if (queueServer != null) {
            main.getQueueManager().addToQueue(new VelocityPlayer(player), queueServer);
        }
    }

    @SuppressWarnings("UnstableApiUsage")
    @Subscribe
    public void onJoin(ServerPostConnectEvent e) {
        if(e.getPreviousServer() == null) { 
            main.getEventHandler().onPlayerJoin(new VelocityPlayer(e.getPlayer()));
        }
        main.getEventHandler().onPlayerJoinServer(new VelocityPlayer(e.getPlayer()));
    }

    @Subscribe
    public void onLeave(DisconnectEvent e) {
        main.getEventHandler().onPlayerLeave(new VelocityPlayer(e.getPlayer()));
    }

    @Subscribe
    public void onKick(KickedFromServerEvent e) {
        if(!e.getPlayer().getCurrentServer().isPresent()) return; 
        Optional<Component> reasonOptional = e.getServerKickReason();
        main.getEventHandler().onServerKick(
                new VelocityPlayer(e.getPlayer()),
                new VelocityServer(e.getServer()),
                reasonOptional.orElseGet(() -> Component.text("Proxy lost connection")),

                e.kickedDuringServerConnect()
        );
    }

    @Override
    public void unregisterCommand(String name) {
        commandManager.unregister(name);
    }

    @Override
    public void registerCommand(IBaseCommand command) {
        commandManager.register(
                commandManager.metaBuilder(command.getName())
                        .aliases(command.getAliases().toArray(new String[]{}))
                        .build(),
                new VelocityCommand(main, (BaseCommand) command)
        );
    }
}
