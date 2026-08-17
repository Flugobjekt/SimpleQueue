package us.ajg0702.queue.api;

import com.google.common.collect.ImmutableList;
import us.ajg0702.queue.api.players.AdaptedPlayer;
import us.ajg0702.queue.api.players.QueuePlayer;
import us.ajg0702.queue.api.queues.QueueServer;

import java.util.Map;

public interface QueueManager {







    boolean addToQueue(AdaptedPlayer player, QueueServer server);







    @SuppressWarnings("UnusedReturnValue")
    boolean addToQueue(AdaptedPlayer player, String serverName);





    ImmutableList<QueueServer> getServers();





    ImmutableList<String> getServerNames();






    QueueServer getSingleServer(AdaptedPlayer player);







    String getQueuedName(AdaptedPlayer player);







    void reloadServers();




    void sendActionBars();




    void sendTitles();




    void sendQueueEvents();




    void sendMessages();





    void sendMessage(QueuePlayer queuePlayer);




    void updateServers();






    QueueServer findServer(String name);




    void sendPlayers();





    void sendPlayers(QueueServer server);






    ImmutableList<QueuePlayer> findPlayerInQueues(AdaptedPlayer p);






    ImmutableList<QueuePlayer> findPlayerInQueuesByName(String name);






    ImmutableList<QueueServer> getPlayerQueues(AdaptedPlayer p);

    void clear(AdaptedPlayer player);

    Map<QueuePlayer, Integer> getSendingAttempts();
}
