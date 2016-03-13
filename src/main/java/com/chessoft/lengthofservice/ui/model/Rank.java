package com.chessoft.lengthofservice.ui.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Rank implements Comparable<Rank> {
	private final Integer rankId;
	private final String rankName;

	@Override
	public String toString() {
		return rankName;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (! (obj instanceof Rank)) {
			return false;
		}
		Rank rank2 = (Rank)obj;
		return rank2.getRankId().equals(rankId);
	}

	@Override
	public int compareTo(Rank rank) {
		return rankId - rank.getRankId();
	}
}
