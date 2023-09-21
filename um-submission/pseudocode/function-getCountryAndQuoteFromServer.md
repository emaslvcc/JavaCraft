# function getCountryAndQuoteFromServer()

```java
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
```

1. **TRY TO:** Create an inputstream from parameter fileName for the corresponding file to deserialize its data.

   **CATCH:** for ClassNotFoundException or IOException: Print "Error while loading the game state: `<errormessage from exception>`"
   1. Get the new world width as `JavaCraft.NEW_WORLD_WIDTH` from the created inputstream
   2. Get the new world height as `JavaCraft.NEW_WORLD_HEIGHT` from the created inputstream
   3. Get the game world as `JavaCraft.world` from the created inputstream
   4. Get the players X position as `JavaCraft.playerX` from the created inputstream
   5. Get the players Y position as `JavaCraft.playerY` from the created inputstream
   6. Get the players inventory as `JavaCraft.inventory` from the created inputstream
   7. Get the players crafted items as `JavaCraft.craftedItems` from the created inputstream
   8. Get the value of the unlock mode as `JavaCraft.unlockMode` from the created inputstream
2.  Print "Game state loaded from file: `<fileName>`"
3.  Wait for player to press ENTER
