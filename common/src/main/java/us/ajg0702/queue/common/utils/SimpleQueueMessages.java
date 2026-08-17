package us.ajg0702.queue.common.utils;

import net.kyori.adventure.text.Component;
import us.ajg0702.queue.common.QueueMain;
import us.ajg0702.utils.common.Messages;

import java.io.File;
import java.util.LinkedHashMap;

public class SimpleQueueMessages extends Messages {

    private final QueueMain main;

    public SimpleQueueMessages(QueueMain main, File dataFolder, LogConverter logger, LinkedHashMap<String, Object> defaults) {
        super(dataFolder, logger, defaults);
        this.main = main;
    }

    @Override
    public Component getComponent(String key, String... replacements) {
        String raw = getRawString(key);
        if (raw == null || raw.isEmpty()) {
            return Component.empty();
        }

        for (String replacement : replacements) {
            String[] parts = replacement.split(":", 2);
            if (parts.length == 2) {
                raw = raw.replace("{" + parts[0] + "}", parts[1]);
            }
        }


        String prefixStr = getPrefixString();
        raw = raw.replace("{PREFIX}", prefixStr);

        Component component = ColorUtils.format(raw);

        if (main != null && main.getConfig() != null && shouldPrefix(key)) {
            boolean usePrefix = main.getConfig().getBoolean("use-prefix");

            if (usePrefix) {
                Component prefixComponent = ColorUtils.format(prefixStr);
                return prefixComponent.append(component);
            }
        }

        return component;
    }

    public String getPrefixString() {
        if (main != null && main.getConfig() != null) {
            String cfgPrefix = main.getConfig().getString("prefix");
            if (cfgPrefix != null && !cfgPrefix.isEmpty()) {
                return cfgPrefix;
            }
        }
        return "&#FFB900&lQUEUE &8» ";
    }

    private boolean shouldPrefix(String key) {
        if (key == null) return false;

        if (key.startsWith("spigot.actionbar.") ||
                key.startsWith("title.") ||
                key.startsWith("format.") ||
                key.startsWith("placeholders.") ||
                key.startsWith("status.offline.offline") ||
                key.startsWith("status.offline.restarting") ||
                key.startsWith("status.offline.full") ||
                key.startsWith("status.offline.restricted") ||
                key.startsWith("status.offline.paused") ||
                key.startsWith("status.offline.whitelisted") ||
                key.equals("list.playerlist") ||
                key.equals("list.none") ||
                key.startsWith("updater.")) {
            return false;
        }
        return true;
    }
}
