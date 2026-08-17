package us.ajg0702.queue.common.utils;

import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import us.ajg0702.queue.api.players.AdaptedPlayer;
import us.ajg0702.queue.api.queues.QueueServer;
import us.ajg0702.queue.common.QueueMain;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class BossBarManager {

    private final QueueMain main;
    private final Map<UUID, BossBar> activeBars = new ConcurrentHashMap<>();

    public BossBarManager(QueueMain main) {
        this.main = main;
    }

    public void updateBossBar(AdaptedPlayer player, QueueServer server, int pos, int len, String time, String status) {
        if (player == null || !player.isConnected() || main == null || main.getConfig() == null) return;

        boolean enabled = main.getConfig().getBoolean("bossbar.enabled");
        if (!enabled) {
            removeBossBar(player);
            return;
        }

        String rawText = main.getConfig().getString("bossbar.text");
        if (rawText == null || rawText.isEmpty()) {
            rawText = "<#FFB900><bold>{SERVER}</bold></#FFB900> &8| &7Position: &f{POS}&7/&f{LEN} &8(&e{TIME}&8)";
        }

        rawText = rawText
                .replace("{SERVER}", server.getAlias())
                .replace("{SERVERNAME}", server.getName())
                .replace("{POS}", String.valueOf(pos))
                .replace("{LEN}", String.valueOf(len))
                .replace("{TIME}", time != null ? time : "")
                .replace("{STATUS}", status != null ? status : "");

        Component title = ColorUtils.format(rawText);

        float progress = 1.0f;
        if (len > 0) {
            progress = Math.max(0.0f, Math.min(1.0f, (float) (len - pos + 1) / (float) len));
        }

        BossBar.Color color = parseColor(main.getConfig().getString("bossbar.color"));
        BossBar.Overlay overlay = parseOverlay(main.getConfig().getString("bossbar.style"));

        BossBar bar = activeBars.get(player.getUniqueId());
        if (bar == null) {
            bar = BossBar.bossBar(title, progress, color, overlay);
            activeBars.put(player.getUniqueId(), bar);
            player.showBossBar(bar);
        } else {
            bar.name(title);
            bar.progress(progress);
            bar.color(color);
            bar.overlay(overlay);
        }
    }

    public void removeBossBar(AdaptedPlayer player) {
        if (player == null) return;
        BossBar bar = activeBars.remove(player.getUniqueId());
        if (bar != null) {
            try {
                player.hideBossBar(bar);
            } catch (Exception ignored) {}
        }
    }

    public void removeAll() {
        activeBars.clear();
    }

    private BossBar.Color parseColor(String colorName) {
        if (colorName == null) return BossBar.Color.YELLOW;
        try {
            return BossBar.Color.valueOf(colorName.toUpperCase());
        } catch (IllegalArgumentException e) {
            return BossBar.Color.YELLOW;
        }
    }

    private BossBar.Overlay parseOverlay(String styleName) {
        if (styleName == null) return BossBar.Overlay.PROGRESS;
        try {
            return BossBar.Overlay.valueOf(styleName.toUpperCase());
        } catch (IllegalArgumentException e) {
            return BossBar.Overlay.PROGRESS;
        }
    }
}
