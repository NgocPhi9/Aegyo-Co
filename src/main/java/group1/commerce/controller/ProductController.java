package group1.commerce.controller;

import group1.commerce.dto.ProductDTO;
import group1.commerce.dto.UserDTO;
import group1.commerce.entity.Product;
import group1.commerce.entity.ProductSortOption;
import group1.commerce.entity.Reviews;
import group1.commerce.entity.User;
import group1.commerce.repository.ReviewRepository;
import group1.commerce.service.ProductService;
import group1.commerce.service.ReviewService;
import group1.commerce.service.UserService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/4Moos")
public class ProductController {
    private final ProductService productService;
    private final ReviewService reviewService;
    private final ReviewRepository reviewRepository;
    private final UserService userService;

    public ProductController(ProductService productService, ReviewService reviewService, ReviewRepository reviewsRepository, UserService userService) {
        this.productService = productService;
        this.reviewService = reviewService;
        this.reviewRepository = reviewsRepository;
        this.userService = userService;
    }

    private void addPaginationAttributesToModel(Model model, Page<ProductDTO> productsPage, String listAttributeName, int currentViewPage, String currentSortOption, String baseUrl) {
        List<ProductDTO> contentList = productsPage.getContent();

        model.addAttribute("currentPage", currentViewPage);
        model.addAttribute("totalPages", productsPage.getTotalPages());
        model.addAttribute("totalItems", productsPage.getTotalElements());
        model.addAttribute(listAttributeName, contentList);

        // For the sort fragment
        model.addAttribute("currentSortOption", currentSortOption);
        model.addAttribute("sortOptions", Arrays.asList(ProductSortOption.values()));
        model.addAttribute("baseUrl", baseUrl);

    }

    @GetMapping
    public String index(Model model) {
        List<ProductDTO> products = productService.getFeaturedProducts(10);
        model.addAttribute("products", products);
        return "index";
    }

    // Get all products
    @GetMapping("/shop-all/{page}")
    public String getAllProducts(@PathVariable (value = "page") int page,
                                 @RequestParam(name = "sort", defaultValue = "BEST_SELLING") String sortOptionString, Model model) {
        int pageSize = 16;
        Page<ProductDTO> productsPage = productService.getAllProducts(page, pageSize, sortOptionString);
        addPaginationAttributesToModel(model, productsPage, "products", page, sortOptionString, "/4Moos/shop-all");
        return "shop-all";
    }

    @GetMapping("/best-sellers")
    public String getBestSellers(Model model) {
        List<ProductDTO> products = productService.getBestSellers(16);
        model.addAttribute("products", products);
        return "best-sellers";
    }

    @GetMapping("/new-releases")
    public String getNewReleases(Model model) {
        List<ProductDTO> products = productService.getNewReleases(16);
        model.addAttribute("products", products);
        return "new-releases";
    }

    @GetMapping("/")
    public String getTopSellingArtistNames(Model model) {
        List<ProductDTO> products = productService.getNewReleases(2);
        model.addAttribute("products", products);
        return "new-releases";
    }

    @GetMapping("/albums/{page}")
    public String getAlbums(@PathVariable (value = "page") int page,
                            @RequestParam(name = "sort", defaultValue = "BEST_SELLING") String sortOptionString, Model model) {
        int pageSize = 16;
        Page<ProductDTO> productsPage = productService.getProductsByCategory("ALBUM", page, pageSize, sortOptionString);
        addPaginationAttributesToModel(model, productsPage, "products", page, sortOptionString, "/4Moos/albums");
        return "albums";
    }

    @GetMapping("/lightsticks/{page}")
    public String getLighsticks(@PathVariable (value = "page") int page,
                            @RequestParam(name = "sort", defaultValue = "BEST_SELLING") String sortOptionString, Model model) {
        int pageSize = 16;
        Page<ProductDTO> productsPage = productService.getProductsByCategory("LIGHTSTICK", page, pageSize, sortOptionString);
        addPaginationAttributesToModel(model, productsPage, "products", page, sortOptionString, "/4Moos/lightsticks");
        return "lightsticks";
    }

    @GetMapping("/md/{page}")
    public String getMD(@PathVariable (value = "page") int page,
                                @RequestParam(name = "sort", defaultValue = "BEST_SELLING") String sortOptionString, Model model) {
        int pageSize = 16;
        Page<ProductDTO> productsPage = productService.getProductsByCategory("MD", page, pageSize, sortOptionString);
        addPaginationAttributesToModel(model, productsPage, "products", page, sortOptionString, "/4Moos/md");
        return "md";
    }

