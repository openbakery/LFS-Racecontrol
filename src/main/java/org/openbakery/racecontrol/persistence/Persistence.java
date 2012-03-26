package org.openbakery.racecontrol.persistence;

import java.util.List;

public interface Persistence {

	public void flush() throws PersistenceException;

	public void close() throws PersistenceException;

	public <T> T store(T object) throws PersistenceException;

	public List<? extends Object> query(String query) throws PersistenceException;

	public List<? extends Object> queryNative(String query, String name) throws PersistenceException;

	public List<? extends Object> queryNative(String query, Class<? extends Object> clazz) throws PersistenceException;

	public <T> T delete(T object) throws PersistenceException;

  public Transaction createTransaction();

}
