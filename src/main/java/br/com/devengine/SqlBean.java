package br.com.devengine;

import java.beans.Transient;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface SqlBean {

    /*public default Map<String, Object> getParamList() {
        try {
            Map<String, Object> resultado = new HashMap();
            String prefix = "param";
            for (Field field : getClass().getDeclaredFields()) {
                field.setAccessible(true);
                Object ob = field.get(this);
                if (ob != null && !field.getName().equals("serialVersionUID") && !field.getName().contains("this")) {
                    Method m = getMethod(field);
                    if (!ob.equals("") && !m.isAnnotationPresent(Transient.class)) {
                        if (field.getType() == String.class) {
                            String valor = ""
                                    .concat("%")
                                    .concat(ob.toString().toLowerCase().replaceAll("\\s+", "%"))
                                    .concat("%");
                            resultado.put(prefix.concat(field.getName().replaceFirst(field.getName().substring(0, 1), field.getName().substring(0, 1).toUpperCase())), valor);
                        } else if (field.getType() == List.class) {
                            if (((List) ob).size() <= 0) {
                                //terminar
                            }
                        } else {
                            resultado.put(prefix.concat(field.getName().replaceFirst(field.getName().substring(0, 1), field.getName().substring(0, 1).toUpperCase())), ob);
                        }
                    }
                }
            }
            return resultado;
        } catch (Exception e) {
            return null;
        }
    }

    public default Method getMethod(Field field) {
        try {
            String prefix = "get";
            if (field.getType() == Boolean.class || field.getType() == boolean.class) {
                prefix = "is";
            }
            return getClass().getDeclaredMethod(prefix.concat(WordUtils.capitalize(field.getName())));
        } catch (Exception e) {
            return null;
        }
    }

    public default String getSQL_Select() {
        try {
            String prefix = getClass().getSimpleName().substring(0, 2).toLowerCase();
            String resultado = "SELECT "
                    .concat(prefix).concat(" FROM ")
                    .concat(getClass().getSimpleName())
                    .concat(" AS ").concat(prefix);
            return resultado;
        } catch (Exception e) {
            return "";
        }
    }

    public default String getSQL_Where(String prefix) {
        try {
            String resultado = "";
            for (Field field : getClass().getDeclaredFields()) {
                field.setAccessible(true);
                Object ob = field.get(this);
                if (ob != null && !field.getName().equals("serialVersionUID") && !field.getName().contains("this")) {
                    Method m = getMethod(field);
                    if (!ob.equals("") && !m.isAnnotationPresent(Transient.class)) {
                        if (field.getType() == List.class) {
                            if (((List) ob).size() <= 0) {
                                continue;
                            }
                        }
                        resultado = getMapSqlField(resultado, field, prefix);
                    }
                }
            }
            return resultado;
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Este método monta um select com todos os dados setados no objeto
     * retirando os com valores nulos.
     *
     * @return String com uma instrução SQL com WHERE
     */

    /*
    public default String getSQL() {
        try {
            return this.getSQL_Select().concat(" ").concat(this.getSQL_Where(null));
        } catch (Exception e) {
            return "";
        }
    }

    public default String getInsertSQL() {
        try {
            String insert = "INSERT INTO " + getClass().getAnnotation(Table.class).name() + "";
            String columns = " (";
            String values = ") VALUES (";
            for (Method m : getClass().getDeclaredMethods()) {
                if (m.isAnnotationPresent(Column.class)) {
                    Column column = m.getAnnotation(Column.class);
                    Object ob = m.invoke(this);
                    if (ob != null) {
                        columns += column.name() + ",";
                        values += "?,";
                    }
                }
            }
            columns = columns.substring(0, columns.length() - 1);
            values = values.substring(0, values.length() - 1);
            insert = insert + columns + values + ")";
            return insert;
        } catch (Exception e) {
            return "";
        }
    }

    public default String getMapSqlField(String resultado, Field field, String prefix) {
        try {
            if (field.isAnnotationPresent(MapSqlField.class)) {
                MapSqlField map = field.getAnnotation(MapSqlField.class);
                if (!map.ignore()) {
                    return mountWhere(resultado, field, prefix, map);
                } else {
                    return resultado;
                }
            } else {
                return mountWhere(resultado, field, prefix, null);
            }
        } catch (Exception e) {
            return resultado;
        }
    }

    public default String mountWhere(String resultado, Field field, String prefix, MapSqlField map) {
        try {
            String p = "";
            if (map == null || map.as_prefix().equals("")) {
                p = prefix == null ? getClass().getSimpleName().substring(0, 2).concat(".").toLowerCase() : prefix.concat(".");
            } else {
                p = map.as_prefix().concat(".");
            }
            String param = "param";

            resultado = mountPrefixWhere(resultado, (map != null ? p.concat(map.mapeamento()) : p.concat(field.getName())), field.getType());

            if (map != null) {
                switch (map.condition()) {
                    case "=":
                    case ">":
                    case "<":
                    case ">=":
                    case "<=":
                    case "<>":
                    case "!=":
                        resultado = resultado.concat(" ".concat(map.condition()).concat(" ").concat(":".concat(param.concat(field.getName().replaceFirst(field.getName().substring(0, 1), field.getName().substring(0, 1).toUpperCase())))));
                        break;
                    case "between":
                        resultado = resultado.concat(" BETWEEN ".concat(":".concat(param.concat(field.getName().replaceFirst(field.getName().substring(0, 1), field.getName().substring(0, 1).toUpperCase())))));
                        resultado = resultado.concat(" AND ".concat(":".concat(param.concat(map.between_partiner().replaceFirst(map.between_partiner().substring(0, 1), map.between_partiner().substring(0, 1).toUpperCase())))));
                        break;
                    case "like":
                        resultado = resultado.replace(p.concat(field.getName()), "LOWER(".concat(p.concat(field.getName())).concat(")"));
                        resultado = resultado.concat(" LIKE :".concat(param.concat(field.getName().replaceFirst(field.getName().substring(0, 1), field.getName().substring(0, 1).toUpperCase()))));
                        break;
                    default:
                        resultado = getDefaultWhereClausule(resultado, field.getName(), field.getType());
                }
            } else {
                resultado = getDefaultWhereClausule(resultado, field.getName(), field.getType());
            }
            return resultado;
        } catch (Exception e) {
            return resultado;
        }
    }

    public default String getDefaultWhereClausule(String resultado, String campo, Class type) {
        try {
            if (type == String.class) {
                resultado = resultado.concat(" LIKE :".concat("param".concat(campo.replaceFirst(campo.substring(0, 1), campo.substring(0, 1).toUpperCase()))));
            } else {
                resultado = resultado.concat(" = :".concat("param".concat(campo.replaceFirst(campo.substring(0, 1), campo.substring(0, 1).toUpperCase()))));
            }
            return resultado;
        } catch (Exception e) {
            return resultado;
        }
    }

    public default String mountPrefixWhere(String resultado, String campo, Class type) {
        try {
            resultado = resultado.equals("") ? resultado.concat("WHERE ").concat(campo) : resultado.concat(" AND ").concat(campo);
            if (type == String.class) {
                resultado = resultado.replace(campo, "LOWER(".concat(campo).concat(")"));
            }
            return resultado;
        } catch (Exception e) {
            return resultado;
        }
    }

     */

    /*
    public default Object getIdValue() {
        try {
            for (Method m : getClass().getDeclaredMethods()) {
                /*if (m.isAnnotationPresent(Id.class)) {
                    return m.invoke(this);
                }*/
    /*
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }
    */
}
