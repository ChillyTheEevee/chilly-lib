package live.chillytheeevee.utility;

import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class ItemUtils {
    public static boolean isAirOrNull(ItemStack i){
        return i == null || i.getType() == Material.AIR;
    }

    public static String getItemName(ItemStack i){
        String name = "null";
        if (i.getItemMeta() != null){
            if (i.getItemMeta().hasDisplayName()){
                name = ChatUtils.chat(i.getItemMeta().getDisplayName());
            } else if (i.getItemMeta().hasLocalizedName()){
                name = ChatUtils.chat(i.getItemMeta().getLocalizedName());
            } else {
                name = i.getType().toString().toLowerCase().replace("_", " ");
            }
        }
        return name;
    }

    public static Map<Integer, ArrayList<ItemStack>> paginateItemStackList(int pageSize, List<ItemStack> allEntries) {
        Map<Integer, ArrayList<ItemStack>> pages = new HashMap<>();
        int stepper = 0;

        for (int pageNumber = 0; pageNumber < Math.ceil((double)allEntries.size()/(double)pageSize); pageNumber++) {
            ArrayList<ItemStack> pageEntries = new ArrayList<>();
            for (int pageEntry = 0; pageEntry < pageSize && stepper < allEntries.size(); pageEntry++, stepper++) {
                pageEntries.add(allEntries.get(stepper));
            }
            pages.put(pageNumber, pageEntries);
        }
        return pages;
    }

    public static void addItem(Player player, ItemStack i, boolean setOwnership){
        Map<Integer, ItemStack> excess = player.getInventory().addItem(i);
        if (!excess.isEmpty()){
            for (Integer slot : excess.keySet()){
                ItemStack slotItem = excess.get(slot);
                Item drop = player.getWorld().dropItem(player.getLocation(), slotItem);
                if (setOwnership) drop.setOwner(player.getUniqueId());
            }
        }
    }

    // Serialization and Deserialization
    public static byte[] serializeItemStack(ItemStack itemStack) {
        return itemStack.serializeAsBytes();
    }

    public static ItemStack deserializeItemStack(byte[] serializedItemStack) {
        return ItemStack.deserializeBytes(serializedItemStack);
    }
}
