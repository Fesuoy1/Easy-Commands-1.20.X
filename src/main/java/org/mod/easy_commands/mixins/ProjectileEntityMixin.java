package org.mod.easy_commands.mixins;


import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.thrown.SnowballEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Vec3d;
import org.mod.easy_commands.EasyCommands;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ProjectileEntity.class)
public class ProjectileEntityMixin {

    @Inject(at = @At(value = "HEAD"), method = "onEntityHit")
    public void onEntityHit(EntityHitResult entityHitResult, CallbackInfo ci) {
        if (EasyCommands.isExplosiveProjectilesEnabled()) {
            EasyCommands.explode(((ProjectileEntity) (Object) this));
        }
    }

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;onProjectileHit(Lnet/minecraft/world/World;Lnet/minecraft/block/BlockState;Lnet/minecraft/util/hit/BlockHitResult;Lnet/minecraft/entity/projectile/ProjectileEntity;)V", shift = At.Shift.AFTER), method = "onBlockHit")
    public void onBlockHit(BlockHitResult blockHitResult, CallbackInfo ci) {
        if (EasyCommands.isExplosiveProjectilesEnabled()) {
            EasyCommands.explode(((ProjectileEntity) (Object) this));
        }
    }

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;tick()V", shift = At.Shift.BEFORE), method = "tick")
    public void onTick(CallbackInfo ci) {
        // Add smoke particles
        if (EasyCommands.isExplosiveProjectilesEnabled()) {
            if (((ProjectileEntity) (Object) this) instanceof SnowballEntity) {
                Vec3d pos = ((ProjectileEntity) (Object) this).getPos();
                ((ProjectileEntity) (Object) this).getWorld().addParticle(ParticleTypes.SMOKE, pos.getX(), pos.getY() + 0.1, pos.getZ(), 0.0, 0.0, 0.0);
            }
        }
    }
}
