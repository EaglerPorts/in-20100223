package net.minecraft.client.model;

import net.minecraft.client.render.Tessellator;
import net.minecraft.game.physics.Vec3D;
import org.lwjgl.opengl.GL11;

public final class ModelRenderer {
	private PositionTextureVertex[] corners;
	private TexturedQuad[] faces;
	private int textureOffsetX;
	private int textureOffsetY;
	private float rotationPointX;
	private float rotationPointY;
	private float rotationPointZ;
	public float rotateAngleX;
	public float rotateAngleY;
	public float rotateAngleZ;
	private boolean compiled = false;
	private int displayList = 0;
	public boolean mirror = false;
	public boolean showModel = true;
	private boolean isHidden = false;

	public ModelRenderer(int var1, int var2) {
		this.textureOffsetX = var1;
		this.textureOffsetY = var2;
	}

	public final void addBox(float var1, float var2, float var3, int var4, int var5, int var6, float var7) {
		this.corners = new PositionTextureVertex[8];
		this.faces = new TexturedQuad[6];
		float var8 = var1 + (float)var4;
		float var9 = var2 + (float)var5;
		float var10 = var3 + (float)var6;
		var1 -= var7;
		var2 -= var7;
		var3 -= var7;
		var8 += var7;
		var9 += var7;
		var10 += var7;
		if(this.mirror) {
			var7 = var8;
			var8 = var1;
			var1 = var7;
		}

		PositionTextureVertex var20 = new PositionTextureVertex(var1, var2, var3, 0.0F, 0.0F);
		PositionTextureVertex var11 = new PositionTextureVertex(var8, var2, var3, 0.0F, 8.0F);
		PositionTextureVertex var12 = new PositionTextureVertex(var8, var9, var3, 8.0F, 8.0F);
		PositionTextureVertex var18 = new PositionTextureVertex(var1, var9, var3, 8.0F, 0.0F);
		PositionTextureVertex var13 = new PositionTextureVertex(var1, var2, var10, 0.0F, 0.0F);
		PositionTextureVertex var15 = new PositionTextureVertex(var8, var2, var10, 0.0F, 8.0F);
		PositionTextureVertex var21 = new PositionTextureVertex(var8, var9, var10, 8.0F, 8.0F);
		PositionTextureVertex var14 = new PositionTextureVertex(var1, var9, var10, 8.0F, 0.0F);
		this.corners[0] = var20;
		this.corners[1] = var11;
		this.corners[2] = var12;
		this.corners[3] = var18;
		this.corners[4] = var13;
		this.corners[5] = var15;
		this.corners[6] = var21;
		this.corners[7] = var14;
		this.faces[0] = new TexturedQuad(new PositionTextureVertex[]{var15, var11, var12, var21}, this.textureOffsetX + var6 + var4, this.textureOffsetY + var6, this.textureOffsetX + var6 + var4 + var6, this.textureOffsetY + var6 + var5);
		this.faces[1] = new TexturedQuad(new PositionTextureVertex[]{var20, var13, var14, var18}, this.textureOffsetX, this.textureOffsetY + var6, this.textureOffsetX + var6, this.textureOffsetY + var6 + var5);
		this.faces[2] = new TexturedQuad(new PositionTextureVertex[]{var15, var13, var20, var11}, this.textureOffsetX + var6, this.textureOffsetY, this.textureOffsetX + var6 + var4, this.textureOffsetY + var6);
		this.faces[3] = new TexturedQuad(new PositionTextureVertex[]{var12, var18, var14, var21}, this.textureOffsetX + var6 + var4, this.textureOffsetY, this.textureOffsetX + var6 + var4 + var4, this.textureOffsetY + var6);
		this.faces[4] = new TexturedQuad(new PositionTextureVertex[]{var11, var20, var18, var12}, this.textureOffsetX + var6, this.textureOffsetY + var6, this.textureOffsetX + var6 + var4, this.textureOffsetY + var6 + var5);
		this.faces[5] = new TexturedQuad(new PositionTextureVertex[]{var13, var15, var21, var14}, this.textureOffsetX + var6 + var4 + var6, this.textureOffsetY + var6, this.textureOffsetX + var6 + var4 + var6 + var4, this.textureOffsetY + var6 + var5);
		if(this.mirror) {
			for(int var16 = 0; var16 < this.faces.length; ++var16) {
				TexturedQuad var17 = this.faces[var16];
				PositionTextureVertex[] var19 = new PositionTextureVertex[var17.vertexPositions.length];

				for(var4 = 0; var4 < var17.vertexPositions.length; ++var4) {
					var19[var4] = var17.vertexPositions[var17.vertexPositions.length - var4 - 1];
				}

				var17.vertexPositions = var19;
			}
		}

	}

