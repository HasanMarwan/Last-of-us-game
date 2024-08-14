package com.intercom.game.controller;

import java.awt.Point;
import java.io.IOException;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.intercom.game.dto.ActionRequest;
import com.intercom.game.dto.CellResponse;
import com.intercom.game.dto.HeroResponse;
import com.intercom.game.dto.MoveRequest;
import com.intercom.game.dto.SetTargetRequest;
import com.intercom.game.dto.ZombieResponse;
import com.intercom.game.services.GameService;

import engine.Game;
import exceptions.InvalidTargetException;
import exceptions.MovementException;
import exceptions.NoAvailableResourcesException;
import exceptions.NotEnoughActionsException;
import model.characters.Direction;

@RestController
@RequestMapping("/")
public class GameController {

    @Autowired
    private GameService gameService;

    @GetMapping("/heroes")
    private ResponseEntity<ArrayList<HeroResponse>> findAllHeroes() {
        ArrayList<HeroResponse> h = gameService.findAvailableHeroes();
        if (h == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(h);
    }

    @GetMapping("/ingame-heroes")
    private ResponseEntity<ArrayList<HeroResponse>> findAllInGameHeroes() {
        ArrayList<HeroResponse> h = gameService.findInGameHeroes();
        if (h == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(h);
    }

    @GetMapping("/zombies")
    private ResponseEntity<ArrayList<ZombieResponse>> findAllZombies() {
        ArrayList<ZombieResponse> z = gameService.findAvailableZombies();
        if (z == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(z);
    }

    @GetMapping("/heroes/{heroName}")
    private ResponseEntity<HeroResponse> findHeroByName(@PathVariable("heroName") String name) {
        HeroResponse h = gameService.findAvailableHeroesByName(name);
        if (h == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(h);
    }

    @GetMapping("/ingame-heroes/{heroName}")
    private ResponseEntity<HeroResponse> findInGameHeroByName(@PathVariable("heroName") String name) {
        HeroResponse h = gameService.findInGameHeroesByName(name);
        if (h == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(h);
    }

    @GetMapping("/zombies/{zombieName}")
    private ResponseEntity<ZombieResponse> findZombieByName(@PathVariable("zombieName") String name) {
        ZombieResponse z = gameService.findAvailableZombieByName(name);
        if (z == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(z);
    }

    @GetMapping("/map")
    private ResponseEntity<CellResponse[][]> getGridMap() {
        CellResponse[][] map = gameService.getMap();
        if (map == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(map);
    }

    @PostMapping("/move")
    public ResponseEntity<Void> reqMove(@RequestBody MoveRequest moveRequest) {
        String name = moveRequest.getName();
        Direction direction = moveRequest.getDirection();
        try {
            gameService.moveHero(name, direction);
            return ResponseEntity.ok().build();
        } catch (NotEnoughActionsException | MovementException | NullPointerException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/attack")
    public ResponseEntity<Void> reqAttack(@RequestBody ActionRequest actionRequest) {
        String name = actionRequest.getName();
        try {
            gameService.attackHero(name);
            return ResponseEntity.ok().build();
        } catch (NotEnoughActionsException | InvalidTargetException | NullPointerException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/cure")
    public ResponseEntity<Void> reqCure(@RequestBody ActionRequest actionRequest) {
        String name = actionRequest.getName();
        try {
            gameService.cureHero(name);
            return ResponseEntity.ok().build();
        } catch (NotEnoughActionsException | NoAvailableResourcesException | InvalidTargetException | NullPointerException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/use-special")
    public ResponseEntity<Void> reqUseSpecial(@RequestBody ActionRequest actionRequest) {
        String name = actionRequest.getName();
        try {
            gameService.useSpecialHero(name);
            return ResponseEntity.ok().build();
        } catch (NoAvailableResourcesException | InvalidTargetException | NullPointerException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/set-target")
    public ResponseEntity<Void> reqSetTarget(@RequestBody SetTargetRequest setTargetRequest) {
        String name = setTargetRequest.getName();
        Point p = setTargetRequest.getTargetPoint();
        try {
            gameService.setTargetHero(name, p);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/load-heroes")
    public ResponseEntity<Void> reqLoadHeroes() {
        try {
            gameService.loadHeroesCSV();
            return ResponseEntity.ok().build();
        } catch (IOException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/end-turn")
    public ResponseEntity<Void> reqEndTurn() {
        try {
            Game.endTurn();
            return ResponseEntity.ok().build();
        } catch (NotEnoughActionsException | InvalidTargetException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/start-game")
    public ResponseEntity<Void> reqStartGame(@RequestBody ActionRequest actionRequest) {
        String name = actionRequest.getName();
        try {
            gameService.startNewGame(name);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
