package me.jesusmx.hubcore.queue;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import me.jesusmx.hubcore.TacoHubLite;
import me.jesusmx.hubcore.util.files.ConfigFile;
import me.joeleoli.portal.shared.queue.Queue;
import me.signatured.ezqueueshared.QueueInfo;
import me.signatured.ezqueuespigot.EzQueueAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class QueueManager {

	private ConfigFile config = TacoHubLite.getInstance().getSettingsConfig();

	public QueueTypes getQueueSupport() {
		switch (config.getString("system.queue")) {
			case "Ezqueue":
				return QueueTypes.Ezqueue;
			case "Portal":
				return QueueTypes.Portal;
			default:
				return QueueTypes.None;
		}
	}
	
	public boolean inQueue(Player player) {
		switch (this.getQueueSupport()) {
			case Ezqueue:
				return QueueInfo.getQueueInfo(EzQueueAPI.getQueue(player)) != null;
			case Portal:
				return Queue.getByPlayer(player.getUniqueId()) != null;
			default:
				return false;
		}
	}
	
	public void sendPlayer(Player player, String server) {
		switch (this.getQueueSupport()) {
			case Ezqueue:
				EzQueueAPI.addToQueue(player, server);
				break;
			case Portal:
				Bukkit.getServer().dispatchCommand(player, "joinqueue " + server);
				break;
			default:
				ByteArrayDataOutput out = ByteStreams.newDataOutput();
				out.writeUTF("Connect");
				out.writeUTF(server);
				player.getPlayer().sendPluginMessage(TacoHubLite.getInstance(), "BungeeCord", out.toByteArray());
				break;
		}
	}
	
	public String getQueueIn(Player player) {
		switch (this.getQueueSupport()) {
			case Ezqueue:
				return EzQueueAPI.getQueue(player);
			case Portal:
				return Queue.getByPlayer(player.getUniqueId()).getName();
			default:
				return "NoInQueue";
		}
	}
	
	public int getPosition(Player player) {
		switch (this.getQueueSupport()) {
			case Ezqueue:
				return EzQueueAPI.getPosition(player);
			case Portal:
				return Queue.getByPlayer(player.getUniqueId()).getPosition(player.getUniqueId());
			default:
				return 0;
		}
	}
	
	public int getInQueue(String queue) {
		switch (this.getQueueSupport()) {
			case Ezqueue:
				return QueueInfo.getQueueInfo(queue).getPlayersInQueue().size();
			case Portal:
				return Queue.getByName(queue).getPlayers().size();
			default:
				return 0;
		}
	}
}
