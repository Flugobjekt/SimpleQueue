package us.ajg0702.queue.common.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ColorUtils {

    private static final Pattern HEX_PATTERN = Pattern.compile("(?i)(?:&|§)#([0-9a-fA-F]{6})");
    private static final Pattern AMPERSAND_HEX_PATTERN = Pattern.compile("(?i)(?:&|§)x(?:(?:&|§)([0-9a-fA-F])){6}");

    private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();





    public static Component format(String message) {
        if (message == null || message.isEmpty()) {
            return Component.empty();
        }


        Matcher hexMatcher = HEX_PATTERN.matcher(message);
        StringBuffer sb = new StringBuffer();
        while (hexMatcher.find()) {
            hexMatcher.appendReplacement(sb, "<#" + hexMatcher.group(1) + ">");
        }
        hexMatcher.appendTail(sb);
        String converted = sb.toString();


        Matcher ampHexMatcher = AMPERSAND_HEX_PATTERN.matcher(converted);
        sb = new StringBuffer();
        while (ampHexMatcher.find()) {
            String full = ampHexMatcher.group(0).replaceAll("[&§xX]", "");
            ampHexMatcher.appendReplacement(sb, "<#" + full + ">");
        }
        ampHexMatcher.appendTail(sb);
        converted = sb.toString();


        converted = convertLegacyToMiniMessage(converted);

        try {
            return MINI_MESSAGE.deserialize(converted);
        } catch (Exception e) {

            return LegacyComponentSerializer.legacyAmpersand().deserialize(message);
        }
    }

    public static String convertLegacyToMiniMessage(String text) {
        if (text == null) return "";
        return text
                .replace("&0", "<black>").replace("§0", "<black>")
                .replace("&1", "<dark_blue>").replace("§1", "<dark_blue>")
                .replace("&2", "<dark_green>").replace("§2", "<dark_green>")
                .replace("&3", "<dark_aqua>").replace("§3", "<dark_aqua>")
                .replace("&4", "<dark_red>").replace("§4", "<dark_red>")
                .replace("&5", "<dark_purple>").replace("§5", "<dark_purple>")
                .replace("&6", "<gold>").replace("§6", "<gold>")
                .replace("&7", "<gray>").replace("§7", "<gray>")
                .replace("&8", "<dark_gray>").replace("§8", "<dark_gray>")
                .replace("&9", "<blue>").replace("§9", "<blue>")
                .replace("&a", "<green>").replace("§a", "<green>")
                .replace("&b", "<aqua>").replace("§b", "<aqua>")
                .replace("&c", "<red>").replace("§c", "<red>")
                .replace("&d", "<light_purple>").replace("§d", "<light_purple>")
                .replace("&e", "<yellow>").replace("§e", "<yellow>")
                .replace("&f", "<white>").replace("§f", "<white>")
                .replace("&k", "<obfuscated>").replace("§k", "<obfuscated>")
                .replace("&l", "<bold>").replace("§l", "<bold>")
                .replace("&m", "<strikethrough>").replace("§m", "<strikethrough>")
                .replace("&n", "<underlined>").replace("§n", "<underlined>")
                .replace("&o", "<italic>").replace("§o", "<italic>")
                .replace("&r", "<reset>").replace("§r", "<reset>");
    }
}
