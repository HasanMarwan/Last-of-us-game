package com.intercom.game.services;

import java.io.IOException;
import java.util.ArrayList;

import org.springframework.stereotype.Service;

import com.intercom.game.dto.CellResponse;
import com.intercom.game.dto.CharacterResponse;
import com.intercom.game.dto.HeroResponse;
import com.intercom.game.dto.ZombieResponse;

import engine.Game;
import exceptions.InvalidTargetException;
import exceptions.MovementException;
import exceptions.NoAvailableResourcesException;
import exceptions.NotEnoughActionsException;
import model.characters.Direction;
import model.characters.Hero;
import model.characters.Zombie;
import model.collectibles.Vaccine;
import model.world.Cell;
import model.world.CharacterCell;
import model.world.CollectibleCell;
import model.world.TrapCell;

@Service
public class GameService {

    /////Helpers/////

    // mapping entity character attributes using its corresponding model character attributes except the target
    public void setEntityCharacter(CharacterResponse h1, model.characters.Character h) {
        h1.setAttackDmg(h.getAttackDmg());
        h1.setCurrentHp(h.getCurrentHp());
        h1.setLocation(h.getLocation());
        h1.setMaxHp(h.getMaxHp());
        h1.setName(h.getName());
        // setEntityTarget(h1, h);
    }

    // same as setEntityCharacter with adding hero only attributes
    public void setEntityHero(HeroResponse h1, model.characters.Hero h) {
        setEntityCharacter(h1, h);
        h1.setActionsAvailable(h.getActionsAvailable());
        h1.setMaxActions(h.getMaxActions());
        h1.setSupplyInventory(h.getSupplyInventory().size());
        h1.setVaccineInventory(h.getVaccineInventory().size());
    }

    // set entity target
    public void setEntityTarget(CharacterResponse h1, model.characters.Character h) {
        CharacterResponse target;
        if (h.getTarget() instanceof Hero) {
            target = new HeroResponse();
            setEntityHero((HeroResponse) target, (Hero) h.getTarget());
        } else if (h.getTarget() instanceof Zombie) {
            target = new ZombieResponse();
            setEntityCharacter(target, h.getTarget());
        } else {
            target = null;
        }

        h1.setTarget(target);
    }

    ///////////////////End of helpers//////////////////////

    public HeroResponse findAvailableHeroesByName(String name) {
        for (Hero h : Game.availableHeroes) {
            if (h.getName().equalsIgnoreCase(name)) {
                HeroResponse h1 = new HeroResponse();
                setEntityHero(h1, h);
                setEntityTarget(h1, h);
                return h1;
            }
        }
        return null;
    }

    public HeroResponse findInGameHeroesByName(String name) {
        for (Hero h : Game.heroes) {
            if (h.getName().equalsIgnoreCase(name)) {
                HeroResponse h1 = new HeroResponse();
                setEntityHero(h1, h);
                setEntityTarget(h1, h);
                return h1;
            }
        }
        return null;
    }

    public ZombieResponse findAvailableZombieByName(String name) {
        for (Zombie z : Game.zombies) {
            if (z.getName().equalsIgnoreCase(name)) {
                ZombieResponse z1 = new ZombieResponse();
                setEntityCharacter(z1, z);
                setEntityTarget(z1, z);
                return z1;
            }
        }
        return null;
    }

    public ArrayList<HeroResponse> findAvailableHeroes() {
        ArrayList<HeroResponse> arr = new ArrayList();
        for (Hero h : Game.availableHeroes) {
            HeroResponse h1 = new HeroResponse();
            setEntityHero(h1, h);
            setEntityTarget(h1, h);
            arr.add(h1);
        }
        return arr;
    }

    public ArrayList<HeroResponse> findInGameHeroes() {
        ArrayList<HeroResponse> arr = new ArrayList();
        for (Hero h : Game.heroes) {
            HeroResponse h1 = new HeroResponse();
            setEntityHero(h1, h);
            setEntityTarget(h1, h);
            arr.add(h1);
        }
        return arr;
    }

