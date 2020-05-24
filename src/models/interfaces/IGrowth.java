package models.interfaces;

import models.Point;
import models.pond.PondObject;

/**
 * Интерфейс функций роста
 */
public interface IGrowth {
    void growth();
    void decreaseGrowth();
    void setGrowthRate(Integer growthRate);
    PondObject createNewObject(Point point);
}
