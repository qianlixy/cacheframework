package io.github.qianlixy.smartcache.utils;

import org.junit.Test;

import io.github.qianlixy.smartcache.utils.BaseDataTypeArrayUtil;

public class BaseDataTypeArrayUtilTest {

	@Test
	public void test() {
		Object intArray = new int[]{1, 2, 3};
		Object byteArray = new byte[]{1, 2, 3};
		Object shortArray = new short[]{1, 2, 3};
		Object longArray = new long[]{1L, 2L, 3L};
		Object floatArray = new float[]{1.0F, 2.0F, 3.0F};
		Object doubleArray = new double[]{1.0, 2.0, 3.0};
		Object booleanArray = new boolean[]{true, false, true};
		Object charArray = new char[]{'a', 'b', 'a'};
		Object StringArray = new String[]{"a", "b", "c"};
		System.out.println(BaseDataTypeArrayUtil.arrayValue(intArray, 1));
		System.out.println(BaseDataTypeArrayUtil.arrayValue(byteArray, 1));
		System.out.println(BaseDataTypeArrayUtil.arrayValue(shortArray, 1));
		System.out.println(BaseDataTypeArrayUtil.arrayValue(longArray, 1));
		System.out.println(BaseDataTypeArrayUtil.arrayValue(floatArray, 1));
		System.out.println(BaseDataTypeArrayUtil.arrayValue(doubleArray, 1));
		System.out.println(BaseDataTypeArrayUtil.arrayValue(booleanArray, 1));
		System.out.println(BaseDataTypeArrayUtil.arrayValue(charArray, 1));
		System.out.println(BaseDataTypeArrayUtil.arrayValue(StringArray, 1));
		
		int length = BaseDataTypeArrayUtil.arrayLength(intArray);
		for(int i=0; i<length; i++) {
			System.out.println(BaseDataTypeArrayUtil.arrayValue(intArray, i));
		}
	}
	
}
