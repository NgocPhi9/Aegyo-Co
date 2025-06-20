package group1.commerce.repository;

import group1.commerce.entity.Orders;
import group1.commerce.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
    List<Product> findProductsByProductName(String productName);
    @Query("SELECT p FROM Product p ORDER BY p.productDetails.soldQuantity DESC")
    List<Product> findTopBestsellers(Pageable pageable);

    @Query("SELECT p FROM Product p ORDER BY p.productDetails.averageRating DESC")
    List<Product> findFeaturedProducts(Pageable pageable);

    @Query("SELECT p FROM Product p ORDER BY p.createdAt DESC")
    List<Product> findNewProducts(Pageable pageable);

    Page<Product> findProductsByCategory(String category, Pageable pageable);

    @Override
    Page<Product> findAll(Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.artist = :artist")
    Page<Product> findProductsByArtist(String artist, Pageable pageable);

    @Query("SELECT p.artist FROM Product p ORDER BY p.productDetails.soldQuantity DESC")
    List<String> findTopSellingArtistNames(Pageable pageable);

    @Query("SELECT DISTINCT p.artist FROM Product p WHERE p.artist IS NOT NULL AND p.artist <> '' ORDER BY p.artist ASC")
    List<String> findAllArtist();

    Page<Product> findByProductNameContainingIgnoreCase(String keyword, Pageable pageable);

    // In ProductRepository.java

    // Finds products by the same artist, excluding the current product ID
    List<Product> findByArtistAndIdProductNot(String artist, String idProduct, Pageable pageable);

    // Finds products in the same category, excluding the current product ID
    List<Product> findByCategoryAndIdProductNot(String category, String idProduct, Pageable pageable);


}
