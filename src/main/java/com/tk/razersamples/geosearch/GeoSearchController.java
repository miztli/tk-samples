package com.tk.razersamples.geosearch;

import java.util.Map;
import javax.annotation.Resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController(value = "/search")
public class GeoSearchController
{

    @Resource
    private GeoSearchService geoSearchService;

    @GetMapping
    public Map<String, Object> search(@RequestParam(name = "search") final String search) {
        return geoSearchService.testGeoSearch(search);
    }
}
