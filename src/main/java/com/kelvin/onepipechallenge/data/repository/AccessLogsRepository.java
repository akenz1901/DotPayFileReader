package com.kelvin.onepipechallenge.data.repository;

import com.kelvin.onepipechallenge.data.model.Log;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AccessLogsRepository extends JpaRepository<Log,Long> {
}
