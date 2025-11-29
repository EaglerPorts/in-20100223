package net.minecraft.client;

import net.lax1dude.eaglercraft.EagRuntime;
import net.lax1dude.eaglercraft.EagUtils;
import net.lax1dude.eaglercraft.internal.PlatformApplication;
import net.lax1dude.eaglercraft.internal.buffer.IntBuffer;
import net.lax1dude.eaglercraft.internal.vfs2.VFile2;
import net.minecraft.client.controller.PlayerController;
import net.minecraft.client.controller.PlayerControllerCreative;
import net.minecraft.client.controller.PlayerControllerSP;
import net.minecraft.client.effect.EffectRenderer;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiErrorScreen;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.container.GuiInventory;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.player.EntityPlayerSP;
import net.minecraft.client.player.MovementInputFromOptions;
import net.minecraft.client.render.EntityRenderer;
import net.minecraft.client.render.RenderEngine;
import net.minecraft.client.render.RenderGlobal;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.texture.TextureFlamesFX;
import net.minecraft.client.render.texture.TextureGearsFX;
import net.minecraft.client.render.texture.TextureLavaFX;
import net.minecraft.client.render.texture.TextureWaterFX;
import net.minecraft.client.render.texture.TextureWaterFlowFX;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.entity.EntityLiving;
import net.minecraft.game.entity.player.InventoryPlayer;
import net.minecraft.game.item.Item;
import net.minecraft.game.item.ItemStack;
import net.minecraft.game.level.World;
import net.minecraft.game.level.block.Block;
import net.minecraft.game.level.generator.LevelGenerator;
import net.minecraft.game.physics.MovingObjectPosition;
import util.GLAllocation;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import dev.colbster937.eaglercraft.LevelUtils;
import dev.colbster937.eaglercraft.gui.GuiChat;

public final class Minecraft implements Runnable {
	public PlayerController playerController = new PlayerControllerSP(this);
	public int displayWidth;
	public int displayHeight;
	private Timer timer = new Timer(20.0F);
	public World theWorld;
	public RenderGlobal renderGlobal;
	public EntityPlayerSP thePlayer;
	public EffectRenderer effectRenderer;
	public Session session = null;
	public String minecraftUri;
	public volatile boolean isGamePaused = false;
	public RenderEngine renderEngine;
	public FontRenderer fontRenderer;
	public GuiScreen currentScreen = null;
	public LoadingScreenRenderer loadingScreen = new LoadingScreenRenderer(this);
	public EntityRenderer entityRenderer = new EntityRenderer(this);
	private int ticksRan = 0;
	private int leftClickCounter = 0;;
	public String loadMapUser = null;
	public int loadMapID = 0;
	public GuiIngame ingameGUI;
	public boolean skipRenderWorld = false;
	public MovingObjectPosition objectMouseOver;
	public GameSettings options;
	public SoundManager sndManager;
	public MouseHelper mouseHelper;
	public VFile2 mcDataDir;
	private String server;
	private TextureWaterFX textureWaterFX;
	private TextureLavaFX textureLavaFX;
	volatile boolean running;
	public String debug;
	public int fps;
	public boolean inventoryScreen;
	private int prevFrameTime;
	public boolean inGameHasFocus;

	private static Minecraft mc;

	public Minecraft() {
		new ModelBiped(0.0F);
		this.objectMouseOver = null;
		this.sndManager = new SoundManager();
		this.server = null;
		this.textureWaterFX = new TextureWaterFX();
		this.textureLavaFX = new TextureLavaFX();
		this.running = false;
		this.debug = "";
		this.inventoryScreen = false;
		this.prevFrameTime = 0;
		this.inGameHasFocus = false;
		this.displayWidth = Display.getWidth();
		this.displayHeight = Display.getHeight();

		this.session = new Session("Player", "");
		LevelUtils.init(this);
		mc = this;
	}

	public static Minecraft getMinecraft() {
		return mc;
	}

	public void updateDisplay() {
		if (Display.isVSyncSupported()) {
			if (this.theWorld == null || this.currentScreen != null) Display.setVSync(true);
			else Display.setVSync(this.options.vsync);
		}
		Display.update();
		if (Display.wasResized()) this.resize(Display.getWidth(), Display.getHeight());
	}

