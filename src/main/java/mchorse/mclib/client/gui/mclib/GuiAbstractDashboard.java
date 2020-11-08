package mchorse.mclib.client.gui.mclib;

import mchorse.mclib.client.gui.framework.GuiBase;
import net.minecraft.client.Minecraft;

public abstract class GuiAbstractDashboard extends GuiBase
{
	public GuiDashboardPanels panels;

	protected boolean wasClosed = true;

	public GuiAbstractDashboard(Minecraft mc)
	{
		this.panels = this.createDashboard(mc);

		this.panels.flex().relative(this.viewport).wh(1F, 1F);
		this.registerPanels(mc);

		this.root.add(this.panels);
	}

	protected abstract GuiDashboardPanels createDashboard(Minecraft mc  );

	protected abstract void registerPanels(Minecraft mc);

	@Override
	public boolean doesGuiPauseGame()
	{
		return false;
	}

	@Override
	public void onGuiClosed()
	{
		this.close();
		super.onGuiClosed();
	}

	private void close()
	{
		this.panels.close();
		this.wasClosed = true;
	}

	@Override
	public void setWorldAndResolution(Minecraft mc, int width, int height)
	{
		if (this.wasClosed)
		{
			this.wasClosed = false;
			this.panels.open();
			this.panels.setPanel(this.panels.view.delegate);
		}

		super.setWorldAndResolution(mc, width, height);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		if (this.panels.view.delegate != null && this.panels.canBeSeen())
		{
			this.panels.view.delegate.drawBackground(this.context);
		}

		super.drawScreen(mouseX, mouseY, partialTicks);
	}
}