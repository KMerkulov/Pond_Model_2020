package models.pond;

import models.Point;
import models.enums.PondEntity;
import models.interfaces.IColorable;

import java.awt.*;

/**
 * Базовый класс объект пруда
 */
public abstract class PondObject implements IColorable , Cloneable {
    private models.Point point;
    private Pond pond;
    private Integer visibilityOrder;

    /**
     * Конструктор объекта
     * @param point - координата объекта
     */
    public PondObject(models.Point point) {
        this.point = point;
    }

    public PondObject() {

    }

    protected models.Point getPoint() {
        return point;
    }

    void setPoint(Point coordinates) {
        this.point = coordinates;
    }

    protected Pond getPond() {
        return pond;
    }

    public void setPond(Pond pond) {
        this.pond = pond;
    }

    /**
     * Возвращает приоритет видимости объекта на карте
     * @return
     */
    Integer getVisibilityOrder() {
        return visibilityOrder;
    }

    /**
     * Задает тип видимости
     * @param visibilityOrder
     */
    protected void setVisibilityOrder(Integer visibilityOrder) {
        this.visibilityOrder = visibilityOrder;
    }

    @Override
    public Color getColor() {
        return null;
    }

    public abstract PondEntity getEntityType();
    public abstract Boolean decreaseLifeTime();
}
