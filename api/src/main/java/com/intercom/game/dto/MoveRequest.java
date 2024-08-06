package com.intercom.game.dto;

import lombok.Getter;
import lombok.Setter;
import model.characters.Direction;

@Getter
@Setter
public class MoveRequest {

    private String name;
    private Direction direction;
}
