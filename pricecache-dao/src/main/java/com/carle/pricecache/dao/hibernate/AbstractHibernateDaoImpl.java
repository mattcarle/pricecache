package com.carle.pricecache.dao.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 * Abstract base class for Hibernate DAOs. Wraps the session factory and provides a method for retrieving the current
 * session.
 * 
 * @author Matt Carle
 * 
 */
public abstract class AbstractHibernateDaoImpl {
	private final SessionFactory sessionFactory;

	public AbstractHibernateDaoImpl(final SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	protected Session currentSession() {
		return sessionFactory.getCurrentSession();
	}
}
