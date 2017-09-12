package dejssa.justmoney;

/**
 * Created by Алексей on 11.09.2017.
 */

public class Currency {
    private String name;
    private String value;

    public Currency(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }
}
