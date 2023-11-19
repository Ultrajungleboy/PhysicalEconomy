package me.ultrajungleboy.physicaleconomy.shops;

import me.ultrajungleboy.physicaleconomy.PhysicalEconomy;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class UpdateShop {
    public UpdateShop() {
    }

    public static void UpdateShopInventory(Player player, Inventory inv) {
        NamespacedKey quantityKey = new NamespacedKey(PhysicalEconomy.getPlugin(), "Quantity");
        PersistentDataContainer playerData = player.getPersistentDataContainer();
        NamespacedKey itemIndexKey = new NamespacedKey(PhysicalEconomy.getPlugin(), "ItemIndex");
        int quantity = (Integer)playerData.get(quantityKey, PersistentDataType.INTEGER);
        if (playerData.has(itemIndexKey, PersistentDataType.INTEGER)) {
            ItemStack item = inv.getItem((Integer)playerData.get(itemIndexKey, PersistentDataType.INTEGER));
            inv.setItem(49, item);
        }

        quantity = SetNumber(inv, quantity, 50);
        quantity = SetNumber(inv, quantity, 51);
        SetNumber(inv, quantity, 52);
    }

    private static int SetNumber(Inventory inv, int quantity, int itemIndex) {
        int num = 0;
        int multiplier = 1;
        if (quantity >= 100 && itemIndex == 50) {
            num = quantity / 100;
            multiplier = 100;
        } else if (quantity >= 10 && itemIndex == 51) {
            num = quantity / 10;
            multiplier = 10;
        } else if (itemIndex == 52) {
            num = quantity / 1;
        }

        ItemStack item = inv.getItem(itemIndex);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setCustomModelData(2100 + num);
        item.setItemMeta(itemMeta);
        item.setType(Material.FLINT);
        inv.setItem(itemIndex, item);
        return quantity - num * multiplier;
    }
}
