package com.example.Team19PlaceIts;

public class PlaceItCategory {
	private final String category;

	public PlaceItCategory(String name) {
		category = name;
	}

	@Override
	public boolean equals(Object o) {
		return category.equalsIgnoreCase(((PlaceItCategory) o).category);
	}

	@Override
	public String toString() {
		return category;
	}

}