	public final void setServer(String var1, int var2) {
		this.server = var1;
	}

	public final void displayGuiScreen(GuiScreen var1) {
		if(!(this.currentScreen instanceof GuiErrorScreen)) {
			if(this.currentScreen != null) {
				this.currentScreen.onGuiClosed();
			}

			if(var1 == null && this.theWorld == null) {
				var1 = new GuiMainMenu();
			} else if(var1 == null && this.thePlayer.health <= 0) {
				var1 = new GuiGameOver();
			}

			this.currentScreen = (GuiScreen)var1;
			if(var1 != null) {
				this.inputLock();
				ScaledResolution var2 = new ScaledResolution(this.displayWidth, this.displayHeight);
				int var3 = var2.getScaledWidth();
				int var4 = var2.getScaledHeight();
				((GuiScreen)var1).setWorldAndResolution(this, var3, var4);
				this.skipRenderWorld = false;
			} else {
				this.setIngameFocus();
			}
		}
	}

	public final void shutdownMinecraftApplet() {
		this.sndManager.closeMinecraft();

	}

	public final void run() {
		this.running = true;

		try {
			Display.setTitle("Minecraft Indev");

			IntBuffer var24;

			System.out.println("LWJGL version: " + EagRuntime.getVersion());
			System.out.println("GL RENDERER: " + GL11.glGetString(GL11.GL_RENDERER));
			System.out.println("GL VENDOR: " + GL11.glGetString(GL11.GL_VENDOR));
			System.out.println("GL VERSION: " + GL11.glGetString(GL11.GL_VERSION));

			this.mouseHelper = new MouseHelper();

			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glShadeModel(GL11.GL_SMOOTH);
			GL11.glClearDepth(1.0D);
			GL11.glEnable(GL11.GL_DEPTH_TEST);
			GL11.glDepthFunc(GL11.GL_LEQUAL);
			GL11.glEnable(GL11.GL_ALPHA_TEST);
			GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
			GL11.glCullFace(GL11.GL_BACK);
			GL11.glMatrixMode(GL11.GL_PROJECTION);
			GL11.glLoadIdentity();
			GL11.glMatrixMode(GL11.GL_MODELVIEW);

			this.mcDataDir = new VFile2("minecraft/");
			this.options = new GameSettings(this, this.mcDataDir);
			this.sndManager.loadSoundSettings(this.options);
			this.renderEngine = new RenderEngine(this.options);
			this.renderEngine.registerTextureFX(this.textureLavaFX);
			this.renderEngine.registerTextureFX(this.textureWaterFX);
			this.renderEngine.registerTextureFX(new TextureWaterFlowFX());
			this.renderEngine.registerTextureFX(new TextureFlamesFX(0));
			this.renderEngine.registerTextureFX(new TextureFlamesFX(1));
			this.renderEngine.registerTextureFX(new TextureGearsFX(0));
			this.renderEngine.registerTextureFX(new TextureGearsFX(1));
			this.fontRenderer = new FontRenderer(this.options, "/default.png", this.renderEngine);
			var24 = GLAllocation.createIntBuffer(256);
			var24.clear().limit(256);
			this.renderGlobal = new RenderGlobal(this, this.renderEngine);
			GL11.glViewport(0, 0, this.displayWidth, this.displayHeight);
			if(this.server != null && this.session != null) {
				World var31 = new World();
				var31.generate(8, 8, 8, new byte[512], new byte[512]);
				this.setLevel(var31);
			} else if(this.theWorld == null) {
				this.displayGuiScreen(new GuiMainMenu());
			}

			this.effectRenderer = new EffectRenderer(this.theWorld, this.renderEngine);

			this.ingameGUI = new GuiIngame(this);
		} catch (Exception var22) {
			var22.printStackTrace();
			EagRuntime.showPopup("Failed to start Minecraft: " + var22.toString());
			return;
		}

		long var23 = System.currentTimeMillis();
		int var28 = 0;

		try {
			while(this.running) {
				if(this.theWorld != null) {
					this.theWorld.updateLighting();
				}

				if(Display.isCloseRequested()) {
					this.running = false;
				}

				try {
					if(this.isGamePaused) {
						float var29 = this.timer.renderPartialTicks;
						this.timer.updateTimer();
						this.timer.renderPartialTicks = var29;
					} else {
						this.timer.updateTimer();
					}

					for(int var30 = 0; var30 < this.timer.elapsedTicks; ++var30) {
						++this.ticksRan;
						this.runTick();
					}

					this.sndManager.setListener(this.thePlayer, this.timer.renderPartialTicks);
					GL11.glEnable(GL11.GL_TEXTURE_2D);
					this.playerController.setPartialTime(this.timer.renderPartialTicks);
					this.entityRenderer.updateCameraAndRender(this.timer.renderPartialTicks);

					this.screenshotListener();
					this.updateDisplay();

					++var28;
					this.isGamePaused = this.currentScreen != null && this.currentScreen.doesGuiPauseGame();
				} catch (Exception var18) {
					this.displayGuiScreen(new GuiErrorScreen("Client error", "The game broke! [" + var18 + "]"));
					var18.printStackTrace();
					return;
				}

				while(System.currentTimeMillis() >= var23 + 1000L) {
					this.debug = var28 + " fps, " + WorldRenderer.chunksUpdated + " chunk updates";
					this.fps = var28;
					WorldRenderer.chunksUpdated = 0;
					var23 += 1000L;
					var28 = 0;
				}
			}

			return;
		} catch (MinecraftError var19) {
			return;
		} catch (Exception var20) {
			var20.printStackTrace();
		} finally {
			this.shutdownMinecraftApplet();
		}

	}

