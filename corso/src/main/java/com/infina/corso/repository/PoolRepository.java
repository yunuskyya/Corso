package com.infina.corso.repository;

import com.infina.corso.model.Pool;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PoolRepository extends JpaRepository<Pool, Integer> {

    Pool findById(Long id);

    Pool deleteById(Long id);
}
