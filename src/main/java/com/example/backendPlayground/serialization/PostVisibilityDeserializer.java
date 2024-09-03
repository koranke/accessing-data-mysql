package com.example.backendPlayground.serialization;

import com.example.backendPlayground.enums.PostVisibility;
import com.example.backendPlayground.exceptions.InvalidEnumValueException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class PostVisibilityDeserializer extends JsonDeserializer<PostVisibility> {

	@Override
	public PostVisibility deserialize(JsonParser jsonParser, DeserializationContext context) throws IOException {
		String value = jsonParser.getText();
		try {
			return PostVisibility.valueOf(value.toUpperCase());
		} catch (IllegalArgumentException e) {
			throw new InvalidEnumValueException("Invalid value for PostVisibility: " + value);
		}
	}
}
