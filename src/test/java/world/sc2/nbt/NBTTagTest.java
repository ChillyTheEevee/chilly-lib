package world.sc2.nbt;

import org.bukkit.NamespacedKey;
import org.bukkit.block.TileState;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.persistence.PersistentDataType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class NBTTagTest {

    @Mock
    NamespacedKey mockNamespacedKey;
    @Mock
    ItemStack mockItemStack;
    @Mock
    ItemMeta mockItemMeta;
    @Mock
    PersistentDataHolder mockPersistentDataHolder;
    @Mock
    TileState mockTileState;
    @Mock
    PersistentDataContainer mockPersistentDataContainer;
    private final String defaultData = "Default data!";
    private final String data = "Hello there!";
    private final PersistentDataType<String, String> persistentDataType = PersistentDataType.STRING;
    private NBTTag<String, String> nbtTag;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // setup mockNamespacedKey

        // setup mockItemStack
        when(mockItemStack.getItemMeta()).thenReturn(mockItemMeta);

        // setup mockItemMeta
        when(mockItemMeta.getPersistentDataContainer()).thenReturn(mockPersistentDataContainer);

        // setup mockPersistentDataHolder
        when(mockPersistentDataHolder.getPersistentDataContainer()).thenReturn(mockPersistentDataContainer);

        // setup mockTileState
        when(mockTileState.getPersistentDataContainer()).thenReturn(mockPersistentDataContainer);

        this.nbtTag = new NBTTag<>(mockNamespacedKey, persistentDataType, defaultData);
    }

    @DisplayName("getNamespacedKey(): When getNamespacedKey is called, return internal NamespacedKey.")
    @Test
    void whenGetNamespacedKey_returnInternalNamespacedKey() {
        assertEquals(nbtTag.getNamespacedKey(), mockNamespacedKey);
    }

    @DisplayName("getPersistentDataType(): When getPersistentDataType is called, return internal persistentDataType.")
    @Test
    void whenGetPersistentDataType_returnInternalPersistentDataType() {
        assertEquals(nbtTag.getPersistentDataType(), persistentDataType);
    }

    @DisplayName("getDefaultData(): When getDefaultData is called and default data was not supplied, return null")
    @Test
    void whenGetDefaultData_ifDefaultDataNotSupplied_returnNull() {
        this.nbtTag = new NBTTag<>(mockNamespacedKey, persistentDataType);
        assertNull(nbtTag.getDefaultData());
    }

    @DisplayName("applyTag(ItemStack): When applyTag is called and default data was supplied, apply default data to " +
            "the ItemStack's PersistentDataContainer")
    @Test
    void whenApplyTagItemStack_ifDefaultDataSupplied_applyDefaultData() {
        nbtTag.applyTag(mockItemStack);
        verify(mockItemStack).getItemMeta();
        verify(mockPersistentDataContainer).set(mockNamespacedKey, persistentDataType, defaultData);
        verify(mockItemStack).setItemMeta(mockItemMeta);
    }

    @DisplayName("applyTag(ItemStack): When applyTag is called and default data was not supplied, " +
            "throw IllegalStateException")
    @Test
    void whenApplyTagItemStack_ifDefaultDataNotSupplied_throwIllegalStateException() {
        this.nbtTag = new NBTTag<>(mockNamespacedKey, persistentDataType);
        Assertions.assertThrows(IllegalStateException.class, () -> nbtTag.applyTag(mockItemStack) );
    }

    @DisplayName("applyTag(PersistentDataHolder): When applyTag is called and default data was supplied, apply default" +
            " data to the PersistentDataHolder's PersistentDataContainer.")
    @Test
    void whenApplyTagPersistentDataHolder_ifDefaultDataSupplied_applyDefaultData() {
        nbtTag.applyTag(mockPersistentDataHolder);
        verify(mockPersistentDataContainer).set(mockNamespacedKey, persistentDataType, defaultData);
    }

    @DisplayName("applyTag(PersistentDataHolder): When applyTag is called and default data was not supplied, throw" +
            " IllegalStateException")
    @Test
    void whenApplyTagPersistentDataHolder_ifDefaultDataNotSupplied_throwIllegalStateException() {
        this.nbtTag = new NBTTag<>(mockNamespacedKey, persistentDataType);
        Assertions.assertThrows(IllegalStateException.class, () -> nbtTag.applyTag(mockPersistentDataHolder));
    }

    @DisplayName("applyTag(TileState): When applyTag is called and default data was supplied, apply default" +
            " data to the TileState and update the TileState.")
    @Test
    void whenApplyTagTileState_ifDefaultDataSupplied_applyDefaultData() {
        nbtTag.applyTag(mockTileState);
        verify(mockPersistentDataContainer).set(mockNamespacedKey, persistentDataType, defaultData);
        verify(mockTileState).update();
    }

    @DisplayName("applyTag(TileState): When applyTag is called and default data was not supplied, throw" +
            " IllegalStateException")
    @Test
    void whenApplyTagTileState_ifDefaultDataNotSupplied_throwIllegalStateException() {
        this.nbtTag = new NBTTag<>(mockNamespacedKey, persistentDataType);
        Assertions.assertThrows(IllegalStateException.class, () -> nbtTag.applyTag(mockTileState));
    }

    @DisplayName("applyTag(ItemStack, Z): When applyTag is called, apply supplied data to the ItemStack's " +
            "PersistentDataContainer")
    @Test
    void whenApplyTagItemStackZ_applyGivenData() {
        nbtTag.applyTag(mockItemStack, data);
        verify(mockItemStack).getItemMeta();
        verify(mockPersistentDataContainer).set(mockNamespacedKey, persistentDataType, data);
        verify(mockItemStack).setItemMeta(mockItemMeta);
    }

    @DisplayName("applyTag(PersistentDataHolder, Z): When applyTag is called, apply data to the PersistentDataHolder's "
            + "PersistentDataContainer.")
    @Test
    void whenApplyTagPersistentDataHolderZ_applyData() {
        nbtTag.applyTag(mockPersistentDataHolder, data);
        verify(mockPersistentDataContainer).set(mockNamespacedKey, persistentDataType, data);
    }

    @DisplayName("applyTag(TileState, Z): When applyTag is called, apply data to the TileState and update the " +
            "TileState.")
    @Test
    void whenApplyTagTileStateZ_applyData() {
        nbtTag.applyTag(mockTileState, data);
        verify(mockPersistentDataContainer).set(mockNamespacedKey, persistentDataType, data);
        verify(mockTileState).update();
    }

    @DisplayName("removeTag(ItemStack): When removeTag is called, remove NamespacedKey from ItemMeta PDC")
    @Test
    void whenRemoveTagItemStack_removeTagFromPDC() {
        nbtTag.removeTag(mockItemStack);
        verify(mockItemStack).getItemMeta();
        verify(mockPersistentDataContainer).remove(mockNamespacedKey);
        verify(mockItemStack).setItemMeta(mockItemMeta);
    }

    @DisplayName("removeTag(PersistentDataHolder): When removeTag is called, remove NamespacedKey from PDC")
    @Test
    void whenRemoveTagPersistentDataHolder_removeTagFromPDC() {
        nbtTag.removeTag(mockPersistentDataHolder);
        verify(mockPersistentDataContainer).remove(mockNamespacedKey);
    }

    @DisplayName("removeTag(TileState): When removeTag is called, remove NamespacedKey from TileState PDC and" +
            " update TileState")
    @Test
    void whenRemoveTagTileState_removeTagFromPDC() {
        nbtTag.removeTag(mockTileState);
        verify(mockPersistentDataContainer).remove(mockNamespacedKey);
        verify(mockTileState).update();
    }

    @DisplayName("getStoredData(ItemStack): When getStoredData is called, if ItemStack has data, return data")
    @Test
    void whenGetStoredDataItemStack_ifItemStackHasData_returnData() {
        when(mockPersistentDataContainer.get(mockNamespacedKey, persistentDataType)).thenReturn(data);
        assertEquals(nbtTag.getStoredData(mockItemStack), data);
        verify(mockPersistentDataContainer).get(mockNamespacedKey, persistentDataType);
    }

    @DisplayName("getStoredData(ItemStack): When getStoredData is called, if ItemStack has no data, return null")
    @Test
    void whenGetStoredDataItemStack_ifItemStackHasNoData_returnNull() {
        when(mockPersistentDataContainer.get(mockNamespacedKey, persistentDataType)).thenReturn(null);
        assertNull(nbtTag.getStoredData(mockItemStack));
        verify(mockPersistentDataContainer).get(mockNamespacedKey, persistentDataType);
    }

    @DisplayName("getStoredData(PersistentDataHolder): When getStoredData is called, if PDC has data, return data")
    @Test
    void whenGetStoredDataPersistentDataHolder_ifPDCHasData_returnData() {
        when(mockPersistentDataContainer.get(mockNamespacedKey, persistentDataType)).thenReturn(data);
        assertEquals(nbtTag.getStoredData(mockPersistentDataHolder), data);
        verify(mockPersistentDataContainer).get(mockNamespacedKey, persistentDataType);
    }

    @DisplayName("getStoredData(PersistentDataHolder): When getStoredData is called, if PDC has no data, return null")
    @Test
    void whenGetStoredDataPersistentDataHolder_ifItemStackHasNoData_returnNull() {
        when(mockPersistentDataContainer.get(mockNamespacedKey, persistentDataType)).thenReturn(null);
        assertNull(nbtTag.getStoredData(mockPersistentDataHolder));
        verify(mockPersistentDataContainer).get(mockNamespacedKey, persistentDataType);
    }

    @DisplayName("hasTag(ItemStack): When hasTag is called, if ItemStack has tag, return true")
    @Test
    void whenHasTagItemStack_ifHasTag_returnTrue() {
        when(mockPersistentDataContainer.has(mockNamespacedKey)).thenReturn(true);
        assertTrue(nbtTag.hasTag(mockItemStack));
        verify(mockItemStack).getItemMeta();
        verify(mockItemMeta).getPersistentDataContainer();
    }

    @DisplayName("hasTag(ItemStack): When hasTag is called, if ItemStack does not have tag, return false")
    @Test
    void whenHasTagItemStack_ifNotHaveTag_returnFalse() {
        when(mockPersistentDataContainer.has(mockNamespacedKey)).thenReturn(false);
        assertFalse(nbtTag.hasTag(mockItemStack));
        verify(mockItemStack).getItemMeta();
        verify(mockItemMeta).getPersistentDataContainer();
    }

    @DisplayName("hasTag(PersistentDataHolder): When hasTag is called, if PDC has tag, return true")
    @Test
    void whenHasTagPersistentDataHolder_ifHasTag_returnTrue() {
        when(mockPersistentDataContainer.has(mockNamespacedKey)).thenReturn(true);
        assertTrue(nbtTag.hasTag(mockPersistentDataHolder));
        verify(mockPersistentDataHolder).getPersistentDataContainer();
    }

    @DisplayName("hasTag(PersistentDataHolder): When hasTag is called, if PDC does not have tag, return false")
    @Test
    void whenHasTagPersistentDataHolder_ifNotHaveTag_returnFalse() {
        when(mockPersistentDataContainer.has(mockNamespacedKey)).thenReturn(false);
        assertFalse(nbtTag.hasTag(mockPersistentDataHolder));
        verify(mockPersistentDataHolder).getPersistentDataContainer();
    }

    @DisplayName("hasTag(TileState): When hasTag is called, if PDC has tag, return true")
    @Test
    void whenHasTagTileState_ifHasTag_returnTrue() {
        when(mockPersistentDataContainer.has(mockNamespacedKey)).thenReturn(true);
        assertTrue(nbtTag.hasTag(mockTileState));
        verify(mockTileState).getPersistentDataContainer();
    }

    @DisplayName("hasTag(TileState): When hasTag is called, if PDC does not have tag, return false")
    @Test
    void whenHasTagTileState_ifNotHaveTag_returnFalse() {
        when(mockPersistentDataContainer.has(mockNamespacedKey)).thenReturn(false);
        assertFalse(nbtTag.hasTag(mockTileState));
        verify(mockTileState).getPersistentDataContainer();
    }
}