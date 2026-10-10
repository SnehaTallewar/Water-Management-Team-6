
package com.water.water_infrastructure_service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class PumpApiTest {

    public static void main(String[] args) throws Exception {

        HttpClient client = HttpClient.newHttpClient();

        String updateData = """
                {
                    "name": "Updated Main Pump",
                    "location": "Zone B",
                    "status": "Under Maintenance",
                    "capacity": 750
                }
                """;

        HttpRequest updateRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8084/api/pumps/1"))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(updateData))
                .build();

        HttpResponse<String> updateResponse = client.send(
                updateRequest,
                HttpResponse.BodyHandlers.ofString());

        System.out.println("Update status: " + updateResponse.statusCode());
        System.out.println("Update response: " + updateResponse.body());

        HttpRequest getRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8084/api/pumps"))
                .GET()
                .build();

        HttpResponse<String> getResponse = client.send(
                getRequest,
                HttpResponse.BodyHandlers.ofString());

        System.out.println("All pumps: " + getResponse.body());
    }
}
