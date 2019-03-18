package d.tonyandfriends.thirdtimesthecharm;

@SuppressWarnings("WeakerAccess")
public class Product {
    private String productKey;
    private String name;
    private String dateRecentlyScanned;
    private int scanCount;

    public Product() {

    }

    public Product(String productKey, String name, String dateRecentlyScanned, int scanCount) {
        this.productKey = productKey;
        this.name = name;
        this.dateRecentlyScanned = dateRecentlyScanned;
        this.scanCount = scanCount;
    }

    public String getName() {
        return name;
    }

    public int getScanCount() {
        return scanCount;
    }

    public String getDateRecentlyScanned() {
        return dateRecentlyScanned;
    }

    public String getProductKey() {
        return productKey;
    }

    public void setProductKey(String productKey) {
        this.productKey = productKey;
    }

    public void setDateRecentlyScanned(String dateRecentlyScanned) {
        this.dateRecentlyScanned = dateRecentlyScanned;
    }

    public void setScanCount(int scanCount) {
        this.scanCount = scanCount;
    }
}
