package networkbook.testutil;


import networkbook.logic.commands.EditCommand;
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
 * A utility class to help with building EditPersonDescriptor objects.
 */
public class EditPersonDescriptorBuilder {

    private EditCommand.EditPersonDescriptor descriptor;

    public EditPersonDescriptorBuilder() {
        descriptor = new EditCommand.EditPersonDescriptor();
    }

    public EditPersonDescriptorBuilder(EditCommand.EditPersonDescriptor descriptor) {
        this.descriptor = new EditCommand.EditPersonDescriptor(descriptor);
    }

    /**
     * Returns an {@code EditPersonDescriptor} with fields containing {@code person}'s details
     */
    public EditPersonDescriptorBuilder(Person person) {
        descriptor = new EditCommand.EditPersonDescriptor();
        descriptor.setName(person.getName());
        descriptor.setPhone(person.getPhone());
        descriptor.setEmails(person.getEmails());
        descriptor.setLinks(person.getLinks());
        descriptor.setGraduatingYear(person.getGraduatingYear());
        descriptor.setCourse(person.getCourse());
        descriptor.setSpecialisation(person.getSpecialisation());
        descriptor.setTags(person.getTags());
        person.getPriority().ifPresent((Priority p) -> descriptor.setPriority(p));
    }

    /**
     * Sets the {@code Name} of the {@code EditPersonDescriptor} that we are building.
     */
    public EditPersonDescriptorBuilder withName(String name) {
        descriptor.setName(new Name(name));
        return this;
    }

    /**
     * Sets the {@code Phone} of the {@code EditPersonDescriptor} that we are building.
     */
    public EditPersonDescriptorBuilder withPhone(String phone) {
        descriptor.setPhone(new Phone(phone));
        return this;
    }

    /**
     * Sets the {@code Email} of the {@code EditPersonDescriptor} that we are building.
     */
    public EditPersonDescriptorBuilder withEmail(String email) {
        descriptor.addEmail(new Email(email));
        return this;
    }

    /**
     * Sets the {@code Link} of the {@code EditPersonDescriptor} that we are building.
     */
    public EditPersonDescriptorBuilder withLink(String link) {
        descriptor.addLink(new Link(link));
        return this;
    }

    /**
     * Sets the {@code GraduatingYear} of the {@code EditPersonDescriptor} that we are building.
     */
    public EditPersonDescriptorBuilder withGraduatingYear(String graduatingYear) {
        descriptor.setGraduatingYear(new GraduatingYear(graduatingYear));
        return this;
    }

    /**
     * Sets the {@code Course} of the {@code EditPersonDescriptor} that we are building.
     */
    public EditPersonDescriptorBuilder withCourse(String course) {
        descriptor.setCourse(new Course(course));
        return this;
    }

    /**
     * Sets the {@code Specialisation} of the {@code EditPersonDescriptor} that we are building.
     */
    public EditPersonDescriptorBuilder withSpecialisation(String specialisation) {
        descriptor.setSpecialisation(new Specialisation(specialisation));
        return this;
    }

    /**
     * Parses the {@code tags} into a {@code Set<Tag>} and set it to the {@code EditPersonDescriptor}
     * that we are building.
     */
    public EditPersonDescriptorBuilder withTags(String... tags) {
        descriptor.setTags(new UniqueList<>());
        for (String tag : tags) {
            descriptor.addTag(new Tag(tag));
        }
        return this;
    }

    /**
     * Sets the {@code Priority} of the {@code EditPersonDescriptor} that we are building.
     */
    public EditPersonDescriptorBuilder withPriority(String priority) {
        descriptor.setPriority(new Priority(priority));
        return this;
    }

    public EditCommand.EditPersonDescriptor build() {
        return descriptor;
    }
}
