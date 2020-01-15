package entity;

import lombok.Data;

import javax.persistence.*;

/**
 * @author wb
 * @date 2020/1/14
 */
@Entity
@Table(name = "user")
@Data
public class User {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    @Transient
    private Boolean test;
    @ManyToOne
    @JoinColumn(name = "id")
    private Class clz;
}
