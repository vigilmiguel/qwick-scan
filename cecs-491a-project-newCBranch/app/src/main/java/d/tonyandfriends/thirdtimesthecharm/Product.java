package d.tonyandfriends.thirdtimesthecharm;

public class Product {
    private String name;
    private String dateRecentlyScanned;
    private int scanCount;

    public Product() {

    }

    public Product(String name, String dateRecentlyScanned, int scanCount) {
        this.name = name;
        this.dateRecentlyScanned = dateRecentlyScanned;
        this.scanCount = scanCount;
    }

    public String getName() {
        return name;
    }

    public String getDateRecentlyScanned() {
        return dateRecentlyScanned;
    }

    public int getScanCount() {
        return scanCount;
    }
}
