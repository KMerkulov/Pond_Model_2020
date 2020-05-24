package models.pond.entities;

import models.enums.PondEntity;
import models.Point;
import models.pond.PondObject;

import java.awt.*;

/**
 * Класс камень
 */
public class Rock extends PondObject {
    public Rock(Point coordinates) {
        super(coordinates);
        setVisibilityOrder(1);
    }

    Rock() {
        super();
    }

    /**
     * Функция получения цвета
     * @return
     */
    @Override
    public Color getColor() {
        return Color.BLACK;
    }

    /**
     * Функция получения цвета
     * @return
     */
    @Override
    public PondEntity getEntityType() {
        return PondEntity.Rock;
    }

    /**
     * Функция уменьшения времени жизни
     * @return
     */
    @Override
    public Boolean decreaseLifeTime() {
        return false;
    }
}
