package io.github.qianlixy.framework.cache.utils;

import java.lang.reflect.Array;

public class BaseDataTypeArrayUtil {

	public static int arrayLength(Object array) {
		switch (array.getClass().getName()) {
			case "[I" : return ((int[]) array).length;
			case "[B" : return ((byte[]) array).length;
			case "[S" : return ((short[]) array).length;
			case "[J" : return ((long[]) array).length;
			case "[F" : return ((float[]) array).length;
			case "[D" : return ((double[]) array).length;
			case "[Z" : return ((boolean[]) array).length;
			case "[C" : return ((char[]) array).length;
			default : return ((Object[]) array).length;
		}
	}
	
	public static Object arrayValue(Object array, int index) {
		switch (array.getClass().getName()) {
			case "[I" : return Array.getInt(array, index);
			case "[B" : return Array.getByte(array, index);
			case "[S" : return Array.getShort(array, index);
			case "[J" : return Array.getLong(array, index);
			case "[F" : return Array.getFloat(array, index);
			case "[D" : return Array.getDouble(array, index);
			case "[Z" : return Array.getBoolean(array, index);
			case "[C" : return Array.getChar(array, index);
			default : return ((Object[]) array)[index];
		}
	}
}
