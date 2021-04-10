package br.com.alura.forum.controller.dto.output;

import java.util.ArrayList;
import java.util.List;

import br.com.alura.forum.model.Category;


public class CategorySearchInputDto {
	
	private Long id;
	
	private String name;
	
	private List<Category> subcategories = new ArrayList<>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Category> getSubcategories() {
		return subcategories;
	}

	public void setSubcategories(List<Category> subcategories) {
		this.subcategories = subcategories;
	}
	
	
	

}
