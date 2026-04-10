package io.venefact.venefact.category.service;

import io.venefact.venefact.category.domain.Category;
import io.venefact.venefact.category.model.CategoryDTO;
import io.venefact.venefact.category.repos.CategoryRepository;
import io.venefact.venefact.events.BeforeDeleteCategory;
import io.venefact.venefact.util.NotFoundException;
import java.util.List;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ApplicationEventPublisher publisher;

    public CategoryService(final CategoryRepository categoryRepository,
            final ApplicationEventPublisher publisher) {
        this.categoryRepository = categoryRepository;
        this.publisher = publisher;
    }

    public List<CategoryDTO> findAll() {
        final List<Category> categories = categoryRepository.findAll(Sort.by("id"));
        return categories.stream()
                .map(category -> mapToDTO(category, new CategoryDTO()))
                .toList();
    }

    public CategoryDTO get(final Long id) {
        return categoryRepository.findById(id)
                .map(category -> mapToDTO(category, new CategoryDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final CategoryDTO categoryDTO) {
        final Category category = new Category();
        mapToEntity(categoryDTO, category);
        return categoryRepository.save(category).getId();
    }

    public void update(final Long id, final CategoryDTO categoryDTO) {
        final Category category = categoryRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(categoryDTO, category);
        categoryRepository.save(category);
    }

    public void delete(final Long id) {
        final Category category = categoryRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        publisher.publishEvent(new BeforeDeleteCategory(id));
        categoryRepository.delete(category);
    }

    private CategoryDTO mapToDTO(final Category category, final CategoryDTO categoryDTO) {
        categoryDTO.setId(category.getId());
        categoryDTO.setName(category.getName());
        return categoryDTO;
    }

    private Category mapToEntity(final CategoryDTO categoryDTO, final Category category) {
        category.setName(categoryDTO.getName());
        return category;
    }

    public boolean nameExists(final String name) {
        return categoryRepository.existsByNameIgnoreCase(name);
    }

}
