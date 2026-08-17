package us.ajg0702.queue.api;

import us.ajg0702.queue.api.commands.IBaseCommand;
import us.ajg0702.queue.api.commands.ICommandSender;
import us.ajg0702.queue.api.players.AdaptedPlayer;
import us.ajg0702.queue.api.server.AdaptedServer;

import java.util.List;
import java.util.UUID;

public interface PlatformMethods {







    void sendPluginMessage(AdaptedPlayer player, String channel, String... data);






    AdaptedPlayer senderToPlayer(ICommandSender sender);

    String getPluginVersion();

    @SuppressWarnings("unused")
    List<AdaptedPlayer> getOnlinePlayers();
    List<String> getPlayerNames(boolean lowercase);






    AdaptedPlayer getPlayer(String name);






    AdaptedPlayer getPlayer(UUID uuid);





    List<String> getServerNames();





    String getImplementationName();

    List<IBaseCommand> getCommands();






    boolean hasPlugin(String pluginName);






    AdaptedServer getServer(String name);

    List<AdaptedServer> getServers();

    String getProtocolName(int protocol);
}
