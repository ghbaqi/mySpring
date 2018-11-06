package com.bcc.test;

import com.bcc.annotation.Action;
import com.bcc.annotation.Controller;
import com.bcc.annotation.Inject;

import java.util.Arrays;
import java.util.List;

@Controller
public class TestController {

    @Inject
    private TestService testService;

    public TestService getTestService() {
        return testService;
    }

    @Action("get:/getList")
    public List<String> getList() {
        return Arrays.asList("aa", "bb", "cc");
    }

    @Action("post:update")
    public void updatePersion() {
        System.out.println("updatePersion ...");
    }
}
