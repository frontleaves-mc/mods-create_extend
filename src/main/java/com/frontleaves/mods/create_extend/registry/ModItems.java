package com.frontleaves.mods.create_extend.registry;

import com.frontleaves.mods.create_extend.CreateExtend;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

/**
 * 模组物品注册中心。
 *
 * <p>管理所有非方块对应的独立物品注册。方块对应的物品由
 * {@link com.frontleaves.mods.create_extend.registry.ModBlocks} 自动注册。</p>
 */
public class ModItems {
    /** 物品延迟注册器 */
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(CreateExtend.MODID);

    /** 绿宝石粒 */
    public static final Supplier<Item> EMERALD_NUGGET = ITEMS.register("emerald_nugget",
        () -> new Item(new Item.Properties()));

    /** 绿宝石合金 */
    public static final Supplier<Item> EMERALD_ALLOY = ITEMS.register("emerald_alloy",
        () -> new Item(new Item.Properties()));

    /**
     * 将物品延迟注册器绑定到模组事件总线。
     *
     * @param eventBus 模组事件总线
     */
    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
