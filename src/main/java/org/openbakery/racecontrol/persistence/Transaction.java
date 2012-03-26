package org.openbakery.racecontrol.persistence;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

/**
 * Created by IntelliJ IDEA.
 * User: rene
 * Date: 23.03.12
 * Time: 16:53
 * To change this template use File | Settings | File Templates.
 */
public class Transaction {

  private EntityManager entityManager;
  private EntityTransaction entityTransaction;

  public Transaction(EntityManager entityManager, EntityTransaction entityTransaction) {
    this.entityManager = entityManager;
    this.entityTransaction = entityTransaction;

  }

  public void commit()
  {
    entityTransaction.commit();
    entityManager.close();
  }

  public <T> T refresh(Class<T> clazz, Object primaryKey) throws PersistenceException {
    return entityManager.find(clazz, primaryKey);
  }
}
