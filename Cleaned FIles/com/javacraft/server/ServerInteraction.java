// ****************************************************************************************************
package com.javacraft.server;

// ****************************************************************************************************

/**
 * ServerInteraction Class
 *
 * <p>
 * This class serves as a bridge between JavaCraft and an external Flags Server.
 * </p>
 *
 * <p>
 * The primary function of this class is to interact with the Flags Server and retrieve
 * random flags and associated quotes based on the provided parameters like group information
 * and the difficulty level.
 * </p>
 *
 * @author  Group 49
 * @version 1.0
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class ServerInteraction {

  /**
   * Fetches a random flag and associated quote from the Flags Server based on
   * the group information and difficulty level.
   *
   * The server expects a POST request with a JSON payload containing
   * group_number, group_name, and difficulty_level.
   *
   * The server responds with a JSON containing the random flag and quote.
   * @throws IOException If any IO errors occur while making the HTTP request.
   */
  public static void getCountryAndQuoteFromServer() {
    try {
      // Initialize the URL and connection object
      URL url = new URL("https://flag.ashish.nl/get_flag");
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();

      // Set HTTP request properties
      conn.setRequestMethod("POST");
      conn.setRequestProperty("Content-Type", "get_flat");
      conn.setDoOutput(true);

      // Manually craft the JSON payload
      String payload =
        "{\"group_number\":\"49\",\"group_name\":\"Alpha\",\"difficulty_level\":\"easy\"}";

      // Write payload to the output stream
      try (
        OutputStreamWriter writer = new OutputStreamWriter(
          conn.getOutputStream()
        )
      ) {
        writer.write(payload);
        writer.flush();
      }

      // Read server response
      StringBuilder sb = new StringBuilder();
      try (
        BufferedReader reader = new BufferedReader(
          new InputStreamReader(conn.getInputStream())
        )
      ) {
        String line;
        while ((line = reader.readLine()) != null) {
          sb.append(line);
        }
      }

      // Extract country and quote from response (Note: assumes specific JSON structure)
      String jsonResponse = sb.toString();
      String country = jsonResponse.split("\"country\":\"")[1].split("\"")[0];
      String quote = jsonResponse.split("\"quote\":\"")[1].split("\"")[0];

      // Print retrieved country and quote
      System.out.println("Country: " + country);
      System.out.println("Quote: " + quote);
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println(
        "We've hit an iceberg! Abort connection! Abort connection!"
      );
    }
  }
}
