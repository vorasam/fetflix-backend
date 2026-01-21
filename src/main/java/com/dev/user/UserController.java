package com.dev.user;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("admin/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @PatchMapping("/{id}/enabled")
    public void updateEnabled(
            @PathVariable Long id,
            @RequestParam boolean enabled
    ) {
        userService.updateEnabled(id, enabled);
    }
}

