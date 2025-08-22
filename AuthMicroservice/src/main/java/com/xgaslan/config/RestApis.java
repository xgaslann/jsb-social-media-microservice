package com.xgaslan.config;

import org.springframework.core.env.Environment;

public class RestApis {
    public static final String DEVELOPMENT = "/dev";
    public static final String TEST = "/test";
    public static final String PRODUCTION = "/prod";
    public static final String VERSIONS = "/v1";

    public static final String AUTH_SERVICE = DEVELOPMENT + VERSIONS + "/auth";
    public static final String REGISTER = "/register";
    public static final String LOGIN = "/login";
}
