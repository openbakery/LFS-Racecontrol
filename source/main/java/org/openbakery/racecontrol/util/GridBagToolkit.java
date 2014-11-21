package org.openbakery.racecontrol.util;

import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

public class GridBagToolkit {
	
	/**
	 *  Resize the component both horizontally and vertically.
	 *
	 * @see    java.awt.GridBagConstraints#BOTH
	 */
	public final static int BOTH = GridBagConstraints.BOTH;
	/**
	 *  Put the component in the center of its display area.
	 *
	 * @see    java.awt.GridBagConstraints#CENTER
	 */
	public final static int CENTER = GridBagConstraints.CENTER;
	/**
	 *  Put the component on the right side of its display area, centered
	 *  vertically.
	 *
	 * @see    java.awt.GridBagConstraints#EAST
	 */
	public final static int EAST = GridBagConstraints.EAST;
	/**
	 *  Place the component in the corner of its display area where the first line
	 *  of text on a page would normally end for the current ComponentOrienation.
	 *
	 * @see    java.awt.GridBagConstraints#FIRST_LINE_END
	 */
	public final static int FIRST_LINE_END = GridBagConstraints.FIRST_LINE_END;
	/**
	 *  Place the component in the corner of its display area where the first line
	 *  of text on a page would normally begin for the current ComponentOrienation.
	 *
	 * @see    java.awt.GridBagConstraints#FIRST_LINE_START
	 */
	public final static int FIRST_LINE_START = GridBagConstraints.FIRST_LINE_START;
	/**
	 *  Resize the component horizontally but not vertically.
	 *
	 * @see    java.awt.GridBagConstraints#HORIZONTAL
	 */
	public final static int HORIZONTAL = GridBagConstraints.HORIZONTAL;
	/**
	 *  Place the component in the corner of its display area where the last line
	 *  of text on a page would normally end for the current ComponentOrienation.
	 *
	 * @see    java.awt.GridBagConstraints#LAST_LINE_END
	 */
	public final static int LAST_LINE_END = GridBagConstraints.LAST_LINE_END;
	/**
	 *  Place the component in the corner of its display area where the last line
	 *  of text on a page would normally start for the current ComponentOrienation.
	 *
	 * @see    java.awt.GridBagConstraints#LAST_LINE_START
	 */
	public final static int LAST_LINE_START = GridBagConstraints.LAST_LINE_START;
	/**
	 *  Place the component centered along the edge of its display area where lines
	 *  of text would normally end for the current ComponentOrienation.
	 *
	 * @see    java.awt.GridBagConstraints#LAST_LINE_END
	 */
	public final static int LINE_END = GridBagConstraints.LINE_END;
	/**
	 *  Place the component centered along the edge of its display area where lines
	 *  of text would normally begin for the current ComponentOrienation.
	 *
	 * @see    java.awt.GridBagConstraints#LINE_START
	 */
	public final static int LINE_START = GridBagConstraints.LINE_START;
	/**
	 *  Do not resize the component.
	 *
	 * @see    java.awt.GridBagConstraints#NONE
	 */
	public final static int NONE = GridBagConstraints.NONE;
	/**
	 *  Put the component at the top of its display area, centered horizontally.
	 *
	 * @see    java.awt.GridBagConstraints#NONE
	 */
	public final static int NORTH = GridBagConstraints.NORTH;
	/**
	 *  Put the component at the top-right corner of its display area.
	 *
	 * @see    java.awt.GridBagConstraints#NORTHEAST
	 */
	public final static int NORTHEAST = GridBagConstraints.NORTHEAST;
	/**
	 *  Put the component at the top-left corner of its display area.
	 *
	 * @see    java.awt.GridBagConstraints#NORTHWEST
	 */
	public final static int NORTHWEST = GridBagConstraints.NORTHWEST;
	/**
	 *  Place the component centered along the edge of its display area associated
	 *  with the end of a page for the current ComponentOrienation.
	 *
	 * @see    java.awt.GridBagConstraints#PAGE_END
	 */
	public final static int PAGE_END = GridBagConstraints.PAGE_END;
	/**
	 *  Place the component centered along the edge of its display area associated
	 *  with the start of a page for the current ComponentOrienation.
	 *
	 * @see    java.awt.GridBagConstraints#PAGE_START
	 */
	public final static int PAGE_START = GridBagConstraints.PAGE_START;
	/**
	 *  Specifies that this component is the next-to-last component in its column
	 *  or row (gridwidth, gridheight), or that this component be placed next to
	 *  the previously added component (gridx, gridy).
	 *
	 * @see    java.awt.GridBagConstraints#RELATIVE
	 */
	public final static int RELATIVE = GridBagConstraints.RELATIVE;
	/**
	 *  Specifies that this component is the last component in its column or row.
	 *
	 * @see    java.awt.GridBagConstraints#REMAINDER
	 */
	public final static int REMAINDER = GridBagConstraints.REMAINDER;
	/**
	 *  Put the component at the bottom of its display area, centered horizontally.
	 *
	 * @see    java.awt.GridBagConstraints#SOUTH
	 */
	public final static int SOUTH = GridBagConstraints.SOUTH;
	/**
	 *  Put the component at the bottom-right corner of its display area.
	 *
	 * @see    java.awt.GridBagConstraints#SOUTHEAST
	 */
	public final static int SOUTHEAST = GridBagConstraints.SOUTHEAST;
	/**
	 *  Put the component at the bottom-left corner of its display area.
	 *
	 * @see    java.awt.GridBagConstraints#NORTHWEST
	 */
	public final static int SOUTHWEST = GridBagConstraints.SOUTHWEST;
	/**
	 *  Resize the component vertically but not horizontally.
	 *
	 * @see    java.awt.GridBagConstraints#VERTICAL
	 */
	public final static int VERTICAL = GridBagConstraints.VERTICAL;
	/**
	 *  Put the component on the left side of its display area, centered
	 *  vertically.
	 *
	 * @see    java.awt.GridBagConstraints#WEST
	 */
	public final static int WEST = GridBagConstraints.WEST;

