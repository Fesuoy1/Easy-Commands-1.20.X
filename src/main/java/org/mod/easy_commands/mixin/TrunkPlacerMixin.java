package org.mod.easy_commands.mixin;

import net.minecraft.util.math.random.Random;
import net.minecraft.world.gen.trunk.TrunkPlacer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static org.mod.easy_commands.EasyCommands.getTreeHeight;

@Mixin(TrunkPlacer.class)
public class TrunkPlacerMixin {

    @Final
    @Shadow
    protected int baseHeight;

    @Final
    @Shadow
    protected int firstRandomHeight;

    @Final
    @Shadow
    protected int secondRandomHeight;

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/random/Random;nextInt(I)I"), method = "getHeight", cancellable = true)
    public void onGetHeight(Random random, CallbackInfoReturnable<Integer> cir) {
        // Original code: return this.baseHeight + random.nextInt(this.firstRandomHeight + 1) + random.nextInt(this.secondRandomHeight + 1);
        // we want to modify the "+ 1" that come after the first random height and the second random height, and we want to change that to the amount we want.
        // That way we can set the height to the amount of blocks we want. then the tree will grow bigger.
        cir.setReturnValue(this.baseHeight + random.nextInt(this.firstRandomHeight + getTreeHeight() + random.nextInt(this.secondRandomHeight + getTreeHeight())));
    }
}
