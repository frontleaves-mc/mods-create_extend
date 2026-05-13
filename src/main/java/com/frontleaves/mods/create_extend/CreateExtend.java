package com.frontleaves.mods.create_extend;

import com.frontleaves.mods.create_extend.compat.FTBUltimineCompat;
import com.frontleaves.mods.create_extend.compat.GogglesCompat;
import com.frontleaves.mods.create_extend.content.decoration.encasing.EmeraldEncasing;
import com.frontleaves.mods.create_extend.registry.ModBlocks;
import com.frontleaves.mods.create_extend.registry.ModItems;
import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import org.slf4j.Logger;

/**
 * 机械动力：锋楪扩展的主入口类。
 *
 * <p>负责模组的初始化流程，包括方块/物品注册监听、通用设置事件调度。
 * 具体业务逻辑委托至各功能模块：</p>
 * <ul>
 *   <li>{@link com.frontleaves.mods.create_extend.registry.ModBlocks} — 方块注册</li>
 *   <li>{@link com.frontleaves.mods.create_extend.registry.ModItems} — 物品注册</li>
 *   <li>{@link com.frontleaves.mods.create_extend.content.decoration.encasing.EmeraldEncasing} — 绿宝石包裹注册</li>
 *   <li>{@link com.frontleaves.mods.create_extend.compat.GogglesCompat} — 护目镜兼容</li>
 *   <li>{@link com.frontleaves.mods.create_extend.compat.FTBUltimineCompat} — FTB Ultimine 兼容</li>
 * </ul>
 */
@Mod(CreateExtend.MODID)
public class CreateExtend {
    /** 模组标识符 */
    public static final String MODID = "create_extend";
    private static final Logger LOGGER = LogUtils.getLogger();

    /**
     * 模组构造函数，由 NeoForge 在模组加载时自动调用。
     *
     * @param modEventBus 模组事件总线，用于注册 DeferredRegister 和监听事件
     * @param modContainer 模组容器
     */
    public CreateExtend(IEventBus modEventBus, ModContainer modContainer) {
        ModBlocks.register(modEventBus);
        ModItems.register(modEventBus);
        modEventBus.addListener(this::onCommonSetup);
    }

    /**
     * 通用设置回调，在模组加载完成后执行。
     *
     * <p>按顺序执行以下注册：</p>
     * <ol>
     *   <li>绿宝石包裹变体注册（轴、齿轮、大齿轮）</li>
     *   <li>Curios 护目镜佩戴谓词注册（若已安装）</li>
     *   <li>Accessories 护目镜佩戴谓词注册（若已安装）</li>
     *   <li>FTB Ultimine 扳手兼容处理器注册（若已安装）</li>
     * </ol>
     *
     * @param event 通用设置事件
     */
    private void onCommonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            EmeraldEncasing.register();

            if (ModList.get().isLoaded("curios")) {
                GogglesCompat.registerCurios();
                LOGGER.info("Registered Curios wearing predicate for aviator goggles");
            }

            if (ModList.get().isLoaded("accessories")) {
                GogglesCompat.registerAccessories();
                LOGGER.info("Registered Accessories wearing predicate for create goggles");
            }

            if (ModList.get().isLoaded("ftbultimine")) {
                FTBUltimineCompat.register();
                LOGGER.info("Registered FTB Ultimine compatibility handler for Create wrench");
            }
        });
    }
}
