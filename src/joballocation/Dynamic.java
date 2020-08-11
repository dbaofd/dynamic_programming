package joballocation;

import java.util.ArrayList;
import java.util.List;

public class Dynamic {

    /**
     * @require The array cost is not null, and all the integers in the array
     *          (the daily costs of the worker) are greater than or equal to
     *          zero. (The number of days that you are running the company is
     *          defined to be n = cost.length).
     * 
     *          The minimum number of days between shifts is greater than or
     *          equal to one (1 <= minShiftBreak). The maximum shift length is
     *          greater than or equal to one (1 <= maxShiftLength).
     * 
     *          The array jobs is not null, and does not contain null values.
     *          The jobs are sorted in ascending order of their end days. The
     *          end day of every job must be strictly less than the length of
     *          the cost array (n = cost.length).
     * 
     * @ensure Returns the maximum profit that can be earned by you for your
     *         company given parameters cost, minShiftBreak, maxShiftLength and
     *         jobs.
     * 
     *         (See handout for details.)
     * 
     *         This method must be implemented using an efficient bottom-up
     *         dynamic programming solution to the problem (not memoised).
     */
    public static int maximumProfitDynamic(int[] cost, int minShiftBreak,
            int maxShiftLength, Job[] jobs) {
        int [][][] table =maximumProfit(cost,minShiftBreak,maxShiftLength,jobs);
        return table[0][jobs.length][jobs.length];
    }

    /**
     * @require The array cost is not null, and all the integers in the array
     *          (the daily costs of the worker) are greater than or equal to
     *          zero. (The number of days that you are running the company is
     *          defined to be n = cost.length).
     * 
     *          The minimum number of days between shifts is greater than or
     *          equal to one (1 <= minShiftBreak). The maximum shift length is
     *          greater than or equal to one (1 <= maxShiftLength).
     * 
     *          The array jobs is not null, and does not contain null values.
     *          The jobs are sorted in ascending order of their end days. The
     *          end day of every job must be strictly less than the length of
     *          the cost array (n = cost.length).
     * 
     * @ensure Returns a valid selection of shifts and job opportunities that
     *         results in the largest possible profit to your company (given
     *         parameters cost, minShiftBreak, maxShiftLength and jobs).
     * 
     *         (See handout for details.)
     * 
     *         This method must be implemented using an efficient bottom-up
     *         dynamic programming solution to the problem (not memoised).
     */
    public static Solution optimalSolutionDynamic(int[] cost, int minShiftBreak,
            int maxShiftLength, Job[] jobs) {
        int m=jobs.length;
        int [][][] table= maximumProfit(cost,minShiftBreak,maxShiftLength,jobs);
        int i;
        int currentJobProfit=0;
        int finalResult=table[0][m][m];
        int firstJobInShift=-1;//job index
        int lastJobInShift=-1;
        List<Job> jobSelection=new ArrayList<>();
        List<Shift> shiftSelection=new ArrayList<>();
        //table has three dimensions(m*m*m), it records all the computing processes of this algorithm,
        //in order to find the chosen jobs, we need to do the following for loop.
        for(i=0;i<m;i++){
            if(firstJobInShift==-1){//means not job in the selection at the moment, need to add the first one.
                //if job i is the first job to be chosen, we compute it's profit first.
                currentJobProfit= computeProfitForCurrentJob(jobs[i].start(), jobs[i].end(), cost, i, jobs);
                //table[i+1][i][i] is the next sub problem if job i is selected.
                //e.g. job 0 is selected  as the first job, then the next sub problem is table[1][0][0],
                //1 is job 1, means we need to consider if job 1 will be selected.
                //0 and 0 means current shift is job 0.start_day to job 0.end_day.
                //since we already generated the table, if job 0 is the first chosen job, then
                //currentJobProfit+table[1][0][0] should equal to finalResult, if it doesn't equal to
                //finalResult, it means job 0 is not selected.
                if(currentJobProfit+table[i+1][i][i]==finalResult){
                    firstJobInShift=i;//update shift
                    lastJobInShift=i;//update shift
                    finalResult=finalResult-currentJobProfit;//need to minus currentJobProfit each time.
                    jobSelection.add(jobs[i]);
                }
            }else{//the following conditions are exactly same with these in maximumProfit.
                if(jobs[lastJobInShift].end() <jobs[i].start()){//skip incompatible jobs.
                    //start new shift
                    if(jobs[i].end()-jobs[firstJobInShift].start()+1<=maxShiftLength&&jobs[lastJobInShift].end()+minShiftBreak+1<=jobs[i].start()){
                        currentJobProfit=computeProfitForCurrentJob(jobs[i].start(), jobs[i].end(), cost, i, jobs);
                        //if job 2 is selected, then start a new shift, the next sub problem is table[3][2][2]
                        //if the equation is true, then job 2 is the chosen job
                        if(currentJobProfit+table[i+1][i][i]==finalResult){
                            //every time when a new shift is about to create, then the old shift should be added into shiftSelection
                            shiftSelection.add(new Shift(jobs[firstJobInShift].start(),jobs[lastJobInShift].end()));
                            jobSelection.add(jobs[i]);
                            firstJobInShift=i;//update shift
                            lastJobInShift=i;
                            finalResult=finalResult-currentJobProfit;
                        }
                        //expand the shift
                    }else if(jobs[i].end()-jobs[firstJobInShift].start()+1<=maxShiftLength){
                        currentJobProfit= computeProfitForCurrentJob(jobs[lastJobInShift].end()+1, jobs[i].end(), cost, i, jobs);
                        //if job 2 is selected, then the shift will be expanded, if the current shift is job 1's start day to
                        //job 1's end day, the expanded shift will be job 1's start day to job 2's end day. So the next sub
                        //problem is table[3][1][2]
                        if(currentJobProfit+table[i+1][firstJobInShift][i]==finalResult){
                            jobSelection.add(jobs[i]);
                            lastJobInShift=i;//only need to expand the shift
                            finalResult=finalResult-currentJobProfit;
                        }
                        //start new shift
                    }else if(jobs[lastJobInShift].end()+minShiftBreak+1<=jobs[i].start()){
                        currentJobProfit=computeProfitForCurrentJob(jobs[i].start(), jobs[i].end(), cost, i, jobs);
                        if(currentJobProfit+table[i+1][i][i]==finalResult){
                            shiftSelection.add(new Shift(jobs[firstJobInShift].start(),jobs[lastJobInShift].end()));
                            jobSelection.add(jobs[i]);
                            firstJobInShift=i;
                            lastJobInShift=i;
                            finalResult=finalResult-currentJobProfit;
                        }
                    }
                }
            }
        }
        //don't forget to add the last shift
        //when firstJobInShift=-1 here means no job is selected
        if(firstJobInShift!=-1) {
            shiftSelection.add(new Shift(jobs[firstJobInShift].start(), jobs[lastJobInShift].end()));
        }
//        for(i=0;i<shiftSelection.size();i++)
//            System.out.println(shiftSelection.get(i));
//        for(i=0;i<jobSelection.size();i++)
//            System.out.println(jobSelection.get(i));

        Solution solution=new Solution(shiftSelection,jobSelection);
        return solution;
    }

