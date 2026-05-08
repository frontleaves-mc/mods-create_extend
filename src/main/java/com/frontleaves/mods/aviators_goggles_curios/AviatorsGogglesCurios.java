package com.frontleaves.mods.aviators_goggles_curios;

import com.mojang.logging.LogUtils;
import com.simibubi.create.content.equipment.goggles.GogglesItem;
import io.wispforest.accessories.api.AccessoriesCapability;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import org.slf4j.Logger;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.function.Predicate;

@Mod(AviatorsGogglesCurios.MODID)
public class AviatorsGogglesCurios {
    public static final String MODID = "aviators_goggles_curios";
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final ResourceLocation AVIATORS_GOGGLES = ResourceLocation.parse("aeronautics:aviators_goggles");
    private static final ResourceLocation CREATE_GOGGLES = ResourceLocation.parse("create:goggles");

    public AviatorsGogglesCurios(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::onCommonSetup);
    }

    private void onCommonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            if (ModList.get().isLoaded("curios")) {
                Predicate<Holder<Item>> gogglesMatcher = holder ->
                    holder.unwrapKey().map(key -> key.location().equals(AVIATORS_GOGGLES)).orElse(false);
                GogglesItem.addIsWearingPredicate(player ->
                    CuriosApi.getCuriosInventory(player).map(handler -> {
                        var equipped = handler.getEquippedCurios();
                        for (int i = 0; i < equipped.getSlots(); i++) {
                            if (equipped.getStackInSlot(i).is(gogglesMatcher)) {
                                return true;
                            }
                        }
                        return false;
                    }).orElse(false)
                );
                LOGGER.info("Registered Curios wearing predicate for aviator goggles");
            }

            if (ModList.get().isLoaded("accessories")) {
                Predicate<Holder<Item>> gogglesMatcher = holder ->
                    holder.unwrapKey().map(key -> key.location().equals(AVIATORS_GOGGLES)).orElse(false);
                GogglesItem.addIsWearingPredicate(player ->
                    AccessoriesCapability.get(player) != null
                        && AccessoriesCapability.get(player).isEquipped(
                            stack -> stack.is(gogglesMatcher))
                );
                LOGGER.info("Registered Accessories wearing predicate for aviator goggles");

                Predicate<Holder<Item>> createGogglesMatcher = holder ->
                    holder.unwrapKey().map(key -> key.location().equals(CREATE_GOGGLES)).orElse(false);
                GogglesItem.addIsWearingPredicate(player ->
                    AccessoriesCapability.get(player) != null
                        && AccessoriesCapability.get(player).isEquipped(
                            stack -> stack.is(createGogglesMatcher))
                );
                LOGGER.info("Registered Accessories wearing predicate for create goggles");
            }
        });
    }
}
