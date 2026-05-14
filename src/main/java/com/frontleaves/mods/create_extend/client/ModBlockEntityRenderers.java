package com.frontleaves.mods.create_extend.client;

import com.frontleaves.mods.create_extend.registry.ModBlockEntityTypes;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

import java.lang.reflect.Constructor;

/**
 * 客户端方块实体渲染器注册。
 *
 * <p>通过反射创建 Create 的渲染器实例并绑定到模组自定义的方块实体类型，
 * 避免编译期对 Ponder 库（VirtualBlockEntity）的直接依赖。
 * 运行时 Create 完整 jar 已通过 neoforge.mods.toml 加载，
 * 故反射可正常获取 ShaftRenderer、EncasedCogRenderer 等类。</p>
 */
@EventBusSubscriber(modid = "create_extend", bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModBlockEntityRenderers {

    @SubscribeEvent
    @SuppressWarnings("unchecked")
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(
            ModBlockEntityTypes.EMERALD_ENCASED_SHAFT.get(),
            context -> createShaftRenderer(context));

        event.registerBlockEntityRenderer(
            ModBlockEntityTypes.EMERALD_ENCASED_COGWHEEL.get(),
            context -> createCogRenderer(false, context));

        event.registerBlockEntityRenderer(
            ModBlockEntityTypes.EMERALD_ENCASED_LARGE_COGWHEEL.get(),
            context -> createCogRenderer(true, context));
    }

    /**
     * 通过反射创建 ShaftRenderer 实例。
     *
     * <p>构造器签名：{@code ShaftRenderer(BlockEntityRendererProvider.Context)}</p>
     *
     * @param context 渲染器上下文
     * @param <T>     方块实体类型
     * @return ShaftRenderer 实例
     */
    @SuppressWarnings("unchecked")
    private static <T extends BlockEntity> BlockEntityRenderer<T> createShaftRenderer(
            BlockEntityRendererProvider.Context context) {
        try {
            Class<?> clazz = Class.forName(
                "com.simibubi.create.content.kinetics.base.ShaftRenderer");
            Constructor<?> ctor = clazz.getConstructor(BlockEntityRendererProvider.Context.class);
            return (BlockEntityRenderer<T>) ctor.newInstance(context);
        } catch (Exception e) {
            throw new RuntimeException("无法创建 ShaftRenderer 渲染器实例", e);
        }
    }

    /**
     * 通过反射创建 EncasedCogRenderer 实例，自动尝试两种参数顺序。
     *
     * <p>构造器签名可能是 {@code (boolean, Context)} 或 {@code (Context, boolean)}，
     * 反射会自动回退尝试。</p>
     *
     * @param isLarge 是否为大齿轮
     * @param context 渲染器上下文
     * @param <T>     方块实体类型
     * @return EncasedCogRenderer 实例
     */
    @SuppressWarnings("unchecked")
    private static <T extends BlockEntity> BlockEntityRenderer<T> createCogRenderer(
            boolean isLarge, BlockEntityRendererProvider.Context context) {
        final String className = "com.simibubi.create.content.kinetics.simpleRelays.encased.EncasedCogRenderer";
        try {
            // (boolean, Context) 参数顺序
            Class<?> clazz = Class.forName(className);
            Constructor<?> ctor = clazz.getConstructor(boolean.class, BlockEntityRendererProvider.Context.class);
            return (BlockEntityRenderer<T>) ctor.newInstance(isLarge, context);
        } catch (NoSuchMethodException e) {
            // 回退到 (Context, boolean) 参数顺序
            try {
                Class<?> clazz = Class.forName(className);
                Constructor<?> ctor = clazz.getConstructor(BlockEntityRendererProvider.Context.class, boolean.class);
                return (BlockEntityRenderer<T>) ctor.newInstance(context, isLarge);
            } catch (Exception ex) {
                throw new RuntimeException("无法创建 EncasedCogRenderer 渲染器实例", ex);
            }
        } catch (Exception e) {
            throw new RuntimeException("无法创建 EncasedCogRenderer 渲染器实例", e);
        }
    }
}
