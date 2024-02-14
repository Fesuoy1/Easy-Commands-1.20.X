package org.mod.easy_commands.mixin;


import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.mod.easy_commands.EasyCommands;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PersistentProjectileEntity.class)
public class PersistentProjectileEntityMixin {

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/PersistentProjectileEntity;clearPiercingStatus()V"), method = "onBlockHit")
    public void onBlockHit(BlockHitResult blockHitResult, CallbackInfo ci) {
        World world = ((PersistentProjectileEntity) (Object) this).getWorld();
        if (EasyCommands.isExplosiveProjectilesEnabled(world)) {
            EasyCommands.explode(((PersistentProjectileEntity) (Object) this));
        }
    }

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/util/hit/EntityHitResult;getEntity()Lnet/minecraft/entity/Entity;"), method = "onEntityHit", cancellable = true)
    public void onEntityHit(EntityHitResult entityHitResult, CallbackInfo ci) {
        World world = ((PersistentProjectileEntity) (Object) this).getWorld();
        if (EasyCommands.isExplosiveProjectilesEnabled(world)) {
            EasyCommands.explode(((PersistentProjectileEntity) (Object) this));
            ci.cancel();
        }
    }

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/PersistentProjectileEntity;checkBlockCollision()V", shift = At.Shift.BEFORE), method = "tick")
    public void onTick(CallbackInfo ci) {
        // Add smoke particles
        if (EasyCommands.isExplosiveProjectilesEnabled(((PersistentProjectileEntity) (Object) this).getWorld())) {
            Vec3d pos = ((ProjectileEntity) (Object) this).getPos();
            ((ProjectileEntity) (Object) this).getWorld().addParticle(ParticleTypes.SMOKE, pos.getX(), pos.getY() + 0.1, pos.getZ(), 0.0, 0.0, 0.0);

        }
    }
}
