package io.levysworks.Utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddressValidator {
    public static boolean isAddressValid(String address) {
        Pattern pattern = Pattern.compile("(([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])");
        Matcher matcher = pattern.matcher(address);

        return matcher.matches() || address.equals("localhost");
    }

    public static boolean isPrivateAddress(String address) {
        String[] octets = address.split("\\.");
        return octets[0].matches("172") || (octets[0].matches("192") && octets[1].matches("168")) || (octets[0].equals("10") && octets[1].matches("^(1[6-9]|2[0-9]|3[0-1])$") || address.equals("0.0.0.0") || address.equals("127.0.0.1") || address.equals("localhost"));
    };
}