package org.openbakery.racecontrol.gui;

import java.io.IOException;

import org.openbakery.jinsim.request.ButtonFunctionRequest;
import org.openbakery.jinsim.request.ButtonRequest;
import org.openbakery.jinsim.response.InSimListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openbakery.racecontrol.JInSimClient;
import org.openbakery.racecontrol.gui.util.HideRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Button {

	public boolean isVisible() {
		return isVisible;
	}

  private static Logger log = LoggerFactory.getLogger(Button.class);

	public enum TextAlign {
		left,
		center,
		right;
	}

	private int x;

	private int y;

	private int width;

	private int height;

	private int style = ButtonRequest.BUTTON_STYLE_DARK | ButtonRequest.BUTTON_STYLE_WHITE;

	private int hideTime = 0;

	private String text;

	private boolean isVisible = false;

	private int id = 0;

	private int connectionId = 255;

	private boolean typeIn;

	private InSimListener clickable;

	private Object object;

	private TextAlign textAlign = TextAlign.center;

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}

	public Button(int connectionId) {
		this(connectionId, "");
	}

	public Button(int connectionId, String text) {
		this(connectionId, text, 0, 0, 30, 6);
	}

	public Button(int connectionId, String text, int x, int y, int width, int height) {
		this.connectionId = connectionId;
		this.text = text;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		id = ButtonIdHelper.getInstance().popId(connectionId);
	}

	public synchronized void setVisible(boolean visible) throws IOException {
		if (id == 0) {
			throw new IllegalStateException("Button is already destroyed: " + toString());
		}
		if (visible) {

			ButtonRequest buttonRequest = new ButtonRequest();

			byte flags = (byte) style;
			if (textAlign == TextAlign.right) {
				flags |= ButtonRequest.BUTTON_STYLE_RIGHT;
			} else if (textAlign == TextAlign.left) {
				flags |= ButtonRequest.BUTTON_STYLE_LEFT;
			}
			if (clickable != null) {
				flags |= ButtonRequest.BUTTON_STYLE_CLICK;
			}
			buttonRequest.setButtonStyle(flags);

			buttonRequest.setText(text);
			buttonRequest.setConnectionId((byte) connectionId);
			buttonRequest.setClickId((byte) id);
			if (clickable != null) {
				buttonRequest.setRequestInfo((byte) JInSimClient.getInstance().getIndex(clickable));
			} else {
				buttonRequest.setRequestInfo((byte) 1);
			}
			buttonRequest.setLeft((byte) x);
			buttonRequest.setTop((byte) y);
			if (!isVisible) {
				buttonRequest.setWidth((byte) width);
				buttonRequest.setHeight((byte) height);
			} else {
				buttonRequest.setWidth((byte) 0);
				buttonRequest.setHeight((byte) 0);
			}
			if (typeIn) {
				buttonRequest.setTypeIn((byte) 96);
			}
			if (clickable != null) {
				JInSimClient.getInstance().send(buttonRequest, clickable);
			} else {
				JInSimClient.getInstance().send(buttonRequest);
			}
			if (hideTime > 0) {
				Thread thread = new Thread(new HideRunner(this, hideTime));
				thread.start();
			}
		}
		if (!visible) {
			// hide button
			ButtonFunctionRequest request = new ButtonFunctionRequest();
			request.setConnectionId((byte) connectionId);
			request.setClickId((byte) id);
			JInSimClient.getInstance().send(request);
		}
		isVisible = visible;
		/*
		 * if (log.isDebugEnabled()) { log.debug("Button visible: " + isVisible + " " + toString()); }
		 */

	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getHideTime() {
		return hideTime;
	}

	public void setHideTime(int hideTime) {
		this.hideTime = hideTime;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		if (!text.equals(this.text)) {
			this.text = text;
			if (isVisible) {
				try {
					setVisible(true);
				} catch (IOException e) {
					log.error("Cannot display button", e);
				}
			}
		}
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setTypeIn(boolean typeIn) {
		this.typeIn = typeIn;
	}

	public void setTextAlign(TextAlign align) {
		textAlign = align;
	}

	public String toString() {
		return "Button [x=" + x + ", y=" + y + ", '" + text + "', id=" + id + ", connectionId=" + connectionId + "]";
	}

	public void destroy() {
		if (id != 0) {
			if (isVisible) {
				try {
					setVisible(false);
				} catch (IOException e) {
					log.error("Cannot hide button", e);
				}
			}
			ButtonIdHelper.getInstance().pushId(connectionId, (byte) id);
			id = 0;
		}
	}

	protected void finalize() throws Throwable {
		destroy();
		super.finalize();
	}

	public boolean isClickable() {
		return clickable != null;
	}

	public void setClickable(InSimListener clickable) {
		this.clickable = clickable;
	}

	public int getStyle() {
		return style;
	}

	public void setStyle(int style) {
		this.style = style;
	}
}
