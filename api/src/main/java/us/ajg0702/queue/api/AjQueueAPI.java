package us.ajg0702.queue.api;

import us.ajg0702.queue.api.premium.Logic;
import us.ajg0702.queue.api.premium.LogicGetter;
import us.ajg0702.queue.api.util.QueueLogger;
import us.ajg0702.utils.common.Config;
import us.ajg0702.utils.common.Messages;

public abstract class AjQueueAPI {

    public static AjQueueAPI INSTANCE;





    @SuppressWarnings("unused")
    public static AjQueueAPI getInstance() {
        return INSTANCE;
    }






    public abstract double getTimeBetweenPlayers();




    public abstract void setTimeBetweenPlayers();





    public abstract Config getConfig();





    public abstract Messages getMessages();






    public abstract AliasManager getAliasManager();





    public abstract Logic getLogic();





    public abstract boolean isPremium();






    public abstract PlatformMethods getPlatformMethods();





    public abstract QueueLogger getLogger();







    public abstract EventHandler getEventHandler();





    public abstract QueueManager getQueueManager();





    public abstract LogicGetter getLogicGetter();






    public abstract ProtocolNameManager getProtocolNameManager();




    public abstract void shutdown();
}
