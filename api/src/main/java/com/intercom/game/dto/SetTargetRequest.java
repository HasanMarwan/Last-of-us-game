package com.intercom.game.dto;

import java.awt.Point;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SetTargetRequest {

    String name;
    Point targetPoint;
}
