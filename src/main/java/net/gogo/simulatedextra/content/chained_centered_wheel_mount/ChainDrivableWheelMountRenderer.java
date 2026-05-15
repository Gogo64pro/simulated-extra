package net.gogo.simulatedextra.content.chained_centered_wheel_mount;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.foundation.blockEntity.behaviour.filtering.FilteringRenderer;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import dev.engine_room.flywheel.lib.transform.TransformStack;
import dev.ryanhcode.offroad.content.blocks.wheel_mount.WheelMountBlockEntity;
import dev.ryanhcode.offroad.content.components.TireLike;
import dev.ryanhcode.offroad.index.OffroadDataComponents;
import dev.ryanhcode.offroad.index.OffroadPartialModels;
import net.createmod.catnip.math.AngleHelper;
import net.createmod.catnip.render.CachedBuffers;
import net.createmod.catnip.render.SuperByteBuffer;
import net.gogo.simulatedextra.content.centered_wheel_mount.CenteredWheelMountBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector2d;

public class ChainDrivableWheelMountRenderer extends KineticBlockEntityRenderer<ChainDrivableWheelMountBlockEntity> {

    private static final double HORIZONTAL_WHEEL_POSITION = 0.0 / 16.0;
    private static final double WHEEL_PIVOT_OFFSET_HOR = 10.0 / 16.0;
    private static final double SPRING_WHEEL_PIVOT_OFFSET_HOR = 12.0 / 16.0;
    private static final double SPRING_WHEEL_PIVOT_OFFSET_VER = -2.0 / 16.0;

    public ChainDrivableWheelMountRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected void renderSafe(ChainDrivableWheelMountBlockEntity be, float partialTicks, PoseStack ms,
                              MultiBufferSource buffer, int light, int overlay) {

        final BlockState state = this.getRenderedBlockState(be);
        final RenderType type = this.getRenderType(be, state);
        final VertexConsumer vb = buffer.getBuffer(type);

        // --- Stationary top ---
        CachedBuffers.partial(ChainDrivablePartialModels.CHAIN_TOP, be.getBlockState())
                .light(light)
                .renderInto(ms, vb);

        // --- Spinning bottom: rotates with steering yaw ---
        CachedBuffers.partial(ChainDrivablePartialModels.CHAIN_BOTTOM, be.getBlockState())
                .light(light)
                .rotateCentered((float) be.getLerpedYaw(partialTicks), Axis.YP)
                .renderInto(ms, vb);

        FilteringRenderer.renderOnBlockEntity(be, partialTicks, ms, buffer, light, overlay);

        final VertexConsumer cutoutVb = buffer.getBuffer(RenderType.cutoutMipped());

        final Direction direction = be.getBlockState()
                .getValue(BlockStateProperties.HORIZONTAL_FACING)
                .getOpposite();
        final BlockState blockState = be.getBlockState();

        final SuperByteBuffer teleOuter    = CachedBuffers.partial(OffroadPartialModels.TELE_OUTER,    blockState);
        final SuperByteBuffer teleInner    = CachedBuffers.partial(OffroadPartialModels.TELE_INNER,    blockState);
        final SuperByteBuffer teleMount    = CachedBuffers.partial(OffroadPartialModels.TELE_MOUNT,    blockState);
        final SuperByteBuffer springTop    = CachedBuffers.partial(OffroadPartialModels.SPRING_UPPER,  blockState);
        final SuperByteBuffer springBottom = CachedBuffers.partial(OffroadPartialModels.SPRING_LOWER,  blockState);
        final SuperByteBuffer springMiddle = CachedBuffers.partial(OffroadPartialModels.SPRING_MIDDLE, blockState);

        // Wheel is centered horizontally (0,0) — only moves vertically
        final double horizontalWheelPosition = 0.0;
        final double verticalWheelPosition   = -(be.getLerpedExtension(partialTicks) - be.getOffset() / 16.0);

        // Tele arm pivot: block center, offset up by offset/16
        final double teleMountHor = 0.0 / 16.0;
        final double teleMountVer = -6.0 / 16.0 + be.getOffset() / 16.0;

        // Spring pivot: fixed inside block
        final double springMountHor = 7.0 / 16.0;
        final double springMountVer = 7.0 / 16.0;

        final double wheelPivotOffsetHor       = 10.0 / 16.0;
        final double springWheelPivotOffsetHor = 12.0 / 16.0;
        final double springWheelPivotOffsetVer = -2.0 / 16.0;

        // Tele angle/distance: from tele pivot to wheel attachment
        final double teleAngle    = Math.atan2(
                verticalWheelPosition - teleMountVer,
                horizontalWheelPosition - wheelPivotOffsetHor - teleMountHor);
        final double teleDistance = new Vector2d(
                verticalWheelPosition - teleMountVer,
                horizontalWheelPosition - wheelPivotOffsetHor - teleMountHor).length();

        // Spring angle/distance: from spring pivot to spring attachment on wheel
        final double springAngle    = Math.atan2(
                verticalWheelPosition - springWheelPivotOffsetVer - springMountVer,
                horizontalWheelPosition - springWheelPivotOffsetHor - springMountHor);
        final double springDistance = new Vector2d(
                verticalWheelPosition - springWheelPivotOffsetVer - springMountVer,
                horizontalWheelPosition - springWheelPivotOffsetHor - springMountHor).length();

        ms.pushPose();

        // All assembly is facing-agnostic for centered mount — no facing rotation needed
        // since the wheel is centered. Only the yaw drives orientation.

        // --- Tele arms (inside block, aimed downward) ---
        ms.pushPose();
        ms.translate(0.0, teleMountVer, 0.0);
        ms.translate(0.5, 0.5, 0.5);
        ms.mulPose(Axis.XP.rotation((float) teleAngle));
        ms.translate(-0.5, -0.5, -0.5);
        teleOuter.light(light).renderInto(ms, cutoutVb);
        ms.translate(0.0, 0.0, -(teleDistance - 1.0));
        teleInner.light(light).renderInto(ms, cutoutVb);
        ms.popPose();

        // --- teleMount + wheel: orbits block center on Y ---
        ms.pushPose();
        ms.translate(0.0, verticalWheelPosition, 0.0);

        // Orbit around block center Y axis
        ms.translate(0.5, 0.5, 0.5);
        ms.mulPose(Axis.YP.rotation((float) be.getLerpedYaw(partialTicks)));
        ms.translate(-0.5, -0.5, -0.5);

        teleMount.light(light).renderInto(ms, cutoutVb);

        // Wheel spin at mount center
        ms.translate(0.5, 0.5, 0.5);

        final ItemStack itemStack = be.getHeldItem();
        final TireLike tireLike   = itemStack.get(OffroadDataComponents.TIRE);

        final double signMultiplier = -be.getLerpedAngle(partialTicks)
                * (direction.getAxisDirection() == Direction.AxisDirection.POSITIVE ? 1.0 : -1.0)
                * (direction.getAxis() == Direction.Axis.X ? 1.0 : -1.0);
        ms.mulPose(Axis.ZP.rotation((float) signMultiplier));

        if (tireLike != null) {
            final Vec3 rotation = tireLike.rotation();
            ms.mulPose(Axis.XP.rotation((float) Math.toRadians(rotation.x)));
            ms.mulPose(Axis.YP.rotation((float) Math.toRadians(rotation.y)));
            ms.mulPose(Axis.ZP.rotation((float) Math.toRadians(rotation.z)));

            if (tireLike.model().isPresent()) {
                ms.translate(tireLike.offset().x, tireLike.offset().y, tireLike.offset().z);
                CachedBuffers.partial(PartialModel.of(tireLike.model().get()), state)
                        .light(light)
                        .translate(-0.5f, 0.0f, -0.5f)
                        .renderInto(ms, cutoutVb);
            } else {
                ms.translate(tireLike.offset().x, tireLike.offset().y, tireLike.offset().z);
                Minecraft.getInstance().getItemRenderer().renderStatic(
                        itemStack, ItemDisplayContext.NONE,
                        light, overlay, ms, buffer, be.getLevel(), 0);
            }
        }
        ms.popPose();

        // --- Spring ---
        ms.pushPose();
        ms.translate(0.5, 0.5 + springMountVer, 0.5 - springMountHor);
        ms.mulPose(Axis.XP.rotation((float) springAngle + Mth.PI / 2.0f));
        ms.translate(-0.5, -0.5 - springMountVer, -0.5 + springMountHor);

        final float springSpan = (float) springDistance - 4.0f / 16.0f;

        springTop.light(light).renderInto(ms, cutoutVb);
        springMiddle.light(light)
                .translate(0.0f, 13.0f / 16.0f, 0.0f)
                .scale(1.0f, springSpan / (14.0f / 16.0f), 1.0f)
                .translateBack(0.0f, 13.0f / 16.0f, 0.0f)
                .renderInto(ms, cutoutVb);
        springBottom.light(light)
                .translate(0.0, -(springSpan - 14.0 / 16.0), 0.0)
                .renderInto(ms, cutoutVb);
        ms.popPose();

        ms.popPose();
    }

    @Override
    protected SuperByteBuffer getRotatedModel(final ChainDrivableWheelMountBlockEntity te, final BlockState state) {
        shaft(state.getValue(BlockStateProperties.HORIZONTAL_FACING).getAxis());
        return CachedBuffers.partialFacing(AllPartialModels.SHAFT_HALF, te.getBlockState(), Direction.UP);
    }

    @Override
    public int getViewDistance() {
        return 512;
    }
}