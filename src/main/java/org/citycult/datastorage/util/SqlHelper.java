package org.citycult.datastorage.util;

import java.sql.Timestamp;

public class SqlHelper {

    public static final String SELECT = "SELECT";
    public static final String SELECT_ = SELECT + " ";

    public static final String SELECT_DISTINCT_ = SELECT + " DISTINCT ";

    public static final String FROM = "FROM";
    public static final String FROM_ = FROM + " ";
    public static final String _FROM_ = " " + FROM + " ";

    public static final String WHERE = "WHERE";
    public static final String _WHERE_ = " " + WHERE + " ";

    public static final String ORDERBY = "ORDER BY";
    public static final String ORDERBY_ = ORDERBY + " ";
    public static final String _ORDERBY_ = " " + ORDERBY + " ";

    public static final String DESC = "DESC";
    public static final String _DESC = " " + DESC;

    public static final String GROUPBY = "GROUP BY";
    public static final String _GROUPBY_ = " " + GROUPBY + " ";

    public static final String HAVING = "HAVING";
    public static final String _HAVING_ = " " + HAVING + " ";

    public static final String JOIN = "JOIN";
    public static final String _JOIN_ = " " + JOIN + " ";

    public static final String USING = "USING";
    public static final String _USING = " " + USING;

    public static final String INSERT_INTO = "INSERT INTO";
    public static final String INSERT_INTO_ = INSERT_INTO + " ";

    public static final String VALUES = "VALUES";
    public static final String _VALUES_ = " " + VALUES + " ";

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

    public static String getJoinUsing(String column) {
        return _USING + "(" + column.trim() + ")";
    }

    public static String selectFromWhere(String select, String from,
        String where) {
        StringBuilder sb = new StringBuilder();

        sb.append(SELECT_);
        sb.append(select.trim());

        sb.append(_FROM_);
        sb.append(from.trim());

        if (where == null || "".equals(where))
            where = "TRUE";

        sb.append(_WHERE_);
        sb.append(where.trim());

        return sb.toString();
    }

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

    public static String getDateRangeCondition(DateHelper.DateRange range,
        String eStart, String eEnd) {
        // range: r.start, r.end
        // event: e.start, e.end
        Timestamp rStart = new Timestamp(range.getStart().getTime());
        Timestamp rEnd = new Timestamp(range.getEnd().getTime());

        // (Condition1 OR Condition2 OR Condition3)
        // Open condition block
        StringBuilder sbCondition = new StringBuilder();
        sbCondition.append("(");

        // Condition 1: r.start <= e.start <= r.end
        sbCondition.append(eStart);
        sbCondition.append(_BETWEEN_);
        sbCondition.append("'" + rStart + "'");
        sbCondition.append(_AND_);
        sbCondition.append("'" + rEnd + "'");

        sbCondition.append(_OR_);

        // Condition 2: r.start <= e.end <= r.end
        sbCondition.append(eEnd);
        sbCondition.append(_BETWEEN_);
        sbCondition.append("'" + rStart + "'");
        sbCondition.append(_AND_);
        sbCondition.append("'" + rEnd + "'");

        sbCondition.append(_OR_);

        // Condition 3: e.start <= r.start && r.end <= e.end
        sbCondition.append("(");
        sbCondition.append(eStart);
        sbCondition.append(" <= ");
        sbCondition.append("'" + rStart + "'");
        sbCondition.append(_AND_);
        sbCondition.append("'" + rEnd + "'");
        sbCondition.append(" <= ");
        sbCondition.append(eEnd);
        sbCondition.append(")");

        // Close condition block
        sbCondition.append(")");

        return sbCondition.toString();
    }

}