	public final void setRotationPoint(float var1, float var2, float var3) {
		this.rotationPointX = var1;
		this.rotationPointY = var2;
		this.rotationPointZ = var3;
	}

	public final void render(float var1) {
		if(this.showModel) {
			if(!this.compiled) {
				float var3 = var1;
				ModelRenderer var2 = this;
				this.displayList = GL11.glGenLists(1);
				GL11.glNewList(this.displayList, GL11.GL_COMPILE);
				Tessellator var4 = Tessellator.instance;

				for(int var5 = 0; var5 < var2.faces.length; ++var5) {
					var4.startDrawingQuads();
					TexturedQuad var10000 = var2.faces[var5];
					float var8 = var3;
					Tessellator var7 = var4;
					TexturedQuad var6 = var10000;
					Vec3D var9 = var6.vertexPositions[1].vector3D.subtract(var6.vertexPositions[0].vector3D).normalize();
					Vec3D var10 = var6.vertexPositions[1].vector3D.subtract(var6.vertexPositions[2].vector3D).normalize();
					var9 = (new Vec3D(var9.yCoord * var10.zCoord - var9.zCoord * var10.yCoord, var9.zCoord * var10.xCoord - var9.xCoord * var10.zCoord, var9.xCoord * var10.yCoord - var9.yCoord * var10.xCoord)).normalize();
					var7.setNormal(-var9.xCoord, -var9.yCoord, -var9.zCoord);

					for(int var11 = 0; var11 < 4; ++var11) {
						PositionTextureVertex var12 = var6.vertexPositions[var11];
						var7.addVertexWithUV(var12.vector3D.xCoord * var8, var12.vector3D.yCoord * var8, var12.vector3D.zCoord * var8, var12.texturePositionX, var12.texturePositionY);
					}

					var4.draw();
				}

				GL11.glEndList();
				var2.compiled = true;
			}

			if(this.rotateAngleX == 0.0F && this.rotateAngleY == 0.0F && this.rotateAngleZ == 0.0F) {
				if(this.rotationPointX == 0.0F && this.rotationPointY == 0.0F && this.rotationPointZ == 0.0F) {
					GL11.glCallList(this.displayList);
				} else {
					GL11.glTranslatef(this.rotationPointX * var1, this.rotationPointY * var1, this.rotationPointZ * var1);
					GL11.glCallList(this.displayList);
					GL11.glTranslatef(-this.rotationPointX * var1, -this.rotationPointY * var1, -this.rotationPointZ * var1);
				}
			} else {
				GL11.glPushMatrix();
				GL11.glTranslatef(this.rotationPointX * var1, this.rotationPointY * var1, this.rotationPointZ * var1);
				if(this.rotateAngleZ != 0.0F) {
					GL11.glRotatef(this.rotateAngleZ * (180.0F / (float)Math.PI), 0.0F, 0.0F, 1.0F);
				}

				if(this.rotateAngleY != 0.0F) {
					GL11.glRotatef(this.rotateAngleY * (180.0F / (float)Math.PI), 0.0F, 1.0F, 0.0F);
				}

				if(this.rotateAngleX != 0.0F) {
					GL11.glRotatef(this.rotateAngleX * (180.0F / (float)Math.PI), 1.0F, 0.0F, 0.0F);
				}

				GL11.glCallList(this.displayList);
				GL11.glPopMatrix();
			}
		}
	}
}
