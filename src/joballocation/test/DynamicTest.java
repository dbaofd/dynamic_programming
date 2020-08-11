package joballocation.test;

import joballocation.Dynamic;
import joballocation.Job;
import joballocation.Shift;
import joballocation.Solution;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Basic tests for the {@link Dynamic} implementation class.
 * <p>
 * We will use a more comprehensive test suite to test your code, so you should
 * add your own tests to this test suite to help you to debug your
 * implementation.
 */
public class DynamicTest {

    /**
     * Basic test of the Dynamic.maximumProfitDynamic method from handout.
     */
    @Test
    public void maxProfitHandoutTest() {
        /* initialise the inputs used for testing */
        int[] cost = {1, 2, 2, 2, 3, 0, 5, 1, 3, 2, 2, 3, 1};
        int n = cost.length;
        int m = 9;
        int minShiftBreak = 2;
        int maxShiftLength = 6;
        Job[] jobs = new Job[m];
        jobs[0] = new Job(1, 1, 5);
        jobs[1] = new Job(0, 2, 15);
        jobs[2] = new Job(4, 4, 9);
        jobs[3] = new Job(2, 5, 17);
        jobs[4] = new Job(5, 5, 5);
        jobs[5] = new Job(5, 6, 12);
        jobs[6] = new Job(8, 8, 2);
        jobs[7] = new Job(11, 12, 5);
        jobs[8] = new Job(11, 12, 6);

        /* compare expected to actual results */
        int expectedMaxProfit = 21;
        int actualMaxProfit = Dynamic.maximumProfitDynamic(cost, minShiftBreak,
                maxShiftLength, jobs);
        System.out.println(actualMaxProfit);
        Assert.assertEquals(expectedMaxProfit, actualMaxProfit);
    }

    /**
     * Basic test of the Dynamic.optimalSolutionDynamic method from handout.
     */
    @Test
    public void optimalSolutionHandoutTest() {
        /* initialise the inputs used for testing */
        int[] cost = {1, 2, 2, 2, 3, 0, 5, 1, 3, 2, 2, 3, 1};
        int n = cost.length;
        int m = 9;
        int minShiftBreak = 2;
        int maxShiftLength = 6;
        Job[] jobs = new Job[m];
        jobs[0] = new Job(1, 1, 5);
        jobs[1] = new Job(0, 2, 15);
        jobs[2] = new Job(4, 4, 9);
        jobs[3] = new Job(2, 5, 17);
        jobs[4] = new Job(5, 5, 5);
        jobs[5] = new Job(5, 6, 12);
        jobs[6] = new Job(8, 8, 2);
        jobs[7] = new Job(11, 12, 5);
        jobs[8] = new Job(11, 12, 6);

        /* compare expected to actual results */
        Solution solution = Dynamic.optimalSolutionDynamic(cost, minShiftBreak,
                maxShiftLength, jobs);
        int expectedMaxProfit = 21;
        int actualMaxProfit = solution.profit(cost);

        // check that allocation is valid and that the profit from it is maximal
        checkValidAllocation(cost, minShiftBreak, maxShiftLength, jobs,
                solution.getChosenShifts(), solution.getChosenJobs());
        Assert.assertEquals(expectedMaxProfit, actualMaxProfit);
    }

    /*------------helper methods------------*/

    /**
     * Checks that the chosen shifts and chosen jobs are valid given the
     * parameters to the algorithm.
     */
    private static void checkValidAllocation(int[] cost, int minShiftBreak,
                                             int maxShiftLength, Job[] jobs, List<Shift> chosenShifts,
                                             List<Job> chosenJobs) {

        /*
         * Check that each chosen job is chosen from the list of available jobs
         */
        for (int i = 0; i < chosenJobs.size(); i++) {
            Assert.assertTrue(
                    (Arrays.asList(jobs)).contains(chosenJobs.get(i)));
        }

        /*
         * Check that the list of chosen jobs is ordered in ascending order of
         * end day, and that the chosen jobs are compatible.
         */
        for (int i = 1; i < chosenJobs.size(); i++) {
            Assert.assertTrue(
                    chosenJobs.get(i - 1).end() < chosenJobs.get(i).start());
        }

        /*
         * Check that the list of chosen shifts is ordered in ascending order of
         * end day, and that the chosen shifts do not overlap, and the worker
         * has at least minShiftBreak free days between shifts.
         */
        for (int i = 1; i < chosenShifts.size(); i++) {
            Assert.assertTrue(chosenShifts.get(i - 1).end()
                    + minShiftBreak < chosenShifts.get(i).start());
        }

        /*
         * Check that each shift lasts for less than or equal to maxShiftLength
         * days and that each shift starts and ends on days that you are in
         * charge of the company (day 0 to day cost.length-1).
         */
        for (int i = 0; i < chosenShifts.size(); i++) {
            Assert.assertTrue(chosenShifts.get(i).length() <= maxShiftLength);
            Assert.assertTrue(chosenShifts.get(i).end() < cost.length);
        }

        /*
         * Check that each chosen job is able to be completed during one of the
         * chosen shifts.
         */
        for (Job job : chosenJobs) {
            Shift compatibleShift = null;
            for (Shift shift : chosenShifts) {
                if (job.completableDuringShift(shift)) {
                    compatibleShift = shift;
                }
            }
            Assert.assertTrue(compatibleShift != null);
        }
    }

}
