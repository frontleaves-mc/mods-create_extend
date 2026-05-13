package com.frontleaves.mods.create_extend.content.decoration.encasing;

import com.frontleaves.mods.create_extend.registry.ModBlocks;
import com.simibubi.create.content.decoration.encasing.EncasingRegistry;
import com.simibubi.create.content.kinetics.simpleRelays.CogWheelBlock;
import com.simibubi.create.content.kinetics.simpleRelays.ShaftBlock;
import com.simibubi.create.content.kinetics.simpleRelays.encased.EncasedCogwheelBlock;
import com.simibubi.create.content.kinetics.simpleRelays.encased.EncasedShaftBlock;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;

/**
 * 绿宝石包裹变体注册。
 *
 * <p>向 Create 的 {@link EncasingRegistry} 注册绿宝石机壳对应的包裹变体，
 * 使玩家可以通过右键使用绿宝石机壳包裹原版轴、齿轮和大齿轮。</p>
 *
 * <p>必须在方块注册完成后（{@code FMLCommonSetupEvent} 阶段）调用，
 * 因为需要从全局注册表中查找 Create 原版方块实例。</p>
 */
public class EmeraldEncasing {
    /**
     * 注册所有绿宝石包裹变体。
     *
     * <p>从 NeoForge 全局注册表中查找 Create 的轴、齿轮和大齿轮方块，
     * 然后将它们分别映射到本模组的绿宝石包裹版本。</p>
     */
    public static void register() {
        ShaftBlock shaft = (ShaftBlock) BuiltInRegistries.BLOCK.get(ResourceLocation.parse("create:shaft"));
        CogWheelBlock cogwheel = (CogWheelBlock) BuiltInRegistries.BLOCK.get(ResourceLocation.parse("create:cogwheel"));
        CogWheelBlock largeCogwheel = (CogWheelBlock) BuiltInRegistries.BLOCK.get(ResourceLocation.parse("create:large_cogwheel"));
        EncasingRegistry.addVariant(shaft, (EncasedShaftBlock) ModBlocks.EMERALD_ENCASED_SHAFT.get());
        EncasingRegistry.addVariant(cogwheel, (EncasedCogwheelBlock) ModBlocks.EMERALD_ENCASED_COGWHEEL.get());
        EncasingRegistry.addVariant(largeCogwheel, (EncasedCogwheelBlock) ModBlocks.EMERALD_ENCASED_LARGE_COGWHEEL.get());
    }
}
