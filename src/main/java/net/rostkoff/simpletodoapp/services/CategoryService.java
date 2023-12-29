package net.rostkoff.simpletodoapp.services;

import net.rostkoff.simpletodoapp.contract.CategoryDto;
import net.rostkoff.simpletodoapp.exceptions.categories.CategoryNotFound;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
public class CategoryService {
    RestClient restClient;
    private final String API_URL = "http://localhost:8081";

    public CategoryService() {
        restClient = RestClient.create();
    }

    public List<CategoryDto> getAllCategories() {
        List<CategoryDto> categories = restClient.get()
                .uri(API_URL + "/categories/all")
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
        if(categories == null || categories.isEmpty())
            throw new CategoryNotFound();

        return categories;
    }
}
