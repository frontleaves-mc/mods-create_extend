package com.frontleaves.mods.create_extend.registry;

import com.frontleaves.mods.create_extend.CreateExtend;
import com.frontleaves.mods.create_extend.content.kinetics.engine.entity.CustomEngineBlockEntity;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.simpleRelays.SimpleKineticBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

/**
 * 模组方块实体类型注册中心。
 *
 * <p>为自定义包裹方块注册独立的 {@link BlockEntityType}，
 * 使其方块实体验证通过。每个类型声明其对应的有效方块列表。</p>
 */
public class ModBlockEntityTypes {
    private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES =
        DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, CreateExtend.MODID);

    @SuppressWarnings("unchecked")
    private static final DeferredHolder<BlockEntityType<?>, ?>[] SLOT = new DeferredHolder[8];

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<KineticBlockEntity>> EMERALD_ENCASED_SHAFT =
        BLOCK_ENTITY_TYPES.register("emerald_encased_shaft", () ->
            BlockEntityType.Builder.of(
                new KineticFactory(() -> SLOT[0]),
                ModBlocks.EMERALD_ENCASED_SHAFT.get()
            ).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SimpleKineticBlockEntity>> EMERALD_ENCASED_COGWHEEL =
        BLOCK_ENTITY_TYPES.register("emerald_encased_cogwheel", () ->
            BlockEntityType.Builder.of(
                new SimpleFactory(() -> SLOT[1]),
                ModBlocks.EMERALD_ENCASED_COGWHEEL.get()
            ).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SimpleKineticBlockEntity>> EMERALD_ENCASED_LARGE_COGWHEEL =
        BLOCK_ENTITY_TYPES.register("emerald_encased_large_cogwheel", () ->
            BlockEntityType.Builder.of(
                new SimpleFactory(() -> SLOT[2]),
                ModBlocks.EMERALD_ENCASED_LARGE_COGWHEEL.get()
            ).build(null));

    // ==================== 自定义动力引擎方块实体 ====================

    /** 湛源引擎方块实体 */
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CustomEngineBlockEntity>> CYAN_ENGINE_BE =
        BLOCK_ENTITY_TYPES.register("cyan_engine", () ->
            BlockEntityType.Builder.of(
                new EngineFactory(() -> SLOT[3]),
                ModBlocks.CYAN_ENGINE.get()
            ).build(null));

    /** 碧渊引擎方块实体 */
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CustomEngineBlockEntity>> AZURE_ENGINE_BE =
        BLOCK_ENTITY_TYPES.register("azure_engine", () ->
            BlockEntityType.Builder.of(
                new EngineFactory(() -> SLOT[4]),
                ModBlocks.AZURE_ENGINE.get()
            ).build(null));

    /** 墨渊引擎方块实体 */
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CustomEngineBlockEntity>> INDIGO_ENGINE_BE =
        BLOCK_ENTITY_TYPES.register("indigo_engine", () ->
            BlockEntityType.Builder.of(
                new EngineFactory(() -> SLOT[5]),
                ModBlocks.INDIGO_ENGINE.get()
            ).build(null));

    /** 紫曜引擎方块实体 */
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CustomEngineBlockEntity>> VIOLET_ENGINE_BE =
        BLOCK_ENTITY_TYPES.register("violet_engine", () ->
            BlockEntityType.Builder.of(
                new EngineFactory(() -> SLOT[6]),
                ModBlocks.VIOLET_ENGINE.get()
            ).build(null));

    /** 苍穹引擎方块实体 */
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CustomEngineBlockEntity>> AETHER_ENGINE_BE =
        BLOCK_ENTITY_TYPES.register("aether_engine", () ->
            BlockEntityType.Builder.of(
                new EngineFactory(() -> SLOT[7]),
                ModBlocks.AETHER_ENGINE.get()
            ).build(null));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITY_TYPES.register(eventBus);
        SLOT[0] = EMERALD_ENCASED_SHAFT;
        SLOT[1] = EMERALD_ENCASED_COGWHEEL;
        SLOT[2] = EMERALD_ENCASED_LARGE_COGWHEEL;
        SLOT[3] = CYAN_ENGINE_BE;
        SLOT[4] = AZURE_ENGINE_BE;
        SLOT[5] = INDIGO_ENGINE_BE;
        SLOT[6] = VIOLET_ENGINE_BE;
        SLOT[7] = AETHER_ENGINE_BE;
    }

    private record KineticFactory(
        Supplier<DeferredHolder<BlockEntityType<?>, ?>> holder)
        implements BlockEntityType.BlockEntitySupplier<KineticBlockEntity> {
        @Override
        @SuppressWarnings("unchecked")
        public KineticBlockEntity create(BlockPos pos, BlockState state) {
            return new KineticBlockEntity(((DeferredHolder<BlockEntityType<?>, BlockEntityType<KineticBlockEntity>>) holder.get()).get(), pos, state);
        }
    }

    private record SimpleFactory(
        Supplier<DeferredHolder<BlockEntityType<?>, ?>> holder)
        implements BlockEntityType.BlockEntitySupplier<SimpleKineticBlockEntity> {
        @Override
        @SuppressWarnings("unchecked")
        public SimpleKineticBlockEntity create(BlockPos pos, BlockState state) {
            return new SimpleKineticBlockEntity(((DeferredHolder<BlockEntityType<?>, BlockEntityType<SimpleKineticBlockEntity>>) holder.get()).get(), pos, state);
        }
    }

    /**
     * 自定义引擎方块实体工厂。
     *
     * <p>通过 SLOT 数组延迟解析 {@link DeferredHolder}，解决 Block↔BlockEntityType 循环依赖。</p>
     */
    private record EngineFactory(
        Supplier<DeferredHolder<BlockEntityType<?>, ?>> holder)
        implements BlockEntityType.BlockEntitySupplier<CustomEngineBlockEntity> {
        @Override
        @SuppressWarnings("unchecked")
        public CustomEngineBlockEntity create(BlockPos pos, BlockState state) {
            return new CustomEngineBlockEntity(((DeferredHolder<BlockEntityType<?>, BlockEntityType<CustomEngineBlockEntity>>) holder.get()).get(), pos, state);
        }
    }
}