	public final void setIngameFocus() {
		if(Display.isActive()) {
			if(!this.inventoryScreen) {
				this.inventoryScreen = true;
				this.mouseHelper.grabMouseCursor();
				this.displayGuiScreen((GuiScreen)null);
				this.prevFrameTime = this.ticksRan + 10000;
			}
		}
	}

	private void inputLock() {
		if(this.inventoryScreen) {
			if(this.thePlayer != null) {
				EntityPlayerSP var1 = this.thePlayer;
				var1.movementInput.resetKeyState();
			}

			this.inventoryScreen = false;

			Mouse.setGrabbed(true);
		}
	}

	public final void displayInGameMenu() {
		if(this.currentScreen == null) {
			this.displayGuiScreen(new GuiIngameMenu());
		}
	}

	private void clickMouse(int var1) {
		if(var1 != 0 || this.leftClickCounter <= 0) {
			if(var1 == 0) {
				this.entityRenderer.itemRenderer.equippedItemRender();
			}

			ItemStack var2;
			int var3;
			World var5;
			if(var1 == 1) {
				var2 = this.thePlayer.inventory.getCurrentItem();
				if(var2 != null) {
					var3 = var2.stackSize;
					EntityPlayerSP var7 = this.thePlayer;
					var5 = this.theWorld;
					ItemStack var4 = var2.getItem().onItemRightClick(var2, var5, var7);
					if(var4 != var2 || var4 != null && var4.stackSize != var3) {
						this.thePlayer.inventory.mainInventory[this.thePlayer.inventory.currentItem] = var4;
						this.entityRenderer.itemRenderer.resetEquippedProgress();
						if(var4.stackSize == 0) {
							this.thePlayer.inventory.mainInventory[this.thePlayer.inventory.currentItem] = null;
						}
					}
				}
			}

			if(this.objectMouseOver == null) {
				if(var1 == 0 && !(this.playerController instanceof PlayerControllerCreative)) {
					this.leftClickCounter = 10;
				}

			} else {
				ItemStack var9;
				if(this.objectMouseOver.typeOfHit == 1) {
					if(var1 == 0) {
						Entity var14 = this.objectMouseOver.entityHit;
						EntityPlayerSP var12 = this.thePlayer;
						InventoryPlayer var11 = var12.inventory;
						var9 = var11.getStackInSlot(var11.currentItem);
						int var17 = var9 != null ? Item.itemsList[var9.itemID].getDamageVsEntity() : 1;
						if(var17 > 0) {
							var14.attackEntityFrom(var12, var17);
							var2 = var12.inventory.getCurrentItem();
							if(var2 != null && var14 instanceof EntityLiving) {
								EntityLiving var8 = (EntityLiving)var14;
								Item.itemsList[var2.itemID].hitEntity(var2);
								if(var2.stackSize <= 0) {
									var12.destroyCurrentEquippedItem();
								}
							}
						}

						return;
					}
				} else if(this.objectMouseOver.typeOfHit == 0) {
					int var10 = this.objectMouseOver.blockX;
					var3 = this.objectMouseOver.blockY;
					int var13 = this.objectMouseOver.blockZ;
					int var15 = this.objectMouseOver.sideHit;
					Block var6 = Block.blocksList[this.theWorld.getBlockId(var10, var3, var13)];
					if(var1 == 0) {
						this.theWorld.extinguishFire(var10, var3, var13, this.objectMouseOver.sideHit);
						if(var6 != Block.bedrock) {
							this.playerController.clickBlock(var10, var3, var13);
							return;
						}
					} else {
						var9 = this.thePlayer.inventory.getCurrentItem();
						int var16 = this.theWorld.getBlockId(var10, var3, var13);
						if(var16 > 0 && Block.blocksList[var16].blockActivated(this.theWorld, var10, var3, var13, this.thePlayer)) {
							return;
						}

						if(var9 == null) {
							return;
						}

						var16 = var9.stackSize;
						int var18 = var15;
						var5 = this.theWorld;
						if(var9.getItem().onItemUse(var9, var5, var10, var3, var13, var18)) {
							this.entityRenderer.itemRenderer.equippedItemRender();
						}

						if(var9.stackSize == 0) {
							this.thePlayer.inventory.mainInventory[this.thePlayer.inventory.currentItem] = null;
							return;
						}

						if(var9.stackSize != var16) {
							this.entityRenderer.itemRenderer.equipAnimationSpeed();
						}
					}
				}

			}
		}
	}

