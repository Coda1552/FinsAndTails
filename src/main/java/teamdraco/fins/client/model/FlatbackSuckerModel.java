package teamdraco.fins.client.model;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class FlatbackSuckerModel<T extends Entity> extends EntityModel<T> {
    public ModelRenderer body;
    public ModelRenderer whiskers;
    public ModelRenderer tail;
    public ModelRenderer finBackLeft;
    public ModelRenderer finBackRight;
    public ModelRenderer finFrontRight;
    public ModelRenderer finFrontLeft;
    public ModelRenderer tailFin;

    public FlatbackSuckerModel() {
        this.texWidth = 32;
        this.texHeight = 32;
        this.finBackLeft = new ModelRenderer(this, 0, 16);
        this.finBackLeft.mirror = true;
        this.finBackLeft.setPos(2.0F, 0.5F, 3.5F);
        this.finBackLeft.addBox(0.0F, -0.5F, -1.0F, 1.0F, 1.0F, 2.0F, 0.0F, 0.0F, 0.0F);
        this.finFrontLeft = new ModelRenderer(this, 0, 11);
        this.finFrontLeft.mirror = true;
        this.finFrontLeft.setPos(2.0F, 0.5F, -1.5F);
        this.finFrontLeft.addBox(0.0F, -0.5F, -2.0F, 2.0F, 1.0F, 4.0F, 0.0F, 0.0F, 0.0F);
        this.tailFin = new ModelRenderer(this, 0, 0);
        this.tailFin.setPos(0.0F, 0.0F, 2.0F);
        this.tailFin.addBox(0.0F, -2.5F, 0.0F, 0.0F, 4.0F, 2.9F, 0.0F, 0.0F, 0.0F);
        this.body = new ModelRenderer(this, 0, 0);
        this.body.setPos(0.0F, 23.0F, 0.0F);
        this.body.addBox(-2.0F, -1.0F, -4.5F, 4.0F, 2.0F, 9.0F, 0.0F, 0.0F, 0.0F);
        this.whiskers = new ModelRenderer(this, 16, 0);
        this.whiskers.setPos(0.0F, 0.0F, -4.5F);
        this.whiskers.addBox(-3.0F, 0.0F, -1.0F, 6.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F);
        this.tail = new ModelRenderer(this, 16, 16);
        this.tail.setPos(0.0F, 0.5F, 4.5F);
        this.tail.addBox(-1.0F, -0.5F, 0.0F, 2.0F, 1.0F, 2.0F, 0.0F, 0.0F, 0.0F);
        this.finBackRight = new ModelRenderer(this, 0, 16);
        this.finBackRight.setPos(-2.0F, 0.5F, 3.5F);
        this.finBackRight.addBox(-1.0F, -0.5F, -1.0F, 1.0F, 1.0F, 2.0F, 0.0F, 0.0F, 0.0F);
        this.finFrontRight = new ModelRenderer(this, 0, 11);
        this.finFrontRight.setPos(-2.0F, 0.5F, -1.5F);
        this.finFrontRight.addBox(-2.0F, -0.5F, -2.0F, 2.0F, 1.0F, 4.0F, 0.0F, 0.0F, 0.0F);
        this.body.addChild(this.finBackLeft);
        this.body.addChild(this.finFrontLeft);
        this.tail.addChild(this.tailFin);
        this.body.addChild(this.whiskers);
        this.body.addChild(this.tail);
        this.body.addChild(this.finBackRight);
        this.body.addChild(this.finFrontRight);
    }

    @Override
    public void renderToBuffer(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) { 
        ImmutableList.of(this.body).forEach((modelRenderer) -> { 
            modelRenderer.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        });
    }

    @Override
    public void setupAnim(T entityIn, float f, float f1, float ageInTicks, float netHeadYaw, float headPitch) {
        float degree = 1.0f;
        float speed = 3.0f;
        this.body.yRot = MathHelper.cos(f * speed * 0.4F) * degree * 0.5F * f1;
        this.tail.yRot = MathHelper.cos(f * speed * 0.4F) * degree * -0.5F * f1;
        this.finFrontLeft.zRot = MathHelper.cos(f * speed * 0.4F) * degree * 1.2F * f1;
        this.finFrontRight.zRot = MathHelper.cos(f * speed * 0.4F) * degree * -1.2F * f1;
        this.finBackRight.zRot = MathHelper.cos(f * speed * 0.4F) * degree * 1.2F * f1;
        this.finBackLeft.zRot = MathHelper.cos(f * speed * 0.4F) * degree * -1.2F * f1;
    }

    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.xRot = x;
        modelRenderer.yRot = y;
        modelRenderer.zRot = z;
    }
}
