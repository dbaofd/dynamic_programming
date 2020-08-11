package joballocation;

public class Recursive {

    /**
     * @require The array cost is not null, and all the integers in the array
     * (the daily costs of the worker) are greater than or equal to
     * zero. (The number of days that you are running the company is
     * defined to be n = cost.length).
     * <p>
     * The minimum number of days between shifts is greater than or
     * equal to one (1 <= minShiftBreak). The maximum shift length is
     * greater than or equal to one (1 <= maxShiftLength).
     * <p>
     * The array jobs is not null, and does not contain null values.
     * The jobs are sorted in ascending order of their end days. The
     * end day of every job must be strictly less than the length of
     * the cost array (n = cost.length).
     * @ensure Returns the maximum profit that can be earned by you for your
     * company given parameters cost, minShiftBreak, maxShiftLength and
     * jobs.
     * <p>
     * (See handout for details.)
     * <p>
     * This method must be implemented using a recursive programming
     * solution to the problem. It is expected to have a worst-case
     * running time that is exponential in m = jobs.length. (You must
     * NOT provide a dynamic programming solution to this question.)
     */
    public static int maximumProfitRecursive(int[] cost, int minShiftBreak,
                                             int maxShiftLength, Job[] jobs) {
        // IMPLEMENT THIS METHOD BY IMPLEMENTING THE PRIVATE METHOD IN THIS
        // CLASS THAT HAS THE SAME NAME
        return maximumProfitRecursive(cost, minShiftBreak, maxShiftLength, jobs,
                0, jobs.length, jobs.length);
    }

