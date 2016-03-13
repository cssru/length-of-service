package com.chessoft.lengthofservice.service.impl;

import com.chessoft.lengthofservice.utils.Utils;
import com.chessoft.lengthofservice.dao.HumanDao;
import com.chessoft.lengthofservice.domain.Human;
import com.chessoft.lengthofservice.domain.Period;
import com.chessoft.lengthofservice.factory.DaoFactory;
import com.chessoft.lengthofservice.service.HumanService;
import com.chessoft.lengthofservice.utils.HibernateUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class HumanServiceImpl implements HumanService {
	private HumanDao humanDao = DaoFactory.getHumanDao();

	@Override
	public Human create(Human human) {
		Session session = HibernateUtils.openSession();
		Transaction transaction = session.beginTransaction();

		Human result = humanDao.create(session, human);

		transaction.commit();
		session.close();

		return result;
	}

	@Override
	public Human read(Long id) {
		Session session = HibernateUtils.openSession();

		Human result = humanDao.read(session, id);

		session.close();

		return result;
	}

	@Override
	public void update(Human human) {
		Session session = HibernateUtils.openSession();
		Transaction transaction = session.beginTransaction();

		humanDao.update(session, human);

		transaction.commit();
		session.close();
	}

	@Override
	public void delete(Long id) {
		Session session = HibernateUtils.openSession();
		Transaction transaction = session.beginTransaction();

		humanDao.delete(session, id);

		transaction.commit();
		session.close();
	}

	@Override
	public List<Human> list() {
		Session session = HibernateUtils.openSession();

		List<Human> result = humanDao.list(session);

		session.close();

		return result;
	}

	@Override
	public void addPeriod(Long humanId, Period period) {
		Utils.checkPeriod(period);
		Session session = HibernateUtils.openSession();
		Transaction transaction = session.beginTransaction();

		Human human = humanDao.read(session, humanId);
		try {
			Utils.checkPeriodIntersection(period, human.getPeriods());
			period.setHuman(human);
			human.getPeriods().add(period);
			humanDao.update(session, human);
		} catch (IllegalArgumentException exception) {
			throw exception;
		} finally {
			transaction.commit();
			session.close();
		}
	}

	@Override
	public void removePeriod(Long humanId, Period period) {
		Session session = HibernateUtils.openSession();
		Transaction transaction = session.beginTransaction();

		Human human = humanDao.read(session, humanId);
		human.getPeriods().remove(period);
		humanDao.update(session, human);

		transaction.commit();
		session.close();
	}
}
