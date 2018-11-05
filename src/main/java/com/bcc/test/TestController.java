package com.bcc.test;

//@Controller
public class TestController {

//    @Inject
    private TestService testService;

    public TestService getTestService() {
        return testService;
    }
}
