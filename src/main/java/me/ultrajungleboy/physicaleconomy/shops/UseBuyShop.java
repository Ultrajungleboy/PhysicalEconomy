package me.ultrajungleboy.physicaleconomy.shops;

import me.ultrajungleboy.physicaleconomy.PhysicalEconomy;
import me.ultrajungleboy.physicaleconomy.commands.Commands;
import java.util.Locale;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class UseBuyShop {
    public UseBuyShop() {
    }

    public static void useBuyShop(Player player, int amount, Sign sign) {
        Commands.removeMoney(amount, player);
        PersistentDataContainer signData = sign.getPersistentDataContainer();
        NamespacedKey itemArrayIndex = new NamespacedKey(PhysicalEconomy.getPlugin(), "itemArrayIndex");
        int itemIndex = (Integer)signData.get(itemArrayIndex, PersistentDataType.INTEGER);
        ItemStack itemToGive = PhysicalEconomy.data.getConfig().getItemStack("shops." + itemIndex + ".itemArrayIndex");
        if (player.getInventory().firstEmpty() == -1) {
            player.getLocation().getWorld().dropItem(player.getLocation(), itemToGive);
            if (itemToGive.getItemMeta().getDisplayName().equals("")) {
                player.sendMessage("§eYou bought and dropped " + itemToGive.getAmount() + " of " + itemToGive.getType().toString().toLowerCase(Locale.ROOT) + "!");
            } else {
                player.sendMessage("§eYou bought and dropped" + itemToGive.getAmount() + " of " + itemToGive.getItemMeta().getDisplayName() + "!");
            }
        } else {
            player.getInventory().addItem(new ItemStack[]{itemToGive});
            if (itemToGive.getItemMeta().getDisplayName().equals("")) {
                player.sendMessage("§eYou bought " + itemToGive.getAmount() + " of " + itemToGive.getType().toString().toLowerCase(Locale.ROOT) + "!");
            } else {
                player.sendMessage("§eYou bought " + itemToGive.getAmount() + " of " + itemToGive.getItemMeta().getDisplayName() + "!");
            }
        }

    }
}
