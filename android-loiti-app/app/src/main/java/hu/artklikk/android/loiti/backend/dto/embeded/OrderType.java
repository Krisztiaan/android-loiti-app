package hu.artklikk.android.loiti.backend.dto.embeded;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

public class OrderType implements Comparable<OrderType>, Serializable {

	private static final long serialVersionUID = 1L;

	@JsonProperty
	@JsonInclude(Include.NON_NULL)
	private String name;
	@JsonProperty
	private Type type;

	protected OrderType() {
	}

	@JsonCreator
	public OrderType(String name) {
		setName(name);
		setType(Type.fromJson(name));
	}

	public OrderType(String name, Type type) {
		setName(name);
		setType(type);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		if("".equals(name)) name = null;
		this.name = name;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	@Override
	public int compareTo(OrderType otherOrderType) {
		if(name == null) {
			if(otherOrderType.getName() == null) return 0;
			else return -1;
		}
		else if(otherOrderType.getName() == null) {
			return 1;
		}
		else {
			return name.compareToIgnoreCase(otherOrderType.getName());
		}
	}

	@Override
	public boolean equals(Object otherObject) {
		if(!(otherObject instanceof OrderType)) return false;
		else {
			final OrderType otherOrderType = (OrderType) otherObject;

			final boolean customNamed =
					(Type.CUSTOM.equals(this.type) && (this.name != null) && this.name.equalsIgnoreCase(otherOrderType.getName()));
			final boolean equalsType = ((this.type != null) && this.type.equals(otherOrderType.getType()));

			return (customNamed || equalsType);
		}
	}

	@Override
	public int hashCode() {
		if(name == null) return super.hashCode();
		else return name.toLowerCase().hashCode();
	}

	@Override
	public String toString() {
		if(name == null) {
			if(type != null) return type.name();
			else return super.toString();
		}
		else return String.format("%s [%s]", name, ((type != null) ? type.name().toLowerCase() : "N\\A"));
	}

	public static enum Type {
		ALACARTE,
		MENU,
		CUSTOM;

		@JsonValue
		public String toJson() {
			return name().toLowerCase();
		}

		@JsonCreator
		public static Type fromJson(String string) {
			try {
				return valueOf(string.toUpperCase());
			} catch(IllegalArgumentException e) {
				return CUSTOM;
			}
			catch (NullPointerException e) {
				return CUSTOM;
			}
		}
	}
}
