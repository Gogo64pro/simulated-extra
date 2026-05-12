package net.gogo.simulatedextra.content.chained_centered_wheel_mount;

import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.gogo.simulatedextra.Simulatedextra;

public class ChainDrivablePartialModels {
    public static final PartialModel CHAIN_TOP
            = PartialModel.of(Simulatedextra.path("models/block/chain_wheel_mount_tpp"));
    public static final PartialModel CHAIN_BOTTOM
            = PartialModel.of(Simulatedextra.path("models/block/chain_wheel_mount_bottom"));

    public static void init() {}
}
