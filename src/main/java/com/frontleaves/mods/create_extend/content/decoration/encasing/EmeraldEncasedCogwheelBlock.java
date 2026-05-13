package com.frontleaves.mods.create_extend.content.decoration.encasing;

import com.frontleaves.mods.create_extend.registry.ModBlockEntityTypes;
import com.simibubi.create.content.kinetics.simpleRelays.SimpleKineticBlockEntity;
import com.simibubi.create.content.kinetics.simpleRelays.encased.EncasedCogwheelBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Supplier;

/**
 * 绿宝石包裹齿轮方块。
 *
 * <p>继承 Create 的 {@link EncasedCogwheelBlock}，覆盖 {@link #getBlockEntityType()}
 * 以返回模组自定义的 {@link BlockEntityType}，解决方块实体验证失败的问题。</p>
 */
public class EmeraldEncasedCogwheelBlock extends EncasedCogwheelBlock {
    public EmeraldEncasedCogwheelBlock(Properties properties, boolean isLarge, Supplier<Block> casing) {
        super(properties, isLarge, casing);
    }

    @Override
    public BlockEntityType<? extends SimpleKineticBlockEntity> getBlockEntityType() {
        return isLarge
            ? ModBlockEntityTypes.EMERALD_ENCASED_LARGE_COGWHEEL.get()
            : ModBlockEntityTypes.EMERALD_ENCASED_COGWHEEL.get();
    }
}
