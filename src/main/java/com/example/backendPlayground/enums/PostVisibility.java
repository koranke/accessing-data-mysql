package com.example.backendPlayground.enums;

import com.example.backendPlayground.exceptions.InvalidEnumValueException;

public enum PostVisibility {
	PUBLIC("public"),
	PRIVATE("private"),
	SHARED("shared")
	;

	private final String visibility;

	PostVisibility(String visibility) {
		this.visibility = visibility;
	}

	public String getVisibility() {
		return visibility;
	}

	public static PostVisibility fromString(String visibility) {
		for (PostVisibility postVisibility : PostVisibility.values()) {
			if (postVisibility.getVisibility().equalsIgnoreCase(visibility)) {
				return postVisibility;
			}
		}
		throw new InvalidEnumValueException("Unknown visibility: " + visibility);
	}

}
