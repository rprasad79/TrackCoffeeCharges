
import java.util.*;

public class TrackCoffeeCharges {
    private static Map<String, Double> dailyOrders = new LinkedHashMap<>();
    private static String[] members = {"Sachin", "Virat", "Smith", "Kane", "Root"};
    private static int payerIndex = 0;
    private static Random random = new Random();
    private static Map<String, Map<String, Double>> amountsowed = new HashMap<>();
    private static boolean useRoundRobin = false;
    private static double owedAmount = 0.0;
    private static Map<String, Integer> paymentCounts = new HashMap<>();


    public static void main(String[] args) {
        //System.out.println("Hello, World!");
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\nChoose your choice:");
            System.out.println("1. Enter Order of the Day");
            System.out.println("2. Calculate Today Expense");
            System.out.println("3. Total Owed Amounts");
            System.out.println("4. Selection Mode");
            System.out.println("5. Exit");

            System.out.print("Enter your choice: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    addOrderDetails(scanner);
                    break;
                case "2":
                    CalculateTotalExpenses();
                    break;
                case "3":
                    DisplayTotalOwedAmounts();
                    break;
                case "4":
                    ChangeSelectionMode(scanner);
                    break;
                case "5":
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice.");
                    break;
            }
        }
    }

    private static void addOrderDetails(Scanner scanner) {
        dailyOrders.clear();
        for (String member : members) {
            System.out.print("Enter " + member + "'s order amount: ");
            try {
                double amount = Double.parseDouble(scanner.nextLine());
                dailyOrders.put(member, amount);
            } catch (NumberFormatException e) {
                System.out.println("Invalid order amount. Order not added for " + member);
            }
        }
    }

    private static void CalculateTotalExpenses() {
        if (dailyOrders.isEmpty()) {
            System.out.println("Nothing to do.");
            return;
        }

        String payingMember = getPayingMember();
        //Get the values from the hashmap for all the users and add it up
        double totalExp = dailyOrders.values().stream().mapToDouble(Double::doubleValue).sum();
        double averageShare = totalExp / members.length;

        System.out.println("\n" + payingMember + " paid for today's coffee.");
        System.out.printf("Total bill: $%.2f\n", totalExp);
        System.out.printf("Average share: $%.2f\n", averageShare);
        for (String member : members) {
            //double diffAmount = dailyOrders.get(member) - averageShare;
            //Finding the difference of amount between the payer and the member.
            double diffAmount = dailyOrders.get(member) - dailyOrders.get(payingMember);
            System.out.printf("Diff Amount of " + member + " is %.2f\n", diffAmount);
            if (!member.equals(payingMember)) {
                owedAmount = dailyOrders.get(member); // Full amount

                amountsowed.computeIfAbsent(member, k -> new HashMap<>());
                amountsowed.computeIfAbsent(payingMember, k -> new HashMap<>());

                amountsowed.get(payingMember).merge(member, owedAmount, Double::sum);
                amountsowed.get(member).merge(payingMember, -owedAmount, Double::sum);

                System.out.printf("%s owes %s $%.2f\n", member, payingMember, owedAmount);
            }

/* This is a Different Logic
//            if (diffAmount != 0) {
//                amountsowed.computeIfAbsent(member, k -> new HashMap<>());
//                amountsowed.computeIfAbsent(payingMember, k -> new HashMap<>());
//
//                if (diffAmount < 0) {
//                    //Here paying member owes the other person as the diff is negative
//                    amountsowed.get(member).merge(payingMember, Math.abs(diffAmount), Double::sum);
//                    amountsowed.get(payingMember).merge(member, -Math.abs(diffAmount), Double::sum);
//                    System.out.printf("%s owes %s $%.2f\n", payingMember, member, Math.abs(diffAmount));
//                } else if (diffAmount > 0) {
//                    //Here member needs to owe the payer
//                    amountsowed.get(payingMember).merge(member, diffAmount, Double::sum);
//                    amountsowed.get(member).merge(payingMember, -diffAmount, Double::sum);
//                    System.out.printf("%s owes %s $%.2f\n", member, payingMember, diffAmount);
//                }
//            } */
        }
        dailyOrders.clear();
        //function finishes with this
    }

    private static void DisplayTotalOwedAmounts() {
        System.out.println("\nTotal owed amounts :");
        amountsowed.forEach((person, owes) -> {
            owes.forEach((oweTo, amount) -> {
                if (amount > 0) {
                    System.out.printf("%s owes %s $%.2f\n", oweTo, person, amount);
                }
            });
        });
    }

    private static String getPayingMember() {
        if (useRoundRobin) {
            String payingMember = members[payerIndex];
            payerIndex = (payerIndex + 1) % members.length;
            return payingMember;
        } else {
            //return members[random.nextInt(members.length)];
            return executePayerSelection();
        }
    }

    private static String executePayerSelection() {
        // Calculate mean and mode of order amounts
        double mean = calculateMean();
        String mode = calculateMode();

        //Logic involves the mode and mean to identify who can pay AI-inspired logic:
        // If mode exists, the person with the mode order pays.
        // If no mode, the person whose order is closest to the mean pays.
        if (mode != null) {
            return mode;
        } else {
            return getClosestToMean();
        }
    }

    private static double calculateMean() {
        return dailyOrders.values().stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
    }

    private static String calculateMode() {
        Map<Double, Integer> frequencyMap = new HashMap<>();
        for (double amount : dailyOrders.values()) {
            frequencyMap.put(amount, frequencyMap.getOrDefault(amount, 0) + 1);
        }

        int maxFrequency = 0;
        double modeAmount = 0.0;
        boolean modeExists = false;

        for (Map.Entry<Double, Integer> entry : frequencyMap.entrySet()) {
            if (entry.getValue() > maxFrequency) {
                maxFrequency = entry.getValue();
                modeAmount = entry.getKey();

                modeExists = true;
            }
        }

        if (modeExists) {
            System.out.println("Mode Exists and the value: " + modeAmount);
            for (Map.Entry<String, Double> entry : dailyOrders.entrySet()) {
                if (entry.getValue() == modeAmount) {
                    System.out.printf("%s is the same as mode.\n", entry.getKey());
                    return entry.getKey();
                }
            }
        }
        return null;
    }

    private static String getClosestToMean() {
        double mean = calculateMean();
        System.out.println("Calculated Mean: " + mean);
        String closestMember = null;
        double minDifference = Double.MAX_VALUE;

        for (Map.Entry<String, Double> entry : dailyOrders.entrySet()) {
            double difference = Math.abs(entry.getValue() - mean);
            if (difference < minDifference) {
                minDifference = difference;
                closestMember = entry.getKey();
            }
        }
        System.out.println("Closest Member: " + closestMember);
        return closestMember;
    }

    /*
     private static String combinedPayerSelection() {
        if (paymentCounts.isEmpty()) {
            for (String member : members) {
                paymentCounts.put(member, 0);
            }
        }

        Map<String, Double> combinedScores = new HashMap<>();
        double mean = calculateMean();
        String mode = calculateMode();

        for (String member : members) {
            double frequencyWeight = 1.0 / (paymentCounts.get(member) + 1.0); // Inverse frequency
            double meanModeScore = 0.0;

            if (mode != null && dailyOrders.get(member).equals(dailyOrders.get(mode))) {
                meanModeScore = 1.0; // High score for mode
            } else {
                meanModeScore = 1.0 - (Math.abs(dailyOrders.get(member) - mean) / mean); // Score based on proximity to mean
            }

            combinedScores.put(member, frequencyWeight + meanModeScore);
        }

        String bestPayer = null;
        double maxScore = -1.0;

        for (Map.Entry<String, Double> entry : combinedScores.entrySet()) {
            if (entry.getValue() > maxScore) {
                maxScore = entry.getValue();
                bestPayer = entry.getKey();
            }
        }
        updatePaymentCounts(bestPayer);
        return bestPayer;
    }*/



    private static void ChangeSelectionMode(Scanner scanner) {
        System.out.println("\nSelect Payer Selection Method:");
        System.out.println("1. Round Robin");
        System.out.println("2. Random");
        System.out.print("Enter your choice: ");
        String choice = scanner.nextLine();

        if (choice.equals("1")) {
            useRoundRobin = true;
            System.out.println("Payer selection method changed to Round Robin.");
        }
    }
}

