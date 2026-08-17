package us.ajg0702.queue.common.utils;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import us.ajg0702.queue.api.players.AdaptedPlayer;
import us.ajg0702.queue.common.QueueMain;

public class SoundManager {

    private final QueueMain main;

    public SoundManager(QueueMain main) {
        this.main = main;
    }

    public void playJoinSound(AdaptedPlayer player) {
        playSound(player, "join", "minecraft:entity.experience_orb.pickup", 1.0f, 1.2f);
    }

    public void playSendSound(AdaptedPlayer player) {
        playSound(player, "send", "minecraft:entity.player.levelup", 1.0f, 1.0f);
    }

    public void playLeaveSound(AdaptedPlayer player) {
        playSound(player, "leave", "minecraft:ui.button.click", 0.8f, 0.8f);
    }

    public void playPositionUpdateSound(AdaptedPlayer player) {
        playSound(player, "position-update", "minecraft:block.note_block.pling", 0.8f, 1.0f);
    }

    public void playSound(AdaptedPlayer player, String type, String defaultSound, float defaultVolume, float defaultPitch) {
        if (player == null || main == null || main.getConfig() == null) return;
        try {
            boolean enabled = main.getConfig().getBoolean("sounds." + type + ".enabled");
            if (!enabled) return;

            String soundKey = main.getConfig().getString("sounds." + type + ".sound");
            if (soundKey == null || soundKey.isEmpty()) {
                soundKey = defaultSound;
            }

            float volume = (float) main.getConfig().getDouble("sounds." + type + ".volume");
            if (volume <= 0) volume = defaultVolume;

            float pitch = (float) main.getConfig().getDouble("sounds." + type + ".pitch");
            if (pitch <= 0) pitch = defaultPitch;

            Key key;
            if (soundKey.contains(":")) {
                String[] parts = soundKey.split(":", 2);
                key = Key.key(parts[0], parts[1]);
            } else {
                key = Key.key("minecraft", soundKey);
            }

            Sound sound = Sound.sound(key, Sound.Source.PLAYER, volume, pitch);
            player.playSound(sound);
        } catch (Exception ignored) {}
    }
}
