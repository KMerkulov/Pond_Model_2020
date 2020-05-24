package models.pond;

import models.enums.PondEntity;
import models.interfaces.IGrowth;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * Класс точки на карте пруда
 */
public class PondPoint {
    private final Comparator<PondObject> comp = Comparator.comparingInt(PondObject::getVisibilityOrder);
    private ArrayList<PondObject> pointObjects;

    /**
     * Конструктор точки
     */
    PondPoint() {
        this.pointObjects = new ArrayList<>();
    }

    /**
     * Функция получения всех объектов в точке
     * @return
     */
    ArrayList<PondObject> getPointObjects() {
        return pointObjects;
    }

    /**
     * Добавление объекта в эту точку
     * @param pondObject - Добавляемый объект
     */
    void add(PondObject pondObject) {
        pointObjects.add(pondObject);
        // Если в этой точке есть водоросли и карась
        // то караси съедают водоросли и растут
        if (contains(PondEntity.Crucian) && contains(PondEntity.SeaWeed)){
            removeAll(PondEntity.SeaWeed);
            decreaseGrowth(PondEntity.Crucian);
        }
        // Если в этой точке есть щуки и караси
        // то щуки съедают карасей и растут
        if (contains(PondEntity.Luce) && contains(PondEntity.Crucian)){
            removeAll(PondEntity.Crucian);
            decreaseGrowth(PondEntity.Luce);
        }
    }

    /**
     * Функция роста объектов заданого типа в точке
     * @param pondEntity
     */
    private void decreaseGrowth(PondEntity pondEntity) {
        pointObjects.stream().filter(po -> po.getEntityType().equals(pondEntity)).forEach(po -> ((IGrowth)po).decreaseGrowth());
    }

    /**
     * Удалить объект с точки
     * @param pondObject
     */
    void remove(PondObject pondObject){
        pointObjects.remove(pondObject);
    }

    /**
     * Проверка , что точка пуста
     * @return
     */
    boolean empty() {
        return pointObjects.isEmpty();
    }

    /**
     * Проверка , содержит ли эта точка объекты заданного типа
     * @param pondObjectType
     * @return
     */
    boolean contains(PondEntity pondObjectType) {
        return pointObjects.stream().anyMatch(o -> o.getEntityType().equals(pondObjectType));
    }

    /**
     * Удалить все объекты заданного типа
     * @param pondObjectType
     */
    private void removeAll(PondEntity pondObjectType) {
        pointObjects.removeIf(o -> o.getEntityType().equals(pondObjectType));
    }

    /**
     * Получение цвета точки
     * @return
     */
    public Color getColor() {
        if (pointObjects.isEmpty()){
            return null;
        }
        PondObject pondObject = pointObjects.stream().max(comp).get();
        return  pondObject.getColor();
    }

    public boolean exists(PondObject pondObject) {
        return pointObjects.contains(pondObject);
    }
}
