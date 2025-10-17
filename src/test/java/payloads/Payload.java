package payloads;


import net.datafaker.*;
import java.text.*;
import java.util.*;
import pojo.*;

public class Payload {

    private static final Faker faker=new Faker();
    private static final String categories[]= {"electronics", "furniture", "clothing", "books", "beauty"};
    private static final Random random=new Random();

    //product
    public static Product productPayload(){
        String name=faker.commerce().productName();
        double price=Double.parseDouble(faker.commerce().price());
        String description=faker.lorem().sentence();
        String imageUrl="https://i.pravatar.cc/100";
        String category=categories[random.nextInt(categories.length)];

        new Product(name, price, description, imageUrl, category);
        return new Product(name, price, description, imageUrl, category);
    }

    //user

    public static User userPayload()
    {
        //name
        String firstname=faker.name().firstName();
        String lastname=faker.name().lastName();

        Name name=new Name(firstname,lastname);

        //location
        String lat=faker.address().latitude();
        String lng=faker.address().longitude();

        Geolocation location=new Geolocation(lat,lng);

        //Address

        String city=faker.address().city();
        String street=faker.address().streetName();
        int number=random.nextInt(100);
        String zipcode=faker.address().zipCode();
        Address address=new Address(city,street,number,zipcode,location);

        //User
        String email=faker.internet().emailAddress();
        String username = faker.internet().domainWord() + faker.number().digits(3);
        String password = faker.bothify("??##??##");
        String phonenumber=faker.phoneNumber().cellPhone();

        User user=new User(email,username,password,name,address,phonenumber);

        return user;
    }

    //cart
    public static Cart cartPayload(int userId) {
        List<CartProduct> products = new ArrayList<>();

        // Adding one random product to the cart
        int productId = random.nextInt(100);
        int quantity = random.nextInt(10) + 1;
        CartProduct cartProduct= new CartProduct(productId, quantity);
        products.add(cartProduct);

        //new Date Returns date in format: Wed Feb 19 13:17:45 IST 202
        // We need to convert this to "yyyy-MM-dd" format in String

        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);// Define output date format
        String date = outputFormat.format(new Date());//Converting to String

        return new Cart(userId, date, products);
    }

    //login
    public static Login loginPayload()
    {
        String username = faker.internet().domainWord() + faker.number().digits(3);
        String password = faker.bothify("??##??##");

        Login login=new Login(username,password);
        return login;
    }
}