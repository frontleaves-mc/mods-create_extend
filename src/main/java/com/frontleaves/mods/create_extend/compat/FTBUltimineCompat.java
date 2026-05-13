package com.frontleaves.mods.create_extend.compat;

import com.frontleaves.mods.create_extend.handler.CreateWrenchBlockBreakHandler;
import dev.ftb.mods.ftbultimine.BlockBreakingRegistry;

/**
 * FTB Ultimine 兼容层。
 *
 * <p>将 Create 扳手的方块破坏处理器注册到 FTB Ultimine 的多方块挖掘系统中，
 * 使玩家在使用 FTB Ultimine 连锁挖掘时手持扳手可以正确拆卸 Create 方块。</p>
 */
public class FTBUltimineCompat {
    /**
     * 注册 FTB Ultimine 的方块破坏处理器。
     */
    public static void register() {
        BlockBreakingRegistry.INSTANCE.registerHandler(
            CreateWrenchBlockBreakHandler.INSTANCE
        );
    }
}
