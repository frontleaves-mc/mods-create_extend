package com.frontleaves.mods.create_extend.content.kinetics.engine;

import com.frontleaves.mods.create_extend.content.kinetics.engine.entity.CustomEngineBlockEntity;
import com.simibubi.create.content.kinetics.base.DirectionalKineticBlock;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;

import java.util.function.Supplier;

/**
 * 自定义动力引擎方块。
 *
 * <p>继承 Create 的 {@link DirectionalKineticBlock}，实现 {@link IBE} 接口，
 * 提供自定义的动力源方块，隐藏应力影响。</p>
 */
public class CustomEngineBlock extends DirectionalKineticBlock implements IBE<CustomEngineBlockEntity> {

    /** 固定应力容量值（SU），不随转速变化 */
    private final float stressCapacity;

    private final Supplier<BlockEntityType<? extends CustomEngineBlockEntity>> blockEntityTypeSupplier;

    public CustomEngineBlock(
        Properties properties,
        float stressCapacity,
        Supplier<BlockEntityType<? extends CustomEngineBlockEntity>> blockEntityTypeSupplier
    ) {
        super(properties);
        this.stressCapacity = stressCapacity;
        this.blockEntityTypeSupplier = blockEntityTypeSupplier;
    }

    /**
     * 获取该引擎方块的固定应力容量值。
     *
     * @return 固定 SU 值
     */
    public float getStressCapacity() {
        return stressCapacity;
    }

    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return face == state.getValue(FACING);
    }

    @Override
    public Axis getRotationAxis(BlockState state) {
        return state.getValue(FACING).getAxis();
    }

    @Override
    public boolean hideStressImpact() {
        return true;
    }

    @Override
    public Class<CustomEngineBlockEntity> getBlockEntityClass() {
        return CustomEngineBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends CustomEngineBlockEntity> getBlockEntityType() {
        return blockEntityTypeSupplier.get();
    }

    @Override
    protected boolean isPathfindable(BlockState state, PathComputationType pathComputationType) {
        return false;
    }
}
