package net.gogo.simulatedextra.registers;

import com.tterrag.registrate.util.entry.BlockEntityEntry;
import net.gogo.simulatedextra.Simulatedextra;
import net.gogo.simulatedextra.content.centered_wheel_mount.CenteredWheelMountBlockEntity;
import net.gogo.simulatedextra.content.centered_wheel_mount.CenteredWheelMountRenderer;
import net.gogo.simulatedextra.content.linking_redstone_link.IdLinkBlockEntityRenderer;
import net.gogo.simulatedextra.content.linking_redstone_link.LinkingRedstoneLinkBlockEntity;

public class BlockEntityTypes {
    private BlockEntityTypes() {
    }

    public static final BlockEntityEntry<CenteredWheelMountBlockEntity> CENTERED_WHEEL_MOUNT =
            Simulatedextra.REGISTRATE.blockEntity("centered_wheel_mount", CenteredWheelMountBlockEntity::new)
                    .validBlocks(BlocksReg.CENTERED_WHEEL_MOUNT, BlocksReg.CHAIN_DRIVABLE_WHEEL_MOUNT)
                    .register();

    //public static final BlockEntityEntry<CenteredWheelMountBlockEntity> CHAIN_DRIVABLE_WHEEL_MOUNT =
    //        Simulatedextra.REGISTRATE.blockEntity("chain_drivable_wheel_mount", CenteredWheelMountBlockEntity::new)
    //                .validBlocks(BlocksReg.CHAIN_DRIVABLE_WHEEL_MOUNT)
    //                .renderer(() -> CenteredWheelMountRenderer::new)
    //                .register();

    public static final BlockEntityEntry<LinkingRedstoneLinkBlockEntity> LINKING_REDSTONE_LINK =
            Simulatedextra.REGISTRATE.blockEntity("linking_redstone_link", LinkingRedstoneLinkBlockEntity::new)
                    .validBlocks(BlocksReg.LINKING_REDSTONE_LINK)
                    .renderer(() -> IdLinkBlockEntityRenderer::new)
                    .register();

    public static void register() {

    }

}