	private static GridBagToolkit that;
	private GridBagConstraints constraints;
	private Insets defaultInsets;


	/**
	 *  Conststructor
	 */
	private GridBagToolkit() {
		constraints = new GridBagConstraints();
		defaultInsets = new Insets(0, 0, 0, 0);
	}


	/**
	 *  Method to get the GridBagTool instance. This class is a singleton.
	 *
	 * @return    the GridBagTool instance
	 */
	public static GridBagToolkit getInstance() {
		if (that == null) {
			that = new GridBagToolkit();
		}
		return that;
	}


	/**
	 *  Set the default insets.
	 *
	 * @param  defaultInsets  The new defaultInsets value
	 */
	public void setDefaultInsets(Insets defaultInsets) {
		this.defaultInsets = defaultInsets;
	}


	/**
	 *  Get the default insets.
	 *
	 * @return    the default insets
	 */
	public Insets getDefaultInsets() {
		return defaultInsets;
	}


	/**
	 *  Addes the specified component to the specified container.
	 *
	 * @param  container  the container
	 * @param  component  the component to be added
	 * @param  gridx      the gridx parameter
	 * @param  gridy      the gridy parameter
	 * @see               java.awt.GridBagConstraints#gridx
	 * @see               java.awt.GridBagConstraints#gridy
	 */
	public void add(
			Container container,
			Component component,
			int gridx,
			int gridy) {
		add(container, component, gridx, gridy, 1, 1, 0, 0, NONE, NORTHWEST, 0, 0, defaultInsets);
	}


