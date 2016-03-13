package com.chessoft.lengthofservice.dao.impl;

import com.chessoft.lengthofservice.dao.HumanDao;
import com.chessoft.lengthofservice.domain.Human;
import org.hibernate.Session;

import java.util.List;

public class HumanDaoImpl implements HumanDao {

	@Override
	public Human create(Session session, Human human) {
		session.save(human);
		return human;
	}

	@Override
	public Human read(Session session, Long id) {
		Human human = session.get(Human.class, id);
		return human;
	}

	@Override
	public void update(Session session, Human human) {
		session.update(human);
	}

	@Override
	public void delete(Session session, Long id) {
		Human persistedHuman = session.get(Human.class, id);
		if (persistedHuman != null) {
			session.delete(persistedHuman);
		}
	}

	@Override
	public List<Human> list(Session session) {
		return session.createQuery("from Human").list();
	}
}
