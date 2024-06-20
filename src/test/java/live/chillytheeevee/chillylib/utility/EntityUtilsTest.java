package live.chillytheeevee.chillylib.utility;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

class EntityUtilsTest {

    @Mock
    Projectile mockProjectile;

    @Mock
    LivingEntity mockShooter;

    @Mock
    Entity mockNonLivingEntity;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        when(mockProjectile.getShooter()).thenReturn(mockShooter);
    }

    @DisplayName("EntityUtils.getRealAttacker(): If entity is a projectile, then its LivingEntity shooter is returned")
    @Test
    void whenGetRealAttacker_ifEntityIsProjectileShotByLivingEntity_thenReturnItsShooter() {
        assertEquals(EntityUtils.getRealAttacker(mockProjectile), mockShooter);
    }

    @DisplayName("EntityUtils.getRealAttacker(): If parameter entity is a LivingEntity, then parameter is returned")
    @Test
    void whenGetRealAttacker_ifGivenEntityIsInstanceofLivingEntity_thenReturnGivenEntity() {
        assertEquals(EntityUtils.getRealAttacker(mockShooter), mockShooter);
    }

    @DisplayName("EntityUtils.getRealAttacker(): If parameter entity is not a projectile nor a LivingEntity," +
            " null is returned")
    @Test
    void whenGetRealAttacker_ifGivenEntityIsNotALivingEntityOrProjectile_thenReturnNull() {
        assertNull(EntityUtils.getRealAttacker(mockNonLivingEntity));
    }
}