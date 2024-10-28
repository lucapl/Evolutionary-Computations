package put.ec.solution;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import put.ec.problem.City;
import put.ec.problem.TravellingSalesmanProblem;
import put.ec.solution.solvers.Solver;

import java.io.FileWriter;
import java.io.IOException;

public class SolutionWriter {

    public void writeSolution(Solution solution, Solver solver, TravellingSalesmanProblem problemInstance, String outFolder){
        String solverName = solver.getName();
        int startingCity = solution.getStartingCityIndex();
        String instanceName = problemInstance.getName();
        String fileName = problemInstance.getName()+"_"+startingCity+"_"+solverName;
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("solverType",solverName);
        jsonObject.put("instance",instanceName);
        jsonObject.put("cost",solution.getSolutionCost());
        jsonObject.put("edge length",solution.getEdgeLength());
        jsonObject.put("objective function",solution.getObjectiveFunctionValue());
        jsonObject.put("starting city", solution.getStartingCityIndex());
        if(solver.iterations >= 0){
            jsonObject.put("iterations", solver.iterations);
        }

        JSONArray jsonArray = new JSONArray();
        for(City city: solution.getCityOrder()){
            jsonArray.add(city.getIndex());
        }
        jsonObject.put("cityOrder",jsonArray);

        try {
            FileWriter file = new FileWriter(outFolder+fileName+".json");
            file.write(jsonObject.toJSONString());
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("JSON file created: "+jsonObject);
    }
}
