package ru.mhelper.models;

import org.springframework.data.jpa.domain.AbstractAuditable;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "procurement")
public class Procurement extends AbstractAuditable <User,Long>{


}