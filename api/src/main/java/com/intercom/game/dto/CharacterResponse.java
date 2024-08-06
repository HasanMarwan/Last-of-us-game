package com.intercom.game.dto;

import java.awt.Point;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class CharacterResponse {

    private String name;
    private int maxHp;
    private int currentHp;
    private Point location;
    private int attackDmg;
    private CharacterResponse target;
}
