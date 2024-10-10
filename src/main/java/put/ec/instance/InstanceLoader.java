package put.ec.instance;

import put.ec.problem.City;
import put.ec.problem.TravellingSalesmanProblem;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class InstanceLoader {

    public TravellingSalesmanProblem load(String fileName){
        Scanner sc = null;

        sc = new Scanner(getFileAsIOStream(fileName));

        sc.useDelimiter(";");
        TravellingSalesmanProblem tsp = new TravellingSalesmanProblem();
        while (sc.hasNextLine()) {
            String[] cityString = sc.nextLine().split(";");
            City city = new City(cityString);
            tsp.addCity(city);
        }
        sc.close();

        return tsp;
    }

    private InputStream getFileAsIOStream(final String fileName)
    {
        InputStream ioStream = this.getClass()
                .getClassLoader()
                .getResourceAsStream(fileName);

        if (ioStream == null) {
            throw new IllegalArgumentException(fileName + " is not found");
        }
        return ioStream;
    }
}
