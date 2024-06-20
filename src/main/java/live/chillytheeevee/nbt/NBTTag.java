package live.chillytheeevee.nbt;

import org.bukkit.NamespacedKey;
import org.bukkit.block.TileState;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

/**
 * Represents all data stored within an NBT Tag. This data includes the {@link NamespacedKey} of the NBTTag, the
 * {@link PersistentDataType} of the NBT Tag, and the actual Data stored within that NBT Tag.
 */
public class NBTTag<T, Z> {

    private final NamespacedKey namespacedKey;
    private final PersistentDataType<T, Z> persistentDataType;
    private final Z defaultData;

    /**
     * Creates a new NBTTag Object without starting data.
     * @param namespacedKey The {@link NamespacedKey} of this NBT Tag.
     * @param persistentDataType The data type of this NBT Tag.
     */
    public NBTTag(NamespacedKey namespacedKey, PersistentDataType<T, Z> persistentDataType) {
        this.namespacedKey = namespacedKey;
        this.persistentDataType = persistentDataType;
        defaultData = null;
    }

    /**
     * Creates a new NBTTag Object with initially stored data.
     * @param namespacedKey The {@link NamespacedKey} of this NBT Tag.
     * @param persistentDataType The data type of this NBT Tag.
     * @param defaultData The data initially stored within this NBT Tag.
     */
    public NBTTag(NamespacedKey namespacedKey, PersistentDataType<T, Z> persistentDataType, Z defaultData) {
        this.namespacedKey = namespacedKey;
        this.persistentDataType = persistentDataType;
        this.defaultData = defaultData;
    }

    /**
     * @return The {@link NamespacedKey} of this NBT Tag.
     */
    public NamespacedKey getNamespacedKey() {
        return namespacedKey;
    }

    /**
     * @return The data type of this NBT Tag.
     */
    public PersistentDataType<T, Z> getPersistentDataType() {
        return persistentDataType;
    }

    /**
     * @return The data initially stored within this NBT Tag.
     */
    public Z getDefaultData() {
        return defaultData;
    }

    /**
     * Applies this NBT Tag to the {@link ItemStack} given.
     * @param item The {@link ItemStack} to apply this NBT Tag to.
     * @throws IllegalStateException if called when default data was not supplied to the NBT Tag.
     */
    public void applyTag(@NotNull ItemStack item) {
        if (defaultData == null) {
            throw new IllegalStateException("Attempted to apply NBT Tag " + namespacedKey + ", which does not have a " +
                    "set default value, to an item without supplying a value.");
        }
        ItemMeta itemMeta = item.getItemMeta();
        applyTag(itemMeta);
        item.setItemMeta(itemMeta);
    }

    /**
     * Applies this NBT Tag to the {@link PersistentDataHolder} given.
     * @param persistentDataHolder The {@link PersistentDataHolder} to apply this NBT Tag to.
     * @throws IllegalStateException if called when default data was not supplied to the NBT Tag.
     */
    public void applyTag(@NotNull PersistentDataHolder persistentDataHolder) {
        if (defaultData == null) {
            throw new IllegalStateException("Attempted to apply NBT Tag " + namespacedKey + ", which does not have a " +
                    "set default value, to a PersistentDataHolder without supplying a value.");
        }
        PersistentDataContainer dataContainer = persistentDataHolder.getPersistentDataContainer();
        dataContainer.set(namespacedKey, persistentDataType, defaultData);
    }

    /**
     * Applies this NBT Tag to the {@link TileState} given.
     * @param tileState The {@link TileState} to apply this NBT Tag to.
     * @throws IllegalStateException if called when default data was not supplied to this NBT Tag.
     */
    public void applyTag(@NotNull TileState tileState) {
        if (defaultData == null) {
            throw new IllegalStateException("Attempted to apply NBT Tag " + namespacedKey + ", which does not have a " +
                    "set default value, to a TileState without supplying a value.");
        }
        PersistentDataContainer dataContainer = tileState.getPersistentDataContainer();
        dataContainer.set(namespacedKey, persistentDataType, defaultData);
        tileState.update();
    }

    /**
     * Applies this NBT Tag to the {@link ItemStack} with the data given.
     * @param item The {@link ItemStack} to apply this NBT Tag to.
     * @param data The data to apply when applying this NBT Tag.
     */
    public void applyTag(@NotNull ItemStack item, Z data) {
        ItemMeta itemMeta = item.getItemMeta();
        applyTag(itemMeta, data);
        item.setItemMeta(itemMeta);
    }

