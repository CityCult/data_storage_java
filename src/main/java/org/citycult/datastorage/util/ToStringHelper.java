package org.citycult.datastorage.util;

import java.util.LinkedList;

/**
 * Helps to have consistent format for toString(). The implementation is null-safe.
 *
 * @author Christof Pieloth
 */
public class ToStringHelper {
    private final String type;
    private final LinkedList<Attribute> attributes = new LinkedList<>();

    public ToStringHelper(Object obj){
        if(obj != null)
            this.type = obj.getClass().getSimpleName();
        else
            this.type = null;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(type);
        sb.append("[");
        for(Attribute a : attributes) {
            sb.append(a).append(", ");
        }
        sb.delete(sb.length() - 2, sb.length());
        sb.append("]");
        return sb.toString();
    }

    static public ToStringHelper add(Object obj, String name, Object value) {
        ToStringHelper helper = new ToStringHelper(obj);
        helper.add(name, value);
        return helper;
    }

    public ToStringHelper add(String name, Object value) {
        attributes.add(new Attribute(name, value));
        return this;
    }

    public ToStringHelper add(String name, Object value, int max) {
        final String string = value != null ? value.toString() : "";
        String shortValue = "";
        if (value != null && !"".equals(value))
            shortValue = string.substring(0, string.length() > max ? max : string.length()) + " ...";
        return this.add(name, shortValue);
    }

    public ToStringHelper addShort(String name, Object value) {
        return this.add(name, value, 8);
    }

    private static class Attribute {
        private final String name;
        private final String value;

        public Attribute(String name, Object obj) {
            this.name = name;
            if(obj != null) {
                this.value = obj instanceof String ? "\"" + obj.toString() + "\"" : obj.toString();
            }
            else
                this.value = "";
        }

        @Override
        public String toString() {
            return name + "=" + value;
        }
    }

}
