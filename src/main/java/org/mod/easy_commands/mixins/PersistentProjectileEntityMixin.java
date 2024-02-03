package org.mod.easy_commands.mixins;


import net.minecraft.entity.projectile.PersistentProjectileEntity;
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

@Mixin(PersistentProjectileEntity.class)
public class PersistentProjectileEntityMixin {

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/PersistentProjectileEntity;clearPiercingStatus()V"), method = "onBlockHit")
    public void onBlockHit(BlockHitResult blockHitResult, CallbackInfo ci) {
        if (EasyCommands.explosiveProjectilesEnabled) {
            // make the arrow explode when it hits a block. will replace "this.clearPiercingStatus();"
            explode(((PersistentProjectileEntity) (Object) this));
        }
    }

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/util/hit/EntityHitResult;getEntity()Lnet/minecraft/entity/Entity;"), method = "onEntityHit", cancellable = true)
    public void onEntityHit(EntityHitResult entityHitResult, CallbackInfo ci) {
        if (EasyCommands.explosiveProjectilesEnabled) {
            // make the arrow explode when it hits an entity. will replace line "Entity entity = entityHitResult.getEntity();"
            explode(((PersistentProjectileEntity) (Object) this));
            ci.cancel();
        }
    }

    @Unique
    private void explode(PersistentProjectileEntity persistentProjectileEntity) {
        Vec3d pos = persistentProjectileEntity.getPos();
        World world = persistentProjectileEntity.getWorld();
        persistentProjectileEntity.discard();
        world.createExplosion(persistentProjectileEntity, pos.x, pos.y, pos.z, 3.5f, World.ExplosionSourceType.TNT);
    }

}
