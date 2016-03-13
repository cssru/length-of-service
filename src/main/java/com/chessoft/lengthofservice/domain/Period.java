package com.chessoft.lengthofservice.domain;

import com.chessoft.lengthofservice.enums.PeriodType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "periods")
public class Period {
	@Id
	@Column
	@GeneratedValue
	private Long id;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private PeriodType type;
	
	@ManyToOne
	private Human human;
	
	@Column(nullable = false)
	private Date begin;

	@Column(nullable = false)
	private Date end;
	
	@Column
	private String comment;

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (! (obj instanceof Period)) {
			return false;
		}
		Period period2 = (Period)obj;
		return period2.getId().equals(id);
	}
}
