package org.mod.easy_commands.mixins;


import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import org.mod.easy_commands.EasyCommands;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PersistentProjectileEntity.class)
public class PersistentProjectileEntityMixin {

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/PersistentProjectileEntity;clearPiercingStatus()V"), method = "onBlockHit")
    public void onBlockHit(BlockHitResult blockHitResult, CallbackInfo ci) {
        if (EasyCommands.isExplosiveProjectilesEnabled()) {
            EasyCommands.explode(((PersistentProjectileEntity) (Object) this));
        }
    }

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/util/hit/EntityHitResult;getEntity()Lnet/minecraft/entity/Entity;"), method = "onEntityHit", cancellable = true)
    public void onEntityHit(EntityHitResult entityHitResult, CallbackInfo ci) {
        if (EasyCommands.isExplosiveProjectilesEnabled()) {
            EasyCommands.explode(((PersistentProjectileEntity) (Object) this));
            ci.cancel();
        }
    }

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/PersistentProjectileEntity;checkBlockCollision()V", shift = At.Shift.BEFORE), method = "tick")
    public void onTick(CallbackInfo ci) {
        // Add smoke particles
        if (EasyCommands.isExplosiveProjectilesEnabled()) {
            if ((PersistentProjectileEntity) (Object) this instanceof ArrowEntity) {
                ((PersistentProjectileEntity) (Object) this).getWorld().addParticle(ParticleTypes.SMOKE, ((PersistentProjectileEntity) (Object) this).getParticleX(0.5) - 0.1, ((PersistentProjectileEntity) (Object) this).getRandomBodyY(), ((PersistentProjectileEntity) (Object) this).getParticleZ(0.5), 0.0, 0.0, 0.0);
            }
        }
    }
}
