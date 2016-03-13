package com.chessoft.lengthofservice.factory;

import com.chessoft.lengthofservice.dao.HumanDao;
import com.chessoft.lengthofservice.dao.impl.HumanDaoImpl;

public class DaoFactory {
	private static HumanDao humanDao = new HumanDaoImpl();

	private DaoFactory() {
	}

	public static HumanDao getHumanDao() {
		return humanDao;
	}
}
