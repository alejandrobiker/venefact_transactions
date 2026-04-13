package io.venefact.venefact.category.rest;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.venefact.venefact.category.model.CategoryDTO;
import io.venefact.venefact.category.service.CategoryService;
import io.venefact.venefact.util.ResponseAPI;
import jakarta.validation.Valid;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/api/categories", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Categoria", description = "Se utiliza para agrupar gastos bajo un mismo propósito (ej: Salud, Alimentación, Deudas)")
public class CategoryResource {

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
        return ResponseEntity.ok(categoryService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> getCategory(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(categoryService.getById(id));
    }

    @PostMapping
    public ResponseEntity<ResponseAPI<CategoryDTO>> createCategory(@RequestBody @Valid final CategoryDTO categoryDTO) {
        CategoryDTO categoryCreated = categoryService.create(categoryDTO);
        return new ResponseEntity<>(new ResponseAPI<>("Categoría creada", categoryCreated), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseAPI<CategoryDTO>> updateCategory(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final CategoryDTO categoryDTO) {
        CategoryDTO categoryUpdated = categoryService.update(id, categoryDTO);
        return new ResponseEntity<>(new ResponseAPI<>("Categoría actualizada", categoryUpdated), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<ResponseAPI<Boolean>> deleteCategory(@PathVariable(name = "id") final Long id) {
        categoryService.delete(id);
        return new ResponseEntity<>(new ResponseAPI<>("Categoría eliminada", true), HttpStatus.OK);
    }

}
