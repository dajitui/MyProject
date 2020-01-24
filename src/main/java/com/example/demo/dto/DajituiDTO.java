package com.example.demo.dto;

import com.example.demo.annotation.MyField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Field;

/**
 * @author M
 */
@AllArgsConstructor
@Data
@NoArgsConstructor
public class DajituiDTO {

    private String key;

    @MyField
    private String name;

    private Integer age;

    public static void main(String[] args) {
        // 获取类模板
        Class c = DajituiDTO.class;
        // 获取所有字段
        for (Field f : c.getDeclaredFields()) {
            // 判断这个字段是否有MyField注解
            if (f.isAnnotationPresent(MyField.class)) {
                MyField annotation = f.getAnnotation(MyField.class);
                System.out.println("字段:[" + f.getName() + "]");
            }
        }
    }
}
