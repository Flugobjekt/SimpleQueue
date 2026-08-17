package us.ajg0702.queue.spigot;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import us.ajg0702.queue.spigot.utils.FoliaScheduler;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Placeholders extends PlaceholderExpansion {

    private final SpigotMain plugin;
    private final String identifier;

    public Placeholders(SpigotMain plugin, String identifier) {
        this.plugin = plugin;
        this.identifier = identifier;
    }

    public Placeholders(SpigotMain plugin) {
        this(plugin, "simplequeue");
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public String getAuthor() {
        return plugin.getDescription().getAuthors().toString();
    }

    @Override
    public String getIdentifier() {
        return identifier;
    }

    @Override
    public String getVersion() {
        return plugin.getDescription().getVersion();
    }

    final ConcurrentHashMap<Player, ConcurrentHashMap<String, String>> responseCache = new ConcurrentHashMap<>();

    public void cleanCache() {
        Iterator<Player> it = responseCache.keySet().iterator();
        while (it.hasNext()) {
            Player p = it.next();
            if (p == null || !p.isOnline()) {
                it.remove();
            }
        }
    }

    @Override
    public String onPlaceholderRequest(Player player, final String identifier) {
        if (player == null) return "No player";

        String noc = "_nocache";
        if (identifier.length() > noc.length()) {
            int olen = identifier.length() - noc.length();
            if (identifier.indexOf(noc) == olen) {
                String idfr = identifier.substring(0, olen);
                return this.parsePlaceholder(player, idfr);
            }
        }

        FoliaScheduler.runAsync(plugin, () -> {
            ConcurrentHashMap<String, String> playerCache = responseCache.computeIfAbsent(player, k -> new ConcurrentHashMap<>());
            if (playerCache.size() > 75) {
                Map.Entry<String, String> firstEntry = playerCache.entrySet().iterator().next();
                if (firstEntry != null) {
                    playerCache.remove(firstEntry.getKey());
                }
            }
            String resp = parsePlaceholder(player, identifier);
            if (resp != null) {
                playerCache.put(identifier, resp);
            }
        });

        if (responseCache.containsKey(player)) {
            ConcurrentHashMap<String, String> playerCache = responseCache.get(player);
            if (playerCache.containsKey(identifier)) {
                return playerCache.get(identifier);
            }
        } else {
            if (identifier.equalsIgnoreCase("queued") || identifier.equalsIgnoreCase("queuename")) {
                return "None";
            }
            if (identifier.equalsIgnoreCase("position") || identifier.equalsIgnoreCase("of") || identifier.equalsIgnoreCase("positionof")) {
                return "None";
            }
            if (identifier.equalsIgnoreCase("inqueue")) {
                return "false";
            }
            if (identifier.matches("queuedfor_*.*") || identifier.matches("queuelength_*.*")) {
                return "0";
            }
            if (identifier.matches("status_*.*")) {
                return "Loading";
            }
        }

        return null;
    }

    private String parsePlaceholder(Player player, String identifier) {
        if (identifier.equalsIgnoreCase("queued") || identifier.equalsIgnoreCase("queuename")) {
            plugin.sendMessage(player, "queuename", "");
        } else if (identifier.equalsIgnoreCase("position")) {
            plugin.sendMessage(player, "position", "");
        } else if (identifier.equalsIgnoreCase("of") || identifier.equalsIgnoreCase("positionof")) {
            plugin.sendMessage(player, "positionof", "");
        } else if (identifier.equalsIgnoreCase("inqueue")) {
            plugin.sendMessage(player, "inqueue", "");
        } else if (identifier.equalsIgnoreCase("estimated_time") || identifier.equalsIgnoreCase("esttime")) {
            plugin.sendMessage(player, "estimated_time", "");
        } else if (identifier.matches("queuedfor_*.*")) {
            plugin.sendMessage(player, "queuedfor", identifier.split("_")[1]);
        } else if (identifier.matches("queuelength_*.*")) {
            plugin.sendMessage(player, "queuedfor", identifier.split("_")[1]);
        } else if (identifier.matches("status_*.*")) {
            plugin.sendMessage(player, "status", identifier.split("_")[1]);
        }
        return null;
    }
}
