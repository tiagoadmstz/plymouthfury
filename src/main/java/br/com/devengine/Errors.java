package br.com.devengine;

import java.util.function.Function;

public final class Errors {

    public static  <T, R> R tryCatchDefault(Function action, R defaultReturn) {
        try {
            return (R) action.apply(null);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return defaultReturn;
    }

}
