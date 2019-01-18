package com.smort.controllers.v1;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(description = "User controller")
@RestController
@RequestMapping("")
public class UserController {

    public static String BASE_URL = "api/v1/users";

}
