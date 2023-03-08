package hello.proxy.app.v2;

public class OrderRepositoryV2 {

    public void save(String itemId) {
        if(itemId.equals("ex")) {
            throw new IllegalArgumentException();
        }

        sleep(1000);
    }

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
