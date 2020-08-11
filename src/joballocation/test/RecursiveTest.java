package joballocation.test;

import joballocation.Job;
import joballocation.Recursive;
import org.junit.Assert;
import org.junit.Test;

/**
 * Basic tests for the {@link Recursive} implementation class.
 * 
 * We will use a more comprehensive test suite to test your code, so you should
 * add your own tests to this test suite to help you to debug your
 * implementation.
 */
public class RecursiveTest {

    /**
     * Basic test of the Recursive.maximumProfitRecursive method from handout.
     */
    @Test
    public void maxProfitHandoutTest() {
        /* initialise the inputs used for testing */
        int[] cost = { 1, 2, 2, 2, 3, 0, 5, 1, 3, 2, 2, 3, 1 };
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
        int actualMaxProfit = Recursive.maximumProfitRecursive(cost,
                minShiftBreak, maxShiftLength, jobs);
        System.out.println(actualMaxProfit);
        Assert.assertEquals(expectedMaxProfit, actualMaxProfit);
    }

}
