# void getCountryAndQuoteFromServer()

## Java

```java
public static void getCountryAndQuoteFromServer() {
    try {
        URL url = new URL(" ");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);
        String payload = " ";
        OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
        writer.write(payload);
        writer.flush();
        writer.close();
        BufferedReader reader =
                new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        String json = sb.toString();
        int countryStart = json.indexOf(" ") + 11;
        int countryEnd = json.indexOf(" ", countryStart);
        String country = json.substring(countryStart, countryEnd);
        int quoteStart = json.indexOf(" ") + 9;
        int quoteEnd = json.indexOf(" ", quoteStart);
        String quote = json.substring(quoteStart, quoteEnd);
        quote = quote.replace(" ", " ");
        System.out.println(" " + country);
        System.out.println(" " + quote);
    } catch (Exception e) {
        e.printStackTrace();
        System.out.println("Error connecting to the server");
    }
}
```

## Pseudocode

```java
BEGIN

TRY TO
    Assign `<URL> url `= `empty url`;
    Assign `<Connection> conn` = `connection to <URL> url`;
ON EXCEPTION
    PRINT ERROR containing `stacktrace`;
    PRINT ERROR "Error connecting to the server";

END
```
