package us.ajg0702.queue.spigot;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Iterator;





public class Placeholders extends PlaceholderExpansion {

    private final SpigotMain plugin;









    public Placeholders(SpigotMain plugin){
        this.plugin = plugin;
    }








    @Override
    public boolean persist(){
        return true;
    }







    @Override
    public boolean canRegister(){
        return true;
    }







    @Override
    public String getAuthor(){
        return plugin.getDescription().getAuthors().toString();
    }










    @Override
    public String getIdentifier(){
        return "ajqueue";
    }









    @Override
    public String getVersion(){
        return plugin.getDescription().getVersion();
    }

    final HashMap<Player, HashMap<String, String>> responseCache = new HashMap<>();

    public void cleanCache() {
    	Iterator<Player> it = responseCache.keySet().iterator();
    	while(it.hasNext()) {
    		Player p = it.next();
    		if(p == null) {
    			it.remove();
    			continue;
    		}
    		if(!p.isOnline()) {
    			it.remove();
    		}
    	}
    }














    @SuppressWarnings("SuspiciousMethodCalls")
	@Override
    public String onPlaceholderRequest(Player player, final String identifier){



        if(player == null) return "No player";


    	String noc = "_nocache";
    	if(identifier.length() > noc.length()) {
    		int olen = identifier.length()-noc.length();
        	if(identifier.indexOf(noc) == olen) {
        		String idfr = identifier.substring(0, olen);
        		return this.parsePlaceholder(player, idfr);
        	}
    	}

    	Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
			HashMap<String, String> playerCache;
			if(responseCache.containsKey(player)) {
				playerCache = responseCache.get(player);
			} else {
				playerCache = new HashMap<>();
			}
			if(playerCache.size() > 75) {
				try {
					playerCache.remove(playerCache.keySet().toArray()[0]);
				} catch(ConcurrentModificationException e) {
					Bukkit.getScheduler().runTask(plugin, () -> playerCache.remove(playerCache.keySet().toArray()[0]));
				}
			}
			String resp = parsePlaceholder(player, identifier);
			if(resp == null) return;
			playerCache.put(identifier, resp);
			responseCache.put(player, playerCache);
		});


    	if(responseCache.containsKey(player)) {
    		HashMap<String, String> playerCache = responseCache.get(player);
    		if(playerCache.containsKey(identifier)) {
    			return playerCache.get(identifier);
    		}
    	} else {
    		if(identifier.equalsIgnoreCase("queued")) {
    			return "None";
    		}
    		if(identifier.equalsIgnoreCase("position") || identifier.equalsIgnoreCase("of")) {
    			return "None";
    		}
    		if(identifier.equalsIgnoreCase("inqueue")) {
    			return "false";
    		}
    		if(identifier.matches("queuedfor_*.*")) {
        		return "0";
        	}
			if(identifier.matches("status_*.*")) {
				return "Loading";
			}
    	}


        return null;
    }

    @SuppressWarnings("SameReturnValue")
	private String parsePlaceholder(Player player, String identifier) {
    	if(identifier.equalsIgnoreCase("queued")) {
        	plugin.sendMessage(player, "queuename", "");
        } else
    	if(identifier.equalsIgnoreCase("position")) {
    		plugin.sendMessage(player, "position", "");
    	} else
    	if(identifier.equalsIgnoreCase("of")) {
    		plugin.sendMessage(player, "positionof", "");
    	} else
    	if(identifier.equalsIgnoreCase("inqueue")) {
    		plugin.sendMessage(player, "inqueue", "");
    	} else
		if(identifier.equalsIgnoreCase("estimated_time")) {
			plugin.sendMessage(player, "estimated_time", "");
		} else
    	if(identifier.matches("queuedfor_*.*")) {
    		plugin.sendMessage(player, "queuedfor", identifier.split("_")[1]);
    	} else
		if(identifier.matches("status_*.*")) {
			plugin.sendMessage(player, "status", identifier.split("_")[1]);
		}


        return null;
    }
}
