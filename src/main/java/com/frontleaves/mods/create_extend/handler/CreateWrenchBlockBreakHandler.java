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

/**
 * Create 扳手方块破坏处理器。
 *
 * <p>实现 FTB Ultimine 的 {@link BlockBreakHandler} 接口，
 * 在玩家使用 FTB Ultimine 连锁挖掘且手持 Create 扳手时，
 * 以正确的扳手拆卸逻辑处理方块破坏（掉落正确物品、播放拆卸音效等）。</p>
 *
 * <p>处理流程：</p>
 * <ol>
 *   <li>检查玩家是否手持 Create 扳手（主手或副手）</li>
 *   <li>检查玩家是否按住 Shift（扳手拆卸模式）</li>
 *   <li>检查目标方块是否为 {@link IWrenchable} 或带有扳手拾取标签</li>
 *   <li>触发 NeoForge 破坏事件并处理掉落物</li>
 *   <li>播放 Create 扳手拆卸音效</li>
 * </ol>
 */
public enum CreateWrenchBlockBreakHandler implements BlockBreakHandler {
    /** 单例实例 */
    INSTANCE;

    /**
     * 处理单个方块的破坏逻辑。
     *
     * @param player    执行破坏的玩家
     * @param pos       方块位置
     * @param state     方块状态
     * @param shape     FTB Ultimine 挖掘形状
     * @param hitResult 方块命中结果
     * @return 处理结果：{@link Result#SUCCESS} 表示成功处理，{@link Result#PASS} 表示跳过，{@link Result#FAIL} 表示取消
     */
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

    /**
     * 所有方块破坏完成后的回调。当前无额外处理逻辑。
     *
     * @param player 执行破坏的玩家
     */
    @Override
    public void postBreak(Player player) {
    }
}
