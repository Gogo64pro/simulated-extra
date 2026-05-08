package net.gogo.simulatedextra.content.linking_redstone_link;

import com.simibubi.create.foundation.blockEntity.behaviour.*;
import com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.INamedIconOptions;
import com.simibubi.create.foundation.gui.AllIcons;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.Tags;

import java.util.List;

public class ClearBehaviour extends BlockEntityBehaviour implements ValueSettingsBehaviour {
    public static final BehaviourType<ClearBehaviour> TYPE = new BehaviourType<>();
    ValueBoxTransform slot;
    public ClearBehaviour(LinkingRedstoneLinkBlockEntity be, ValueBoxTransform slot) {
        super(be);
        this.slot = slot;
    }
    @Override
    public BehaviourType<?> getType() { return TYPE; }
    @Override
    public boolean testHit(Vec3 hit) {
        var localHit = hit.subtract(Vec3.atLowerCornerOf(blockEntity.getBlockPos()));
        return slot.testHit(blockEntity.getLevel(), blockEntity.getBlockPos(), blockEntity.getBlockState(), localHit);
    }
    @Override
    public boolean isActive() { return true; }
    @Override
    public boolean onlyVisibleWithWrench() { return true; }
    @Override
    public ValueBoxTransform getSlotPositioning() { return slot; }

    @Override
    public ValueSettingsBoard createBoard(Player player, BlockHitResult hitResult) {
        return new ValueSettingsBoard(
                Component.literal("Clear"),
                0,
                1,
                List.of(Component.literal("Clear")),
                new ValueSettingsFormatter.ScrollOptionSettingsFormatter(
                        new INamedIconOptions[] {
                                new INamedIconOptions() {
                                    @Override
                                    public String getTranslationKey() {
                                        return "your.mod.id.clear";
                                    }
                                    @Override
                                    public AllIcons getIcon() {
                                        return AllIcons.I_TRASH;
                                    }
                                }
                        }
                )
        );
    }

    @Override
    public void setValueSettings(Player player, ValueSettings valueSetting, boolean ctrlDown) {
        LinkingRedstoneLinkBlockEntity casted = (LinkingRedstoneLinkBlockEntity) blockEntity;
        casted.newFrequency();
    }
    @Override
    public ValueSettings getValueSettings() { return new ValueSettings(0,0); }
    @Override
    public void onShortInteract(Player player, InteractionHand hand, Direction side, BlockHitResult hitResult) {
        setValueSettings(player, getValueSettings(), false);
    }
    @Override
    public boolean bypassesInput(ItemStack mainhandItem) { return mainhandItem.is(Tags.Items.TOOLS_WRENCH); }
    @Override
    public int netId() { return 4; }
}