package joballocation;

/**
 * A class representing a shift, with a start day and an end day.
 * 
 * The start and end day must be greater than or equal to zero, and the start
 * day must be less than or equal to the end day.
 * 
 * DO NOT MODIFY THIS FILE IN ANY WAY.
 */
public class Shift {

    // The start day of the shift
    private final int start;
    // The end day of the shift
    private final int end;

    /**
     * @require 0 <= start && start <= end
     * 
     * @ensure Creates a new shift with the given start day and end day.
     */
    public Shift(int start, int end) {
        if (start < 0 || start > end) {
            throw new IllegalArgumentException(
                    "The start day must be greater than or equal to zero, "
                            + "and the start day must be less than "
                            + "or equal to the end day.");
        }
        this.start = start;
        this.end = end;
    }

    /**
     * @ensure Returns the day the shift starts.
     */
    public int start() {
        return start;
    }

    /**
     * @ensure Returns the day the shift ends.
     */
    public int end() {
        return end;
    }

    /**
     * @ensure Returns the total number of days that the shift takes.
     */
    public int length() {
        return (end - start + 1);
    }

    /**
     * @require shift != null && 0 <= minShiftBreak
     * @ensure Returns true if and only if this shift and the given shift do not
     *         overlap and the worker has at least minShiftBreak free days
     *         between these shifts.
     */
    public boolean compatible(Shift shift, int minShiftBreak) {
        return this.end + minShiftBreak < shift.start
                || shift.end + minShiftBreak < this.start;
    }

    /**
     * @require cost != null && end < cost.length
     * @ensure Returns the cost of the shift given the supplied costs.
     */
    public int cost(int[] cost) {
        int result = 0;
        for (int i = start; i <= end; i++) {
            result += cost[i];
        }
        return result;
    }

    @Override
    public String toString() {
        return "(" + start + ", " + end + ")";
    }

}
