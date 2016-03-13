package com.chessoft.lengthofservice.service;

import com.chessoft.lengthofservice.domain.Human;
import com.chessoft.lengthofservice.domain.Period;

import java.util.List;

public interface HumanService {
	Human create(Human human);
	Human read(Long id);
	void update(Human human);
	void delete(Long id);
	List<Human> list();
	void addPeriod(Long humanId, Period period);
	void removePeriod(Long humanId, Period period);
}
