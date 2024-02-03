package org.mod.easy_commands.mixins;


import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.mod.easy_commands.EasyCommands;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ProjectileEntity.class)
public class ProjectileEntityMixin {

    @Inject(at = @At(value = "HEAD"), method = "onEntityHit", cancellable = true)
    public void onEntityHit(EntityHitResult entityHitResult, CallbackInfo ci) {
        if (EasyCommands.explosiveProjectilesEnabled) {
            // make the arrow explode when it hits an entity. will add a new line.
            explode(((ProjectileEntity) (Object) this));
        }
    }

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;onProjectileHit(Lnet/minecraft/world/World;Lnet/minecraft/block/BlockState;Lnet/minecraft/util/hit/BlockHitResult;Lnet/minecraft/entity/projectile/ProjectileEntity;)V", shift = At.Shift.AFTER), method = "onBlockHit", cancellable = true)
    public void onBlockHit(BlockHitResult blockHitResult, CallbackInfo ci) {
        if (EasyCommands.explosiveProjectilesEnabled) {
            // make the arrow explode when it hits a block. will add a new line.
            explode(((ProjectileEntity) (Object) this));
        }
    }



    @Unique
    private void explode(ProjectileEntity projectileEntity) {
        Vec3d pos = projectileEntity.getPos();
        World world = projectileEntity.getWorld();
        projectileEntity.discard();
        world.createExplosion(projectileEntity, pos.x, pos.y, pos.z, 3.5f, World.ExplosionSourceType.TNT);
    }
}
