# Aegyo&Co 🎵

A full-stack e-commerce platform specializing in K-pop merchandise, including albums, lightsticks, and official merchandise. Built with Spring Boot and modern web technologies.

![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.4-brightgreen)
![MySQL](https://img.shields.io/badge/MySQL-Database-blue)
![Thymeleaf](https://img.shields.io/badge/Thymeleaf-Template%20Engine-green)

## 📖 Overview

Aegyo&Co is a comprehensive e-commerce solution that provides a seamless shopping experience for K-pop fans. The platform features a complete product catalog, shopping cart functionality, order management, and user authentication with both traditional form-based login and OAuth2 social login integration.

**Demo**: *[Video/GIF demo coming soon]*

## 👥 Team & Contributions

This project was developed by a team of 2 developers. My primary contributions include:

- **Authentication System**: Implemented form-based registration and login with Spring Security
- **Review & Rating System**: Built the product review functionality with rating aggregation
- **Comment System**: Developed commenting features for products
- **File Upload Management**: Implemented image upload and storage for product images
- **Admin Dashboard**: Created the admin interface for product and order management
- **User Profile Management**: Built profile update functionality including personal info, address, and password changes
- **UI/UX Design**: Designed and implemented the main user interface components

## ✨ Key Features

### Customer Features
- **Product Browsing**: Browse products by category (Albums, Lightsticks, MD), artist, or search by keyword
- **Advanced Filtering**: Sort by best-selling, newest, price, and ratings
- **Shopping Cart**: Session-based cart for guests, persistent cart for registered users
- **User Authentication**: 
  - Form-based registration and login
  - OAuth2 integration (Google, Facebook)
  - Secure password encryption with BCrypt
- **Order Management**: Complete checkout flow with order tracking and status updates
- **Review System**: Rate and review purchased products
- **Product Recommendations**: Intelligent suggestions based on artist and category

### Admin Features
- **Product Management**: Full CRUD operations with image upload
- **Order Processing**: Update order status through complete fulfillment workflow
- **User Management**: View customer information and manage purchase restrictions
- **Dashboard Analytics**: Overview of sales and user activity

## 🛠️ Technology Stack

### Backend
- **Framework**: Spring Boot 3.4.4
- **Language**: Java 17
- **Security**: Spring Security with OAuth2
- **ORM**: Spring Data JPA / Hibernate
- **Database**: MySQL
- **Build Tool**: Maven

### Frontend
- **Template Engine**: Thymeleaf
- **Styling**: CSS3, Bootstrap (implied from structure)
- **JavaScript**: Vanilla JS for interactive features

### Key Dependencies
- Spring Boot Starter Web
- Spring Boot Starter Data JPA
- Spring Boot Starter Security
- Spring Boot Starter OAuth2 Client
- Spring Boot Starter Validation
- MySQL Connector
- Lombok
- Jackson for JSON processing

## 📁 Project Structure

```
src/main/java/group1/commerce/
├── controller/          # REST controllers and request handlers
├── service/            # Business logic layer
├── repository/         # Data access layer
├── entity/            # JPA entities
├── dto/               # Data transfer objects
├── mapper/            # Entity-DTO mappers
└── security/          # Security configurations and custom user details

src/main/resources/
└── templates/         # Thymeleaf HTML templates
```

## 🔐 Security Features

- **Password Encryption**: BCrypt hashing for secure password storage
- **Role-Based Access Control**: Separate permissions for customers and administrators
- **OAuth2 Integration**: Social login with Google and Facebook
- **Session Management**: Secure session handling for cart and user data
- **Purchase Restrictions**: Ability to restrict problematic users from making purchases

## 💡 Technical Highlights

### Cart Management
Implemented a dual-cart system:
- **Guest Users**: Session-based cart storage
- **Registered Users**: Database-persisted cart with automatic merge on login

### Order Workflow
Complete order lifecycle management:
```
PLACED → CONFIRMED → SHIPPED → DELIVERED → REVIEWED
                ↓
            CANCELLED (with reason tracking)
```

### File Upload System
- Secure image upload with validation
- Automatic filename generation to prevent conflicts
- Old image cleanup on product updates
- Organized storage in `public/images/`

### Review Aggregation
- Real-time average rating calculation
- Review count tracking
- Automatic order status update when all items are reviewed

## 🚀 Getting Started

### Prerequisites
- Java 17 or higher
- MySQL 8.0+
- Maven 3.6+

### Installation

1. **Clone the repository**
```bash
git clone [repository-url]
cd commerce
```

2. **Configure Database**
Create a MySQL database and update `application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/your_database
spring.datasource.username=your_username
spring.datasource.password=your_password
```

3. **Configure OAuth2 (Optional)**
Add your OAuth2 credentials in `application.properties`:
```properties
spring.security.oauth2.client.registration.google.client-id=your-client-id
spring.security.oauth2.client.registration.google.client-secret=your-client-secret
```

4. **Build and Run**
```bash
mvn clean install
mvn spring-boot:run
```

5. **Access the Application**
```
http://localhost:8080/4Moos
```

## 📊 Database Schema

Key entities include:
- **User**: Customer and admin account information
- **Product**: Product catalog with details and inventory
- **ProductDetails**: Extended product information (price, quantity, ratings)
- **CartItem**: Shopping cart entries
- **Orders**: Order information and fulfillment details
- **OrderItem**: Individual items within an order
- **OrderStatus**: Order status history tracking
- **Reviews**: Product reviews and ratings

## 🎯 Future Enhancements

- Payment gateway integration (Stripe/PayPal)
- Email notifications for order updates
- Advanced analytics dashboard
- Wishlist functionality
- Product comparison feature
- Multi-language support
- Mobile responsive optimization
- RESTful API for mobile app integration

## 📝 API Endpoints

### Public Endpoints
- `GET /4Moos` - Home page
- `GET /4Moos/shop-all/{page}` - Browse all products
- `GET /4Moos/product/{id}` - Product details
- `POST /4Moos/cart/add` - Add to cart

### Protected Endpoints (Authenticated Users)
- `GET /4Moos/profile` - User profile
- `POST /4Moos/checkout` - Checkout process
- `GET /4Moos/orders/{page}` - Order history
- `POST /4Moos/order/{id}/review` - Submit review

### Admin Endpoints
- `GET /4Moos/admin/products/{page}` - Manage products
- `POST /4Moos/admin/products/create` - Create product
- `GET /4Moos/admin/orders/{page}` - Manage orders
- `GET /4Moos/admin/users/{page}` - Manage users

## 🤝 Contributing

This is an academic/portfolio project. If you'd like to suggest improvements, please feel free to reach out.

## 📄 License

[Specify your license here]

## 📧 Contact

**Your Name** - [Your Email]
- LinkedIn: [Your LinkedIn]
- Portfolio: [Your Portfolio URL]

---

⭐ If you found this project interesting, please consider giving it a star!
