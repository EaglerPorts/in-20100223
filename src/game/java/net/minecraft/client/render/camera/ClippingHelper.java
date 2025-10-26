package net.minecraft.client.render.camera;

public class ClippingHelper {
	public float[][] frustrum = new float[16][16];
	public float[] projectionMatrix = new float[16];
	public float[] modelviewMatrix = new float[16];
	public float[] clippingMatrix = new float[16];
}
