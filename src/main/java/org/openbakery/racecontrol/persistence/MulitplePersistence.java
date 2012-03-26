package org.openbakery.racecontrol.persistence;

import java.util.ArrayList;
import java.util.List;

public class MulitplePersistence implements Persistence {

	private List<Persistence> writeOnlyPersistence;

	private Persistence readWritePersistence;

	public void setWriteOnlyPersistence(List<Persistence> writeOnlyPersistence) {
		this.writeOnlyPersistence = writeOnlyPersistence;
	}

	public void setReadWritePersistence(Persistence readWritePersistence) {
		this.readWritePersistence = readWritePersistence;
	}

	public void close() throws PersistenceException {
		for (Persistence persistence : writeOnlyPersistence) {
			persistence.close();
		}
	}

	public void flush() throws PersistenceException {
		for (Persistence persistence : writeOnlyPersistence) {
			persistence.flush();
		}
	}

	public List<? extends Object> query(String query) throws PersistenceException {
		List<Object> result = new ArrayList<Object>();
		return readWritePersistence.query(query);
	}

	public List<? extends Object> queryNative(String query, String name) throws PersistenceException {
		return readWritePersistence.queryNative(query, name);
	}

	public List<? extends Object> queryNative(String query, Class<? extends Object> clazz) throws PersistenceException {
		return readWritePersistence.queryNative(query, clazz);
	}

	public <T> T store(T object) throws PersistenceException {
		for (Persistence persistence : writeOnlyPersistence) {
			persistence.store(object);
		}
		return readWritePersistence.store(object);
	}

	public <T> T delete(T object) throws PersistenceException {
		for (Persistence persistence : writeOnlyPersistence) {
			persistence.delete(object);
		}
		return readWritePersistence.delete(object);
	}

  @Override
  public Transaction createTransaction() {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

}
