package com.init_spring_bean_mvn.demo.emsbackend.repository;

import com.init_spring_bean_mvn.demo.emsbackend.entity.Analyst;
import org.springframework.data.jpa.repository.JpaRepository;

// we don't need to annotate with #repository
// all methods are transactional and don't need to use it in our #Reposilotry
public interface AnalystRepository extends JpaRepository<Analyst, Long> {

    // jpaRepository extends listCrudRepository which extends CrudRepository
    // jpaRepository implements SimpleJpaRespository and implements JpaResposiotrory Implementation and its methods




}
