package utils;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DreamAIClassifier {

    private static final String MODEL = "gemini-3.6-flash";

    private static final String API_URL =
            "https://generativelanguage.googleapis.com/v1beta/models/"
                    + MODEL
                    + ":generateContent";

    private final HttpClient client = HttpClient.newHttpClient();

    public Map<String, String> classifyDreams(
            List<String> dreamNames) throws Exception {

        String apiKey = System.getenv("GEMINI_API_KEY");

        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException(
                    "GEMINI_API_KEY environment variable is not set."
            );
        }

        StringBuilder dreamList = new StringBuilder();

        for (String dream : dreamNames) {
            dreamList.append("- ")
                    .append(dream)
                    .append("\n");
        }

        String prompt =
                "Classify each dream as exactly one of Good or Bad.\n"
                        + "Return ONLY one line for each dream.\n"
                        + "Use this exact format:\n"
                        + "Dream Name = Good\n"
                        + "Dream Name = Bad\n\n"
                        + "Do not add explanations.\n"
                        + "Do not add numbering.\n\n"
                        + "Dreams:\n"
                        + dreamList;

        String jsonBody =
                "{"
                        + "\"contents\":[{"
                        + "\"parts\":[{"
                        + "\"text\":\"" + escapeJson(prompt) + "\""
                        + "}]"
                        + "}]"
                        + "}";

        int maxAttempts = 4;

        for (int attempt = 1; attempt <= maxAttempts; attempt++) {

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL))
                    .header("Content-Type", "application/json")
                    .header("x-goog-api-key", apiKey)
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            HttpResponse<String> response =
                    client.send(
                            request,
                            HttpResponse.BodyHandlers.ofString()
                    );

            // Successful response
            if (response.statusCode() == 200) {

                String result = extractText(response.body());

                return parseResults(
                        result,
                        dreamNames
                );
            }

            // Retry temporary server errors
            if (response.statusCode() == 503
                    && attempt < maxAttempts) {

                long waitTime =
                        (long) Math.pow(2, attempt) * 1000;

                System.out.println(
                        "Gemini returned 503. "
                                + "Retrying after "
                                + waitTime
                                + " ms."
                );

                Thread.sleep(waitTime);

                continue;
            }

            // Quota exceeded
            if (response.statusCode() == 429) {

                throw new RuntimeException(
                        "Gemini API quota exceeded. "
                                + "Please wait before running the AI test again.\n"
                                + response.body()
                );
            }

            // Other API errors
            throw new RuntimeException(
                    "Gemini API request failed. HTTP "
                            + response.statusCode()
                            + ": "
                            + response.body()
            );
        }

        throw new RuntimeException(
                "Gemini classification failed."
        );
    }

    private Map<String, String> parseResults(
            String response,
            List<String> dreamNames) {

        Map<String, String> results =
                new HashMap<>();

        String normalizedResponse =
                response
                        .replace("**", "")
                        .replace("`", "")
                        .trim();

        for (String dreamName : dreamNames) {

            String escapedDream =
                    Pattern.quote(dreamName);

            Pattern pattern =
                    Pattern.compile(
                            escapedDream
                                    + "\\s*=\\s*(Good|Bad)",
                            Pattern.CASE_INSENSITIVE
                    );

            Matcher matcher =
                    pattern.matcher(normalizedResponse);

            if (matcher.find()) {

                String classification =
                        matcher.group(1);

                if (classification.equalsIgnoreCase("Good")) {

                    results.put(
                            dreamName,
                            "Good"
                    );

                } else {

                    results.put(
                            dreamName,
                            "Bad"
                    );
                }
            }
        }

        // Verify every dream received an AI classification
        for (String dreamName : dreamNames) {

            if (!results.containsKey(dreamName)) {

                System.out.println(
                        "Gemini raw response:"
                );

                System.out.println(
                        normalizedResponse
                );

                throw new RuntimeException(
                        "Gemini did not classify dream: "
                                + dreamName
                );
            }
        }

        return results;
    }

    private String escapeJson(String text) {

        return text
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r");
    }

    private String extractText(String json) {

        String marker = "\"text\"";

        int markerIndex =
                json.indexOf(marker);

        if (markerIndex == -1) {

            throw new RuntimeException(
                    "Could not find classification text "
                            + "in Gemini response."
            );
        }

        int colonIndex =
                json.indexOf(
                        ":",
                        markerIndex
                );

        int firstQuote =
                json.indexOf(
                        "\"",
                        colonIndex + 1
                );

        int secondQuote =
                json.indexOf(
                        "\"",
                        firstQuote + 1
                );

        if (firstQuote == -1
                || secondQuote == -1) {

            throw new RuntimeException(
                    "Could not parse Gemini response."
            );
        }

        return json.substring(
                firstQuote + 1,
                secondQuote
        ).trim();
    }
}