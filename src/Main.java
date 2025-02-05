import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Repository repository = new Repository();

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("E: Exit, L:Login");
            String userInput = scanner.nextLine();

            switch (userInput) {
                case "E":
                    System.exit(0);
                    break;
                    case "L":
                        System.out.println("Username: ");
                        String username = scanner.nextLine();
                        System.out.println("Password: ");
                        String password = scanner.nextLine();

                        Optional<Customer> result = repository.getCustomer(username, password);

                        if (result.isPresent()) {
                            Customer customer = result.get();
                            System.out.println("User " + username + " logged in.");

                            while (true) {
                                List<OrderDetail> cart = repository.getCart(customer.getId());
                                System.out.println("Cart:");
                                printCart(cart);
                                System.out.println("------------------------------");

                                System.out.println("Available shoes: ");
                                List<Shoe> shoes = repository.getShoes();
                                printShoes(shoes);

                                System.out.println("Selection: ");
                                try {
                                    int selection = Integer.parseInt(scanner.nextLine());

                                    if (selection >= 0 && selection < shoes.size()) {
                                        int shoeId = shoes.get(selection).getId();

                                        try {
                                            repository.addToCart(customer.getId(), shoeId);
                                            System.out.println("Shoe successfully added to the cart.");
                                        } catch (SQLException e) {
                                            System.out.println(e.getMessage());
                                        }

                                    } else {
                                        System.out.println("Invalid selection.");
                                    }
                                } catch (NumberFormatException e) {
                                    System.out.println("Invalid selection. Try again.");
                                }

                                System.out.println("Continue? Y/N");
                                String input = scanner.nextLine();

                                if (input.equalsIgnoreCase("n")) {
                                    break;
                                }
                            }
                        } else {
                            System.out.println("Username AND/OR password are incorrect.");
                        }
            }
        }
    }

    public static void printShoes(List<Shoe> shoes) {
        for (int i = 0; i < shoes.size(); i++) { //1,...,n
            Shoe shoe = shoes.get(i);
            System.out.printf("Id:%d,Brand:%s, Model:%s, Color:%s, Category:%s, Gender:%s, Size:%d, Price:%.2f%n", i, shoe.getBrand(), shoe.getModel(), shoe.getColor(), shoe.getCategory(), shoe.getGender(), shoe.getSize(), shoe.getPrice());
        }
    }

    public static void printCart(List<OrderDetail> orderDetails) {
        for (OrderDetail orderDetail : orderDetails) {
            Shoe shoe = orderDetail.getShoe();
            System.out.printf("Brand:%s, Model:%s, Color:%s, Category:%s, Gender:%s, Size:%d| Quantity: %d%n", shoe.getBrand(), shoe.getModel(), shoe.getColor(), shoe.getCategory(), shoe.getGender(), shoe.getSize(), orderDetail.getQuantity());
        }
    }
}
