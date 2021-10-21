package qubu;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Criterion class set criterion used for filtering data
 */
public class Criterion {
    public static final String AND = "AND";
    public static final String OR = "OR";

    private final String criterion;
    private String method;

    private Criterion(String criterion) {
        this.criterion = criterion;

        this.method = Criterion.AND; // Default value
    }

    private static String build(String... pieces) {
        return String.join(" ", pieces);
    }

    /**
     * Equality filter
     *
     * @param col1 Column at left of the filter
     * @param col2 Column at the right of the filter
     * @return a builder instance of the class
     */
    public static Criterion eq(String col1, String col2) {
        return new Criterion(build(col1, "=", col2));
    }

    /**
     * Not equality filter
     *
     * @param col1 Column at left of the filter
     * @param col2 Column at the right of the filter
     * @return a builder instance of the class
     */
    public static Criterion neq(String col1, String col2) {
        return new Criterion(build(col1, "!=", col2));
    }

    /**
     * Greater than filter
     *
     * @param col1 Column at left of the filter
     * @param col2 Column at the right of the filter
     * @return a builder instance of the class
     */
    public static Criterion gt(String col1, String col2) {
        return new Criterion(build(col1, ">", col2));
    }

    /**
     * Greater or equal than filter
     *
     * @param col1 Column at left of the filter
     * @param col2 Column at the right of the filter
     * @return a builder instance of the class
     */
    public static Criterion gte(String col1, String col2) {
        return new Criterion(build(col1, ">=", col2));
    }

    /**
     * Less than filter
     *
     * @param col1 Column at left of the filter
     * @param col2 Column at the right of the filter
     * @return a builder instance of the class
     */
    public static Criterion lt(String col1, String col2) {
        return new Criterion(build(col1, "<", col2));
    }

    /**
     * Less or equal than filter
     *
     * @param col1 Column at left of the filter
     * @param col2 Column at the right of the filter
     * @return a builder instance of the class
     */
    public static Criterion lte(String col1, String col2) {
        return new Criterion(build(col1, "<=", col2));
    }

    /**
     * IN filter
     *
     * @param col1   Column at left of the filter
     * @param values Values used in the IN filter
     * @return a builder instance of the class
     */
    public static Criterion in(String col1, String... values) {
        String val = Arrays.stream(values).collect(Collectors.joining(", ", "(", ")"));
        return new Criterion(build(col1, "IN", val));
    }

    /**
     * NOT IN filter
     *
     * @param col1   Column at left of the filter
     * @param values Values used in the IN filter
     * @return a builder instance of the class
     */
    public static Criterion nin(String col1, String... values) {
        String val = Arrays.stream(values).collect(Collectors.joining(", ", "(", ")"));
        return new Criterion(build(col1, "NOT IN", val));
    }

    /**
     * Between filter
     *
     * @param column Column to be evaluated
     * @param start  Value used to start evaluation
     * @param end    Value used to end evaluation
     * @return a builder instance of the class
     */
    public static Criterion between(String column, String start, String end) {
        return new Criterion(build(column, "BETWEEN", start, "AND", end));
    }

    /**
     * Sets the method that criterion need to be evaluated. Default method is in AND
     *
     * @param method define the method
     * @return a builder instance of the class
     */
    public Criterion method(String method) {
        this.method = method;

        return this;
    }

    /**
     * Returns the method used to evaluate the criterion
     *
     * @return Returns the method used to evaluate the criterion
     */
    public String getMethod() {
        return method;
    }


    /**
     * getSql returns generated criterion
     *
     * @return a String that represents the generated criterion
     */
    public String getSql() {
        return this.criterion;
    }

    @Override
    public String toString() {
        return this.getSql();
    }
}
