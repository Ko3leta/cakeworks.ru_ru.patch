package com.teamharmony.cakeworks.common.event;

import com.teamabnormals.neapolitan.core.registry.NeapolitanBlocks;
import com.teamharmony.cakeworks.core.CakeWorks;
import com.teamharmony.cakeworks.core.registry.CakeWorksItems;
import com.teamharmony.cakeworks.core.registry.CakeWorksParticleTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class CakeWorksEvents {
    private static final Map<Block, Block> DOUBLE_CAKE_CACHE = new HashMap<>();

    @SubscribeEvent
    public static void onBlockClick(PlayerInteractEvent.RightClickBlock event) {
        Level level = event.getLevel();
        BlockPos pos = event.getPos();
        BlockState state = level.getBlockState(pos);
        ItemStack stack = event.getItemStack();
        Player player = event.getEntity();

        Block targetCake = getCakeFromIcing(stack.getItem());
        if (targetCake == null) return;

        Block block = state.getBlock();
        String className = block.getClass().getName();

        if (ModList.get().isLoaded("amendments") && className.contains("DoubleCakeBlock")) {
            BlockState mimic = getMimicState(block);

            if (mimic != null && mimic.is(BuiltInRegistries.BLOCK.get(CakeWorks.location("cake_base")))) {
                event.setCanceled(true);
                event.setCancellationResult(InteractionResult.SUCCESS);

                if (!level.isClientSide) {
                    Block doubleTarget = findDoubleCakeFor(targetCake);
                    if (doubleTarget != null) {
                        BlockState newState = doubleTarget.defaultBlockState();
                        if (state.hasProperty(BlockStateProperties.BITES)) newState = newState.setValue(BlockStateProperties.BITES, state.getValue(BlockStateProperties.BITES));

                        level.setBlockAndUpdate(pos, newState);
                        level.playSound(null, pos, SoundEvents.HONEYCOMB_WAX_ON, SoundSource.BLOCKS, 1.0F, 1.0F);
                        consumeIcing(stack, player, event.getHand());
                    }
                } else {
                    spawnIcingParticles(level, pos);
                }
            }
        }
    }

    private static BlockState getMimicState(Block block) {
        try {
            Field field = block.getClass().getDeclaredField("mimic");
            field.setAccessible(true);
            return (BlockState) field.get(block);
        } catch (Exception e) {
            try {
                Field field = block.getClass().getSuperclass().getDeclaredField("mimic");
                field.setAccessible(true);
                return (BlockState) field.get(block);
            } catch (Exception e2) {
                return null;
            }
        }
    }

    private static Block findDoubleCakeFor(Block singleCake) {
        if (DOUBLE_CAKE_CACHE.containsKey(singleCake)) return DOUBLE_CAKE_CACHE.get(singleCake);

        for (Block block : BuiltInRegistries.BLOCK) {
            ResourceLocation id = BuiltInRegistries.BLOCK.getKey(block);
            if (id.getNamespace().equals("amendments") && id.getPath().contains("double")) {
                BlockState mimic = getMimicState(block);
                if (mimic != null && mimic.is(singleCake)) {
                    DOUBLE_CAKE_CACHE.put(singleCake, block);
                    return block;
                }
            }
        }
        return null;
    }

    private static void spawnIcingParticles(Level level, BlockPos pos) {
        for (int i = 0; i < 16; ++i) {
            double x = (double) pos.getX() + level.random.nextDouble();
            double y = (double) pos.getY() + 0.1D + level.random.nextDouble() * 0.8D;
            double z = (double) pos.getZ() + level.random.nextDouble();
            level.addParticle(CakeWorksParticleTypes.ICING.get(), x, y, z, 0.0D, 0.0D, 0.0D);
        }
    }

    private static void consumeIcing(ItemStack stack, Player player, InteractionHand hand) {
        if (!player.getAbilities().instabuild) {
            ItemStack remainder = stack.getCraftingRemainingItem();
            stack.shrink(1);
            if (stack.isEmpty()) player.setItemInHand(hand, remainder);
            else if (!player.getInventory().add(remainder)) player.drop(remainder, false);
        }
        player.awardStat(Stats.ITEM_USED.get(stack.getItem()));
    }

    private static Block getCakeFromIcing(Item item) {
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