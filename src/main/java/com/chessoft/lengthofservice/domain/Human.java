package com.chessoft.lengthofservice.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "people")
public class Human {
	@Id
	@Column
	@GeneratedValue
	private Long id;

	@Column(nullable = false)
	private Integer rank; // from 0 to 14
	
	@Column(nullable = false)
	private String surname;
	
	@Column(nullable = false)
	private String name;
	
	@Column(nullable = false)
	private String lastname;
	
	@Column(nullable = false)
	private Date birthday;

	@Column
	private String personalNumber;

	@Column
	private String note;

	@OneToMany(mappedBy = "human", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	private List<Period> periods;

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (! (obj instanceof Human)) {
			return false;
		}
		Human human2 = (Human)obj;
		return human2.getId().equals(id);
	}
}
