public class Main {
    public static void main(String[] args) {
        Repository repository = new Repository();
        System.out.println(repository.isRegistered("anna.johansson@example.com", "password2"));

        //repository.getCustomer();
        //repository.getShoes("Adidas",null,null,null);
//        repository.getCustomerOrderHistory(1);
    }
}
