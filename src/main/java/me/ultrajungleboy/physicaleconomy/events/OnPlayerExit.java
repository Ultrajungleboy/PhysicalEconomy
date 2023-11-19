package me.ultrajungleboy.physicaleconomy.events;

import me.ultrajungleboy.physicaleconomy.PhysicalEconomy;
import me.ultrajungleboy.physicaleconomy.players.Util;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class OnPlayerExit implements Listener {
    public OnPlayerExit() {
    }

    @EventHandler
    public void onPlayerExit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        PersistentDataContainer playerData = player.getPersistentDataContainer();
        Util.ResetShopData(player);
        NamespacedKey viewingBankKey = new NamespacedKey(PhysicalEconomy.getPlugin(), "ViewingBank");
        if (playerData.has(viewingBankKey, PersistentDataType.INTEGER)) {
            NamespacedKey billAmountKey = new NamespacedKey(PhysicalEconomy.getPlugin(), "BillAmount");
            playerData.remove(viewingBankKey);
            playerData.remove(billAmountKey);
        }

    }
}
