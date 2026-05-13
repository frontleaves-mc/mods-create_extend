package com.frontleaves.mods.create_extend.content.decoration.encasing;

import com.frontleaves.mods.create_extend.registry.ModBlockEntityTypes;
import com.simibubi.create.content.kinetics.simpleRelays.SimpleKineticBlockEntity;
import com.simibubi.create.content.kinetics.simpleRelays.encased.EncasedCogwheelBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Supplier;

/**
 * 绿宝石包裹大齿轮方块。
 *
 * <p>继承 Create 的 {@link EncasedCogwheelBlock}，覆盖 {@link #getBlockEntityType()}
 * 返回模组自定义的方块实体类型，以通过方块实体验证。</p>
 */
public class EmeraldEncasedLargeCogwheelBlock extends EncasedCogwheelBlock {
    public EmeraldEncasedLargeCogwheelBlock(Properties properties, Supplier<Block> casing) {
        super(properties, true, casing);
    }

    @Override
    public BlockEntityType<? extends SimpleKineticBlockEntity> getBlockEntityType() {
        return ModBlockEntityTypes.EMERALD_ENCASED_LARGE_COGWHEEL.get();
    }
}
