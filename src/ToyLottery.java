import java.io.FileWriter;
import java.io.IOException;
import java.util.PriorityQueue;
import java.util.Random;

class Toy {
    int id;
    String name;
    int weight;

    public Toy(int id, String name, int weight) {
        this.id = id;
        this.name = name;
        this.weight = weight;
    }
}

public class ToyLottery {
    private PriorityQueue<Toy> toyQueue;
    private Random random;

    public ToyLottery(String[] toyData) {
        toyQueue = new PriorityQueue<>((t1, t2) -> Integer.compare(t2.weight, t1.weight));
        random = new Random();

        for (String data : toyData) {
            String[] parts = data.split(" ");
            if (parts.length >= 3) {
                int id = Integer.parseInt(parts[0]);
                String name = parts[1];
                try {
                    int weight = Integer.parseInt(parts[2]);
                    toyQueue.add(new Toy(id, name, weight));
                } catch (NumberFormatException e) {
                    System.err.println("Error parsing toy data: " + e.getMessage());
                }
            }
        }
    }

    public void playAndWriteResultsToFile(String filePath) {
        try (FileWriter writer = new FileWriter(filePath)) {
            for (int i = 0; i < 10; i++) {
                int result = getToy();
                writer.write(result + System.lineSeparator());
            }
            System.out.println("Results written to file: " + filePath);
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }

    private int getToy() {
        int randomNumber = random.nextInt(100);
        int threshold = 0;
        int totalWeight = toyQueue.stream().mapToInt(t -> t.weight).sum();

        for (Toy toy : toyQueue) {
            threshold += (toy.weight * 100 / totalWeight);
            if (randomNumber < threshold) {
                return toy.id;
            }
        }

        // This should not happen under normal circumstances
        return -1;
    }

    public static void main(String[] args) {
        // Example toy data
        String[] toyData = {
                "1 2 2",
                "2 2 2",
                "3 6 2"
        };

        // Create ToyLottery instance
        ToyLottery toyLottery = new ToyLottery(toyData);

        // Play the lottery and write results to file
        toyLottery.playAndWriteResultsToFile("toy_lottery_results.txt");
    }
}
