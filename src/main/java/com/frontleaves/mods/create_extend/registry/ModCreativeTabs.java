package com.frontleaves.mods.create_extend.registry;

import com.frontleaves.mods.create_extend.CreateExtend;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;

/**
 * 模组创造模式物品栏注册。
 *
 * <p>创建模组专属的创造模式标签页，将所有模组物品和方块集中在同一标签页中展示。</p>
 */
public class ModCreativeTabs {
    private static final DeferredRegister<CreativeModeTab> REGISTER =
        DeferredRegister.create(Registries.CREATIVE_MODE_TAB, CreateExtend.MODID);

    /**
     * 锋楪扩展创造模式标签页。
     *
     * <p>标签页图标使用绿宝石机壳方块，按照物品→方块的顺序展示所有模组内容。</p>
     */
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> CREATE_EXTEND_TAB = REGISTER.register("base",
        () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.create_extend.base"))
            .icon(() -> new ItemStack(ModBlocks.EMERALD_CASING.get()))
            .displayItems((parameters, output) -> {
                // 物品
                output.accept(new ItemStack(ModItems.EMERALD_NUGGET.get()));
                output.accept(new ItemStack(ModItems.EMERALD_ALLOY.get()));
                // 方块
                output.accept(new ItemStack(ModBlocks.EMERALD_CASING.get()));
                output.accept(new ItemStack(ModBlocks.EMERALD_ENCASED_SHAFT.get()));
                output.accept(new ItemStack(ModBlocks.EMERALD_ENCASED_COGWHEEL.get()));
                output.accept(new ItemStack(ModBlocks.EMERALD_ENCASED_LARGE_COGWHEEL.get()));
            })
            .build());

    /**
     * 将创造模式标签页注册器绑定到模组事件总线。
     *
     * @param eventBus 模组事件总线
     */
    public static void register(IEventBus eventBus) {
        REGISTER.register(eventBus);
    }
}
