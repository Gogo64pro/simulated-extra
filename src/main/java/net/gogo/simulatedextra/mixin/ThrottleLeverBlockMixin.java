package net.gogo.simulatedextra.mixin;

import dev.simulated_team.simulated.content.blocks.throttle_lever.ThrottleLeverBlock;
import dev.simulated_team.simulated.content.blocks.throttle_lever.ThrottleLeverBlockEntity;
import net.gogo.simulatedextra.Imixin.IThrottleLeverCustomName;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.FaceAttachedHorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.Objects;

@Mixin(ThrottleLeverBlock.class)
public abstract class ThrottleLeverBlockMixin extends FaceAttachedHorizontalDirectionalBlock {

    protected ThrottleLeverBlockMixin(Properties p_53182_) {
        super(p_53182_);
    }

    @Override
    @Unique
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (!stack.is(Items.NAME_TAG)) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        if (level.isClientSide) {
            return ItemInteractionResult.CONSUME;
        }

        if (!(level.getBlockEntity(pos) instanceof ThrottleLeverBlockEntity be)) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        Component newName = stack.get(DataComponents.CUSTOM_NAME);
        Component oldName = ((IThrottleLeverCustomName) be).simulatedextra$getCustomLabel();

        if(Objects.equals(oldName, newName)) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        ((IThrottleLeverCustomName) be).simulatedextra$setCustomLabel(newName);

        level.playSound(null, pos, SoundEvents.ITEM_FRAME_REMOVE_ITEM, SoundSource.BLOCKS, 0.5F, 1.0F);

        if (!player.isCreative() ) {
            stack.shrink(1);
        }

        return ItemInteractionResult.SUCCESS;
    }

}
