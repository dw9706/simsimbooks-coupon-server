package org.simsimbooks.couponserver.category.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter
@Getter
public class CategoryRequestDto {
    private String name;
    private Long parent;
}
