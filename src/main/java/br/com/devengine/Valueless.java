package br.com.devengine;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

public final class Valueless {

    public static boolean notEmptyOrNull(Object... object) {
        return Arrays.asList(object).stream().allMatch(ob -> notEmptyOrNull(ob));
    }

    public static boolean notEmptyOrNull(Object object) {
        if (object != null) {
            if (object.getClass().getSuperclass() == List.class) {
                return !((List) object).isEmpty();
            } else if (object.getClass().getSuperclass() == Vector.class) {
                return !((Vector) object).isEmpty();
            }
            return (object != null && !"".equals(object));
        } else {
            return false;
        }
    }

    public static <T, R> R notEmptyOrNullExecute(T object, String method) {
        try {
            String[] mts = method.split("\\.");
            if (mts.length == 1) {
                Method mth = object.getClass().getDeclaredMethod(method);
                return (R) mth.invoke(object);
            } else {
                Object ob = object.getClass().getDeclaredMethod(mts[0]).invoke(object);
                for (int i = 1; i < mts.length; i++) {
                    if (notEmptyOrNull(ob)) {
                        Method mth = ob.getClass().getDeclaredMethod(mts[i]);
                        ob = mth.invoke(ob);
                    }
                }
                return (R) ob;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
