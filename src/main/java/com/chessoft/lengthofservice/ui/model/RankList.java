package com.chessoft.lengthofservice.ui.model;

import java.util.ArrayList;
import java.util.List;

public class RankList {
	private static final List<Rank> ranks;
	static {
		ranks = new ArrayList<>(15);
		ranks.add(new Rank(0, "рядовой"));
		ranks.add(new Rank(1, "ефрейтор"));
		ranks.add(new Rank(2, "младший сержант"));
		ranks.add(new Rank(3, "сержант"));
		ranks.add(new Rank(4, "старший сержант"));
		ranks.add(new Rank(5, "старшина"));
		ranks.add(new Rank(6, "прапорщик"));
		ranks.add(new Rank(7, "старший прапорщик"));
		ranks.add(new Rank(8, "младший лейтенант"));
		ranks.add(new Rank(9, "лейтенант"));
		ranks.add(new Rank(10, "старший лейтенант"));
		ranks.add(new Rank(11, "капитан"));
		ranks.add(new Rank(12, "майор"));
		ranks.add(new Rank(13, "подполковник"));
		ranks.add(new Rank(14, "полковник"));
	}

	public static Rank get(Integer rankId) {
		return ranks.get(rankId);
	}

	public static List<Rank> list() {
		return ranks;
	}
}
