package com.example.backendPlayground.enums;

import com.example.backendPlayground.exceptions.InvalidEnumValueException;
import com.example.backendPlayground.serialization.PostVisibilityDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;

@Getter
@JsonDeserialize(using = PostVisibilityDeserializer.class)
public enum PostVisibility {
	PUBLIC("public"),
	PRIVATE("private"),
	SHARED("shared")
	;

	private final String visibility;

	PostVisibility(String visibility) {
		this.visibility = visibility;
	}

	public static PostVisibility fromString(String visibility) {
		for (PostVisibility postVisibility : PostVisibility.values()) {
			if (postVisibility.getVisibility().equalsIgnoreCase(visibility)) {
				return postVisibility;
			}
		}
		throw new InvalidEnumValueException("Unknown visibility value: " + visibility);
	}

}
