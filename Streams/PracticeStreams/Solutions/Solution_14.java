package Streams.PracticeStreams.Solutions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Solution_14 {
    public static void main(String[] args) {

        // Map<String, String> → key = employee name, value = department
        Map<String, String> empDeptMap = new HashMap<>();
        empDeptMap.put("Aryan", "Backend Developer");
        empDeptMap.put("Aakashvani", "Backend Developer");
        empDeptMap.put("Ramandeep", "Cricketer");
        empDeptMap.put("Shubman", "Cricketer");
        empDeptMap.put("Yashashvi", "Cricketer");
        empDeptMap.put("Paramveer", "Travell Vlogger");
        empDeptMap.put("Nomad Shubham", "Travell Vlogger");

        // empDeptMap.entrySet() returns → Set<Map.Entry<String, String>>
        // Each element in the set is a Map.Entry object holding one key-value pair.
        //
        // Stream pipeline steps:
        // 1. entrySet().stream()   → Stream<Map.Entry<String, String>>
        // 2. groupingBy(entry -> entry.getValue())
        //       → groups entries by their VALUE (department name) as the key
        // 3. Collectors.mapping(entry -> entry.getKey(), toList())
        //       → for each group, extracts only the KEY (employee name) into a List<String>
        //          (without this, the list would contain Map.Entry objects, not Strings!)
        //
        // Result: Map<String, List<String>>
        // {
        //   "Backend Developer" → ["Aryan", "Aakashvani"]
        //   "Cricketer"         → ["Ramandeep", "Shubman", "Yashashvi"]
        //   "Travell Vlogger"   → ["Paramveer", "Nomad Shubham"]
        // }
        Map<String, List<String>> map = empDeptMap.entrySet().stream()
                .collect(Collectors.groupingBy(
                        entry -> entry.getValue(),                                   // classifier: group by department
                        Collectors.mapping(entry -> entry.getKey(), Collectors.toList()) // downstream: extract employee names
                ));

        System.out.println(map);
    }
}
