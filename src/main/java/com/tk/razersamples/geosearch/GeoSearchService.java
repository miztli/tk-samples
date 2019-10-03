package com.tk.razersamples.geosearch;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import pb.ApiClient;
import pb.ApiException;
import pb.Configuration;
import pb.auth.OAuth;
import pb.locationintelligence.LIAPIGeoSearchServiceApi;
import pb.locationintelligence.model.GeosearchLocations;

@Service
public class GeoSearchService
{
    // FIXME Assign your Client Id here
    private static String API_KEY = "vHEOFlu7fxjFIrGYJ8dU4QkMoG5Mmkef";

    // FIXME Assign your Secret here
    private static String SECRET = "Vsoa60o5AFGYznbT";

    private String country = "USA";

    public Map<String, Object> testGeoSearch(final String address)
    {
        Map<String, Object> values = new HashMap<>();
        ApiClient client = Configuration.getDefaultApiClient();

        OAuth oAuth2Password = (OAuth) client.getAuthentication("oAuth2Password");
        oAuth2Password.setApiKey(API_KEY);
        oAuth2Password.setSecret(SECRET);

        LIAPIGeoSearchServiceApi searchApi = new LIAPIGeoSearchServiceApi();
        try
        {
            GeosearchLocations response = searchApi
                .geoSearch(address, null, null, null, null, null, country, null, null, null, null, null, null, null, null, null);

            System.out.println(response.toString());
            values.put("ok", response);
            return values;
        }
        catch (ApiException e)
        {
            System.out.println("Exception thrown: " + e.getMessage());
            System.out.println(e);
            values.put("failed", e);
        }
        return values;
    }
}
