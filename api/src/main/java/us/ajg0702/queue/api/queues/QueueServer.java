package us.ajg0702.queue.api.queues;

import com.google.common.collect.ImmutableList;
import us.ajg0702.queue.api.players.AdaptedPlayer;
import us.ajg0702.queue.api.players.QueuePlayer;
import us.ajg0702.queue.api.server.AdaptedServer;
import us.ajg0702.queue.api.server.AdaptedServerPing;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;




@SuppressWarnings({"unused", "BooleanMethodIsAlwaysInverted"})
public interface QueueServer {





    ImmutableList<QueuePlayer> getQueue();






    String getStatusString(AdaptedPlayer p);






    String getStatusString();






    String getStatus(AdaptedPlayer p);






    String getStatus();




    void updatePing();





    int getOfflineTime();





    long getLastSentTime();





    void setLastSentTime(long lastSentTime);







    boolean isWhitelisted();




    void setWhitelisted(boolean whitelisted);





    ImmutableList<UUID> getWhitelistedPlayers();




    void setWhitelistedPlayers(List<UUID> whitelistedPlayers);






    boolean isJoinable(AdaptedPlayer p);





    void setPaused(boolean paused);





    boolean isPaused();





    boolean isOnline();





    boolean justWentOnline();





    boolean isFull();





    void removePlayer(QueuePlayer player);





    void removePlayer(AdaptedPlayer player);






    void addPlayer(QueuePlayer player);







    void addPlayer(QueuePlayer player, int position);




    void sendPlayer();





    String getName();








    boolean canAccess(AdaptedPlayer ply);





    String getAlias();





    ImmutableList<AdaptedServer> getServers();





    ImmutableList<String> getServerNames();






    boolean isGroup();





    QueuePlayer findPlayer(String player);





    QueuePlayer findPlayer(AdaptedPlayer player);





    QueuePlayer findPlayer(UUID uuid);







    AdaptedServer getIdealServer(AdaptedPlayer player);





    HashMap<AdaptedServer, AdaptedServerPing> getLastPings();






    List<Integer> getSupportedProtocols();






    void setSupportedProtocols(List<Integer> list);





    Balancer getBalancer();






    boolean canJoinFull(AdaptedPlayer player);




    void addPlayer(AdaptedServer server);







    void setOnline(boolean online);






    @SuppressWarnings({"unused", "SameReturnValue"})
    default boolean elliot_is_bad() {
        return true;
    }
}
