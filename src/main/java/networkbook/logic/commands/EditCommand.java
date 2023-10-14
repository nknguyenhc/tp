package networkbook.logic.commands;

import static java.util.Objects.requireNonNull;
import static networkbook.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import networkbook.commons.core.index.Index;
import networkbook.commons.util.CollectionUtil;
import networkbook.commons.util.ToStringBuilder;
import networkbook.logic.Messages;
import networkbook.logic.commands.exceptions.CommandException;
import networkbook.logic.parser.CliSyntax;
import networkbook.model.Model;
import networkbook.model.person.Course;
import networkbook.model.person.Email;
import networkbook.model.person.GraduatingYear;
import networkbook.model.person.Link;
import networkbook.model.person.Name;
import networkbook.model.person.Person;
import networkbook.model.person.Phone;
import networkbook.model.person.Priority;
import networkbook.model.person.Specialisation;
import networkbook.model.tag.Tag;
import networkbook.model.util.UniqueList;

/**
 * Edits the details of an existing person in the network book.
 */
public class EditCommand extends Command {

    public static final String COMMAND_WORD = "edit";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the details of the person identified "
            + "by the index number used in the displayed person list. "
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "[" + CliSyntax.PREFIX_NAME + "NAME] "
            + "[" + CliSyntax.PREFIX_PHONE + "PHONE] "
            + "[" + CliSyntax.PREFIX_EMAIL + "EMAIL] "
            + "[" + CliSyntax.PREFIX_LINK + "LINK] "
            + "[" + CliSyntax.PREFIX_GRADUATING_YEAR + "GRADUATING YEAR] "
            + "[" + CliSyntax.PREFIX_COURSE + "COURSE OF STUDY] "
            + "[" + CliSyntax.PREFIX_SPECIALISATION + "SPECIALISATION] "
            + "[" + CliSyntax.PREFIX_TAG + "TAG] "
            + "[" + CliSyntax.PREFIX_PRIORITY + "PRIORITY]...\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + CliSyntax.PREFIX_PHONE + " 91234567 "
            + CliSyntax.PREFIX_EMAIL + " johndoe@example.com";

    public static final String MESSAGE_EDIT_PERSON_SUCCESS = "Edited Person: %1$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_DUPLICATE_PERSON = "This person already exists in the network book.";

    private final Index index;
    private final EditPersonDescriptor editPersonDescriptor;

    /**
     * @param index of the person in the filtered person list to edit
     * @param editPersonDescriptor details to edit the person with
     */
    public EditCommand(Index index, EditPersonDescriptor editPersonDescriptor) {
        requireNonNull(index);
        requireNonNull(editPersonDescriptor);

        this.index = index;
        this.editPersonDescriptor = new EditPersonDescriptor(editPersonDescriptor);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToEdit = lastShownList.get(index.getZeroBased());
        Person editedPerson = createEditedPerson(personToEdit, editPersonDescriptor);

        if (!personToEdit.isSame(editedPerson) && model.hasPerson(editedPerson)) {
            throw new CommandException(MESSAGE_DUPLICATE_PERSON);
        }

        model.setItem(personToEdit, editedPerson);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        return new CommandResult(String.format(MESSAGE_EDIT_PERSON_SUCCESS, Messages.format(editedPerson)));
    }

