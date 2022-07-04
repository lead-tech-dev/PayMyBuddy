package com.mjtech.mybuddy.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * The Bank class implements a bank
 * entity.
 */

@lombok.Generated
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "bank")
@Entity
public class Bank {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  private String iban;

  private double balance;

  private String description;

  private String bankAccountName;

  @ManyToOne
  @OnDelete(action = OnDeleteAction.CASCADE)
  @JoinColumn(name = "user_id")
  private Users user;
}
