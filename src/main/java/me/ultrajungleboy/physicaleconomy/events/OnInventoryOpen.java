package me.ultrajungleboy.physicaleconomy.events;

import me.ultrajungleboy.physicaleconomy.PhysicalEconomy;
import me.ultrajungleboy.physicaleconomy.players.Util;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class OnInventoryOpen implements Listener {
    public OnInventoryOpen() {
    }

    @EventHandler
    public static void onInventoryOpen(InventoryCloseEvent event) {
        if (event.getPlayer() instanceof Player) {
            Player player = (Player)event.getPlayer();
            PersistentDataContainer playerData = player.getPersistentDataContainer();
            NamespacedKey creatingShopKey = new NamespacedKey(PhysicalEconomy.getPlugin(), "CreatingShop");
            if (!playerData.has(creatingShopKey, PersistentDataType.STRING)) {
                Util.ResetShopData(player);
            }
        }

    }
}
