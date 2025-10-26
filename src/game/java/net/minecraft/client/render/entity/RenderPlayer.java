package net.minecraft.client.render.entity;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.entity.EntityLiving;
import net.minecraft.game.entity.player.EntityPlayer;
import net.minecraft.game.entity.player.InventoryPlayer;
import net.minecraft.game.item.Item;
import net.minecraft.game.item.ItemArmor;
import net.minecraft.game.item.ItemStack;

public final class RenderPlayer extends RenderLiving {
	private ModelBiped modelBipedMain = (ModelBiped)this.mainModel;
	private ModelBiped modelArmorChestplate = new ModelBiped(1.0F);
	private ModelBiped modelArmor = new ModelBiped(0.5F);
	private static final String[] armorFilenamePrefix = new String[]{"cloth", "chain", "iron", "diamond", "gold"};

	public RenderPlayer() {
		super(new ModelBiped(0.0F), 0.5F);
	}

	private void renderPlayer(EntityPlayer var1, float var2, float var3, float var4, float var5, float var6) {
		super.a(var1, var2, var3 - var1.yOffset, var4, var5, var6);
	}

	public final void drawFirstPersonHand() {
		this.modelBipedMain.bipedRightArm.render(1.0F);
	}

	protected final boolean shouldRenderPass(EntityLiving var1, int var2) {
		EntityPlayer var10001 = (EntityPlayer)var1;
		int var3 = var2;
		EntityPlayer var5 = var10001;
		int var4 = 3 - var3;
		InventoryPlayer var6 = var5.inventory;
		ItemStack var7 = var6.armorInventory[var4];
		if(var7 != null) {
			Item var8 = var7.getItem();
			if(var8 instanceof ItemArmor) {
				ItemArmor var9 = (ItemArmor)var8;
				this.loadTexture("/armor/" + armorFilenamePrefix[var9.renderIndex] + "_" + (var3 == 2 ? 2 : 1) + ".png");
				ModelBiped var10 = var3 == 2 ? this.modelArmor : this.modelArmorChestplate;
				var10.bipedHead.showModel = var3 == 0;
				var10.bipedHeadwear.showModel = var3 == 0;
				var10.bipedBody.showModel = var3 == 1 || var3 == 2;
				var10.bipedRightArm.showModel = var3 == 1;
				var10.bipedLeftArm.showModel = var3 == 1;
				var10.bipedRightLeg.showModel = var3 == 2 || var3 == 3;
				var10.bipedLeftLeg.showModel = var3 == 2 || var3 == 3;
				this.setRenderPassModel(var10);
				return true;
			}
		}

		return false;
	}

	public final void a(EntityLiving var1, float var2, float var3, float var4, float var5, float var6) {
		this.renderPlayer((EntityPlayer)var1, var2, var3, var4, var5, var6);
	}

	public final void doRender(Entity var1, float var2, float var3, float var4, float var5, float var6) {
		this.renderPlayer((EntityPlayer)var1, var2, var3, var4, var5, var6);
	}
}
