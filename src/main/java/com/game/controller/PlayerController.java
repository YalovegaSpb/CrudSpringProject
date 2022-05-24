package com.game.controller;

import com.game.entity.Player;
import com.game.entity.PlayerFilter;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.repository.PlayerRepository;
import com.game.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.DataBinder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;

@Validated
@RestController
@RequestMapping("/rest/players")
public class PlayerController {


    @Autowired
    private PlayerValidator playerValidator;
    @Autowired
    private PlayerUpdateValidator playerUpdateValidator;
    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private PlayerService playerService;

    @GetMapping(
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public List<Player> getPlayers(
            @RequestParam Optional<String> name,
            @RequestParam Optional<String> title,
            @RequestParam Optional<Race> race,
            @RequestParam Optional<Profession> profession,
            @RequestParam Optional<Long> after,
            @RequestParam Optional<Long> before,
            @RequestParam Optional<Boolean> banned,
            @RequestParam Optional<Integer> minExperience,
            @RequestParam Optional<Integer> maxExperience,
            @RequestParam Optional<Integer> minLevel,
            @RequestParam Optional<Integer> maxLevel,
            @RequestParam Optional<PlayerOrder> order,
            @RequestParam Optional<Integer> pageNumber,
            @RequestParam Optional<Integer> pageSize
    ) {

        PlayerFilter filter = new PlayerFilter();

        if (name.isPresent()) filter.setName(name.get());
        if (title.isPresent()) filter.setTitle(title.get());
        if (race.isPresent()) filter.setRace(race.get());
        if (profession.isPresent()) filter.setProfession(profession.get());
        if (after.isPresent()) filter.setAfter(after.get());
        if (before.isPresent()) filter.setBefore(before.get());
        if (banned.isPresent()) filter.setBanned(banned.get());
        if (minExperience.isPresent()) filter.setMinExperience(minExperience.get());
        if (maxExperience.isPresent()) filter.setMaxExperience(maxExperience.get());
        if (minLevel.isPresent()) filter.setMinLevel(minLevel.get());
        if (maxLevel.isPresent()) filter.setMaxLevel(maxLevel.get());


        PlayerOrder playerOrder;

        if (order.isPresent()) {
            playerOrder = order.get();
        } else {
            playerOrder = PlayerOrder.ID;
        }

        int pNumber = 0;
        int pSize = 3;

        if (pageNumber.isPresent()) {
            pNumber = pageNumber.get();
        }

        if (pageSize.isPresent()) {
            pSize = pageSize.get();
        }

        PageRequest page = PageRequest.of(pNumber, pSize, Sort.by(playerOrder.getFieldName()));

        return playerService.getPlayers(filter, page).getContent();
    }

    @GetMapping(value = "/count",
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public Long getPlayersCount(
            @RequestParam Optional<String> name,
            @RequestParam Optional<String> title,
            @RequestParam Optional<Race> race,
            @RequestParam Optional<Profession> profession,
            @RequestParam Optional<Long> after,
            @RequestParam Optional<Long> before,
            @RequestParam Optional<Boolean> banned,
            @RequestParam Optional<Integer> minExperience,
            @RequestParam Optional<Integer> maxExperience,
            @RequestParam Optional<Integer> minLevel,
            @RequestParam Optional<Integer> maxLevel
    ) {
        PlayerFilter filter = new PlayerFilter();

        if (name.isPresent()) filter.setName(name.get());
        if (title.isPresent()) filter.setTitle(title.get());
        if (race.isPresent()) filter.setRace(race.get());
        if (profession.isPresent()) filter.setProfession(profession.get());
        if (after.isPresent()) filter.setAfter(after.get());
        if (before.isPresent()) filter.setBefore(before.get());
        if (banned.isPresent()) filter.setBanned(banned.get());
        if (minExperience.isPresent()) filter.setMinExperience(minExperience.get());
        if (maxExperience.isPresent()) filter.setMaxExperience(maxExperience.get());
        if (minLevel.isPresent()) filter.setMinLevel(minLevel.get());
        if (maxLevel.isPresent()) filter.setMaxLevel(maxLevel.get());

        return playerService.getCount(filter);
    }

    @PostMapping(
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public ResponseEntity<Player> addPlayer(@RequestBody Player plr) {
        plr.setId(null);
        plr.setLevel(null);
        plr.setUntilNextLevel(null);
        if (plr.getExperience() == null) {
            plr.setExperience(0);
        }
        final DataBinder dataBinder = new DataBinder(plr);
        dataBinder.addValidators(playerValidator);
        dataBinder.validate();

        if (dataBinder.getBindingResult().hasErrors()) {
            return ResponseEntity.badRequest().body(null);
        }
        setLevelAndExpForNextLevel(plr);
        return ResponseEntity.ok(playerRepository.save(plr));

    }

    @GetMapping(value = "/{id}", //
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public ResponseEntity<Player> getPlayer(@PathVariable("id") Long id) {
        if (id < 1) {
            return ResponseEntity.badRequest().body(null);
        }
        Optional<Player> plr = playerRepository.findById(id);


        if (plr.isPresent()) {
            return ResponseEntity.ok().body(plr.get());
        } else {
            return ResponseEntity.notFound().build();
        }

    }

    @PostMapping(value = "/{id}", //
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public ResponseEntity<Player> updatePlayer(@RequestBody Player newPlayerData, @PathVariable("id") Long id) {
        if (id < 1) {
            return ResponseEntity.badRequest().body(null);
        }
        final DataBinder dataBinder = new DataBinder(newPlayerData);
        dataBinder.addValidators(playerUpdateValidator);
        dataBinder.validate();

        if (dataBinder.getBindingResult().hasErrors()) {
            return ResponseEntity.badRequest().body(null);
        }

        Optional<Player> optionalPlayer = playerRepository.findById(id);

        if (optionalPlayer.isPresent()) {
            Player player = optionalPlayer.get();
            if (newPlayerData.getName() != null) player.setName(newPlayerData.getName());
            if (newPlayerData.getTitle() != null) player.setTitle(newPlayerData.getTitle());
            if (newPlayerData.getRace() != null) player.setRace(newPlayerData.getRace());
            if (newPlayerData.getProfession() != null) player.setProfession(newPlayerData.getProfession());
            if (newPlayerData.getBirthday() != null) player.setBirthday(newPlayerData.getBirthday());
            if (newPlayerData.getBanned() != null) player.setBanned(newPlayerData.getBanned());
            if (newPlayerData.getExperience() != null) player.setExperience(newPlayerData.getExperience());

            setLevelAndExpForNextLevel(player);
            return ResponseEntity.ok().body(playerRepository.save(player));

        } else {
            return ResponseEntity.notFound().build();
        }

    }

    @DeleteMapping(value = "/{id}", //
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public void deletePlayer(@PathVariable("id") Long id, HttpServletResponse response) {
        if (id < 1) {
            response.setStatus(400);
            return;
        }
        Optional<Player> player = playerRepository.findById(id);


        if (player.isPresent()) {

            playerRepository.delete(player.get());
            response.setStatus(200);
        } else {
            response.setStatus(404);
        }
    }


    private void setLevelAndExpForNextLevel(Player plr) {
        plr.setLevel((int) ((Math.sqrt(2500 + 200 * plr.getExperience()) - 50) / 100));
        plr.setUntilNextLevel(50 * (plr.getLevel() + 1) * (plr.getLevel() + 2) - plr.getExperience());
    }

}
