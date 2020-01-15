package entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

/**
 * @author wb
 * @date 2020/1/14
 */
@Data
@Entity
@Table(name = "class")
public class Class {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    @OneToMany(mappedBy = "clz")
    private List<User> userList;
}
