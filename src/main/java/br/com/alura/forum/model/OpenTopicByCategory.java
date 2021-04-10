package br.com.alura.forum.model;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class OpenTopicByCategory {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String categoryName;
	private int topicCount;
	private LocalDate date;

	/**
	 * @deprecated
	 */
	public OpenTopicByCategory() {
	}

	public OpenTopicByCategory(String categoryName, Number topicCount, Date instant) {
		this.categoryName = categoryName;
		this.topicCount = topicCount.intValue();
		this.date = instant.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	}

	

}
