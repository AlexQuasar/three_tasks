package com.alexquasar.threeTasks.thirdTask.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(schema = "public", name = "url")
@NoArgsConstructor
@Getter
@Setter
public class Url {

    public Url(String link) {
        this.link = link;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "link")
    private String link;
}
