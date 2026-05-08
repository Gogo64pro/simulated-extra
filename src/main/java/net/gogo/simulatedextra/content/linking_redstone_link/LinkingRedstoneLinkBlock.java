package net.gogo.simulatedextra.content.linking_redstone_link;

import com.simibubi.create.content.redstone.link.RedstoneLinkBlock;
import com.simibubi.create.content.redstone.link.RedstoneLinkBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.ScrollValueBehaviour;
import net.gogo.simulatedextra.data.FrequencyCounter;
import net.gogo.simulatedextra.registers.BlockEntityTypes;
import net.gogo.simulatedextra.registers.BlocksReg;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class LinkingRedstoneLinkBlock extends RedstoneLinkBlock {
    public LinkingRedstoneLinkBlock(Properties properties) {
        super(properties);
    }

    @Override
    public Class<RedstoneLinkBlockEntity> getBlockEntityClass() {
        return RedstoneLinkBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends RedstoneLinkBlockEntity> getBlockEntityType() {
        return BlockEntityTypes.LINKING_REDSTONE_LINK.get();
    }

}
