package net.gogo.simulatedextra.mixin;

import com.simibubi.create.foundation.blockEntity.behaviour.ValueBoxTransform;
import com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.ScrollValueBehaviour;
import net.gogo.simulatedextra.Imixin.ISuspensionStrengthBehaviourAccess;
import net.gogo.simulatedextra.Imixin.ISuspensionStrengthValueBoxAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = ScrollValueBehaviour.class, remap = false)
public abstract class SuspensionStrengthBehaviour implements ISuspensionStrengthBehaviourAccess {

    @Shadow
    ValueBoxTransform slotPositioning;

    @Override
    public void simulatedextra$makeCenteredWheelMount() {
        if (slotPositioning instanceof ISuspensionStrengthValueBoxAccess casted) {
            casted.simulatedextra$makeCenteredWheelMount();
        }
    }
}