package mchorse.mclib.client.gui.mclib;

import mchorse.mclib.client.gui.framework.elements.GuiElement;
import mchorse.mclib.client.gui.framework.elements.utils.GuiContext;
import mchorse.mclib.client.gui.framework.elements.utils.GuiDraw;
import net.minecraft.client.Minecraft;

public class GuiDashboardPanel<T extends GuiAbstractDashboard> extends GuiElement
{
	public final T dashboard;
	public boolean needsBackground = true;

	public GuiDashboardPanel(Minecraft mc, T dashboard)
	{
		super(mc);

		this.dashboard = dashboard;
		this.markContainer();
	}

	public void appear()
	{}

	public void disappear()
	{}

	public void open()
	{}

	public void close()
	{}

	public void drawBackground(GuiContext context)
	{
		if (this.needsBackground)
		{
			GuiDraw.drawCustomBackground(this.area.x, this.area.y, this.area.w, this.area.h);
		}
		else
		{
			int h = this.area.h / 8;

			this.drawGradientRect(this.area.x, this.area.y, this.area.ex(), this.area.y + h, 0x44000000, 0x00000000);
			this.drawGradientRect(this.area.x, this.area.ey() - h, this.area.ex(), this.area.ey(), 0x00000000, 0x44000000);
		}
	}
}