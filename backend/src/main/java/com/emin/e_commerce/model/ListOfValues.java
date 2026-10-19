package com.emin.e_commerce.model;


import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "list_of_values")
@SQLDelete(sql = "UPDATE list_of_values SET deleted_datetime = CURRENT_TIMESTAMP WHERE id = ?")
@SQLRestriction("deleted_datetime IS NULL")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ListOfValues extends BaseEntity{
    private String code;
    private String value;
    private String type;
}
