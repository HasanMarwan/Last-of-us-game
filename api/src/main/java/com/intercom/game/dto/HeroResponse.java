package com.intercom.game.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HeroResponse extends CharacterResponse {

    private int actionsAvailable;
    private int maxActions;
    private boolean specialAction;
    private int vaccineInventory;
    private int supplyInventory;
}
