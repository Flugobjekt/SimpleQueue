package us.ajg0702.queue.api.server;

import net.kyori.adventure.text.Component;
import us.ajg0702.queue.api.util.Handle;

@SuppressWarnings("unused")
public interface AdaptedServerPing extends Handle {




    Component getDescriptionComponent();





    String getPlainDescription();





    int getPlayerCount();





    int getMaxPlayers();




    void addPlayer();
}
