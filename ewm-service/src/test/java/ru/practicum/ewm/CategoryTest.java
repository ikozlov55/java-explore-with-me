package ru.practicum.ewm;

import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import net.datafaker.Faker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.testutils.EwmServiceApi;
import ru.practicum.ewm.testutils.TestData;
import ru.practicum.ewm.user.dto.UserDto;

import java.util.Locale;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureEmbeddedDatabase
@Import({EwmServiceApi.class, TestData.class})
public class CategoryTest {
    @Autowired
    private EwmServiceApi serviceApi;
    @Autowired
    private TestData testData;
    private final Faker faker = new Faker(Locale.of("RU"));

    @Test
    void createCategory() throws Exception {
        CategoryDto categoryDto = new CategoryDto(null, faker.word().noun());

        serviceApi.createCategory(categoryDto)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(greaterThan(0)))
                .andExpect(jsonPath("$.name").value(categoryDto.name()));
    }

    @Test
    void categoryNameIsRequired() throws Exception {
        CategoryDto categoryDto = new CategoryDto(null, null);

        serviceApi.createCategory(categoryDto)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value(startsWith("Field: name. Error: must not be blank")));
    }

    @Test
    void categoryNameMustBeUnique() throws Exception {
        CategoryDto categoryDto = testData.randomCategory();

        serviceApi.createCategory(categoryDto)
                .andExpect(status().isConflict());
    }

    @Test
    void deleteCategory() throws Exception {
        CategoryDto categoryDto = testData.randomCategory();

        serviceApi.deleteCategory(categoryDto.id())
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteCategoryNotFound() throws Exception {
        long categoryId = 9999;
        String message = String.format("Category with id=%d was not found", categoryId);

        serviceApi.deleteCategory(categoryId)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(message));
    }

    @Test
    void categoryWithBoundEventsCantBeDeleted() throws Exception {
        CategoryDto category = testData.randomCategory();
        UserDto user = testData.randomUser();
        testData.randomEvent(user, category);

        serviceApi.deleteCategory(category.id())
                .andExpect(status().isConflict());
    }

    @Test
    void updateCategory() throws Exception {
        CategoryDto categoryDto = testData.randomCategory();
        CategoryDto newCategoryDto = new CategoryDto(categoryDto.id(), faker.word().noun());

        serviceApi.updateCategory(categoryDto.id(), newCategoryDto)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(categoryDto.id()))
                .andExpect(jsonPath("$.name").value(newCategoryDto.name()));
    }

    @Test
    void updatedCategoryNameMustBeUnique() throws Exception {
        CategoryDto categoryDto1 = testData.randomCategory();
        CategoryDto categoryDto2 = testData.randomCategory();
        CategoryDto categoryDto2Update = new CategoryDto(categoryDto2.id(), categoryDto1.name());

        serviceApi.updateCategory(categoryDto2.id(), categoryDto2Update)
                .andExpect(status().isConflict());
    }

    @Test
    void updateCategoryNotFound() throws Exception {
        long categoryId = 9999;
        String message = String.format("Category with id=%d was not found", categoryId);
        CategoryDto categoryDto = new CategoryDto(categoryId, faker.word().noun());

        serviceApi.updateCategory(categoryDto.id(), categoryDto)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(message));
    }

    @Test
    void getCategory() throws Exception {
        CategoryDto category = testData.randomCategory();

        serviceApi.getCategory(category.id())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(category.id()))
                .andExpect(jsonPath("$.name").value(category.name()));
    }

    @Test
    void getCategoryNotFound() throws Exception {
        long categoryId = 9999;

        String message = String.format("Category with id=%d was not found", categoryId);
        serviceApi.getCategory(categoryId)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(message));
    }


    @Test
    void getCategories() throws Exception {
        int categoriesCount = 10;
        for (int i = 0; i < categoriesCount; i++) {
            testData.randomCategory();
        }
        serviceApi.getCategories(0, categoriesCount)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(categoriesCount)));
    }

}
