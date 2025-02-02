public class Customer {
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String personnummer;
    private String city;
    public Customer(int id, String firstName, String lastName, String email, String city, String personnummer) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.city = city;
        this.personnummer = personnummer;
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPersonnummer() {
        return personnummer;
    }

    public String getCity() {
        return city;
    }
}
