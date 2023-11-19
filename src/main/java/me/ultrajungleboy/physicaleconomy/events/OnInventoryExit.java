package me.ultrajungleboy.physicaleconomy.events;

import me.ultrajungleboy.physicaleconomy.PhysicalEconomy;
import me.ultrajungleboy.physicaleconomy.players.Util;
import me.ultrajungleboy.physicaleconomy.shops.SaveGuiShop;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class OnInventoryExit implements Listener {
    public OnInventoryExit() {
    }

    @EventHandler
    public static void onInventoryExit(InventoryCloseEvent event) {
        if (event.getPlayer() instanceof Player) {
            Player player = (Player)event.getPlayer();
            PersistentDataContainer playerData = player.getPersistentDataContainer();
            NamespacedKey namespacedKey = new NamespacedKey(PhysicalEconomy.getPlugin(), "ShopName");
            NamespacedKey creatingShopKey = new NamespacedKey(PhysicalEconomy.getPlugin(), "CreatingShop");
            NamespacedKey viewingShopKey = new NamespacedKey(PhysicalEconomy.getPlugin(), "ViewingShop");
            NamespacedKey viewingBankKey = new NamespacedKey(PhysicalEconomy.getPlugin(), "ViewingBank");
            if (playerData.has(namespacedKey, PersistentDataType.STRING) && playerData.has(creatingShopKey, PersistentDataType.INTEGER)) {
                String shopName = (String)playerData.get(namespacedKey, PersistentDataType.STRING);
                SaveGuiShop.SaveShop(shopName, event.getInventory());
                playerData.remove(creatingShopKey);
            } else if (!playerData.has(creatingShopKey, PersistentDataType.STRING) && playerData.has(viewingShopKey, PersistentDataType.INTEGER)) {
                Util.ResetShopData(player);
            } else if (playerData.has(viewingBankKey, PersistentDataType.INTEGER)) {
                NamespacedKey billAmountKey = new NamespacedKey(PhysicalEconomy.getPlugin(), "BillAmount");
                playerData.remove(viewingBankKey);
                playerData.remove(billAmountKey);
            }
        }

    }
}
