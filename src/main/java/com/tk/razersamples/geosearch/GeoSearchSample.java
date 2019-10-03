package com.tk.razersamples.geosearch;

import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Base64;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

//import org.glassfish.jersey.internal.util.Base64;

/**
 * Code sample for accessing Location Intelligence Geosearch API. This program demonstrates the
 * capability of Location Intelligence Geosearch API to provide both independent and neutral local
 * search information based on the given searchText, location coordinates and
 * other parameters. To make it work, make sure to update your API_KEY and
 * SECRET before running this code.
 */
public class GeoSearchSample {


    // FIXME Assign your Client Id here
    private static String API_KEY = "vHEOFlu7fxjFIrGYJ8dU4QkMoG5Mmkef";

    // FIXME Assign your Secret here
    private static String SECRET = "Vsoa60o5AFGYznbT";
    private static String API_FRAGMENT = "geosearch/v2/locations";

    private static  String OAUTH2_TOKEN_URL = "https://api.pitneybowes.com/oauth/token";
    private static  String LOCATION_INTELLIGENCE_API_URL = "https://api.pitneybowes.com/location-intelligence/"+API_FRAGMENT;

    private static final String ACCESS_TOKEN = "access_token";

    private static final String BEARER = "Bearer ";

    private static final String BASIC = "Basic ";

    private static final String CLIENT_CREDENTIALS = "client_credentials";

    private static final String GRANT_TYPE = "grant_type";

    private static final String AUTH_HEADER = "Authorization";

    private static final String COLON = ":";

    private static String accessToken;

    private static String searchText = "times";
    private static String longitude = "-73.997533";
    private static String latitude = "40.761819";
    private static String country = "USA";

    /*public static void main(String[] args) {

        // Acquires OAuth2 token
        acquireAuthToken();

        tryXmlSamples();

        tryJsonSamples();
    }*/

    public static void tryXmlSamples() {

        // Search with only mandatory parameters, XML
        geoSearch(true, searchText, null, latitude, longitude, null, null, null, null, null, null, null, null, null, null, null, null);


        // Search with only mandatory parameters and searchRadiusUnit, XML
        geoSearch(true, searchText, null,  latitude, longitude, null, "MILES", null, null, null, null, null, null, null, null, null, null);

        // Search with only mandatory parameters and maxCandidates, XML
        geoSearch(true, searchText, null, latitude, longitude, null, null, "3", null, null, null, null, null, null, null, null, null);

        //With Country parameter.

        // Search with only mandatory parameters, XML
        geoSearch(true, searchText, country, latitude, longitude, null, null, null, null, null, null, null, null, null, null, null, null);


        // Search with only mandatory parameters and searchRadiusUnit, XML
        geoSearch(true, searchText, country, latitude, longitude, null, "MILES", null, null, null, null, null, null, null, null, null, null);

        // Search with only mandatory parameters and maxCandidates, XML
        geoSearch(true, searchText, country, latitude, longitude, null, null, "3", null, null, null, null, null, null, null, null, null);

        // Search with only country  parameters  , XML
        geoSearch(true, searchText, country, null, null, null, null, null, null, null, null, null, null, null, null, null, null);

        // Search with IpAddress parameters  , XML
        geoSearch(true, "street", null, null, null, null, null, null, "1.2.3.4", "true", null, null, null, null, null, null, null);

        //Search with multiple line of address , XML
        geoSearch(true, "petrol", country, null, null, null, null, null, null, null, "MD", "Curtis Bay", "21226", null, null, null, null);
    }

    public static void tryJsonSamples() {

        // Search with only mandatory parameters, JSON
        geoSearch(false, searchText, null, latitude, longitude, null, null, null, null, null, null, null, null, null, null, null, null);

        // Search with only mandatory parameters and searchRadiusUnit, JSON
        geoSearch(false, searchText, null, latitude, longitude, null, "MILES", null, null, null, null, null, null, null, null, null, null);

        // Search with only mandatory parameters and maxCandidates, JSON
        geoSearch(false, searchText, null, latitude, longitude, null, null, "3", null, null, null, null, null, null, null, null, null);

        // Search with only mandatory parameters, JSON
        geoSearch(false, searchText, country, latitude, longitude, null, null, null, null, null, null, null, null, null, null, null, null);

        // Search with only mandatory parameters and searchRadiusUnit, JSON
        geoSearch(false, searchText, country, latitude, longitude, null, "MILES", null, null, null, null, null, null, null, null, null, null);

        // Search with country, lat & long mandatory parameters and maxCandidates, JSON
        geoSearch(false, searchText, country, latitude, longitude, null, null, "3", null, null, null, null, null, null, null, null, null);

        // Search with country parameter, JSON
        geoSearch(false, searchText, country, null, null, null, null, null, null, null, null, null, null, null, null, null, null);

        // Search with IpAddress parameters  , JSON
        geoSearch(false, "street", null, null, null, null, null, null, "1.2.3.4", "true", null, null, null, null, null, null, null);

        //Search with multiple line of address , JSON
        geoSearch(false, "petrol", country, null, null, null, null, null, null, null, "MD", "Curtis Bay", "21226", null, null, null, null);


        //Matchon address parameter
        geoSearch(false, "1%20Global%20V", country, "42.682815", "-105.239771", null, null, null, null, null, null, null, null, null, "true", null, null);

        geoSearch(false, "times%20sq", country, "40.761819", "-73.997533", "52800", null, "10", null, null, null, null, null, null, "false", "N", "Y");

    }

