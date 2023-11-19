package me.ultrajungleboy.physicaleconomy.banking;

import me.ultrajungleboy.physicaleconomy.PhysicalEconomy;
import me.ultrajungleboy.physicaleconomy.commands.Commands;
import me.ultrajungleboy.physicaleconomy.events.Events;
import me.ultrajungleboy.physicaleconomy.players.Util;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Instrument;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Note;
import org.bukkit.Note.Tone;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class BankingUtil {
    static Economy econ = null;

    public BankingUtil() {
    }

    public static void init() {
        econ = PhysicalEconomy.getEconomy();
    }

    public static double getBalance(Player player) {
        return econ.getBalance(Bukkit.getPlayer(player.getUniqueId()));
    }

    public static boolean DepositMoney(Player player, int amount) {
        if (Util.cashOnPlayer(player) < amount) {
            return false;
        } else {
            econ.depositPlayer(Bukkit.getPlayer(player.getUniqueId()), (double)amount);
            Commands.removeMoney(amount, player);
            return true;
        }
    }

    public static boolean WithdrawMoney(Player player, int amount) {
        if (getBalance(player) < (double)amount) {
            return false;
        } else {
            econ.withdrawPlayer(Bukkit.getPlayer(player.getUniqueId()), (double)amount);
            return true;
        }
    }

    public static String AddPlayerDollars(Player player, ItemMeta itemMeta, int interval) {
        int[] billAmount = ParseBillAmount(player);
        switch (itemMeta.getCustomModelData()) {
            case 2000:
                if (billAmount[0] + interval >= 0) {
                    billAmount[0] += interval;
                } else {
                    PlayDenySound(player);
                }
                break;
            case 2001:
                if (billAmount[1] + interval >= 0) {
                    billAmount[1] += interval;
                } else {
                    PlayDenySound(player);
                }
                break;
            case 2002:
                if (billAmount[2] + interval >= 0) {
                    billAmount[2] += interval;
                } else {
                    PlayDenySound(player);
                }
                break;
            case 2003:
                if (billAmount[3] + interval >= 0) {
                    billAmount[3] += interval;
                } else {
                    PlayDenySound(player);
                }
                break;
            case 2004:
                if (billAmount[4] + interval >= 0) {
                    billAmount[4] += interval;
                } else {
                    PlayDenySound(player);
                }
                break;
            case 2005:
                if (billAmount[5] + interval >= 0) {
                    billAmount[5] += interval;
                } else {
                    PlayDenySound(player);
                }
        }

        return SerializeBillAmount(billAmount);
    }

    public static int[] ParseBillAmount(Player player) {
        PersistentDataContainer playerData = player.getPersistentDataContainer();
        NamespacedKey billAmountKey = new NamespacedKey(PhysicalEconomy.getPlugin(), "BillAmount");
        String billAmountString = (String)playerData.get(billAmountKey, PersistentDataType.STRING);
        String[] parsedStrings = billAmountString.split(";");
        int[] billAmount = new int[parsedStrings.length];

        for(int i = 0; i < billAmount.length; ++i) {
            billAmount[i] = Integer.parseInt(parsedStrings[i]);
        }

        return billAmount;
    }

    public static String SerializeBillAmount(int[] parsedBillAmount) {
        String billAmount = "";

        for(int i = 0; i < parsedBillAmount.length - 1; ++i) {
            billAmount = billAmount + parsedBillAmount[i] + ";";
        }

        billAmount = billAmount + parsedBillAmount[parsedBillAmount.length - 1];
        return billAmount;
    }

    public static void UpdateBank(Inventory inv, Player player) {
        int totalValue = getTotalValue(player);

        for(int i = 29; i < 33; ++i) {
            totalValue = SetNumber(inv, totalValue, i);
        }

        SetNumber(inv, totalValue, 33);
    }

    public static int getTotalValue(Player player) {
        int[] billAmount = ParseBillAmount(player);
        int totalValue = 0;

        for(int i = 0; i < billAmount.length; ++i) {
            totalValue += Events.showValue(i, billAmount[i]);
        }

        return totalValue;
    }

    private static int SetNumber(Inventory inv, int quantity, int itemIndex) {
        int num = 0;
        int multiplier = 1;
        if (quantity >= 10000 && itemIndex == 29) {
            num = quantity / 10000;
            multiplier = 10000;
        } else if (quantity >= 1000 && itemIndex == 30) {
            num = quantity / 1000;
            multiplier = 1000;
        } else if (quantity >= 100 && itemIndex == 31) {
            num = quantity / 100;
            multiplier = 100;
        } else if (quantity >= 10 && itemIndex == 32) {
            num = quantity / 10;
            multiplier = 10;
        } else if (itemIndex == 33) {
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

    private static void PlayDenySound(Player player) {
        player.playNote(player.getLocation(), Instrument.BASS_DRUM, Note.natural(1, Tone.G));
    }
}
