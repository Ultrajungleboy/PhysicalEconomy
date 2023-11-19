package me.ultrajungleboy.physicaleconomy.players;

import me.ultrajungleboy.physicaleconomy.PhysicalEconomy;
import me.ultrajungleboy.physicaleconomy.events.Events;
import java.util.HashMap;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class Util {
    public Util() {
    }

    public static int cashOnPlayer(Player player) {
        HashMap<Integer, ? extends ItemStack> potentialSlots = player.getInventory().all(Material.FLINT);
        ItemStack[] itemArray = convertHashMapToItemStackArray(potentialSlots);
        int amountInInventory = 0;

        for(int dollarType = 5; dollarType > -1; --dollarType) {
            for(int i = 0; i < itemArray.length; ++i) {
                if (itemArray[i].getItemMeta().hasCustomModelData() && itemArray[i].getItemMeta().getCustomModelData() == 2000 + dollarType) {
                    int totalamount = Events.showValue(dollarType, itemArray[i].getAmount());
                    amountInInventory += totalamount;
                }
            }
        }

        return amountInInventory;
    }

    public static boolean playerHasItems(Player player, ItemStack item) {
        int requiredAmount = item.getAmount();
        return player.getInventory().containsAtLeast(item, requiredAmount);
    }

    private static ItemStack[] convertHashMapToItemStackArray(HashMap<Integer, ? extends ItemStack> hashMap) {
        int[] i = new int[]{0};
        ItemStack[] itemArray = new ItemStack[hashMap.size()];
        hashMap.entrySet().forEach((entry) -> {
            itemArray[i[0]] = (ItemStack)entry.getValue();
            int var10002 = i[0]++;
        });
        return itemArray;
    }

    public static void ResetShopData(Player player) {
        PersistentDataContainer playerData = player.getPersistentDataContainer();
        NamespacedKey creatingShopKey = new NamespacedKey(PhysicalEconomy.getPlugin(), "CreatingShop");
        NamespacedKey shopNameKey = new NamespacedKey(PhysicalEconomy.getPlugin(), "ShopName");
        NamespacedKey onlyShopInventory = new NamespacedKey(PhysicalEconomy.getPlugin(), "OnlyShopInventory");
        NamespacedKey viewingShopKey = new NamespacedKey(PhysicalEconomy.getPlugin(), "ViewingShop");
        NamespacedKey quantityKey = new NamespacedKey(PhysicalEconomy.getPlugin(), "Quantity");
        NamespacedKey itemIndexKey = new NamespacedKey(PhysicalEconomy.getPlugin(), "ItemIndex");
        if (playerData.has(creatingShopKey, PersistentDataType.INTEGER)) {
            playerData.remove(creatingShopKey);
        }

        if (playerData.has(shopNameKey, PersistentDataType.STRING)) {
            playerData.remove(shopNameKey);
        }

        if (playerData.has(onlyShopInventory, PersistentDataType.INTEGER)) {
            playerData.remove(onlyShopInventory);
        }

        if (playerData.has(viewingShopKey, PersistentDataType.INTEGER)) {
            playerData.remove(viewingShopKey);
        }

        if (playerData.has(quantityKey, PersistentDataType.INTEGER)) {
            playerData.remove(quantityKey);
        }

        if (playerData.has(itemIndexKey, PersistentDataType.INTEGER)) {
            playerData.remove(itemIndexKey);
        }

    }
}
