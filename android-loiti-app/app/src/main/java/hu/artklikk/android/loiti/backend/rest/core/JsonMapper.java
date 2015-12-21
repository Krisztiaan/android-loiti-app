package hu.artklikk.android.loiti.backend.rest.core;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class JsonMapper {

	private static ObjectMapper mapper = new ObjectMapper();

	static{
		mapper.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);
		mapper.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);
		mapper.configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY,true);
		mapper.setSerializationInclusion(Include.NON_NULL);
	}
	
	public static ObjectMapper getMapper() {
		return mapper;
	}
	
}
