package org.samydevup.blogrestapi.service.impl;

import org.modelmapper.ModelMapper;
import org.samydevup.blogrestapi.entity.Category;
import org.samydevup.blogrestapi.exception.ResourceNotFoundException;
import org.samydevup.blogrestapi.payload.CategoryDto;
import org.samydevup.blogrestapi.repository.CategoryRepository;
import org.samydevup.blogrestapi.service.CategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    private CategoryRepository categoryRepository;

    private ModelMapper modelMapper;

    private Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);

    public CategoryServiceImpl(CategoryRepository categoryRepository, ModelMapper modelMapper) {
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public CategoryDto addCategory(CategoryDto categoryDto) {
        Category category = modelMapper.map(categoryDto,Category.class);
        Category savedCategory = categoryRepository.save(category);
        return  modelMapper.map(savedCategory, CategoryDto.class);
    }

    @Override
    public CategoryDto getCategory(Long categoryId) {
        Category category =  categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category","id",categoryId));
        return modelMapper.map(category, CategoryDto.class);
    }

    @Override
    public List<CategoryDto> getAllCategories() {
        logger.info("OOOOOOK OOOOOH");
        List<Category> categories = categoryRepository.findAll();
        List<CategoryDto> categoryDtos = categories.stream().map(category->modelMapper.map(category, CategoryDto.class)).collect(Collectors.toList());
        return  categoryDtos;
    }

    @Override
    public CategoryDto updateCategory(CategoryDto categoryDto, Long id) {
        //1-recup la categorie a partir de son id
        Category category = categoryRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Categorie","id",id));
        //2-mis a jours de l'objet retourné avec les nouvelles données
        category.setName(categoryDto.getName());
        category.setDescription(categoryDto.getDescription());
        category = categoryRepository.save(category);
        return modelMapper.map(category,CategoryDto.class);
    }

    @Override
    public void deleteCategory(Long id) {
        //1-verifier si la category existe en bd
        Category category = categoryRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Categorie","id",id));
        //2-si oui la supprimer
        categoryRepository.delete(category);
    }
}
