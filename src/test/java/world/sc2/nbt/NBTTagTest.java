package world.sc2.nbt;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.persistence.PersistentDataType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

class NBTTagTest {

    @Mock
    NamespacedKey mockNamespacedKey;
    @Mock
    ItemStack mockItemStack;
    @Mock
    PersistentDataHolder mockPersistentDataHolder;
    private final String defaultData = "Default data!";
    private NBTTag<String, String> nbtTag;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // setup mockNamespacedKey

        this.nbtTag = new NBTTag<>(mockNamespacedKey, PersistentDataType.STRING, defaultData);
    }

    @DisplayName("getNamespacedKey(): When getNamespacedKey is called, return internal NamespacedKey.")
    @Test
    void whenGetNamespacedKey_returnInternalNamespacedKey() {
        assertEquals(nbtTag.getNamespacedKey(), mockNamespacedKey);
    }

    @DisplayName("getPersistentDataType(): When getPersistentDataType is called, return internal persistentDataType.")
    @Test
    void whenGetPersistentDataType_returnInternalPersistentDataType() {
        assertEquals(nbtTag.getPersistentDataType(), PersistentDataType.STRING);
    }

    @DisplayName("getDefaultData(): When getDefaultData is called, return internal default data")
    @Test
    void getDefaultData() {
        assertEquals(nbtTag.getDefaultData(), defaultData);
    }

    @Test
    void applyTag() {
    }

    @Test
    void testApplyTag() {
    }

    @Test
    void testApplyTag1() {
    }

    @Test
    void testApplyTag2() {
    }

    @Test
    void testApplyTag3() {
    }

    @Test
    void testApplyTag4() {
    }

    @Test
    void removeTag() {
    }

    @Test
    void testRemoveTag() {
    }

    @Test
    void testRemoveTag1() {
    }

    @Test
    void getStoredData() {
    }

    @Test
    void testGetStoredData() {
    }

    @Test
    void hasTag() {
    }

    @Test
    void testHasTag() {
    }

    @Test
    void testHasTag1() {
    }
}