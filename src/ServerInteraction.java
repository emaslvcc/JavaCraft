import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class ServerInteraction {
    private static final String GROUP_NAME = "group82";
    private static final String GROUP_NUMBER = "82";
    private static final String DIFFICULTY = "hard";

    public static String getCountryAndQuoteFromServer() {
        try {
            URL url = new URL("https://flag.ashish.nl/get_flag");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);
            String payload = "{\"group_number\": \"" + GROUP_NUMBER + "\",\"group_name\": \"" + GROUP_NAME + "\",\"difficulty_level\": \"" + DIFFICULTY + "\"}";

            OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
            writer.write(payload);
            writer.flush();
            writer.close();
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            String json = sb.toString();
            int countryStart = json.indexOf(":\"") + 2;
            int countryEnd = json.indexOf("\",", countryStart);
            String country = json.substring(countryStart, countryEnd);
            int quoteStart = json.indexOf(":\"", countryEnd) + 2;
            int quoteEnd = json.indexOf("\"", quoteStart);
            String quote = json.substring(quoteStart, quoteEnd);
            quote = quote.replace(" ", " ");

            System.out.println(" " + country);
            System.out.println(" " + quote);
            return country;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error connecting to the server");
        }
        return "";
    }

}
