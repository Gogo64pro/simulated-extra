package net.gogo.simulatedextra.content.linking_redstone_link;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.blockEntity.renderer.SmartBlockEntityRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;
import com.mojang.math.Axis;

public class IdLinkBlockEntityRenderer extends SmartBlockEntityRenderer<LinkingRedstoneLinkBlockEntity> {

    public IdLinkBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected void renderSafe(LinkingRedstoneLinkBlockEntity be, float partialTicks, PoseStack ms,
                              MultiBufferSource buffer, int light, int overlay) {
        super.renderSafe(be, partialTicks, ms, buffer, light, overlay);

        int id = be.getSavedId();
        String text = String.valueOf(id);

        Direction face = be.getBlockState().getValue(BlockStateProperties.FACING);
        Vec3 offset = new Vec3(0.5, 0.5, 0.5)
                .add(Vec3.atLowerCornerOf(face.getNormal()).scale(0.502));

        ms.pushPose();
        ms.translate(offset.x, offset.y, offset.z);

        float yRot = switch (face) {
            case NORTH -> 0f; case SOUTH -> 180f;
            case EAST  -> 90f; case WEST  -> 270f;
            default    -> 0f;
        };
        ms.mulPose(Axis.YP.rotationDegrees(yRot));
        if (face == Direction.UP)   ms.mulPose(Axis.XP.rotationDegrees(90f));
        if (face == Direction.DOWN) ms.mulPose(Axis.XP.rotationDegrees(-90f));

        ms.translate(0,  0, 12.5 / 16d);
        float scale = 1 / 48f;
        ms.scale(-scale, -scale, scale);

        var font = Minecraft.getInstance().font;
        float w = font.width(text);
        font.drawInBatch(text, -w / 2f, -4f, 0xFFFFFFFF,
                false, ms.last().pose(), buffer,
                Font.DisplayMode.NORMAL,
                0, LightTexture.FULL_BRIGHT);

        ms.popPose();
    }
}