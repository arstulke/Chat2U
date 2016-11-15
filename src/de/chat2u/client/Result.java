package de.chat2u.client;

/**
 * Chat2U:
 * * de.chat2u.client.cucumber.steps:
 * * * Created by KAABERT on 15.11.2016.
 */
public class Result {
    public static final Result DENIED = new Result(403, "Access denied");
    public static final Result GRANTED = new Result(200, "Access granted");

    private final Object value;
    private final int error_code;

    public Result(int error_code, Object value) {
        this.error_code = error_code;
        this.value = value;
    }

    public Object getValue() {
        return value;
    }

    public int getError_code() {
        return error_code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Result result = (Result) o;

        return error_code == result.error_code;
    }

    @Override
    public int hashCode() {
        return error_code;
    }

    @Override
    public String toString() {
        return "Result{" +
                "value=" + value +
                ", error_code=" + error_code +
                '}';
    }
}
