package com.chessoft.lengthofservice.factory;

import com.chessoft.lengthofservice.service.HumanService;
import com.chessoft.lengthofservice.service.impl.HumanServiceImpl;

public class ServiceFactory {
	private static HumanService humanService = new HumanServiceImpl();

	private ServiceFactory() {
	}

	public static HumanService getHumanService() {
		return humanService;
	}
}
