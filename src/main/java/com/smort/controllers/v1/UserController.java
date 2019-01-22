package com.smort.controllers.v1;

import com.smort.api.v1.model.PasswordDTO;
import com.smort.api.v1.model.UserInfoDTO;
import com.smort.domain.RolesEnum;
import com.smort.services.UserInfoService;
import io.swagger.annotations.*;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Api(description = "User controller")
@RestController
@Validated
@RequestMapping(UserController.BASE_URL)
public class UserController {

    public static final String BASE_URL = "/api/v1/users";

    private final UserInfoService userInfoService;

    public UserController(UserInfoService userInfoService) {
        this.userInfoService = userInfoService;
    }

    @ApiOperation(value = "Get List of Users")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<UserInfoDTO> getUsers() {
        return userInfoService.getAllUsers();
    }

    @ApiOperation(value = "Create new user account")
    @PostMapping
    @ApiResponses(value = {@ApiResponse(code = 400, message = "Bad Request")})
    @ResponseStatus(HttpStatus.CREATED)
    public UserInfoDTO createNewUser(@RequestBody @ApiParam("User information for new User to be added") @Valid UserInfoDTO userInfoDTO) {
        return userInfoService.createNewUser(userInfoDTO);
    }

    @ApiOperation(value = "Set user role")
    @PostMapping("/{id}/roles")
    @ApiResponses(value = {@ApiResponse(code = 400, message = "Bad Request")})
    @ResponseStatus(HttpStatus.OK)
    public UserInfoDTO setRole(@RequestParam RolesEnum role, @PathVariable Long id) {
        return userInfoService.setRole(role, id);
    }

    @ApiOperation(value = "Activate user")
    @PostMapping("/{id}/activate")
    @ResponseStatus(HttpStatus.OK)
    public UserInfoDTO setActive(@PathVariable Long id) {
        return userInfoService.activateUser(id);
    }

    @ApiOperation(value = "Delete user")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteUser(@PathVariable Long id) {
        userInfoService.deleteUserById(id);
    }

    @ApiOperation(value = "Edit user")
    @PutMapping("/{id}")
    @ApiResponses(value = {@ApiResponse(code = 400, message = "Bad Request")})
    @ResponseStatus(HttpStatus.OK)
    public UserInfoDTO editUser(@ApiParam("User information for user to be edited") @Valid @RequestBody UserInfoDTO userInfoDTO, @PathVariable Long id) {
        return userInfoService.editUser(userInfoDTO, id);
    }

    @ApiOperation(value = "Get user by id")
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserInfoDTO getUser(@PathVariable Long id) {
        return userInfoService.getUserById(id);
    }

    @ApiOperation(value = "Revoke user role")
    @DeleteMapping("/{id}/roles")
    @ApiResponses(value = {@ApiResponse(code = 400, message = "Bad Request")})
    @ResponseStatus(HttpStatus.OK)
    public UserInfoDTO revokeRole(@PathVariable Long id, @RequestParam RolesEnum role) {
        return userInfoService.revokeRole(id, role);
    }

    @ApiOperation(value = "Reset user password")
    @PostMapping("/{id}/password")
    @ApiResponses(value = {@ApiResponse(code = 400, message = "Bad Request")})
    @ResponseStatus(HttpStatus.OK)
    public UserInfoDTO changePassword(@PathVariable Long id, @RequestBody PasswordDTO passwordDTO) {
        return userInfoService.resetPassword(id, passwordDTO);
    }


}
