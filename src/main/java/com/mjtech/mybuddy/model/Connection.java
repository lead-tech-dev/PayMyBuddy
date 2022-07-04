package com.mjtech.mybuddy.model;

import com.mjtech.mybuddy.enumeration.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * The Connection class implements a connection
 * entity.
 */

@lombok.Generated
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "connection")
@Entity
public class Connection {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @ManyToOne
  @OnDelete(action = OnDeleteAction.CASCADE)
  @JoinColumn(name = "sender_id")
  private Users sender;

  @ManyToOne
  @OnDelete(action = OnDeleteAction.CASCADE)
  @JoinColumn(name = "receiver_id")
  private Users receiver;

  @Enumerated(EnumType.STRING)
  private Status status;

  private Date createdAt;

  @OneToMany(mappedBy = "connection", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  private List<Transfer> transfers = new ArrayList<>();

  public Connection(Users sender, Users receiver) {
    this.sender = sender;
    this.receiver = receiver;
    this.status = Status.INACTIVE;
    this.createdAt = new Date();
    this.transfers = new ArrayList<>();
    ;
  }


}
