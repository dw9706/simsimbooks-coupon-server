package org.simsimbooks.couponserver.category;

import org.simsimbooks.couponserver.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category,Long> {
}
