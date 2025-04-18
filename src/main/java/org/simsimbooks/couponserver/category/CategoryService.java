package org.simsimbooks.couponserver.category;

import lombok.RequiredArgsConstructor;
import org.simsimbooks.couponserver.category.dto.CategoryRequestDto;
import org.simsimbooks.couponserver.category.dto.CategoryResponseDto;
import org.simsimbooks.couponserver.category.entity.Category;
import org.simsimbooks.couponserver.category.mapper.CategoryMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    @Transactional
    public CategoryResponseDto createCategory(CategoryRequestDto requestDto) {
        Category parent = null;

        // 부모 카테고리를 조회 후 있으면 참조하도록, 없으면 null
        if (Objects.nonNull(requestDto.getParent())) {
           parent = categoryRepository.findById(requestDto.getParent()).orElse(null);
        }

        Category save = categoryRepository.save(CategoryMapper.toCategory(requestDto, parent));

        return CategoryMapper.toResponse(save);

    }

    public CategoryResponseDto getCategory(Long categoryId) {
        if (Objects.isNull(categoryId)) {
            throw new IllegalArgumentException("categoryId is null");
        }

        Optional<Category> optionalCategory = categoryRepository.findById(categoryId);

        if (optionalCategory.isEmpty()) {
            throw new NoSuchElementException("id : " + categoryId + "인 category 없습니다.");
        }

        return CategoryMapper.toResponse(optionalCategory.get());
    }

    @Transactional
    public CategoryResponseDto updateCategory(Long categoryId, CategoryRequestDto requestDto) {
        if (Objects.isNull(categoryId)) {
            throw new IllegalArgumentException("categoryId is null");
        }

        Optional<Category> optionalCategory = categoryRepository.findById(categoryId);

        if (optionalCategory.isEmpty()) {
            throw new NoSuchElementException("id : " + categoryId + "인 category 없습니다.");
        }

        Category category = optionalCategory.get();
        category.changeName(requestDto.getName());

        // 변경사항에 부모 카테고리 변경도 있으면 조회 후 변경
        if (Objects.nonNull(requestDto.getParent())) {
            categoryRepository.findById(requestDto.getParent()).ifPresent(category::changeParent);
        }

        return CategoryMapper.toResponse(category);
    }

    @Transactional
    public void deleteCategory(Long categoryId) {
        if (Objects.isNull(categoryId)) {
            throw new IllegalArgumentException("categoryId is null");
        }

        Optional<Category> optionalCategory = categoryRepository.findById(categoryId);

        if (optionalCategory.isEmpty()) {
            throw new NoSuchElementException("id : " + categoryId + "인 category 없습니다.");
        }

        categoryRepository.delete(optionalCategory.get());
    }
}
