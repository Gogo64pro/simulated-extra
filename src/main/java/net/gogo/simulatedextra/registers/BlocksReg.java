package net.gogo.simulatedextra.registers;

import com.simibubi.create.AllTags.AllBlockTags;
import com.simibubi.create.content.redstone.link.RedstoneLinkGenerator;
import com.simibubi.create.foundation.data.SharedProperties;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.gogo.simulatedextra.Simulatedextra;
import net.gogo.simulatedextra.content.centered_wheel_mount.CenteredWheelMountBlock;
import net.gogo.simulatedextra.content.linking_redstone_link.LinkingRedstoneLinkBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.MapColor;

import static com.simibubi.create.foundation.data.ModelGen.customItemModel;
import static com.simibubi.create.foundation.data.TagGen.axeOrPickaxe;

public class BlocksReg {
    private BlocksReg() {
    }

    public static final BlockEntry<CenteredWheelMountBlock> CENTERED_WHEEL_MOUNT =
            Simulatedextra.REGISTRATE.block("centered_wheel_mount", CenteredWheelMountBlock::new)
                    .properties(properties -> properties
                            .mapColor(MapColor.COLOR_GRAY)
                            .noOcclusion()
                            .isRedstoneConductor((state, level, pos) -> false)
                            .strength(3.5f, 3.5f)
                            .sound(SoundType.STONE)
                            .requiresCorrectToolForDrops()
                    )
                    .simpleItem()
                    .register();

    public static final BlockEntry<LinkingRedstoneLinkBlock> LINKING_REDSTONE_LINK =
            Simulatedextra.REGISTRATE.block("linking_redstone_link", LinkingRedstoneLinkBlock::new)
                    .initialProperties(SharedProperties::wooden)
                    .properties(properties -> properties
                            .mapColor(MapColor.TERRACOTTA_BROWN)
                            .forceSolidOn()
                    )
                    .transform(axeOrPickaxe())
                    .tag(AllBlockTags.BRITTLE.tag, AllBlockTags.SAFE_NBT.tag)
                    .blockstate(new RedstoneLinkGenerator()::generate)
                    .item()
                    .transform(customItemModel("_", "transmitter"))
                    .register();

    public static void register() {
        // Static initialization registers entries with Registrate.
    }

}
