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

                            List<Shoe> shoes = repository.getShoes();
                            printShoes(shoes);

                            System.out.println("Selection: ");
                            int selection = Integer.parseInt(scanner.nextLine());

                            int shoeId = shoes.get(selection).getId();
                            repository.addToCart(customer.getId(), shoeId);
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
}
