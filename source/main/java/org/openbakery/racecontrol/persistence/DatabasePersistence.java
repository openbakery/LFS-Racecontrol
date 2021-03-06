package org.openbakery.racecontrol.persistence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

public class DatabasePersistence implements Persistence {

  private static Logger log = LoggerFactory.getLogger(DatabasePersistence.class);

	private EntityManagerFactory entityManagerFactory;
  
	public DatabasePersistence(String persistence) {
		entityManagerFactory = javax.persistence.Persistence.createEntityManagerFactory(persistence);
	}

	public <T> T store(T object) throws PersistenceException {
    log.debug("store: " + object);
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		object = entityManager.merge(object);
		transaction.commit();
		entityManager.close();
		return object;
	}

	@SuppressWarnings("unchecked")
	public List<? extends Object> query(String query) throws PersistenceException {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		List<? extends Object> result = entityManager.createQuery(query).getResultList();
		entityManager.close();
		return result;
	}

	@SuppressWarnings("unchecked")
	public List<? extends Object> queryNative(String query, String name) throws PersistenceException {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		List<? extends Object> result = entityManager.createNativeQuery(query, name).getResultList();
		entityManager.close();
		return result;
	}

	@SuppressWarnings("unchecked")
	public List<? extends Object> queryNamed(String name) throws PersistenceException {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		List result = entityManager.createNamedQuery(name).getResultList();
		entityManager.close();
		return result;
	}

  public Transaction createTransaction()
  {
    EntityManager entityManager = entityManagerFactory.createEntityManager();
    EntityTransaction entityTransaction = entityManager.getTransaction();
    entityTransaction.begin();
    return new Transaction(entityManager, entityTransaction);
  }

	public void close() throws PersistenceException {
		entityManagerFactory.close();
	}

	public void flush() throws PersistenceException {
	}

	public List<? extends Object> queryNative(String queryString, Map<String, String> parameters, Class<? extends Object> clazz) throws PersistenceException {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		Query query = entityManager.createNativeQuery(queryString, clazz);
		for (Map.Entry<String, String> entry : parameters.entrySet()) {
			query.setParameter(entry.getKey(), entry.getValue());
		}
		List<? extends Object> result = query.getResultList();
		entityManager.close();
		return result;
	}


	public int queryNativeInt(String queryString, Map<String, String> parameters) throws PersistenceException {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		Query query = entityManager.createNativeQuery(queryString);
		for (Map.Entry<String, String> entry : parameters.entrySet()) {
			query.setParameter(entry.getKey(), entry.getValue());
		}
		int result = 0;
		Object resultObject = query.getSingleResult();
		if (resultObject instanceof Number) {
			result = ((Number)resultObject).intValue();
		}

		entityManager.close();
		return result;
	}


	public <T> T delete(T object) throws PersistenceException {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		Object newObject = entityManager.merge(object);
		entityManager.remove(newObject);
		transaction.commit();
		entityManager.close();
		return object;
	}

}