	private void resize(int var1, int var2) {
		this.displayWidth = var1;
		this.displayHeight = var2;
		if(this.currentScreen != null) {
			ScaledResolution var3 = new ScaledResolution(var1, var2);
			var2 = var3.getScaledWidth();
			var1 = var3.getScaledHeight();
			this.currentScreen.setWorldAndResolution(this, var2, var1);
		}

	}

	private void runTick() {
		this.ingameGUI.addChatMessage();
		if(!this.isGamePaused && this.theWorld != null) {
			this.playerController.onUpdate();
		}

		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.renderEngine.getTexture("/terrain.png"));
		if(!this.isGamePaused) {
			this.renderEngine.updateDynamicTextures();
		}

		if(this.currentScreen == null && this.thePlayer != null && this.thePlayer.health <= 0) {
			this.displayGuiScreen((GuiScreen)null);
		}

		if(this.currentScreen == null || this.currentScreen.allowUserInput) {
			label286:
			while(true) {
				while(true) {
					int var1;
					int var2;
					while(Mouse.next()) {
						var1 = Mouse.getEventDWheel();
						if(var1 != 0) {
							var2 = var1;
							InventoryPlayer var5 = this.thePlayer.inventory;
							if(var1 > 0) {
								var2 = 1;
							}

							if(var2 < 0) {
								var2 = -1;
							}

							for(var5.currentItem -= var2; var5.currentItem < 0; var5.currentItem += 9) {
							}

							while(var5.currentItem >= 9) {
								var5.currentItem -= 9;
							}
						}

						if(this.currentScreen == null) {
							if(!this.inventoryScreen && Mouse.getEventButtonState()) {
								this.setIngameFocus();
							} else {
								if(Mouse.getEventButton() == 0 && Mouse.getEventButtonState()) {
									this.clickMouse(0);
									this.prevFrameTime = this.ticksRan;
								}

								if(Mouse.getEventButton() == 1 && Mouse.getEventButtonState()) {
									this.clickMouse(1);
									this.prevFrameTime = this.ticksRan;
								}

								if(Mouse.getEventButton() == 2 && Mouse.getEventButtonState() && this.objectMouseOver != null) {
									var2 = this.theWorld.getBlockId(this.objectMouseOver.blockX, this.objectMouseOver.blockY, this.objectMouseOver.blockZ);
									if(var2 == Block.grass.blockID) {
										var2 = Block.dirt.blockID;
									}

									if(var2 == Block.stairDouble.blockID) {
										var2 = Block.stairSingle.blockID;
									}

									if(var2 == Block.bedrock.blockID) {
										var2 = Block.stone.blockID;
									}

									this.thePlayer.inventory.getFirstEmptyStack(var2);
								}
							}
						} else if(this.currentScreen != null) {
							this.currentScreen.handleMouseInput();
						}
					}

					if(this.leftClickCounter > 0) {
						--this.leftClickCounter;
					}

					while(true) {
						while(true) {
							do {
								boolean var3;
								if(!Keyboard.next()) {
									if(this.currentScreen == null) {
										if(Mouse.isButtonDown(0) && (float)(this.ticksRan - this.prevFrameTime) >= this.timer.ticksPerSecond / 4.0F && this.inventoryScreen) {
											this.clickMouse(0);
											this.prevFrameTime = this.ticksRan;
										}

										if(Mouse.isButtonDown(1) && (float)(this.ticksRan - this.prevFrameTime) >= this.timer.ticksPerSecond / 4.0F && this.inventoryScreen) {
											this.clickMouse(1);
											this.prevFrameTime = this.ticksRan;
										}
									}

									var3 = this.currentScreen == null && Mouse.isButtonDown(0) && this.inventoryScreen;
									boolean var9 = false;
									if(!this.playerController.isInTestMode && this.leftClickCounter <= 0) {
										if(var3 && this.objectMouseOver != null && this.objectMouseOver.typeOfHit == 0) {
											var2 = this.objectMouseOver.blockX;
											int var8 = this.objectMouseOver.blockY;
											int var4 = this.objectMouseOver.blockZ;
											this.playerController.sendBlockRemoving(var2, var8, var4, this.objectMouseOver.sideHit);
											this.effectRenderer.addBlockHitEffects(var2, var8, var4, this.objectMouseOver.sideHit);
										} else {
											this.playerController.resetBlockRemoving();
										}
									}
									break label286;
								}

								EntityPlayerSP var10000 = this.thePlayer;
								int var10001 = Keyboard.getEventKey();
								var3 = Keyboard.getEventKeyState();
								var2 = var10001;
								EntityPlayerSP var6 = var10000;
								var6.movementInput.checkKeyForMovementInput(var2, var3);
							} while(!Keyboard.getEventKeyState());

							if(Keyboard.getEventKey() == Keyboard.KEY_F11) {
								Display.toggleFullscreen();
							} else {
								if(this.currentScreen != null) {
									this.currentScreen.handleKeyboardInput();
								} else {
									if (this.theWorld != null) {
										this.setIngameFocus();
									}
									if(Keyboard.getEventKey() == Keyboard.KEY_ESCAPE || Keyboard.getEventKey() == Keyboard.KEY_GRAVE) {
										this.displayInGameMenu();
									}

									// if(Keyboard.getEventKey() == Keyboard.KEY_F7) {
									// 	this.entityRenderer.grabLargeScreenshot();
									// }

									/* if(this.playerController instanceof PlayerControllerCreative) {
										if(Keyboard.getEventKey() == this.options.keyBindLoad.keyCode) {
											this.thePlayer.preparePlayerToSpawn();
										}

										if(Keyboard.getEventKey() == this.options.keyBindSave.keyCode) {
											this.theWorld.setSpawnLocation((int)this.thePlayer.posX, (int)this.thePlayer.posY, (int)this.thePlayer.posZ, this.thePlayer.rotationYaw);
											this.thePlayer.preparePlayerToSpawn();
										}
									} */

									if(Keyboard.getEventKey() == Keyboard.KEY_F5) {
										this.options.thirdPersonView = !this.options.thirdPersonView;
									}

									if(Keyboard.getEventKey() == this.options.keyBindInventory.keyCode) {
										this.displayGuiScreen(new GuiInventory(this.thePlayer.inventory));
									}

									if(Keyboard.getEventKey() == this.options.keyBindDrop.keyCode) {
										this.thePlayer.dropPlayerItemWithRandomChoice(this.thePlayer.inventory.decrStackSize(this.thePlayer.inventory.currentItem, 1), false);
									}

									if(Keyboard.getEventKey() == this.options.keyBindChat.keyCode) {
										this.displayGuiScreen(new GuiChat());
									}

									if(Keyboard.getEventKey() == this.options.keyBindCommand.keyCode) {
										this.displayGuiScreen(new GuiChat());
										if (this.currentScreen instanceof GuiChat) ((GuiChat) this.currentScreen).setMessage("/");
									}
								}

								for(var1 = 0; var1 < 9; ++var1) {
									if(Keyboard.getEventKey() == var1 + 2) {
										this.thePlayer.inventory.currentItem = var1;
									}
								}
							}
						}
					}
				}
			}
		}

