package com.game.repository;

import com.game.entity.Player;
import com.game.entity.PlayerFilter;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PlayerSpecifications {
    public static Specification<Player> getFilteredPlayers(PlayerFilter filter) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filter.getName() != null) {
                predicates.add(criteriaBuilder.like(root.get("name"), String.format("%%%s%%", filter.getName())));
            }
            if (filter.getTitle() != null) {
                predicates.add(criteriaBuilder.like(root.get("title"), String.format("%%%s%%", filter.getTitle())));
            }
            if (filter.getRace() != null) {
                predicates.add(criteriaBuilder.equal(root.get("race"), filter.getRace()));
            }
            if (filter.getProfession() != null) {
                predicates.add(criteriaBuilder.equal(root.get("profession"), filter.getProfession()));
            }
            if (filter.getAfter() != null) {
                predicates.add(criteriaBuilder.greaterThan(root.get("birthday"), new Date(filter.getAfter())));
            }
            if (filter.getBefore() != null) {
                predicates.add(criteriaBuilder.lessThan(root.get("birthday"), new Date(filter.getBefore())));
            }
            if (filter.getBanned() != null) {
                predicates.add(criteriaBuilder.equal(root.get("banned"), filter.getBanned()));
            }
            if (filter.getMinExperience() != null && filter.getMaxExperience() != null) {
                predicates.add(criteriaBuilder.between(root.get("experience"), filter.getMinExperience(), filter.getMaxExperience()));
            }
            if (filter.getMinExperience() != null) {
                predicates.add(criteriaBuilder.greaterThan(root.get("experience"), filter.getMinExperience()));
            }
            if (filter.getMaxExperience() != null) {
                predicates.add(criteriaBuilder.lessThan(root.get("experience"), filter.getMaxExperience()));
            }
            if (filter.getMinLevel() != null && filter.getMaxLevel() != null) {
                predicates.add(criteriaBuilder.between(root.get("level"), filter.getMinLevel(), filter.getMaxLevel()));
            }
            if (filter.getMinLevel() != null) {
                predicates.add(criteriaBuilder.greaterThan(root.get("level"), filter.getMinLevel()));
            }
            if (filter.getMaxLevel() != null) {
                predicates.add(criteriaBuilder.lessThan(root.get("level"), filter.getMaxLevel()));
            }


            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