    public ArrayList<ZombieResponse> findAvailableZombies() {
        ArrayList<ZombieResponse> arr = new ArrayList();
        for (Zombie z : Game.zombies) {
            ZombieResponse z1 = new ZombieResponse();
            setEntityCharacter(z1, z);
            setEntityTarget(z1, z);
            arr.add(z1);
        }
        return arr;
    }

    public void moveHero(String name, Direction d) throws NotEnoughActionsException, MovementException, NullPointerException {
        for (Hero h : Game.heroes) {
            if (name.equalsIgnoreCase(h.getName())) {
                h.move(d);
                return;
            }
        }
        throw new NullPointerException("hero not in game yet");
    }

    public void attackHero(String name) throws NotEnoughActionsException, InvalidTargetException {
        for (Hero h : Game.heroes) {
            if (name.equalsIgnoreCase(h.getName())) {
                h.attack();
                return;
            }
        }
        throw new NullPointerException("hero not in game yet");
    }

    public void cureHero(String name) throws NotEnoughActionsException, NoAvailableResourcesException, InvalidTargetException {
        for (Hero h : Game.heroes) {
            if (name.equalsIgnoreCase(h.getName())) {
                h.cure();
                return;
            }
        }
        throw new NullPointerException("hero not in game yet");
    }

    public void useSpecialHero(String name) throws NoAvailableResourcesException, InvalidTargetException {
        for (Hero h : Game.heroes) {
            if (name.equalsIgnoreCase(h.getName())) {
                h.useSpecial();
                return;
            }
        }
        throw new NullPointerException("hero not in game yet");
    }

    public void setTargetHero(String name, int x,int y) {
        for (Hero h : Game.heroes) {
            if (name.equalsIgnoreCase(h.getName())) {
                Cell cell = Game.map[x][y];
                if (cell instanceof CharacterCell) {
                    h.setTarget(((CharacterCell) cell).getCharacter());
                }
            }
        }
        throw new NullPointerException("hero not in game yet");
    }

    public void startNewGame(String name) {
        for (Hero h : Game.availableHeroes) {
            if (name.equalsIgnoreCase(h.getName())) {
                Game.startGame(h);
                return;
            }
        }
        throw new NullPointerException("hero not in game yet");
    }

    public void loadHeroesCSV() throws IOException {
        Game.loadHeroes("Heroes.csv");
    }

    public CellResponse[][] getMap() {
        CellResponse[][] mapRequest = new CellResponse[15][15];

        for (int i = 0; i < Game.map.length; i++) {
            for (int j = 0; j < Game.map.length; j++) {
                mapRequest[i][j] = new CellResponse();
                Cell c = Game.map[i][j];

                if (c.isVisible()) {
                    mapRequest[i][j].setVisible(true);
                }
                if (c instanceof TrapCell) {
                    mapRequest[i][j].setType("trap");
                    mapRequest[i][j].setTrapDamage(((TrapCell) c).getTrapDamage());
                }
                if (c instanceof CollectibleCell) {
                    if (((CollectibleCell) c).getCollectible() instanceof Vaccine) {
                        mapRequest[i][j].setType("vaccine");
                    } else {
                        mapRequest[i][j].setType("supply");
                    }
                }
                if (c instanceof CharacterCell) {
                    if (((CharacterCell) c).getCharacter() instanceof Hero) {
                        mapRequest[i][j].setType("hero");
                        mapRequest[i][j].setName(((CharacterCell) c).getCharacter().getName());
                    }
                    if (((CharacterCell) c).getCharacter() instanceof Zombie) {
                        mapRequest[i][j].setType("zombie");
                        mapRequest[i][j].setName(((CharacterCell) c).getCharacter().getName());
                    }

                }
            }
        }
        return mapRequest;
    }
}
