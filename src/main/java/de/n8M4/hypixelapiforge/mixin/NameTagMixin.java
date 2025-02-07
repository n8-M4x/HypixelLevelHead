package de.n8M4.hypixelapiforge.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import de.n8M4.hypixelapiforge.Hypixelapiforge;
import de.n8M4.hypixelapiforge.LevelHead;
import de.n8M4.hypixelapiforge.PlayerStats;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.entity.state.PlayerRenderState;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.Field;
import java.util.UUID;

@Mixin(PlayerRenderer.class)
public class NameTagMixin {
    @Unique
    public PlayerRenderer hypixelapiforge$self() {
        return (PlayerRenderer) (Object) this;
    }

    @Inject(method = "renderNameTag*", at = @At("HEAD"))
    public void renderNameTag(PlayerRenderState p_360888_, Component p_117809_, PoseStack p_117810_, MultiBufferSource p_117811_, int p_117812_, CallbackInfo ci) {
        UUID uuid = null;
        if(Minecraft.getInstance().level == null) return;
        for(AbstractClientPlayer player : Minecraft.getInstance().level.players()) {
            if(player.getName().getString().equals(p_117809_.getString())) uuid = player.getUUID();
        }

        if(!Hypixelapiforge.playerApiList.containsKey(uuid)) Hypixelapiforge.playerApiList.put(uuid, new PlayerStats(uuid));
        if(!Hypixelapiforge.playerApiList.get(uuid).hasLevel() && !Hypixelapiforge.playerApiList.get(uuid).isRequested()) {
            Hypixelapiforge.playerApiList.get(uuid).setRequested(true);
            LevelHead.requestPlayerLevel(uuid);
        }
        boolean flag = !p_360888_.isDiscrete;
        p_117809_ = Component.literal("§2Hypixel Level: " + "§b§5" + Hypixelapiforge.playerApiList.get(uuid).getLevel());
        Vec3 vec3 = p_360888_.nameTagAttachment;
        if(vec3 == null) return;
        p_117810_.pushPose();
        p_117810_.translate(vec3.x, vec3.y + (double)0.5F, vec3.z);
        p_117810_.mulPose(hypixelapiforge$getDispatcher(hypixelapiforge$self()).cameraOrientation());
        p_117810_.scale(0.025F, -0.025F, 0.025F);
        Matrix4f matrix4f = p_117810_.last().pose();
        Font font = hypixelapiforge$self().getFont();
        float f = (float)(-font.width(p_117809_)) / 2.0F;

        font.drawInBatch(p_117809_, f, -10, -2130706433, false, matrix4f, p_117811_, flag ? Font.DisplayMode.SEE_THROUGH : Font.DisplayMode.NORMAL, 0, p_117812_);
        p_117810_.popPose();
    }

    @Unique
    private static EntityRenderDispatcher hypixelapiforge$getDispatcher(PlayerRenderer instance) {
        try {
            Field field = PlayerRenderer.class.getSuperclass().getSuperclass().getDeclaredField("entityRenderDispatcher");
            field.setAccessible(true);
            return (EntityRenderDispatcher) field.get(instance);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
