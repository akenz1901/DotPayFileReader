package com.kelvin.onepipechallenge.data.repository;

import com.kelvin.onepipechallenge.data.model.Log;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;


@Repository
public interface AccessLogsRepository extends JpaRepository<Log,Long> {

    @Query("SELECT everthing from log_table AS everthing WHERE everthing.ip=?1")
    List<Log> findRequestMadeByAnIpNumber(String ipNumber);

    @Query("SELECT Db.ip as ip FROM log_table AS Db WHERE Db.startDate BETWEEN ?1 AND ?2 GROUP BY Db.ip HAVING count(Db.ip) >= ?3")
    List<String> findByStartDateIsBetweenAndGreaterThanThreshold(LocalDateTime startDate, LocalDateTime dateTime, Long threshold);
}
