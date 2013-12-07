package pk.labs.LabA.utils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;

public class BeanUtils {

	public static void copyProperties(Object src, Object dest) {
		try {
			BeanInfo srcBeanInfo = Introspector.getBeanInfo(src.getClass());
			BeanInfo destBeanInfo = Introspector.getBeanInfo(dest.getClass());
			
			PropertyDescriptor[] srcPropsDesc = srcBeanInfo.getPropertyDescriptors();
			PropertyDescriptor[] destPropsDesc = destBeanInfo.getPropertyDescriptors();
			
			for (PropertyDescriptor srcProp : srcPropsDesc) {
				if (srcProp.getReadMethod() != null) {
					for (PropertyDescriptor destProp : destPropsDesc)
						if (destProp.getName().equals(srcProp.getName())) {
							if (destProp.getWriteMethod() != null) {
								try {
									Object value = srcProp.getReadMethod().invoke(src, new Object[0]);
									destProp.getWriteMethod().invoke(dest, value);
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
							break;
						}
				}
			}
		} catch (IntrospectionException e) {
			e.printStackTrace();
		}
	}
	
	public static boolean compare(Object src, Object dest) {
		if (compareProperties(src, dest))
			return compareProperties(dest, src);
		return false;
	}
	
	private static boolean compareProperties(Object src, Object dest) {
		try {
			BeanInfo srcBeanInfo = Introspector.getBeanInfo(src.getClass());
			BeanInfo destBeanInfo = Introspector.getBeanInfo(dest.getClass());
			
			PropertyDescriptor[] srcPropsDesc = srcBeanInfo.getPropertyDescriptors();
			PropertyDescriptor[] destPropsDesc = destBeanInfo.getPropertyDescriptors();
			
			for (PropertyDescriptor srcProp : srcPropsDesc) {
				if (srcProp.getReadMethod() != null) {
					boolean found = false;
					for (PropertyDescriptor destProp : destPropsDesc) {
						if (destProp.getName().equals(srcProp.getName())) {
							if (destProp.getReadMethod() != null) {
								try {
									Object value1 = srcProp.getReadMethod().invoke(src, new Object[0]);
									Object value2 = destProp.getReadMethod().invoke(src, new Object[0]);
									if (value1 == null && value2 != null)
										return false;
									if (value1 != null && value2 == null)
										return false;
									if (value1 != null && value2 != null)
										if (!value1.equals(value2))
											if (!compare(value1, value2))
												return false;
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
							else {
								return false;
							}
							found = true;
							break;
						}
					}
					if (!found)
						return false;
				}
			}
		} catch (IntrospectionException e) {
			e.printStackTrace();
		}
		return true;
	}
}
