package com.chessoft.lengthofservice.utils;

import com.chessoft.lengthofservice.SessionFactoryInitializationError;
import com.chessoft.lengthofservice.domain.Human;
import com.chessoft.lengthofservice.domain.Period;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.Properties;

public class HibernateUtils {
	private static final SessionFactory sessionFactory;

	static {
		try {
			Properties properties = new Properties();
			properties.setProperty("hibernate.connection.url", "jdbc:h2:./los");
			properties.setProperty("hibernate.connection.driver_class", "org.h2.Driver");
			properties.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
			properties.setProperty("hibernate.hbm2ddl.auto", "update");

			sessionFactory = new Configuration()
					.addPackage("com.chessoft.lengthofservice.domain")
					.addAnnotatedClass(Human.class)
					.addAnnotatedClass(Period.class)
					.addProperties(properties)
					.buildSessionFactory();
		} catch (Throwable exception) {
			exception.printStackTrace();
			throw new SessionFactoryInitializationError();
		}
	}

	private HibernateUtils() {
	}

	public static Session openSession() {
		return sessionFactory.openSession();
	}

	public static Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}

}
