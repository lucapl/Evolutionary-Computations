package put.ec.solution;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import put.ec.problem.City;

import java.io.FileWriter;
import java.io.IOException;

public class SolutionWriter {

    public void writeSolution(Solution solution,String outFolder, String fileName, String instanceName,String solverName){
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("solverType",solverName);
        jsonObject.put("instance",instanceName);
        jsonObject.put("cost",solution.getSolutionCost());
        jsonObject.put("edge length",solution.getEdgeLength());
        jsonObject.put("objective function",solution.getObjectiveFunctionValue());

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
