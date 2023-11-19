package me.ultrajungleboy.physicaleconomy.events;

import me.ultrajungleboy.physicaleconomy.PhysicalEconomy;
import me.ultrajungleboy.physicaleconomy.banking.BankingUtil;
import me.ultrajungleboy.physicaleconomy.banking.OpenBank;
import me.ultrajungleboy.physicaleconomy.commands.Commands;
import me.ultrajungleboy.physicaleconomy.files.FileManager;
import me.ultrajungleboy.physicaleconomy.items.ItemUtil;
import me.ultrajungleboy.physicaleconomy.players.Util;
import me.ultrajungleboy.physicaleconomy.shops.UpdateShop;
import me.ultrajungleboy.physicaleconomy.tasks.ClickCooldown;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.bukkit.Instrument;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Note;
import org.bukkit.Sound;
import org.bukkit.Note.Tone;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitTask;

public class ItemClicked implements Listener {
    public static NamespacedKey namespacedKey = new NamespacedKey(PhysicalEconomy.getPlugin(), "Placeholder");
    String[] simulateArgs = new String[2];
    Integer[] amounts = new Integer[2];
    PhysicalEconomy mainPlugin = PhysicalEconomy.getPlugin();
    int amountDropped = 0;
    NamespacedKey creatingShopKey = new NamespacedKey(PhysicalEconomy.getPlugin(), "CreatingShop");
    NamespacedKey viewingShopKey = new NamespacedKey(PhysicalEconomy.getPlugin(), "ViewingShop");
    NamespacedKey viewingBankKey = new NamespacedKey(PhysicalEconomy.getPlugin(), "ViewingBank");
    NamespacedKey placeholderKey = new NamespacedKey(PhysicalEconomy.getPlugin(), "Placeholder");
    NamespacedKey itemIndexKey = new NamespacedKey(PhysicalEconomy.getPlugin(), "ItemIndex");
    NamespacedKey clickCooldown = new NamespacedKey(PhysicalEconomy.getPlugin(), "ClickCooldown");

    public ItemClicked() {
    }

