package mchorse.mclib.client.gui.mclib;

import mchorse.mclib.McLib;
import mchorse.mclib.client.gui.utils.Icons;
import mchorse.mclib.client.gui.utils.keys.IKey;
import mchorse.mclib.config.gui.GuiConfig;
import mchorse.mclib.events.RegisterDashboardPanels;
import net.minecraft.client.Minecraft;

public class GuiDashboard extends GuiAbstractDashboard
{
	public static GuiDashboard dashboard;

	public GuiConfig config;

	public static GuiDashboard get()
	{
		if (dashboard == null)
		{
			dashboard = new GuiDashboard(Minecraft.getMinecraft());
		}

		return dashboard;
	}

	public GuiDashboard(Minecraft mc)
	{
		super(mc);
	}

	@Override
	protected GuiDashboardPanels createDashboard(Minecraft mc)
	{
		return new GuiDashboardPanels(mc);
	}

	@Override
	protected void registerPanels(Minecraft mc)
	{
		this.panels.registerPanel(this.config = new GuiConfig(mc, this), IKey.lang("mclib.gui.config.tooltip"), Icons.GEAR);

		if (McLib.debugPanel.get())
		{
			this.panels.registerPanel(new GuiDebugPanel(mc, this), IKey.str("Debug"), Icons.POSE);
		}

		McLib.EVENT_BUS.post(new RegisterDashboardPanels(this));

		this.panels.setPanel(this.config);
	}
}