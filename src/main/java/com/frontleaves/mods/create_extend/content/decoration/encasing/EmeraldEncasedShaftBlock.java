package com.frontleaves.mods.create_extend.content.decoration.encasing;

import com.frontleaves.mods.create_extend.registry.ModBlockEntityTypes;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.simpleRelays.encased.EncasedShaftBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Supplier;

/**
 * 绿宝石包裹轴方块。
 *
 * <p>继承 Create 的 {@link EncasedShaftBlock}，覆盖 {@link #getBlockEntityType()}
 * 以返回模组自定义的 {@link BlockEntityType}，解决方块实体验证失败的问题。</p>
 */
public class EmeraldEncasedShaftBlock extends EncasedShaftBlock {
    public EmeraldEncasedShaftBlock(Properties properties, Supplier<Block> casing) {
        super(properties, casing);
    }

    @Override
    public BlockEntityType<? extends KineticBlockEntity> getBlockEntityType() {
        return ModBlockEntityTypes.EMERALD_ENCASED_SHAFT.get();
    }
}
