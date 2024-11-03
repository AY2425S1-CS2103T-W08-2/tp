package seedu.address.ui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.address.model.participation.Participation;
import seedu.address.model.person.Person;
import seedu.address.model.tutorial.Tutorial;

/**
 * An UI component that displays key information related to students and tutorials.
 */
public class Dashboard extends UiPart<Region> {

    private static final String FXML = "Dashboard.fxml";

    private final ObservableList<Person> personList;
    private final ObservableList<Participation> participationList;
    private final ObservableList<Tutorial> tutorialList;
    private final Map<Tutorial, TutorialCard> tutorialCards = new HashMap<>();

    @FXML
    private HBox dashboard;
    @FXML
    private Label total;
    @FXML
    private Label feesOverdue;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private HBox tutorials;


    /**
     * Creates a {@code Dashboard} with the given {@code Payment} t0 display.
     */
    public Dashboard(ObservableList<Person> personList, ObservableList<Participation> participationList,
                     ObservableList<Tutorial> tutorialList) {
        super(FXML);
        this.personList = personList;
        this.participationList = participationList;
        this.tutorialList = tutorialList;

        initializeTutorialCards();
        updateStudentSummary();
        addParticipationListListener();
    }


    private void initializeTutorialCards() {
        for (Tutorial tutorial : tutorialList) {
            List<Participation> initialParticipationList = getParticipationListForTutorial(tutorial);
            TutorialCard card = new TutorialCard(tutorial.getSubject(), initialParticipationList);
            tutorials.getChildren().add(card.getRoot());
            tutorialCards.put(tutorial, card);  // Store each card in the map
        }
    }

    private List<Participation> getParticipationListForTutorial(Tutorial tutorial) {
        return participationList.stream()
                .filter(p -> p.getTutorial().equals(tutorial))
                .collect(Collectors.toList());
    }

    private void addParticipationListListener() {
        // for tutorial cards
        participationList.addListener((ListChangeListener<Participation>) change -> {
            while (change.next()) {
                if (change.wasAdded() || change.wasRemoved()) {
                    updateTutorialCards();
                }
            }
        });

        // for total and fees overdue
        personList.addListener((ListChangeListener<Person>) change -> {
            while (change.next()) {
                if (change.wasAdded() || change.wasRemoved()) {
                    updateStudentSummary();
                }
            }
        });
    }


    private void updateTutorialCards() {
        for (Tutorial tutorial : tutorialList) {
            List<Participation> updatedParticipationList = getParticipationListForTutorial(tutorial);
            TutorialCard card = tutorialCards.get(tutorial);
            if (card != null) {
                card.updateParticipationList(updatedParticipationList);
            }
        }
    }

    private void updateStudentSummary() {
        // Update total number of students
        total.setText(String.valueOf(personList.size()));

        // Calculate and update number of students with overdue fees
        long studentsWithFeesOverdue = personList.stream()
                .filter(person -> Integer.parseInt(person.getPayment().overdueAmount) > 0)
                .count();
        feesOverdue.setText(String.valueOf(studentsWithFeesOverdue));
    }

}

