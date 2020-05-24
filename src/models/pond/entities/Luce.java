package models.pond.entities;

import models.enums.PondEntity;
import models.Point;
import models.pond.PondObject;

import java.awt.*;

/**
 * Класс щук
 */
public class Luce extends Crucian {
    public Luce(Point coordinates, Integer lifeTime , Integer growthRate) {
        super(coordinates , lifeTime , growthRate);
        setVisibilityOrder(4);
    }

    /**
     * Получение цвета объекта
     * @return
     */
    @Override
    public Color getColor() {
        return Color.RED;
    }

    /**
     * Получение типа объекта
     * @return
     */
    @Override
    public PondEntity getEntityType() {
        return PondEntity.Luce;
    }

    /**
     * Функция роста объекта
     */
    @Override
    public void growth() {

        if (currentGrowthRate != 1){
            return;
        }
        currentGrowthRate = growthRate;

        Point newPoint = getPond().getMovePoint(getPoint() , getEntityType());

        if (newPoint != null){
            getPond().add(createNewObject(newPoint));
        }
    }

    /**
     * Функция создания нового объекта
     * @param point
     * @return
     */
    @Override
    public PondObject createNewObject(Point point) {

        Luce newLuce = new Luce(point , lifeTime , growthRate);
        newLuce.setPond(getPond());
        return newLuce;
    }
}
