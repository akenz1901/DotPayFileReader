package com.dot.dotpay.data.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "blocked_ip")
public class BlockedIP {

    @Id
    private Long id;

    private String ip;
    private Long requestNumber;
    private String comment;

}
