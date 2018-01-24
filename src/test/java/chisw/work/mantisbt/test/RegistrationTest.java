package chisw.work.mantisbt.test;

import org.testng.annotations.Test;

public class RegistrationTest extends TestBase {

    @Test
    public void testRegistration() {
        app.registration().start("user_one", "user_one@mail.com");
    }
}
