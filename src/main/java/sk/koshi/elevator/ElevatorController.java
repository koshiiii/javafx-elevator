package sk.koshi.elevator;

import javafx.animation.PauseTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.stream.Collectors;

public class ElevatorController {

    private static final int MIN_FLOOR = 0;
    private static final int MAX_FLOOR = 8;
    private static final double FLOOR_DISTANCE = 58.0;
    private static final Duration ONE_FLOOR_DURATION = Duration.millis(550);
    private static final Duration WAIT_DURATION = Duration.seconds(2);

    @FXML
    private Rectangle elevator;

    @FXML
    private Label currentFloorLabel;

    @FXML
    private Label targetFloorLabel;

    @FXML
    private Label statusLabel;

    @FXML
    private Label queueLabel;

    private final Deque<Integer> pendingFloors = new ArrayDeque<>();

    private int currentFloor = 0;
    private int targetFloor = 0;
    private boolean moving;
    private boolean waiting;

    @FXML
    private void initialize() {
        elevator.setTranslateY(0);
        refreshInformation();
        statusLabel.setText("Výťah čaká na poschodí 0.");
    }

    @FXML
    private void requestFloor(ActionEvent event) {
        Button button = (Button) event.getSource();
        int requestedFloor = Integer.parseInt(button.getText());

        if (requestedFloor < MIN_FLOOR || requestedFloor > MAX_FLOOR) {
            return;
        }

        if (moving || waiting) {
            enqueueFloor(requestedFloor);
            return;
        }

        if (requestedFloor == currentFloor) {
            statusLabel.setText("Výťah sa už nachádza na poschodí " + currentFloor + ".");
            return;
        }

        startJourney(requestedFloor);
    }

    private void enqueueFloor(int floor) {
        if (floor == targetFloor || pendingFloors.contains(floor)) {
            statusLabel.setText("Poschodie " + floor + " už je naplánované.");
            return;
        }

        pendingFloors.addLast(floor);
        statusLabel.setText("Poschodie " + floor + " bolo pridané do frontu.");
        refreshQueue();
    }

    private void startJourney(int destination) {
        targetFloor = destination;
        moving = true;
        waiting = false;

        statusLabel.setText("Výťah smeruje na poschodie " + targetFloor + ".");
        refreshInformation();
        moveOneFloor();
    }

    private void moveOneFloor() {
        int direction = Integer.compare(targetFloor, currentFloor);

        if (direction == 0) {
            finishJourney();
            return;
        }

        int nextFloor = currentFloor + direction;

        TranslateTransition transition = new TranslateTransition(ONE_FLOOR_DURATION, elevator);
        transition.setToY(-nextFloor * FLOOR_DISTANCE);
        transition.setOnFinished(event -> {
            currentFloor = nextFloor;
            refreshInformation();

            if (currentFloor == targetFloor) {
                finishJourney();
            } else {
                moveOneFloor();
            }
        });
        transition.play();
    }

    private void finishJourney() {
        moving = false;
        currentFloor = targetFloor;
        statusLabel.setText("Výťah dorazil na poschodie " + currentFloor + ".");
        refreshInformation();

        if (!pendingFloors.isEmpty()) {
            waitAndContinue();
        }
    }

    private void waitAndContinue() {
        waiting = true;
        statusLabel.setText(
                "Výťah je na poschodí " + currentFloor + " a čaká 2 sekundy."
        );

        PauseTransition pause = new PauseTransition(WAIT_DURATION);
        pause.setOnFinished(event -> {
            waiting = false;
            Integer nextFloor = pollNextDifferentFloor();

            if (nextFloor == null) {
                statusLabel.setText("Výťah čaká na poschodí " + currentFloor + ".");
                refreshInformation();
            } else {
                startJourney(nextFloor);
            }
        });
        pause.play();
    }

    private Integer pollNextDifferentFloor() {
        while (!pendingFloors.isEmpty()) {
            int nextFloor = pendingFloors.removeFirst();
            refreshQueue();

            if (nextFloor != currentFloor) {
                return nextFloor;
            }
        }

        return null;
    }

    private void refreshInformation() {
        currentFloorLabel.setText(Integer.toString(currentFloor));
        targetFloorLabel.setText(moving ? Integer.toString(targetFloor) : "—");
        refreshQueue();
    }

    private void refreshQueue() {
        String queue = pendingFloors.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(" → "));

        queueLabel.setText(queue.isEmpty() ? "prázdny" : queue);
    }
}
