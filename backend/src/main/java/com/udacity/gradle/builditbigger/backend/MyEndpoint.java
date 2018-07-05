package com.udacity.gradle.builditbigger.backend;

import com.example.android.jokes.Joker;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;


@Api(
        name = "myApi",
        version = "v1",
        namespace = @ApiNamespace(
                ownerDomain = "backend.builditbigger.gradle.udacity.com",
                ownerName = "backend.builditbigger.gradle.udacity.com",
                packagePath = ""
        )
)
public class MyEndpoint {


    @ApiMethod(name = "fetchData")
    public MyBean fetchData() {
        MyBean response = new MyBean();
        Joker joker = new Joker();
        response.setData(joker.getJoke());
        System.out.print("joker " + joker.getJoke());
        return response;
    }
}
