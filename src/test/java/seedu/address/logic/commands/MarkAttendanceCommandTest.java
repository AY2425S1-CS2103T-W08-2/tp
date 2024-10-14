package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;

/**
 * Contains integration tests (interaction with the Model) and unit tests for
 * {@code MarkAttendanceCommand}.
 */
public class MarkAttendanceCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_validIndexUnfilteredList_success() {
        Person personToMarkAttendance = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person markedPerson = new Person(
                personToMarkAttendance.getName(), personToMarkAttendance.getPhone(),
                personToMarkAttendance.getEmail(), personToMarkAttendance.getAddress(),
                personToMarkAttendance.getPayment(), personToMarkAttendance.getTags());
        MarkAttendanceCommand markAttendanceCommand = new MarkAttendanceCommand(INDEX_FIRST_PERSON);

        String expectedMessage = String.format(MarkAttendanceCommand.MESSAGE_MARK_ATTENDANCE_PERSON_SUCCESS,
                Messages.format(personToMarkAttendance));

        ModelManager expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(model.getFilteredPersonList().get(0), markedPerson);
        assertCommandSuccess(markAttendanceCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        MarkAttendanceCommand markAttendanceCommand = new MarkAttendanceCommand(outOfBoundIndex);

        assertCommandFailure(markAttendanceCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_validIndexFilteredList_success() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Person personToMarkAttendance = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person markedPerson = new Person(
                personToMarkAttendance.getName(), personToMarkAttendance.getPhone(),
                personToMarkAttendance.getEmail(), personToMarkAttendance.getAddress(),
                personToMarkAttendance.getPayment(), personToMarkAttendance.getTags());
        MarkAttendanceCommand markAttendanceCommand = new MarkAttendanceCommand(INDEX_FIRST_PERSON);

        String expectedMessage = String.format(MarkAttendanceCommand.MESSAGE_MARK_ATTENDANCE_PERSON_SUCCESS,
                Messages.format(markedPerson));

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setPerson(model.getFilteredPersonList().get(0), markedPerson);

        assertCommandSuccess(markAttendanceCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexFilteredList_throwsCommandException() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Index outOfBoundIndex = INDEX_SECOND_PERSON;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getPersonList().size());

        MarkAttendanceCommand markAttendanceCommand = new MarkAttendanceCommand(outOfBoundIndex);

        assertCommandFailure(markAttendanceCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        MarkAttendanceCommand markAttendanceFirstCommand = new MarkAttendanceCommand(INDEX_FIRST_PERSON);
        MarkAttendanceCommand markAttendanceSecondCommand = new MarkAttendanceCommand(INDEX_SECOND_PERSON);

        // same object -> returns true
        assertTrue(markAttendanceFirstCommand.equals(markAttendanceFirstCommand));

        // same values -> returns true
        MarkAttendanceCommand markAttendanceFirstCommandCopy = new MarkAttendanceCommand(INDEX_FIRST_PERSON);
        assertTrue(markAttendanceFirstCommand.equals(markAttendanceFirstCommandCopy));

        // different types -> returns false
        assertFalse(markAttendanceFirstCommand.equals(1));

        // null -> returns false
        assertFalse(markAttendanceFirstCommand.equals(null));

        // different person -> returns false
        assertFalse(markAttendanceFirstCommand.equals(markAttendanceSecondCommand));
    }

    @Test
    public void toStringMethod() {
        Index targetIndex = Index.fromOneBased(1);
        MarkAttendanceCommand markAttendanceCommand = new MarkAttendanceCommand(targetIndex);
        String expected = MarkAttendanceCommand.class.getCanonicalName() + "{targetIndex=" + targetIndex + "}";
        assertEquals(expected, markAttendanceCommand.toString());
    }
}

