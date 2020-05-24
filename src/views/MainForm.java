package views;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import models.enums.PondEntity;
import models.pond.Pond;
import models.pond.PondMap;
import models.pond.PondPoint;

import javax.sound.sampled.LineUnavailableException;
import javax.swing.*;
import java.awt.*;
import java.io.BufferedInputStream;
import java.io.InputStream;


import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class MainForm {

    // Значения пруда по умолчания
    private Integer hPondSize = 100;
    private Integer vPondSize = 100;
    private Integer pointSize = 5;

    private Pond pond;
    public JPanel mainPanel;
    private JButton stateButton;
    private JPanel pondPanel;
    private JPanel pondConfigPanel;
    private JSpinner luceSpinner;
    private JSpinner crucianSpinner;
    private JSpinner seaWeedSpinner;
    private JSpinner rockSpinner;
    private JSpinner seaWeedLifeTimeSpinner;
    private JSpinner seaWeedGrowthRateSpinner;
    private JSpinner stepDelaySpinner;
    private JSpinner crucianLifeTimeSpinner;
    private JSpinner luceLifeTimeSpinner;
    private JSpinner crucialGrowthRateSpinner;
    private JSpinner luceGrowthRateSpinner;
    private JLabel TuPicture;
    private boolean loop;
    private boolean PX=true;
    private boolean once=true;

    public MainForm() {
        pond = new Pond(hPondSize, vPondSize);
        initializeForm();
    }

    /**
     * Инициализация формы
     */
    private void initializeForm() {

        // Установка параметров для камней
        initializeRock();
        // Установка параметров для водорослей
        initializeSeaWeed();
        // Установка параметров для карасей
        initializeCrucial();
        // Установка параметров для щук
        initializeLuce();

        // Установка параметра - время между тактами
        stepDelaySpinner.setModel(createSpinnerModel(100, 100, 15000, 100));
        // Установка размеров окна по умолчания
        pondPanel.setSize(new Dimension(500, 500));
        addListeners();
    }

    /**
     * Установка параметров для щук
     */
    private void initializeLuce() {
        luceSpinner.setModel(createSpinnerModel());
        luceLifeTimeSpinner.setModel(createSpinnerModel(50, 1, 100, 1));
        luceGrowthRateSpinner.setModel(createSpinnerModel(4, 1, 100, 1));
    }

    /**
     * Установка параметров для карасей
     */
    private void initializeCrucial() {
        crucianSpinner.setModel(createSpinnerModel());
        crucianLifeTimeSpinner.setModel(createSpinnerModel(50, 1, 100, 1));
        crucialGrowthRateSpinner.setModel(createSpinnerModel(4, 1, 100, 1));
    }

    /**
     * Установка параметров для камней
     */
    private void initializeRock() {
        rockSpinner.setModel(createSpinnerModel());
    }

    /**
     * Установка параметров для водорослей
     */
    private void initializeSeaWeed() {
        seaWeedSpinner.setModel(createSpinnerModel());
        seaWeedGrowthRateSpinner.setModel(createSpinnerModel());
        seaWeedLifeTimeSpinner.setModel(createSpinnerModel(15, 1, 100, 1));

    }

    /**
     * Создание стандартной модели для спиннера
     *
     * @param value    - значение по умолчанию
     * @param minValue - минимальное допустимое значение
     * @param maxValue - максимально допустимое значение
     * @param stepSize - шаг
     * @return модель спиннера
     */
    private SpinnerModel createSpinnerModel(int value, int minValue, int maxValue, int stepSize) {
        return new SpinnerNumberModel(value, minValue, maxValue, stepSize);
    }

    /**
     * Перегрузка метода для модели спиннера с параметрами по умолчанию
     *
     * @return модель спиннера
     */
    private SpinnerModel createSpinnerModel() {
        return createSpinnerModel(5, 1, 100, 1);
    }

    /**
     * Создание обработчиков событий
     */
    private void addListeners() {

        // Обработка события нажатия нап кнопку старт/стоп
        stateButton.addActionListener(e -> {
            if (!pond.isStarted()) {
                startPond();
            } else {
                stopPond();
            }
        });
        // Обработка события пруда
        pond.addMyEventListener(evt -> refreshPondDraw());
    }

    /**
     * Функция остановки жизни в пруду и его очистки
     */
    private void stopPond() {
        stateButton.setText("Start");
        pond.stop();
        pond.reset();
        clearPondDrawPanel();
        PX=false;

    }

    /**
     * Функция запуска имитации жизни в пруду
     */

    Thread thread = new Thread(new Runnable()
    {
        public void run()
        {
            try {
                InputStream in = new BufferedInputStream(this.getClass().getResourceAsStream("/loop_song.wav"));
                Clip clip = AudioSystem.getClip();

                clip.open(AudioSystem.getAudioInputStream(in));
                for(int i=0;i<10;i++){

                    if (PX){
                        clip.start();
  i--;
                    }
                    else{
                        clip.stop();
i--;
                    }
i--;

                }

            } catch (Exception e) {
                System.err.println(e);
            }
        }
    });


    private void startPond() {
        stateButton.setText("Stop");
        Integer stepDelay = (Integer) stepDelaySpinner.getValue();

        // Добавление объектов в пруд, в зависимости от параметров пользователя
        pond.addObjects((Integer) rockSpinner.getValue(), PondEntity.Rock, 0, 0);
        pond.addObjects((Integer) seaWeedSpinner.getValue(), PondEntity.SeaWeed, (Integer) seaWeedLifeTimeSpinner.getValue(), (Integer) seaWeedGrowthRateSpinner.getValue());
        pond.addObjects((Integer) crucianSpinner.getValue(), PondEntity.Crucian, (Integer) crucianLifeTimeSpinner.getValue(), (Integer) crucialGrowthRateSpinner.getValue());
        pond.addObjects((Integer) luceSpinner.getValue(), PondEntity.Luce, (Integer) luceLifeTimeSpinner.getValue(), (Integer) luceGrowthRateSpinner.getValue());
        pond.setStepDelay(stepDelay);



        // Запуск пруда
        pond.start();
if(once) {
    thread.start();
}
once=false;
PX=true;

    }

    /**
     * Очистка пруда
     */
    private void clearPondDrawPanel() {
        pondPanel.removeAll();
        pondPanel.revalidate();
    }

    /**
     * Обновление состояния пруда
     */
    private void refreshPondDraw() {
        clearPondDrawPanel();
        JPanel pondDrawPanel = createPondDrawPanel();
        pondDrawPanel.setLayout(new BorderLayout());
        pondPanel.add(pondDrawPanel);
        pondDrawPanel.revalidate();
    }

    /**
     * Создание новой панели с текущим состояние пруда
     *
     * @return панель
     */
    private JPanel createPondDrawPanel() {
        return new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);

                if (pond == null || !pond.isStarted()) {
                    return;
                }

                PondMap pondMap = pond.getPondMap();
                for (int i = 0; i < hPondSize; i++) {
                    for (int j = 0; j < vPondSize; j++) {
                        PondPoint point = pondMap.get(i, j);
                        Color currentColor = point.getColor();
                        if (currentColor == null) {
                            continue;
                        }
                        g.setColor(currentColor);
                        g.fillRect(i * pointSize, j * pointSize, pointSize, pointSize);
                    }
                }
            }
        };
    }
}
