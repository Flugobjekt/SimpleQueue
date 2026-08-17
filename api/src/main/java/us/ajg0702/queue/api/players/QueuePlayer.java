package us.ajg0702.queue.api.players;

import us.ajg0702.queue.api.queues.QueueServer;

import javax.annotation.Nullable;
import java.util.UUID;

public interface QueuePlayer {





    UUID getUniqueId();





    QueueServer getQueueServer();





    int getPosition();






    @Nullable AdaptedPlayer getPlayer();






    void setPlayer(AdaptedPlayer player);






    int getPriority();




    boolean hasPriority();





    String getName();





    long getTimeSinceOnline();





    int getMaxOfflineTime();
}
