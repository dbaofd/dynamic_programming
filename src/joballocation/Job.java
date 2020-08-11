package joballocation;

/**
 * A class representing a job, with a start day, end day, and payment (in whole
 * dollars).
 * 
 * The start and end day must be greater than or equal to zero, and the start
 * day must be less than or equal to the end day. The payment must be greater
 * than zero dollars.
 * 
 * DO NOT MODIFY THIS FILE IN ANY WAY.
 */
public class Job {

    // The start day of the job.
    private final int start;
    // The end day of the job.
    private final int end;
    // Payment (in whole dollars) that would be received for completing the job.
    private final int payment;

    /**
     * @require 0 <= start && start <= end && 0 < payment
     * 
     * @ensure Creates a new job with the given start day, end day and payment.
     */
    public Job(int start, int end, int payment) {
        if (start < 0 || start > end) {
            throw new IllegalArgumentException(
                    "The start day must be greater than or equal to zero, "
                            + "and the start day must be less than "
                            + "or equal to the end day.");
        }
        if (payment <= 0) {
            throw new IllegalArgumentException(
                    "Payment must be greater than zero.");
        }
        this.start = start;
        this.end = end;
        this.payment = payment;
    }

    /**
     * @ensure Returns the day the job starts.
     */
    public int start() {
        return start;
    }

    /**
     * @ensure Returns the day the job ends.
     */
    public int end() {
        return end;
    }

    /**
     * @ensure Returns the payment for the job.
     */
    public int payment() {
        return payment;
    }

    /**
     * @require job !=null
     * @ensure Returns true when this job is compatible with the given job, and
     *         false otherwise.
     */
    public boolean compatible(Job job) {
        return this.end < job.start || job.end < this.start;
    }

    /**
     * @ensure Returns the total number of days that the job takes.
     */
    public int length() {
        return (end - start + 1);
    }

    /**
     * @require shift != null
     * @return True if and only if this job can be completed during the given
     *         shift.
     */
    public boolean completableDuringShift(Shift shift) {
        return (shift.start() <= this.start && this.end <= shift.end());
    }

    @Override
    public String toString() {
        return "(" + start + ", " + end + ", " + payment + ")";
    }

}
