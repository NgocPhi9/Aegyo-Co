package group1.commerce.controller;

import group1.commerce.dto.UserWithOrderCount;
import group1.commerce.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/4Moos/admin")
public class AdminUserController {
    private final UserService userService;

    public AdminUserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users/{page}")
    public String getUsers(
            @PathVariable("page") int page,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false, name = "sort") String sort,
            Model model
    ) {
        Boolean restrictedFilter = null;

        if (sort != null && !sort.isEmpty()) {
            switch (sort) {
                case "restricted_true" -> restrictedFilter = true;
                case "restricted_false" -> restrictedFilter = false;
            }
        }

        Page<UserWithOrderCount> userPage = userService.filterUsers(keyword, restrictedFilter, sort, page - 1, 16);

        model.addAttribute("users", userPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", userPage.getTotalPages());
        model.addAttribute("totalItems", userPage.getTotalElements());
        model.addAttribute("baseUrl", "/4Moos/admin/users");
        model.addAttribute("keyword", keyword);
        model.addAttribute("currentSortOption", sort);

        return "admin/users";
    }

    @PostMapping("/user/restrict/{id}")
    public String restrictUser(@PathVariable("id") String id, @RequestParam(required = false) Integer page,
                               @RequestParam(required = false, name = "sort") String sort) {
        userService.setRestrict(id);
        return "redirect:/4Moos/admin/users/" + page + "?sort=" + sort;
    }

}
