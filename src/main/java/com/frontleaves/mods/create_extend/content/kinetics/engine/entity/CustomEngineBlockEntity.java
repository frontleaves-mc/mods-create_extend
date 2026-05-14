package com.frontleaves.mods.create_extend.content.kinetics.engine.entity;

import com.frontleaves.mods.create_extend.content.kinetics.engine.CustomEngineBlock;
import com.simibubi.create.content.kinetics.base.GeneratingKineticBlockEntity;
import com.simibubi.create.content.kinetics.motor.KineticScrollValueBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueBoxTransform;
import com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.ScrollValueBehaviour;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.engine_room.flywheel.lib.transform.TransformStack;
import net.createmod.catnip.math.AngleHelper;
import net.createmod.catnip.math.VecHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.List;

/**
 * 自定义动力引擎方块实体。
 *
 * <p>继承 Create 的 {@link GeneratingKineticBlockEntity}，实现 SU 恒定不随转速变化的动力源。
 * 核心逻辑：{@code calculateAddedStressCapacity()} 返回 {@code fixedSU / |speed|}，
 * 使得 KineticNetwork 自动计算后的实际 SU 始终等于 fixedSU。</p>
 */
public class CustomEngineBlockEntity extends GeneratingKineticBlockEntity {

    /** 默认转速 */
    public static final int DEFAULT_SPEED = 16;
    /** 最大转速 */
    public static final int MAX_SPEED = 256;

    /** 滚轮调速行为 */
    public ScrollValueBehaviour generatedSpeed;

    public CustomEngineBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        super.addBehaviours(behaviours);
        generatedSpeed = new KineticScrollValueBehaviour(
            Component.literal("转速"), this, new EngineValueBox());
        generatedSpeed.between(-MAX_SPEED, MAX_SPEED);
        generatedSpeed.value = DEFAULT_SPEED;
        generatedSpeed.withCallback(i -> this.updateGeneratedRotation());
        behaviours.add(generatedSpeed);
    }

    @Override
    public void initialize() {
        super.initialize();
        if (!hasSource() || this.getGeneratedSpeed() > this.getTheoreticalSpeed())
            this.updateGeneratedRotation();
    }

    @Override
    public float getGeneratedSpeed() {
        if (!(getBlockState().getBlock() instanceof CustomEngineBlock))
            return 0;
        return convertToDirection(generatedSpeed.getValue(),
            getBlockState().getValue(CustomEngineBlock.FACING));
    }

    /**
     * 核心 SU 恒定逻辑。
     *
     * <p>KineticNetwork 会自动计算 {@code actualSU = baseCapacity × |speed|}，
     * 因此返回 {@code fixedSU / |speed|} 可确保最终结果恒等于 fixedSU。</p>
     *
     * @return 应力容量基数（SU / 转速），speed 为 0 时返回 0
     */
    @Override
    public float calculateAddedStressCapacity() {
        float speed = Math.abs(this.getGeneratedSpeed());
        if (speed == 0) return 0;
        float fixedSU = ((CustomEngineBlock) getBlockState().getBlock()).getStressCapacity();
        return fixedSU / speed;
    }

    /**
     * 引擎值框变换器 — 控制转速调节面板的位置和朝向。
     */
    static class EngineValueBox extends ValueBoxTransform.Sided {

        @Override
        protected Vec3 getSouthLocation() {
            return VecHelper.voxelSpace(8, 8, 12.5);
        }

        @Override
        public Vec3 getLocalOffset(LevelAccessor level, BlockPos pos, BlockState state) {
            Direction facing = state.getValue(CustomEngineBlock.FACING);
            return super.getLocalOffset(level, pos, state)
                .add(Vec3.atLowerCornerOf(facing.getNormal()).scale(-1 / 16f));
        }

        @Override
        public void rotate(LevelAccessor level, BlockPos pos, BlockState state, PoseStack ms) {
            super.rotate(level, pos, state, ms);
            Direction facing = state.getValue(CustomEngineBlock.FACING);
            if (facing.getAxis() == Axis.Y) return;
            if (getSide() != Direction.UP) return;
            TransformStack.of(ms)
                .rotateZDegrees(-AngleHelper.horizontalAngle(facing) + 180);
        }

        @Override
        protected boolean isSideActive(BlockState state, Direction direction) {
            Direction facing = state.getValue(CustomEngineBlock.FACING);
            if (facing.getAxis() != Axis.Y && direction == Direction.DOWN) return false;
            return direction.getAxis() != facing.getAxis();
        }
    }
}
