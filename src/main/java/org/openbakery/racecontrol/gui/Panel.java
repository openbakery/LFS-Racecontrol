package org.openbakery.racecontrol.gui;

import java.io.IOException;
import java.util.List;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Panel {
	
  private static Logger log = LoggerFactory.getLogger(Panel.class);

	
	private static final int DEFAULT_COLUMN_WIDTH = 30;
	private int x;
	private int y;
	
	private int padding = 0;
	private int height = 4;

	private int[] columnWidth = { DEFAULT_COLUMN_WIDTH };
	
	private Vector<Vector<Button>>columns;
	private boolean isVisible = false;
	
	
	public Panel(int x, int y) {
		this.x = x;
		this.y = y;
		columns = new Vector<Vector<Button>>();
	}
	
	public void setColumns(int... width) {
		columnWidth = width;
	}
	
	public void add(Button button) {
		add(button, 0);
	}
	
	public void add(Button button, int column) {
		add(button, column, getRowCount(column));
	}
	
	public void add(Button button, int column, int row) {
		if (column >= columns.size()) {
			columns.setSize(column+1);
		}
		Vector<Button> buttonRow = columns.get(column);
		if (buttonRow == null) {
			buttonRow = new Vector<Button>();
			columns.add(column, buttonRow);
		}
		if (buttonRow.size() <= row) {
			buttonRow.setSize(row+1);
		}
		
		buttonRow.set(row, button);
		
		int positionX = x;
		int width = DEFAULT_COLUMN_WIDTH;
		for(int i=0; i<=column; i++) {
			if (column < columnWidth.length) {
				width = columnWidth[i];
			} else {
				width = DEFAULT_COLUMN_WIDTH;
			}
			positionX += width;
		}
		positionX-=width;
		button.setX(positionX);
		button.setY(y + row * (height + padding));
		button.setHeight(height);
		button.setWidth(width);
		
		if (isVisible) {
			try {
				button.setVisible(true);
			} catch (IOException e) {
				log.error("Cannot show button", e);
			}
		}
	}
	


	public synchronized void setVisible(boolean visible) throws IOException {
		if (visible) {
			for (List<Button> column : columns) {
				if (column != null) {
					for(Button button : column) {
						if (button != null) {
							button.setVisible(visible);
						}
					}
				}
			}
		} else {
			for (List<Button> column : columns) {
				if (column != null) {
					for(Button button : column) {
						if (button != null) {
							button.setVisible(visible);
							//ButtonIdHelper.getInstance().pushId(driverId, (byte)button.getId());
						}
					}
				}
			}
		}
		isVisible = visible;
	}
	
	public Button getButton(int clickId) {
		for (List<Button> column : columns) {
			if (column != null) {
				for(Button button : column) {
					if (button != null && button.getId() == clickId) {
						return button;
					}
				}
			}
		}
		return null;
	}
	
	public int getRowCount(int column) {
		if (columns.size() > column) {
			List<Button> row = columns.get(column);
			if (row != null) {
				return row.size();
			}
		}
		return 0;
	}
	
	public Button getButton(int row, int column) {
		List<Button> buttonColumn = columns.get(column);
		if (buttonColumn != null) {
			return buttonColumn.get(row);
		}
		return null;
	}

	public void clear() {
		try {
			setVisible(false);
			for (List<Button> column : columns) {
				if (column != null) {
					for (int i=0; i<column.size(); i++) {
						Button button = column.get(i);
						if (button != null) {
							button.destroy();
						}
					}
					column.clear();
				}
			}
		} catch (IOException e) {
			log.error("Error at clearing panel", e);
		}
	}
	
	public Button get(int x, int y) {
		if (columns.size() > x) {
			List<Button> column = columns.get(x);
			if (column != null && column.size() > y) {
				return column.get(y);
			}
		}
		return null;
	}

	public boolean isVisible() {
		return isVisible;
	}
	
	public void destroy() {
		for (List<Button> column : columns) {
			if (column != null) {
				for(Button button : column) {
					if (button != null) {
						button.destroy();
					}
				}
				column.clear();
			}
		}
	}
	
	protected void finalize() throws Throwable
	{
		destroy();
	  super.finalize();
	} 

}