    /**
     * @require The array cost is not null, and all the integers in the array
     * (the daily costs of the worker) are greater than or equal to
     * zero. (The number of days that you are running the company is
     * defined to be n = cost.length).
     * <p>
     * The minimum number of days between shifts is greater than or
     * equal to one (1 <= minShiftBreak). The maximum shift length is
     * greater than or equal to one (1 <= maxShiftLength).
     * <p>
     * The array jobs is not null, and does not contain null values.
     * The jobs are sorted in ascending order of their end days. The
     * end day of every job must be strictly less than the length of
     * the cost array (n = cost.length).
     * <p>
     * Additionally:
     * <p>
     * (0 <= i <= jobs.length) and (0 <= j <= k <= jobs.length) and (if
     * j != jobs.length then 0 <= j <= k < i)
     * @ensure Returns the maximum profit that can be earned by you for your
     * company given that:
     * <p>
     * (i): You can only select job opportunities from index i onwards
     * in the list of jobs; and
     * <p>
     * (ii) If j != jobs.length, then you cannot choose a job that
     * starts earlier than day jobs[k].end() + 1; and
     * <p>
     * (iii): If j != jobs.length, then you must select a shift that
     * starts on day jobs[j].start(), and ends no earlier than end day
     * jobs[k].end(). Since you must select a shift of this nature, you
     * have an obligation to pay for it, and take it into consideration
     * when you are selecting any further shifts to include.
     * <p>
     * (See handout for details.)
     * <p>
     * This method must be implemented using a recursive programming
     * solution to the problem. It is expected to have a worst-case
     * running time that is exponential. (You must NOT provide a dynamic
     * programming solution to this question.)
     */
    private static int maximumProfitRecursive(int[] cost, int minShiftBreak,
                                              int maxShiftLength, Job[] jobs, int i, int j, int k) {
        int m = jobs.length;
        int currentJobProfit = 0;
        int a, b;
        if (i != m) {
            if (j == m) {//j==m means there is no job in the job selection.
                //if job i's period is smaller than maxShiftLength, then we can select it
                if (jobs[i].end() - jobs[i].start() + 1 <= maxShiftLength) {
                    currentJobProfit = computeProfitForCurrentJob(jobs[i].start(), jobs[i].end(), cost, i, jobs);
                    return Math.max(currentJobProfit + maximumProfitRecursive(cost, minShiftBreak, maxShiftLength, jobs, i + 1, i, i),
                            maximumProfitRecursive(cost, minShiftBreak, maxShiftLength, jobs, i + 1, j, k));
                } else {//the job i's period is greater than maxShiftLength,just skip job i
                    return maximumProfitRecursive(cost, minShiftBreak, maxShiftLength, jobs, i + 1, j, k);
                }
            } else if (j != m) {//when job selection is not empty, job j's start day to job k's end day is the current shift.
                //since the jobs in jobs array is sorted by end day in ascend order, just traverse the array from i.
                //In order to find a job that satisfy the following conditions.
                for (int index = i; index < m; index++) {
                    //k is the last job in the shift, j is the first one.
                    //we need to find the next job from i which could be added in the job selection,
                    //if job index's start day is greater than job k's end day
                    //and the job period cannot greater than maxShiftLength
                    if ((jobs[k].end() < jobs[index].start()) &&
                            (jobs[index].end() - jobs[index].start() + 1 <= maxShiftLength)) {
                        //if add this job, the length of expanded shift is still smaller than maxShiftLength,
                        //and also there is a big gap between job index's start day and job k's end day which means
                        // we can just start a new shift to save cost.
                        if ((jobs[index].end() - jobs[j].start() + 1 <= maxShiftLength) &&
                                (jobs[k].end() + minShiftBreak + 1 <= jobs[index].start())) {
                            //The current job profit is the profit that job index can have.
                            currentJobProfit = computeProfitForCurrentJob(jobs[index].start(), jobs[index].end(), cost, index, jobs);
                            //we got two option, include the job index or not, if it is included, then we need to update j,k for the next
                            //sub problem, otherwise just keep j,k
                            return Math.max(currentJobProfit + maximumProfitRecursive(cost, minShiftBreak, maxShiftLength, jobs, index + 1, index, index),
                                    maximumProfitRecursive(cost, minShiftBreak, maxShiftLength, jobs, index + 1, j, k));

                            //if adding this job won't cause the expanded shift greater than maxShiftLength
                        }else if (jobs[index].end() - jobs[j].start() + 1 <= maxShiftLength) {
                            //compute the profit of job index, we can get its payment easily, and it should be responsible for
                            //the cost from day (job k's end day+1) to the job index's end day
                            currentJobProfit = computeProfitForCurrentJob(jobs[k].end() + 1, jobs[index].end(), cost, index, jobs);
                            //if it is included, then update k to index, otherwise don't update it.
                            return Math.max(currentJobProfit + maximumProfitRecursive(cost, minShiftBreak, maxShiftLength, jobs, index + 1, j, index),
                                    maximumProfitRecursive(cost, minShiftBreak, maxShiftLength, jobs, index + 1, j, k));
                            //if the start day of the job index is greater or equal to (job k's end day+minShiftBreak+1)
                            //then add it, but this time will start a new shift.
                        } else if (jobs[k].end() + minShiftBreak + 1 <= jobs[index].start()) {
                            //compute the profit of job index, and it should be responsible for the cost from job index's
                            //start day to end day, cause it is a new shift.
                            currentJobProfit = computeProfitForCurrentJob(jobs[index].start(), jobs[index].end(), cost, index, jobs);
                            //if it is included, then update j k with index, the new shift is from job index's start day to its end day
                            return Math.max(currentJobProfit + maximumProfitRecursive(cost, minShiftBreak, maxShiftLength, jobs, index + 1, index, index),
                                    maximumProfitRecursive(cost, minShiftBreak, maxShiftLength, jobs, index + 1, j, k));
                        }
                    }
                }
            }
        }
        //return 0 when the recurrence finished visiting all the jobs
        return 0;
    }

    public static int computeProfitForCurrentJob(int j, int k, int[] cost, int jobIndex, Job[] jobs) {
        int sum = 0;
        for (int i = j; i <= k; i++) {
            sum += cost[i];
        }
        return jobs[jobIndex].payment() - sum;
    }
    /**
     * Summary of the algorithm
     * the core point of this algorithm is to dynamically compute the current job profit based on two
     * different situations.
     *
     * When the formal shift has been extended, then compute the profit that the added job contributes
     * in this way: (current job's payment) - sum up cost of (job k's end day to current job's end day);
     *
     * if the formal shift is replaced with a brand new shift, then compute the profit that the added
     * job contributes in this way:
     * (current job's payment) - sum up cost of (current job's end day to current job's end day)
     *
     * The algorithm skip incompatible jobs by doing for loop until find a job that can be considered to add in.
     */

}