    /***
     * This is a bottom-up dynamic programming solution
     * @param cost
     * @param minShiftBreak
     * @param maxShiftLength
     * @param jobs
     * @return
     */
    public static int[][][] maximumProfit(int[] cost, int minShiftBreak, int maxShiftLength, Job[] jobs){
        int m=jobs.length;
        int [][][] table=new int [m+1][m+1][m+1];
        int i,j,k;
        int currentJobProfit=0;
        //table[m][][] is the base case, initialize it with 0
        //according to recursive way, the program terminates when i reaches m
        for(j=0;j<=m;j++){
            for(k=0;k<=m;k++){
                table[m][j][k]=0;
            }
        }
        //bottom to up, start from job m-1
        for(i=m-1;i>=0;i--){
            for(j=0;j<=m;j++){
                if(j!=m) {//when j!=m, it should satisfy 0<=j<=k<i
                    for (k = j; k < i; k++) {
                        loop://following algorithm is exactly same with recursive way
                        for (int index = i; index < m; index++) {
                            if ((jobs[k].end() < jobs[index].start()) &&
                                    (jobs[index].end() - jobs[index].start() + 1 <= maxShiftLength)) {
                                if ((jobs[index].end() - jobs[j].start() + 1 <= maxShiftLength) &&
                                        (jobs[k].end() + minShiftBreak + 1 <= jobs[index].start())) {
                                    currentJobProfit = computeProfitForCurrentJob(jobs[index].start(), jobs[index].end(), cost, index, jobs);
                                    if (currentJobProfit + table[index + 1][index][index] > table[index + 1][j][k]) {
                                        table[i][j][k] = currentJobProfit + table[index + 1][index][index];
                                    } else {
                                        table[i][j][k] = table[index + 1][j][k];
                                    }
                                    break loop;
                                } else if (jobs[index].end() - jobs[j].start() + 1 <= maxShiftLength) {
                                    currentJobProfit = computeProfitForCurrentJob(jobs[k].end() + 1, jobs[index].end(), cost, index, jobs);
                                    if (currentJobProfit + table[index + 1][j][index] > table[index + 1][j][k]) {
                                        table[i][j][k] = currentJobProfit + table[index + 1][j][index];
                                    } else {
                                        table[i][j][k] = table[index + 1][j][k];
                                    }
                                    break loop;
                                } else if (jobs[k].end() + minShiftBreak + 1 <= jobs[index].start()) {
                                    currentJobProfit = computeProfitForCurrentJob(jobs[index].start(), jobs[index].end(), cost, index, jobs);
                                    if (currentJobProfit + table[index + 1][index][index] > table[index + 1][j][k]) {
                                        table[i][j][k] = currentJobProfit + table[index + 1][index][index];
                                    } else {
                                        table[i][j][k] = table[index + 1][j][k];
                                    }
                                    break loop;
                                }
                            }
                        }
                    }
                }else{//when j=m, in this case, no job has been selected
                    if (jobs[i].end() - jobs[i].start() + 1 <= maxShiftLength) {
                        currentJobProfit = computeProfitForCurrentJob(jobs[i].start(), jobs[i].end(), cost, i, jobs);
                        if(currentJobProfit+table[i+1][i][i]>table[i+1][j][j]){
                            table[i][j][j]=currentJobProfit+table[i+1][i][i];
                        }else{
                            table[i][j][j]=table[i+1][j][j];
                        }
                    } else {
                        table[i][j][j]=table[i+1][j][j];
                    }

                }
            }
        }

        return table;
    }

    public static int computeProfitForCurrentJob(int j, int k, int[] cost, int jobIndex, Job[] jobs) {
        int sum = 0;
        for (int i = j; i <= k; i++) {
            sum += cost[i];
        }
        return jobs[jobIndex].payment() - sum;
    }
}
