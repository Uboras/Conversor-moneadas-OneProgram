package com.one.conversor.modelos.conexion;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 *
 * @author Hernan Manera
 */
public class ExchangeRateClient {
    //comparacion vivo

    private static final String url = "https://v6.exchangerate-api.com/v6/Your-----Key/pair/";
    //lasted
    private static final String urlRequest = "https://v6.exchangerate-api.com/v6/Your-----Key/latest/USD";
    //cliente http global
    private static HttpClient client = HttpClient.newHttpClient();

    public ExchangeRateClient() {
    }

    public static String request() {

        try {
            // se crea la request
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(urlRequest))
                    .build();
            //se envia la request
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            //metodo para sacar las cotizaciones de 5 pares
            String respuesta = response.body();
            // se crea un Gson para transforamr la info en json
            Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.IDENTITY.UPPER_CASE_WITH_UNDERSCORES).create();
            JsonObject jsonObject = gson.fromJson(respuesta, JsonObject.class);
            // Extraer las tasas de cambio de las 5 monedas más conocidas, buscnado en el json el objeto que tiene las cotizaciones
            JsonObject ratesNode = jsonObject.getAsJsonObject("conversion_rates");
            String jsonString = ratesNode.toString();
            return jsonString;
        } catch (IOException ex) {
            return "Error de IOExption" + ex.getMessage();
        } catch (InterruptedException ex) {
            return "Error de interrupcion" + ex.getMessage();
        }

    }

    public static String requestCotizacion(String par, String par2) {

        try {
            //se crea el cliente
            String urlFinal = url + "/" + par + "/" + par2;
            // se crea la request
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(urlFinal))
                    .build();
            //se envia la request
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            
            
            Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.IDENTITY.UPPER_CASE_WITH_UNDERSCORES).create();
            JsonObject jsonObject = gson.fromJson(response.body(), JsonObject.class);
            
            // Extraer las tasas de cambio de las 5 monedas más conocidas, buscnado en el json el objeto que tiene las cotizaciones
            JsonElement ratesNode = jsonObject.get("conversion_rate");

            String jsonString = String.valueOf(ratesNode.getAsDouble());
            
            return jsonString;
        } catch (IOException ex) {
            return "Error de IOExption" + ex.getMessage();
        } catch (InterruptedException ex) {
            return "Error de interrupcion" + ex.getMessage();
        }

    }
}