    /**
     * Applies this NBT Tag to the {@link PersistentDataHolder} with the data given.
     * @param persistentDataHolder The {@link PersistentDataHolder} to apply this NBT Tag to.
     * @param data The data to apply when applying this NBT Tag.
     */
    public void applyTag(@NotNull PersistentDataHolder persistentDataHolder, Z data) {
        PersistentDataContainer dataContainer = persistentDataHolder.getPersistentDataContainer();
        dataContainer.set(namespacedKey, persistentDataType, data);
    }

    /**
     * Applies this NBT Tag to the {@link TileState} with the data given.
     * @param tileState The {@link TileState} to apply this NBT Tag to.
     * @param data The data to apply when applying this NBT Tag.
     */
    public void applyTag(@NotNull TileState tileState, Z data) {
        PersistentDataContainer dataContainer = tileState.getPersistentDataContainer();
        dataContainer.set(namespacedKey, persistentDataType, data);
        tileState.update();
    }

    /**
     * Removes this NBTTag and all stored data from the {@link ItemStack} given.
     * @param item The {@link ItemStack} to remove this NBT Tag from.
     */
    public void removeTag(@NotNull ItemStack item) {
        ItemMeta itemMeta = item.getItemMeta();
        removeTag(itemMeta);
        item.setItemMeta(itemMeta);
    }

    /**
     * Removes this NBTTag and all stored data from the {@link PersistentDataHolder} given.
     * @param persistentDataHolder The {@link PersistentDataHolder} to remove this NBT Tag from.
     */
    public void removeTag(@NotNull PersistentDataHolder persistentDataHolder) {
        PersistentDataContainer dataContainer = persistentDataHolder.getPersistentDataContainer();
        dataContainer.remove(namespacedKey);
    }

    /**
     * Removes this NBTTag and all stored data from the {@link TileState} given.
     * @param tileState The {@link TileState} to remove this NBT Tag from.
     */
    public void removeTag(@NotNull TileState tileState) {
        PersistentDataContainer persistentDataContainer = tileState.getPersistentDataContainer();
        persistentDataContainer.remove(namespacedKey);
        tileState.update();
    }

    /**
     * Retrieves the data stored within the NBTTag of the given {@link ItemStack}
     * @param item The {@link ItemStack} to get the data of this NBT Tag from.
     * @return the data mapped within the NBTTag of the {@link ItemStack} or null if the {@link ItemStack} does not
     * contain this NBTTag
     */
    public Z getStoredData(@NotNull ItemStack item) throws IllegalStateException {
        PersistentDataContainer dataContainer = item.getItemMeta().getPersistentDataContainer();
        return dataContainer.get(namespacedKey, persistentDataType);
    }

    /**
     * Retrieves the data stored within the NBTTag of the given {@link PersistentDataHolder}
     * @param persistentDataHolder The {@link PersistentDataHolder} to get the data of this NBT Tag from.
     * @return the data mapped within the NBTTag of the {@link PersistentDataHolder} or null if the
     * {@link PersistentDataHolder} does not contain this NBTTag
     */
    public Z getStoredData(@NotNull PersistentDataHolder persistentDataHolder) {
        PersistentDataContainer dataContainer = persistentDataHolder.getPersistentDataContainer();
        return dataContainer.get(namespacedKey, persistentDataType);
    }

    /**
     * Returns true if the given {@link ItemStack} contains this NBT Tag.
     * @param item The ItemStack to check
     * @return true if the given ItemStack contains this NBT Tag.
     */
     public boolean hasTag(@NotNull ItemStack item) {
        return item.getItemMeta().getPersistentDataContainer().has(namespacedKey);
     }

    /**
     * Returns true if the given {@link PersistentDataHolder} contains this NBT Tag.
     * @param persistentDataHolder The PersistentDataHolder to check
     * @return true if the given PersistentDataHolder contains this NBT Tag.
     */
     public boolean hasTag(@NotNull PersistentDataHolder persistentDataHolder) {
        return persistentDataHolder.getPersistentDataContainer().has(namespacedKey);
     }

    /**
     * Returns true if the given {@link TileState} contains this NBT Tag.
     * @param tileState The TileState to check
     * @return true if the given TileState contains this NBT Tag.
     */
    public boolean hasTag(@NotNull TileState tileState) {
        return tileState.getPersistentDataContainer().has(namespacedKey);
    }

}