		if(this.currentScreen != null) {
			this.prevFrameTime = this.ticksRan + 10000;
		}

		if(this.currentScreen != null) {
			GuiScreen var7 = this.currentScreen;

			while(Mouse.next()) {
				var7.handleMouseInput();
			}

			while(Keyboard.next()) {
				var7.handleKeyboardInput();
			}

			if(this.currentScreen != null) {
				this.currentScreen.updateScreen();
			}
		}

		if(this.theWorld != null) {
			this.theWorld.difficultySetting = this.options.difficulty;
			if(!this.isGamePaused) {
				this.entityRenderer.updateRenderer();
			}

			if(!this.isGamePaused) {
				this.renderGlobal.updateClouds();
			}

			if(!this.isGamePaused) {
				this.theWorld.updateEntities();
			}

			if(!this.isGamePaused) {
				this.theWorld.tick();
			}

			if(!this.isGamePaused) {
				this.theWorld.randomDisplayUpdates((int)this.thePlayer.posX, (int)this.thePlayer.posY, (int)this.thePlayer.posZ);
			}

			if(!this.isGamePaused) {
				this.effectRenderer.updateEffects();
			}
		}

	}

	public final void generateLevel(int var1, int var2, int var3, int var4) {
		this.setLevel((World)null);
		System.gc();
		String var5 = this.session != null ? this.session.username : "anonymous";
		LevelGenerator var6 = new LevelGenerator(this.loadingScreen);
		var6.islandGen = var3 == 1;
		var6.floatingGen = var3 == 2;
		var6.flatGen = var3 == 3;
		var6.levelType = var4;
		var1 = 128 << var1;
		var3 = var1;
		short var8 = 64;
		if(var2 == 1) {
			var1 /= 2;
			var3 <<= 1;
		} else if(var2 == 2) {
			var1 /= 2;
			var3 = var1;
			var8 = 256;
		}

		World var7 = var6.generate(var5, var1, var3, var8);
		this.setLevel(var7);
	}

	public final void setLevel(World var1) {
		this.theWorld = var1;

		if(this.theWorld != null) {
			this.theWorld.setLevel();
		}

		if(var1 != null) {
			var1.load();
			this.playerController.onWorldChange(var1);
			if(var1.playerEntity instanceof EntityPlayerSP) {
				this.thePlayer = (EntityPlayerSP)var1.playerEntity;
			} else {
				this.thePlayer = new EntityPlayerSP(this, var1, this.session);
				this.thePlayer.preparePlayerToSpawn();
				var1.spawnEntityInWorld(this.thePlayer);
				var1.playerEntity = this.thePlayer;
			}

			if(this.thePlayer != null) {
				this.thePlayer.movementInput = new MovementInputFromOptions(this.options);
				this.playerController.onRespawn(this.thePlayer);
			}

			if(this.renderGlobal != null) {
				this.renderGlobal.changeWorld(var1);
			}

			if(this.effectRenderer != null) {
				this.effectRenderer.clearEffects(var1);
			}

			this.textureWaterFX.textureId = 0;
			this.textureLavaFX.textureId = 0;
			int var4 = this.renderEngine.getTexture("/water.png");
			if(var1.defaultFluid == Block.waterMoving.blockID) {
				this.textureWaterFX.textureId = var4;
			} else {
				this.textureLavaFX.textureId = var4;
			}
		}

		System.gc();
	}

	private boolean isTakingScreenshot = false;

	private void screenshotListener() {
		if(Keyboard.isKeyDown(Keyboard.KEY_F2)) {
			if(!this.isTakingScreenshot) {
				this.isTakingScreenshot = true;
				PlatformApplication.saveScreenshot();
			}
		} else {
			this.isTakingScreenshot = false;
		}
	}
}
