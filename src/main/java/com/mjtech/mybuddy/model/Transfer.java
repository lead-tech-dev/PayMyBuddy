package com.mjtech.mybuddy.model;

import com.mjtech.mybuddy.enumeration.TransferType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;

/**
 * The Transfer class implements a transfer
 * entity.
 */

@lombok.Generated
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "transfer")
@Entity
public class Transfer {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  private String description;

  private double amount;

  private double commission;

  private Date transferDate;

  @Enumerated(EnumType.STRING)
  private TransferType type;

  @ManyToOne
  @OnDelete(action = OnDeleteAction.CASCADE)
  private Users debtor;

  @ManyToOne
  @OnDelete(action = OnDeleteAction.CASCADE)
  private Users credit;


  @ManyToOne(fetch = FetchType.EAGER)
  @OnDelete(action = OnDeleteAction.CASCADE)
  @JoinColumn(name = "connection_id")
  private Connection connection;

}
