package com.chessoft.lengthofservice.dao;

import com.chessoft.lengthofservice.domain.Human;
import org.hibernate.Session;

import java.util.List;

public interface HumanDao {
	Human create(Session session, Human human);
	Human read(Session session, Long id);
	void update(Session session, Human human);
	void delete(Session session, Long id);
	List<Human> list(Session session);
}
