package teamdraco.fins.common.items;

import teamdraco.fins.common.entities.MudhorseEntity;
import teamdraco.fins.init.FinsEntities;
import teamdraco.fins.init.FinsSounds;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

import java.util.List;

import net.minecraft.item.Item.Properties;

public class SwampDidgeridooItem extends Item {
    public SwampDidgeridooItem(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getItemInHand(hand);
        List<MudhorseEntity> mudhorses = world.getEntities(FinsEntities.MUDHORSE.get(), player.getBoundingBox().inflate(8), entity -> entity.getCommander() == null);
        if (mudhorses.isEmpty()) {
            player.playSound(SoundEvents.VILLAGER_NO, 0.4f, 1);
            addParticleEffect(ParticleTypes.SMOKE, world, player.getX() - 0.5, player.getY() + 1, player.getZ() - 0.5);
            return ActionResult.pass(stack);
        }

        for (MudhorseEntity mudhorseEntity : mudhorses) {
            mudhorseEntity.setCommander(player);
            addParticleEffect(ParticleTypes.HAPPY_VILLAGER, world, mudhorseEntity.getX() - 0.5, mudhorseEntity.getY() + 1.4, mudhorseEntity.getZ() - 0.5);
        }
        player.playSound(FinsSounds.DIDGERIDOO_PLAY.get(), 0.4f, 1);
        player.getCooldowns().addCooldown(this, 600);
        stack.hurtAndBreak(1, player, entity -> entity.broadcastBreakEvent(hand));
        return ActionResult.success(stack);
    }

    private void addParticleEffect(IParticleData particleData, World world, double x, double y, double z) {
        for(int i = 0; i < 10; ++i) {
            double d2 = random.nextGaussian() * 0.02D;
            double d3 = random.nextGaussian() * 0.02D;
            double d4 = random.nextGaussian() * 0.02D;
            double d6 = x + random.nextDouble();
            double d7 = y + random.nextDouble() * 0.5;
            double d8 = z + random.nextDouble();
            world.addParticle(particleData, d6, d7, d8, d2, d3, d4);
        }
    }
}
