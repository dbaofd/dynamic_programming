package joballocation;

import java.util.List;

/**
 * A class representing a solution consisting of a list of chosen shifts and
 * jobs.
 * 
 * DO NOT MODIFY THIS FILE IN ANY WAY.
 */
public class Solution {

    private List<Shift> chosenShifts;
    private List<Job> chosenJobs;

    public Solution(List<Shift> chosenShifts, List<Job> chosenJobs) {
        this.chosenShifts = chosenShifts;
        this.chosenJobs = chosenJobs;
    }

    public List<Shift> getChosenShifts() {
        return chosenShifts;
    }

    public List<Job> getChosenJobs() {
        return chosenJobs;
    }

    /**
     * @require cost != null && the solution is valid for these costs
     * @ensure Returns this solution's profit.
     */
    public int profit(int[] cost) {
        int result = 0;
        for (Shift shift : chosenShifts) {
            result = result - shift.cost(cost);
        }
        for (Job job : chosenJobs) {
            result = result + job.payment();
        }
        System.out.println(result);
        return result;
    }

    @Override
    public String toString() {
        return "chosen shifts: " + chosenShifts + System.lineSeparator()
                + "chosen jobs: " + chosenJobs;
    }

}
