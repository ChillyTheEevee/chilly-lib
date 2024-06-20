package world.sc2.utility;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.potion.PotionEffect;

import java.util.Collection;
import java.util.HashSet;

public class EntityUtils {


    public static LivingEntity getRealAttacker(Entity e){
        if (e instanceof Projectile){
            if (((Projectile) e).getShooter() instanceof LivingEntity) return (LivingEntity) ((Projectile) e).getShooter();
        }
        if (e instanceof LivingEntity) return (LivingEntity) e;
        return null;
    }

    public static void applyPotionEffectIfStronger(LivingEntity entity, PotionEffect effect){
        PotionEffect existingEffect = entity.getPotionEffect(effect.getType());
        if (existingEffect != null){
            if (existingEffect.getAmplifier() <= effect.getAmplifier()){
                entity.addPotionEffect(effect);
            }
        } else {
            entity.addPotionEffect(effect);
        }
    }

    private static String errorPlayerNotFound = "&cPlayer not found";
    private static String errorMalformedTargeter = "&cMalformed target selector";

    public static Collection<Player> selectPlayers(CommandSender source, String selector){
        Collection<Player> targets = new HashSet<>();
        if (selector.startsWith("@")){
            try {
                for (Entity part : Bukkit.selectEntities(source, selector)){
                    if (part instanceof Player){
                        targets.add((Player) part);
                    }
                }
            } catch (IllegalArgumentException e){
                source.sendMessage(ChatUtils.chat(errorMalformedTargeter.replace("%error%", e.getMessage())));
                return targets;
            }
        } else {
            Player target = Bukkit.getServer().getPlayer(selector);
            if (target == null){
                source.sendMessage(ChatUtils.chat(errorPlayerNotFound));
                return targets;
            }
            targets.add(target);
        }
        return targets;
    }
}
