package mchorse.mclib.client.gui.utils;

import mchorse.mclib.McLib;
import mchorse.mclib.client.gui.framework.elements.utils.GuiContext;
import mchorse.mclib.client.gui.framework.elements.utils.GuiDraw;
import mchorse.mclib.utils.ColorUtils;
import mchorse.mclib.utils.MathUtils;
import net.minecraft.client.gui.Gui;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Scrollable area
 * 
 * This class is responsible for storing information for scrollable one 
 * directional objects. 
 */
public class ScrollArea extends Area
{
    /**
     * Size of an element/item in the scroll area
     */
    public int scrollItemSize;

    /**
     * Size of the scrolling area 
     */
    public int scrollSize;

    /**
     * Scroll position 
     */
    public int scroll;

    /**
     * Whether this scroll area gets dragged 
     */
    public boolean dragging;

    /**
     * Speed of how fast shit's scrolling  
     */
    public int scrollSpeed = 10;

    /**
     * Scroll direction, used primarily in the {@link #clamp()} method 
     */
    public ScrollDirection direction = ScrollDirection.VERTICAL;

    /**
     * Whether the scrollbar should be on opposite side (default is right
     * for vertical and bottom for horizontal)
     */
    public boolean opposite;

    /**
     * Width of scroll bar
     */
    public int scrollbarWidth = -1;

    /**
     * Whether this scroll area should cancel mouse events when mouse scroll
     * reaches the end
     */
    public boolean cancelScrollEdge = false;

    public ScrollArea(int x, int y, int w, int h)
    {
        super(x, y, w, h);
    }

    public ScrollArea()
    {}

    public ScrollArea(int itemSize)
    {
        this.scrollItemSize = itemSize;
    }

    public int getScrollbarWidth()
    {
        return this.scrollbarWidth <= 0 ? McLib.scrollbarWidth.get() : this.scrollbarWidth;
    }

    public void setSize(int items)
    {
        this.scrollSize = items * this.scrollItemSize;
    }

    /**
     * Scroll by relative amount 
     */
    public void scrollBy(int x)
    {
        this.scroll += x;
        this.clamp();
    }

    /**
     * Scroll to the position in the scroll area 
     */
    public void scrollTo(int x)
    {
        this.scroll = x;
        this.clamp();
    }

    public void scrollIntoView(int x)
    {
        this.scrollIntoView(x, this.scrollItemSize, 0);
    }

    public void scrollIntoView(int x, int bottomOffset)
    {
        this.scrollIntoView(x, bottomOffset, 0);
    }

    public void scrollIntoView(int x, int bottomOffset, int topOffset)
    {
        if (this.scroll + topOffset > x)
        {
            this.scrollTo(x - topOffset);
        }
        else if (x > this.scroll + this.direction.getSide(this) - bottomOffset)
        {
            this.scrollTo(x - this.direction.getSide(this) + bottomOffset);
        }
    }

    /**
     * Clamp scroll to the bounds of the scroll size; 
     */
    public void clamp()
    {
        int size = this.direction.getSide(this);

        if (this.scrollSize <= size)
        {
            this.scroll = 0;
        }
        else
        {
            this.scroll = MathUtils.clamp(this.scroll, 0, this.scrollSize - size);
        }
    }

    /**
     * Get index of the cursor based on the {@link #scrollItemSize}.  
     */
    public int getIndex(int x, int y)
    {
        int axis = this.direction.getScroll(this, x, y);
        int index = axis / this.scrollItemSize;

        if (axis < 0)
        {
            return -1;
        }
        else if (axis > this.scrollSize)
        {
            return -2;
        }

        return index > this.scrollSize / this.scrollItemSize ? -1 : index;
    }

    /**
     * Calculates scroll bar's height 
     */
    public int getScrollBar(int size)
    {
        int maxSize = this.direction.getSide(this);

        if (this.scrollSize < size)
        {
            return 0;
        }

        return (int) ((1.0F - ((this.scrollSize - maxSize) / (float) this.scrollSize)) * size);
    }

