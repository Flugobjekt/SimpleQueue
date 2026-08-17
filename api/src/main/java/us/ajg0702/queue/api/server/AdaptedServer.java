package us.ajg0702.queue.api.server;

import us.ajg0702.queue.api.players.AdaptedPlayer;
import us.ajg0702.queue.api.util.Handle;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@SuppressWarnings("unused")
public interface AdaptedServer extends Handle {





    AdaptedServerInfo getServerInfo();





    String getName();





    CompletableFuture<AdaptedServerPing> ping();








    @SuppressWarnings("SameReturnValue")
    boolean canAccess(AdaptedPlayer player);

    List<AdaptedPlayer> getPlayers();
}
