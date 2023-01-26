package org.citycult.datastorage.util;

public class JpaQueryHelper {

    public static final String SELECT = "SELECT";
    public static final String SELECT_ = SELECT + " ";

    public static final String SELECT_DISTINCT_ = SELECT + " DISTINCT ";

    public static final String FROM = "FROM";
    public static final String FROM_ = FROM + " ";
    public static final String _FROM_ = " " + FROM + " ";

    public static final String WHERE = "WHERE";
    public static final String _WHERE_ = " " + WHERE + " ";

    public static final String GROUPBY = "GROUP BY";
    public static final String _GROUPBY_ = " " + GROUPBY + " ";

    public static final String HAVING = "HAVING";
    public static final String _HAVING_ = " " + HAVING + " ";

    public static final String JOIN = "JOIN";
    public static final String _JOIN_ = " " + JOIN + " ";

    public static final String USING = "USING";
    public static final String _USING = " " + USING;

    public static final String UPDATE = "UPDATE";
    public static final String UPDATE_ = UPDATE + " ";

    public static final String SET = "SET";
    public static final String SET_ = SET + " ";
    public static final String _SET_ = " " + SET + " ";

    public static final String DELETE = "DELETE";
    public static final String DELETE_ = DELETE + " ";

    public static final String _AND_ = " AND ";
    public static final String _OR_ = " OR ";
    public static final String _BETWEEN_ = " BETWEEN ";

    public static final String PARAM_DATA_START = "ParamStartData";
    public static final String PARAM_DATA_END = "ParamEndData";

    /**
     * This method must be checked for JPA usage!
     */
    @Deprecated
    public static String getJoinUsing(String column) {
        return _USING + "(" + column.trim() + ")";
    }

    public static String selectFromWhere(String select, String from,
        String where) {
        StringBuilder sb = new StringBuilder();

        selectFromWhere(sb, select, from, where);

        return sb.toString();
    }

    public static StringBuilder selectFromWhere(final StringBuilder sb,
        String select, String from, String where) {
        sb.append(SELECT_);
        sb.append(select.trim());

        sb.append(_FROM_);
        sb.append(from.trim());

        if (where == null)
            where = "";

        sb.append(_WHERE_);
        sb.append(where.trim());

        return sb;

    }

    /**
     * This method must be checked for JPA usage!
     */
    @Deprecated
    public static String selectFromGroupHaving(String select, String from,
        String group, String having) {
        StringBuilder sb = new StringBuilder();

        sb.append(SELECT_);
        sb.append(select.trim());

        sb.append(_FROM_);
        sb.append(from.trim());

        sb.append(_GROUPBY_);
        sb.append(group.trim());

        if (having == null || "".equals(having))
            having = "TRUE";

        sb.append(_HAVING_);
        sb.append(having.trim());

        return sb.toString();
    }

    /**
     * This method must be checked for JPA usage!
     */
    @Deprecated
    public static String joinUsing(String left, String right, String column) {
        StringBuilder sb = new StringBuilder();

        sb.append(left.trim());
        sb.append(" AS l");

        sb.append(_JOIN_);

        sb.append(right.trim());
        sb.append(" AS r");

        sb.append(_USING);
        sb.append("(").append(column.trim()).append(")");

        return sb.toString();
    }

    /**
     * This method must be checked for JPA usage!
     */
    @Deprecated
    public static String deleteFromWhere(String from, String where) {
        StringBuilder sb = new StringBuilder();

        sb.append(DELETE_);

        sb.append(FROM_);
        sb.append(from.trim());

        if (where == null || "".equals(where))
            where = "TRUE";

        sb.append(_WHERE_);
        sb.append(where.trim());

        return sb.toString();
    }

    /**
     * This method must be checked for JPA usage!
     */
    @Deprecated
    public static String updateSetWhere(String from, String set, String where) {
        StringBuilder sb = new StringBuilder();

        sb.append(UPDATE_);
        sb.append(from.trim());

        sb.append(_SET_);
        sb.append(set.trim());

        if (where == null || "".equals(where))
            where = "TRUE";

        sb.append(_WHERE_);
        sb.append(where.trim());

        return sb.toString();
    }

    /**
     * Prepares a date range where condition. Use of PARAM_DATA_START and
     * PARAM_DATA_END for named parameters.
     *
     * @param startColumn Name of startDate column.
     * @param endColumn   Name of endDate column.
     * @return A condition string.
     */
    public static String getDateRangeCondition(String startColumn,
        String endColumn) {
        StringBuilder sb = new StringBuilder();

        sb.append(getDateRangeCondition(sb, startColumn, endColumn));

        return sb.toString();
    }

    public static StringBuilder getDateRangeCondition(final StringBuilder sb,
        String startColumn, String endColumn) {
        // range: r.start, r.end
        // event: e.start, e.end
        // (Condition1 OR Condition2 OR Condition3)

        // Open condition block
        sb.append("(");

        // Condition 1: r.start <= e.start <= r.end
        sb.append(startColumn);
        sb.append(_BETWEEN_);
        sb.append(":" + PARAM_DATA_START);
        sb.append(_AND_);
        sb.append(":" + PARAM_DATA_END);

        sb.append(_OR_);

        // Condition 2: r.start <= e.end <= r.end
        sb.append(endColumn);
        sb.append(_BETWEEN_);
        sb.append(":" + PARAM_DATA_START);
        sb.append(_AND_);
        sb.append(":" + PARAM_DATA_END);

        sb.append(_OR_);

        // Condition 3: e.start <= r.start && r.end <= e.end
        sb.append("(");
        sb.append(startColumn);
        sb.append(" <= ");
        sb.append(":" + PARAM_DATA_START);
        sb.append(_AND_);
        sb.append(":" + PARAM_DATA_END);
        sb.append(" <= ");
        sb.append(endColumn);
        sb.append(")");

        // Close condition block
        sb.append(")");

        return sb;
    }

}
