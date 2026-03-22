package Streams.PracticeStreams.Solutions;

import java.util.List;
import java.util.Optional;

public class Solution_06 {
    public static void main(String[] args) {
        List<String[]> orders = List.of(new String[] {"Order1", "350"}, new String[] {"Order2", "250"}, new String[] {"Order3", "400"});

        Optional<String[]> expensiveOrder = orders.stream().max((o1, o2) -> Integer.compare(Integer.parseInt(o1[1]), Integer.parseInt(o2[1])));

        expensiveOrder.ifPresent(order -> System.out.println(order[0] + ": Rs." + order[1]));
    }
}
