package com.educator.admin;

import com.educator.common.dto.PaginatedResponse;
import com.educator.common.pagination.PageableFactory;
import com.educator.users.dto.AdminUserResponse;
import com.educator.users.dto.UpdateUserRolesRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/users")
public class AdminUserController {

    private final AdminUserService adminUserService;

    public AdminUserController(AdminUserService adminUserService) {
        this.adminUserService = adminUserService;
    }

    @GetMapping
    public PaginatedResponse<AdminUserResponse> listUsers(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size
    ) {
        Pageable pageable = PageableFactory.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        return new PaginatedResponse<>(adminUserService.getUsers(pageable));
    }

    @PutMapping("/{id}/roles")
    public ResponseEntity<AdminUserResponse> updateRoles(
            @PathVariable("id") Long userId,
            @Valid @RequestBody UpdateUserRolesRequest request
    ) {
        return ResponseEntity.ok(adminUserService.updateRoles(userId, request.getRoles()));
    }
}
