package com.frontleaves.mods.create_extend.registry;

import com.frontleaves.mods.create_extend.CreateExtend;
import com.frontleaves.mods.create_extend.content.decoration.encasing.EmeraldEncasedCogwheelBlock;
import com.frontleaves.mods.create_extend.content.decoration.encasing.EmeraldEncasedLargeCogwheelBlock;
import com.frontleaves.mods.create_extend.content.decoration.encasing.EmeraldEncasedShaftBlock;
import com.frontleaves.mods.create_extend.content.kinetics.engine.CustomEngineBlock;
import com.frontleaves.mods.create_extend.content.kinetics.engine.entity.CustomEngineBlockEntity;
import com.simibubi.create.content.decoration.encasing.CasingBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

/**
 * 模组方块注册中心。
 *
 * <p>集中管理所有模组内自定义方块的注册，同时为每个方块自动注册对应的 {@link BlockItem}。
 * 注册通过 NeoForge 的 {@link DeferredRegister} 机制完成。</p>
 */
public class ModBlocks {
    /** 方块延迟注册器 */
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(CreateExtend.MODID);
    /** 对应方块物品的延迟注册器（与方块共享命名空间） */
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(CreateExtend.MODID);

    /** 绿宝石机壳方块 */
    public static final DeferredBlock<Block> EMERALD_CASING = registerBlock("emerald_casing",
        () -> new CasingBlock(Block.Properties.of()
            .mapColor(MapColor.COLOR_GREEN)
            .strength(2.0f, 8.0f)
            .sound(SoundType.STONE)
            .requiresCorrectToolForDrops()));

    /** 绿宝石包裹轴方块 */
    public static final DeferredBlock<Block> EMERALD_ENCASED_SHAFT = registerBlock("emerald_encased_shaft",
        () -> new EmeraldEncasedShaftBlock(Block.Properties.of()
            .mapColor(MapColor.PODZOL)
            .strength(3.0f, 6.0f)
            .sound(SoundType.STONE)
            .requiresCorrectToolForDrops(),
            () -> EMERALD_CASING.get()));

    /** 绿宝石包裹齿轮方块 */
    public static final DeferredBlock<Block> EMERALD_ENCASED_COGWHEEL = registerBlock("emerald_encased_cogwheel",
        () -> new EmeraldEncasedCogwheelBlock(Block.Properties.of()
            .mapColor(MapColor.PODZOL)
            .strength(3.0f, 6.0f)
            .sound(SoundType.STONE)
            .requiresCorrectToolForDrops(),
            false,
            () -> EMERALD_CASING.get()));

    /** 绿宝石包裹大齿轮方块 */
    public static final DeferredBlock<Block> EMERALD_ENCASED_LARGE_COGWHEEL = registerBlock("emerald_encased_large_cogwheel",
        () -> new EmeraldEncasedLargeCogwheelBlock(Block.Properties.of()
            .mapColor(MapColor.PODZOL)
            .strength(3.0f, 6.0f)
            .sound(SoundType.STONE)
            .requiresCorrectToolForDrops(),
            () -> EMERALD_CASING.get()));

    // ==================== 自定义动力引擎方块 ====================

    /** 湛源引擎 — 147,456 SU */
    public static final DeferredBlock<Block> CYAN_ENGINE = registerEngine("cyan_engine", 147456f,
        () -> ModBlockEntityTypes.CYAN_ENGINE_BE.get());

    /** 碧渊引擎 — 1,327,104 SU */
    public static final DeferredBlock<Block> AZURE_ENGINE = registerEngine("azure_engine", 1327104f,
        () -> ModBlockEntityTypes.AZURE_ENGINE_BE.get());

    /** 墨渊引擎 — 1,916,928 SU */
    public static final DeferredBlock<Block> INDIGO_ENGINE = registerEngine("indigo_engine", 1916928f,
        () -> ModBlockEntityTypes.INDIGO_ENGINE_BE.get());

    /** 紫曜引擎 — 2,649,216 SU */
    public static final DeferredBlock<Block> VIOLET_ENGINE = registerEngine("violet_engine", 2649216f,
        () -> ModBlockEntityTypes.VIOLET_ENGINE_BE.get());

    /** 苍穹引擎 — 4,194,304 SU */
    public static final DeferredBlock<Block> AETHER_ENGINE = registerEngine("aether_engine", 4194304f,
        () -> ModBlockEntityTypes.AETHER_ENGINE_BE.get());

    /**
     * 注册一个方块并自动注册其对应的 {@link BlockItem}。
     *
     * @param name          方块注册名（同时作为物品注册名）
     * @param blockSupplier 方块实例供应器
     * @return 延迟注册的方块引用
     */
    private static DeferredBlock<Block> registerBlock(String name, Supplier<Block> blockSupplier) {
        DeferredBlock<Block> deferredBlock = BLOCKS.register(name, blockSupplier);
        ITEMS.register(name, () -> new BlockItem(deferredBlock.get(), new Item.Properties()));
        return deferredBlock;
    }

    /**
     * 注册一个自定义动力引擎方块及其对应的 {@link BlockItem}。
     *
     * @param name                   方块注册名
     * @param stressCapacity         固定 SU 值
     * @param blockEntityTypeSupplier 对应方块实体类型的延迟供应器
     * @return 延迟注册的方块引用
     */
    private static DeferredBlock<Block> registerEngine(
        String name,
        float stressCapacity,
        Supplier<BlockEntityType<? extends CustomEngineBlockEntity>> blockEntityTypeSupplier
    ) {
        DeferredBlock<Block> deferredBlock = BLOCKS.register(name, () -> new CustomEngineBlock(
            Block.Properties.of()
                .mapColor(MapColor.COLOR_GREEN)
                .strength(3.0f, 6.0f)
                .sound(SoundType.STONE)
                .requiresCorrectToolForDrops(),
            stressCapacity,
            blockEntityTypeSupplier
        ));
        ITEMS.register(name, () -> new BlockItem(deferredBlock.get(), new Item.Properties()));
        return deferredBlock;
    }

    /**
     * 将方块和物品的延迟注册器绑定到模组事件总线。
     *
     * @param eventBus 模组事件总线
     */
    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
        ITEMS.register(eventBus);
    }
}
