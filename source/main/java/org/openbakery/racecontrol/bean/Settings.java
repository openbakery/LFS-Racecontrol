package org.openbakery.racecontrol.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author Rene Pirringer
 */
public interface Settings extends Serializable {

	public List<String> getSettingFields();
}
