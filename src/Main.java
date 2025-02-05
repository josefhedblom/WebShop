import java.util.List;

public class Main {
    public static void main(String[] args) {
        Repository repository = new Repository();
        List<Shoe> shoes = repository.getShoes();
        printShoes(shoes);
//        shoes.forEach(System.out::println);
    }

    public static void printShoes(List<Shoe> shoes) {
        for (int i = 0; i < shoes.size(); i++) { //1,...,n
            Shoe shoe = shoes.get(i);
            System.out.printf("Id:%d,Brand:%s, Model:%s, Color:%s, Category:%s, Gender:%s, Size:%d, Price:%.2f%n", i, shoe.getBrand(), shoe.getModel(), shoe.getColor(), shoe.getCategory(), shoe.getGender(), shoe.getSize(), shoe.getPrice());
        }
    }
}
