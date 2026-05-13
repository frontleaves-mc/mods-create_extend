package com.frontleaves.mods.create_extend.client;

import java.lang.reflect.Method;
import java.util.function.Function;

import com.frontleaves.mods.create_extend.registry.ModBlocks;
import com.simibubi.create.CreateClient;
import com.simibubi.create.content.decoration.encasing.CasingConnectivity;
import com.simibubi.create.content.decoration.encasing.EncasedCTBehaviour;
import com.simibubi.create.content.kinetics.simpleRelays.encased.EncasedShaftBlock;
import com.simibubi.create.foundation.block.connected.AllCTTypes;
import com.simibubi.create.foundation.block.connected.CTModel;
import com.simibubi.create.foundation.block.connected.CTSpriteShiftEntry;
import com.simibubi.create.foundation.block.connected.CTSpriteShifter;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

/**
 * 绿宝石机壳 CTM 连接纹理注册。
 *
 * <p>为绿宝石机壳及其包裹方块注册 Create 的 CTM（Connected Texture Model）连接纹理行为，
 * 使相邻同类型机壳方块之间显示无缝连接的纹理效果。</p>
 *
 * <p>通过 {@link FMLClientSetupEvent} 在客户端初始化阶段注册，
 * 使用 {@code enqueueWork} 确保在主线程执行注册操作。</p>
 */
@EventBusSubscriber(modid = "create_extend", bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModCTMRegistration {

    @SuppressWarnings("unchecked")
    private static void registerCTModel(ResourceLocation location,
            Function<BakedModel, ? extends BakedModel> factory) {
        try {
            Object customModels = CreateClient.MODEL_SWAPPER.getCustomBlockModels();
            Method registerMethod = customModels.getClass()
                .getDeclaredMethod("register", ResourceLocation.class, Function.class);
            registerMethod.invoke(customModels, location, factory);
        } catch (Exception e) {
            throw new RuntimeException("无法注册 CTM 模型: " + location, e);
        }
    }

    /**
     * 注册绿宝石机壳相关方块的 CTM 连接纹理。
     *
     * <p>为以下 4 个方块注册 CTM 行为：</p>
     * <ul>
     *   <li>emerald_casing — 绿宝石机壳（所有面连接）</li>
     *   <li>emerald_encased_shaft — 绿宝石包裹轴（轴方向面不连接）</li>
     *   <li>emerald_encased_cogwheel — 绿宝石包裹齿轮（所有面连接）</li>
     *   <li>emerald_encased_large_cogwheel — 绿宝石包裹大齿轮（所有面连接）</li>
     * </ul>
     *
     * @param event 客户端初始化事件
     */
    @SubscribeEvent
    public static void registerCTM(final FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            CTSpriteShiftEntry emeraldCasingShift = CTSpriteShifter.getCT(
                AllCTTypes.OMNIDIRECTIONAL,
                ResourceLocation.fromNamespaceAndPath("create_extend", "block/emerald_casing"),
                ResourceLocation.fromNamespaceAndPath("create_extend", "block/emerald_casing_connected")
            );

            registerCTModel(ResourceLocation.parse("create_extend:emerald_casing"),
                model -> new CTModel(model, new EncasedCTBehaviour(emeraldCasingShift)));
            CreateClient.CASING_CONNECTIVITY.makeCasing(
                ModBlocks.EMERALD_CASING.get(), emeraldCasingShift);

            registerCTModel(ResourceLocation.parse("create_extend:emerald_encased_shaft"),
                model -> new CTModel(model, new EncasedCTBehaviour(emeraldCasingShift)));
            CreateClient.CASING_CONNECTIVITY.make(
                ModBlocks.EMERALD_ENCASED_SHAFT.get(),
                emeraldCasingShift,
                (state, face) -> face.getAxis() != state.getValue(EncasedShaftBlock.AXIS));

            registerCTModel(ResourceLocation.parse("create_extend:emerald_encased_cogwheel"),
                model -> new CTModel(model, new EncasedCTBehaviour(emeraldCasingShift)));
            CreateClient.CASING_CONNECTIVITY.makeCasing(
                ModBlocks.EMERALD_ENCASED_COGWHEEL.get(), emeraldCasingShift);

            registerCTModel(ResourceLocation.parse("create_extend:emerald_encased_large_cogwheel"),
                model -> new CTModel(model, new EncasedCTBehaviour(emeraldCasingShift)));
            CreateClient.CASING_CONNECTIVITY.makeCasing(
                ModBlocks.EMERALD_ENCASED_LARGE_COGWHEEL.get(), emeraldCasingShift);
        });
    }
}
