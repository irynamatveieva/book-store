# **Online Book Store📚**

# 🪄Introduction:
This project came about due to the need to create a reliable and efficient solution for managing bookstore operations. our API gives you the tools you need to simplify book accounting, customer data management, and seamless literary sales operations.

# 🛠️Technologies and Tools:
This API has been developed using advanced technologies and tools to ensure reliability, security and efficiency:

- **Spring Boot**:

We used Spring Boot to create an application that is easily scalable and easy to manage.
- **Hibernate**: 

Hibernate in our project was used to optimize interaction with the database by displaying Java objects in database tables.
- **Spring Security**: 

We have implemented Spring Security to ensure that access is limited to authorized users and that your data is protected.
- **Spring Data JPA**: 

Spring Data JPA made managing book data in a database easy.
- **JWT (JSON Web Tokens)**: 

With JWT, we strengthened the application with token-based authentication and authorization.
- **MapStruct**: 

MapStruct helped with the conversion between models.
- **Liquibase**: 

We used Liquibase to comfortably manage tables in the database.
- **Swagger**:

Added Swagger documentation for easy API exploration.

# Functionalities:
### Our API consists of several controllers:

- **🛂Authentication Controller:** Used for user registration and authorization.
- **📖Book Controller:** Provides an opportunity to operate books by user role. For an ordinary user, it is possible to find out the availability of books, to find out the details of each book separately, and even to search by specific parameters, and a user with the role of administrator, in addition to the previous actions, can add books, update data about them, and delete them.
- **📚Category Controller:** Similar to Book controller, provides the ability to operate with categories. Has methods to create, get, update, and delete categories.
- **🧾Order Controller:** Using the methods of this controller we have the ability to create an order, view its contents, update the order, view the order history and the specific product.
- **🛒Shopping Cart Controller:** Allows you to view the contents of the shopping cart, add a product to the cart, change the quantity of the product and remove the product from the shopping cart as needed.

# 💻Installation and Usage:

### You can test our app in several ways:

1. **Using Swagger (No Installation Required)**
   
   So that you don't need to install anything on your computer to see how the app works, I've deployed it on AWS.To test the API, you can use Swagger, which provides a user-friendly interface for interacting with the API.

   1. Open your web browser and navigate to [Swagger UI](http://ec2-52-90-57-125.compute-1.amazonaws.com/api/swagger-ui/index.html#/).
   2. Explore and test the API endpoints directly through the Swagger interface.


   **Authentication credentials for role ADMIN:**

    "username": "admin@example.com"
    "password": "adminPassword"

2. **Local Installation**

   Ensure you have the following prerequisites installed on your machine:

    - [Java 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
    - [Maven](https://maven.apache.org/download.cgi)
    - [Docker](https://docs.docker.com/get-docker/)
    - [Postman](https://www.postman.com/downloads/)
   
   Now you can run the API locally on your machine by performing the following steps:
   1. Clone the [repository](https://github.com/irynamatveieva/book-store) from GitHub. 
   2. Build project with Maven(run this command in terminal 'mvn clean package').
   3. Run the application using Docker Compose(run command 'docker-compose up').
   4. Explore the endpoints using Postman([file with scripts for Postman](postman_requests)).
   5. To stop container run this command 'docker-compose down'.

