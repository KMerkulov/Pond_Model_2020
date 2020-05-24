package models.pond;

import models.Point;
import models.enums.PondEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Карта пруда
 */
public class PondMap {
    private PondPoint[][] pondPoints;
    private Integer hSize;
    private Integer vSize;

    /**
     * Конструктор карты пруда
     * @param horizontalSize
     * @param verticalSize
     */
    PondMap(Integer horizontalSize, Integer verticalSize) {

        hSize = horizontalSize;
        vSize = verticalSize;

        this.pondPoints = new PondPoint[horizontalSize][verticalSize];
    }

    /**
     * Проверка, содержит ли данная точка объекты заданного типа
     * @param point - Точка
     * @param pondEntity - Тип объекта
     * @return
     */
    boolean contains(Point point, PondEntity pondEntity) {
        PondPoint pondPoint = pondPoints[point.getX()][point.getY()];
        if (pondPoint == null) {
            initialize(point);
            return false;
        }
        return pondPoints[point.getX()][point.getY()].contains(pondEntity);
    }

    /**
     * Проверка , есть ли в данной точке какие-либо объекты
     * @param point
     * @return
     */
    boolean empty(Point point) {
        PondPoint pondPoint = pondPoints[point.getX()][point.getY()];
        // Если точка ещё не инициализирована , то создаем её
        if (pondPoint == null) {
            initialize(point);
            return true;
        }

        return pondPoints[point.getX()][point.getY()].empty();
    }

    /**
     * Инициализация точки пруда
     * @param point
     */
    void initialize(Point point) {
        pondPoints[point.getX()][point.getY()] = new PondPoint();
    }

    /**
     * Функция добавления объекта в заданную точку
     * @param pondObject
     */
    void add(PondObject pondObject) {
        Point coordinates = pondObject.getPoint();
        PondPoint pondPoint = pondPoints[coordinates.getX()][coordinates.getY()];
        if (pondPoint == null) {
            initialize(coordinates);
        }
        pondPoints[coordinates.getX()][coordinates.getY()].add(pondObject);
    }

    /**
     * Удаление объекта из пруда
     * @param pondObject
     */
    void remove(PondObject pondObject) {
        Point coordinates = pondObject.getPoint();
        pondPoints[coordinates.getX()][coordinates.getY()].remove(pondObject);
    }

    /**
     * Функция получения точки пруда
     * @param hPosition - Координата X
     * @param vPosition - Координата Y
     * @return
     */
    public PondPoint get(int hPosition, int vPosition) {
        if (pondPoints[hPosition][vPosition] == null)
            pondPoints[hPosition][vPosition] = new PondPoint();
        return pondPoints[hPosition][vPosition];
    }

    /**
     * Очистка пруда
     */
    void clear() {
        pondPoints = new PondPoint[hSize][vSize];
    }

    /**
     * Функция получения всех объектов в пруду
     * @return
     */
    ArrayList<PondObject> getAllPondObject() {
        return Arrays.stream(pondPoints)
                .flatMap(Arrays::stream)
                .filter(Objects::nonNull)
                .flatMap(pondPoint -> pondPoint.getPointObjects().stream())
                .filter(pondObject -> !pondObject.getEntityType().equals(PondEntity.Rock))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Проверка, существует ли объект
     * @param pondObject - Проверяемый объект
     * @return
     */
    boolean exists(PondObject pondObject) {
        Point coordinates = pondObject.getPoint();
        return pondPoints[coordinates.getX()][coordinates.getY()].exists(pondObject);
    }
}
