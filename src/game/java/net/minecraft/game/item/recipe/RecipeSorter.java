package net.minecraft.game.item.recipe;

import java.util.Comparator;

final class RecipeSorter implements Comparator {
	RecipeSorter(CraftingManager var1) {
	}

	public final int compare(Object var1, Object var2) {
		CraftingRecipe var10000 = (CraftingRecipe)var1;
		CraftingRecipe var4 = (CraftingRecipe)var2;
		CraftingRecipe var3 = var10000;
		return var4.b() < var3.b() ? -1 : (var4.b() > var3.b() ? 1 : 0);
	}
}
