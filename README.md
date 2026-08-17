# SimpleQueue

SimpleQueue is a queue plugin designed for Minecraft networks. It helps manage player traffic when connecting between servers, preventing server lag and crashes during peak join times.

The plugin runs on Velocity, BungeeCord, Paper, Folia, and Spigot servers using a single jar file.

---

## Features

- Automatic queueing when players switch servers (via commands, NPCs, compass menus, or portals).
- Native Folia support with multi-threaded region schedulers.
- Configurable delay between player joins (default 3 seconds).
- Multi-level priority permissions for donor ranks.
- LuckPerms context support for server-specific priority and bypasses.
- BossBar support with real-time queue progress.
- Sound effects when joining, moving up, or connecting to a server.
- Full MiniMessage, HEX color, and legacy color code support.
- Configurable prefix for all chat messages.
- Disconnect protection (holds a player's place in line if they disconnect briefly).
- PlaceholderAPI support for scoreboards and tablists on backend servers.

---

## Installation

You only need one file: `SimpleQueue-2.2.9.jar`.

### 1. On your Proxy (Velocity or BungeeCord) - Required
1. Download or build `SimpleQueue-2.2.9.jar`.
2. Put the jar into your proxy's `plugins` folder.
3. Restart your proxy.
4. Edit `plugins/SimpleQueue/config.yml` if needed, then run `/simplequeue reload`.

### 2. On your Backend Servers (Paper, Folia, Purpur, Spigot) - Optional
You only need to put the jar on backend servers if you want to use PlaceholderAPI placeholders on scoreboards, tablists, or holograms.
1. Put `SimpleQueue-2.2.9.jar` into your backend server's `plugins` folder.
2. Restart the backend server.

---

## Configuration

Here are the main settings in `config.yml`:

```yaml
# Chat prefix (supports MiniMessage and HEX colors)
prefix: "&#FFB900&lQUEUE &8» "

# Whether to automatically add the prefix to chat messages
use-prefix: true

# Time to wait between sending players to the target server (in seconds)
wait-time: 3.0

# Automatically put players into a queue when switching servers
intercept-server-switch: true

# BossBar settings while waiting in queue
bossbar:
  enabled: true
  text: "<#FFB900><bold>{SERVER}</bold></#FFB900> &8| &7Position: &f{POS}&7/&f{LEN} &8(&e{TIME}&8)"
  color: "YELLOW"
  style: "PROGRESS"

# Sound effects
sounds:
  join:
    enabled: true
    sound: "minecraft:entity.experience_orb.pickup"
    volume: 1.0
    pitch: 1.2
  send:
    enabled: true
    sound: "minecraft:entity.player.levelup"
    volume: 1.0
    pitch: 1.0
  leave:
    enabled: true
    sound: "minecraft:ui.button.click"
    volume: 0.8
    pitch: 0.8
```

---

## Commands

- `/simplequeue` (Aliases: `/sq`, `/queue`) - Main command.
- `/simplequeue join <server>` - Join a queue manually.
- `/leavequeue [server]` (Alias: `/lq`) - Leave your current queue.
- `/simplequeue list` - View active queues and players in line.
- `/simplequeue pause <server>` - Pause or unpause a queue.
- `/simplequeue reload` - Reload plugin configuration.

---

## Permissions

- `simplequeue.use` - Permission to use basic queue commands.
- `simplequeue.bypass` - Skip the queue and connect immediately.
- `simplequeue.serverbypass.<server>` - Skip the queue for a specific server.
- `simplequeue.priority.<number>` - Set priority in queue (higher numbers go first).
- `simplequeue.serverpriority.<server>.<number>` - Server-specific priority.
- `simplequeue.stayqueued.<seconds>` - Time to hold a player's place in line after disconnecting.
- `simplequeue.joinfull` - Allow joining full servers.
- `simplequeue.manage.*` - Access to administrative commands.

All legacy `ajqueue.*` permissions are also supported for backwards compatibility.

---

## Placeholders (PlaceholderAPI)

When installed on backend servers, both `%simplequeue_*` and `%ajqueue_*` placeholders are supported:

- `%simplequeue_position%` - Player's current position in queue.
- `%simplequeue_positionof%` - Total number of players in the queue.
- `%simplequeue_inqueue%` - Whether the player is currently in a queue (`true`/`false`).
- `%simplequeue_queuename%` - Name of the server the player is queued for.
- `%simplequeue_queuelength_<server>%` - Number of players queued for a specific server.
- `%simplequeue_estimated_time%` - Estimated wait time until connection.

---

## Building from Source

Requirements: Java 17 or higher.

```bash
git clone https://github.com/Flugobjekt/SimpleQueue.git
cd SimpleQueue
./gradlew build :free:shadowJar
```

The compiled jar will be located at:
`free/build/libs/SimpleQueue-2.2.9.jar`
