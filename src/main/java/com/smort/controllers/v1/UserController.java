package com.smort.controllers.v1;

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

    public static final String BASE_URL = "api/v1/users";

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
    @PostMapping("/{id}")
    @ApiResponses(value = {@ApiResponse(code = 400, message = "Bad Request")})
    @ResponseStatus(HttpStatus.CREATED)
    public UserInfoDTO setRole(@RequestParam RolesEnum role, @PathVariable Long id) {
        return userInfoService.setRole(role, id);
    }

}
