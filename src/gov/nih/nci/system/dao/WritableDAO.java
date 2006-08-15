/*
 * Created on Jul 17, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package gov.nih.nci.system.dao;

import gov.nih.nci.system.dao.impl.orm.ORMConnection;
import gov.nih.nci.system.servicelocator.ServiceLocator;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Example;

/**
 * 
 * @author Ekagra Software Technologies Ltd.
 */
public class WritableDAO {
	static final Logger log = Logger.getLogger(WritableDAO.class.getName());

	public Object createObject(Object obj) throws DAOException {
		Session session = null;
		Transaction transaction = null;
		try {
			session = this.getSession(obj);
		} catch (Exception ex) {
			log.error("Could not obtain a session! Could not create "
							+ obj.getClass().getName());
			throw new SessionException("Could not obtain a session! Could not create "
							+ obj.getClass().getName(), ex);
		}
		if (session == null) {
			log.error("Could not obtain a session");
			throw new SessionException("Could not obtain a session");
		}
		try {
			transaction = session.beginTransaction();
			session.save(obj);
			transaction.commit();
		} catch (Exception ex) {
			try {
				transaction.rollback();
			} catch (Exception ex3) {
				log.error("Error while rolling back transaction: "
						+ ex3.getMessage() + " for original Exception: "
						+ ex.getMessage());
				throw new RollbackException(
						"Error while rolling back transaction", ex);
			}

			log.error("Error while creating object "
					+ obj.getClass().getName() + ": " + ex.getMessage());
			throw new CreateException("An error occured while creating object "
					+ obj.getClass().getName(), ex);
		} finally {
			try {
				session.close();
			} catch (Exception ex2) {
				log.error("Error while closing the Session: "
						+ ex2.getMessage());
				throw new SessionException("Error while closing the Session",
						ex2);
			}
		}
		log.debug("Successful in creating " + obj.getClass().getName());

		return obj;

	}

	public Object updateObject(Object obj) throws DAOException {
		Session session = null;
		Transaction transaction = null;
		try {
			session = this.getSession(obj);
		} catch (Exception ex) {
			log.error("Could not obtain a session");
			throw new SessionException(
					"Could not obtain a session! Could not update "
							+ obj.getClass().getName(), ex);
		}
		if (session == null) {
			log.error("Could not obtain a session");
			throw new SessionException(
					"Could not obtain a session! Could not update "
							+ obj.getClass().getName());
		}
		try {
			transaction = session.beginTransaction();
			session.update(obj);
			transaction.commit();
		} catch (Exception ex) {
			try {
				transaction.rollback();
			} catch (Exception ex3) {
				log.error("Error while rolling back transaction: "
						+ ex3.getMessage() + " for original Exception: "
						+ ex.getMessage());
				throw new RollbackException(
						"Error while rolling back transaction: "
								+ ex3.getMessage()
								+ " for original Exception: " + ex.getMessage(),
						ex3);
			}

			log.error("Error while updating " + obj.getClass().getName());
			throw new UpdateException("An error occured while updating "
					+ obj.getClass().getName(), ex);
		} finally {
			try {
				session.close();
			} catch (Exception ex2) {
				log.error("Error while closing the Session: "
						+ ex2.getMessage());
				throw new SessionException("Error while closing the Session",
						ex2);
			}
		}

		log.debug("Successful in updating " + obj.getClass().getName());
		return obj;
	}

	public void removeObject(Object obj) throws DAOException {
		Session session = null;
		Transaction transaction = null;
		try {
			session = this.getSession(obj);
		} catch (Exception ex) {
			log.error("Could not obtain a session! Could not delete "
					+ obj.getClass().getName());
			throw new SessionException(
					"Could not obtain a session! Could not delete "
							+ obj.getClass().getName(), ex);
		}

		if (session == null) {
			log.error("Could not obtain a session! Could not delete "
					+ obj.getClass().getName());
			throw new SessionException(
					"Could not obtain a session! Could not delete "
							+ obj.getClass().getName());
		}
		try {
			transaction = session.beginTransaction();
			session.delete(obj);
			transaction.commit();
		} catch (Exception ex) {
			try {
				transaction.rollback();
			} catch (Exception ex3) {
				log.error("Error while rolling back transaction: "
						+ ex3.getMessage());
				throw new RollbackException(
						"An error occured rolling back transaction while deleting "
								+ obj.getClass().getName(), ex);
			}

			log.error("Error while deleting " + obj.getClass().getName() + ": "
					+ ex.getMessage());
			throw new DeleteException("An error occured while deleting "
					+ obj.getClass().getName(), ex);
		} finally {
			try {
				session.close();
			} catch (Exception ex2) {
				log.error("Error closing the session while deleting "
						+ obj.getClass().getName() + ex2.getMessage());
				throw new SessionException(
						"Error while closing the Session while removing object "
								+ obj.getClass().getName(), ex2);
			}
		}
		log.debug("Successful in deleting " + obj.getClass().getName());

	}

	public List getObjects(Object obj) throws DAOException {
		Session session = null;

		try {
			session = this.getSession(obj);
		} catch (Exception ex) {
			log.error("Could not obtain a session! Could not get "
					+ obj.getClass().getName() + " objects");
			throw new QueryException(
					"Could not obtain a session! Could not get "
							+ obj.getClass().getName() + " objects", ex);
		}
		if (session == null) {
			log.error("Could not obtain a session! Could not get "
					+ obj.getClass().getName() + " objects");
			throw new QueryException(
					"Could not obtain a session! Could not get "
							+ obj.getClass().getName() + " objects");
		}
		List list = null;
		try {
			Criteria criteria = session.createCriteria(obj.getClass());
			criteria.add(Example.create(obj));
			list = criteria.list();
		} catch (Exception ex) {
			log.error("Error while getting objects: " + ex.getMessage());
			throw new QueryException("An error occured while getting objects: "
					+ ex.getMessage());
		} finally {
			try {
				session.close();
			} catch (Exception ex2) {
				log.error("Error while closing the Session: "
						+ ex2.getMessage());
				throw new SessionException("Error while closing the Session",
						ex2);
			}
		}
		log.debug("Successful in getting " + obj.getClass().getName()
				+ " objects");

		return list;
	}

	private Session getSession(Object object) throws Exception {
		ServiceLocator serviceLocator = new ServiceLocator();
		int oRMCounter = serviceLocator.getORMCounter(object.getClass()
				.getName());
		ORMConnection oRMConnection = ORMConnection.getInstance();
		Session session = oRMConnection.openSession(oRMCounter);
		return session;
	}
}
