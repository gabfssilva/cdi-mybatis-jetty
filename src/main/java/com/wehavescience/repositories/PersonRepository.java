package com.wehavescience.repositories;

import com.wehavescience.entities.Person;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author Gabriel Francisco  <gabfssilva@gmail.com>
 */
public interface PersonRepository  {
    @Select("SELECT * FROM people WHERE age = #{age}") @Options(useCache = true) @Many
    List<Person> findAllByAge(Integer age);

    @Insert("INSERT INTO people (name, age, email, information) values(#{name}, #{age}, #{email}, #{information})")
    @Options(useGeneratedKeys = true)
    void save(Person person);

    @Select("SELECT COUNT(id) FROM people") @One
    Long count();

    @Select("SELECT * FROM people WHERE id = #{id}") @One
    Person fetch(Long id);
}
