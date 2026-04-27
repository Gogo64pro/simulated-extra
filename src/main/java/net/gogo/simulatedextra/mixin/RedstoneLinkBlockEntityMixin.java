package net.gogo.simulatedextra.mixin;

import com.simibubi.create.content.redstone.link.LinkBehaviour;
import com.simibubi.create.content.redstone.link.RedstoneLinkBlockEntity;
import net.gogo.simulatedextra.IFrequencyAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(RedstoneLinkBlockEntity.class)
public class RedstoneLinkBlockEntityMixin implements IFrequencyAccess {
    @Shadow private LinkBehaviour link;
    @Shadow private boolean transmitter;
    @Override
    public LinkBehaviour simulatedextra$getLink() {
        return link;
    }

    @Override
    public void simulatedextra$setLink(LinkBehaviour link) {
        this.link = link;
    }

    @Override
    public boolean simulatedextra$isTransmitter() {
        return transmitter;
    }
}
