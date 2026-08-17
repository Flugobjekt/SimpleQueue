package us.ajg0702.queue.spigot.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Method;
import java.util.function.Consumer;

public class FoliaScheduler {

    private static final boolean IS_FOLIA;
    private static Method GET_GLOBAL_REGION_SCHEDULER;
    private static Method GET_ASYNC_SCHEDULER;
    private static Method GET_PLAYER_SCHEDULER;
    private static Method GLOBAL_RUN;
    private static Method GLOBAL_RUN_AT_FIXED_RATE;
    private static Method ASYNC_RUN_NOW;
    private static Method ENTITY_RUN;

    static {
        boolean folia = false;
        try {
            Class.forName("io.papermc.paper.threadedregions.RegionizedServer");
            folia = true;
            Class<?> bukkitClass = Bukkit.class;
            GET_GLOBAL_REGION_SCHEDULER = bukkitClass.getMethod("getGlobalRegionScheduler");
            GET_ASYNC_SCHEDULER = bukkitClass.getMethod("getAsyncScheduler");

            Class<?> globalSchedClass = GET_GLOBAL_REGION_SCHEDULER.getReturnType();
            GLOBAL_RUN = globalSchedClass.getMethod("run", Plugin.class, Consumer.class);
            GLOBAL_RUN_AT_FIXED_RATE = globalSchedClass.getMethod("runAtFixedRate", Plugin.class, Consumer.class, long.class, long.class);

            Class<?> asyncSchedClass = GET_ASYNC_SCHEDULER.getReturnType();
            ASYNC_RUN_NOW = asyncSchedClass.getMethod("runNow", Plugin.class, Consumer.class);

            try {
                GET_PLAYER_SCHEDULER = Player.class.getMethod("getScheduler");
                Class<?> entitySchedClass = GET_PLAYER_SCHEDULER.getReturnType();
                ENTITY_RUN = entitySchedClass.getMethod("run", Plugin.class, Consumer.class, Runnable.class);
            } catch (Exception ignored) {}
        } catch (Throwable ignored) {}
        IS_FOLIA = folia;
    }

    public static boolean isFolia() {
        return IS_FOLIA;
    }

    public static void runAsync(Plugin plugin, Runnable runnable) {
        if (IS_FOLIA && ASYNC_RUN_NOW != null && GET_ASYNC_SCHEDULER != null) {
            try {
                Object asyncSched = GET_ASYNC_SCHEDULER.invoke(null);
                Consumer<Object> consumer = task -> runnable.run();
                ASYNC_RUN_NOW.invoke(asyncSched, plugin, consumer);
                return;
            } catch (Exception ignored) {}
        }
        Bukkit.getScheduler().runTaskAsynchronously(plugin, runnable);
    }

    public static void runSync(Plugin plugin, Runnable runnable) {
        if (IS_FOLIA && GLOBAL_RUN != null && GET_GLOBAL_REGION_SCHEDULER != null) {
            try {
                Object globalSched = GET_GLOBAL_REGION_SCHEDULER.invoke(null);
                Consumer<Object> consumer = task -> runnable.run();
                GLOBAL_RUN.invoke(globalSched, plugin, consumer);
                return;
            } catch (Exception ignored) {}
        }
        Bukkit.getScheduler().runTask(plugin, runnable);
    }

    public static void runSyncForPlayer(Plugin plugin, Player player, Runnable runnable) {
        if (IS_FOLIA && player != null && ENTITY_RUN != null && GET_PLAYER_SCHEDULER != null) {
            try {
                Object entitySched = GET_PLAYER_SCHEDULER.invoke(player);
                Consumer<Object> consumer = task -> runnable.run();
                ENTITY_RUN.invoke(entitySched, plugin, consumer, null);
                return;
            } catch (Exception ignored) {}
        }
        runSync(plugin, runnable);
    }

    public static void runTimer(Plugin plugin, Runnable runnable, long delayTicks, long periodTicks) {
        if (IS_FOLIA && GLOBAL_RUN_AT_FIXED_RATE != null && GET_GLOBAL_REGION_SCHEDULER != null) {
            try {
                Object globalSched = GET_GLOBAL_REGION_SCHEDULER.invoke(null);
                Consumer<Object> consumer = task -> runnable.run();
                GLOBAL_RUN_AT_FIXED_RATE.invoke(globalSched, plugin, consumer, delayTicks, periodTicks);
                return;
            } catch (Exception ignored) {}
        }
        Bukkit.getScheduler().runTaskTimer(plugin, runnable, delayTicks, periodTicks);
    }
}