    /**
     * Acquires OAuth2 token for accessing Location Intelligence APIs
     */
    public static void acquireAuthToken() {
        String fullString = API_KEY + COLON + SECRET;
        String authHeader = BASIC
            + Base64.getEncoder().encodeToString(fullString.getBytes());

        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(OAUTH2_TOKEN_URL);

        Builder builder = target.request().header(AUTH_HEADER, authHeader);
        Form form = new Form();
        form.param(GRANT_TYPE, CLIENT_CREDENTIALS);
        Response response = builder.post(Entity.entity(form,
            MediaType.APPLICATION_FORM_URLENCODED));
        String jsonResponse = response.readEntity(String.class);

        JsonReader jsonReader = Json
            .createReader(new StringReader(jsonResponse));
        JsonObject jsonObject = jsonReader.readObject();
        jsonReader.close();
        accessToken = jsonObject.getString(ACCESS_TOKEN);
        accessToken = BEARER + accessToken;
    }

    /**
     * Generic client request processor that makes a Rest call to Location Intelligence APIs and
     * prints response in XML or JSON formats.
     *
     * @param responseTypeIsXml
     *            A boolean to indicate whether the response required is in XML
     *            or JSON
     * @param apiUrl
     *            Complete URL with query parameters
     */
    public static void processRequest(boolean responseTypeIsXml, String apiUrl) {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(apiUrl);
        Builder builder;
        if (responseTypeIsXml) {
            builder = target.request(MediaType.APPLICATION_XML).header(
                AUTH_HEADER, accessToken);
        } else {
            builder = target.request(MediaType.APPLICATION_JSON).header(
                AUTH_HEADER, accessToken);
        }
        System.out.println(builder.get(String.class));
    }

    /**
     * Print Geosearch results in XML or JSON formats
     *
     * @param responseTypeIsXml
     *            Mandatory - A boolean to indicate whether the response
     *            required is in XML or JSON
     * @param searchText
     *            Mandatory - The input to be searched
     * @param originLatitude
     *            Mandatory - The latitude of the location
     * @param originLongitude
     *            Mandatory - The longitude of the location
     * @param searchRadius
     *            Optional - The radius from the given co-ordinates in which to
     *            search for results.
     * @param searchRadiusUnit
     *            Optional - Unit used for searchRadius
     * @param maxCandidates
     *            Optional - Maximum number of results to return
     */

    public static void geoSearch(boolean responseTypeIsXml, String searchText, String country,
        String originLatitude, String originLongitude, String searchRadius,
        String searchRadiusUnit, String maxCandidates, String ipAddress, String autoDetectLocation, String areaName1, String areaName3, String postCode,
        String searchScope, String matchOnAddressNumber, String returnAdminAreasOnly, String includeRangesDetails) {
        String apiUrl = LOCATION_INTELLIGENCE_API_URL;

        apiUrl += "?searchText=" + searchText;

        if(country != null ){

            apiUrl += "&country=" + country;
        }

        if(originLatitude != null &&  originLongitude != null){

            apiUrl += "&latitude=" + originLatitude;

            apiUrl += "&longitude=" + originLongitude;
        }


        if (searchRadius != null) {
            apiUrl += "&searchRadius=" + searchRadius;
        }

        if (searchRadiusUnit != null) {
            apiUrl += "&searchRadiusUnit=" + searchRadiusUnit;
        }

        if (maxCandidates != null) {
            apiUrl += "&maxCandidates=" + maxCandidates;
        }

        if (ipAddress != null) {
            apiUrl += "&ipAddress=" + ipAddress;
        }

        if (autoDetectLocation != null) {
            apiUrl += "&autoDetectLocation=" + autoDetectLocation;
        }

        if (areaName1 != null) {
            apiUrl += "&areaName1=" + areaName1;
        }

        if (areaName3 != null) {
            try {
                apiUrl += "&areaName3=" + URLEncoder.encode(areaName3,"UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        if (postCode != null) {
            apiUrl += "&postCode=" + postCode;
        }

        if (searchScope != null) {
            apiUrl += "&searchScope=" + searchScope;
        }
        if (matchOnAddressNumber != null) {
            apiUrl += "&matchOnAddressNumber=" + matchOnAddressNumber;
        }
        if (returnAdminAreasOnly != null) {
            apiUrl += "&returnAdminAreasOnly=" + returnAdminAreasOnly;
        }
        if (includeRangesDetails != null) {
            apiUrl += "&includeRangesDetails=" + includeRangesDetails;
        }


        processRequest(responseTypeIsXml, apiUrl);
    }
}
