package models.pond.entities;

import models.enums.PondEntity;
import models.interfaces.IGrowth;
import models.Point;
import models.pond.PondObject;

import java.awt.*;

/**
 * Класс водорослей, расширяет класс камень, реализует интерфейс роста объекта
 */
public class SeaWeed extends Rock implements IGrowth {

    Integer lifeTime;
    private Integer currentLifeTime;
    Integer growthRate;
    Integer currentGrowthRate;

    /**
     * Конструктор класса
     * @param coordinates - координаты
     * @param lifeTime - время жизни
     * @param growthRate - темп роста
     */
    public SeaWeed(Point coordinates , Integer lifeTime, Integer growthRate) {
        super(coordinates);
        this.lifeTime = lifeTime;
        this.currentLifeTime = lifeTime;
        setVisibilityOrder(2);
        setGrowthRate(growthRate);
    }

    /**
     * Функция роста объекта
     */
    @Override
    public void growth() {

        // Если ещё не вырос , то уменьшаем время до роста и выходим
        if (currentGrowthRate != 1){
            decreaseGrowth();
            return;
        }
        currentGrowthRate = growthRate;

        // Если вырос , то создаенм новый объект в свободной точке
        Point newPoint= getPond().getMovePoint(getPoint() , getEntityType());
        if (newPoint != null){
            getPond().add(createNewObject(newPoint));
        }
    }

    /**
     * Уменьшить текущее значение роста на 1
     */
    @Override
    public void decreaseGrowth() {
        currentGrowthRate--;
    }

    /**
     * Установить параметр роста для объекта
     * @param growthRate
     */
    @Override
    public void setGrowthRate(Integer growthRate) {
        this.growthRate = growthRate;
        this.currentGrowthRate = growthRate;
    }

    /**
     * Функция создания нового объекта
     * @param point - координаты объекта
     * @return
     */
    @Override
    public PondObject createNewObject(Point point) {

        SeaWeed newSeaWeed = new SeaWeed(point , lifeTime , growthRate);
        newSeaWeed.setPond(getPond());
        return newSeaWeed;
    }

    /**
     * Функция уменьшения времени жизни
     * @return
     */
    @Override
    public Boolean decreaseLifeTime() {
        currentLifeTime--;
        return currentLifeTime <= 0;
    }

    /**
     * Функция получения цвета объекта
     * @return
     */
    @Override
    public Color getColor() {
        return Color.GREEN;
    }

    /**
     * Функция получения типа объекта
     * @return
     */
    @Override
    public PondEntity getEntityType() {
        return PondEntity.SeaWeed;
    }
}