    @EventHandler
    public void onItemClick(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player) {
            Inventory inv = event.getInventory();
            Player player = (Player)event.getWhoClicked();
            PersistentDataContainer playerData = player.getPersistentDataContainer();
            ItemStack item;
            ItemMeta itemMeta;
            int i;
            if (playerData.has(this.viewingShopKey, PersistentDataType.INTEGER)) {
                item = event.getCurrentItem();
                event.setCancelled(true);
                NamespacedKey quantityKey;
                if (event.getCurrentItem() != null) {
                    itemMeta = item.getItemMeta();
                    quantityKey = new NamespacedKey(PhysicalEconomy.getPlugin(), "Quantity");
                    NamespacedKey ItemIndexKey = new NamespacedKey(PhysicalEconomy.getPlugin(), "ItemIndex");
                    int quantity;
                    if (item.getType().equals(Material.FLINT) && itemMeta.hasCustomModelData() && itemMeta.getCustomModelData() >= 2110 && itemMeta.getCustomModelData() <= 2117) {
                        if (!playerData.has(quantityKey, PersistentDataType.INTEGER)) {
                            playerData.set(quantityKey, PersistentDataType.INTEGER, 1);
                        }

                        quantity = 1;
                        if (playerData.has(quantityKey, PersistentDataType.INTEGER)) {
                            quantity = (Integer)playerData.get(quantityKey, PersistentDataType.INTEGER);
                        }

                        i = 1;
                        if (playerData.has(ItemIndexKey, PersistentDataType.INTEGER)) {
                            i = event.getInventory().getItem((Integer)playerData.get(ItemIndexKey, PersistentDataType.INTEGER)).getAmount();
                        }

                        int totalquantity = quantity * i;
                        quantity = 0;
                        switch (itemMeta.getCustomModelData()) {
                            case 2110:
                                if (totalquantity - 1 * i > 0) {
                                    playerData.set(quantityKey, PersistentDataType.INTEGER, quantity - 1);
                                } else if (quantity == 1) {
                                    this.PlayDenySound(player);
                                } else {
                                    quantity = 1;
                                    playerData.set(quantityKey, PersistentDataType.INTEGER, Integer.valueOf(quantity));
                                }

                                UpdateShop.UpdateShopInventory(player, inv);
                                break;
                            case 2111:
                                if (totalquantity - 4 * i > 0) {
                                    playerData.set(quantityKey, PersistentDataType.INTEGER, quantity - 4);
                                } else if (quantity == 1) {
                                    this.PlayDenySound(player);
                                } else {
                                    quantity = 1;
                                    playerData.set(quantityKey, PersistentDataType.INTEGER, Integer.valueOf(quantity));
                                }

                                UpdateShop.UpdateShopInventory(player, inv);
                                break;
                            case 2112:
                                if (totalquantity - 16 * i > 0) {
                                    playerData.set(quantityKey, PersistentDataType.INTEGER, quantity - 16);
                                } else if (quantity == 1) {
                                    this.PlayDenySound(player);
                                } else {
                                    quantity = 1;
                                    playerData.set(quantityKey, PersistentDataType.INTEGER, Integer.valueOf(quantity));
                                }

                                UpdateShop.UpdateShopInventory(player, inv);
                                break;
                            case 2113:
                                if (totalquantity - 64 * i > 0) {
                                    playerData.set(quantityKey, PersistentDataType.INTEGER, quantity - 64);
                                } else if (quantity == 1) {
                                    this.PlayDenySound(player);
                                } else {
                                    quantity = 1;
                                    playerData.set(quantityKey, PersistentDataType.INTEGER, Integer.valueOf(quantity));
                                }

                                UpdateShop.UpdateShopInventory(player, inv);
                                break;
                            case 2114:
                                playerData.set(quantityKey, PersistentDataType.INTEGER, 1 * i);
                                UpdateShop.UpdateShopInventory(player, inv);
                                break;
                            case 2115:
                                playerData.set(quantityKey, PersistentDataType.INTEGER, 4 * i);
                                UpdateShop.UpdateShopInventory(player, inv);
                                break;
                            case 2116:
                                playerData.set(quantityKey, PersistentDataType.INTEGER, 16 * i);
                                if (16 * i > 999) {
                                    player.sendMessage("That Amount is too large!");
                                    this.PlayDenySound(player);
                                } else {
                                    UpdateShop.UpdateShopInventory(player, inv);
                                }
                                break;
                            case 2117:
                                if (64 * i > 999) {
                                    player.sendMessage("That Amount is too large!");
                                    this.PlayDenySound(player);
                                } else {
                                    playerData.set(quantityKey, PersistentDataType.INTEGER, 64 * i);
                                    UpdateShop.UpdateShopInventory(player, inv);
                                }
                        }
                    } else if (!item.getType().equals(Material.LIME_WOOL)) {
                        if (item.getType().equals(Material.RED_WOOL)) {
                            player.closeInventory();
                            Util.ResetShopData(player);
                        } else if (!itemMeta.getDisplayName().equalsIgnoreCase(" ")) {
                            playerData.set(ItemIndexKey, PersistentDataType.INTEGER, event.getSlot());
                            playerData.set(quantityKey, PersistentDataType.INTEGER, item.getAmount());
                            UpdateShop.UpdateShopInventory(player, inv);
                        }
                    } else if (!playerData.has(ItemIndexKey, PersistentDataType.INTEGER)) {
                        player.sendMessage("Select an item!");
                        this.PlayDenySound(player);
                    } else {
                        quantity = (Integer)playerData.get(ItemIndexKey, PersistentDataType.INTEGER);
                        NamespacedKey ValueAmountKey = new NamespacedKey(PhysicalEconomy.getPlugin(), "ValueAmount");
                        NamespacedKey shopTypeKey = new NamespacedKey(PhysicalEconomy.getPlugin(), "ShopType");
                        NamespacedKey shopNameKey = new NamespacedKey(PhysicalEconomy.getPlugin(), "ShopName");
                        ItemStack[] items = FileManager.LoadShop((String)playerData.get(shopNameKey, PersistentDataType.STRING));
                        item = items[quantity];
                        itemMeta = item.getItemMeta();
                        PersistentDataContainer itemData = itemMeta.getPersistentDataContainer();
                        quantity = (Integer)playerData.get(quantityKey, PersistentDataType.INTEGER);
                        int valueAmount = (Integer)itemData.get(ValueAmountKey, PersistentDataType.INTEGER) * (quantity / item.getAmount());
                        PlayerInventory playerInventory = player.getInventory();
                        ItemStack[] playerItems = playerInventory.getContents();
                        int emptySlots = 0;
                        int slotsNeeded = quantity / 65 + 1;
                        item = ItemUtil.ResetItemData(item);
                        int lastQuantity;
                        i = 0;
                        if (((String)itemData.get(shopTypeKey, PersistentDataType.STRING)).equalsIgnoreCase("buy")) {
                            int amountInInventory = Util.cashOnPlayer(player);
                            if (amountInInventory < valueAmount) {
                                if (valueAmount - amountInInventory > 1) {
                                    player.sendMessage("You don't have enough money! You are §2$" + (valueAmount - amountInInventory) + " Dollars away");
                                    this.PlayDenySound(player);
                                } else {
                                    player.sendMessage("You don't have enough money! You are §2$" + (valueAmount - amountInInventory) + " Dollar away");
                                    this.PlayDenySound(player);
                                }
                            } else {
                                for(lastQuantity = 0; lastQuantity < playerItems.length - 5; ++lastQuantity) {
                                    if (playerItems[lastQuantity] == null) {
                                        ++emptySlots;
                                    }
                                }

                                if (emptySlots >= slotsNeeded) {
                                    if (!playerData.has(this.clickCooldown, PersistentDataType.STRING)) {
                                        playerData.set(this.clickCooldown, PersistentDataType.STRING, "Clicked");
                                        BukkitTask clickCooldown1 = (new ClickCooldown(player)).runTaskLater(this.mainPlugin, 2L);
                                        Commands.removeMoney(valueAmount, player);

                                        for(i = 0; i < slotsNeeded; ++i) {
                                            if (quantity > 64) {
                                                playerInventory.addItem(new ItemStack[]{item.asQuantity(64)});
                                                quantity -= 64;
                                            } else {
                                                playerInventory.addItem(new ItemStack[]{item.asQuantity(quantity)});
                                                quantity = 0;
                                            }
                                        }

                                        this.PlayAcceptSound(player);
                                        if (itemMeta.getDisplayName().equals("")) {
                                            player.sendMessage("§eYou bought " + quantity + " " + item.getType().toString().toLowerCase(Locale.ROOT) + "!");
                                        } else {
                                            player.sendMessage("§eYou bought " + quantity + " " + itemMeta.getDisplayName() + "!");
                                        }
                                    }
                                } else {
                                    player.sendMessage("You don't have enough slots open in your inventory!");
                                    this.PlayDenySound(player);
                                }

                                event.setCancelled(true);
                            }
                        } else if (((String)itemData.get(shopTypeKey, PersistentDataType.STRING)).equalsIgnoreCase("sell")) {
                            List<Integer> slotsToRemove = new ArrayList();
                            lastQuantity = 0;

                            for(i = 0; i < playerItems.length; ++i) {
                                if (playerItems[i] != null && playerItems[i].isSimilar(item) && quantity >= 0) {
                                    if (quantity - playerItems[i].getAmount() < 0) {
                                        lastQuantity = playerItems[i].getAmount() - quantity;
                                    }

                                    quantity -= playerItems[i].getAmount();
                                    slotsToRemove.add(i);
                                }
                            }

                            if (quantity > 0) {
                                player.sendMessage("You dont have enough of that item!");
                                this.PlayDenySound(player);
                            } else {
                                for(i = 0; i < slotsToRemove.size() - 1; ++i) {
                                    playerInventory.setItem((Integer)slotsToRemove.get(i), (ItemStack)null);
                                }

                                if (lastQuantity > 0) {
                                    playerInventory.setItem((Integer)slotsToRemove.get(slotsToRemove.size() - 1), item.asQuantity(lastQuantity));
                                } else {
                                    playerInventory.setItem((Integer)slotsToRemove.get(slotsToRemove.size() - 1), (ItemStack)null);
                                }

                                String[] simulateargs = new String[]{Integer.toString(valueAmount)};
                                Commands.giveMoney(simulateargs, player);
                                this.PlayAcceptSound(player);
                            }
                        }
                    }
                }

                if (inv.getType().equals(InventoryType.CHEST) && item != null && item.getType().equals(Material.BLACK_STAINED_GLASS_PANE) && item.getItemMeta().getPersistentDataContainer().has(namespacedKey, PersistentDataType.INTEGER)) {
                    event.setCancelled(true);
                }

                NamespacedKey creatingShopKey = new NamespacedKey(PhysicalEconomy.getPlugin(), "CreatingShop");
                if (playerData.has(creatingShopKey, PersistentDataType.INTEGER) && item != null && !item.getItemMeta().getPersistentDataContainer().has(namespacedKey, PersistentDataType.INTEGER) && event.getClick().isRightClick() && inv.getSize() == 54) {
                    quantityKey = new NamespacedKey(PhysicalEconomy.getPlugin(), "ItemIndex");
                    playerData.set(quantityKey, PersistentDataType.INTEGER, event.getSlot());
                    event.setCancelled(true);
                    player.closeInventory();
                    player.sendMessage("Use /createshop buy/sell value to set a value for this item in the shop" + event.getSlot());
                }
            } else if (playerData.has(this.creatingShopKey, PersistentDataType.INTEGER)) {
                item = event.getCurrentItem();
                itemMeta = item.getItemMeta();
                if (itemMeta.getPersistentDataContainer().has(this.placeholderKey, PersistentDataType.INTEGER)) {
                    event.setCancelled(true);
                } else if (event.getClick().isRightClick()) {
                    playerData.set(this.itemIndexKey, PersistentDataType.INTEGER, event.getSlot());
                    player.sendMessage("/createshop buy/sell ValueAmount");
                    event.setCancelled(true);
                    player.closeInventory();
                }
            } else if (playerData.has(this.viewingBankKey, PersistentDataType.INTEGER)) {
                event.setCancelled(true);
                NamespacedKey billAmountKey = new NamespacedKey(PhysicalEconomy.getPlugin(), "BillAmount");
                item = inv.getItem(event.getSlot());
                if (item != null) {
                    itemMeta = item.getItemMeta();
                    if (inv.getItem(event.getSlot()).getItemMeta().getDisplayName().equalsIgnoreCase("withdraw")) {
                        OpenBank.OpenBankWithdraw(player);
                        playerData.set(this.viewingBankKey, PersistentDataType.INTEGER, 1);
                        playerData.set(billAmountKey, PersistentDataType.STRING, "0;0;0;0;0;0");
                    } else if (inv.getItem(event.getSlot()).getItemMeta().getDisplayName().equalsIgnoreCase("deposit")) {
                        OpenBank.OpenBankDeposit(player);
                        playerData.set(this.viewingBankKey, PersistentDataType.INTEGER, 1);
                        playerData.set(billAmountKey, PersistentDataType.STRING, "0;0;0;0;0;0");
                    } else if (itemMeta.hasCustomModelData() && event.getClickedInventory().getSize() > 41 && itemMeta.getCustomModelData() >= 2000 && itemMeta.getCustomModelData() <= 2005 && !playerData.has(this.clickCooldown, PersistentDataType.STRING)) {
                        playerData.set(this.clickCooldown, PersistentDataType.STRING, "Clicked");
                        BukkitTask clickCooldown1 = (new ClickCooldown(player)).runTaskLater(this.mainPlugin, 2L);
                        if (event.getClick().isRightClick()) {
                            playerData.set(billAmountKey, PersistentDataType.STRING, BankingUtil.AddPlayerDollars(player, itemMeta, -1));
                        } else {
                            playerData.set(billAmountKey, PersistentDataType.STRING, BankingUtil.AddPlayerDollars(player, itemMeta, 1));
                        }

                        BankingUtil.UpdateBank(inv, player);
                    }

                    if (item.getType().equals(Material.LIME_CONCRETE)) {
                        int totalValue = BankingUtil.getTotalValue(player);
                        int[] billAmount = BankingUtil.ParseBillAmount(player);
                        if (event.getView().getTitle().equalsIgnoreCase("withdraw")) {
                            if (BankingUtil.WithdrawMoney(player, totalValue)) {
                                for(i = 0; i < billAmount.length; ++i) {
                                    this.simulateArgs[0] = Integer.toString(billAmount[i]);
                                    this.simulateArgs[1] = Integer.toString(i);
                                    if (Integer.parseInt(this.simulateArgs[0]) > 0) {
                                        this.amounts = Commands.giveMoneyItems(this.simulateArgs, player);
                                        this.amountDropped += this.amounts[1];
                                    }
                                }

                                player.sendMessage("You withdrew §2$" + totalValue + " Dollars§r!");
                                if (this.amountDropped > 1) {
                                    player.sendMessage("You dropped §2$" + this.amountDropped + " Dollars§r!");
                                }

                                this.amountDropped = 0;
                                this.PlayAcceptSound(player);
                                player.closeInventory();
                            } else {
                                player.sendMessage("You don't have enough money!");
                                this.PlayDenySound(player);
                            }
                        } else if (event.getView().getTitle().equalsIgnoreCase("deposit")) {
                            if (Util.cashOnPlayer(player) >= totalValue) {
                                BankingUtil.DepositMoney(player, totalValue);
                                if (totalValue >= 1) {
                                    player.sendMessage("You deposited §2$" + totalValue + " Dollars§r!");
                                }

                                this.PlayAcceptSound(player);
                                player.closeInventory();
                            } else {
                                player.sendMessage("You don't have enough money!");
                                this.PlayDenySound(player);
                            }
                        }
                    } else if (item.getType().equals(Material.RED_CONCRETE)) {
                        player.closeInventory();
                    } else if (item.getType().equals(Material.GOLD_BLOCK)) {
                        if (Util.cashOnPlayer(player) >= 1) {
                            player.sendMessage("You deposited §2$" + Util.cashOnPlayer(player) + " Dollars§r!");
                            BankingUtil.DepositMoney(player, Util.cashOnPlayer(player));
                            this.PlayAcceptSound(player);
                            player.closeInventory();
                        } else {
                            player.sendMessage("You don't have any money!");
                            this.PlayDenySound(player);
                        }
                    }
                }
            }
        }

    }

    @EventHandler
    public void onArrowHit(EntityDamageEvent event) {
        if (event.getEntity() instanceof ItemFrame && event.getDamage() != 0.0) {
            event.setCancelled(true);
        }

    }

    private void PlayAcceptSound(Player player) {
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 3.0F, 1.0F);
    }

    private void PlayDenySound(Player player) {
        player.playNote(player.getLocation(), Instrument.BASS_DRUM, Note.natural(1, Tone.G));
    }
}
