package vn.edu.hust.simulation;

import org.apache.commons.collections4.map.HashedMap;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Test {

    public static void main(String[] args) {

        Map<String, Integer> citiesWithCode = Test.getCitiesWithCodes();
        Map<Integer, List<String>> result =  citiesWithCode.entrySet().stream().collect(Collectors.groupingBy(
            Map.Entry::getValue, Collectors.mapping(Map.Entry::getKey, Collectors.toList())));
        Map<String, Integer> result2 = citiesWithCode.entrySet().stream()
            .filter(city -> city.getKey().length() < 10)
            .collect(Collectors.toMap(city -> city.getKey(), city -> city.getValue()));
        System.out.println(result2);

    }

    static Map<String, Integer> getCitiesWithCodes() {
        Map<String, Integer> citiesWithCodes = new HashedMap<>();
        citiesWithCodes.put("Berlin", 49);
        citiesWithCodes.put("Frankfurt", 49);
        citiesWithCodes.put("Hamburg", 49);
        citiesWithCodes.put("Cologne", 49);
        citiesWithCodes.put("Salzburg", 43);
        citiesWithCodes.put("Vienna", 43);
        citiesWithCodes.put("Zurich", 41);
        citiesWithCodes.put("Bern", 41);
        citiesWithCodes.put("Interlaken", 41);

        return citiesWithCodes;
    }
}
