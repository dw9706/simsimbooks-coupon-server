package org.simsimbooks.couponserver.category.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Entity
@Table(name = "categories")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Category {

    private Category(String name, Category parent) {
        this.name = name;
        this.parent = parent;
    }

    public static Category createCategory(String name, Category parent) {
        return new Category(name, parent);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;

    @Column(name = "category_name")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent")
    private List<Category> children = new ArrayList<>();


    // update 메서드
    public void changeName(String name) {
        Optional.ofNullable(name).ifPresent(n -> this.name = n);
    }

    public void changeParent(Category parent) {
        //현재 parent와 변경하는 parent가 같으면 바로 리턴
        if (Objects.equals(this.parent, parent)) {
            return;
        }
        this.parent = parent;

        parent.getChildren().add(this);
    }


}
