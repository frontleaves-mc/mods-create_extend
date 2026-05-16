package com.frontleaves.mods.create_extend.client;

import com.frontleaves.mods.create_extend.registry.ModBlockEntityTypes;
import com.simibubi.create.content.kinetics.base.ShaftRenderer;
import com.simibubi.create.content.kinetics.simpleRelays.encased.EncasedCogRenderer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

/**
 * 客户端方块实体渲染器注册。
 *
 * <p>直接引用 Create slim jar 中的渲染器类（ShaftRenderer、EncasedCogRenderer），
 * 通过静态工厂方法创建渲染器实例并绑定到模组自定义的方块实体类型。</p>
 */
@EventBusSubscriber(modid = "create_extend", bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModBlockEntityRenderers {

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(
            ModBlockEntityTypes.EMERALD_ENCASED_SHAFT.get(),
            ShaftRenderer::new);

        event.registerBlockEntityRenderer(
            ModBlockEntityTypes.EMERALD_ENCASED_COGWHEEL.get(),
            EncasedCogRenderer::small);

        event.registerBlockEntityRenderer(
            ModBlockEntityTypes.EMERALD_ENCASED_LARGE_COGWHEEL.get(),
            EncasedCogRenderer::large);

        event.registerBlockEntityRenderer(ModBlockEntityTypes.CYAN_ENGINE_BE.get(), ShaftRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntityTypes.AZURE_ENGINE_BE.get(), ShaftRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntityTypes.INDIGO_ENGINE_BE.get(), ShaftRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntityTypes.VIOLET_ENGINE_BE.get(), ShaftRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntityTypes.AETHER_ENGINE_BE.get(), ShaftRenderer::new);
    }
}
