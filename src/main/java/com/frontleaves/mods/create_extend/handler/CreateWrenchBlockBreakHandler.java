package com.frontleaves.mods.create_extend.handler;

import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.AllTags;
import com.simibubi.create.Create;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.content.equipment.wrench.WrenchItem;
import dev.ftb.mods.ftbultimine.api.blockbreaking.BlockBreakHandler;
import dev.ftb.mods.ftbultimine.api.shape.Shape;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.level.BlockEvent;

public enum CreateWrenchBlockBreakHandler implements BlockBreakHandler {
    INSTANCE;

    @Override
    public Result breakBlock(Player player, BlockPos pos, BlockState state, Shape shape, BlockHitResult hitResult) {
        if (!(player.getMainHandItem().getItem() instanceof WrenchItem) && !(player.getOffhandItem().getItem() instanceof WrenchItem)) {
            return Result.PASS;
        }

        if (!player.isShiftKeyDown()) {
            return Result.PASS;
        }

        Block block = state.getBlock();
        if (!(block instanceof IWrenchable) && !AllTags.AllBlockTags.WRENCH_PICKUP.matches(state)) {
            return Result.PASS;
        }

        Level level = player.level();
        if (level.isClientSide()) {
            return Result.SUCCESS;
        }

        if (player.isCreative()) {
            level.destroyBlock(pos, false);
            return Result.SUCCESS;
        }

        ServerLevel serverLevel = (ServerLevel) level;

        var breakEvent = new BlockEvent.BreakEvent(level, pos, state, player);
        if (NeoForge.EVENT_BUS.post(breakEvent).isCanceled()) {
            return Result.FAIL;
        }

        ItemStack wrenchItem = player.getMainHandItem().getItem() instanceof WrenchItem
                ? player.getMainHandItem()
                : player.getOffhandItem();

        // ItemEntities are intercepted by FTB Ultimine's EntityJoinLevelEvent handler
        var blockEntity = level.getBlockEntity(pos);
        var drops = Block.getDrops(state, serverLevel, pos, blockEntity, player, wrenchItem);
        for (ItemStack itemStack : drops) {
            serverLevel.addFreshEntity(new ItemEntity(
                    serverLevel,
                    pos.getX() + 0.5,
                    pos.getY() + 0.5,
                    pos.getZ() + 0.5,
                    itemStack
            ));
        }

        state.spawnAfterBreak(serverLevel, pos, ItemStack.EMPTY, true);
        level.destroyBlock(pos, false);
        AllSoundEvents.WRENCH_REMOVE.playOnServer(level, pos, 1, Create.RANDOM.nextFloat() * .5f + .5f);

        return Result.SUCCESS;
    }

    @Override
    public void postBreak(Player player) {
    }
}
