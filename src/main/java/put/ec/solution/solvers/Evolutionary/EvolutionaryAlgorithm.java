package put.ec.solution.solvers.Evolutionary;

import put.ec.problem.TravellingSalesmanProblem;
import put.ec.solution.Solution;
import put.ec.solution.solvers.Solver;

import java.util.*;

public abstract class EvolutionaryAlgorithm extends Solver {
    private List<Solution> population;
    private HashSet<Double> isInPopulation;
    private int popSize;
    private Solution worstSolutionInPop;
    private List<Solution> elitePopulation;
    private int elitePopSize;
    private double xoverProb;
    private double mutationProb;
    private boolean steadyState;

    public EvolutionaryAlgorithm(TravellingSalesmanProblem problem, int popSize, int elitePopSize, double mutationProb, double xoverProb, boolean steadyState){
        super(problem);
        this.population = new LinkedList<>();
        this.isInPopulation = new HashSet<>();
        this.setPopSize(popSize);
        this.elitePopSize = elitePopSize;
        this.elitePopulation = new LinkedList<>();

        this.mutationProb = mutationProb;
        this.xoverProb = xoverProb;
        this.steadyState = steadyState;

        setName("EA");
    }

    abstract public Solution mutation(Solution solution);

    abstract public Solution crossover(Solution parent1, Solution parent2);

    public void addToPopulation(Solution newSolution){
        if(steadyState){
            steadyStateReplace(newSolution);
            return;
        }
        throw new RuntimeException("Not implemented yet");
    }

    private void steadyStateReplace(Solution newSolution){
        population.remove(getWorstSolutionInPop());
        setWorstSolutionInPop(findWorst());
        add(newSolution);
    }

    public Solution findWorst(){
        return population.stream()
                .max(Comparator.comparing(Solution::getObjectiveFunctionValue))
                .orElseThrow(NoSuchElementException::new);
    }

    public Solution findBest(){
        return population.stream()
                .min(Comparator.comparing(Solution::getObjectiveFunctionValue))
                .orElseThrow(NoSuchElementException::new);
    }

    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    public int getRandomUniqueFrom(int other, int min, int max){
        return (getRandomNumber(min,max-1) + 1 + other)%max;
    }

    public Solution[] getTwoRandomSolutions(){
        int n = population.size();
        int x = getRandomNumber(0,n);
        int y = getRandomUniqueFrom(x,0,n);

        return new Solution[]{population.get(x), population.get(y)};
    }

    abstract public void initializePopulation();

    public boolean isInPop(Solution solution){
        return isInPopulation.contains(solution.getObjectiveFunctionValue());
    }

    public Solution getWorstSolutionInPop() {
        return worstSolutionInPop;
    }

    public void setWorstSolutionInPop(Solution worstSolutionInPop) {
        this.worstSolutionInPop = worstSolutionInPop;
    }

    public void add(Solution solution){
        population.add(solution);
        isInPopulation.add(solution.getObjectiveFunctionValue());
    }

    public void remove(Solution solution){
        population.remove(solution);
        isInPopulation.remove(solution.getObjectiveFunctionValue());
    }

    public int getPopSize() {
        return popSize;
    }

    public void setPopSize(int popSize) {
        this.popSize = popSize;
    }

    public void clear(){
        population.clear();
        isInPopulation.clear();
    }
}