    /* GUI code for easier manipulations */

    @SideOnly(Side.CLIENT)
    public boolean mouseClicked(GuiContext context)
    {
        return this.mouseClicked(context.mouseX, context.mouseY);
    }

    /**
     * This method should be invoked to register dragging 
     */
    public boolean mouseClicked(int x, int y)
    {
        boolean isInside = this.isInside(x, y) && this.scrollSize > this.h;

        if (isInside)
        {
            int scrollbar = this.getScrollbarWidth();

            if (this.opposite)
            {
                isInside = this.direction == ScrollDirection.VERTICAL ? x <= this.x + scrollbar : y <= this.y + scrollbar;
            }
            else
            {
                isInside = this.direction == ScrollDirection.VERTICAL ? x >= this.ex() - scrollbar : y >= this.ey() - scrollbar;
            }
        }

        if (isInside)
        {
            this.dragging = true;
        }

        return isInside;
    }

    @SideOnly(Side.CLIENT)
    public boolean mouseScroll(GuiContext context)
    {
        return this.mouseScroll(context.mouseX, context.mouseY, context.mouseWheel);
    }

    /**
     * This method should be invoked when mouse wheel is scrolling 
     */
    public boolean mouseScroll(int x, int y, int scroll)
    {
        boolean isInside = this.isInside(x, y);
        int lastScroll = this.scroll;

        if (isInside)
        {
            this.scrollBy((int) Math.copySign(this.scrollSpeed, scroll));
        }

        return isInside && (this.cancelScrollEdge || lastScroll != this.scroll);
    }

    @SideOnly(Side.CLIENT)
    public void mouseReleased(GuiContext context)
    {
        this.mouseReleased(context.mouseX, context.mouseY);
    }

    /**
     * When mouse button gets released
     */
    public void mouseReleased(int x, int y)
    {
        this.dragging = false;
    }

    @SideOnly(Side.CLIENT)
    public void drag(GuiContext context)
    {
        this.drag(context.mouseX, context.mouseY);
    }

    /**
     * This should be invoked in a drawing or and update method. It's 
     * responsible for scrolling through this view when dragging. 
     */
    public void drag(int x, int y)
    {
        if (this.dragging)
        {
            float progress = this.direction.getProgress(this, x, y);

            this.scrollTo((int) (progress * (this.scrollSize - this.direction.getSide(this) + this.getScrollbarWidth())));
        }
    }

    /**
     * This method is responsible for drawing a scroll bar 
     */
    @SideOnly(Side.CLIENT)
    public void drawScrollbar()
    {
        int side = this.direction.getSide(this);

        if (this.scrollSize <= side)
        {
            return;
        }

        int scrollbar = this.getScrollbarWidth();
        int h = this.getScrollBar(side / 2);
        int x = this.opposite ? this.x : this.ex() - scrollbar;
        /* Sometimes I don't understand how I come up with such clever
         * formulas, but it's all ratios, y'all */
        int y = this.y + (int) ((this.scroll / (float) (this.scrollSize - this.h)) * (this.h - h));
        int rx = x + scrollbar;
        int ry = y + h;

        if (this.direction == ScrollDirection.HORIZONTAL)
        {
            y = this.opposite ? this.y : this.ey() - scrollbar;
            x = this.x + (int) ((this.scroll / (float) (this.scrollSize - this.w)) * (this.w - h));
            rx = x + h;
            ry = y + scrollbar;
        }

        if (McLib.scrollbarFlat.get())
        {
            Gui.drawRect(x, y, rx, ry, -6250336);
        }
        else
        {
            int color = McLib.scrollbarShadow.get();

            GuiDraw.drawDropShadow(x, y, rx, ry, 5, color, ColorUtils.setAlpha(color, 0F));

            Gui.drawRect(x, y, rx, ry, 0xffeeeeee);
            Gui.drawRect(x + 1, y + 1, rx, ry, 0xff666666);
            Gui.drawRect(x + 1, y + 1, rx - 1, ry - 1, 0xffaaaaaa);
        }
    }
}