package us.ajg0702.queue.api.players;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import us.ajg0702.queue.api.server.AdaptedServer;
import us.ajg0702.queue.api.util.Handle;

import java.util.List;
import java.util.UUID;




@SuppressWarnings("unused")
public interface AdaptedPlayer extends Handle, Audience {





    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    boolean isConnected();





    void sendMessage(@NotNull Component message);





    void sendActionBar(@NotNull Component message);






    void sendMessage(String message);






    boolean hasPermission(String permission);





    String getServerName();





    UUID getUniqueId();





    void connect(AdaptedServer server);





    int getProtocolVersion();





    String getName();





    void kick(Component reason);

    List<String> getPermissions();

    default boolean equals(AdaptedPlayer other) {
        return this.getUniqueId().equals(other.getUniqueId());
    }
}
