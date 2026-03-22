package Streams.PracticeStreams.Solutions;

import java.util.List;
import java.util.stream.Collectors;

public class Solution_04 {
    public static void main(String[] args) {
        List<Double> prices = List.of(3040.53, 1023.52, 4024.43, 5062.62, 2343.56);

        List<Double> discountedPrices = prices.stream().map(price -> {
            double discPrice = price - price*0.1;
            price = Math.round(discPrice * 1000) / 1000.0;
            return price;
        }).collect(Collectors.toList());

        discountedPrices.forEach(System.out::println);
    }
}
