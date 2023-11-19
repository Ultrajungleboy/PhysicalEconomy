package me.ultrajungleboy.physicaleconomy.items;

import me.ultrajungleboy.physicaleconomy.PhysicalEconomy;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class ItemUtil {
    public ItemUtil() {
    }

    public static ItemStack createPlaceHolder() {
        ItemStack item = new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(" ");
        meta.setLore((List)null);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack ResetItemData(ItemStack item) {
        ItemMeta itemMeta = item.getItemMeta();
        PersistentDataContainer itemData = itemMeta.getPersistentDataContainer();
        NamespacedKey ValueAmountKey = new NamespacedKey(PhysicalEconomy.getPlugin(), "ValueAmount");
        NamespacedKey shopTypeKey = new NamespacedKey(PhysicalEconomy.getPlugin(), "ShopType");
        if (itemData.has(ValueAmountKey, PersistentDataType.INTEGER)) {
            itemData.remove(ValueAmountKey);
        }

        if (itemData.has(shopTypeKey, PersistentDataType.STRING)) {
            itemData.remove(shopTypeKey);
        }

        item.setItemMeta(itemMeta);
        return item;
    }
}
