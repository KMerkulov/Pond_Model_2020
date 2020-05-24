package models.pond.entities;

import models.enums.PondEntity;
import models.interfaces.IMoveable;
import models.Point;
import models.pond.PondObject;

import java.awt.*;

/**
 * Класс карасей , наследуется от водорослей и реализует интерфейс движения
 */
public class Crucian extends SeaWeed implements IMoveable {
    public Crucian(Point coordinates, Integer lifeTime , Integer growthRate) {
        super(coordinates, lifeTime , growthRate);
        setVisibilityOrder(3);
    }

    /**
     * Функция движения объекта
     */
    @Override
    public void move() {

        // Получает следующую точку , для перемешения
        Point newPoint = getPond().getMovePoint(getPoint() , getEntityType());
        if (newPoint == null){
            return;
        }
        // Перемещение
        getPond().move(this , newPoint);
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

        Point newPoint= getPond().getMovePoint(getPoint() , getEntityType());

        if (newPoint != null){
            getPond().add(createNewObject(newPoint));
        }
    }

    /**
     * Создание нового объекта
     * @param point - координаты объекта
     * @return
     */
    @Override
    public PondObject createNewObject(Point point) {

        Crucian newCrucian = new Crucian(point , lifeTime , growthRate);
        newCrucian.setPond(getPond());
        return newCrucian;
    }

    /**
     * Получение типа объекта
     * @return
     */
    @Override
    public Color getColor() {
        return Color.YELLOW;
    }

    /**
     * Функция получения типа
     * @return
     */
    @Override
    public PondEntity getEntityType() {
        return PondEntity.Crucian;
    }

}
