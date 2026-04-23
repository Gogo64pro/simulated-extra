package net.gogo.simulatedextra.content.centered_wheel_mount;

import dev.ryanhcode.offroad.content.blocks.wheel_mount.WheelMountBlock;
import dev.ryanhcode.offroad.content.blocks.wheel_mount.WheelMountBlockEntity;
import net.gogo.simulatedextra.registers.BlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class CenteredWheelMountBlock extends WheelMountBlock {

    public CenteredWheelMountBlock(Properties properties) {
        super(properties);
    }

    @Override
    public BlockEntityType<? extends WheelMountBlockEntity> getBlockEntityType() {
        return BlockEntityTypes.CENTERED_WHEEL_MOUNT.get();
    }

    @Override
    public boolean hasShaftTowards(final LevelReader world, final BlockPos pos, final BlockState state, final Direction face) {
        var direction = state.getValue(HORIZONTAL_FACING);
        return face == direction || face == direction.getOpposite();
    }
//    @Override
//    public void appendHoverText(@NotNull ItemStack stack, Item. @NotNull TooltipContext context,
//                                @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
//
//        if (flag.isAdvanced()) {
//            tooltip.add(Component.translatable("block.mymod.my_block.tooltip")
//                    .withStyle(ChatFormatting.GRAY));
//        } else {
//            tooltip.add(TooltipHelper.holdShift(FontHelper.Palette.STANDARD_CREATE, false));
//        }
//
//        TooltipModifier modifier = KineticStats.create(this.asItem());
//        if (modifier != null) {
//            modifier.modify(new ItemTooltipEvent(stack, null, tooltip, flag, context));
//        }
//
//        super.appendHoverText(stack, context, tooltip, flag);
//    }
}

