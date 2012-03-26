package org.openbakery.racecontrol.service;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.ConversionException;
import org.apache.commons.configuration.DataConfiguration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.openbakery.racecontrol.bean.Settings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SettingsLoader {

	private Logger log = LoggerFactory.getLogger(SettingsLoader.class);

	private String path;

	private String basePath;

	public String getBasePath() {
		return basePath;
	}

	public void setBasePath(String basePath) {
		this.basePath = basePath;
	}

	public SettingsLoader(String path) {
		this.path = path;

		File directory = new File(path);
		if (!directory.exists()) {

			if (!directory.mkdirs()) {
				throw new RuntimeException("cannot create settings directory: " + directory.getAbsolutePath());
			}
		}
	}

	public void load(Settings settings) {

		try {
			File configurationFile = new File(getFilename(settings));
			if (!configurationFile.exists()) {
				log.debug("configuration file does not exist: {}", configurationFile);
				return;
			}
			DataConfiguration config = new DataConfiguration(new PropertiesConfiguration(configurationFile));

			for (String field : settings.getSettingFields()) {

				Type type = PropertyUtils.getPropertyType(settings, field);
				Object object = null;
				if (type == List.class) {
					Type listType = getGenericType(settings, field);
					if (listType != null && listType instanceof Class) {
						try {
							object = config.getList((Class) listType, field);
						} catch (ConversionException ex) {
							object = null;
						}
					} else {
						object = config.getList(field);
					}
				} else {
					object = config.get((Class) type, field);
				}
				PropertyUtils.setProperty(settings, field, object);
			}
		} catch (IllegalAccessException e) {
			log.error("Something went wrong, this exception should not occur!", e);
		} catch (InvocationTargetException e) {
			log.error("Something went wrong, this exception should not occur!", e);
		} catch (NoSuchMethodException e) {
			log.error("Something went wrong, this exception should not occur!", e);
		} catch (ConfigurationException e) {
			log.error("Something went wrong, this exception should not occur!", e);
		}

	}

	private Type getGenericType(Object object, String fieldName) {
		Field field = null;
		try {
			field = object.getClass().getDeclaredField(fieldName);
		} catch (SecurityException e) {
			return null;
		} catch (NoSuchFieldException e) {
			return null;
		}

		Type type = field.getGenericType();
		if (type instanceof ParameterizedType) {
			ParameterizedType pt = (ParameterizedType) type;
			for (Type t : pt.getActualTypeArguments()) {
				return t;
			}
		}
		return null;
	}

	public void save(Settings settings) {
		try {

			File file = new File(getFilename(settings));
			if (!file.exists()) {
				file.createNewFile();
			}
			PropertiesConfiguration config = new PropertiesConfiguration(file);

			for (String field : settings.getSettingFields()) {

				Object property = PropertyUtils.getProperty(settings, field);
				config.setProperty(field, property);

			}
			config.save();
		} catch (IllegalAccessException e) {
			log.error("Something went wrong, this exception should not occur!", e);
		} catch (InvocationTargetException e) {
			log.error("Something went wrong, this exception should not occur!", e);
		} catch (NoSuchMethodException e) {
			log.error("Something went wrong, this exception should not occur!", e);
		} catch (ConfigurationException e) {
			log.error("Something went wrong, this exception should not occur!", e);
		} catch (IOException e) {
			log.error("Something went wrong, this exception should not occur!", e);
		}

	}

	private String getFilename(Settings settings) {
		StringBuilder filename = new StringBuilder();
		File pathFile = new File(path);
		if (!pathFile.isAbsolute() && basePath != null && basePath.length() > 0) {
			filename.append(basePath);
			filename.append(File.separator);
		}
		filename.append(path);
		filename.append(File.separator);
		filename.append(settings.getClass().getName());
		filename.append(".properties");

		return filename.toString();
	}
}
