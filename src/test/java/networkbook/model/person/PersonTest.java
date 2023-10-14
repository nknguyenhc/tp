package networkbook.model.person;

import static networkbook.logic.commands.CommandTestUtil.VALID_COURSE_BOB;
import static networkbook.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static networkbook.logic.commands.CommandTestUtil.VALID_GRADUATING_YEAR_BOB;
import static networkbook.logic.commands.CommandTestUtil.VALID_LINK_BOB;
import static networkbook.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static networkbook.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static networkbook.logic.commands.CommandTestUtil.VALID_SPECIALISATION_BOB;
import static networkbook.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static networkbook.testutil.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import networkbook.testutil.PersonBuilder;
import networkbook.testutil.TypicalPersons;

public class PersonTest {

    @Test
    public void asObservableList_modifyList_throwsUnsupportedOperationException() {
        Person person = new PersonBuilder().build();
        assertThrows(UnsupportedOperationException.class, () -> person.getTags().remove(0));
    }

    @Test
    public void isSame() {
        // same object -> returns true
        assertTrue(TypicalPersons.AMY.isSame(TypicalPersons.AMY));

        // null -> returns false
        assertFalse(TypicalPersons.AMY.isSame(null));

        // same name, all other attributes different -> returns true
        Person editedAmy = new PersonBuilder(TypicalPersons.AMY)
                .withPhone(VALID_PHONE_BOB).withEmail(VALID_EMAIL_BOB)
                .withLink(VALID_LINK_BOB).withGraduatingYear(VALID_GRADUATING_YEAR_BOB)
                .withCourse(VALID_COURSE_BOB).withSpecialisation(VALID_SPECIALISATION_BOB)
                .withTags(VALID_TAG_HUSBAND).build();
        assertTrue(TypicalPersons.AMY.isSame(editedAmy));

        // different name, all other attributes same -> returns false
        editedAmy = new PersonBuilder(TypicalPersons.ALICE).withName(VALID_NAME_BOB).build();
        assertFalse(TypicalPersons.ALICE.isSame(editedAmy));

        // name differs in case, all other attributes same -> returns false
        Person editedBob = new PersonBuilder(TypicalPersons.BOB).withName(VALID_NAME_BOB.toLowerCase()).build();
        assertFalse(TypicalPersons.BOB.isSame(editedBob));

        // name has trailing spaces, all other attributes same -> returns false
        String nameWithTrailingSpaces = VALID_NAME_BOB + " ";
        editedBob = new PersonBuilder(TypicalPersons.BOB).withName(nameWithTrailingSpaces).build();
        assertFalse(TypicalPersons.BOB.isSame(editedBob));
    }

    @Test
    public void getValue_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, TypicalPersons.ALICE::getValue);
    }

    @Test
    public void equals() {
        // same values -> returns true
        Person aliceCopy = new PersonBuilder(TypicalPersons.AMY).build();
        assertTrue(TypicalPersons.AMY.equals(aliceCopy));

        // same object -> returns true
        assertTrue(TypicalPersons.AMY.equals(TypicalPersons.AMY));

        // null -> returns false
        assertFalse(TypicalPersons.AMY.equals(null));

        // different type -> returns false
        assertFalse(TypicalPersons.AMY.equals(5));

        // different person -> returns false
        assertFalse(TypicalPersons.AMY.equals(TypicalPersons.BOB));

        // different name -> returns false
        Person editedAmy = new PersonBuilder(TypicalPersons.AMY).withName(VALID_NAME_BOB).build();
        assertFalse(TypicalPersons.AMY.equals(editedAmy));

        // different phone -> returns false
        editedAmy = new PersonBuilder(TypicalPersons.AMY).withPhone(VALID_PHONE_BOB).build();
        assertFalse(TypicalPersons.AMY.equals(editedAmy));

        // different email -> returns false
        editedAmy = new PersonBuilder(TypicalPersons.AMY).withEmail(VALID_EMAIL_BOB).build();
        assertFalse(TypicalPersons.AMY.equals(editedAmy));

        // different link -> returns false
        editedAmy = new PersonBuilder(TypicalPersons.AMY).withLink(VALID_LINK_BOB).build();
        assertFalse(TypicalPersons.AMY.equals(editedAmy));

        // different graduating year -> returns false
        editedAmy = new PersonBuilder(TypicalPersons.AMY).withGraduatingYear(VALID_GRADUATING_YEAR_BOB).build();
        assertFalse(TypicalPersons.AMY.equals(editedAmy));

        // different course -> returns false
        editedAmy = new PersonBuilder(TypicalPersons.AMY).withCourse(VALID_COURSE_BOB).build();
        assertFalse(TypicalPersons.AMY.equals(editedAmy));

        // different specialisation -> returns false
        editedAmy = new PersonBuilder(TypicalPersons.AMY).withSpecialisation(VALID_SPECIALISATION_BOB).build();
        assertFalse(TypicalPersons.AMY.equals(editedAmy));

        // different tags -> returns false
        editedAmy = new PersonBuilder(TypicalPersons.AMY).withTags(VALID_TAG_HUSBAND).build();
        assertFalse(TypicalPersons.AMY.equals(editedAmy));
    }

    @Test
    public void toStringMethod() {
        String expected = Person.class.getCanonicalName() + "{name=" + TypicalPersons.AMY.getName()
                + ", phone=" + TypicalPersons.AMY.getPhone() + ", email=" + TypicalPersons.AMY.getEmails()
                + ", link=" + TypicalPersons.AMY.getLink()
                + ", graduating year=" + TypicalPersons.AMY.getGraduatingYear()
                + ", course=" + TypicalPersons.AMY.getCourse()
                + ", specialisation=" + TypicalPersons.AMY.getSpecialisation()
                + ", tags=" + TypicalPersons.AMY.getTags() + ", priority=" + TypicalPersons.AMY.getPriority().get()
                + "}";
        assertEquals(expected, TypicalPersons.AMY.toString());
    }
}