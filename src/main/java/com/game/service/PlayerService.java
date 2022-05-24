package com.game.service;

import com.game.entity.Player;
import com.game.entity.PlayerFilter;
import com.game.repository.PlayerRepository;
import com.game.repository.PlayerSpecifications;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class PlayerService {
    @Autowired
    private PlayerRepository playerRepository;

    public Page<Player> getPlayers(PlayerFilter filter, PageRequest pageRequest) {
        Specification<Player> specification = PlayerSpecifications.getFilteredPlayers(filter);
        return playerRepository.findAll(specification, pageRequest);
    }

    public Long getCount(PlayerFilter filter) {
        return playerRepository.count(PlayerSpecifications.getFilteredPlayers(filter));
    }
}
