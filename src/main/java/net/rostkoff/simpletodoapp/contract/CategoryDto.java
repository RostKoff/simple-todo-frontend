package net.rostkoff.simpletodoapp.contract;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryDto {
    private Long id;
    private String title;
    private String colour;
}