    @GetMapping("/artists/{artist}/{page}")
    public String getArtists(@PathVariable (value = "artist") String artist,
                             @PathVariable (value = "page") int page,
                             @RequestParam(name = "sort", defaultValue = "BEST_SELLING") String sortOptionString,Model model) {
        int pageSize = 16;
        Page<ProductDTO> productsPage = productService.getProductsByArtist(artist, page, pageSize, sortOptionString);
        addPaginationAttributesToModel(model, productsPage, "products", page, sortOptionString, "/4Moos/artists" + artist);
        // Add artistName to the model for the title and pagination links
        model.addAttribute("artistName", artist);

        return "artist";
    }


    @GetMapping("/all-artists")
    public String showAllArtistsPage(Model model) {
        List<String> allArtistNames = productService.getAllDistinctArtistNames();

        // Define the key for the "other" group
        final String OTHER_GROUP_KEY = "#";

        // Group artists: A-Z and a special '#' group
        // Using TreeMap with a custom comparator to ensure '#' comes last
        Map<String, List<String>> groupedArtists = new TreeMap<>(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                if (o1.equals(OTHER_GROUP_KEY) && o2.equals(OTHER_GROUP_KEY)) {
                    return 0;
                }
                if (o1.equals(OTHER_GROUP_KEY)) {
                    return 1; // '#' group should come after letters
                }
                if (o2.equals(OTHER_GROUP_KEY)) {
                    return -1; // Letters should come before '#' group
                }
                return o1.compareTo(o2); // Standard alphabetical comparison for letters
            }
        });

        if (allArtistNames != null) {
            for (String artistName : allArtistNames) {
                if (artistName == null || artistName.trim().isEmpty()) {
                    continue; // Skip null or empty names
                }
                char firstChar = artistName.toUpperCase().charAt(0);
                String groupKey;

                if (firstChar >= 'A' && firstChar <= 'Z') {
                    groupKey = String.valueOf(firstChar);
                } else {
                    groupKey = OTHER_GROUP_KEY;
                }
                groupedArtists.computeIfAbsent(groupKey, k -> new ArrayList<>()).add(artistName);
            }
        }

        // The artist names within each group are already sorted if your repository query sorts them.
        // If not, you might need to sort each list in the map:
        // groupedArtists.forEach((key, list) -> Collections.sort(list));

        model.addAttribute("groupedArtists", groupedArtists);
        model.addAttribute("otherGroupKey", OTHER_GROUP_KEY);

        return "all-artists"; // Your existing template name
    }


    // Get product by id
    @GetMapping("/product/{id}")
    public String getProductById(@PathVariable String id, Model model) {
        Optional<ProductDTO> product = productService.getProductById(id);
        product.map(productDTO -> model.addAttribute("product", productDTO));
        productService.viewProduct(id);

        // See reviews
        List<Reviews> reviews = reviewService.getReviewsByProduct(id);
        model.addAttribute("reviews", reviews);
        model.addAttribute("newComment", new Reviews());
        return "product";
    }

    // In ProductController.java

    // Add this new method
    @PostMapping("/product/{id}/comment")
    public String saveComment(@PathVariable String id,
                              @ModelAttribute("user") UserDTO loggedInUser,
                              @RequestParam String commentText) {

        productService.getProductById(id).ifPresent(productDTO -> {
            Product product = new Product();
            product.setIdProduct(productDTO.getIdProduct());

            Reviews comment = new Reviews();
            comment.setProduct(product);
            comment.setComment(commentText);

            // If a user is logged in, associate them with the comment
            if (loggedInUser != null) {
                User userEntity = userService.getUserById(loggedInUser.getIdUser());
                comment.setUser(userEntity);
            }
            // If no one is logged in, the user field on the comment remains null.

            reviewRepository.save(comment);
        });

        return "redirect:/4Moos/product/" + id;
    }

    @GetMapping("/search/{page}")
    public String searchResults(@RequestParam("keyword") String keyword,
                                @PathVariable("page") int page,
                                @RequestParam(name = "sort", defaultValue = "BEST_SELLING") String sortOptionString,
                                Model model) {
        int pageSize = 16;
        Page<ProductDTO> resultsPage = productService.searchProducts(keyword, page, pageSize, sortOptionString);

        model.addAttribute("products", resultsPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", resultsPage.getTotalPages());
        model.addAttribute("totalItems", resultsPage.getTotalElements());

        // Pass sorting data
        model.addAttribute("currentSortOption", sortOptionString);
        model.addAttribute("sortOptions", Arrays.asList(ProductSortOption.values()));

        model.addAttribute("keyword", keyword);

        String baseUrl = "/4Moos/search";
        Map<String, String> otherParams = new HashMap<>();
        otherParams.put("keyword", keyword);
        model.addAttribute("baseUrl", baseUrl);
        model.addAttribute("otherParams", otherParams);


        return "search-results";
    }

}
