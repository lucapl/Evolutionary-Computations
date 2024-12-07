package put.ec.solution;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import put.ec.problem.City;
import put.ec.problem.TravellingSalesmanProblem;
import put.ec.solution.solvers.Solver;
import put.ec.utils.StatKeeper;
import put.ec.utils.TimeMeasure;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class SolutionWriter {
    private JSONObject solutionsInfo;
    private String instanceName;
    private String solverName;
    private List<JSONObject> instanceSolutions; // Store solutions for a specific instance
    private StatKeeper timeStat;
    private StatKeeper objectiveFunctionStat;

    public SolutionWriter() {
        this.solutionsInfo = new JSONObject();
        this.solutionsInfo.put("solutions", new JSONObject());
        this.instanceSolutions = new ArrayList<>();
        setTimeStat(new StatKeeper("time"));
        objectiveFunctionStat = new StatKeeper("objective function");
    }

    public void newInstance(String instanceName) {
        this.instanceName = instanceName;
        this.instanceSolutions.clear();
        getTimeStat().clear();
        objectiveFunctionStat.clear();
    }

    public void writeSolution(Solution solution, Solver solver, TimeMeasure timeMeasure, TravellingSalesmanProblem problemInstance) {
        solverName = solver.getName();
        int startingCity = solution.getStartingCityIndex();
        String instanceName = problemInstance.getName();

        double elapsedTime = timeMeasure.getElapsedTime(-6);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("solverType", solverName);
        jsonObject.put("instance", instanceName);
        jsonObject.put("cost", solution.getSolutionCost());
        jsonObject.put("edge length", solution.getEdgeLength());
        jsonObject.put("objective function", solution.getObjectiveFunctionValue());
        jsonObject.put("starting city", startingCity);
        jsonObject.put("elapsed time", elapsedTime); // Time in nanoseconds

        if (solver.iterations >= 0) {
            jsonObject.put("iterations", solver.iterations);
        }

        JSONArray cityOrder = new JSONArray();
        for (City city : solution.getCityOrder()) {
            cityOrder.add(city.getIndex());
        }
        jsonObject.put("cityOrder", cityOrder);

        // Update min and max objective function values
        double objectiveValue = solution.getObjectiveFunctionValue();
        objectiveFunctionStat.add(objectiveValue);
        getTimeStat().add(elapsedTime);

        // Add solution to the list for this instance
        instanceSolutions.add(jsonObject);
    }

    public void saveInstanceSolutions() {
        if (instanceName == null || instanceName.isEmpty()) {
            System.err.println("SolutionWriter: Instance name is not set.");
            return;
        }

        JSONObject instanceInfo = new JSONObject();
        JSONArray solutionsArray = new JSONArray();
        solutionsArray.addAll(instanceSolutions);

        instanceInfo.put("solutions", solutionsArray);
        instanceInfo.put(getTimeStat().getName(), getTimeStat().toJSONObject());
        instanceInfo.put(objectiveFunctionStat.getName(),objectiveFunctionStat.toJSONObject());

        // Add to the main solutions info
        JSONObject mainSolutions = (JSONObject) solutionsInfo.get("solutions");
        mainSolutions.put(instanceName, instanceInfo);
        System.out.println(solverName+" for " + instanceName + " done; "+objectiveFunctionStat+" "+ getTimeStat());
    }

    public void saveSolutionInfo(String outFolder) {
        try {
            Files.createDirectories(Paths.get(outFolder));
        } catch (IOException e) {
            System.err.println("SolutionWriter: Could not create the directory:");
            e.printStackTrace();
        }

        String fileName = outFolder + solverName + ".json";

        try (FileWriter file = new FileWriter(fileName)) {
            file.write(solutionsInfo.toJSONString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("JSON file created: " + fileName);
    }

    public StatKeeper getTimeStat() {
        return timeStat;
    }

    public void setTimeStat(StatKeeper timeStat) {
        this.timeStat = timeStat;
    }
}
