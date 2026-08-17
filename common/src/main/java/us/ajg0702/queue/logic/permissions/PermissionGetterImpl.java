package us.ajg0702.queue.logic.permissions;

import us.ajg0702.queue.api.players.AdaptedPlayer;
import us.ajg0702.queue.api.premium.PermissionGetter;
import us.ajg0702.queue.api.premium.PermissionHook;
import us.ajg0702.queue.common.QueueMain;
import us.ajg0702.queue.logic.permissions.hooks.AquaCoreHook;
import us.ajg0702.queue.logic.permissions.hooks.BuiltInHook;
import us.ajg0702.queue.logic.permissions.hooks.LuckPermsHook;
import us.ajg0702.queue.logic.permissions.hooks.UltraPermissionsHook;

import java.util.*;

public class PermissionGetterImpl implements PermissionGetter {

    private final List<PermissionHook> hooks;

    private final QueueMain main;
    public PermissionGetterImpl(QueueMain main) {
        hooks = Arrays.asList(
                new BuiltInHook(main),
                new LuckPermsHook(main),
                new UltraPermissionsHook(main),
                new AquaCoreHook(main)
        );
        this.main = main;
    }

    private PermissionHook selected;
    @Override
    public PermissionHook getSelected() {
        if(selected != null) return selected;
        if(hooks == null) {
            throw new IllegalStateException("Hooks are not initialized yet!");
        }
        for(PermissionHook hook : hooks) {
            if(hook.canUse()) {
                selected = hook;
            }
        }
        if(selected == null) {
            throw new IllegalStateException("All hooks are unusable!");
        }
        main.getLogger().info("Using "+selected.getName()+" for permissions.");
        return selected;
    }

    @Override
    public int getMaxOfflineTime(AdaptedPlayer player) {
        int sq = getHighestPermission(player, "simplequeue.stayqueued.");
        int aj = getHighestPermission(player, "ajqueue.stayqueued.");
        return Math.max(sq, aj);
    }

    @Override
    public int getPriority(AdaptedPlayer player) {
        int sq = getHighestPermission(player, "simplequeue.priority.");
        int aj = getHighestPermission(player, "ajqueue.priority.");
        return Math.max(sq, aj);
    }

    @Override
    public int getServerPriotity(String server, AdaptedPlayer player) {
        int sq = getHighestPermission(player, "simplequeue.serverpriority."+server+".");
        int aj = getHighestPermission(player, "ajqueue.serverpriority."+server+".");
        return Math.max(sq, aj);
    }

    @Override
    public boolean hasContextBypass(AdaptedPlayer player, String server) {
        if(getSelected() == null) {
            return false;
        }
        List<String> perms = getSelected().getPermissions(player);
        return perms.contains("simplequeue.serverbypass."+server) || perms.contains("ajqueue.serverbypass."+server);
    }

    @Override
    public boolean hasUniqueFullBypass(AdaptedPlayer player, String server) {
        if(player.hasPermission("simplequeue.joinfullandbypassserver."+server) || player.hasPermission("ajqueue.joinfullandbypassserver."+server)) return true;

        if(getSelected() == null) {
            return false;
        }
        List<String> perms = getSelected().getPermissions(player);
        perms.removeIf(s -> !s.startsWith("simplequeue.joinfullandbypassserver."+server) && !s.startsWith("ajqueue.joinfullandbypassserver."+server));
        return perms.size() > 0;
    }

    private int getHighestPermission(AdaptedPlayer player, String prefix) {
        if(getSelected() == null) {
            return -1;
        }
        List<String> perms = getSelected().getPermissions(player);
        Iterator<String> it = perms.iterator();
        String highestPerm = prefix+"0";
        while(it.hasNext()) {
            String perm = it.next();
            if(!perm.startsWith(prefix)) continue;
            if(highestPerm.isEmpty()) {
                highestPerm = perm;
                continue;
            }
            try {
                int level = Integer.parseInt(perm.substring(prefix.length()));
                int highestlevel = Integer.parseInt(highestPerm.substring(prefix.length()));
                if(level > highestlevel) {
                    highestPerm = perm;
                }
            } catch(NumberFormatException ignored) {}
        }
        try {
            return Integer.parseInt(highestPerm.substring(prefix.length()));
        } catch(NumberFormatException e) {
            return 0;
        }
    }
}
