package net.gogo.simulatedextra.content.motorcycle_wheel_mount;

import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.minecraft.resources.ResourceLocation;
import static net.gogo.simulatedextra.Simulatedextra.ID;

public class MotoPartialModels {

    public static final PartialModel MOTO_FORK_UPPER    = PartialModel.of(ResourceLocation.fromNamespaceAndPath(ID, "models/block/moto/moto_fork_upper"));
    public static final PartialModel MOTO_FORK_LOWER    = PartialModel.of(ResourceLocation.fromNamespaceAndPath(ID, "models/block/moto/moto_fork_lower"));
    public static final PartialModel MOTO_FORK_MOUNT    = PartialModel.of(ResourceLocation.fromNamespaceAndPath(ID, "models/block/moto/moto_fork_mount"));
    public static final PartialModel MOTO_SPRING_TOP    = PartialModel.of(ResourceLocation.fromNamespaceAndPath(ID, "models/block/moto/moto_spring_top"));
    public static final PartialModel MOTO_SPRING_MID    = PartialModel.of(ResourceLocation.fromNamespaceAndPath(ID, "models/block/moto/moto_spring_mid"));
    public static final PartialModel MOTO_SPRING_BOTTOM = PartialModel.of(ResourceLocation.fromNamespaceAndPath(ID, "models/block/moto/moto_spring_bottom"));

    public static void init() {}
}