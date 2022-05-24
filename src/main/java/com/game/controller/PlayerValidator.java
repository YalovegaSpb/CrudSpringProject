package com.game.controller;

import com.game.entity.Player;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Service
public class PlayerValidator implements Validator {

    @Override
    public boolean supports(Class<?> player) {
        return Player.class.equals(player);
    }

    @Override
    public void validate(Object o, Errors errors) {
        ValidationUtils.rejectIfEmpty(errors, "name", "name.empty");
        ValidationUtils.rejectIfEmpty(errors, "title", "title.empty");
        ValidationUtils.rejectIfEmpty(errors, "race", "race.empty");
        ValidationUtils.rejectIfEmpty(errors, "profession", "profession.empty");
        ValidationUtils.rejectIfEmpty(errors, "birthday", "birthday.empty");
        ValidationUtils.rejectIfEmpty(errors, "experience", "experience.empty");

        Player player = (Player) o;
        if (player.getName() != null) {
            if ((player.getName().length() > 12) || (player.getName().isEmpty())) {
                errors.rejectValue("name", "value.negative");
            }
        }
        if (player.getTitle() != null) {
            if (player.getTitle().length() > 30) {
                errors.rejectValue("title", "value.negative");
            }
        }

        if (player.getExperience() == null || (player.getExperience() < 0 || player.getExperience() > 10000000)) {
            errors.rejectValue("experience", "value.negative");
        }
        if (player.getBirthday() != null) {
            if (player.getBirthday().getTime() < 0) {
                errors.rejectValue("birthday", "value.negative");
            }
            int year = player.getBirthday().getYear() + 1900;
            if ((year < 2000) || (year > 3000)) {
                errors.rejectValue("birthday", "value.negative");
            }
        }


    }
}
