package me.ultrajungleboy.physicaleconomy.tasks;

import me.ultrajungleboy.physicaleconomy.PhysicalEconomy;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

public class ClickCooldown extends BukkitRunnable {
    Player player;
    PersistentDataContainer playerData;
    NamespacedKey clickCooldown = new NamespacedKey(PhysicalEconomy.getPlugin(), "ClickCooldown");

    public ClickCooldown(Player player) {
        this.player = player;
        this.playerData = player.getPersistentDataContainer();
    }

    public void run() {
        if (this.playerData.has(this.clickCooldown, PersistentDataType.STRING)) {
            this.playerData.remove(this.clickCooldown);
        }

    }
}
