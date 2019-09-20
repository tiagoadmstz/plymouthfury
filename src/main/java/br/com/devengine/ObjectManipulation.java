package br.com.devengine;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public interface ObjectManipulation<T> {

    public default void clear() {
        try {
            for (Field field : getClass().getDeclaredFields()) {
                field.setAccessible(true);
                if (!field.getName().equals("serialVersionUID") && !field.getName().equals("version") && !field.getName().contains("this")) {
                    if (field.getType() != boolean.class && field.getType() != Boolean.class) {
                        field.set(this, null);
                    } else if (field.getType() == boolean.class && field.getType() == Boolean.class) {
                        field.setBoolean(this, false);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public default T clearSensibleData(String... fields) {
        Arrays.asList(fields).stream().forEach(fld -> {
            try {
                Field f = getClass().getDeclaredField(fld);
                f.setAccessible(true);
                f.set(this, null);
            } catch (Exception e) {
            }
        });
        return (T) this;
    }

    public default void copiar(T object) {
        try {
            if (object != null) {
                for (Field field : getClass().getDeclaredFields()) {
                    field.setAccessible(true);
                    if (!field.getName().equals("serialVersionUID") && !field.getName().contains("this")) {
                        field.set(this, field.get(object));
                    }
                }
            } else {
                this.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public default T clonar() {
        try {
            T object = (T) getClass().getConstructor().newInstance();
            for (Field field : getClass().getDeclaredFields()) {
                field.setAccessible(true);
                if (!field.getName().equals("serialVersionUID") && !field.getName().contains("this")) {
                    Field f = object.getClass().getDeclaredField(field.getName());
                    f.setAccessible(true);
                    f.set(object, field.get(this));
                }
            }
            return object;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public default void update(T object) {
        try {
            for (Field field : getClass().getDeclaredFields()) {
                field.setAccessible(true);
                if (!field.getName().equals("serialVersionUID") && !field.getName().contains("this")) {
                    Field f = object.getClass().getDeclaredField(field.getName());
                    f.setAccessible(true);
                    if (field.get(this) != f.get(object)) {
                        field.set(this, f.get(object));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public default void convert(Object object) {
        try {
            List<Field> fields = new ArrayList();
            if (getClass().getSuperclass() != null) {
                fields.addAll(Arrays.asList(getClass().getSuperclass().getDeclaredFields()));
            }
            fields.addAll(Arrays.asList(getClass().getDeclaredFields()));
            for (Field field : fields) {
                field.setAccessible(true);
                Field tf = null;
                try {
                    tf = object.getClass().getDeclaredField(field.getName());
                } catch (Exception e) {
                    continue;
                }
                if (tf != null) {
                    tf.setAccessible(true);
                    if (tf.get(object) != null && !tf.getName().equals("serialVersionUID") && !field.getName().contains("this")) {
                        field.set(this, tf.get(object));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public default void printValues() {
        try {
            List<Field> fields = new ArrayList();
            if (getClass().getSuperclass() != null) {
                fields.addAll(Arrays.asList(getClass().getSuperclass().getDeclaredFields()));
            }
            fields.addAll(Arrays.asList(getClass().getDeclaredFields()));
            for (Field field : fields) {
                field.setAccessible(true);
                if (field.get(this) != null && !field.getName().equals("serialVersionUID") && !field.getName().contains("this")) {
                    System.out.println(field.getName() + " = " + field.get(this));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public default boolean isNull() {
        try {
            for (Field field : getClass().getDeclaredFields()) {
                field.setAccessible(true);
                if (field.get(this) != null && !field.getName().equals("serialVersionUID") && !field.getName().contains("this")) {
                    if (field.getType() == List.class) {
                        if (((List) field.get(this)).isEmpty()) continue;
                    } else if (field.getType() == String.class) {
                        if ("".equals((String) field.get(this))) continue;
                    }
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            return true;
        }
    }

    public default T getInstance() {
        try {
            for (Constructor construtor : getClass().getConstructors()) {
                if (construtor.getParameterCount() == 0) {
                    return (T) construtor.newInstance();
                } else {
                    return null;
                }
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

}
