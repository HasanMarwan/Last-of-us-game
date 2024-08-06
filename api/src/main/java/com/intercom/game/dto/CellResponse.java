package com.intercom.game.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CellResponse {

    String type="";
    String name="";
    int trapDamage=0;
    boolean isVisible = false;
}
