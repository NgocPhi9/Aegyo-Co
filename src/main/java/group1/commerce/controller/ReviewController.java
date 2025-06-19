package group1.commerce.controller;

import group1.commerce.entity.Reviews;
import group1.commerce.service.ReviewService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ReviewController {
    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping("/4Moos/order/{idOrderItem}/review")
    public String submitReview(@PathVariable int idOrderItem,
                               @ModelAttribute("review") @Valid Reviews review,
                               BindingResult result,
                               RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Review could not be submitted.");
            int orderId = reviewService.getOrderIdFromOrderItem(idOrderItem);
            return "redirect:/4Moos/order/" + orderId;
        }
        int orderId = reviewService.saveReview(review, idOrderItem);

        redirectAttributes.addFlashAttribute("successMessage", "Thank you for your review!");
        return "redirect:/4Moos/order/" + orderId;
    }
}
