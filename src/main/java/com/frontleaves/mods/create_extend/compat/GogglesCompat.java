package com.frontleaves.mods.create_extend.compat;

import com.simibubi.create.content.equipment.goggles.GogglesItem;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;

import java.util.function.Predicate;

/**
 * 护目镜饰品兼容层。
 *
 * <p>为 Curios 和 Accessories 两套饰品系统分别注册 Create 工程师护目镜的佩戴检测谓词，
 * 使佩戴在饰品栏中的护目镜物品也能正确触发 Create 的动力学信息显示等功能。</p>
 *
 * <p>支持以下物品：</p>
 * <ul>
 *   <li>{@code aeronautics:aviators_goggles} — 飞行员护目镜（Curios + Accessories）</li>
 *   <li>{@code create:goggles} — Create 工程师护目镜（Accessories）</li>
 * </ul>
 */
public class GogglesCompat {
    /** 飞行员护目镜的资源定位符 */
    private static final ResourceLocation AVIATORS_GOGGLES = ResourceLocation.parse("aeronautics:aviators_goggles");
    /** Create 工程师护目镜的资源定位符 */
    private static final ResourceLocation CREATE_GOGGLES = ResourceLocation.parse("create:goggles");

    /**
     * 注册 Curios 饰品系统的护目镜佩戴谓词。
     *
     * <p>遍历 Curios 饰品栏位，检测玩家是否装备了飞行员护目镜。</p>
     */
    public static void registerCurios() {
        Predicate<Holder<Item>> gogglesMatcher = holder ->
            holder.unwrapKey().map(key -> key.location().equals(AVIATORS_GOGGLES)).orElse(false);
        GogglesItem.addIsWearingPredicate(player -> curiosPredicate(player, gogglesMatcher));
    }

    /**
     * 检查 Curios 饰品栏中是否存在匹配的护目镜物品。
     *
     * @param player  玩家实体
     * @param matcher 物品匹配谓词
     * @return 是否装备了匹配的护目镜
     */
    private static boolean curiosPredicate(Player player, Predicate<Holder<Item>> matcher) {
        return top.theillusivec4.curios.api.CuriosApi.getCuriosInventory(player).map(handler -> {
            var equipped = handler.getEquippedCurios();
            for (int i = 0; i < equipped.getSlots(); i++) {
                if (equipped.getStackInSlot(i).is(matcher)) {
                    return true;
                }
            }
            return false;
        }).orElse(false);
    }

    /**
     * 注册 Accessories 饰品系统的护目镜佩戴谓词。
     *
     * <p>分别为飞行员护目镜和 Create 工程师护目镜注册佩戴检测，
     * 通过 Accessories API 检查玩家是否装备了对应物品。</p>
     */
    public static void registerAccessories() {
        Predicate<Holder<Item>> gogglesMatcher = holder ->
            holder.unwrapKey().map(key -> key.location().equals(AVIATORS_GOGGLES)).orElse(false);
        GogglesItem.addIsWearingPredicate(player ->
            io.wispforest.accessories.api.AccessoriesCapability.get(player) != null
                && io.wispforest.accessories.api.AccessoriesCapability.get(player).isEquipped(
                    stack -> stack.is(gogglesMatcher))
        );

        Predicate<Holder<Item>> createGogglesMatcher = holder ->
            holder.unwrapKey().map(key -> key.location().equals(CREATE_GOGGLES)).orElse(false);
        GogglesItem.addIsWearingPredicate(player ->
            io.wispforest.accessories.api.AccessoriesCapability.get(player) != null
                && io.wispforest.accessories.api.AccessoriesCapability.get(player).isEquipped(
                    stack -> stack.is(createGogglesMatcher))
        );
    }
}
