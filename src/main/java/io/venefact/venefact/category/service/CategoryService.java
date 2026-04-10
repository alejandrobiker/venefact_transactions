package io.venefact.venefact.category.service;

import io.venefact.venefact.category.domain.Category;
import io.venefact.venefact.category.model.CategoryDTO;
import io.venefact.venefact.category.repos.CategoryRepository;
import io.venefact.venefact.events.BeforeDeleteCategory;
import io.venefact.venefact.util.NotFoundException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ApplicationEventPublisher publisher;

    public List<CategoryDTO> findAll() {
        final List<Category> categories = categoryRepository.findAll(Sort.by("id").ascending());
        return categories.stream()
                .map(this::mapToDTO)
                .toList();
    }

    public CategoryDTO getById(final Long id) {
        return categoryRepository.findById(id)
                .map(this::mapToDTO)
                .orElseThrow(NotFoundException::new);
    }

    public Category create(final CategoryDTO categoryDTO) {
        final Category category = new Category();
        category.setName(categoryDTO.getName());
        return categoryRepository.save(category);
    }

    public Category update(final Long id, final CategoryDTO categoryDTO) {
        final Category category = categoryRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        category.setName(categoryDTO.getName());
        return categoryRepository.save(category);
    }

    public void delete(final Long id) {
        final Category category = categoryRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        publisher.publishEvent(new BeforeDeleteCategory(id));
        categoryRepository.delete(category);
    }

    private CategoryDTO mapToDTO(final Category category) {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(category.getId());
        categoryDTO.setName(category.getName());
        return categoryDTO;
    }

    public boolean nameExists(final String name) {
        return categoryRepository.existsByNameIgnoreCase(name);
    }

}