	/**
	 *  Addes the specified component to the specified container.
	 *
	 * @param  container   the container
	 * @param  component   the component to be added
	 * @param  gridx       the gridx parameter
	 * @param  gridy       the gridy parameter
	 * @param  gridwidth   the gridwidth parameter
	 * @param  gridheight  the gridheight parameter
	 * @see                java.awt.GridBagConstraints#gridx
	 * @see                java.awt.GridBagConstraints#gridy
	 * @see                java.awt.GridBagConstraints#gridwidth
	 * @see                java.awt.GridBagConstraints#gridheight
	 */
	public void add(
			Container container,
			Component component,
			int gridx,
			int gridy,
			int gridwidth,
			int gridheight) {
		add(container, component, gridx, gridy, gridwidth, gridheight, 0, 0, NONE, NORTHWEST, 0, 0, defaultInsets);
	}


	/**
	 *  Addes the specified component to the specified container.
	 *
	 * @param  container   the container
	 * @param  component   the component to be added
	 * @param  gridx       the gridx parameter
	 * @param  gridy       the gridy parameter
	 * @param  gridwidth   the gridwidth parameter
	 * @param  gridheight  the gridheight parameter
	 * @param  fill        the fill parameter
	 * @param  weightx     Description of Parameter
	 * @param  weighty     Description of Parameter
	 * @see                java.awt.GridBagConstraints#gridx
	 * @see                java.awt.GridBagConstraints#gridy
	 * @see                java.awt.GridBagConstraints#gridwidth
	 * @see                java.awt.GridBagConstraints#gridheight
	 * @see                java.awt.GridBagConstraints#fill
	 */
	public void add(
			Container container,
			Component component,
			int gridx,
			int gridy,
			int gridwidth,
			int gridheight,
			double weightx,
			double weighty,
			int fill) {
		add(container, component, gridx, gridy, gridwidth, gridheight, weightx, weighty, fill, NORTHWEST, 0, 0, defaultInsets);
	}


	/**
	 *  Addes the specified component to the specified container.
	 *
	 * @param  container   the container
	 * @param  component   the component to be added
	 * @param  gridx       the gridx parameter
	 * @param  gridy       the gridy parameter
	 * @param  gridwidth   the gridwidth parameter
	 * @param  gridheight  the gridheight parameter
	 * @param  weightx     the weightx parameter
	 * @param  weighty     the weighty parameter
	 * @param  fill        the fill parameter
	 * @param  anchor      the anchor parameter
	 * @param  ipadx       the ipadx parameter
	 * @param  ipady       the ipady parameter
	 * @param  insets      the insets parameter
	 * @see                java.awt.GridBagConstraints#gridx
	 * @see                java.awt.GridBagConstraints#gridy
	 * @see                java.awt.GridBagConstraints#gridwidth
	 * @see                java.awt.GridBagConstraints#gridheight
	 * @see                java.awt.GridBagConstraints#weightx
	 * @see                java.awt.GridBagConstraints#weighty
	 * @see                java.awt.GridBagConstraints#fill
	 * @see                java.awt.GridBagConstraints#anchor
	 * @see                java.awt.GridBagConstraints#ipadx
	 * @see                java.awt.GridBagConstraints#ipady
	 * @see                java.awt.GridBagConstraints#insets
	 */
	public void add(
			Container container,
			Component component,
			int gridx,
			int gridy,
			int gridwidth,
			int gridheight,
			double weightx,
			double weighty,
			int fill,
			int anchor,
			int ipadx,
			int ipady,
			Insets insets) {
		GridBagLayout gridBagLayout = (GridBagLayout) container.getLayout();
		constraints.gridx = gridx;
		constraints.gridy = gridy;
		constraints.gridwidth = gridwidth;
		constraints.gridheight = gridheight;
		constraints.weightx = weightx;
		constraints.weighty = weighty;
		constraints.fill = fill;
		constraints.anchor = anchor;
		constraints.ipadx = ipadx;
		constraints.ipady = ipady;
		constraints.insets = insets;
		gridBagLayout.setConstraints(component, constraints);
		container.add(component);
	}

}
