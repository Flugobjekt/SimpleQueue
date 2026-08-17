package us.ajg0702.queue.api.premium;

import us.ajg0702.queue.api.AjQueueAPI;
import us.ajg0702.queue.api.players.AdaptedPlayer;
import us.ajg0702.queue.api.players.QueuePlayer;
import us.ajg0702.queue.api.queues.QueueServer;
import us.ajg0702.utils.common.Config;

@SuppressWarnings({"SameReturnValue", "unused"})

public interface Logic {




    boolean isPremium();






    QueuePlayer priorityLogic(QueueServer server, AdaptedPlayer player);






    boolean playerDisconnectedTooLong(QueuePlayer player);





    PermissionGetter getPermissionGetter();

    static int getUnJoinablePriorities(QueueServer server, AdaptedPlayer player) {
        Config config = AjQueueAPI.getInstance().getConfig();
        int highest = 0;

        int whitelitedPriority = config.getInt("give-whitelisted-players-priority");
        int bypassPausedPriotity = config.getInt("give-whitelisted-players-priority");
        int fulljoinPriority = config.getInt("give-whitelisted-players-priority");

        if(whitelitedPriority > 0) {
            if(server.isWhitelisted() && server.getWhitelistedPlayers().contains(player.getUniqueId())) {
                highest = whitelitedPriority;
            }
        }

        if(bypassPausedPriotity > 0) {
            if(server.isPaused() && (player.hasPermission("ajqueue.bypasspaused"))) {
                highest = Math.max(highest, bypassPausedPriotity);
            }
        }

        if(fulljoinPriority > 0) {
            if(server.isFull() && server.canJoinFull(player)) {
                highest = Math.max(highest, fulljoinPriority);
            }
        }


        return highest;
    }
}
