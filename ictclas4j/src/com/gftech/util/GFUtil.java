package com.gftech.util;

import org.apache.jcs.JCS;
import org.apache.jcs.access.exception.CacheException;

public class GFUtil {

	public static boolean putIntoCache(JCS cache, Object name, Object obj) throws CacheException {
		if (cache != null && name != null && obj != null) {
			if ((name instanceof String) && ((String) name).endsWith(":"))
				return false;

			cache.put(name, obj);
		}

		return false;
	}

}
