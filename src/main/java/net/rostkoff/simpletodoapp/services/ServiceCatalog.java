package net.rostkoff.simpletodoapp.services;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Getter
@RequiredArgsConstructor
@Component
public class ServiceCatalog {
    private final CategoryService category;
    private final TaskService task;
}
