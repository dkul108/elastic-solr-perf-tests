package perf;

import org.junit.Test;

public class RandomGuideTest {
    @Test
    public void showRandomGuide() {
        System.out.println(Common.randomGuide());
    }

    @Test
    public void showRandomGuideJson() throws Exception {
        System.out.println(Common.randomGuideJson());
    }
}
