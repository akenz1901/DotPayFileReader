package com.dot.dotpay.data.repository;

import com.dot.dotpay.data.model.UserAccessLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;


@Repository
public interface AccessLogsRepository extends JpaRepository<UserAccessLog,Long> {

    @Query("SELECT everthing from user_access_log AS everthing WHERE everthing.ip=?1")
    List<UserAccessLog> findRequestMadeByAnIpNumber(String ipNumber);

    @Query(value="SELECT Db.ip as ip FROM user_access_log AS Db WHERE Db.startDate BETWEEN ?1 AND ?2 GROUP BY Db.ip HAVING count(Db.ip) >= ?3", nativeQuery = true)
    List<UserAccessLog> findByStartDateIsBetweenAndGreaterThanThreshold(LocalDateTime startDate, LocalDateTime dateTime, Long threshold);
}
