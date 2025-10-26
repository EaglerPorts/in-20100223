package net.minecraft.client.render.camera;

import org.lwjgl.opengl.GL11;

import net.lax1dude.eaglercraft.internal.buffer.FloatBuffer;
import util.GLAllocation;
import util.MathHelper;

public final class ClippingHelperImplementation extends ClippingHelper {
	private static ClippingHelperImplementation instance = new ClippingHelperImplementation();
	private FloatBuffer projectionMatrixBuffer = GLAllocation.createFloatBuffer(16);
	private FloatBuffer modelviewMatrixBuffer = GLAllocation.createFloatBuffer(16);
	private FloatBuffer ICamera = GLAllocation.createFloatBuffer(16);

	public static ClippingHelper init() {
		ClippingHelperImplementation var0 = instance;
		var0.projectionMatrixBuffer.clear();
		var0.modelviewMatrixBuffer.clear();
		var0.ICamera.clear();
		GL11.glGetFloat(GL11.GL_PROJECTION_MATRIX, var0.projectionMatrixBuffer);
		GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX, var0.modelviewMatrixBuffer);
		var0.projectionMatrixBuffer.flip().limit(16);
		var0.projectionMatrixBuffer.get(var0.projectionMatrix);
		var0.modelviewMatrixBuffer.flip().limit(16);
		var0.modelviewMatrixBuffer.get(var0.modelviewMatrix);
		var0.clippingMatrix[0] = var0.modelviewMatrix[0] * var0.projectionMatrix[0] + var0.modelviewMatrix[1] * var0.projectionMatrix[4] + var0.modelviewMatrix[2] * var0.projectionMatrix[8] + var0.modelviewMatrix[3] * var0.projectionMatrix[12];
		var0.clippingMatrix[1] = var0.modelviewMatrix[0] * var0.projectionMatrix[1] + var0.modelviewMatrix[1] * var0.projectionMatrix[5] + var0.modelviewMatrix[2] * var0.projectionMatrix[9] + var0.modelviewMatrix[3] * var0.projectionMatrix[13];
		var0.clippingMatrix[2] = var0.modelviewMatrix[0] * var0.projectionMatrix[2] + var0.modelviewMatrix[1] * var0.projectionMatrix[6] + var0.modelviewMatrix[2] * var0.projectionMatrix[10] + var0.modelviewMatrix[3] * var0.projectionMatrix[14];
		var0.clippingMatrix[3] = var0.modelviewMatrix[0] * var0.projectionMatrix[3] + var0.modelviewMatrix[1] * var0.projectionMatrix[7] + var0.modelviewMatrix[2] * var0.projectionMatrix[11] + var0.modelviewMatrix[3] * var0.projectionMatrix[15];
		var0.clippingMatrix[4] = var0.modelviewMatrix[4] * var0.projectionMatrix[0] + var0.modelviewMatrix[5] * var0.projectionMatrix[4] + var0.modelviewMatrix[6] * var0.projectionMatrix[8] + var0.modelviewMatrix[7] * var0.projectionMatrix[12];
		var0.clippingMatrix[5] = var0.modelviewMatrix[4] * var0.projectionMatrix[1] + var0.modelviewMatrix[5] * var0.projectionMatrix[5] + var0.modelviewMatrix[6] * var0.projectionMatrix[9] + var0.modelviewMatrix[7] * var0.projectionMatrix[13];
		var0.clippingMatrix[6] = var0.modelviewMatrix[4] * var0.projectionMatrix[2] + var0.modelviewMatrix[5] * var0.projectionMatrix[6] + var0.modelviewMatrix[6] * var0.projectionMatrix[10] + var0.modelviewMatrix[7] * var0.projectionMatrix[14];
		var0.clippingMatrix[7] = var0.modelviewMatrix[4] * var0.projectionMatrix[3] + var0.modelviewMatrix[5] * var0.projectionMatrix[7] + var0.modelviewMatrix[6] * var0.projectionMatrix[11] + var0.modelviewMatrix[7] * var0.projectionMatrix[15];
		var0.clippingMatrix[8] = var0.modelviewMatrix[8] * var0.projectionMatrix[0] + var0.modelviewMatrix[9] * var0.projectionMatrix[4] + var0.modelviewMatrix[10] * var0.projectionMatrix[8] + var0.modelviewMatrix[11] * var0.projectionMatrix[12];
		var0.clippingMatrix[9] = var0.modelviewMatrix[8] * var0.projectionMatrix[1] + var0.modelviewMatrix[9] * var0.projectionMatrix[5] + var0.modelviewMatrix[10] * var0.projectionMatrix[9] + var0.modelviewMatrix[11] * var0.projectionMatrix[13];
		var0.clippingMatrix[10] = var0.modelviewMatrix[8] * var0.projectionMatrix[2] + var0.modelviewMatrix[9] * var0.projectionMatrix[6] + var0.modelviewMatrix[10] * var0.projectionMatrix[10] + var0.modelviewMatrix[11] * var0.projectionMatrix[14];
		var0.clippingMatrix[11] = var0.modelviewMatrix[8] * var0.projectionMatrix[3] + var0.modelviewMatrix[9] * var0.projectionMatrix[7] + var0.modelviewMatrix[10] * var0.projectionMatrix[11] + var0.modelviewMatrix[11] * var0.projectionMatrix[15];
		var0.clippingMatrix[12] = var0.modelviewMatrix[12] * var0.projectionMatrix[0] + var0.modelviewMatrix[13] * var0.projectionMatrix[4] + var0.modelviewMatrix[14] * var0.projectionMatrix[8] + var0.modelviewMatrix[15] * var0.projectionMatrix[12];
		var0.clippingMatrix[13] = var0.modelviewMatrix[12] * var0.projectionMatrix[1] + var0.modelviewMatrix[13] * var0.projectionMatrix[5] + var0.modelviewMatrix[14] * var0.projectionMatrix[9] + var0.modelviewMatrix[15] * var0.projectionMatrix[13];
		var0.clippingMatrix[14] = var0.modelviewMatrix[12] * var0.projectionMatrix[2] + var0.modelviewMatrix[13] * var0.projectionMatrix[6] + var0.modelviewMatrix[14] * var0.projectionMatrix[10] + var0.modelviewMatrix[15] * var0.projectionMatrix[14];
		var0.clippingMatrix[15] = var0.modelviewMatrix[12] * var0.projectionMatrix[3] + var0.modelviewMatrix[13] * var0.projectionMatrix[7] + var0.modelviewMatrix[14] * var0.projectionMatrix[11] + var0.modelviewMatrix[15] * var0.projectionMatrix[15];
		var0.frustrum[0][0] = var0.clippingMatrix[3] - var0.clippingMatrix[0];
		var0.frustrum[0][1] = var0.clippingMatrix[7] - var0.clippingMatrix[4];
		var0.frustrum[0][2] = var0.clippingMatrix[11] - var0.clippingMatrix[8];
		var0.frustrum[0][3] = var0.clippingMatrix[15] - var0.clippingMatrix[12];
		normalize(var0.frustrum, 0);
		var0.frustrum[1][0] = var0.clippingMatrix[3] + var0.clippingMatrix[0];
		var0.frustrum[1][1] = var0.clippingMatrix[7] + var0.clippingMatrix[4];
		var0.frustrum[1][2] = var0.clippingMatrix[11] + var0.clippingMatrix[8];
		var0.frustrum[1][3] = var0.clippingMatrix[15] + var0.clippingMatrix[12];
		normalize(var0.frustrum, 1);
		var0.frustrum[2][0] = var0.clippingMatrix[3] + var0.clippingMatrix[1];
		var0.frustrum[2][1] = var0.clippingMatrix[7] + var0.clippingMatrix[5];
		var0.frustrum[2][2] = var0.clippingMatrix[11] + var0.clippingMatrix[9];
		var0.frustrum[2][3] = var0.clippingMatrix[15] + var0.clippingMatrix[13];
		normalize(var0.frustrum, 2);
		var0.frustrum[3][0] = var0.clippingMatrix[3] - var0.clippingMatrix[1];
		var0.frustrum[3][1] = var0.clippingMatrix[7] - var0.clippingMatrix[5];
		var0.frustrum[3][2] = var0.clippingMatrix[11] - var0.clippingMatrix[9];
		var0.frustrum[3][3] = var0.clippingMatrix[15] - var0.clippingMatrix[13];
		normalize(var0.frustrum, 3);
		var0.frustrum[4][0] = var0.clippingMatrix[3] - var0.clippingMatrix[2];
		var0.frustrum[4][1] = var0.clippingMatrix[7] - var0.clippingMatrix[6];
		var0.frustrum[4][2] = var0.clippingMatrix[11] - var0.clippingMatrix[10];
		var0.frustrum[4][3] = var0.clippingMatrix[15] - var0.clippingMatrix[14];
		normalize(var0.frustrum, 4);
		var0.frustrum[5][0] = var0.clippingMatrix[3] + var0.clippingMatrix[2];
		var0.frustrum[5][1] = var0.clippingMatrix[7] + var0.clippingMatrix[6];
		var0.frustrum[5][2] = var0.clippingMatrix[11] + var0.clippingMatrix[10];
		var0.frustrum[5][3] = var0.clippingMatrix[15] + var0.clippingMatrix[14];
		normalize(var0.frustrum, 5);
		return instance;
	}

	private static void normalize(float[][] var0, int var1) {
		float var2 = MathHelper.sqrt_float(var0[var1][0] * var0[var1][0] + var0[var1][1] * var0[var1][1] + var0[var1][2] * var0[var1][2]);
		var0[var1][0] /= var2;
		var0[var1][1] /= var2;
		var0[var1][2] /= var2;
		var0[var1][3] /= var2;
	}
}
