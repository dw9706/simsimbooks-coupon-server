package org.simsimbooks.couponserver.category.mapper;

import org.simsimbooks.couponserver.category.dto.CategoryRequestDto;
import org.simsimbooks.couponserver.category.dto.CategoryResponseDto;
import org.simsimbooks.couponserver.category.entity.Category;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class CategoryMapper {
    public static CategoryResponseDto toResponse(Category category) {
        List<Long> children = new ArrayList<>();

        // 카테고리 엔티티의 자식 카테고리를 모두 조회하며 id를 List에 추가
        for (Category child : category.getChildren()) {
            children.add(child.getId());
        }


        return CategoryResponseDto.builder()
                .id(category.getId())
                .name(category.getName())
                .parent(Objects.isNull(category.getParent()) ? null : category.getParent().getId())
                .children(children)
                .build();
    }

    public static Category toCategory(CategoryRequestDto requestDto, Category parent) {
        return Category.createCategory(
                requestDto.getName(),
                parent
        );
    }


}
