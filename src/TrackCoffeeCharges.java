
import java.util.*;

public class TrackCoffeeCharges {
    private static Map<String, Double> dailyOrders = new LinkedHashMap<>();
    private static String[] members = {"Sachin", "Virat", "Smith", "Kane", "Root"};
    private static int payerIndex = 0;
    private static Random random = new Random();
    private static Map<String, Map<String, Double>> amountsowed = new HashMap<>();
    private static boolean useRoundRobin = true;

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
            if (diffAmount != 0) {
                amountsowed.computeIfAbsent(member, k -> new HashMap<>());
                amountsowed.computeIfAbsent(payingMember, k -> new HashMap<>());

                if (diffAmount < 0) {
                    //Here paying member owes the other person as the diff is negative
                    amountsowed.get(member).merge(payingMember, Math.abs(diffAmount), Double::sum);
                    amountsowed.get(payingMember).merge(member, -Math.abs(diffAmount), Double::sum);
                    System.out.printf("%s owes %s $%.2f\n", payingMember, member, Math.abs(diffAmount));
                } else if (diffAmount > 0) {
                    //Here member needs to owe the payer
                    amountsowed.get(payingMember).merge(member, diffAmount, Double::sum);
                    amountsowed.get(member).merge(payingMember, -diffAmount, Double::sum);
                    System.out.printf("%s owes %s $%.2f\n", member, payingMember, diffAmount);
                }
            }
        }
        dailyOrders.clear();
        //function finishes with this
    }

    private static void DisplayTotalOwedAmounts() {
        System.out.println("\nTotal owed amounts :");
        amountsowed.forEach((person, owes) -> {
            owes.forEach((oweTo, amount) -> {
                if (amount > 0) {
                    System.out.printf("%s owes %s $%.2f\n", person, oweTo, amount);
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
            return members[random.nextInt(members.length)];
        }
    }

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