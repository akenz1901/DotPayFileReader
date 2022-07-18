package com.dot.dotpay.data.repository;

import com.dot.dotpay.data.model.BlockedIP;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlockedIpRepository extends JpaRepository<BlockedIP, Long> {
}
