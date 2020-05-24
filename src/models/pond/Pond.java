package models.pond;

import events.PondChanged;
import events.PondChangedListener;
import models.Point;
import models.enums.PondEntity;
import models.interfaces.IGrowth;
import models.interfaces.IMoveable;
import models.pond.entities.*;

import javax.swing.event.EventListenerList;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Pond {

    private PondMap pondMap;
    private Integer hSize;
    private Integer vSize;
    private Timer timer;
    private Integer stepDelay;
    private Boolean started = false;

    private EventListenerList listenerList = new EventListenerList();

    /**
     * Добавляет
     * @param listener
     */
    public void addMyEventListener(PondChangedListener listener) {
        listenerList.add(PondChangedListener.class, listener);
    }

    /**
     * Отправляет событие изменения состояния пруда всем заинтересованым
     * @param evt
     */
    private void firePondChanged(PondChanged evt) {
        Object[] listeners = listenerList.getListenerList();
        for (int i = 0; i < listeners.length; i = i + 2) {
            if (listeners[i] == PondChangedListener.class) {
                ((PondChangedListener) listeners[i + 1]).pondChangedEventOccured(evt);
            }
        }
    }

    /**
     * Конструктор пруда
     * @param horizontalSize
     * @param verticalSize
     */
    public Pond(int horizontalSize, int verticalSize) {
        this.setHSize(horizontalSize);
        this.setVSize(verticalSize);
        generatePond();
        timer = new Timer();
    }

    /**
     * Создание карты пруда
     */
    private void generatePond() {
        this.pondMap = new PondMap(hSize, vSize);
    }

    /**
     * Функция получения следующего шага объекта пруда
     * @param currentPoint
     * @param pondObjectType
     * @return
     */
    public Point getMovePoint(Point currentPoint, PondEntity pondObjectType) {
        ArrayList<Point> neighboringPoints = getNeighboringPoints(currentPoint);
        Optional<Point> point = Optional.empty();
        switch (pondObjectType) {
            // Если двигается карась, то он ищет водоросли в ближайщих клетках
            case Crucian:
                point = neighboringPoints.stream().filter(p -> pondMap.contains(p, PondEntity.SeaWeed)).findAny();
                break;
            // Если двигается щука, то она ищет карася в ближайщих клетках
            case Luce:
                point = neighboringPoints.stream().filter(p -> pondMap.contains(p, PondEntity.Crucian)).findAny();
    }

        if (point.isPresent()) {
            return point.get();
        }

        // Если точка ещё не определена , то берем случайную из доступных
        Random rand = new Random();
        Stream<Point> aviablePoints = neighboringPoints.stream().filter(pondMap::empty);

        ArrayList<Point> resultPoints = aviablePoints.collect(Collectors.toCollection(ArrayList::new));

        // Если доступных точек нет  ,то стоит на месте
        if (resultPoints.isEmpty()) {
            return null;
        }

        if (resultPoints.size() == 1) {
            return resultPoints.get(0);
        }

        return resultPoints.get(rand.nextInt(resultPoints.size() - 1));
    }

    /**
     * Возвращает карту пруда
     * @return
     */
    public PondMap getPondMap() {
        return pondMap;
    }

    public void add(PondObject pondObject) {
        Point coordinates = pondObject.getPoint();
        if (pondMap.empty(coordinates)) {
            pondMap.initialize(coordinates);
        }
        pondMap.add(pondObject);
    }

    /**
     * Удаление объекта из пруда
     * @param pondObject
     */
    private void remove(PondObject pondObject) {
        pondMap.remove(pondObject);
    }

    /**
     * Функция передвижения объекта
     * @param pondObject
     * @param newPoint
     */
    public void move(PondObject pondObject, Point newPoint) {
        pondMap.remove(pondObject);
        pondObject.setPoint(newPoint);
        pondMap.add(pondObject);
    }

    private void setHSize(Integer hSize) {
        this.hSize = hSize;
    }

    private void setVSize(Integer vSize) {
        this.vSize = vSize;
    }

    /**
     * Функция запуска имитации жизни в пруду
     */
    public void start() {
        started = true;
        timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                nextStep();
                firePondChanged(new PondChanged(pondMap));
            }
        }, stepDelay, stepDelay);
    }

    /**
     * Функция остановки пруда
     */
    public void stop() {
        timer.cancel();
        timer.purge();
        started = false;
    }

    /**
     * Функция получения соседних точек
     * @param point текущая позиция
     * @return доступные соседние точки
     */
    private ArrayList<Point> getNeighboringPoints(Point point) {
        ArrayList<Point> neighboringPoints = new ArrayList<>();
        Integer x = point.getX();
        Integer y = point.getY();

        if (x > 0 && y > 0) {
            neighboringPoints.add(new Point(x - 1, y - 1));
        }
        if (x > 0) {
            neighboringPoints.add(new Point(x - 1, y));
            if (y < vSize - 1) {
                neighboringPoints.add(new Point(x - 1, y + 1));
            }
        }

        if (y > 0) {
            neighboringPoints.add(new Point(x, y - 1));
            if (x < hSize - 1) {
                neighboringPoints.add(new Point(x + 1, y - 1));
            }
        }

        if (y < vSize - 1) {

            neighboringPoints.add(new Point(x, y + 1));
            if (x < hSize - 1) {
                neighboringPoints.add(new Point(x + 1, y + 1));
            }
        }
        if (x < hSize - 1) {
            neighboringPoints.add(new Point(x + 1, y));
        }
        return neighboringPoints;
    }

    /**
     * Функция обновления состояний объекта
     */
    private void nextStep() {
        updatePondObjectState();
    }

    /**
     *  Функция рассчета следующего шага
     */
    private void updatePondObjectState() {
        Stream<PondObject> pondObjects = pondMap.getAllPondObject().stream();
        updateObjects(pondObjects);
    }

    /**
     * Функция перемещения/роста объектов
     * @param poundObjectStream
     */
    private void updateObjects(Stream<PondObject> poundObjectStream) {
        ArrayList<PondObject> pondObjects = poundObjectStream.collect(Collectors.toCollection(ArrayList::new));
        for (int i = 0; i < pondObjects.size(); i++) {
            PondObject pondObject = pondObjects.get(i);
            if (pondObject == null || !pondMap.exists(pondObject)) {
                continue;
            }

            // Если объект может двигаться , то пускай движется
            if (pondObject instanceof IMoveable) {
                ((IMoveable) pondObject).move();
            }

            // Если объект может рости , то пускай растет
            if (pondObject instanceof IGrowth) {
                ((IGrowth) pondObject).growth();
            }

            // Если у объекта кончилось время жизни , то удаляем из пруда
            if (pondObject.decreaseLifeTime()) {
                remove(pondObject);
            }
        }
    }

    public void setStepDelay(Integer stepDelay) {
        this.stepDelay = stepDelay;
    }

    public Boolean isStarted() {
        return started;
    }

    /**
     * Функция очистки карты пруда
     */
    public void reset() {
        pondMap.clear();
    }

    /**
     * Добавление объектов пруд
     * @param objectCount - Кол-во
     * @param pondEntity - Тип объекта
     * @param lifeTime - Время жизни
     * @param growthRate - Время роста
     */
    public void addObjects(Integer objectCount, PondEntity pondEntity, int lifeTime, int growthRate) {

        Random rand = new Random();
        for (int i = 0; i < objectCount; i++) {
            Point point;
            do {
                point = new Point(rand.nextInt(hSize), rand.nextInt(vSize));

            } while (!pondMap.empty(point));

            PondObject pondObject = null;

            switch (pondEntity) {
                case Rock:
                    pondObject = new Rock(point);
                    break;
                case SeaWeed:
                    pondObject = new SeaWeed(point, lifeTime, growthRate);
                    break;
                case Crucian:
                    pondObject = new Crucian(point, lifeTime, growthRate);
                    break;
                case Luce:
                    pondObject = new Luce(point, lifeTime, growthRate);
                    break;
            }
            pondObject.setPond(this);
            pondMap.add(pondObject);
        }
    }
}