    /**
     * Creates and returns a {@code Person} with the details of {@code personToEdit}
     * edited with {@code editPersonDescriptor}.
     */
    private static Person createEditedPerson(Person personToEdit, EditPersonDescriptor editPersonDescriptor) {
        assert personToEdit != null;

        Name updatedName = editPersonDescriptor.getName().orElse(personToEdit.getName());
        Phone updatedPhone = editPersonDescriptor.getPhone().orElse(personToEdit.getPhone());
        UniqueList<Email> updatedEmails = editPersonDescriptor.getEmails().orElse(personToEdit.getEmails());
        Link updatedLink = editPersonDescriptor.getLink().orElse(personToEdit.getLink());
        GraduatingYear updatedGraduatingYear = editPersonDescriptor.getGraduatingYear()
                    .orElse(personToEdit.getGraduatingYear());
        Course updatedCourse = editPersonDescriptor.getCourse().orElse(personToEdit.getCourse());
        Specialisation updatedSpecialisation = editPersonDescriptor.getSpecialisation()
                    .orElse(personToEdit.getSpecialisation());
        Set<Tag> updatedTags = editPersonDescriptor.getTags().orElse(personToEdit.getTags());
        Priority updatedPriority = editPersonDescriptor.getPriority().orElse(personToEdit.getPriority()
                                                                     .orElse(null));

        return new Person(updatedName, updatedPhone, updatedEmails, updatedLink, updatedGraduatingYear,
                updatedCourse, updatedSpecialisation, updatedTags, updatedPriority);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EditCommand)) {
            return false;
        }

        EditCommand otherEditCommand = (EditCommand) other;
        return index.equals(otherEditCommand.index)
                && editPersonDescriptor.equals(otherEditCommand.editPersonDescriptor);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("index", index)
                .add("editPersonDescriptor", editPersonDescriptor)
                .toString();
    }

    /**
     * Stores the details to edit the person with. Each non-empty field value will replace the
     * corresponding field value of the person.
     */
    public static class EditPersonDescriptor {
        private Name name;
        private Phone phone;
        private UniqueList<Email> emails;
        private Link link;
        private GraduatingYear graduatingYear;
        private Course course;
        private Specialisation specialisation;
        private Set<Tag> tags;
        private Priority priority;

        public EditPersonDescriptor() {}

        /**
         * Copy constructor.
         * A defensive copy of {@code tags} is used internally.
         */
        public EditPersonDescriptor(EditPersonDescriptor toCopy) {
            setName(toCopy.name);
            setPhone(toCopy.phone);
            setEmails(toCopy.emails);
            setLink(toCopy.link);
            setGraduatingYear(toCopy.graduatingYear);
            setCourse(toCopy.course);
            setSpecialisation(toCopy.specialisation);
            setTags(toCopy.tags);
            setPriority(toCopy.priority);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(name, phone, emails, link, graduatingYear, course,
                        specialisation, tags, priority);
        }

        public void setName(Name name) {
            this.name = name;
        }

        public Optional<Name> getName() {
            return Optional.ofNullable(name);
        }

        public void setPhone(Phone phone) {
            this.phone = phone;
        }

        public Optional<Phone> getPhone() {
            return Optional.ofNullable(phone);
        }

        public void setEmails(UniqueList<Email> emails) {
            this.emails = emails;
        }

        public Optional<UniqueList<Email>> getEmails() {
            return Optional.ofNullable(emails);
        }

        public void setLink(Link link) {
            this.link = link;
        }

        public Optional<Link> getLink() {
            return Optional.ofNullable(link);
        }

        public void setGraduatingYear(GraduatingYear graduatingYear) {
            this.graduatingYear = graduatingYear;
        }

        public Optional<GraduatingYear> getGraduatingYear() {
            return Optional.ofNullable(graduatingYear);
        }

        public void setCourse(Course course) {
            this.course = course;
        }

        public Optional<Course> getCourse() {
            return Optional.ofNullable(course);
        }

        public void setSpecialisation(Specialisation specialisation) {
            this.specialisation = specialisation;
        }

        public Optional<Specialisation> getSpecialisation() {
            return Optional.ofNullable(specialisation);
        }

        /**
         * Sets {@code tags} to this object's {@code tags}.
         * A defensive copy of {@code tags} is used internally.
         */
        public void setTags(Set<Tag> tags) {
            this.tags = (tags != null) ? new HashSet<>(tags) : null;
        }

        /**
         * Returns an unmodifiable tag set, which throws {@code UnsupportedOperationException}
         * if modification is attempted.
         * Returns {@code Optional#empty()} if {@code tags} is null.
         */
        public Optional<Set<Tag>> getTags() {
            return (tags != null) ? Optional.of(Collections.unmodifiableSet(tags)) : Optional.empty();
        }

        public void setPriority(Priority priority) {
            this.priority = priority;
        }

        public Optional<Priority> getPriority() {
            return Optional.ofNullable(this.priority);
        }

        @Override
        public boolean equals(Object other) {
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof EditPersonDescriptor)) {
                return false;
            }

            EditPersonDescriptor otherEditPersonDescriptor = (EditPersonDescriptor) other;
            return Objects.equals(name, otherEditPersonDescriptor.name)
                    && Objects.equals(phone, otherEditPersonDescriptor.phone)
                    && Objects.equals(emails, otherEditPersonDescriptor.emails)
                    && Objects.equals(link, otherEditPersonDescriptor.link)
                    && Objects.equals(graduatingYear, otherEditPersonDescriptor.graduatingYear)
                    && Objects.equals(course, otherEditPersonDescriptor.course)
                    && Objects.equals(specialisation, otherEditPersonDescriptor.specialisation)
                    && Objects.equals(tags, otherEditPersonDescriptor.tags)
                    && Objects.equals(priority, otherEditPersonDescriptor.priority);
        }

        @Override
        public String toString() {
            ToStringBuilder tsb = new ToStringBuilder(this)
                    .add("name", name)
                    .add("phone", phone)
                    .add("email", emails)
                    .add("link", link)
                    .add("graduating year", graduatingYear)
                    .add("course", course)
                    .add("specialisation", specialisation)
                    .add("tags", tags);
            if (priority != null) {
                tsb.add("priority", priority);
            }
            return tsb.toString();
        }
    }
}