package com.teamharmony.cakeworks.common.block;
import com.teamabnormals.neapolitan.core.registry.NeapolitanBlocks;
import com.teamharmony.cakeworks.core.registry.CakeWorksItems;
import com.teamharmony.cakeworks.core.registry.CakeWorksParticleTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CakeBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.fml.ModList;
public class CakeWorksCakeBlock extends CakeBlock {

    public CakeWorksCakeBlock(FoodProperties foodProperties, Properties properties) {
        super(properties);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        Item item = stack.getItem();
        Block targetCake = getCakeFromIcing(item);

        if (targetCake != null) {
            level.playSound(player, pos, SoundEvents.HONEYCOMB_WAX_ON, SoundSource.BLOCKS, 1.0F, 1.0F);

            if (level.isClientSide) {
                for (int i = 0; i < 16; ++i) {
                    double x = pos.getX();
                    double y = pos.getY() + 0.1D + level.random.nextDouble() * 0.4D;
                    double z = pos.getZ();

                    int side = level.random.nextInt(4);
                    if (side == 0) { x += 0.0625D; z += level.random.nextDouble(); }
                    else if (side == 1) { x += 0.9375D; z += level.random.nextDouble(); }
                    else if (side == 2) { x += level.random.nextDouble(); z += 0.0625D; }
                    else { x += level.random.nextDouble(); z += 0.9375D; }

                    x = Math.max(pos.getX() + 0.0625D, Math.min(pos.getX() + 0.9375D, x));
                    z = Math.max(pos.getZ() + 0.0625D, Math.min(pos.getZ() + 0.9375D, z));

                    level.addParticle(CakeWorksParticleTypes.ICING.get(), x, y, z, 0.0D, 0.0D, 0.0D);
                }
            } else {
                int currentBites = state.getValue(BITES);
                level.setBlockAndUpdate(pos, targetCake.defaultBlockState().setValue(BITES, currentBites));

                if (!player.getAbilities().instabuild) {
                    ItemStack remainder = stack.getCraftingRemainingItem();
                    stack.shrink(1);

                    if (stack.isEmpty()) {
                        player.setItemInHand(hand, remainder);
                    } else if (!player.getInventory().add(remainder)) {
                        player.drop(remainder, false);
                    }
                }

                player.awardStat(Stats.ITEM_USED.get(item));
            }
            return ItemInteractionResult.SUCCESS;
        }

        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }

    private Block getCakeFromIcing(Item item) {
        if (item == CakeWorksItems.CAKE_ICING.get()) return Blocks.CAKE;

        if (ModList.get().isLoaded("neapolitan")) {
            if (item == CakeWorksItems.VANILLA_ICING.get()) return NeapolitanBlocks.VANILLA_CAKE.get();
            if (item == CakeWorksItems.CHOCOLATE_ICING.get()) return NeapolitanBlocks.CHOCOLATE_CAKE.get();
            if (item == CakeWorksItems.STRAWBERRY_ICING.get()) return NeapolitanBlocks.STRAWBERRY_CAKE.get();
            if (item == CakeWorksItems.BANANA_ICING.get()) return NeapolitanBlocks.BANANA_CAKE.get();
            if (item == CakeWorksItems.MINT_ICING.get()) return NeapolitanBlocks.MINT_CAKE.get();
            if (item == CakeWorksItems.ADZUKI_ICING.get()) return NeapolitanBlocks.ADZUKI_CAKE.get();
        }

        return null;
    }
}