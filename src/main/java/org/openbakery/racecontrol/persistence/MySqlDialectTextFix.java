package org.openbakery.racecontrol.persistence;

import org.hibernate.Hibernate;
//import org.hsqldb.Types;

public class MySqlDialectTextFix extends org.hibernate.dialect.MySQLDialect{
  /*
    
	   public MySqlDialectTextFix() { 
	      super(); 
	      registerHibernateType(Types.LONGVARCHAR, Hibernate.TEXT.getName() ); 
	   }     
	  */
}
