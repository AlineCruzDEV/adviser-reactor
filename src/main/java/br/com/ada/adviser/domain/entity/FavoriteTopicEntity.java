package br.com.ada.adviser.domain.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.ArrayList;
import java.util.List;

@Data
@Table("topics")
public class FavoriteTopicEntity {
    @Id
    @Column("id")
    private Long id;

    @Column("name")
    private String name;
}