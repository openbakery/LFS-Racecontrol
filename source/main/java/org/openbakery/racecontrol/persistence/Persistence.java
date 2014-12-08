package org.openbakery.racecontrol.persistence;

import java.util.List;
import java.util.Map;

public interface Persistence {

	public void flush() throws PersistenceException;

	public void close() throws PersistenceException;

	public <T> T store(T object) throws PersistenceException;

	public List<? extends Object> query(String query) throws PersistenceException;

	public List<? extends Object> queryNative(String query, String name) throws PersistenceException;

	public List<? extends Object> queryNative(String query, Map<String, String> parameters, Class<? extends Object> clazz) throws PersistenceException;

	public int queryNativeInt(String query, Map<String, String> parameters) throws PersistenceException;

	public <T> T delete(T object) throws PersistenceException;

  public Transaction createTransaction();

}
