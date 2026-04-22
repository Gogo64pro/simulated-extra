package net.gogo.simulatedextra.mixin;

import dev.ryanhcode.offroad.content.blocks.wheel_mount.WheelMountBlockEntity;
import dev.ryanhcode.offroad.content.components.TireLike;
import dev.ryanhcode.offroad.index.OffroadDataComponents;
import net.gogo.simulatedextra.content.centered_wheel_mount.CenteredWheelMountBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import static net.gogo.simulatedextra.content.centered_wheel_mount.CenteredWheelMountRenderer.VERTICAL_WHEEL_OFFSET;


@Mixin(value = WheelMountBlockEntity.class, remap = false)
public abstract class CenteredWheelMountPhysicsMixin {

    @ModifyVariable(
            method = "computeMaxExtensionToTerrain",
            at = @At(value = "STORE"),
            name = "wheelPosCenter",
            remap = false
    )
    private Vec3 redirectRaycastOrigin(Vec3 wheelPosCenter) {
        if ((Object) this instanceof CenteredWheelMountBlockEntity be) {
            ItemStack item = be.getHeldItem();
            TireLike tire = item.get(OffroadDataComponents.TIRE);
            float radius = tire != null ? tire.radius() : 0.0f;
            return Vec3.atCenterOf(be.getBlockPos()).subtract(0, radius - VERTICAL_WHEEL_OFFSET, 0);
        }
        return wheelPosCenter;
    }
    @ModifyVariable(
            method = "sable$physicsTick",
            at = @At(value = "STORE"),
            name = "localPos",
            remap = false
    )
    private Vec3 redirectForceApplicationPoint(Vec3 localPos) {
        if ((Object) this instanceof CenteredWheelMountBlockEntity be) {
            ItemStack item = be.getHeldItem();
            TireLike tire = item.get(OffroadDataComponents.TIRE);
            float radius = tire != null ? tire.radius() : 0.0f;
            return Vec3.atCenterOf(be.getBlockPos()).subtract(0, radius - VERTICAL_WHEEL_OFFSET, 0);
        }
        return localPos;
    }
}
